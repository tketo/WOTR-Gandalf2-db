package wotr.tools;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import wotr.database.DatabaseManager;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AdjacencyImporter - Import region adjacency data from metadata.json
 * 
 * Reads the connections array from metadata.json and populates
 * the region_adjacency table with all game map connections.
 */
public class AdjacencyImporter {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Adjacency Data Importer ===\n");
        
        try {
            importAdjacencyData();
            System.out.println("\n=== Import Complete! ===");
            
        } catch (Exception e) {
            System.err.println("\n=== Import Failed ===");
            e.printStackTrace();
        }
    }
    
    private static void importAdjacencyData() throws Exception {
        // Read metadata.json
        System.out.println("Reading metadata.json...");
        Gson gson = new Gson();
        FileReader reader = new FileReader("data/metadata.json");
        JsonObject metadata = gson.fromJson(reader, JsonObject.class);
        reader.close();
        
        JsonArray connections = metadata.getAsJsonArray("connections");
        System.out.println("Found " + connections.size() + " connections");
        
        // Connect to database
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection conn = dbManager.getConnection();
        
        // First, populate nations if empty
        PreparedStatement checkNations = conn.prepareStatement("SELECT COUNT(*) FROM nations");
        java.sql.ResultSet rsNations = checkNations.executeQuery();
        int nationCount = 0;
        if (rsNations.next()) {
            nationCount = rsNations.getInt(1);
        }
        rsNations.close();
        checkNations.close();
        
        if (nationCount == 0) {
            System.out.println("\nPopulating nations table first...");
            JsonArray nations = metadata.getAsJsonArray("nations");
            PreparedStatement insertNation = conn.prepareStatement(
                "INSERT INTO nations (id, name) VALUES (?, ?)"
            );
            
            for (int i = 0; i < nations.size(); i++) {
                JsonObject nation = nations.get(i).getAsJsonObject();
                int id = nation.get("id").getAsInt();
                String name = nation.get("name").getAsString();
                
                insertNation.setInt(1, id);
                insertNation.setString(2, name);
                insertNation.executeUpdate();
            }
            insertNation.close();
            System.out.println("✓ Populated " + nations.size() + " nations");
        } else {
            System.out.println("\nNations table already has " + nationCount + " entries");
        }
        
        // Next, populate regions if empty
        PreparedStatement checkRegions = conn.prepareStatement("SELECT COUNT(*) FROM regions");
        java.sql.ResultSet rsCheck = checkRegions.executeQuery();
        int regionCount = 0;
        if (rsCheck.next()) {
            regionCount = rsCheck.getInt(1);
        }
        rsCheck.close();
        checkRegions.close();
        
        if (regionCount == 0) {
            System.out.println("\nPopulating regions table first...");
            JsonArray regions = metadata.getAsJsonArray("regions");
            PreparedStatement insertRegion = conn.prepareStatement(
                "INSERT INTO regions (id, name, nation_id) VALUES (?, ?, ?)"
            );
            
            for (int i = 0; i < regions.size(); i++) {
                JsonObject region = regions.get(i).getAsJsonObject();
                String id = region.get("id").getAsString();
                String name = region.get("name").getAsString();
                String nationStr = region.has("nation") ? region.get("nation").getAsString() : "0";
                int nationId = nationStr.isEmpty() ? 0 : Integer.parseInt(nationStr);
                
                insertRegion.setString(1, id);
                insertRegion.setString(2, name);
                insertRegion.setInt(3, nationId);
                insertRegion.executeUpdate();
            }
            insertRegion.close();
            System.out.println("✓ Populated " + regions.size() + " regions");
        } else {
            System.out.println("\nRegions table already has " + regionCount + " entries");
        }
        
        // Clear existing adjacency data (except sample)
        System.out.println("\nClearing old adjacency data...");
        PreparedStatement clearStmt = conn.prepareStatement(
            "DELETE FROM region_adjacency WHERE id > 4"
        );
        int deleted = clearStmt.executeUpdate();
        System.out.println("Cleared " + deleted + " old entries");
        clearStmt.close();
        
        // Prepare insert statement
        String insertSQL = "INSERT OR IGNORE INTO region_adjacency " +
                          "(region_a, region_b, terrain_type, movement_cost) " +
                          "VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
        
        // Import each connection
        int imported = 0;
        for (int i = 0; i < connections.size(); i++) {
            JsonArray pair = connections.get(i).getAsJsonArray();
            
            if (pair.size() != 2) {
                System.err.println("Warning: Invalid connection at index " + i);
                continue;
            }
            
            JsonObject regionA = pair.get(0).getAsJsonObject();
            JsonObject regionB = pair.get(1).getAsJsonObject();
            
            String idA = regionA.get("id").getAsString();
            String idB = regionB.get("id").getAsString();
            
            // Insert the connection (bidirectional handled by view)
            insertStmt.setString(1, idA);
            insertStmt.setString(2, idB);
            insertStmt.setString(3, "normal"); // Default terrain type
            insertStmt.setInt(4, 1); // Default movement cost
            
            try {
                insertStmt.executeUpdate();
                imported++;
                
                if ((i + 1) % 50 == 0) {
                    System.out.println("  Imported " + (i + 1) + " / " + connections.size());
                }
            } catch (SQLException e) {
                // Ignore duplicates
                if (!e.getMessage().contains("UNIQUE constraint")) {
                    throw e;
                }
            }
        }
        
        insertStmt.close();
        
        System.out.println("\n✓ Successfully imported " + imported + " adjacency connections");
        
        // Verify
        PreparedStatement countStmt = conn.prepareStatement(
            "SELECT COUNT(*) FROM region_adjacency"
        );
        java.sql.ResultSet rs = countStmt.executeQuery();
        if (rs.next()) {
            int total = rs.getInt(1);
            System.out.println("✓ Total adjacency entries in database: " + total);
        }
        rs.close();
        countStmt.close();
        
        // Sample query
        System.out.println("\n--- Sample Adjacencies ---");
        PreparedStatement sampleStmt = conn.prepareStatement(
            "SELECT region_b FROM v_region_adjacency WHERE region_a = '53' LIMIT 5"
        );
        rs = sampleStmt.executeQuery();
        System.out.println("Regions adjacent to Minas Tirith (53):");
        while (rs.next()) {
            System.out.println("  → Region " + rs.getString(1));
        }
        rs.close();
        sampleStmt.close();
    }
}
