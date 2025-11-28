package wotr.database;

import java.sql.*;

public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Database Setup Test ===\n");
        
        try {
            // Test 1: Initialize DatabaseManager
            System.out.println("Test 1: Initializing DatabaseManager...");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            System.out.println("✓ DatabaseManager initialized\n");
            
            // Test 2: Check if tables exist
            System.out.println("Test 2: Checking tables...");
            DatabaseMetaData meta = conn.getMetaData();
            String[] tables = {
                "nations", "regions", "settlements", "characters",
                "character_abilities", "character_effects", 
                "combat_cards", "event_cards", "army_setup", "deployed_units"
            };
            
            for (String tableName : tables) {
                ResultSet rs = meta.getTables(null, null, tableName, null);
                if (rs.next()) {
                    System.out.println("  ✓ Table exists: " + tableName);
                } else {
                    System.out.println("  ✗ Table missing: " + tableName);
                }
                rs.close();
            }
            System.out.println();
            
            // Test 3: Check schema version
            System.out.println("Test 3: Checking schema version...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT version, description, applied_date FROM schema_version"
            );
            while (rs.next()) {
                System.out.println("  Version: " + rs.getInt("version"));
                System.out.println("  Description: " + rs.getString("description"));
                System.out.println("  Date: " + rs.getString("applied_date"));
            }
            rs.close();
            stmt.close();
            System.out.println();
            
            // Test 4: Test insert and query
            System.out.println("Test 4: Testing basic operations...");
            testBasicOperations(conn);
            System.out.println();
            
            System.out.println("=== All tests passed! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed!");
            e.printStackTrace();
        }
    }
    
    private static void testBasicOperations(Connection conn) throws SQLException {
        // Insert test nation
        PreparedStatement insertStmt = conn.prepareStatement(
            "INSERT INTO nations (id, name) VALUES (?, ?)"
        );
        insertStmt.setInt(1, 999);
        insertStmt.setString(2, "Test Nation");
        insertStmt.executeUpdate();
        insertStmt.close();
        System.out.println("  ✓ Inserted test nation");
        
        // Query test nation
        Statement selectStmt = conn.createStatement();
        ResultSet rs = selectStmt.executeQuery(
            "SELECT * FROM nations WHERE id = 999"
        );
        if (rs.next()) {
            System.out.println("  ✓ Retrieved: " + rs.getString("name"));
        }
        rs.close();
        selectStmt.close();
        
        // Delete test nation
        Statement deleteStmt = conn.createStatement();
        deleteStmt.execute("DELETE FROM nations WHERE id = 999");
        deleteStmt.close();
        System.out.println("  ✓ Deleted test nation");
    }
}
