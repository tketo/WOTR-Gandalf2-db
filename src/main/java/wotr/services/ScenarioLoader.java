package wotr.services;

import wotr.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ScenarioLoader - Database-driven scenario initialization
 * 
 * Provides piece setup data from scenario_setup table.
 * Game.java uses this data to create pieces with package-private constructors.
 * 
 * Phase 2 of Database Architecture Evolution:
 * - Load piece data from scenario_setup table
 * - Falls back to bitInit() if table is empty or missing
 * - Supports multiple scenarios (base, warriors, lords, custom)
 */
public class ScenarioLoader {
    private Connection connection;
    
    public ScenarioLoader() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        this.connection = dbManager.getConnection();
    }
    
    /**
     * Check if scenario data exists in database
     */
    public boolean hasScenarioData(String scenarioId) {
        try {
            String sql = "SELECT COUNT(*) as count FROM scenario_setup WHERE scenario_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, scenarioId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                pstmt.close();
                return count > 0;
            }
            
            rs.close();
            pstmt.close();
            return false;
        } catch (SQLException e) {
            System.err.println("[ScenarioLoader] Error checking scenario data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load scenario piece setup data
     * Returns list of PieceSetup objects for Game.java to create pieces
     */
    public List<PieceSetup> loadScenarioData(String scenarioId) {
        List<PieceSetup> setups = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM scenario_setup WHERE scenario_id = ? ORDER BY piece_id";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, scenarioId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PieceSetup setup = new PieceSetup();
                setup.pieceId = rs.getInt("piece_id");
                setup.pieceClass = rs.getString("piece_class");
                setup.areaId = rs.getInt("initial_area_id");
                setup.areaName = rs.getString("initial_area_name");
                setup.faction = rs.getString("faction");
                
                // Card-specific fields
                setup.smallImage = rs.getString("small_image");
                setup.bigImage = rs.getString("big_image");
                setup.cardName = rs.getString("card_name");
                
                // BattleCard-specific fields
                setup.smallBackImage = rs.getString("small_back_image");
                setup.bigBackImage = rs.getString("big_back_image");
                setup.cardType = rs.getString("card_type");
                
                // Unit-specific fields
                setup.nation = rs.getString("nation");
                setup.unitType = rs.getString("unit_type");
                
                setups.add(setup);
            }
            
            rs.close();
            pstmt.close();
            
            System.out.println("[ScenarioLoader] Loaded " + setups.size() + " piece setups for scenario: " + scenarioId);
            return setups;
            
        } catch (SQLException e) {
            System.err.println("[ScenarioLoader] Error loading scenario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Data class for piece setup information
     * Maps directly to scenario_setup table columns
     */
    public static class PieceSetup {
        // Core fields
        public int pieceId;              // bits[] array index
        public String pieceClass;        // Class name: FreeStrategyCard, UnitGondorRegular, etc.
        public int areaId;               // areas[] array index
        public String areaName;          // Human-readable area name
        public String faction;           // 'free_peoples' or 'shadow'
        
        // Card-specific fields (null for non-cards)
        public String smallImage;        // Path to small card image
        public String bigImage;          // Path to big card image
        public String cardName;          // Card name
        
        // BattleCard-specific fields (null for non-BattleCards)
        public String smallBackImage;    // Path to small back card image (BattleCards only)
        public String bigBackImage;      // Path to big back card image (BattleCards only)
        public String cardType;          // Card type string (BattleCards only)
        
        // Unit-specific fields (null for cards)
        public String nation;            // Unit nation
        public String unitType;          // 'regular', 'elite', 'leader'
    }
}
