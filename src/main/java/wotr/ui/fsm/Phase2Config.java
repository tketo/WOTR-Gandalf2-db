package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 2: Fellowship Phase
 */
public class Phase2Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase2Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "🌍 Phase 2: Fellowship Phase";
    }
    
    @Override
    public String getDescription() {
        return "Free Peoples may: (1) Declare Fellowship to reveal position, " +
               "(2) Heal Ring-bearer if in FP City/Stronghold (remove 1 Corruption), " +
               "(3) Activate Nation if declared in its City/Stronghold, " +
               "(4) Change Guide to any Companion of equal level, " +
               "or skip to next phase.";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("<html><b>Fellowship Status</b></html>");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        
        // TODO: Query game state for Fellowship status
        JLabel status = new JLabel("<html>" +
            "Status: <b>Hidden</b><br>" +
            "Location: Rivendell<br>" +
            "Corruption: 0/12<br>" +
            "Guide: Gandalf the Grey<br>" +
            "Companions: 8" +
            "</html>");
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(status);
        panel.add(Box.createVerticalStrut(10));
        
        JLabel actions = new JLabel("<html><b>Available Actions:</b></html>");
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actions);
        panel.add(Box.createVerticalStrut(5));
        
        JLabel actionsList = new JLabel("<html>" +
            "• <b>Declare:</b> Reveal Fellowship position<br>" +
            "• <b>Heal:</b> Remove 1 Corruption (if in FP City/Stronghold)<br>" +
            "• <b>Activate Nation:</b> Flip to Active (if in Nation's City)<br>" +
            "• <b>Change Guide:</b> Select Companion of equal level<br>" +
            "• <b>Skip:</b> Continue to Phase 3" +
            "</html>");
        actionsList.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actionsList);
        
        return panel;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> actions = new ArrayList<>();
        
        // Declare Fellowship (reveal position)
        if (validEvents.contains("FP_DECLARE_FELLOWSHIP")) {
            actions.add(new ActionButton("📍 Declare Fellowship", "FP_DECLARE_FELLOWSHIP"));
        }
        
        // Heal Ring-bearer (remove 1 Corruption)
        if (validEvents.contains("FP_HEAL_RINGBEARER")) {
            actions.add(new ActionButton("💚 Heal Ring-bearer", "FP_HEAL_RINGBEARER"));
        }
        
        // Activate Nation
        if (validEvents.contains("FP_ACTIVATE_NATION")) {
            actions.add(new ActionButton("⚔️ Activate Nation", "FP_ACTIVATE_NATION"));
        }
        
        // Change Guide
        if (validEvents.contains("FP_CHANGE_GUIDE")) {
            actions.add(new ActionButton("👤 Change Guide", "FP_CHANGE_GUIDE"));
        }
        
        // Done with Fellowship Phase
        if (validEvents.contains("FP_FELLOWSHIP_DECISIONS_READY")) {
            actions.add(new ActionButton("Done", "FP_FELLOWSHIP_DECISIONS_READY", true));
        }
        
        return actions;
    }
}