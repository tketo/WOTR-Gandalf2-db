package wotr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Manual test for piece movement database synchronization
 * Run this directly with: java -cp target/classes wotr.ManualPieceSyncTest
 */
public class ManualPieceSyncTest {
    
    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("  Piece Movement Sync Manual Test");
        System.out.println("========================================\n");
        
        try {
            // Create game (will initialize database)
            System.out.println("Step 1: Initializing game and database...");
            Game game = new Game();
            System.out.println("✓ Game initialized\n");
            
            // Wait for initialization to complete
            Thread.sleep(3000);
            
            // Get database connection
            Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            // Test 1: Check if table exists and has data
            System.out.println("Test 1: Checking game_pieces table...");
            testTableInitialized(conn);
            
            // Test 2: Check piece types
            System.out.println("\nTest 2: Checking piece types...");
            testPieceTypes(conn);
            
            // Test 3: Check nation mapping
            System.out.println("\nTest 3: Checking nation mapping...");
            testNations(conn);
            
            // Test 4: Test actual movement sync
            System.out.println("\nTest 4: Testing piece movement sync...");
            testMovementSync(game, conn);
            
            // Test 5: Check fellowship pieces
            System.out.println("\nTest 5: Checking fellowship pieces...");
            testFellowshipPieces(conn);
            
            System.out.println("\n========================================");
            System.out.println("  All Tests Complete!");
            System.out.println("========================================\n");
            
        } catch (Exception e) {
            System.err.println("\n❌ Test failed with error:");
            e.printStackTrace();
        }
    }
    
    private static void testTableInitialized(Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) as count FROM game_pieces";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            int count = rs.getInt("count");
            System.out.println("  Pieces in database: " + count);
            
            if (count > 0) {
                System.out.println("  ✓ Table initialized successfully");
            } else {
                System.out.println("  ❌ Table is empty!");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    private static void testPieceTypes(Connection conn) throws Exception {
        String sql = "SELECT piece_type, COUNT(*) as count FROM game_pieces GROUP BY piece_type ORDER BY count DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        System.out.println("  Piece type breakdown:");
        int totalTypes = 0;
        while (rs.next()) {
            String type = rs.getString("piece_type");
            int count = rs.getInt("count");
            System.out.println("    - " + String.format("%-20s", type) + ": " + count);
            totalTypes++;
        }
        
        System.out.println("  ✓ Found " + totalTypes + " different piece types");
        
        rs.close();
        stmt.close();
    }
    
    private static void testNations(Connection conn) throws Exception {
        String sql = "SELECT nation_id, COUNT(*) as count FROM game_pieces " +
                    "WHERE nation_id IS NOT NULL GROUP BY nation_id ORDER BY nation_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        String[] nationNames = {"", "Dwarves", "Elves", "Gondor", "North", "Rohan", "Isengard", "Sauron", "S&E"};
        
        System.out.println("  Nation breakdown:");
        int totalNations = 0;
        while (rs.next()) {
            int nationId = rs.getInt("nation_id");
            int count = rs.getInt("count");
            String name = (nationId >= 0 && nationId < nationNames.length) ? nationNames[nationId] : "Unknown";
            System.out.println("    - Nation " + nationId + " (" + String.format("%-10s", name) + "): " + count + " pieces");
            totalNations++;
        }
        
        System.out.println("  ✓ Found " + totalNations + " nations with pieces");
        
        rs.close();
        stmt.close();
    }
    
    private static void testMovementSync(Game game, Connection conn) throws Exception {
        // Find a piece to move
        String findSql = "SELECT piece_id, area_id, piece_type FROM game_pieces " +
                        "WHERE piece_type = 'regular' AND area_id >= 0 LIMIT 1";
        PreparedStatement findStmt = conn.prepareStatement(findSql);
        ResultSet rs = findStmt.executeQuery();
        
        if (!rs.next()) {
            System.out.println("  ⚠ No suitable piece found for movement test");
            rs.close();
            findStmt.close();
            return;
        }
        
        int pieceId = rs.getInt("piece_id");
        int originalArea = rs.getInt("area_id");
        String type = rs.getString("piece_type");
        
        rs.close();
        findStmt.close();
        
        System.out.println("  Selected piece:");
        System.out.println("    - Piece ID: " + pieceId);
        System.out.println("    - Type: " + type);
        System.out.println("    - Current area: " + originalArea);
        
        // Get the actual GamePiece and move it
        if (pieceId >= 0 && pieceId < game.bits.length && game.bits[pieceId] != null) {
            GamePiece piece = game.bits[pieceId];
            
            // Find a different area
            int targetArea = (originalArea + 5) % Game.areas.length;
            while (targetArea == originalArea || Game.areas[targetArea] == null) {
                targetArea = (targetArea + 1) % Game.areas.length;
            }
            
            System.out.println("  Moving to area: " + targetArea);
            
            // Perform the move
            piece.moveTo(Game.areas[targetArea]);
            
            // Wait for sync
            Thread.sleep(1000);
            
            // Check database
            String checkSql = "SELECT area_id FROM game_pieces WHERE piece_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, pieceId);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next()) {
                int dbArea = checkRs.getInt("area_id");
                System.out.println("  Database shows area: " + dbArea);
                
                if (dbArea == targetArea) {
                    System.out.println("  ✓ Movement synced correctly!");
                } else {
                    System.out.println("  ❌ Sync failed! Expected " + targetArea + ", got " + dbArea);
                }
            } else {
                System.out.println("  ❌ Piece not found in database after move!");
            }
            
            checkRs.close();
            checkStmt.close();
        } else {
            System.out.println("  ⚠ Piece not found in bits[] array");
        }
    }
    
    private static void testFellowshipPieces(Connection conn) throws Exception {
        String sql = "SELECT piece_type, piece_id, area_id FROM game_pieces " +
                    "WHERE piece_type IN ('fellowship', 'fellowship_counter', 'corruption_counter') " +
                    "ORDER BY piece_type";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        System.out.println("  Fellowship system pieces:");
        int count = 0;
        while (rs.next()) {
            String type = rs.getString("piece_type");
            int pieceId = rs.getInt("piece_id");
            int areaId = rs.getInt("area_id");
            System.out.println("    - " + String.format("%-20s", type) + " (ID: " + pieceId + ", Area: " + areaId + ")");
            count++;
        }
        
        if (count > 0) {
            System.out.println("  ✓ Found " + count + " fellowship pieces");
        } else {
            System.out.println("  ⚠ No fellowship pieces found");
        }
        
        rs.close();
        stmt.close();
    }
}
