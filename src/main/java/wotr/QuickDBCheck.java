package wotr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QuickDBCheck {
    public static void main(String[] args) {
        try {
            Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            // Check if table exists and has data
            String sql = "SELECT COUNT(*) as count FROM game_pieces";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("✓ game_pieces table has " + count + " pieces");
                
                if (count == 0) {
                    System.out.println("⚠ WARNING: Table is empty! Initialization may have failed.");
                }
            }
            
            // Check piece types
            sql = "SELECT piece_type, COUNT(*) as count FROM game_pieces GROUP BY piece_type LIMIT 5";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            System.out.println("\nSample piece types:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("piece_type") + ": " + rs.getInt("count"));
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("Error checking database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
