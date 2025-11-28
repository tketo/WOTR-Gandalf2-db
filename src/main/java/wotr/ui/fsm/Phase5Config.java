package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 5: Action Resolution
 */
public class Phase5Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase5Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "⚔️ Phase 5: Action Resolution";
    }
    
    @Override
    public String getDescription() {
        return "Use your action dice to move armies, recruit units, play cards, and more.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("<html><body style='width: 260px;'>" +
            "<b>Available Actions</b><br><br>" +
            "• Army moves<br>" +
            "• Character moves<br>" +
            "• Muster units<br>" +
            "• Play cards<br>" +
            "• Fellowship movement<br><br>" +
            "Click a die to select it, then use board UI." +
            "</body></html>");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> buttons = new ArrayList<>();
        
        // Could add buttons for specific actions if FSM provides them
        if (validEvents.contains("ACTION_COMPLETE")) {
            buttons.add(new ActionButton("Action Complete", "ACTION_COMPLETE"));
        }
        
        return buttons;
    }
}