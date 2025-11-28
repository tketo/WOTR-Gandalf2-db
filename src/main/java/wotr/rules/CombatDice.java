package wotr.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * CombatDice - Handle combat dice rolling and hit calculation
 * 
 * Based on War of the Ring rules:
 * - Roll up to 5 dice (min of Combat Strength or unit count)
 * - Natural 1 always misses, natural 6 always hits
 * - Standard: Hit on 5+
 * - City/Fortification (first round): Attacker hits on 6+, defender on 5+
 * - Stronghold (siege): Attacker hits on 6+, defender on 5+
 * - Leader re-roll: Re-roll failed dice up to Leadership value
 */
public class CombatDice {
    
    /**
     * Roll combat dice
     * 
     * @param diceCount Number of dice to roll (max 5)
     * @param targetNumber Number needed to hit (5 or 6)
     * @return List of die results
     */
    public static List<Integer> rollDice(int diceCount) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < Math.min(diceCount, 5); i++) {
            results.add(rollD6());
        }
        return results;
    }
    
    /**
     * Count hits from dice results
     * 
     * @param diceResults List of die results (1-6)
     * @param targetNumber Number needed to hit (5 or 6)
     * @return Number of hits
     */
    public static int countHits(List<Integer> diceResults, int targetNumber) {
        int hits = 0;
        for (int result : diceResults) {
            if (result == 1) {
                // Natural 1 always misses
                continue;
            } else if (result == 6) {
                // Natural 6 always hits
                hits++;
            } else if (result >= targetNumber) {
                // Normal hit
                hits++;
            }
        }
        return hits;
    }
    
    /**
     * Re-roll failed dice (leader ability)
     * 
     * @param diceResults Original dice results
     * @param targetNumber Number needed to hit
     * @param leadership Leadership value (max re-rolls)
     * @return New list with re-rolled dice
     */
    public static List<Integer> rerollFailed(List<Integer> diceResults, int targetNumber, int leadership) {
        List<Integer> newResults = new ArrayList<>(diceResults);
        int rerollsUsed = 0;
        
        for (int i = 0; i < newResults.size() && rerollsUsed < leadership; i++) {
            int result = newResults.get(i);
            
            // Check if this die failed (not a natural 6, and less than target)
            if (result != 6 && result < targetNumber) {
                newResults.set(i, rollD6());
                rerollsUsed++;
            }
        }
        
        return newResults;
    }
    
    /**
     * Determine number of dice to roll
     * 
     * Dice = min(Combat Strength, Unit Count, 5)
     * 
     * @param combatStrength Total combat strength
     * @param unitCount Number of units in army
     * @return Number of dice to roll (max 5)
     */
    public static int determineDiceCount(int combatStrength, int unitCount) {
        return Math.min(Math.min(combatStrength, unitCount), 5);
    }
    
    /**
     * Get target number for hitting
     * 
     * @param isAttacker Is this the attacking side?
     * @param terrainType Terrain type ("field", "city", "fortification", "stronghold")
     * @param isFirstRound Is this the first combat round?
     * @param isSiege Is this a siege battle?
     * @return Target number (5 or 6)
     */
    public static int getTargetNumber(boolean isAttacker, String terrainType, 
                                      boolean isFirstRound, boolean isSiege) {
        // Siege rules
        if (isSiege) {
            return isAttacker ? 6 : 5;
        }
        
        // Fortification/City rules (first round only)
        if (isFirstRound && (terrainType.equals("city") || terrainType.equals("fortification"))) {
            return isAttacker ? 6 : 5;
        }
        
        // Standard field battle or subsequent rounds
        return 5;
    }
    
    /**
     * Roll a single d6
     */
    private static int rollD6() {
        return (int) (Math.random() * 6) + 1;
    }
    
    /**
     * Format dice results for display
     */
    public static String formatDiceResults(List<Integer> results, int hits) {
        StringBuilder sb = new StringBuilder();
        sb.append("Rolled: [");
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(results.get(i));
        }
        sb.append("] → ").append(hits).append(" hit");
        if (hits != 1) sb.append("s");
        return sb.toString();
    }
}
