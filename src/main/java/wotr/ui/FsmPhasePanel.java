package wotr.ui;

import wotr.Game;
import wotr.ui.fsm.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * FSM Control Panel - Phase-aware UI showing current game state,
 * available actions, and context-specific guidance.
 * 
 * Replaces the simple status bar with a comprehensive control center
 * driven by FSM server messages.
 */
public class FsmPhasePanel extends JPanel {
    
    private final Game game;
    
    // Header components
    private JLabel turnPhaseLabel;
    private JLabel activePlayerLabel;
    private JLabel stateLabel;  // Debug info
    
    // Body (CardLayout for different phase UIs)
    private JPanel bodyPanel;
    private CardLayout bodyLayout;
    private Map<String, PhaseConfig> phaseConfigs;
    
    // Actions area
    private JPanel actionPanel;
    private JButton doneButton;
    private JButton helpButton;
    
    // Current state
    private String currentState = "";
    private Set<String> validEvents = new HashSet<>();
    private PhaseConfig currentPhaseConfig;
    
    // Dimensions
    private static final int PANEL_WIDTH = 320;
    private static final Color HEADER_BG = new Color(240, 240, 245);
    private static final Color BODY_BG = Color.WHITE;
    private static final Color ACTION_BG = new Color(250, 250, 250);
    
