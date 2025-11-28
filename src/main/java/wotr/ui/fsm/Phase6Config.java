package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 6: Victory Check & End of Turn
 */
public class Phase6Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase6Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "🏆 End of Turn";
    }
    
    @Override
    public String getDescription() {
        return "Check victory conditions and prepare for next turn.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("<html><body style='width: 260px;'>" +
            "<b>Victory Check</b><br><br>" +
            "FSM will check for victory conditions:<br>" +
            "• Military victory<br>" +
            "• Corruption victory<br>" +
            "• Ring destroyed<br><br>" +
            "If no winner, turn will advance." +
            "</body></html>");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> buttons = new ArrayList<>();
        
        if (validEvents.contains("START_NEXT_TURN")) {
            buttons.add(new ActionButton("Start Next Turn", "START_NEXT_TURN", true));
        }
        
        return buttons;
    }
}