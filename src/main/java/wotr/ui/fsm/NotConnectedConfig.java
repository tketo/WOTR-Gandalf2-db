package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase configuration for when FSM server is not connected
 */
public class NotConnectedConfig implements PhaseConfig {
    
    @Override
    public String getTitle() {
        return "FSM: Not Connected";
    }
    
    @Override
    public String getDescription() {
        return "Connect to FSM server via Multiplayer menu to enable automated turn flow.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("<html><body style='width: 250px; text-align: center;'>" +
            "<br><br><b>FSM Server Not Connected</b><br><br>" +
            "To connect:<br>" +
            "1. Start FSM server (cd fsm/server && npm start)<br>" +
            "2. Multiplayer → Connect to FSM Server<br>" +
            "3. Enter localhost:8080<br><br>" +
            "The FSM server orchestrates game flow and enables AI play." +
            "</body></html>");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        // No actions when not connected
        return new ArrayList<>();
    }
}
