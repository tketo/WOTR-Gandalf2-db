package wotr.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import wotr.services.GameStateService;
import wotr.turn.TurnManager;

/**
 * TurnPhaseDemo - Demonstration of Phase 5 Turn & Phase UI
 * 
 * Shows the new turn/phase orchestration system in action
 * Tests all UI components independently of the main game
 */
public class TurnPhaseDemo {
    private JFrame frame;
    private TurnOrchestrator orchestrator;
    private JTextArea logArea;
    
    /**
     * Create and display the demo
     */
    public TurnPhaseDemo() {
        createUI();
    }
    
    /**
     * Create UI components
     */
    private void createUI() {
        frame = new JFrame("WOTR - Phase 5: Turn & Phase Control Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Initialize database and turn manager
        GameStateService gameState = new GameStateService();
        
        // Create a demo game session for testing
        try {
            gameState.createNewGame("free_peoples");
            System.out.println("Created demo game session for testing");
        } catch (Exception e) {
            System.err.println("Warning: Could not create game session: " + e.getMessage());
        }
        
        TurnManager turnManager = new TurnManager(gameState);
        
        // Create UI components
        TurnControlPanel turnControl = new TurnControlPanel();
        PhaseIndicatorPanel phaseIndicator = new PhaseIndicatorPanel();
        
        // Create orchestrator
        orchestrator = new TurnOrchestrator(turnManager, turnControl, phaseIndicator);
        
        // Add phase change listener for logging
        orchestrator.addPhaseChangeListener(phase -> {
            log("Phase changed to: " + phase.getDisplayName());
        });
        
        // Left panel: Turn and Phase controls
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        leftPanel.add(turnControl);
        leftPanel.add(phaseIndicator);
        
        // Right panel: Action log and test controls
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        // Log area
        logArea = new JTextArea(20, 40);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Action Log"));
        rightPanel.add(logScroll, BorderLayout.CENTER);
        
        // Test controls
        JPanel testPanel = createTestControls();
        rightPanel.add(testPanel, BorderLayout.SOUTH);
        
        // Add panels to frame
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);
        
        // Window listener for cleanup
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });
        
        // Pack and display
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Initial log
        log("=== WOTR Turn & Phase Control Demo ===");
        log("Phase 5 UI Integration Test");
        log("Current state: " + orchestrator.getTurnSummary());
        log("\nUse the buttons to test turn progression!");
    }
    
    /**
     * Create test control buttons
     */
    private JPanel createTestControls() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Test Controls"));
        
        JButton rollDiceBtn = new JButton("Simulate Roll Dice");
        rollDiceBtn.addActionListener(e -> {
            log("\n--- Rolling dice ---");
            boolean success = orchestrator.executeAction("Roll Free Peoples Dice");
            log("Roll FP: " + (success ? "Success" : "Failed"));
            success = orchestrator.executeAction("Roll Shadow Dice");
            log("Roll Shadow: " + (success ? "Success" : "Failed"));
        });
        
        JButton recoverDiceBtn = new JButton("Simulate Recover Dice");
        recoverDiceBtn.addActionListener(e -> {
            log("\n--- Recovering dice ---");
            boolean success = orchestrator.executeAction("Recover Dice");
            log("Recover: " + (success ? "Success" : "Failed"));
        });
        
        JButton showActionsBtn = new JButton("Show Available Actions");
        showActionsBtn.addActionListener(e -> {
            log("\n--- Available Actions ---");
            String[] actions = orchestrator.getAvailableActions();
            for (String action : actions) {
                log("  - " + action);
            }
        });
        
        JButton summaryBtn = new JButton("Show Turn Summary");
        summaryBtn.addActionListener(e -> {
            log("\n--- Turn Summary ---");
            log(orchestrator.getTurnSummary());
        });
        
        panel.add(rollDiceBtn);
        panel.add(recoverDiceBtn);
        panel.add(showActionsBtn);
        panel.add(summaryBtn);
        
        return panel;
    }
    
    /**
     * Log message to text area
     */
    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    /**
     * Cleanup resources
     */
    private void cleanup() {
        log("\n=== Closing demo ===");
        // Add any cleanup needed
    }
    
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TurnPhaseDemo();
        });
    }
}
