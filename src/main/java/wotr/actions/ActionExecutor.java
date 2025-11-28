package wotr.actions;

import wotr.services.GameStateService;
import wotr.rules.RulesEngine;
import wotr.rules.ValidationResult;
import wotr.dice.DicePool;
import wotr.dice.ActionDie;
import wotr.dice.DieType;
import wotr.cards.CardManager;
import java.sql.SQLException;

/**
 * ActionExecutor - Central system for executing game actions
 * 
 * Coordinates die usage, movement, mustering, card effects, and combat
 * Validates actions using RulesEngine before execution
 * Updates game state via GameStateService
 */
public class ActionExecutor {
    private GameStateService gameState;
    private RulesEngine rules;
    private DicePool dicePool;
    private CardManager cardManager;
    
    public ActionExecutor(GameStateService gameState, DicePool dicePool, CardManager cardManager) {
        this.gameState = gameState;
        this.rules = new RulesEngine(gameState);
        this.dicePool = dicePool;
        this.cardManager = cardManager;
    }
    
    /**
     * Execute a Character die action
     * Allows: Character movement, Guide change, Separate companions
     */
    public ActionResult executeCharacterAction(String characterId, String targetRegionId, ActionDie die) {
        try {
            // Validate die is Character type and available
            if (die.getResult() != DieType.CHARACTER) {
                return ActionResult.failure("This die is not a Character die");
            }
            
            // Validate character movement
            ValidationResult validation = rules.validateCharacterMove(characterId, targetRegionId);
            if (!validation.isValid()) {
                return ActionResult.failure(validation.getMessage());
            }
            
            // Execute movement
            gameState.moveCharacter(characterId, targetRegionId);
            
            // Use the die
            dicePool.useDie(die.getId());
            
            return ActionResult.success("Character moved to " + targetRegionId);
            
        } catch (SQLException e) {
            return ActionResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Execute an Army die action
     * Allows: Army movement, Attack (move into enemy region)
     */
    public ActionResult executeArmyAction(String fromRegionId, String toRegionId, 
                                         int nationId, int regularCount, int eliteCount, ActionDie die) {
        try {
            // Validate die is Army type and available
            if (die.getResult() != DieType.ARMY) {
                return ActionResult.failure("This die is not an Army die");
            }
            
            // Validate army movement
            ValidationResult validation = rules.validateArmyMove(
                fromRegionId, toRegionId, nationId, regularCount, eliteCount
            );
            if (!validation.isValid()) {
                return ActionResult.failure(validation.getMessage());
            }
            
            // Execute movement
            gameState.moveArmy(fromRegionId, toRegionId, nationId, regularCount, eliteCount);
            
            // Use the die
            dicePool.useDie(die.getId());
            
            // Check if this creates a combat situation
            ValidationResult combatCheck = rules.validateCombat(toRegionId);
            if (combatCheck.isValid()) {
                return ActionResult.successWithCombat(
                    "Army moved to " + toRegionId + " - Combat initiated!",
                    toRegionId
                );
            }
            
            return ActionResult.success("Army moved to " + toRegionId);
            
        } catch (SQLException e) {
            return ActionResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Execute a Muster die action
     * Allows: Recruit units, Bring character into play, Advance political track
     */
    public ActionResult executeMusterAction(String regionId, int nationId, 
                                           String unitType, int count, ActionDie die) {
        try {
            // Validate die is Muster type and available
            if (die.getResult() != DieType.MUSTER) {
                return ActionResult.failure("This die is not a Muster die");
            }
            
            // TODO: Add muster validation rules
            // - Region must be controlled by nation
            // - Must be stronghold or city for recruiting
            // - Nation must be active
            
            // Execute muster
            gameState.addUnits(regionId, nationId, unitType, count);
            
            // Use the die
            dicePool.useDie(die.getId());
            
            return ActionResult.success("Mustered " + count + " " + unitType + " units in " + regionId);
            
        } catch (SQLException e) {
            return ActionResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Execute an Event die action
     * Allows: Play an event card
     */
    public ActionResult executeEventAction(String faction, String cardId, ActionDie die) {
        try {
            // Validate die is Event type
            if (die.getResult() != DieType.EVENT) {
                return ActionResult.failure("This die is not an Event die");
            }
            
            // Play the card (CardManager validates die requirement)
            boolean played = cardManager.playCard(faction, cardId, "event");
            if (!played) {
                return ActionResult.failure("Cannot play card - check requirements");
            }
            
            // Use the die
            dicePool.useDie(die.getId());
            
            // TODO: Execute card effect
            return ActionResult.success("Played event card: " + cardId);
            
        } catch (Exception e) {
            return ActionResult.error("Error playing card: " + e.getMessage());
        }
    }
    
    /**
     * Execute a Will of the West die action (Free Peoples only)
     * Allows: Advance political track, Play any event card
     */
    public ActionResult executeWillAction(String faction, String cardId, ActionDie die) {
        try {
            // Validate die is Will of West type
            if (die.getResult() != DieType.WILL_OF_WEST) {
                return ActionResult.failure("This die is not a Will of the West die");
            }
            
            // Will of West can play any FP card
            if (cardId != null && !cardId.isEmpty()) {
                boolean played = cardManager.playCard(faction, cardId, "event");
                if (!played) {
                    return ActionResult.failure("Cannot play card");
                }
            }
            
            // Use the die
            dicePool.useDie(die.getId());
            
            return ActionResult.success("Used Will of the West die");
            
        } catch (Exception e) {
            return ActionResult.error("Error: " + e.getMessage());
        }
    }
    
    /**
     * Execute Fellowship movement with Hunt
     * Fellowship movement dice go to Hunt Box
     */
    public ActionResult executeFellowshipMove(String toRegionId, ActionDie die) {
        try {
            // Validate movement (Fellowship uses character movement rules)
            // TODO: Add Fellowship-specific validation
            
            // Move Fellowship
            gameState.moveFellowship(toRegionId);
            
            // Place die in Hunt Box (Fellowship movement triggers Hunt)
            dicePool.placeInHuntBox(die);
            
            // Roll Hunt dice
            int huntRoll = rollHunt();
            
            return ActionResult.successWithHunt(
                "Fellowship moved to " + toRegionId,
                huntRoll,
                dicePool.getHuntBoxCount()
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Roll Hunt dice
     */
    private int rollHunt() {
        // TODO: Implement proper Hunt roll with modifiers
        // - Base roll (1-6)
        // - Add Hunt dice count
        // - Add terrain modifier
        // - Check for success/failure
        return (int) (Math.random() * 6) + 1;
    }
    
    /**
     * Initiate combat in a region
     */
    public ActionResult initiateCombat(String regionId) {
        try {
            // Validate combat can occur
            ValidationResult validation = rules.validateCombat(regionId);
            if (!validation.isValid()) {
                return ActionResult.failure(validation.getMessage());
            }
            
            // Get combat strength for both sides
            int fpStrength = rules.calculateCombatStrength(regionId, "free_peoples");
            int shadowStrength = rules.calculateCombatStrength(regionId, "shadow");
            
            return ActionResult.combatInitiated(regionId, fpStrength, shadowStrength);
            
        } catch (Exception e) {
            return ActionResult.error("Error initiating combat: " + e.getMessage());
        }
    }
    
    /**
     * Pass action (skip using a die)
     */
    public ActionResult executePass(String faction) {
        return ActionResult.success(faction + " passed their action");
    }
    
    /**
     * Get available actions for a die type
     */
    public String[] getAvailableActionsForDie(DieType dieType) {
        switch (dieType) {
            case CHARACTER:
                return new String[]{
                    "Move Character",
                    "Change Guide",
                    "Separate Companion"
                };
            case ARMY:
                return new String[]{
                    "Move Army",
                    "Attack Region"
                };
            case MUSTER:
                return new String[]{
                    "Recruit Units",
                    "Bring Character into Play",
                    "Advance Political Track"
                };
            case EVENT:
                return new String[]{
                    "Play Event Card"
                };
            case WILL_OF_WEST:
                return new String[]{
                    "Play Any Event Card",
                    "Advance Political Track"
                };
            case EYE:
                return new String[]{
                    "Hunt Allocation (automatic)"
                };
            default:
                return new String[0];
        }
    }
    
    /**
     * Get rules engine
     */
    public RulesEngine getRules() {
        return rules;
    }
    
    /**
     * Get dice pool
     */
    public DicePool getDicePool() {
        return dicePool;
    }
    
    /**
     * Get card manager
     */
    public CardManager getCardManager() {
        return cardManager;
    }
}
