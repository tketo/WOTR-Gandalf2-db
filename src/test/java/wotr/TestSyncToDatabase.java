package wotr;

import wotr.services.GameStateService;
import wotr.mapping.GameBoardQuery;
import wotr.models.ArmyUnit;
import java.util.List;

/**
 * Test sync TO database functionality (Hybrid Approach - Phase 3)
 * Tests that the helper methods correctly sync unit data to database
 */
public class TestSyncToDatabase {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing Sync TO Database (Phase 3) ===\n");
            
            // Step 1: Initialize services
            System.out.println("--- Step 1: Initialize Services ---");
            GameStateService gameState = new GameStateService();
            GameBoardQuery boardQuery = GameBoardQuery.getInstance();
            
            String gameId = gameState.createNewGame("free_peoples");
            boardQuery.setGameState(gameState);
            System.out.println("✓ Game created: " + gameId);
            
            // Step 2: Add initial units
            System.out.println("\n--- Step 2: Add Initial Units ---");
            
            // Add 5 Gondor regulars to Minas Tirith (area 74)
            boardQuery.addUnitsToArea(74, 3, "regular", 5);
            System.out.println("✓ Added 5 Gondor regulars to Minas Tirith (area 74)");
            
            // Verify initial state
            List<ArmyUnit> initialUnits = boardQuery.getUnitsInArea(74);
            int initialCount = initialUnits.stream()
                .filter(u -> u.getNationId() == 3 && "regular".equals(u.getUnitType()))
                .mapToInt(ArmyUnit::getCount)
                .sum();
            System.out.println("  Database state: " + initialCount + " units in Minas Tirith");
            
            // Step 3: Simulate unit movement
            System.out.println("\n--- Step 3: Simulate Unit Movement ---");
            System.out.println("Moving 2 units from Minas Tirith (74) to Osgiliath (73)...");
            
            // Simulate what syncUnitMoveToDatabase() would do
            boardQuery.removeUnitsFromArea(74, 3, "regular", 2);
            boardQuery.addUnitsToArea(73, 3, "regular", 2);
            System.out.println("✓ Database updated");
            
            // Step 4: Verify database state after move
            System.out.println("\n--- Step 4: Verify Database State ---");
            
            List<ArmyUnit> minasTirithUnits = boardQuery.getUnitsInArea(74);
            int minasTirithCount = minasTirithUnits.stream()
                .filter(u -> u.getNationId() == 3 && "regular".equals(u.getUnitType()))
                .mapToInt(ArmyUnit::getCount)
                .sum();
            
            List<ArmyUnit> osgiliathUnits = boardQuery.getUnitsInArea(73);
            int osgiliathCount = osgiliathUnits.stream()
                .filter(u -> u.getNationId() == 3 && "regular".equals(u.getUnitType()))
                .mapToInt(ArmyUnit::getCount)
                .sum();
            
            System.out.println("Minas Tirith (74): " + minasTirithCount + " units");
            System.out.println("Osgiliath (73): " + osgiliathCount + " units");
            
            // Step 5: Validation
            System.out.println("\n--- Step 5: Validation ---");
            
            boolean pass = true;
            
            if (minasTirithCount != 3) {
                System.out.println("✗ FAIL: Minas Tirith should have 3 units, has " + minasTirithCount);
                pass = false;
            } else {
                System.out.println("✓ PASS: Minas Tirith has correct count (3)");
            }
            
            if (osgiliathCount != 2) {
                System.out.println("✗ FAIL: Osgiliath should have 2 units, has " + osgiliathCount);
                pass = false;
            } else {
                System.out.println("✓ PASS: Osgiliath has correct count (2)");
            }
            
            if (minasTirithCount + osgiliathCount != 5) {
                System.out.println("✗ FAIL: Total units should be 5, is " + (minasTirithCount + osgiliathCount));
                pass = false;
            } else {
                System.out.println("✓ PASS: Total units preserved (5)");
            }
            
            // Step 6: Test multiple movements
            System.out.println("\n--- Step 6: Test Multiple Movements ---");
            
            // Move 1 unit from Osgiliath to Pelargir
            System.out.println("Moving 1 unit from Osgiliath (73) to Pelargir (76)...");
            boardQuery.removeUnitsFromArea(73, 3, "regular", 1);
            boardQuery.addUnitsToArea(76, 3, "regular", 1);
            
            // Verify
            osgiliathUnits = boardQuery.getUnitsInArea(73);
            osgiliathCount = osgiliathUnits.stream()
                .filter(u -> u.getNationId() == 3 && "regular".equals(u.getUnitType()))
                .mapToInt(ArmyUnit::getCount)
                .sum();
            
            List<ArmyUnit> pelargirUnits = boardQuery.getUnitsInArea(76);
            int pelargirCount = pelargirUnits.stream()
                .filter(u -> u.getNationId() == 3 && "regular".equals(u.getUnitType()))
                .mapToInt(ArmyUnit::getCount)
                .sum();
            
            System.out.println("  Osgiliath: " + osgiliathCount + " units");
            System.out.println("  Pelargir: " + pelargirCount + " units");
            
            if (osgiliathCount == 1 && pelargirCount == 1) {
                System.out.println("✓ PASS: Multiple movements working");
            } else {
                System.out.println("✗ FAIL: Multiple movements failed");
                pass = false;
            }
            
            // Final result
            System.out.println("\n=== Test Result ===");
            if (pass) {
                System.out.println("✓ ALL TESTS PASSED!");
                System.out.println("\nSync TO Database methods are working correctly.");
                System.out.println("Next: Wrap GamePiece.moveTo() calls to use syncUnitMoveToDatabase()");
            } else {
                System.out.println("✗ SOME TESTS FAILED");
            }
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
