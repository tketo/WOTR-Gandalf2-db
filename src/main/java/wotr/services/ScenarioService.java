package wotr.services;

import wotr.database.DatabaseManager;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioService - Load and apply game scenarios
 * 
 * Replaces hardcoded newWOTR(), newBase(), newGondor(), etc.
 * with database-driven scenario loading
 */
public class ScenarioService {
    private Connection connection;
    private GameStateService gameState;
    
    public ScenarioService(GameStateService gameState) {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        this.connection = dbManager.getConnection();
        this.gameState = gameState;
    }
    
    /**
     * Get scenario metadata
     */
    public ScenarioInfo getScenario(String scenarioId) throws SQLException {
        String sql = "SELECT * FROM scenarios WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, scenarioId);
        ResultSet rs = pstmt.executeQuery();
        
        ScenarioInfo info = null;
        if (rs.next()) {
            info = new ScenarioInfo();
            info.id = rs.getString("id");
            info.name = rs.getString("name");
            info.boardType = rs.getString("board_type");
            info.variantType = rs.getString("variant_type");
            info.boardImage = rs.getString("board_image");
            info.overlayImage = rs.getString("overlay_image");
            info.description = rs.getString("description");
        }
        
        rs.close();
        pstmt.close();
        return info;
    }
    
    /**
     * Load a scenario into a new game
     * 
     * This replaces all the hardcoded newWOTR(), newBase(), etc. methods
     */
    public String loadScenario(String scenarioId, String startingPlayer) throws SQLException {
        // Get scenario info
        ScenarioInfo scenario = getScenario(scenarioId);
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario not found: " + scenarioId);
        }
        
        System.out.println("Loading scenario: " + scenario.name);
        
        // Create new game
        String gameId = gameState.createNewGame(startingPlayer);
        
        // Load initial unit placements
        loadScenarioUnits(gameId, scenarioId);
        
        // Load initial character positions
        loadScenarioCharacters(gameId, scenarioId);
        
        // Load initial region control
        loadScenarioControl(gameId, scenarioId);
        
        // Load fellowship state
        loadScenarioFellowship(gameId, scenarioId);
        
        System.out.println("Scenario loaded successfully: " + gameId);
        return gameId;
    }
    
    private void loadScenarioUnits(String gameId, String scenarioId) throws SQLException {
        String sql = "SELECT * FROM scenario_units WHERE scenario_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, scenarioId);
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            String regionId = rs.getString("region_id");
            int nationId = rs.getInt("nation_id");
            String unitType = rs.getString("unit_type");
            int unitCount = rs.getInt("count");
            
            gameState.addUnits(regionId, nationId, unitType, unitCount);
            count++;
        }
        
        rs.close();
        pstmt.close();
        System.out.println("  - Loaded " + count + " unit placements");
    }
    
    private void loadScenarioCharacters(String gameId, String scenarioId) throws SQLException {
        String sql = "SELECT * FROM scenario_characters WHERE scenario_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, scenarioId);
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            String characterId = rs.getString("character_id");
            String regionId = rs.getString("region_id");
            
            gameState.moveCharacter(characterId, regionId);
            count++;
        }
        
        rs.close();
        pstmt.close();
        System.out.println("  - Loaded " + count + " character positions");
    }
    
    private void loadScenarioControl(String gameId, String scenarioId) throws SQLException {
        String sql = "SELECT * FROM scenario_control WHERE scenario_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, scenarioId);
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            String regionId = rs.getString("region_id");
            String controller = rs.getString("controlled_by");
            
            gameState.setRegionControl(regionId, controller);
            count++;
        }
        
        rs.close();
        pstmt.close();
        System.out.println("  - Loaded " + count + " region controls");
    }
    
    private void loadScenarioFellowship(String gameId, String scenarioId) throws SQLException {
        // TODO: Implement when fellowship initialization is added to GameStateService
        System.out.println("  - Fellowship state (TODO)");
    }
    
    /**
     * List all available scenarios
     */
    public Map<String, ScenarioInfo> getAllScenarios() throws SQLException {
        Map<String, ScenarioInfo> scenarios = new HashMap<>();
        String sql = "SELECT * FROM scenarios ORDER BY name";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            ScenarioInfo info = new ScenarioInfo();
            info.id = rs.getString("id");
            info.name = rs.getString("name");
            info.boardType = rs.getString("board_type");
            info.variantType = rs.getString("variant_type");
            info.boardImage = rs.getString("board_image");
            info.overlayImage = rs.getString("overlay_image");
            info.description = rs.getString("description");
            scenarios.put(info.id, info);
        }
        
        rs.close();
        stmt.close();
        return scenarios;
    }
    
    // ===== INNER CLASS =====
    
    public static class ScenarioInfo {
        public String id;
        public String name;
        public String boardType;
        public String variantType;
        public String boardImage;
        public String overlayImage;
        public String description;
        
        @Override
        public String toString() {
            return name + " (" + boardType + "/" + variantType + ")";
        }
    }
}