    public FsmPhasePanel(Game game) {
        this.game = game;
        this.phaseConfigs = new HashMap<>();
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH, 600));
        setBorder(new LineBorder(Color.GRAY, 1));
        
        initializePhaseConfigs();
        buildHeader();
        buildBody();
        buildFooter();
        
        // Start in "Not Connected" state
        updateState("NOT_CONNECTED");
    }
    
    /**
     * Initialize all phase configurations
     */
    private void initializePhaseConfigs() {
        phaseConfigs.put("SETUP_GAME_OPTIONS", new SetupConfig());
        phaseConfigs.put("PHASE1", new Phase1Config(game));
        phaseConfigs.put("PHASE2", new Phase2Config(game));
        phaseConfigs.put("PHASE3", new Phase3Config(game));
        phaseConfigs.put("PHASE4", new Phase4Config(game));
        phaseConfigs.put("PHASE5", new Phase5Config(game));
        phaseConfigs.put("PHASE6", new Phase6Config(game));
        phaseConfigs.put("NOT_CONNECTED", new NotConnectedConfig());
    }
    
    /**
     * Build header section (turn, phase, player, state)
     */
    private void buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Turn & Phase
        turnPhaseLabel = new JLabel("Not Connected");
        turnPhaseLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        turnPhaseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(turnPhaseLabel);
        
        header.add(Box.createVerticalStrut(5));
        
        // Active Player
        activePlayerLabel = new JLabel(" ");
        activePlayerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        activePlayerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(activePlayerLabel);
        
        header.add(Box.createVerticalStrut(5));
        
        // State (debug)
        stateLabel = new JLabel(" ");
        stateLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
        stateLabel.setForeground(Color.GRAY);
        stateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(stateLabel);
        
        add(header, BorderLayout.NORTH);
    }
    
    /**
     * Build body section (CardLayout for phase-specific content)
     */
    private void buildBody() {
        bodyLayout = new CardLayout();
        bodyPanel = new JPanel(bodyLayout);
        bodyPanel.setBackground(BODY_BG);
        bodyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add a panel for each phase config
        for (Map.Entry<String, PhaseConfig> entry : phaseConfigs.entrySet()) {
            JPanel phasePanel = entry.getValue().buildBodyPanel(game);
            bodyPanel.add(phasePanel, entry.getKey());
        }
        
        add(new JScrollPane(bodyPanel), BorderLayout.CENTER);
    }
    
    /**
     * Build footer section (action buttons)
     */
    private void buildFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBackground(ACTION_BG);
        footer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Action buttons panel (vertical layout)
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(ACTION_BG);
        footer.add(actionPanel, BorderLayout.NORTH);
        
        // Bottom row: Done + Help
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        bottomRow.setBackground(ACTION_BG);
        
        doneButton = new JButton("Done With Phase");
        doneButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        doneButton.setEnabled(false);
        doneButton.addActionListener(e -> sendEvent("PHASE_COMPLETE"));
        bottomRow.add(doneButton);
        
        helpButton = new JButton("?");
        helpButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
        helpButton.setToolTipText("View phase rules");
        helpButton.addActionListener(e -> showHelp());
        bottomRow.add(helpButton);
        
        footer.add(bottomRow, BorderLayout.SOUTH);
        
        add(footer, BorderLayout.SOUTH);
    }
    
    /**
     * Update panel when FSM state changes
     */
    public void updateState(String state) {
        this.currentState = state;
        stateLabel.setText("State: " + state);
        
        // Determine which phase config to use
        String phaseKey = determinePhaseKey(state);
        PhaseConfig config = phaseConfigs.get(phaseKey);
        
        if (config != null && config != currentPhaseConfig) {
            // Exit old phase
            if (currentPhaseConfig != null) {
                currentPhaseConfig.onPhaseExit();
            }
            
            // Enter new phase
            currentPhaseConfig = config;
            currentPhaseConfig.onPhaseEnter();
            
            // Update header
            turnPhaseLabel.setText(config.getTitle());
            activePlayerLabel.setText(getActivePlayer(state));
            
            // Switch body panel
            bodyLayout.show(bodyPanel, phaseKey);
            
            // Update actions
            updateActions();
        }
    }
    
    /**
     * Update panel when valid events change
     */
    public void updateValidEvents(String[] events) {
        this.validEvents = new HashSet<>(Arrays.asList(events));
        
        // Enable/disable done button
        doneButton.setEnabled(validEvents.contains("PHASE_COMPLETE"));
        
        // Update phase-specific action buttons
        updateActions();
    }
    
    /**
     * Update phase info from FSM messages (e.g., "<auto> Turn 1 — Phase 1")
     */
    public void updatePhaseInfo(String message) {
        // Extract phase from message (FSM sends: "🌅 PHASE 1: Recover Dice & Draw Cards")
        if (message == null || message.isEmpty()) {
            return;
        }
        
        String upperMsg = message.toUpperCase();
        
        // Detect which phase from message content
        if (upperMsg.contains("TURN 0") || upperMsg.contains("SETUP") || upperMsg.contains("INITIALIZATION") || upperMsg.contains("CHOOSE EXPANSIONS")) {
            updateState("SETUP_GAME_OPTIONS");
            turnPhaseLabel.setText("Setup");
        } else if (upperMsg.contains("PHASE 1") || upperMsg.contains("RECOVER") && upperMsg.contains("DRAW")) {
            updateState("PHASE1_RECOVER_AND_DRAW");
        } else if (upperMsg.contains("PHASE 2") || upperMsg.contains("FELLOWSHIP PHASE")) {
            updateState("FP_FELLOWSHIP_DECISIONS");
        } else if (upperMsg.contains("PHASE 3") || upperMsg.contains("HUNT ALLOCATION")) {
            updateState("SHADOW_HUNT_ALLOCATION");
        } else if (upperMsg.contains("PHASE 4") || upperMsg.contains("ACTION ROLL")) {
            updateState("FP_ACTION_ROLL");
        } else if (upperMsg.contains("PHASE 5") || upperMsg.contains("ACTION RESOLUTION")) {
            updateState("FP_ACTION_RESOLUTION");
        } else if (upperMsg.contains("PHASE 6") || upperMsg.contains("END OF TURN") || upperMsg.contains("VICTORY")) {
            updateState("END_OF_TURN_CHECKS");
        }
    }
    
    /**
     * Rebuild action buttons based on current phase and valid events
     */
    private void updateActions() {
        actionPanel.removeAll();
        
        if (currentPhaseConfig != null) {
            List<ActionButton> buttons = currentPhaseConfig.getActions(validEvents);
            
            for (ActionButton actionBtn : buttons) {
                JButton btn = new JButton(actionBtn.getLabel());
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMaximumSize(new Dimension(280, 30));
                
                if (actionBtn.isPrimary()) {
                    btn.setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                
                if (actionBtn.hasEvent()) {
                    btn.addActionListener(e -> sendEvent(actionBtn.getEvent()));
                } else if (actionBtn.hasAction()) {
                    btn.addActionListener(e -> actionBtn.getAction().run());
                }
                
                actionPanel.add(btn);
                actionPanel.add(Box.createVerticalStrut(5));
            }
        }
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Map FSM state to phase config key
     */
    private String determinePhaseKey(String state) {
        if (state == null || state.equals("NOT_CONNECTED")) {
            return "NOT_CONNECTED";
        }
        
        // Map state names to phase configs
        String upperState = state.toUpperCase();
        
        // Setup/Initialization states
        if (upperState.contains("SETUP") || upperState.contains("INITIALIZATION") || 
            upperState.equals("SETUP_GAME_OPTIONS")) {
            return "SETUP_GAME_OPTIONS";
        }
        
        if (upperState.contains("RECOVER") || upperState.contains("DRAW") || 
            upperState.contains("PHASE1") || upperState.contains("PHASE_1")) {
            return "PHASE1";
        }
        if (upperState.contains("FELLOWSHIP") || upperState.contains("PHASE2") || 
            upperState.contains("PHASE_2")) {
            return "PHASE2";
        }
        if (upperState.contains("HUNT") || upperState.contains("PHASE3") || 
            upperState.contains("PHASE_3")) {
            return "PHASE3";
        }
        if (upperState.contains("ROLL") || upperState.contains("PHASE4") || 
            upperState.contains("PHASE_4")) {
            return "PHASE4";
        }
        if (upperState.contains("ACTION") || upperState.contains("RESOLUTION") || 
            upperState.contains("PHASE5") || upperState.contains("PHASE_5")) {
            return "PHASE5";
        }
        if (upperState.contains("VICTORY") || upperState.contains("END") || 
            upperState.contains("PHASE6") || upperState.contains("PHASE_6")) {
            return "PHASE6";
        }
        
        return "NOT_CONNECTED";
    }
    
    /**
     * Determine active player from state name
     */
    private String getActivePlayer(String state) {
        if (state == null) return "";
        
        String upper = state.toUpperCase();
        if (upper.contains("FP_") || upper.contains("FREE")) {
            return "Active Player: Free Peoples";
        }
        if (upper.contains("SHADOW") || upper.contains("SP_")) {
            return "Active Player: Shadow";
        }
        if (upper.contains("PHASE1") || upper.contains("RECOVER")) {
            return "Active Player: Both";
        }
        
        return "";
    }
    
    /**
     * Send event to FSM server
     */
    private void sendEvent(String event) {
        if (game.fsmConnection != null && game.fsmConnection.isConnected()) {
            game.fsmConnection.sendEvent(event);
            // Log to console (chat reference would need to be passed in)
            System.out.println("[FSM Panel] Sent: EVENT:" + event);
        }
    }
    
    /**
     * Show help dialog for current phase
     */
    private void showHelp() {
        if (currentPhaseConfig != null) {
            String help = currentPhaseConfig.getDescription();
            JOptionPane.showMessageDialog(this, help, 
                "Phase Help", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Get current FSM state
     */
    public String getCurrentState() {
        return currentState;
    }
    
    /**
     * Get current valid events
     */
    public Set<String> getValidEvents() {
        return new HashSet<>(validEvents);
    }
}
