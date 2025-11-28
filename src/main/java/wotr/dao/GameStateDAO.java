package wotr.dao;

import wotr.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameStateDAO - Query the "game board" state
 * 
 * This DAO treats the database like a physical game board.
 * Methods answer questions like "Where is Strider?" or "What armies are in Minas Tirith?"
 */
public class GameStateDAO {
    private Connection connection;
    
    public GameStateDAO(Connection connection) {
        this.connection = connection;
    }
    
    // ===== CHARACTER QUERIES (Where are the pieces?) =====
    
    /**
     * Get a character's current location
     * Example: getCharacterLocation("game1", "strider") -> "edoras"
     */
    public String getCharacterLocation(String gameId, String characterId) throws SQLException {
        String sql = "SELECT region_id FROM character_positions WHERE game_id = ? AND character_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, characterId);
        ResultSet rs = pstmt.executeQuery();
        
        String regionId = null;
        if (rs.next()) {
            regionId = rs.getString("region_id");
        }
        
        rs.close();
        pstmt.close();
        return regionId;
    }
    
    /**
     * Check if a character is in play
     */
    public boolean isCharacterInPlay(String gameId, String characterId) throws SQLException {
        String sql = "SELECT status FROM character_positions WHERE game_id = ? AND character_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, characterId);
        ResultSet rs = pstmt.executeQuery();
        
        boolean inPlay = false;
        if (rs.next()) {
            inPlay = "in_play".equals(rs.getString("status"));
        }
        
        rs.close();
        pstmt.close();
        return inPlay;
    }
    
    /**
     * Move a character to a new region
     */
    public void updateCharacterPosition(String gameId, String characterId, String newRegionId) throws SQLException {
        String sql = "UPDATE character_positions SET region_id = ?, status = 'in_play' " +
                    "WHERE game_id = ? AND character_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newRegionId);
        pstmt.setString(2, gameId);
        pstmt.setString(3, characterId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Get all characters in a specific region
     */
    public List<String> getCharactersInRegion(String gameId, String regionId) throws SQLException {
        List<String> characters = new ArrayList<String>();
        String sql = "SELECT character_id FROM character_positions " +
                    "WHERE game_id = ? AND region_id = ? AND status = 'in_play'";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, regionId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            characters.add(rs.getString("character_id"));
        }
        
        rs.close();
        pstmt.close();
        return characters;
    }
    
    // ===== ARMY QUERIES (What units are where?) =====
    
    /**
     * Get all army units in a region
     * Example: getArmyUnits("game1", "minas_tirith") -> [3 Gondor regulars, 1 Gondor elite, 1 leader]
     * 
     * @deprecated As of Nov 16, 2024. Use Game.getPiecesInArea(areaId) instead.
     *             The army_units table is deprecated in favor of the unified game_pieces table.
     */
    @Deprecated
    public List<ArmyUnit> getArmyUnits(String gameId, String regionId) throws SQLException {
        List<ArmyUnit> units = new ArrayList<ArmyUnit>();
        String sql = "SELECT * FROM army_units WHERE game_id = ? AND region_id = ? AND count > 0";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, regionId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            ArmyUnit unit = new ArmyUnit();
            unit.setGameId(rs.getString("game_id"));
            unit.setRegionId(rs.getString("region_id"));
            unit.setNationId(rs.getInt("nation_id"));
            unit.setUnitType(rs.getString("unit_type"));
            unit.setCount(rs.getInt("count"));
            units.add(unit);
        }
        
        rs.close();
        pstmt.close();
        return units;
    }
    
