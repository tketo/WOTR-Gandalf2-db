package wotr.rules;

/**
 * CombatResult - Outcome of a combat round
 * 
 * Tracks hits, casualties, and combat status for both sides
 */
public class CombatResult {
    private final String regionId;
    private final int round;
    
    // Attacker stats
    private final int attackerDiceRolled;
    private final int attackerHits;
    private final int attackerCasualties;
    private final boolean attackerContinues;
    
    // Defender stats  
    private final int defenderDiceRolled;
    private final int defenderHits;
    private final int defenderCasualties;
    private final boolean defenderRetreats;
    
    // Combat status
    private final boolean combatEnds;
    private final String winner; // "attacker", "defender", "ongoing", "draw"
    private final String message;
    
    private CombatResult(Builder builder) {
        this.regionId = builder.regionId;
        this.round = builder.round;
        this.attackerDiceRolled = builder.attackerDiceRolled;
        this.attackerHits = builder.attackerHits;
        this.attackerCasualties = builder.attackerCasualties;
        this.attackerContinues = builder.attackerContinues;
        this.defenderDiceRolled = builder.defenderDiceRolled;
        this.defenderHits = builder.defenderHits;
        this.defenderCasualties = builder.defenderCasualties;
        this.defenderRetreats = builder.defenderRetreats;
        this.combatEnds = builder.combatEnds;
        this.winner = builder.winner;
        this.message = builder.message;
    }
    
    // Getters
    public String getRegionId() { return regionId; }
    public int getRound() { return round; }
    public int getAttackerDiceRolled() { return attackerDiceRolled; }
    public int getAttackerHits() { return attackerHits; }
    public int getAttackerCasualties() { return attackerCasualties; }
    public boolean isAttackerContinues() { return attackerContinues; }
    public int getDefenderDiceRolled() { return defenderDiceRolled; }
    public int getDefenderHits() { return defenderHits; }
    public int getDefenderCasualties() { return defenderCasualties; }
    public boolean isDefenderRetreats() { return defenderRetreats; }
    public boolean isCombatEnds() { return combatEnds; }
    public String getWinner() { return winner; }
    public String getMessage() { return message; }
    
    /**
     * Builder for CombatResult
     */
    public static class Builder {
        private String regionId;
        private int round = 1;
        private int attackerDiceRolled = 0;
        private int attackerHits = 0;
        private int attackerCasualties = 0;
        private boolean attackerContinues = false;
        private int defenderDiceRolled = 0;
        private int defenderHits = 0;
        private int defenderCasualties = 0;
        private boolean defenderRetreats = false;
        private boolean combatEnds = false;
        private String winner = "ongoing";
        private String message = "";
        
        public Builder(String regionId, int round) {
            this.regionId = regionId;
            this.round = round;
        }
        
        public Builder attackerDice(int rolled, int hits) {
            this.attackerDiceRolled = rolled;
            this.attackerHits = hits;
            return this;
        }
        
        public Builder attackerCasualties(int casualties) {
            this.attackerCasualties = casualties;
            return this;
        }
        
        public Builder attackerContinues(boolean continues) {
            this.attackerContinues = continues;
            return this;
        }
        
        public Builder defenderDice(int rolled, int hits) {
            this.defenderDiceRolled = rolled;
            this.defenderHits = hits;
            return this;
        }
        
        public Builder defenderCasualties(int casualties) {
            this.defenderCasualties = casualties;
            return this;
        }
        
        public Builder defenderRetreats(boolean retreats) {
            this.defenderRetreats = retreats;
            return this;
        }
        
        public Builder combatEnds(boolean ends, String winner) {
            this.combatEnds = ends;
            this.winner = winner;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public CombatResult build() {
            return new CombatResult(this);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Round ").append(round).append(":\n");
        sb.append("  Attacker: ").append(attackerDiceRolled).append(" dice → ")
          .append(attackerHits).append(" hits, ")
          .append(attackerCasualties).append(" casualties\n");
        sb.append("  Defender: ").append(defenderDiceRolled).append(" dice → ")
          .append(defenderHits).append(" hits, ")
          .append(defenderCasualties).append(" casualties\n");
        
        if (combatEnds) {
            sb.append("  Combat ends: ").append(winner).append(" wins\n");
        } else if (defenderRetreats) {
            sb.append("  Defender retreats\n");
        } else if (attackerContinues) {
            sb.append("  Attacker continues to next round\n");
        } else {
            sb.append("  Combat ends\n");
        }
        
        if (!message.isEmpty()) {
            sb.append("  ").append(message).append("\n");
        }
        
        return sb.toString();
    }
}
