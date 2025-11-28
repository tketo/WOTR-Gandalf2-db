package wotr.ui.fsm;

import wotr.Game;
import javax.swing.JPanel;
import java.util.List;
import java.util.Set;

/**
 * Configuration for a specific game phase in the FSM Control Panel.
 * Each phase provides its own title, body content, and action buttons
 * based on the current valid events from the FSM server.
 */
public interface PhaseConfig {
    
    /**
     * Get the phase title (e.g., "🌅 Phase 1: Recover & Draw")
     */
    String getTitle();
    
    /**
     * Get a brief description of what happens in this phase
     */
    String getDescription();
    
    /**
     * Build the body panel showing phase-specific context and information.
     * This is called when the phase becomes active.
     * 
     * @param game The game instance for querying state
     * @return JPanel with phase-specific UI
     */
    JPanel buildBodyPanel(Game game);
    
    /**
     * Get action buttons for this phase based on valid events from FSM.
     * Only buttons for events in validEvents will be shown.
     * 
     * @param validEvents Set of event names that are currently valid
     * @return List of action buttons to display
     */
    List<ActionButton> getActions(Set<String> validEvents);
    
    /**
     * Optional: Called when phase becomes active.
     * Use for setup, logging, or notifications.
     */
    default void onPhaseEnter() {
        // Optional override
    }
    
    /**
     * Optional: Called when phase is exited.
     * Use for cleanup or state saving.
     */
    default void onPhaseExit() {
        // Optional override
    }
}
