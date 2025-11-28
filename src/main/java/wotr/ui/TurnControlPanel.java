package wotr.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import wotr.turn.GamePhase;

/**
 * TurnControlPanel - UI component for turn and phase control
 * 
 * Displays:
 * - Current turn number
 * - Current phase name and description
 * - Active player indicator
 * - "Advance Phase" button
 * - Phase completion status
 */
public class TurnControlPanel extends JPanel {
    private JLabel turnNumberLabel;
    private JLabel phaseNameLabel;
    private JLabel phaseDescriptionLabel;
    private JLabel activePlayerLabel;
    private JLabel phaseStatusLabel;
    private JButton advancePhaseButton;
    private JButton markCompleteButton;
    
    private int currentTurn;
    private GamePhase currentPhase;
    private boolean phaseComplete;
    
    /**
     * Create turn control panel
     */
    public TurnControlPanel() {
        this.currentTurn = 1;
        this.currentPhase = GamePhase.RECOVER_AND_DRAW;
        this.phaseComplete = false;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Turn & Phase Control"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(new Color(240, 240, 240));
        
        initComponents();
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Top section: Turn number and phase name
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Turn number
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel turnLabel = new JLabel("Turn:", SwingConstants.RIGHT);
        turnLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        topPanel.add(turnLabel, gbc);
        
        gbc.gridx = 1;
        turnNumberLabel = new JLabel("1");
        turnNumberLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        turnNumberLabel.setForeground(new Color(0, 100, 0));
        topPanel.add(turnNumberLabel, gbc);
        
        // Phase name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        phaseNameLabel = new JLabel(currentPhase.getDisplayName(), SwingConstants.CENTER);
        phaseNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        phaseNameLabel.setForeground(new Color(0, 0, 139));
        phaseNameLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        phaseNameLabel.setOpaque(true);
        phaseNameLabel.setBackground(new Color(220, 220, 255));
        topPanel.add(phaseNameLabel, gbc);
        
        // Center section: Phase description and active player
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(getBackground());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        phaseDescriptionLabel = new JLabel("<html><i>" + currentPhase.getDescription() + "</i></html>");
        phaseDescriptionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        centerPanel.add(phaseDescriptionLabel, BorderLayout.CENTER);
        
        activePlayerLabel = new JLabel("Active: " + formatActivePlayer(currentPhase.getActivePlayer()));
        activePlayerLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        activePlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(activePlayerLabel, BorderLayout.SOUTH);
        
        // Bottom section: Status and buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(getBackground());
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Phase status
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        phaseStatusLabel = new JLabel("Status: In Progress", SwingConstants.CENTER);
        phaseStatusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        phaseStatusLabel.setForeground(new Color(139, 69, 19));
        bottomPanel.add(phaseStatusLabel, gbc);
        
        // Mark complete button
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        markCompleteButton = new JButton("Mark Phase Complete");
        markCompleteButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        markCompleteButton.setToolTipText("Mark the current phase as complete to enable advancement");
        bottomPanel.add(markCompleteButton, gbc);
        
        // Advance phase button
        gbc.gridx = 1;
        advancePhaseButton = new JButton("Advance Phase");
        advancePhaseButton.setFont(new Font("Dialog", Font.BOLD, 12));
        advancePhaseButton.setEnabled(false);
        advancePhaseButton.setToolTipText("Advance to the next phase");
        bottomPanel.add(advancePhaseButton, gbc);
        
        // Add sections to main panel
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Set preferred size
        setPreferredSize(new Dimension(350, 200));
        setMinimumSize(new Dimension(300, 180));
    }
    
    /**
     * Update display with current turn and phase
     */
    public void updateDisplay(int turn, GamePhase phase, boolean complete) {
        this.currentTurn = turn;
        this.currentPhase = phase;
        this.phaseComplete = complete;
        
        turnNumberLabel.setText(String.valueOf(turn));
        phaseNameLabel.setText(phase.getDisplayName());
        phaseDescriptionLabel.setText("<html><i>" + phase.getDescription() + "</i></html>");
        activePlayerLabel.setText("Active: " + formatActivePlayer(phase.getActivePlayer()));
        
        // Update status
        if (complete) {
            phaseStatusLabel.setText("Status: Complete ✓");
            phaseStatusLabel.setForeground(new Color(0, 128, 0));
            advancePhaseButton.setEnabled(true);
            markCompleteButton.setEnabled(false);
        } else {
            phaseStatusLabel.setText("Status: In Progress");
            phaseStatusLabel.setForeground(new Color(139, 69, 19));
            advancePhaseButton.setEnabled(false);
            markCompleteButton.setEnabled(true);
        }
        
        // Highlight new turn
        if (phase.isFirstPhase()) {
            turnNumberLabel.setForeground(new Color(255, 0, 0));
        } else {
            turnNumberLabel.setForeground(new Color(0, 100, 0));
        }
    }
    
    /**
     * Format active player for display
     */
    private String formatActivePlayer(String player) {
        switch (player) {
            case "free_peoples":
                return "Free Peoples";
            case "shadow":
                return "Shadow";
            case "both":
                return "Both Players";
            default:
                return player;
        }
    }
    
    /**
     * Add action listener to advance phase button
     */
    public void addAdvancePhaseListener(ActionListener listener) {
        advancePhaseButton.addActionListener(listener);
    }
    
    /**
     * Add action listener to mark complete button
     */
    public void addMarkCompleteListener(ActionListener listener) {
        markCompleteButton.addActionListener(listener);
    }
    
    /**
     * Get current turn number
     */
    public int getCurrentTurn() {
        return currentTurn;
    }
    
    /**
     * Get current phase
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * Is phase complete?
     */
    public boolean isPhaseComplete() {
        return phaseComplete;
    }
}