    /**
     * Add units to a region
     * 
     * @deprecated As of Nov 16, 2024. Piece locations are now synced automatically via Game.syncPieceLocation().
     *             The army_units table is deprecated in favor of the unified game_pieces table.
     */
    @Deprecated
    public void addUnits(String gameId, String regionId, int nationId, String unitType, int count) throws SQLException {
        String sql = "INSERT INTO army_units (game_id, region_id, nation_id, unit_type, count) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(game_id, region_id, nation_id, unit_type) " +
                    "DO UPDATE SET count = count + ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, regionId);
        pstmt.setInt(3, nationId);
        pstmt.setString(4, unitType);
        pstmt.setInt(5, count);
        pstmt.setInt(6, count);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Remove units from a region
     * 
     * @deprecated As of Nov 16, 2024. Piece locations are now synced automatically via Game.syncPieceLocation().
     *             The army_units table is deprecated in favor of the unified game_pieces table.
     */
    @Deprecated
    public void removeUnits(String gameId, String regionId, int nationId, String unitType, int count) throws SQLException {
        String sql = "UPDATE army_units SET count = count - ? " +
                    "WHERE game_id = ? AND region_id = ? AND nation_id = ? AND unit_type = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, count);
        pstmt.setString(2, gameId);
        pstmt.setString(3, regionId);
        pstmt.setInt(4, nationId);
        pstmt.setString(5, unitType);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // ===== REGION CONTROL =====
    
    /**
     * Get who controls a region
     * Returns: "free_peoples", "shadow", or "neutral"
     */
    public String getRegionControl(String gameId, String regionId) throws SQLException {
        String sql = "SELECT controlled_by FROM region_control WHERE game_id = ? AND region_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, regionId);
        ResultSet rs = pstmt.executeQuery();
        
        String control = "neutral";
        if (rs.next()) {
            control = rs.getString("controlled_by");
        }
        
        rs.close();
        pstmt.close();
        return control;
    }
    
    /**
     * Set region control
     */
    public void setRegionControl(String gameId, String regionId, String controller) throws SQLException {
        String sql = "INSERT INTO region_control (game_id, region_id, controlled_by) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT(game_id, region_id) " +
                    "DO UPDATE SET controlled_by = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, regionId);
        pstmt.setString(3, controller);
        pstmt.setString(4, controller);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // ===== FELLOWSHIP STATE (Board-based queries) =====
    
    /**
     * Get Fellowship location by querying game_pieces table
     * Treats the game board as the source of truth
     */
    public String getFellowshipLocation(String gameId) throws SQLException {
        String sql = "SELECT area_id FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        String regionId = null;
        if (rs.next()) {
            int areaId = rs.getInt("area_id");
            // TODO: Convert area_id to region_id via areas table
            regionId = String.valueOf(areaId);
        }
        
        rs.close();
        pstmt.close();
        return regionId;
    }
    
    /**
     * Check if Fellowship is revealed by reading piece properties
     */
    public boolean isFellowshipRevealed(String gameId) throws SQLException {
        String sql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        boolean revealed = false;
        if (rs.next()) {
            String stateData = rs.getString("state_data");
            // State data stored as JSON string, e.g.: {"revealed":true,"corruption":3}
            revealed = stateData != null && stateData.contains("\"revealed\":true");
        }
        
        rs.close();
        pstmt.close();
        return revealed;
    }
    
    /**
     * Get Fellowship corruption level from piece properties
     */
    public int getFellowshipCorruption(String gameId) throws SQLException {
        String sql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        int corruption = 0;
        if (rs.next()) {
            String stateData = rs.getString("state_data");
            // Parse corruption from JSON state_data
            if (stateData != null && stateData.contains("\"corruption\":")) {
                // Simple parsing: find "corruption":X
                int idx = stateData.indexOf("\"corruption\":");
                if (idx >= 0) {
                    String substr = stateData.substring(idx + 13); // Skip "corruption":
                    // Extract number
                    int end = 0;
                    while (end < substr.length() && java.lang.Character.isDigit(substr.charAt(end))) {
                        end++;
                    }
                    if (end > 0) {
                        corruption = Integer.parseInt(substr.substring(0, end));
                    }
                }
            }
        }
        
        rs.close();
        pstmt.close();
        return corruption;
    }
    
    /**
     * Move Fellowship piece to new location on the board
     */
    public void moveFellowshipPiece(String gameId, String newRegionId) throws SQLException {
        // TODO: Convert region_id to area_id
        int areaId = Integer.parseInt(newRegionId); // Temporary
        
        String sql = "UPDATE game_pieces SET area_id = ? WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, areaId);
        pstmt.setString(2, gameId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Set Fellowship revealed state in piece properties
     */
    public void setFellowshipRevealed(String gameId, boolean revealed) throws SQLException {
        // Get current state_data
        String currentData = null;
        String selectSql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setString(1, gameId);
        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
            currentData = rs.getString("state_data");
        }
        rs.close();
        selectStmt.close();
        
        // Update state_data JSON
        String newData;
        if (currentData == null || currentData.isEmpty()) {
            newData = String.format("{\"revealed\":%s,\"corruption\":0}", revealed);
        } else {
            // Simple replace
            if (currentData.contains("\"revealed\":")) {
                newData = currentData.replaceAll("\"revealed\":(true|false)", "\"revealed\":" + revealed);
            } else {
                newData = currentData.substring(0, currentData.length()-1) + 
                          ",\"revealed\":" + revealed + "}";
            }
        }
        
        String updateSql = "UPDATE game_pieces SET state_data = ? WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setString(1, newData);
        updateStmt.setString(2, gameId);
        updateStmt.executeUpdate();
        updateStmt.close();
    }
    
    /**
     * Set Fellowship guide in piece properties
     */
    public void setFellowshipGuide(String gameId, String guideId) throws SQLException {
        // Get current state_data
        String currentData = null;
        String selectSql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setString(1, gameId);
        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
            currentData = rs.getString("state_data");
        }
        rs.close();
        selectStmt.close();
        
        // Update state_data JSON
        String newData;
        if (currentData == null || currentData.isEmpty()) {
            newData = String.format("{\"revealed\":false,\"corruption\":0,\"guide\":\"%s\"}", guideId);
        } else {
            // Simple replace
            if (currentData.contains("\"guide\":")) {
                newData = currentData.replaceAll("\"guide\":\"[^\"]*\"", "\"guide\":\"" + guideId + "\"");
            } else {
                newData = currentData.substring(0, currentData.length()-1) + 
                          ",\"guide\":\"" + guideId + "\"}";
            }
        }
        
        String updateSql = "UPDATE game_pieces SET state_data = ? WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setString(1, newData);
        updateStmt.setString(2, gameId);
        updateStmt.executeUpdate();
        updateStmt.close();
    }
    
    /**
     * Add corruption to Fellowship piece properties
     */
    public void addFellowshipCorruption(String gameId, int amount) throws SQLException {
        int currentCorruption = getFellowshipCorruption(gameId);
        int newCorruption = currentCorruption + amount;
        
        // Get current state_data
        String currentData = null;
        String selectSql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setString(1, gameId);
        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
            currentData = rs.getString("state_data");
        }
        rs.close();
        selectStmt.close();
        
        // Update state_data JSON
        String newData;
        if (currentData == null || currentData.isEmpty()) {
            newData = String.format("{\"revealed\":false,\"corruption\":%d}", newCorruption);
        } else {
            // Simple replace
            if (currentData.contains("\"corruption\":")) {
                newData = currentData.replaceAll("\"corruption\":\\d+", "\"corruption\":" + newCorruption);
            } else {
                newData = currentData.substring(0, currentData.length()-1) + 
                          ",\"corruption\":" + newCorruption + "}";
            }
        }
        
        String updateSql = "UPDATE game_pieces SET state_data = ? WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setString(1, newData);
        updateStmt.setString(2, gameId);
        updateStmt.executeUpdate();
        updateStmt.close();
    }
    
    // ===== GAME STATE =====
    
    /**
     * Create a new game state record
     */
    public void createGameState(String gameId, String activePlayer) throws SQLException {
        String sql = "INSERT INTO game_state (game_id, turn_number, phase, active_player, " +
                    "victory_points_fp, victory_points_shadow, created_at, updated_at) " +
                    "VALUES (?, 1, 'recover_action_dice', ?, 0, 0, datetime('now'), datetime('now'))";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, activePlayer);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Get current game state (turn, phase, VP)
     */
    public GameState getGameState(String gameId) throws SQLException {
        String sql = "SELECT * FROM game_state WHERE game_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        GameState state = null;
        if (rs.next()) {
            state = new GameState();
            state.setGameId(rs.getString("game_id"));
            state.setTurnNumber(rs.getInt("turn_number"));
            state.setPhase(rs.getString("phase"));
            state.setActivePlayer(rs.getString("active_player"));
            state.setVictoryPointsFP(rs.getInt("victory_points_fp"));
            state.setVictoryPointsShadow(rs.getInt("victory_points_shadow"));
            state.setCreatedAt(rs.getString("created_at"));
            state.setUpdatedAt(rs.getString("updated_at"));
        }
        
        rs.close();
        pstmt.close();
        return state;
    }
    
    /**
     * Advance to next phase
     */
    public void advancePhase(String gameId, String newPhase) throws SQLException {
        String sql = "UPDATE game_state SET phase = ?, updated_at = datetime('now') WHERE game_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newPhase);
        pstmt.setString(2, gameId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Advance to next turn
     */
    public void nextTurn(String gameId) throws SQLException {
        String sql = "UPDATE game_state SET turn_number = turn_number + 1, " +
                    "phase = 'recover_action_dice', updated_at = datetime('now') WHERE game_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // ===== ACTION DICE =====
    
    /**
     * Create a new action die in the database
     */
    public void createActionDie(String gameId, String dieId, int nationId, String dieType, boolean used) throws SQLException {
        String sql = "INSERT INTO action_dice (game_id, die_id, nation_id, die_type, used) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, dieId);
        pstmt.setInt(3, nationId);
        pstmt.setString(4, dieType);
        pstmt.setBoolean(5, used);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Get all action dice for a game
     */
    public List<wotr.dice.ActionDie> getActionDice(String gameId) throws SQLException {
        List<wotr.dice.ActionDie> dice = new ArrayList<wotr.dice.ActionDie>();
        String sql = "SELECT * FROM action_dice WHERE game_id = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            wotr.dice.ActionDie die = new wotr.dice.ActionDie();
            die.setId(rs.getString("die_id"));
            die.setNationId(rs.getInt("nation_id"));
            
            // Convert die_type string to DieType enum
            String dieTypeStr = rs.getString("die_type");
            if (dieTypeStr != null && !dieTypeStr.equals("null")) {
                die.setResult(parseDieType(dieTypeStr));
            }
            
            // Set status based on 'used' field
            boolean used = rs.getBoolean("used");
            die.setStatus(used ? wotr.dice.DieStatus.USED : wotr.dice.DieStatus.AVAILABLE);
            
            // Check if die is an eye (which goes to hunt box)
            if (dieTypeStr != null && dieTypeStr.equals("eye")) {
                die.setInHuntBox(true);
            }
            
            dice.add(die);
        }
        
        rs.close();
        pstmt.close();
        return dice;
    }
    
    /**
     * Update action die state
     */
    public void updateActionDie(String gameId, String dieId, String dieType, boolean used) throws SQLException {
        String sql = "UPDATE action_dice SET die_type = ?, used = ? " +
                    "WHERE game_id = ? AND die_id = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, dieType);
        pstmt.setBoolean(2, used);
        pstmt.setString(3, gameId);
        pstmt.setString(4, dieId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Parse die type string to DieType enum
     */
    private wotr.dice.DieType parseDieType(String dieTypeStr) {
        switch (dieTypeStr) {
            case "character": return wotr.dice.DieType.CHARACTER;
            case "army": return wotr.dice.DieType.ARMY;
            case "muster": return wotr.dice.DieType.MUSTER;
            case "event": return wotr.dice.DieType.EVENT;
            case "will_of_west": return wotr.dice.DieType.WILL_OF_WEST;
            case "eye": return wotr.dice.DieType.EYE;
            default: return wotr.dice.DieType.CHARACTER; // fallback
        }
    }
    
    // ===== FELLOWSHIP CHARACTER QUERIES =====
    
    /**
     * Create Fellowship piece with initial properties
     */
    public void createFellowshipPiece(String gameId, int startingAreaId, String stateData) throws SQLException {
        String sql = "INSERT INTO game_pieces (game_id, piece_id, piece_type, area_id, state_data) " +
                    "VALUES (?, 'fellowship', 'fellowship', ?, ?)";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setInt(2, startingAreaId);
        pstmt.setString(3, stateData);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Get Fellowship guide from piece properties
     */
    public String getFellowshipGuide(String gameId) throws SQLException {
        String sql = "SELECT state_data FROM game_pieces WHERE game_id = ? AND piece_type = 'fellowship'";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        String guide = null;
        if (rs.next()) {
            String stateData = rs.getString("state_data");
            // Parse guide from JSON state_data
            if (stateData != null && stateData.contains("\"guide\":\"")) {
                int idx = stateData.indexOf("\"guide\":\"");
                if (idx >= 0) {
                    String substr = stateData.substring(idx + 9); // Skip "guide":"
                    int endIdx = substr.indexOf("\"");
                    if (endIdx > 0) {
                        guide = substr.substring(0, endIdx);
                    }
                }
            }
        }
        
        rs.close();
        pstmt.close();
        return guide != null ? guide : "gandalf"; // Default guide
    }
    
    /**
     * Get all characters in a specific area
     * This replaces the region-based query for special areas like Fellowship box, casualties, spare
     */
    public List<String> getCharactersInArea(String gameId, int areaId) throws SQLException {
        List<String> characters = new ArrayList<String>();
        String sql = "SELECT piece_id FROM game_pieces " +
                    "WHERE game_id = ? AND area_id = ? AND piece_type = 'character'";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setInt(2, areaId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            characters.add(rs.getString("piece_id"));
        }
        
        rs.close();
        pstmt.close();
        return characters;
    }
    
    /**
     * Move character to a specific area
     * Used for moving characters to Fellowship box, casualties, spare, etc.
     */
    public void moveCharacterToArea(String gameId, String characterId, int areaId) throws SQLException {
        String sql = "UPDATE game_pieces SET area_id = ? WHERE game_id = ? AND piece_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, areaId);
        pstmt.setString(2, gameId);
        pstmt.setString(3, characterId);
        
        int rowsUpdated = pstmt.executeUpdate();
        
        // If character doesn't exist in game_pieces, insert it
        if (rowsUpdated == 0) {
            String insertSql = "INSERT INTO game_pieces (game_id, piece_id, piece_type, area_id) " +
                              "VALUES (?, ?, 'character', ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, gameId);
            insertStmt.setString(2, characterId);
            insertStmt.setInt(3, areaId);
            insertStmt.executeUpdate();
            insertStmt.close();
        }
        
        pstmt.close();
    }
    
    /**
     * Get the area_id for a character
     */
    public Integer getCharacterAreaId(String gameId, String characterId) throws SQLException {
        String sql = "SELECT area_id FROM game_pieces WHERE game_id = ? AND piece_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, characterId);
        ResultSet rs = pstmt.executeQuery();
        
        Integer areaId = null;
        if (rs.next()) {
            areaId = rs.getInt("area_id");
        }
        
        rs.close();
        pstmt.close();
        return areaId;
    }
    
    // ===== POLITICAL TRACK QUERIES (Board Pieces) =====
    
    /**
     * Get political marker area for a nation
     * Political markers are physical pieces in areas 117-120 on the board
     * Area 117 = Position 3, Area 118 = Position 2, Area 119 = Position 1, Area 120 = Position 0 (At War)
     */
    public Integer getPoliticalMarkerArea(String gameId, String nationId) throws SQLException {
        String sql = "SELECT area_id FROM game_pieces " +
                    "WHERE game_id = ? AND piece_type = 'political_marker' AND nation = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, nationId);
        ResultSet rs = pstmt.executeQuery();
        
        Integer areaId = null;
        if (rs.next()) {
            areaId = rs.getInt("area_id");
        }
        
        rs.close();
        pstmt.close();
        return areaId;
    }
    
    /**
     * Move political marker to new area (position on political track)
     * Moves the physical political marker piece on the board
     */
    public void movePoliticalMarker(String gameId, String nationId, int newAreaId) throws SQLException {
        String sql = "UPDATE game_pieces SET area_id = ? " +
                    "WHERE game_id = ? AND piece_type = 'political_marker' AND nation = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, newAreaId);
        pstmt.setString(2, gameId);
        pstmt.setString(3, nationId);
        
        int rowsUpdated = pstmt.executeUpdate();
        
        // If political marker doesn't exist, insert it
        if (rowsUpdated == 0) {
            String insertSql = "INSERT INTO game_pieces (game_id, piece_type, nation, area_id) " +
                              "VALUES (?, 'political_marker', ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, gameId);
            insertStmt.setString(2, nationId);
            insertStmt.setInt(3, newAreaId);
            insertStmt.executeUpdate();
            insertStmt.close();
        }
        
        pstmt.close();
    }
    
    /**
     * Get TwoChit active/passive state for a nation
     * Queries the 'active' property from the TwoChit piece
     * Returns true if active (can advance to At War), false if passive
     */
    public Boolean getTwoChitState(String gameId, String nationId) throws SQLException {
        String sql = "SELECT properties FROM game_pieces " +
                    "WHERE game_id = ? AND piece_type = 'two_chit' AND nation = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, nationId);
        ResultSet rs = pstmt.executeQuery();
        
        Boolean isActive = null;
        if (rs.next()) {
            String properties = rs.getString("properties");
            // Parse JSON properties to get 'active' field
            // Simple parsing: look for "active":true or "active":false
            if (properties != null) {
                if (properties.contains("\"active\":true") || properties.contains("\"active\": true")) {
                    isActive = true;
                } else if (properties.contains("\"active\":false") || properties.contains("\"active\": false")) {
                    isActive = false;
                }
            }
        }
        
        rs.close();
        pstmt.close();
        return isActive;
    }
    
    /**
     * Set TwoChit to active (flip from passive to active)
     * Updates the 'active' property on the TwoChit piece
     */
    public void setTwoChitActive(String gameId, String nationId, boolean active) throws SQLException {
        // Create JSON properties string
        String properties = "{\"active\":" + active + "}";
        
        String sql = "UPDATE game_pieces SET properties = ? " +
                    "WHERE game_id = ? AND piece_type = 'two_chit' AND nation = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, properties);
        pstmt.setString(2, gameId);
        pstmt.setString(3, nationId);
        
        int rowsUpdated = pstmt.executeUpdate();
        
        // If TwoChit doesn't exist, insert it
        if (rowsUpdated == 0) {
            String insertSql = "INSERT INTO game_pieces (game_id, piece_type, nation, properties) " +
                              "VALUES (?, 'two_chit', ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, gameId);
            insertStmt.setString(2, nationId);
            insertStmt.setString(3, properties);
            insertStmt.executeUpdate();
            insertStmt.close();
        }
        
        pstmt.close();
    }
    
    // ===== ON-TABLE CARD TRACKING =====
    
    /**
     * Add a card to on-table tracking
     */
    public void addOnTableCard(String gameId, String cardName, String faction, int areaId) throws SQLException {
        String sql = "INSERT OR REPLACE INTO on_table_cards (game_id, card_name, faction, area_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, cardName);
        pstmt.setString(3, faction);
        pstmt.setInt(4, areaId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Remove a card from on-table tracking
     */
    public void removeOnTableCard(String gameId, String cardName) throws SQLException {
        String sql = "DELETE FROM on_table_cards WHERE game_id = ? AND card_name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, cardName);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    /**
     * Get all cards on table
     */
    public List<String> getOnTableCards(String gameId) throws SQLException {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT card_name FROM on_table_cards WHERE game_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            cards.add(rs.getString("card_name"));
        }
        
        rs.close();
        pstmt.close();
        return cards;
    }
    
    /**
     * Get on-table cards for a specific faction
     */
    public List<String> getOnTableCardsByFaction(String gameId, String faction) throws SQLException {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT card_name FROM on_table_cards WHERE game_id = ? AND faction = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setString(2, faction);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            cards.add(rs.getString("card_name"));
        }
        
        rs.close();
        pstmt.close();
        return cards;
    }
    
    /**
     * Check if an area is occupied by an on-table card
     */
    public boolean isOnTableAreaOccupied(String gameId, int areaId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM on_table_cards WHERE game_id = ? AND area_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.setInt(2, areaId);
        ResultSet rs = pstmt.executeQuery();
        
        boolean occupied = false;
        if (rs.next()) {
            occupied = rs.getInt("count") > 0;
        }
        
        rs.close();
        pstmt.close();
        return occupied;
    }
    
    /**
     * Clear all on-table cards
     */
    public void clearOnTableCards(String gameId) throws SQLException {
        String sql = "DELETE FROM on_table_cards WHERE game_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, gameId);
        pstmt.executeUpdate();
        pstmt.close();
    }
}
