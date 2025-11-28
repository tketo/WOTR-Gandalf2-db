package wotr.ui.fsm;

/**
 * Represents an action button in the FSM Control Panel.
 * Each button triggers an FSM event when clicked.
 */
public class ActionButton {
    private final String label;
    private final String event;
    private final Runnable action;
    private final boolean primary;
    
    /**
     * Create a button that sends an FSM event
     */
    public ActionButton(String label, String event) {
        this.label = label;
        this.event = event;
        this.action = null;
        this.primary = false;
    }
    
    /**
     * Create a button with custom action (for non-FSM actions)
     */
    public ActionButton(String label, Runnable action) {
        this.label = label;
        this.event = null;
        this.action = action;
        this.primary = false;
    }
    
    /**
     * Create a button with primary styling
     */
    public ActionButton(String label, String event, boolean primary) {
        this.label = label;
        this.event = event;
        this.action = null;
        this.primary = primary;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getEvent() {
        return event;
    }
    
    public Runnable getAction() {
        return action;
    }
    
    public boolean isPrimary() {
        return primary;
    }
    
    public boolean hasEvent() {
        return event != null;
    }
    
    public boolean hasAction() {
        return action != null;
    }
}
