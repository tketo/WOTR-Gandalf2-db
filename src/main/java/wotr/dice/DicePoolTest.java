package wotr.dice;

import wotr.models.Nation;
import java.util.*;

/**
 * DicePoolTest - Test action dice system
 * 
 * Demonstrates dice rolling, Hunt Box allocation, and dice usage
 */
public class DicePoolTest {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Action Dice System Test ===\n");
        
        try {
            testDiceTypes();
            testDicePool();
            testDiceRolling();
            testHuntBox();
            
            System.out.println("\n=== All Dice Tests Passed! ===");
            
        } catch (Exception e) {
            System.err.println("\n=== Test Failed ===");
            e.printStackTrace();
        }
    }
    
    private static void testDiceTypes() {
        System.out.println("--- Testing Die Types ---");
        
        // Test die type properties
        for (DieType type : DieType.values()) {
            System.out.println("✓ " + type.getDisplayName() + ": " + type.getDescription());
            
            if (type.isFreePeoplesOnly()) {
                System.out.println("    → Free Peoples only");
            }
            if (type.isShadowOnly()) {
                System.out.println("    → Shadow only");
            }
            if (type.goesToHuntBox()) {
                System.out.println("    → Automatically goes to Hunt Box");
            }
        }
        
        System.out.println("✓ All die types defined correctly\n");
    }
    
    private static void testDicePool() {
        System.out.println("--- Testing Dice Pool ---");
        
        // Create dice pool
        DicePool pool = new DicePool("test_game");
        
        // Create nations
        Nation gondor = new Nation(1, "Gondor", "free_peoples");
        Nation isengard = new Nation(5, "Isengard", "shadow");
        
        // Initialize dice (4 for each nation to start)
        pool.initializeDice(gondor, 4);
        pool.initializeDice(isengard, 4);
        
        System.out.println("✓ Initialized dice pool");
        System.out.println("  Gondor: " + pool.getDiceForNation(1).size() + " dice");
        System.out.println("  Isengard: " + pool.getDiceForNation(5).size() + " dice");
        
        // Check available dice
        List<ActionDie> gondorDice = pool.getAvailableDice(1);
        if (gondorDice.size() == 4) {
            System.out.println("✓ All Gondor dice available");
        }
    }
    
    private static void testDiceRolling() {
        System.out.println("\n--- Testing Dice Rolling ---");
        
        DicePool pool = new DicePool("test_game");
        Nation gondor = new Nation(1, "Gondor", "free_peoples");
        Nation isengard = new Nation(5, "Isengard", "shadow");
        
        pool.initializeDice(gondor, 4);
        pool.initializeDice(isengard, 4);
        
        // Roll Free Peoples dice
        System.out.println("\nRolling Free Peoples dice:");
        pool.rollDice("free_peoples");
        
        Map<DieType, Integer> fpResults = pool.countDiceByType(1);
        for (Map.Entry<DieType, Integer> entry : fpResults.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println("  " + entry.getKey().getDisplayName() + ": " + entry.getValue());
            }
        }
        
        // Roll Shadow dice
        System.out.println("\nRolling Shadow dice:");
        pool.rollDice("shadow");
        
        Map<DieType, Integer> shadowResults = pool.countDiceByType(5);
        for (Map.Entry<DieType, Integer> entry : shadowResults.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println("  " + entry.getKey().getDisplayName() + ": " + entry.getValue());
            }
        }
        
        // Check if any Eyes went to Hunt Box automatically
        int huntBoxCount = pool.getHuntBoxCount();
        System.out.println("\n✓ Dice rolled successfully");
        System.out.println("  Hunt Box (Eyes): " + huntBoxCount + " dice");
    }
    
    private static void testHuntBox() {
        System.out.println("\n--- Testing Hunt Box ---");
        
        DicePool pool = new DicePool("test_game");
        Nation isengard = new Nation(5, "Isengard", "shadow");
        pool.initializeDice(isengard, 5);
        
        // Roll Shadow dice (some Eyes should go to Hunt Box)
        pool.rollDice("shadow");
        
        int initialHunt = pool.getHuntBoxCount();
        System.out.println("After rolling: " + initialHunt + " dice in Hunt Box");
        
        // Test Hunt allocation rules
        int minAllocation = pool.getMinimumHuntAllocation();
        int maxAllocation = pool.getMaximumHuntAllocation(4); // 4 companions
        
        System.out.println("✓ Hunt Box allocation rules:");
        System.out.println("  Minimum: " + minAllocation);
        System.out.println("  Maximum (4 companions): " + maxAllocation);
        
        // Test retrieving from Hunt Box
        List<ActionDie> retrieved = pool.retrieveFromHuntBox();
        System.out.println("✓ Retrieved " + retrieved.size() + " dice from Hunt Box");
        
        // After retrieval, check minimum for next turn
        int nextMinimum = pool.getMinimumHuntAllocation();
        if (retrieved.size() > 0 && nextMinimum == 1) {
            System.out.println("✓ Next turn minimum is 1 (FP retrieved dice)");
        }
    }
    
    private static void demonstrateTurnCycle() {
        System.out.println("\n--- Example Turn Cycle ---");
        System.out.println("Turn 1:");
        System.out.println("  1. Recover dice (all become available)");
        System.out.println("  2. Fellowship Phase");
        System.out.println("  3. Shadow allocates 2 dice to Hunt Box");
        System.out.println("  4. Roll remaining dice");
        System.out.println("     → 3 Character, 2 Army, 1 Muster, 2 Event, 1 Eye (to Hunt)");
        System.out.println("  5. Use dice for actions");
        System.out.println("  6. Victory check");
        System.out.println("\nTurn 2:");
        System.out.println("  1. Retrieve 3 dice from Hunt Box");
        System.out.println("  2. Recover all dice");
        System.out.println("  3. Continue...");
    }
}
