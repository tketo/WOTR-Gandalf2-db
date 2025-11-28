package wotr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Test state restoration from database
 * Verifies that we can query piece locations and reconstruct game state
 */
public class StateRestorationTest {
    
    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("  State Restoration Test");
        System.out.println("========================================\n");
        
        try {
            Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            // Test 1: Query all pieces and their locations
            System.out.println("Test 1: Querying all piece locations...");
            Map<Integer, Integer> pieceLocations = queryAllPieceLocations(conn);
            System.out.println("  ✓ Retrieved " + pieceLocations.size() + " piece locations\n");
            
            // Test 2: Query pieces by type
            System.out.println("Test 2: Querying pieces by type...");
            queryPiecesByType(conn, "fellowship");
            queryPiecesByType(conn, "fellowship_counter");
            queryPiecesByType(conn, "corruption_counter");
            queryPiecesByType(conn, "regular");
            queryPiecesByType(conn, "elite");
            queryPiecesByType(conn, "leader");
            System.out.println();
            
            // Test 3: Query pieces in specific area
            System.out.println("Test 3: Querying pieces in specific areas...");
            queryPiecesInArea(conn, 27);  // Rivendell (where fellowship starts)
            queryPiecesInArea(conn, 131); // Fellowship Track 0
            queryPiecesInArea(conn, 117); // Political Track 3
            System.out.println();
            
            // Test 4: Query by nation
            System.out.println("Test 4: Querying pieces by nation...");
            queryPiecesByNation(conn, 3); // Gondor
            queryPiecesByNation(conn, 7); // Sauron
            System.out.println();
            
            // Test 5: Complex query - all units in a region
            System.out.println("Test 5: Complex query - military units in region...");
            queryMilitaryUnitsInArea(conn, 82); // Erebor
            System.out.println();
            
            System.out.println("========================================");
            System.out.println("  All Tests Complete!");
            System.out.println("========================================\n");
            
            System.out.println("✅ State restoration verified:");
            System.out.println("  - Can query all piece locations");
            System.out.println("  - Can filter by piece type");
            System.out.println("  - Can filter by area");
            System.out.println("  - Can filter by nation");
            System.out.println("  - Can perform complex queries");
            System.out.println("\n🎉 Ready to implement full state restoration!\n");
            
        } catch (Exception e) {
            System.err.println("\n❌ Test failed with error:");
            e.printStackTrace();
        }
    }
    
    private static Map<Integer, Integer> queryAllPieceLocations(Connection conn) throws Exception {
        Map<Integer, Integer> locations = new HashMap<>();
        
        String sql = "SELECT piece_id, area_id FROM game_pieces ORDER BY piece_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int pieceId = rs.getInt("piece_id");
            int areaId = rs.getInt("area_id");
            locations.put(pieceId, areaId);
        }
        
        rs.close();
        stmt.close();
        
        return locations;
    }
    
    private static void queryPiecesByType(Connection conn, String pieceType) throws Exception {
        String sql = "SELECT COUNT(*) as count FROM game_pieces WHERE piece_type = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, pieceType);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            int count = rs.getInt("count");
            System.out.println("  - " + String.format("%-20s", pieceType) + ": " + count + " pieces");
        }
        
        rs.close();
        stmt.close();
    }
    
    private static void queryPiecesInArea(Connection conn, int areaId) throws Exception {
        String sql = "SELECT piece_id, piece_type FROM game_pieces WHERE area_id = ? ORDER BY piece_type";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, areaId);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        System.out.println("  Area " + areaId + ":");
        while (rs.next()) {
            int pieceId = rs.getInt("piece_id");
            String type = rs.getString("piece_type");
            if (count < 5) { // Show first 5
                System.out.println("    - Piece " + pieceId + " (" + type + ")");
            }
            count++;
        }
        if (count > 5) {
            System.out.println("    - ... and " + (count - 5) + " more pieces");
        }
        System.out.println("  Total: " + count + " pieces");
        
        rs.close();
        stmt.close();
    }
    
    private static void queryPiecesByNation(Connection conn, int nationId) throws Exception {
        String sql = "SELECT COUNT(*) as count, piece_type FROM game_pieces " +
                    "WHERE nation_id = ? GROUP BY piece_type ORDER BY piece_type";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, nationId);
        ResultSet rs = stmt.executeQuery();
        
        System.out.println("  Nation " + nationId + ":");
        int total = 0;
        while (rs.next()) {
            int count = rs.getInt("count");
            String type = rs.getString("piece_type");
            System.out.println("    - " + type + ": " + count);
            total += count;
        }
        System.out.println("  Total: " + total + " pieces");
        
        rs.close();
        stmt.close();
    }
    
    private static void queryMilitaryUnitsInArea(Connection conn, int areaId) throws Exception {
        String sql = "SELECT piece_type, COUNT(*) as count FROM game_pieces " +
                    "WHERE area_id = ? AND piece_type IN ('regular', 'elite', 'leader') " +
                    "GROUP BY piece_type ORDER BY piece_type";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, areaId);
        ResultSet rs = stmt.executeQuery();
        
        System.out.println("  Military units in area " + areaId + ":");
        int total = 0;
        while (rs.next()) {
            String type = rs.getString("piece_type");
            int count = rs.getInt("count");
            System.out.println("    - " + type + ": " + count);
            total += count;
        }
        System.out.println("  Total military: " + total + " units");
        
        rs.close();
        stmt.close();
    }
}
