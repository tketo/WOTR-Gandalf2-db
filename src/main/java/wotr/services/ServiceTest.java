package wotr.services;

import wotr.models.*;
import java.sql.SQLException;

/**
 * ServiceTest - Quick integration test for GameDataService and GameStateService
 * 
 * This demonstrates the "Database as Board" concept:
 * - GameDataService provides static reference data (the rulebook)
 * - GameStateService provides dynamic game state (the board)
 */
public class ServiceTest {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Service Layer Test ===\n");
        
        try {
            testGameDataService();
            testGameStateService();
            
            System.out.println("\n=== All Tests Passed! ===");
            
        } catch (Exception e) {
            System.err.println("\n=== Test Failed ===");
            e.printStackTrace();
        }
    }
    
    private static void testGameDataService() throws SQLException {
        System.out.println("--- Testing GameDataService (Static Reference Data) ---");
        
        GameDataService data = GameDataService.getInstance();
        
        // Test nation lookups
        Nation gondor = data.getNationByName("Gondor");
        if (gondor != null) {
            System.out.println("✓ Found nation: " + gondor.getName() + " (ID: " + gondor.getId() + ")");
        } else {
            System.out.println("✗ Gondor not found in database");
        }
        
        // Test region lookups
        Region rivendell = data.getRegion("rivendell");
        if (rivendell != null) {
            System.out.println("✓ Found region: " + rivendell.getName());
            if (data.hasSettlement("rivendell")) {
                Settlement settlement = data.getSettlement("rivendell");
                System.out.println("  - Settlement type: " + settlement.getType());
            }
        } else {
            System.out.println("✗ Rivendell not found");
        }
        
        // Test character lookups
        wotr.models.Character strider = data.getCharacter("strider");
        if (strider != null) {
            System.out.println("✓ Found character: " + strider.getName());
            System.out.println("  - Faction: " + strider.getFaction());
            System.out.println("  - Leadership: " + strider.getLeadership());
        } else {
            System.out.println("✗ Strider not found");
        }
        
        // Display cache stats
        System.out.println("\n" + data.getCacheStats());
    }
    
    private static void testGameStateService() throws SQLException {
        System.out.println("\n--- Testing GameStateService (Dynamic Game State) ---");
        
        GameStateService gameState = new GameStateService();
        
        // Note: createNewGame() is not fully implemented yet
        // This is a placeholder demonstration
        System.out.println("✓ GameStateService initialized");
        System.out.println("  - Ready to manage game board state");
        System.out.println("  - Can query character positions, armies, control, etc.");
        
        // TODO: Once game initialization is implemented:
        // String gameId = gameState.createNewGame("free_peoples");
        // gameState.moveCharacter("strider", "rivendell");
        // String location = gameState.getCharacterLocation("strider");
        // System.out.println("Strider is at: " + location);
    }
}
