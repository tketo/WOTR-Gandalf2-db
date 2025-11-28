package wotr.turn;

import wotr.services.GameStateService;
import wotr.rules.RulesEngine;
import wotr.dice.DicePool;
import wotr.dice.ActionDie;
import wotr.dice.DieType;
import wotr.actions.ActionExecutor;
import wotr.actions.ActionResult;
import wotr.cards.CardManager;
import java.sql.SQLException;
import java.util.List;

/**
 * TurnManager - Manages turn progression and phase transitions
 * 
 * State machine that ensures game flows through phases correctly:
 * 1. Recover & Draw
 * 2. Fellowship Phase
 * 3. Hunt Allocation
 * 4. Action Roll
 * 5. Action Resolution
 * 6. Victory Check → back to 1
 */
public class TurnManager {
    private GameStateService gameState;
    private RulesEngine rules;
    private DicePool dicePool;
    private CardManager cardManager;
    private ActionExecutor actionExecutor;
    private GamePhase currentPhase;
    private int currentTurn;
    private boolean phaseComplete;
    
    public TurnManager(GameStateService gameState) {
        this.gameState = gameState;
        this.rules = new RulesEngine(gameState);
        
        // Get actual game ID from GameStateService
        String gameId = gameState.getCurrentGameId();
        if (gameId == null) {
            throw new IllegalStateException("No active game loaded. Call createNewGame() or loadGame() first.");
        }
        
        this.dicePool = new DicePool(gameId);
        this.cardManager = new CardManager();
        this.actionExecutor = new ActionExecutor(gameState, dicePool, cardManager);
        this.currentPhase = GamePhase.RECOVER_AND_DRAW;
        this.currentTurn = 1;
        this.phaseComplete = false;
        
        // Try to load existing game state from database
        try {
            loadGameState();
        } catch (SQLException e) {
            System.err.println("Warning: Could not load game state, using defaults: " + e.getMessage());
            // Fall back to defaults already set above
        }
    }
    
    /**
     * Load existing game state
     */
    public void loadGameState() throws SQLException {
        currentTurn = gameState.getCurrentTurn();
        String phaseName = gameState.getCurrentPhase();
        currentPhase = parsePhase(phaseName);
        phaseComplete = false;
        
        // Load dice from database
        dicePool.loadFromDatabase(getGameStateDAO());
    }
    
    /**
     * Get GameStateDAO for dice loading
     */
    private wotr.dao.GameStateDAO getGameStateDAO() {
        // Access the DAO from GameStateService
        try {
            java.lang.reflect.Field field = gameState.getClass().getDeclaredField("gameStateDAO");
            field.setAccessible(true);
            return (wotr.dao.GameStateDAO) field.get(gameState);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access GameStateDAO", e);
        }
    }
    
    /**
     * Advance to next phase
     * 
     * Validates that current phase is complete before advancing
     */
    public boolean advancePhase() throws SQLException {
        if (!canAdvancePhase()) {
            return false;
        }
        
        GamePhase nextPhase = currentPhase.next();
        
        // If we just finished Victory Check, increment turn
        if (currentPhase == GamePhase.VICTORY_CHECK) {
            currentTurn++;
            System.out.println("=== Starting Turn " + currentTurn + " ===");
        }
        
        currentPhase = nextPhase;
        phaseComplete = false;
        
        // Update database
        gameState.advancePhase(currentPhase.name().toLowerCase());
        if (currentPhase.isFirstPhase()) {
            gameState.nextTurn();
        }
        
        System.out.println("Advanced to: " + currentPhase.getDisplayName());
        return true;
    }
    
    /**
     * Can we advance to the next phase?
     */
    public boolean canAdvancePhase() {
        // Phase-specific completion checks
        switch (currentPhase) {
            case RECOVER_AND_DRAW:
                // Complete when dice recovered and cards drawn
                return phaseComplete;
                
            case FELLOWSHIP_PHASE:
                // Complete when FP player says so (or passes)
                return phaseComplete;
                
            case HUNT_ALLOCATION:
                // Complete when Shadow places dice in Hunt Box
                return phaseComplete;
                
            case ACTION_ROLL:
                // Complete when dice are rolled
                return phaseComplete;
                
            case ACTION_RESOLUTION:
                // Complete when all Action Dice are used
                return phaseComplete;
                
            case VICTORY_CHECK:
                // Complete after checking victory
                return phaseComplete;
                
            default:
                return false;
        }
    }
    
    /**
     * Mark current phase as complete
     */
    public void markPhaseComplete() {
        this.phaseComplete = true;
        System.out.println("Phase complete: " + currentPhase.getDisplayName());
    }
    
    /**
     * Get current phase
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * Get current turn number
     */
    public int getCurrentTurn() {
        return currentTurn;
    }
    
    /**
     * Is the current phase complete?
     */
    public boolean isPhaseComplete() {
        return phaseComplete;
    }
    
    /**
     * Get dice pool
     */
    public DicePool getDicePool() {
        return dicePool;
    }
    
