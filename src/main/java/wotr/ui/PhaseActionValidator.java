package wotr.ui;

import java.util.HashMap;
import java.util.Map;
import wotr.turn.GamePhase;

/**
 * PhaseActionValidator - Validates which actions are allowed in each phase
 * 
 * Used to enable/disable UI controls based on current game phase
 * Enforces War of the Ring turn structure
 */
public class PhaseActionValidator {
    
    /**
     * Action types that can be validated
     */
    public enum ActionType {
        // Fellowship Phase actions
        DECLARE_FELLOWSHIP,
        HEAL_RINGBEARER,
        ACTIVATE_NATION,
        CHANGE_GUIDE,
        MOVE_FELLOWSHIP,
        
        // Dice actions
        RECOVER_DICE,
        PLACE_HUNT_DIE,
        ROLL_DICE,
        
        // Card actions
        DRAW_CHARACTER_CARD,
        DRAW_STRATEGY_CARD,
        PLAY_EVENT_CARD,
        PLAY_COMBAT_CARD,
        
        // Action Resolution actions
        USE_CHARACTER_DIE,
        USE_ARMY_DIE,
        USE_MUSTER_DIE,
        USE_EVENT_DIE,
        USE_WILL_DIE,
        
        // Movement and combat
        MOVE_ARMY,
        ATTACK_REGION,
        
        // Victory
        CHECK_VICTORY
    }
    
    private Map<GamePhase, Map<ActionType, Boolean>> validActions;
    
    /**
     * Create validator with default rules
     */
    public PhaseActionValidator() {
        initializeValidActions();
    }
    
    /**
     * Initialize which actions are valid in each phase
     */
    private void initializeValidActions() {
        validActions = new HashMap<>();
        
        // Phase 1: Recover and Draw
        Map<ActionType, Boolean> phase1 = new HashMap<>();
        phase1.put(ActionType.RECOVER_DICE, true);
        phase1.put(ActionType.DRAW_CHARACTER_CARD, true);
        phase1.put(ActionType.DRAW_STRATEGY_CARD, true);
        validActions.put(GamePhase.RECOVER_AND_DRAW, phase1);
        
        // Phase 2: Fellowship Phase
        Map<ActionType, Boolean> phase2 = new HashMap<>();
        phase2.put(ActionType.DECLARE_FELLOWSHIP, true);
        phase2.put(ActionType.HEAL_RINGBEARER, true);
        phase2.put(ActionType.ACTIVATE_NATION, true);
        phase2.put(ActionType.CHANGE_GUIDE, true);
        validActions.put(GamePhase.FELLOWSHIP_PHASE, phase2);
        
        // Phase 3: Hunt Allocation
        Map<ActionType, Boolean> phase3 = new HashMap<>();
        phase3.put(ActionType.PLACE_HUNT_DIE, true);
        validActions.put(GamePhase.HUNT_ALLOCATION, phase3);
        
        // Phase 4: Action Roll
        Map<ActionType, Boolean> phase4 = new HashMap<>();
        phase4.put(ActionType.ROLL_DICE, true);
        phase4.put(ActionType.PLACE_HUNT_DIE, true); // For Eye results
        validActions.put(GamePhase.ACTION_ROLL, phase4);
        
        // Phase 5: Action Resolution
        Map<ActionType, Boolean> phase5 = new HashMap<>();
        phase5.put(ActionType.USE_CHARACTER_DIE, true);
        phase5.put(ActionType.USE_ARMY_DIE, true);
        phase5.put(ActionType.USE_MUSTER_DIE, true);
        phase5.put(ActionType.USE_EVENT_DIE, true);
        phase5.put(ActionType.USE_WILL_DIE, true);
        phase5.put(ActionType.MOVE_FELLOWSHIP, true);
        phase5.put(ActionType.MOVE_ARMY, true);
        phase5.put(ActionType.ATTACK_REGION, true);
        phase5.put(ActionType.PLAY_EVENT_CARD, true);
        phase5.put(ActionType.PLAY_COMBAT_CARD, true);
        validActions.put(GamePhase.ACTION_RESOLUTION, phase5);
        
        // Phase 6: Victory Check
        Map<ActionType, Boolean> phase6 = new HashMap<>();
        phase6.put(ActionType.CHECK_VICTORY, true);
        validActions.put(GamePhase.VICTORY_CHECK, phase6);
    }
    
    /**
     * Check if an action is valid for the given phase
     */
    public boolean isActionValid(GamePhase phase, ActionType action) {
        Map<ActionType, Boolean> phaseActions = validActions.get(phase);
        if (phaseActions == null) {
            return false;
        }
        return phaseActions.getOrDefault(action, false);
    }
    
    /**
     * Get all valid actions for a phase
     */
    public ActionType[] getValidActions(GamePhase phase) {
        Map<ActionType, Boolean> phaseActions = validActions.get(phase);
        if (phaseActions == null) {
            return new ActionType[0];
        }
        
        return phaseActions.keySet().stream()
            .filter(action -> phaseActions.get(action))
            .toArray(ActionType[]::new);
    }
    
    /**
     * Get validation message for invalid action
     */
    public String getValidationMessage(GamePhase phase, ActionType action) {
        if (isActionValid(phase, action)) {
            return "Action is valid";
        }
        
        return String.format(
            "Action '%s' is not valid during %s. Please advance to the correct phase first.",
            action.name().replace('_', ' '),
            phase.getDisplayName()
        );
    }
    
    /**
     * Get phase where action is valid
     */
    public GamePhase getPhaseForAction(ActionType action) {
        for (Map.Entry<GamePhase, Map<ActionType, Boolean>> entry : validActions.entrySet()) {
            if (entry.getValue().getOrDefault(action, false)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Get hint for when action becomes available
     */
    public String getActionHint(GamePhase currentPhase, ActionType action) {
        if (isActionValid(currentPhase, action)) {
            return "Available now";
        }
        
        GamePhase validPhase = getPhaseForAction(action);
        if (validPhase == null) {
            return "Not available";
        }
        
        return String.format("Available in: %s", validPhase.getDisplayName());
    }
}
