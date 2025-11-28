package wotr.turn;

/**
 * GamePhase - The 6 official phases of each game turn
 * 
 * Based on official War of the Ring rules:
 * Each turn progresses through these phases in order
 */
public enum GamePhase {
    /**
     * Phase 1: Recover Action Dice and Draw Event Cards
     * - Recover used Action Dice
     * - Each player draws 1 Character card and 1 Strategy card
     */
    RECOVER_AND_DRAW("Recover Action Dice and Draw Event Cards"),
    
    /**
     * Phase 2: Fellowship Phase
     * Free Peoples player may:
     * - Declare Fellowship position
     * - Heal Ring-bearers (remove 1 Corruption in FP City/Stronghold)
     * - Activate a Nation
     * - Change Fellowship Guide
     */
    FELLOWSHIP_PHASE("Fellowship Phase"),
    
    /**
     * Phase 3: Hunt Allocation
     * Shadow player places Action Dice in Hunt Box:
     * - Minimum: 1 die if FP retrieved any last turn
     * - Maximum: Number of Companions in Fellowship (min 1)
     */
    HUNT_ALLOCATION("Hunt Allocation"),
    
    /**
     * Phase 4: Action Roll
     * - Roll Action Dice (except those in Hunt Box)
     * - Shadow places all "Eye" results in Hunt Box
     */
    ACTION_ROLL("Action Roll"),
    
    /**
     * Phase 5: Action Resolution
     * Players alternate actions using one Action Die result each:
     * - Free Peoples goes first
     * - FP places Fellowship movement dice in Hunt Box
     * - Continue until all dice used
     */
    ACTION_RESOLUTION("Action Resolution"),
    
    /**
     * Phase 6: Victory Check
     * - Check Military Victory conditions
     * - If no victory, start new turn
     * Note: Ring victories end game immediately, bypass this phase
     */
    VICTORY_CHECK("Victory Check");
    
    private final String displayName;
    
    GamePhase(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Get user-friendly phase name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the next phase in sequence
     * After VICTORY_CHECK, returns RECOVER_AND_DRAW for new turn
     */
    public GamePhase next() {
        switch (this) {
            case RECOVER_AND_DRAW:
                return FELLOWSHIP_PHASE;
            case FELLOWSHIP_PHASE:
                return HUNT_ALLOCATION;
            case HUNT_ALLOCATION:
                return ACTION_ROLL;
            case ACTION_ROLL:
                return ACTION_RESOLUTION;
            case ACTION_RESOLUTION:
                return VICTORY_CHECK;
            case VICTORY_CHECK:
                return RECOVER_AND_DRAW; // New turn
            default:
                throw new IllegalStateException("Unknown phase: " + this);
        }
    }
    
    /**
     * Is this the last phase of the turn?
     */
    public boolean isLastPhase() {
        return this == VICTORY_CHECK;
    }
    
    /**
     * Is this the first phase of the turn?
     */
    public boolean isFirstPhase() {
        return this == RECOVER_AND_DRAW;
    }
    
    /**
     * Get phase description for help text
     */
    public String getDescription() {
        switch (this) {
            case RECOVER_AND_DRAW:
                return "Recover used Action Dice and draw 1 Character + 1 Strategy card";
            case FELLOWSHIP_PHASE:
                return "Free Peoples may declare Fellowship, heal, activate nation, or change guide";
            case HUNT_ALLOCATION:
                return "Shadow player places dice in Hunt Box (1 min, Fellowship size max)";
            case ACTION_ROLL:
                return "Roll Action Dice, Shadow places Eye results in Hunt Box";
            case ACTION_RESOLUTION:
                return "Players alternate using Action Dice, FP goes first";
            case VICTORY_CHECK:
                return "Check for Military Victory, then start new turn";
            default:
                return "";
        }
    }
    
    /**
     * Which player is active in this phase?
     */
    public String getActivePlayer() {
        switch (this) {
            case RECOVER_AND_DRAW:
                return "both"; // Both players act
            case FELLOWSHIP_PHASE:
                return "free_peoples";
            case HUNT_ALLOCATION:
                return "shadow";
            case ACTION_ROLL:
                return "both";
            case ACTION_RESOLUTION:
                return "both"; // Alternating
            case VICTORY_CHECK:
                return "both";
            default:
                return "both";
        }
    }
}
