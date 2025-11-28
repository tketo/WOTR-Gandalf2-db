package wotr.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import wotr.turn.TurnManager;
import wotr.turn.GamePhase;
import wotr.actions.ActionResult;
import wotr.dice.ActionDie;
import wotr.combat.CombatState;
import wotr.combat.CombatRoundResult;

/**
 * TurnOrchestrator - Integration layer between TurnManager and UI
 * 
 * Coordinates turn/phase changes with UI updates
 * Manages action validation and UI state
 */
public class TurnOrchestrator {
    private TurnManager turnManager;
    private TurnControlPanel turnControl;
    private PhaseIndicatorPanel phaseIndicator;
    private List<PhaseChangeListener> listeners;
    
    /**
     * Create orchestrator with turn manager and UI components
     */
    public TurnOrchestrator(TurnManager turnManager, TurnControlPanel turnControl, PhaseIndicatorPanel phaseIndicator) {
        this.turnManager = turnManager;
        this.turnControl = turnControl;
        this.phaseIndicator = phaseIndicator;
        this.listeners = new ArrayList<>();
        
        initializeUI();
        wireUpListeners();
    }
    
    /**
     * Initialize UI with current turn state
     */
    private void initializeUI() {
        updateUI();
    }
    
    /**
     * Wire up button listeners
     */
    private void wireUpListeners() {
        // Advance phase button
        turnControl.addAdvancePhaseListener(e -> advancePhase());
        
        // Mark complete button
        turnControl.addMarkCompleteListener(e -> markPhaseComplete());
    }
    
