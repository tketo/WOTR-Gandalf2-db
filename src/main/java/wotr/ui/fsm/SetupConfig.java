package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Setup Phase Configuration
 * Shows game setup options and initialization progress
 */
public class SetupConfig implements PhaseConfig {
    
    @Override
    public String getTitle() {
        return "⚙️ Game Setup";
    }
    
    @Override
    public String getDescription() {
        return "Configure game options before starting";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Setup instructions
        JTextArea instructions = new JTextArea(
            "Configure game options:\n\n" +
            "• Toggle expansions (LOME, WOME)\n" +
            "• Enable/disable Treebeard\n" +
            "• Set player count (1-4)\n\n" +
            "Once configured, confirm to begin game initialization."
        );
        instructions.setEditable(false);
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instructions.setBackground(Color.WHITE);
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(instructions);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> actions = new ArrayList<>();
        
        // Map valid events to action buttons
        if (validEvents.contains("TOGGLE_LOME")) {
            actions.add(new ActionButton("Toggle LOME", "TOGGLE_LOME", false));
        }
        
        if (validEvents.contains("TOGGLE_WOME")) {
            actions.add(new ActionButton("Toggle WOME", "TOGGLE_WOME", false));
        }
        
        if (validEvents.contains("TOGGLE_TREEBEARD")) {
            actions.add(new ActionButton("Toggle Treebeard", "TOGGLE_TREEBEARD", false));
        }
        
        if (validEvents.contains("SET_PLAYER_COUNT")) {
            actions.add(new ActionButton("Set Player Count", "SET_PLAYER_COUNT", false));
        }
        
        if (validEvents.contains("GAME_OPTIONS_CONFIRMED")) {
            actions.add(new ActionButton("✓ Confirm & Start", "GAME_OPTIONS_CONFIRMED", true));
        }
        
        return actions;
    }
}
