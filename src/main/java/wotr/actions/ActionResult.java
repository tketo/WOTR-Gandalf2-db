package wotr.actions;

/**
 * ActionResult - Result of executing a game action
 * 
 * Encapsulates success/failure and any side effects like combat or Hunt rolls
 */
public class ActionResult {
    private boolean success;
    private String message;
    private ActionType actionType;
    
    // Combat-related fields
    private boolean combatTriggered;
    private String combatRegionId;
    private int fpCombatStrength;
    private int shadowCombatStrength;
    
    // Hunt-related fields
    private boolean huntTriggered;
    private int huntRoll;
    private int huntDiceCount;
    
    private ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.combatTriggered = false;
        this.huntTriggered = false;
    }
    
    /**
     * Create a success result
     */
    public static ActionResult success(String message) {
        return new ActionResult(true, message);
    }
    
    /**
     * Create a failure result
     */
    public static ActionResult failure(String message) {
        return new ActionResult(false, message);
    }
    
    /**
     * Create an error result
     */
    public static ActionResult error(String message) {
        return new ActionResult(false, "ERROR: " + message);
    }
    
    /**
     * Create a success result with combat triggered
     */
    public static ActionResult successWithCombat(String message, String regionId) {
        ActionResult result = new ActionResult(true, message);
        result.combatTriggered = true;
        result.combatRegionId = regionId;
        return result;
    }
    
    /**
     * Create a combat initiated result
     */
    public static ActionResult combatInitiated(String regionId, int fpStrength, int shadowStrength) {
        ActionResult result = new ActionResult(true, "Combat initiated in " + regionId);
        result.combatTriggered = true;
        result.combatRegionId = regionId;
        result.fpCombatStrength = fpStrength;
        result.shadowCombatStrength = shadowStrength;
        return result;
    }
    
    /**
     * Create a success result with Hunt triggered
     */
    public static ActionResult successWithHunt(String message, int huntRoll, int diceCount) {
        ActionResult result = new ActionResult(true, message);
        result.huntTriggered = true;
        result.huntRoll = huntRoll;
        result.huntDiceCount = diceCount;
        return result;
    }
    
    // Getters
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
    
    public boolean isCombatTriggered() {
        return combatTriggered;
    }
    
    public String getCombatRegionId() {
        return combatRegionId;
    }
    
    public int getFpCombatStrength() {
        return fpCombatStrength;
    }
    
    public int getShadowCombatStrength() {
        return shadowCombatStrength;
    }
    
    public boolean isHuntTriggered() {
        return huntTriggered;
    }
    
    public int getHuntRoll() {
        return huntRoll;
    }
    
    public int getHuntDiceCount() {
        return huntDiceCount;
    }
    
    /**
     * Get full result summary
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        
        if (combatTriggered) {
            sb.append("\n  Combat: FP=").append(fpCombatStrength)
              .append(" vs Shadow=").append(shadowCombatStrength);
        }
        
        if (huntTriggered) {
            sb.append("\n  Hunt: Roll=").append(huntRoll)
              .append(", Dice=").append(huntDiceCount);
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
    
    /**
     * Action types for categorization
     */
    public enum ActionType {
        CHARACTER_MOVE,
        ARMY_MOVE,
        MUSTER,
        EVENT_CARD,
        FELLOWSHIP_MOVE,
        COMBAT,
        PASS
    }
}
