package wotr.fellowship;

import wotr.services.GameStateService;
import wotr.dice.DicePool;
import java.sql.SQLException;
import java.util.Random;

/**
 * HuntMechanic - Implements Fellowship Hunt system
 * 
 * Based on War of the Ring rules:
 * - Hunt occurs when Fellowship moves
 * - Roll 1d6 + number of Hunt dice
 * - Result determines if Fellowship is revealed, damaged, or moves safely
 */
public class HuntMechanic {
    private GameStateService gameState;
    private DicePool dicePool;
    private Random random;
    
    public HuntMechanic(GameStateService gameState, DicePool dicePool) {
        this.gameState = gameState;
        this.dicePool = dicePool;
        this.random = new Random();
    }
    
    /**
     * Execute Hunt roll when Fellowship moves
     * 
     * @return HuntResult with outcome
     */
    public HuntResult executeHunt() throws SQLException {
        // Roll base die (1-6)
        int baseDieRoll = random.nextInt(6) + 1;
        
        // Add Hunt dice count
        int huntDiceCount = dicePool.getHuntBoxCount();
        
        // Get terrain modifier from current region
        String currentRegion = gameState.getFellowshipLocation();
        int terrainModifier = getTerrainModifier(currentRegion);
        
        // Calculate total
        int totalRoll = baseDieRoll + huntDiceCount + terrainModifier;
        
        // Determine result based on total
        HuntResult result = new HuntResult();
        result.setBaseDieRoll(baseDieRoll);
        result.setHuntDiceCount(huntDiceCount);
        result.setTerrainModifier(terrainModifier);
        result.setTotalRoll(totalRoll);
        
        // Interpret result
        if (totalRoll <= 3) {
            // Safe - no effect
            result.setOutcome(HuntOutcome.SAFE);
            result.setMessage("Fellowship moves safely (Roll: " + totalRoll + ")");
        } else if (totalRoll <= 6) {
            // Revealed but no damage
            result.setOutcome(HuntOutcome.REVEALED);
            result.setMessage("Fellowship is revealed! (Roll: " + totalRoll + ")");
            gameState.revealFellowship();
        } else if (totalRoll <= 9) {
            // Revealed + 1 corruption
            result.setOutcome(HuntOutcome.DAMAGE);
            result.setMessage("Fellowship revealed and gains 1 corruption (Roll: " + totalRoll + ")");
            gameState.revealFellowship();
            gameState.addCorruption(1);
        } else {
            // Revealed + 2 corruption
            result.setOutcome(HuntOutcome.HEAVY_DAMAGE);
            result.setMessage("Fellowship revealed and gains 2 corruption! (Roll: " + totalRoll + ")");
            gameState.revealFellowship();
            gameState.addCorruption(2);
        }
        
        return result;
    }
    
    /**
     * Get terrain modifier for Hunt rolls
     */
    private int getTerrainModifier(String regionId) {
        // TODO: Query region terrain type from database
        // For now, return 0
        // Mountains/wilderness = -1
        // Open terrain = 0
        // Roads/controlled regions = +1
        return 0;
    }
    
    /**
     * Can Fellowship hide (after being revealed)?
     */
    public boolean canHide() throws SQLException {
        // Fellowship can hide if in FP-controlled stronghold or city
        String region = gameState.getFellowshipLocation();
        String control = gameState.getRegionControl(region);
        
        // TODO: Check if region is stronghold or city
        return "free_peoples".equals(control);
    }
    
    /**
     * Hide the Fellowship (after being revealed)
     */
    public void hideFellowship() throws SQLException {
        gameState.hideFellowship();
    }
    
    /**
     * Hunt result container
     */
    public static class HuntResult {
        private int baseDieRoll;
        private int huntDiceCount;
        private int terrainModifier;
        private int totalRoll;
        private HuntOutcome outcome;
        private String message;
        
        public int getBaseDieRoll() { return baseDieRoll; }
        public void setBaseDieRoll(int roll) { this.baseDieRoll = roll; }
        
        public int getHuntDiceCount() { return huntDiceCount; }
        public void setHuntDiceCount(int count) { this.huntDiceCount = count; }
        
        public int getTerrainModifier() { return terrainModifier; }
        public void setTerrainModifier(int modifier) { this.terrainModifier = modifier; }
        
        public int getTotalRoll() { return totalRoll; }
        public void setTotalRoll(int total) { this.totalRoll = total; }
        
        public HuntOutcome getOutcome() { return outcome; }
        public void setOutcome(HuntOutcome outcome) { this.outcome = outcome; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        @Override
        public String toString() {
            return String.format("Hunt Roll: %d (base) + %d (hunt dice) + %d (terrain) = %d → %s",
                baseDieRoll, huntDiceCount, terrainModifier, totalRoll, outcome);
        }
    }
    
    /**
     * Possible Hunt outcomes
     */
    public enum HuntOutcome {
        SAFE,           // No effect
        REVEALED,       // Fellowship revealed
        DAMAGE,         // Revealed + 1 corruption
        HEAVY_DAMAGE    // Revealed + 2 corruption
    }
}
