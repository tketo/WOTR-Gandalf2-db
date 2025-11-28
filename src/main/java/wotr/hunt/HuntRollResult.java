package wotr.hunt;

import java.util.ArrayList;
import java.util.List;

/**
 * HuntRollResult - Result of Hunt dice roll (before drawing tiles)
 */
public class HuntRollResult {
    private int huntLevel;              // Number of Shadow dice
    private int fpDiceModifier;         // Number of FP dice (+1 each)
    private List<Integer> rolls;        // Individual die rolls
    private int successCount;           // Number of successes (6+)
    private int reRollsAvailable;       // Based on region conditions
    
    public HuntRollResult() {
        this.rolls = new ArrayList<>();
    }
    
    public int getHuntLevel() {
        return huntLevel;
    }
    
    public void setHuntLevel(int level) {
        this.huntLevel = level;
    }
    
    public int getFpDiceModifier() {
        return fpDiceModifier;
    }
    
    public void setFpDiceModifier(int modifier) {
        this.fpDiceModifier = modifier;
    }
    
    public List<Integer> getRolls() {
        return rolls;
    }
    
    public void setRolls(List<Integer> rolls) {
        this.rolls = rolls;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(int count) {
        this.successCount = count;
    }
    
    public int getReRollsAvailable() {
        return reRollsAvailable;
    }
    
    public void setReRollsAvailable(int reRolls) {
        this.reRollsAvailable = reRolls;
    }
    
    @Override
    public String toString() {
        return String.format("Hunt Roll: Level=%d, FP+%d, Rolls=%s, Successes=%d, ReRolls=%d",
            huntLevel, fpDiceModifier, rolls, successCount, reRollsAvailable);
    }
}
