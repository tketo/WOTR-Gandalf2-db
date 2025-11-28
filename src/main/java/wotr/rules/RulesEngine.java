package wotr.rules;

import wotr.services.*;
import wotr.cards.CardEffectExecutor;
import wotr.cards.EventCard;
import wotr.dice.DicePool;
import wotr.actions.ActionResult;
import java.sql.SQLException;

/**
 * RulesEngine - Central rules validation system
 * 
 * Core concept: Rules are database queries that check current board state.
 * Instead of complex in-memory state tracking, we query the database like
 * looking at a physical board.
 * 
 * Example: "Can Strider move to Minas Tirith?"
 *   1. Query: Where is Strider? (database)
 *   2. Query: Is that region adjacent to Minas Tirith? (database)
 *   3. Query: Is the path controlled by enemies? (database)
 *   4. Return: true/false
 */
public class RulesEngine {
    private GameStateService gameState;
    private GameDataService gameData;
    private MovementRules movement;
    private CombatRules combat;
    private VictoryRules victory;
    private CardEffectExecutor cardExecutor;
    
    public RulesEngine(GameStateService gameState) {
        this(gameState, null);
    }
    
    public RulesEngine(GameStateService gameState, DicePool dicePool) {
        this.gameState = gameState;
        this.gameData = GameDataService.getInstance();
        
        // Initialize rule modules
        this.movement = new MovementRules(gameState, gameData);
        this.combat = new CombatRules(gameState, gameData);
        this.victory = new VictoryRules(gameState, gameData);
        
        // Initialize card executor if dice pool provided
        if (dicePool != null) {
            this.cardExecutor = new CardEffectExecutor(gameState, dicePool);
        }
    }
    
    // ===== MOVEMENT VALIDATION =====
    
    /**
     * Can this character move to the target region?
     */
    public ValidationResult validateCharacterMove(String characterId, String toRegionId) {
        try {
            return movement.validateCharacterMove(characterId, toRegionId);
        } catch (SQLException e) {
            return ValidationResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Can this army move to the target region?
     */
    public ValidationResult validateArmyMove(String fromRegionId, String toRegionId, 
                                             int nationId, int regularCount, int eliteCount) {
        try {
            return movement.validateArmyMove(fromRegionId, toRegionId, nationId, 
                                            regularCount, eliteCount);
        } catch (SQLException e) {
            return ValidationResult.error("Database error: " + e.getMessage());
        }
    }
    
    // ===== COMBAT VALIDATION =====
    
    /**
     * Can combat occur in this region?
     */
    public ValidationResult validateCombat(String regionId) {
        try {
            return combat.validateCombat(regionId);
        } catch (SQLException e) {
            return ValidationResult.error("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Calculate combat strength for a side
     */
    public int calculateCombatStrength(String regionId, String side) {
        try {
            return combat.calculateCombatStrength(regionId, side);
        } catch (SQLException e) {
            System.err.println("Error calculating combat strength: " + e.getMessage());
            return 0;
        }
    }
    
    // ===== VICTORY CONDITIONS =====
    
    /**
     * Check if game is over and who won
     */
    public VictoryResult checkVictory() {
        try {
            return victory.checkVictory();
        } catch (SQLException e) {
            System.err.println("Error checking victory: " + e.getMessage());
            return VictoryResult.noVictory();
        }
    }
    
    /**
     * Check if victory points trigger a win
     */
    public boolean checkVictoryPoints() {
        try {
            return victory.checkVictoryPoints();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // ===== CARD EFFECTS =====
    
    /**
     * Validate if card effect can be executed
     * Checks prerequisites, targets, and game state
     */
    public ValidationResult validateCardEffect(EventCard card, String targetRegion, String targetCharacter) {
        if (cardExecutor == null) {
            return ValidationResult.failure("Card executor not initialized");
        }
        
        try {
            // Check basic card requirements
            if (card == null) {
                return ValidationResult.failure("No card specified");
            }
            
            // Check if card can be executed
            if (!cardExecutor.canExecuteCard(card, targetRegion, targetCharacter)) {
                return ValidationResult.failure("Card cannot be played at this time");
            }
            
            // Validate targets if specified
            if (targetRegion != null && !isValidRegion(targetRegion)) {
                return ValidationResult.failure("Invalid target region");
            }
            
            if (targetCharacter != null && !isValidCharacter(targetCharacter)) {
                return ValidationResult.failure("Invalid target character");
            }
            
            return ValidationResult.success();
            
        } catch (Exception e) {
            return ValidationResult.error("Error validating card: " + e.getMessage());
        }
    }
    
    /**
     * Execute card effect through rules engine
     * Validates and applies the card effect to game state
     */
    public ActionResult executeCardEffect(EventCard card, String targetRegion, String targetCharacter) {
        if (cardExecutor == null) {
            return ActionResult.error("Card executor not initialized");
        }
        
        // Validate first
        ValidationResult validation = validateCardEffect(card, targetRegion, targetCharacter);
        if (!validation.isValid()) {
            return ActionResult.failure(validation.getMessage());
        }
        
        // Execute the effect
        return cardExecutor.executeCardEffect(card, targetRegion, targetCharacter);
    }
    
    /**
     * Get card effect description for UI display
     */
    public String getCardEffectDescription(EventCard card) {
        if (cardExecutor == null) {
            return card.getEventText();
        }
        return cardExecutor.getEffectDescription(card);
    }
    
    // ===== VALIDATION HELPERS =====
    
    /**
     * Check if region ID is valid
     */
    private boolean isValidRegion(String regionId) {
        try {
            return gameData.getRegion(regionId) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if character ID is valid and in play
     */
    private boolean isValidCharacter(String characterId) {
        try {
            return gameState.isCharacterInPlay(characterId);
        } catch (SQLException e) {
            return false;
        }
    }
    
    // ===== UTILITY =====
    
    /**
     * Get reference to movement rules
     */
    public MovementRules getMovementRules() {
        return movement;
    }
    
    /**
     * Get reference to combat rules
     */
    public CombatRules getCombatRules() {
        return combat;
    }
    
    /**
     * Get reference to victory rules
     */
    public VictoryRules getVictoryRules() {
        return victory;
    }
    
    /**
     * Get reference to card executor
     */
    public CardEffectExecutor getCardExecutor() {
        return cardExecutor;
    }
}