    /**
     * Get available actions for current phase
     */
    public String[] getAvailableActions() {
        switch (currentPhase) {
            case RECOVER_AND_DRAW:
                return new String[]{
                    "Recover Action Dice",
                    "Draw Character Card",
                    "Draw Strategy Card"
                };
                
            case FELLOWSHIP_PHASE:
                return new String[]{
                    "Declare Fellowship Position",
                    "Heal Ring-bearers",
                    "Activate Nation",
                    "Change Fellowship Guide",
                    "Pass"
                };
                
            case HUNT_ALLOCATION:
                return new String[]{
                    "Place Die in Hunt Box",
                    "Complete Allocation"
                };
                
            case ACTION_ROLL:
                return new String[]{
                    "Roll Action Dice",
                    "Place Eye Results in Hunt Box"
                };
                
            case ACTION_RESOLUTION:
                return new String[]{
                    "Use Character Die",
                    "Use Army Die",
                    "Use Muster Die",
                    "Use Event Die",
                    "Use Will of West Die",
                    "Move Fellowship",
                    "Pass"
                };
                
            case VICTORY_CHECK:
                return new String[]{
                    "Check Military Victory",
                    "Continue to Next Turn"
                };
                
            default:
                return new String[0];
        }
    }
    
    /**
     * Execute phase-specific action
     */
    public boolean executePhaseAction(String action) throws SQLException {
        System.out.println("Executing action: " + action + " in phase: " + currentPhase);
        
        switch (currentPhase) {
            case RECOVER_AND_DRAW:
                return executeRecoverAction(action);
            case FELLOWSHIP_PHASE:
                return executeFellowshipAction(action);
            case HUNT_ALLOCATION:
                return executeHuntAction(action);
            case ACTION_ROLL:
                return executeRollAction(action);
            case ACTION_RESOLUTION:
                return executeResolutionAction(action);
            case VICTORY_CHECK:
                return executeVictoryAction(action);
            default:
                return false;
        }
    }
    
    private boolean executeRecoverAction(String action) {
        if (action.equals("Recover Dice")) {
            dicePool.recoverAllDice();
            System.out.println("Recovered all dice from previous turn");
            return true;
        }
        if (action.equals("Draw Character Card")) {
            // TODO: Implement card drawing
            System.out.println("Drew Character card");
            return true;
        }
        if (action.equals("Draw Strategy Card")) {
            // TODO: Implement card drawing
            System.out.println("Drew Strategy card");
            return true;
        }
        if (action.equals("Complete Recovery")) {
            markPhaseComplete();
            return true;
        }
        return false;
    }
    
    private boolean executeFellowshipAction(String action) {
        // TODO: Implement fellowship phase actions
        if (action.equals("Pass")) {
            markPhaseComplete();
            return true;
        }
        return false;
    }
    
    private boolean executeHuntAction(String action) {
        // TODO: Implement hunt allocation
        if (action.equals("Complete Allocation")) {
            markPhaseComplete();
            return true;
        }
        return false;
    }
    
    private boolean executeRollAction(String action) {
        if (action.equals("Roll Free Peoples Dice")) {
            dicePool.rollDice("free_peoples");
            System.out.println("Rolled Free Peoples dice");
            return true;
        }
        if (action.equals("Roll Shadow Dice")) {
            dicePool.rollDice("shadow");
            System.out.println("Rolled Shadow dice, Eyes to Hunt Box");
            return true;
        }
        if (action.equals("Roll Complete")) {
            markPhaseComplete();
            return true;
        }
        return false;
    }
    
    private boolean executeResolutionAction(String action) {
        // Action Resolution phase - use ActionExecutor
        // Players alternate using dice for various actions
        if (action.equals("All Dice Used")) {
            markPhaseComplete();
            return true;
        }
        
        // For specific die actions, call ActionExecutor
        // This will be expanded with UI integration
        return false;
    }
    
    /**
     * Execute a character movement action
     */
    public ActionResult executeCharacterMove(String characterId, String targetRegionId, ActionDie die) {
        return actionExecutor.executeCharacterAction(characterId, targetRegionId, die);
    }
    
    /**
     * Execute an army movement action
     */
    public ActionResult executeArmyMove(String fromRegionId, String toRegionId, 
                                       int nationId, int regularCount, int eliteCount, ActionDie die) {
        return actionExecutor.executeArmyAction(fromRegionId, toRegionId, nationId, regularCount, eliteCount, die);
    }
    
    /**
     * Execute a muster action
     */
    public ActionResult executeMuster(String regionId, int nationId, 
                                     String unitType, int count, ActionDie die) {
        return actionExecutor.executeMusterAction(regionId, nationId, unitType, count, die);
    }
    
    /**
     * Execute an event card action
     */
    public ActionResult executeEventCard(String faction, String cardId, ActionDie die) {
        return actionExecutor.executeEventAction(faction, cardId, die);
    }
    
    /**
     * Execute Fellowship movement
     */
    public ActionResult executeFellowshipMove(String toRegionId, ActionDie die) {
        return actionExecutor.executeFellowshipMove(toRegionId, die);
    }
    
    /**
     * Initiate combat in a region
     */
    public ActionResult initiateCombat(String regionId) {
        return actionExecutor.initiateCombat(regionId);
    }
    
    private boolean executeVictoryAction(String action) throws SQLException {
        // Check victory using rules engine
        if (action.equals("Check Victory")) {
            boolean hasVictory = rules.checkVictory().isGameOver();
            if (!hasVictory) {
                markPhaseComplete();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get turn/phase summary
     */
    public String getTurnSummary() {
        return String.format(
            "Turn %d - Phase %d/6: %s\n  Status: %s\n  Description: %s",
            currentTurn,
            currentPhase.ordinal() + 1,
            currentPhase.getDisplayName(),
            phaseComplete ? "Complete" : "In Progress",
            currentPhase.getDescription()
        );
    }
    
    /**
     * Parse phase name from database
     */
    private GamePhase parsePhase(String phaseName) {
        try {
            return GamePhase.valueOf(phaseName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to first phase
            return GamePhase.RECOVER_AND_DRAW;
        }
    }
}
