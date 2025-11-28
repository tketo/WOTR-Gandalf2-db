package wotr.combat;

/**
 * CombatRoundResult - Result of a single combat round
 * 
 * Includes leader re-roll information
 */
public class CombatRoundResult {
    private int round;
    private int fpDiceCount;
    private int fpRoll;
    private int fpModifier;
    private int fpHits;
    private int fpLeadership;
    private int shadowDiceCount;
    private int shadowRoll;
    private int shadowModifier;
    private int shadowHits;
    private int shadowLeadership;
    private boolean combatEnded;
    private String winner;
    
    // Getters and setters
    
    public int getRound() {
        return round;
    }
    
    public void setRound(int round) {
        this.round = round;
    }
    
    public int getFpDiceCount() {
        return fpDiceCount;
    }
    
    public void setFpDiceCount(int fpDiceCount) {
        this.fpDiceCount = fpDiceCount;
    }
    
    public int getFpRoll() {
        return fpRoll;
    }
    
    public void setFpRoll(int fpRoll) {
        this.fpRoll = fpRoll;
    }
    
    public int getFpModifier() {
        return fpModifier;
    }
    
    public void setFpModifier(int fpModifier) {
        this.fpModifier = fpModifier;
    }
    
    public int getFpHits() {
        return fpHits;
    }
    
    public void setFpHits(int fpHits) {
        this.fpHits = fpHits;
    }
    
    public int getFpLeadership() {
        return fpLeadership;
    }
    
    public void setFpLeadership(int fpLeadership) {
        this.fpLeadership = fpLeadership;
    }
    
    public int getShadowDiceCount() {
        return shadowDiceCount;
    }
    
    public void setShadowDiceCount(int shadowDiceCount) {
        this.shadowDiceCount = shadowDiceCount;
    }
    
    public int getShadowRoll() {
        return shadowRoll;
    }
    
    public void setShadowRoll(int shadowRoll) {
        this.shadowRoll = shadowRoll;
    }
    
    public int getShadowModifier() {
        return shadowModifier;
    }
    
    public void setShadowModifier(int shadowModifier) {
        this.shadowModifier = shadowModifier;
    }
    
    public int getShadowHits() {
        return shadowHits;
    }
    
    public void setShadowHits(int shadowHits) {
        this.shadowHits = shadowHits;
    }
    
    public int getShadowLeadership() {
        return shadowLeadership;
    }
    
    public void setShadowLeadership(int shadowLeadership) {
        this.shadowLeadership = shadowLeadership;
    }
    
    public boolean isCombatEnded() {
        return combatEnded;
    }
    
    public void setCombatEnded(boolean combatEnded) {
        this.combatEnded = combatEnded;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public void setWinner(String winner) {
        this.winner = winner;
    }
    
    /**
     * Get summary of round
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Round %d:\n", round));
        sb.append(String.format("  FP: %d dice → %d hits (+%d modifier) = %d total",
            fpDiceCount, fpRoll, fpModifier, fpHits));
        if (fpLeadership > 0) {
            sb.append(String.format(" [Leadership: %d]", fpLeadership));
        }
        sb.append("\n");
        
        sb.append(String.format("  Shadow: %d dice → %d hits (+%d modifier) = %d total",
            shadowDiceCount, shadowRoll, shadowModifier, shadowHits));
        if (shadowLeadership > 0) {
            sb.append(String.format(" [Leadership: %d]", shadowLeadership));
        }
        sb.append("\n");
        
        if (combatEnded) {
            sb.append(String.format("  Combat ended - %s wins!\n", winner));
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}
