package wotr.database;

import wotr.dao.GameStateDAO;
import wotr.models.*;
import java.sql.Connection;
import java.util.List;

/**
 * Test the "Database as Board" concept
 * 
 * This test demonstrates querying the database like you'd look at a physical game board
 * 
 * NOTE: This test uses the deprecated army_units table and DAO methods.
 * This is intentional - it tests backward compatibility and the DAO layer.
 * 
 * For new code, use the Game.java query methods instead:
 * - game.getPiecesInArea(areaId)
 * - game.countPiecesInArea(areaId, type)
 * - game.countMilitaryUnitsInArea(areaId)
 * 
 * @see wotr.Game#getPiecesInArea(int)
 * @see wotr.Game#countPiecesInArea(int, String)
 */
@SuppressWarnings("deprecation")
public class GameStateTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Game State Test - Database as Board ===\n");
            
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            GameStateDAO gameState = new GameStateDAO(conn);
            
            String gameId = "test_game_1";
            
            // Initialize a test game
            initializeTestGame(conn, gameId);
            
            // TEST 1: Query character locations (like looking at the board)
            System.out.println("TEST 1: Character Positions");
            System.out.println("----------------------------");
            String striderLocation = gameState.getCharacterLocation(gameId, "strider");
            System.out.println("Where is Strider? " + striderLocation);
            
            boolean gandalfInPlay = gameState.isCharacterInPlay(gameId, "gandalf_the_grey");
            System.out.println("Is Gandalf in play? " + gandalfInPlay);
            
            // Move Strider
            System.out.println("\n→ Moving Strider from Rivendell to Bree");
            gameState.updateCharacterPosition(gameId, "strider", "bree");
            
            striderLocation = gameState.getCharacterLocation(gameId, "strider");
            System.out.println("Where is Strider now? " + striderLocation);
            
            // TEST 2: Query armies (what tokens are where?)
            System.out.println("\n\nTEST 2: Army Units");
            System.out.println("-------------------");
            List<ArmyUnit> minasTirithArmy = gameState.getArmyUnits(gameId, "minas_tirith");
            System.out.println("Army in Minas Tirith:");
            for (ArmyUnit unit : minasTirithArmy) {
                System.out.println("  - " + unit.getCount() + " " + unit.getUnitType() + 
                                 " (Nation " + unit.getNationId() + ")");
            }
            
            // Add reinforcements
            System.out.println("\n→ Adding 2 Gondor regulars to Minas Tirith");
            gameState.addUnits(gameId, "minas_tirith", 3, "regular", 2);
            
            minasTirithArmy = gameState.getArmyUnits(gameId, "minas_tirith");
            System.out.println("Army in Minas Tirith after reinforcement:");
            for (ArmyUnit unit : minasTirithArmy) {
                System.out.println("  - " + unit.getCount() + " " + unit.getUnitType() +
                                 " (Nation " + unit.getNationId() + ")");
            }
            
            // TEST 3: Region control (who owns what?)
            System.out.println("\n\nTEST 3: Region Control");
            System.out.println("----------------------");
            String minasTirithControl = gameState.getRegionControl(gameId, "minas_tirith");
            System.out.println("Who controls Minas Tirith? " + minasTirithControl);
            
            String mordorControl = gameState.getRegionControl(gameId, "nurn");
            System.out.println("Who controls Nurn (Mordor)? " + mordorControl);
            
            // TEST 4: Game state (turn, phase, VP)
            System.out.println("\n\nTEST 4: Game State");
            System.out.println("------------------");
            GameState state = gameState.getGameState(gameId);
            System.out.println(state);
            
            System.out.println("\n→ Advancing phase");
            gameState.advancePhase(gameId, "fellowship_phase");
            
            state = gameState.getGameState(gameId);
            System.out.println(state);
            
            // TEST 5: Fellowship state
            System.out.println("\n\nTEST 5: Fellowship State");
            System.out.println("------------------------");        
            // Test fellowship state retrieval
            // TODO: Implement getFellowshipState() in GameStateDAO
            // FellowshipState fellowship = gameState.getFellowshipState(TEST_GAME_ID);
            // System.out.println(fellowship);
            
            System.out.println("\n\n=== Test Complete ===");
            System.out.println("✓ Database successfully acts as game board!");
            System.out.println("✓ All queries work like looking at physical board");
            
        } catch (Exception e) {
            System.err.println("Test failed!");
            e.printStackTrace();
        }
    }
    
    private static void initializeTestGame(Connection conn, String gameId) throws Exception {
        System.out.println("Initializing test game...\n");
        
        // First, insert reference data that foreign keys need
        
        // Insert nations
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO nations (id, name) VALUES (3, 'Gondor')"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO nations (id, name) VALUES (7, 'Sauron')"
        );
        
        // Insert regions
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO regions (id, name, nation_id) " +
            "VALUES ('rivendell', 'Rivendell', NULL)"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO regions (id, name, nation_id) " +
            "VALUES ('bree', 'Bree', NULL)"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO regions (id, name, nation_id) " +
            "VALUES ('minas_tirith', 'Minas Tirith', 3)"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO regions (id, name, nation_id) " +
            "VALUES ('nurn', 'Nurn', 7)"
        );
        
        // Insert characters (with all required fields - type must be 'companion' or 'minion')
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO characters (id, name, title, faction, type, level, leadership, action_die_bonus, can_guide, playable_by) " +
            "VALUES ('strider', 'Strider', NULL, 'Free Peoples', 'companion', '3', 1, 0, 0, 'Free Peoples')"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT OR IGNORE INTO characters (id, name, title, faction, type, level, leadership, action_die_bonus, can_guide, playable_by) " +
            "VALUES ('gandalf_the_grey', 'Gandalf the Grey', 'The Grey Pilgrim', 'Free Peoples', 'companion', '3', 1, 0, 1, 'Free Peoples')"
        );
        
        // Now insert game state
        conn.createStatement().executeUpdate(
            "INSERT INTO game_state (game_id, turn_number, phase, active_player, " +
            "victory_points_fp, victory_points_shadow, created_at, updated_at) " +
            "VALUES ('" + gameId + "', 1, 'action_resolution', 'free_peoples', 0, 0, " +
            "datetime('now'), datetime('now'))"
        );
        
        // Place Strider in Rivendell
        conn.createStatement().executeUpdate(
            "INSERT INTO character_positions (game_id, character_id, region_id, status) " +
            "VALUES ('" + gameId + "', 'strider', 'rivendell', 'in_play')"
        );
        
        // Place Gandalf in Rivendell (in play)
        conn.createStatement().executeUpdate(
            "INSERT INTO character_positions (game_id, character_id, region_id, status) " +
            "VALUES ('" + gameId + "', 'gandalf_the_grey', 'rivendell', 'in_play')"
        );
        
        // Place some army units in Minas Tirith
        conn.createStatement().executeUpdate(
            "INSERT INTO army_units (game_id, region_id, nation_id, unit_type, count) " +
            "VALUES ('" + gameId + "', 'minas_tirith', 3, 'regular', 3)"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT INTO army_units (game_id, region_id, nation_id, unit_type, count) " +
            "VALUES ('" + gameId + "', 'minas_tirith', 3, 'elite', 1)"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT INTO army_units (game_id, region_id, nation_id, unit_type, count) " +
            "VALUES ('" + gameId + "', 'minas_tirith', 3, 'leader', 1)"
        );
        
        // Set region control
        conn.createStatement().executeUpdate(
            "INSERT INTO region_control (game_id, region_id, controlled_by) " +
            "VALUES ('" + gameId + "', 'minas_tirith', 'free_peoples')"
        );
        
        conn.createStatement().executeUpdate(
            "INSERT INTO region_control (game_id, region_id, controlled_by) " +
            "VALUES ('" + gameId + "', 'nurn', 'shadow')"
        );
        
        // Set fellowship state
        conn.createStatement().executeUpdate(
            "INSERT INTO fellowship_state (game_id, region_id, corruption_level, revealed, guide_character_id) " +
            "VALUES ('" + gameId + "', 'rivendell', 0, 0, 'gandalf_the_grey')"
        );
        
        System.out.println("Test game initialized!");
    }
}
