package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 3: Hunt Allocation
 */
public class Phase3Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase3Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "👁️ Phase 3: Hunt Allocation";
    }
    
    @Override
    public String getDescription() {
        return "Shadow player allocates Eye dice to Hunt Box for hunting the Fellowship.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("<html><body style='width: 260px;'>" +
            "<b>Hunt Allocation</b><br><br>" +
            "Shadow player: Drag Eye dice to Hunt Box.<br><br>" +
            "These dice will be used when Fellowship moves." +
            "</body></html>");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> buttons = new ArrayList<>();
        
        if (validEvents.contains("SHADOW_CONFIRMS_HUNT_ALLOCATION")) {
            buttons.add(new ActionButton("Confirm Allocation", "SHADOW_CONFIRMS_HUNT_ALLOCATION", true));
        }
        
        return buttons;
    }
}