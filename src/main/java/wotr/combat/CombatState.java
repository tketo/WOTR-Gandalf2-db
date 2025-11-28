package wotr.combat;

/**
 * CombatState - Tracks the current state of an ongoing combat
 */
public class CombatState {
    private String regionId;
    private int round;
    private String attacker;
    private String defender;
    private int fpStrength;
    private int shadowStrength;
    private boolean combatEnded;
    private String winner;
    
    // Getters and setters
    
    public String getRegionId() {
        return regionId;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    
    public int getRound() {
        return round;
    }
    
    public void setRound(int round) {
        this.round = round;
    }
    
    public String getAttacker() {
        return attacker;
    }
    
    public void setAttacker(String attacker) {
        this.attacker = attacker;
    }
    
    public String getDefender() {
        return defender;
    }
    
    public void setDefender(String defender) {
        this.defender = defender;
    }
    
    public int getFpStrength() {
        return fpStrength;
    }
    
    public void setFpStrength(int fpStrength) {
        this.fpStrength = fpStrength;
    }
    
    public int getShadowStrength() {
        return shadowStrength;
    }
    
    public void setShadowStrength(int shadowStrength) {
        this.shadowStrength = shadowStrength;
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
    
    @Override
    public String toString() {
        return String.format("Combat in %s (Round %d): FP=%d vs Shadow=%d [%s attacking]",
            regionId, round, fpStrength, shadowStrength, attacker);
    }
}
