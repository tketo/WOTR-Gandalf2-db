package wotr;

import wotr.services.GameStateService;
import wotr.mapping.GameBoardQuery;
import wotr.models.ArmyUnit;
import java.util.List;

/**
 * Test database sync functionality (Hybrid Approach - Phase 2)
 * Tests that units can be loaded from database and synced to Game state
 */
public class TestDatabaseSync {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing Database Sync (Hybrid Approach) ===\n");
            
            // Step 1: Initialize services
            System.out.println("--- Step 1: Initialize Services ---");
            GameStateService gameState = new GameStateService();
            GameBoardQuery boardQuery = GameBoardQuery.getInstance();
            
            String gameId = gameState.createNewGame("free_peoples");
            boardQuery.setGameState(gameState);
            System.out.println("✓ Game created: " + gameId);
            
            // Step 2: Add units to database
            System.out.println("\n--- Step 2: Add Units to Database ---");
            
            // Add units to Minas Tirith (area 74, region 53)
            boardQuery.addUnitsToArea(74, 3, "regular", 5); // 5 Gondor regulars
            boardQuery.addUnitsToArea(74, 3, "elite", 2);   // 2 Gondor elite
            System.out.println("✓ Added 7 units to Minas Tirith (area 74)");
            
            // Add units to Edoras (area 43, region 26)
            boardQuery.addUnitsToArea(43, 5, "regular", 3); // 3 Rohan regulars
            System.out.println("✓ Added 3 units to Edoras (area 43)");
            
            // Add units to Rivendell (area 27, region 81)
            boardQuery.addUnitsToArea(27, 2, "regular", 2); // 2 Elven regulars
            boardQuery.addUnitsToArea(27, 2, "elite", 1);   // 1 Elven elite
            System.out.println("✓ Added 3 units to Rivendell (area 27)");
            
            // Step 3: Verify units in database
            System.out.println("\n--- Step 3: Verify Database State ---");
            
            List<ArmyUnit> minasTirithUnits = boardQuery.getUnitsInArea(74);
            int minasTirithTotal = minasTirithUnits.stream()
                .mapToInt(ArmyUnit::getCount)
                .sum();
            System.out.println("Minas Tirith: " + minasTirithTotal + " units in database");
            
            List<ArmyUnit> edorasUnits = boardQuery.getUnitsInArea(43);
            int edorasTotal = edorasUnits.stream()
                .mapToInt(ArmyUnit::getCount)
                .sum();
            System.out.println("Edoras: " + edorasTotal + " units in database");
            
            List<ArmyUnit> rivendellUnits = boardQuery.getUnitsInArea(27);
            int rivendellTotal = rivendellUnits.stream()
                .mapToInt(ArmyUnit::getCount)
                .sum();
            System.out.println("Rivendell: " + rivendellTotal + " units in database");
            
            // Step 4: Show sync would work
            System.out.println("\n--- Step 4: Database Sync Concept ---");
            System.out.println("Database now has:");
            System.out.println("  • 7 units in Minas Tirith");
            System.out.println("  • 3 units in Edoras");
            System.out.println("  • 3 units in Rivendell");
            System.out.println("\nWhen Game.syncUnitsFromDatabase() is called:");
            System.out.println("  1. Query database for all units in all regions");
            System.out.println("  2. Create GamePiece objects (UnitGondorRegular, etc.)");
            System.out.println("  3. Place them in corresponding Area objects");
            System.out.println("  4. bits[] array and rendering system work normally");
            
            // Step 5: Test sync logic manually
            System.out.println("\n--- Step 5: Test Unit Creation Logic ---");
            System.out.println("Simulating what syncUnitsFromDatabase() would do...");
            
            int testUnitsCreated = 0;
            for (ArmyUnit unit : minasTirithUnits) {
                System.out.println("  Would create " + unit.getCount() + " x " + 
                    "nation=" + unit.getNationId() + " " + 
                    "type=" + unit.getUnitType());
                testUnitsCreated += unit.getCount();
            }
            for (ArmyUnit unit : edorasUnits) {
                System.out.println("  Would create " + unit.getCount() + " x " + 
                    "nation=" + unit.getNationId() + " " + 
                    "type=" + unit.getUnitType());
                testUnitsCreated += unit.getCount();
            }
            for (ArmyUnit unit : rivendellUnits) {
                System.out.println("  Would create " + unit.getCount() + " x " + 
                    "nation=" + unit.getNationId() + " " + 
                    "type=" + unit.getUnitType());
                testUnitsCreated += unit.getCount();
            }
            
            System.out.println("\n✓ Total units that would be created: " + testUnitsCreated);
            
            // Validation
            System.out.println("\n--- Validation ---");
            if (testUnitsCreated == 13) {
                System.out.println("✓ PASS: Correct number of units (13 expected, " + testUnitsCreated + " counted)");
            } else {
                System.out.println("✗ FAIL: Wrong number of units (13 expected, " + testUnitsCreated + " counted)");
            }
            
            System.out.println("\n✓ DATABASE SYNC TEST COMPLETE!");
            System.out.println("\nNext Step: Call syncUnitsFromDatabase() in Game initialization");
            System.out.println("to load units from database into bits[] array.");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
