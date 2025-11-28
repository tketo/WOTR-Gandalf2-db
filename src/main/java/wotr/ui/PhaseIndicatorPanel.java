package wotr.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import wotr.turn.GamePhase;

/**
 * PhaseIndicatorPanel - Visual display of all 6 game phases
 * 
 * Shows all phases with the current phase highlighted
 * Provides quick overview of game flow
 */
public class PhaseIndicatorPanel extends JPanel {
    private static final Color INACTIVE_COLOR = new Color(200, 200, 200);
    private static final Color ACTIVE_COLOR = new Color(100, 149, 237); // Cornflower blue
    private static final Color COMPLETE_COLOR = new Color(144, 238, 144); // Light green
    
    private JLabel[] phaseLabels;
    private GamePhase currentPhase;
    
    /**
     * Create phase indicator panel
     */
    public PhaseIndicatorPanel() {
        this.currentPhase = GamePhase.RECOVER_AND_DRAW;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Game Phases"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setBackground(new Color(245, 245, 245));
        
        initComponents();
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Create grid for phase indicators
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        gridPanel.setBackground(getBackground());
        gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        GamePhase[] phases = GamePhase.values();
        phaseLabels = new JLabel[phases.length];
        
        for (int i = 0; i < phases.length; i++) {
            GamePhase phase = phases[i];
            
            // Create phase indicator
            JPanel phasePanel = new JPanel(new BorderLayout());
            phasePanel.setBackground(INACTIVE_COLOR);
            phasePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            
            // Phase number
            JLabel numberLabel = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            numberLabel.setFont(new Font("Dialog", Font.BOLD, 18));
            numberLabel.setForeground(Color.DARK_GRAY);
            numberLabel.setPreferredSize(new Dimension(30, 30));
            
            // Phase name
            JLabel nameLabel = new JLabel("<html><center>" + phase.getDisplayName() + "</center></html>", SwingConstants.CENTER);
            nameLabel.setFont(new Font("Dialog", Font.BOLD, 11));
            nameLabel.setForeground(Color.BLACK);
            
            phasePanel.add(numberLabel, BorderLayout.WEST);
            phasePanel.add(nameLabel, BorderLayout.CENTER);
            
            phaseLabels[i] = numberLabel;
            phasePanel.setToolTipText(phase.getDescription());
            
            gridPanel.add(phasePanel);
        }
        
        add(gridPanel, BorderLayout.CENTER);
        
        // Add legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legendPanel.setBackground(getBackground());
        
        legendPanel.add(createLegendItem("Current", ACTIVE_COLOR));
        legendPanel.add(createLegendItem("Complete", COMPLETE_COLOR));
        legendPanel.add(createLegendItem("Pending", INACTIVE_COLOR));
        
        add(legendPanel, BorderLayout.SOUTH);
        
        // Set preferred size
        setPreferredSize(new Dimension(350, 250));
        setMinimumSize(new Dimension(300, 220));
        
        // Initial highlight
        updatePhaseIndicator(currentPhase);
    }
    
    /**
     * Create a legend item
     */
    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(getBackground());
        
        JLabel colorBox = new JLabel("   ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.PLAIN, 10));
        
        item.add(colorBox);
        item.add(label);
        
        return item;
    }
    
    /**
     * Update phase indicator to highlight current phase
     */
    public void updatePhaseIndicator(GamePhase phase) {
        this.currentPhase = phase;
        
        GamePhase[] phases = GamePhase.values();
        for (int i = 0; i < phases.length; i++) {
            JLabel label = phaseLabels[i];
            JPanel parent = (JPanel) label.getParent();
            
            if (phases[i] == phase) {
                // Current phase - highlight in blue
                parent.setBackground(ACTIVE_COLOR);
                label.setForeground(Color.WHITE);
                parent.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            } else if (i < phase.ordinal()) {
                // Completed phase - show in green
                parent.setBackground(COMPLETE_COLOR);
                label.setForeground(Color.DARK_GRAY);
                parent.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            } else {
                // Future phase - show in gray
                parent.setBackground(INACTIVE_COLOR);
                label.setForeground(Color.DARK_GRAY);
                parent.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            }
        }
        
        repaint();
    }
    
    /**
     * Get current phase
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }
}
