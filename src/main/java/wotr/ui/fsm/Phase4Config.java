package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 4: Action Roll
 */
public class Phase4Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase4Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "🎲 Phase 4: Action Roll";
    }
    
    @Override
    public String getDescription() {
        return "Roll all action dice for this turn. FSM will roll automatically.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("<html><body style='width: 260px;'>" +
            "<b>Rolling Action Dice</b><br><br>" +
            "FSM will automatically roll all dice.<br><br>" +
            "Results will appear in the dice pool." +
            "</body></html>");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        // Automatic phase, no actions
        return new ArrayList<>();
    }
}