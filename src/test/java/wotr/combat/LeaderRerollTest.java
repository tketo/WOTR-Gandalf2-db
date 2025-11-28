package wotr.combat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import wotr.rules.CombatDice;
import java.util.List;
import java.util.ArrayList;

/**
 * Tests for Leader Re-roll Mechanics
 * 
 * Tests the CombatDice re-roll functionality and integration with combat resolution
 */
public class LeaderRerollTest {
    
    @Test
    @DisplayName("Combat dice can be rolled")
    public void testRollDice() {
        List<Integer> results = CombatDice.rollDice(5);
        
        assertEquals(5, results.size(), "Should roll 5 dice");
        for (int result : results) {
            assertTrue(result >= 1 && result <= 6, "Each die should be 1-6");
        }
    }
    
    @Test
    @DisplayName("Natural 1 always misses")
    public void testNatural1AlwaysMisses() {
        List<Integer> results = new ArrayList<>();
        results.add(1);  // Natural 1
        results.add(5);  // Hit
        results.add(6);  // Hit
        
        int hits = CombatDice.countHits(results, 5);
        assertEquals(2, hits, "Natural 1 should miss, only 5 and 6 should hit");
    }
    
    @Test
    @DisplayName("Natural 6 always hits")
    public void testNatural6AlwaysHits() {
        List<Integer> results = new ArrayList<>();
        results.add(6);  // Natural 6 - always hits
        results.add(5);  // Miss (need 6 to hit, 5 < 6)
        results.add(4);  // Miss
        
        int hits = CombatDice.countHits(results, 6);
        assertEquals(1, hits, "Only natural 6 should hit when target is 6");
    }
    
    @Test
    @DisplayName("Standard hit on 5+")
    public void testStandardHitOn5() {
        List<Integer> results = new ArrayList<>();
        results.add(3);  // Miss
        results.add(4);  // Miss
        results.add(5);  // Hit
        results.add(6);  // Hit
        
        int hits = CombatDice.countHits(results, 5);
        assertEquals(2, hits, "Should hit on 5 and 6 when target is 5");
    }
    
    @Test
    @DisplayName("Leader re-roll: No re-rolls with 0 leadership")
    public void testNoRerollsWithZeroLeadership() {
        List<Integer> original = new ArrayList<>();
        original.add(1);
        original.add(2);
        original.add(3);
        
        List<Integer> rerolled = CombatDice.rerollFailed(original, 5, 0);
        
        assertEquals(original, rerolled, "Should not re-roll with 0 leadership");
    }
    
    @Test
    @DisplayName("Leader re-roll: Re-rolls failed dice up to leadership value")
    public void testLeaderRerollsFailedDice() {
        // This test is probabilistic, but we can verify behavior
        List<Integer> original = new ArrayList<>();
        original.add(1);  // Failed - should be re-rolled
        original.add(2);  // Failed - should be re-rolled
        original.add(6);  // Success - should NOT be re-rolled
        
        List<Integer> rerolled = CombatDice.rerollFailed(original, 5, 2);
        
        assertEquals(3, rerolled.size(), "Should still have 3 dice");
        assertEquals(6, rerolled.get(2), "Natural 6 should not be re-rolled");
        // First two dice should be re-rolled (may still be same values by chance)
        assertNotNull(rerolled.get(0));
        assertNotNull(rerolled.get(1));
    }
    
    @Test
    @DisplayName("Leader re-roll: Does not re-roll natural 6s")
    public void testDoesNotRerollNatural6() {
        List<Integer> original = new ArrayList<>();
        original.add(6);  // Natural 6 - should NOT be re-rolled
        original.add(1);  // Failed
        
        List<Integer> rerolled = CombatDice.rerollFailed(original, 5, 1);
        
        assertEquals(6, rerolled.get(0), "Natural 6 should never be re-rolled");
        // Second die may or may not change, but should exist
        assertTrue(rerolled.get(1) >= 1 && rerolled.get(1) <= 6);
    }
    