    /**
     * Advance to next phase
     */
    private void advancePhase() {
        try {
            boolean advanced = turnManager.advancePhase();
            
            if (advanced) {
                updateUI();
                notifyPhaseChange(turnManager.getCurrentPhase());
                
                // Show notification if new turn started
                if (turnManager.getCurrentPhase().isFirstPhase()) {
                    showNotification("New Turn", "Turn " + turnManager.getCurrentTurn() + " has begun!");
                }
            } else {
                showError("Cannot advance phase. Current phase is not complete.");
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Mark current phase as complete
     */
    private void markPhaseComplete() {
        turnManager.markPhaseComplete();
        updateUI();
        
        showNotification("Phase Complete", 
            turnManager.getCurrentPhase().getDisplayName() + " marked as complete.");
    }
    
    /**
     * Update all UI components with current state
     */
    private void updateUI() {
        int turn = turnManager.getCurrentTurn();
        GamePhase phase = turnManager.getCurrentPhase();
        boolean complete = turnManager.isPhaseComplete();
        
        turnControl.updateDisplay(turn, phase, complete);
        phaseIndicator.updatePhaseIndicator(phase);
    }
    
    /**
     * Execute a phase action
     */
    public boolean executeAction(String action) {
        try {
            boolean success = turnManager.executePhaseAction(action);
            
            if (success) {
                updateUI();
                return true;
            }
            return false;
        } catch (SQLException ex) {
            showError("Error executing action: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get available actions for current phase
     */
    public String[] getAvailableActions() {
        return turnManager.getAvailableActions();
    }
    
    /**
     * Get current phase
     */
    public GamePhase getCurrentPhase() {
        return turnManager.getCurrentPhase();
    }
    
    /**
     * Get current turn
     */
    public int getCurrentTurn() {
        return turnManager.getCurrentTurn();
    }
    
    /**
     * Is current phase complete?
     */
    public boolean isPhaseComplete() {
        return turnManager.isPhaseComplete();
    }
    
    /**
     * Can advance to next phase?
     */
    public boolean canAdvancePhase() {
        return turnManager.canAdvancePhase();
    }
    
    /**
     * Get turn summary
     */
    public String getTurnSummary() {
        return turnManager.getTurnSummary();
    }
    
    /**
     * Add phase change listener
     */
    public void addPhaseChangeListener(PhaseChangeListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Notify all listeners of phase change
     */
    private void notifyPhaseChange(GamePhase newPhase) {
        for (PhaseChangeListener listener : listeners) {
            listener.onPhaseChanged(newPhase);
        }
    }
    
    /**
     * Show notification dialog
     */
    private void showNotification(String title, String message) {
        JOptionPane.showMessageDialog(
            turnControl,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show error dialog
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            turnControl,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Get turn control panel
     */
    public TurnControlPanel getTurnControl() {
        return turnControl;
    }
    
    /**
     * Get phase indicator panel
     */
    public PhaseIndicatorPanel getPhaseIndicator() {
        return phaseIndicator;
    }
    
    /**
     * Get turn manager
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }
    
    // ===== ACTION EXECUTION METHODS =====
    
    /**
     * Execute character movement
     */
    public ActionResult executeCharacterMove(String characterId, String targetRegionId, ActionDie die) {
        try {
            ActionResult result = turnManager.executeCharacterMove(characterId, targetRegionId, die);
            updateUI();
            
            if (result.isSuccess()) {
                showNotification("Character Moved", result.getMessage());
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error moving character: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Execute army movement
     */
    public ActionResult executeArmyMove(String fromRegionId, String toRegionId, 
                                       int nationId, int regularCount, int eliteCount, ActionDie die) {
        try {
            ActionResult result = turnManager.executeArmyMove(
                fromRegionId, toRegionId, nationId, regularCount, eliteCount, die
            );
            updateUI();
            
            if (result.isSuccess()) {
                if (result.isCombatTriggered()) {
                    showNotification("Combat!", result.getMessage());
                } else {
                    showNotification("Army Moved", result.getMessage());
                }
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error moving army: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Execute muster action
     */
    public ActionResult executeMuster(String regionId, int nationId, 
                                     String unitType, int count, ActionDie die) {
        try {
            ActionResult result = turnManager.executeMuster(regionId, nationId, unitType, count, die);
            updateUI();
            
            if (result.isSuccess()) {
                showNotification("Muster Complete", result.getMessage());
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error mustering: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Execute event card
     */
    public ActionResult executeEventCard(String faction, String cardId, ActionDie die) {
        try {
            ActionResult result = turnManager.executeEventCard(faction, cardId, die);
            updateUI();
            
            if (result.isSuccess()) {
                showNotification("Card Played", result.getMessage());
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error playing card: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Execute Fellowship movement
     */
    public ActionResult executeFellowshipMove(String toRegionId, ActionDie die) {
        try {
            ActionResult result = turnManager.executeFellowshipMove(toRegionId, die);
            updateUI();
            
            if (result.isSuccess()) {
                if (result.isHuntTriggered()) {
                    showNotification("Hunt Roll!", result.getSummary());
                } else {
                    showNotification("Fellowship Moved", result.getMessage());
                }
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error moving Fellowship: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Initiate combat
     */
    public ActionResult initiateCombat(String regionId) {
        try {
            ActionResult result = turnManager.initiateCombat(regionId);
            updateUI();
            
            if (result.isSuccess()) {
                showNotification("Combat Initiated!", result.getSummary());
            } else {
                showError(result.getMessage());
            }
            
            return result;
        } catch (Exception e) {
            showError("Error initiating combat: " + e.getMessage());
            return ActionResult.error(e.getMessage());
        }
    }
    
    /**
     * Public method to mark phase complete (for GameIntegrationBridge)
     */
    public void markCurrentPhaseComplete() {
        markPhaseComplete();
    }
    
    /**
     * Public method to advance phase (for GameIntegrationBridge)
     */
    public boolean advanceToNextPhase() {
        try {
            boolean advanced = turnManager.advancePhase();
            if (advanced) {
                updateUI();
                notifyPhaseChange(turnManager.getCurrentPhase());
            }
            return advanced;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Interface for listening to phase changes
     */
    public interface PhaseChangeListener {
        void onPhaseChanged(GamePhase newPhase);
    }
}
