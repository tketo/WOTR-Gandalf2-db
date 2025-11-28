package wotr.ui.fsm;

import wotr.Game;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Phase 1: Recover Dice & Draw Cards
 * 
 * This phase is mostly automatic. FSM executes:
 * - Recover all FP action dice
 * - Recover all Shadow action dice  
 * - Draw 1 FP character card
 * - Draw 1 FP strategy card
 * - Draw 1 Shadow character card
 * - Draw 1 Shadow strategy card
 * 
 * Players may need to discard if over hand limit (6 cards).
 */
public class Phase1Config implements PhaseConfig {
    
    private final Game game;
    
    public Phase1Config(Game game) {
        this.game = game;
    }
    
    @Override
    public String getTitle() {
        return "🌅 Phase 1: Recover & Draw";
    }
    
    @Override
    public String getDescription() {
        return "Automatically recover action dice and draw cards. " +
               "Discard cards if over hand limit (6).";
    }
    
    @Override
    public JPanel buildBodyPanel(Game game) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Description
        JLabel desc = new JLabel("<html><i>Automatically recover dice and draw cards</i></html>");
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(desc);
        panel.add(Box.createVerticalStrut(10));
        
        // Checklist of automatic actions
        panel.add(createChecklist());
        panel.add(Box.createVerticalStrut(10));
        
        // Hand size status
        panel.add(createHandSizePanel());
        panel.add(Box.createVerticalStrut(10));
        
        // Instructions
        JLabel instructions = new JLabel("<html><body style='width: 260px;'>" +
            "<b>What to do:</b><br>" +
            "• If any hand has > 6 cards, discard to hand limit<br>" +
            "• When ready, click 'Done With Phase'" +
            "</body></html>");
        instructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(instructions);
        
        return panel;
    }
    
    /**
     * Create checklist of automatic actions
     */
    private JPanel createChecklist() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Automatic Actions"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] actions = {
            "☑ Recover FP action dice",
            "☑ Recover Shadow action dice",
            "☑ Draw 1 FP character card",
            "☑ Draw 1 FP strategy card",
            "☑ Draw 1 Shadow character card",
            "☑ Draw 1 Shadow strategy card"
        };
        
        for (String action : actions) {
            JLabel label = new JLabel(action);
            label.setFont(new Font("SansSerif", Font.PLAIN, 11));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);
        }
        
        return panel;
    }
    
    /**
     * Create hand size status panel
     */
    private JPanel createHandSizePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Hand Sizes"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Get actual hand sizes from database
        int fpHandSize = getHandSize("FP");
        int shadowHandSize = getHandSize("Shadow");
        int handLimit = 6;
        
        // FP hand
        JLabel fpLabel = new JLabel(formatHandSize("Free Peoples", fpHandSize, handLimit));
        fpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fpLabel);
        
        // Shadow hand
        JLabel shadowLabel = new JLabel(formatHandSize("Shadow", shadowHandSize, handLimit));
        shadowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(shadowLabel);
        
        return panel;
    }
    
    /**
     * Format hand size with warning if over limit
     */
    private String formatHandSize(String faction, int handSize, int limit) {
        if (handSize > limit) {
            return String.format("⚠️  %s: %d / %d (discard %d)", 
                faction, handSize, limit, handSize - limit);
        } else {
            return String.format("✅ %s: %d / %d", faction, handSize, limit);
        }
    }
    
    /**
     * Get hand size from database (stub for now)
     */
    private int getHandSize(String faction) {
        // TODO: Query database for actual hand size
        // For now, return placeholder
        return 6;
    }
    
    @Override
    public List<ActionButton> getActions(Set<String> validEvents) {
        List<ActionButton> buttons = new ArrayList<>();
        
        // Phase 1 is mostly automatic, no action buttons
        // "Done With Phase" button is handled by parent panel
        
        return buttons;
    }
}