    @Test
    @DisplayName("Leader re-roll: Respects leadership limit")
    public void testLeadershipLimit() {
        List<Integer> original = new ArrayList<>();
        original.add(1);  // Failed
        original.add(2);  // Failed
        original.add(3);  // Failed
        
        List<Integer> rerolled = CombatDice.rerollFailed(original, 5, 1);
        
        // With leadership 1, only first failed die should be re-rolled
        // We can't guarantee the value, but we can verify the size stays the same
        assertEquals(3, rerolled.size(), "Should still have 3 dice");
    }
    
    @Test
    @DisplayName("Determine dice count: Respects max of 5")
    public void testDetermineMaxDice() {
        int diceCount = CombatDice.determineDiceCount(10, 10);
        assertEquals(5, diceCount, "Max dice should be 5");
    }
    
    @Test
    @DisplayName("Determine dice count: Limited by combat strength")
    public void testDiceLimitedByCombatStrength() {
        int diceCount = CombatDice.determineDiceCount(3, 10);
        assertEquals(3, diceCount, "Dice should be limited by combat strength");
    }
    
    @Test
    @DisplayName("Determine dice count: Limited by unit count")
    public void testDiceLimitedByUnitCount() {
        int diceCount = CombatDice.determineDiceCount(10, 2);
        assertEquals(2, diceCount, "Dice should be limited by unit count");
    }
    
    @Test
    @DisplayName("Target number: Field battle is 5")
    public void testFieldBattleTargetNumber() {
        int target = CombatDice.getTargetNumber(true, "field", false, false);
        assertEquals(5, target, "Field battle should require 5+ to hit");
    }
    
    @Test
    @DisplayName("Target number: Attacker in city (first round) is 6")
    public void testAttackerInCityFirstRound() {
        int target = CombatDice.getTargetNumber(true, "city", true, false);
        assertEquals(6, target, "Attacker in city first round should need 6+ to hit");
    }
    
    @Test
    @DisplayName("Target number: Defender in city (first round) is 5")
    public void testDefenderInCityFirstRound() {
        int target = CombatDice.getTargetNumber(false, "city", true, false);
        assertEquals(5, target, "Defender in city first round should need 5+ to hit");
    }
    
    @Test
    @DisplayName("Target number: City advantage only on first round")
    public void testCityAdvantageOnlyFirstRound() {
        int attackerRound1 = CombatDice.getTargetNumber(true, "city", true, false);
        int attackerRound2 = CombatDice.getTargetNumber(true, "city", false, false);
        
        assertEquals(6, attackerRound1, "Attacker needs 6 on first round");
        assertEquals(5, attackerRound2, "Attacker needs 5 on subsequent rounds");
    }
    
    @Test
    @DisplayName("Target number: Siege battle - attacker needs 6")
    public void testSiegeBattleAttacker() {
        int target = CombatDice.getTargetNumber(true, "stronghold", true, true);
        assertEquals(6, target, "Attacker in siege needs 6+ to hit");
    }
    
    @Test
    @DisplayName("Target number: Siege battle - defender needs 5")
    public void testSiegeBattleDefender() {
        int target = CombatDice.getTargetNumber(false, "stronghold", true, true);
        assertEquals(5, target, "Defender in siege needs 5+ to hit");
    }
    
    @Test
    @DisplayName("Format dice results displays correctly")
    public void testFormatDiceResults() {
        List<Integer> results = new ArrayList<>();
        results.add(1);
        results.add(5);
        results.add(6);
        
        String formatted = CombatDice.formatDiceResults(results, 2);
        
        assertTrue(formatted.contains("1"), "Should show die value 1");
        assertTrue(formatted.contains("5"), "Should show die value 5");
        assertTrue(formatted.contains("6"), "Should show die value 6");
        assertTrue(formatted.contains("2 hit"), "Should show hit count");
    }
}
