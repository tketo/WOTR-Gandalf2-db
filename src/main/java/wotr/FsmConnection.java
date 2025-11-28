package wotr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Connection to FSM (Finite State Machine) Server
 * Handles turn/phase arbitration and game flow orchestration
 * 
 * The FSM server sends macro commands (F7, Ctrl+R, etc.) that the client executes.
 * The client sends event notifications (CHARACTER_MOVED, DIE_SELECTED, etc.) back to FSM.
 */
public class FsmConnection extends Thread {
    private Game game;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;
    private boolean running = true;
    
    private String serverHost;
    private int serverPort;
    
    // Track valid events from FSM
    private String[] validEvents = new String[0];
    
    public FsmConnection(Game game) {
        this.game = game;
        setDaemon(true); // Don't prevent JVM shutdown
        setName("FSM-Connection");
    }
    
    /**
     * Connect to FSM server
     */
    public boolean connect(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
        
        try {
            System.out.println("[FSM] Connecting to " + host + ":" + port + "...");
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            
            // Start listening thread
            start();
            
            System.out.println("[FSM] Connected successfully!");
            game.controls.chat.write("<game> Connected to FSM server at " + host + ":" + port);
            
            return true;
        } catch (Exception e) {
            System.err.println("[FSM] Connection failed: " + e.getMessage());
            game.controls.chat.write("<game> FSM connection failed: " + e.getMessage());
            connected = false;
            return false;
        }
    }
    
    /**
     * Disconnect from FSM server
     */
    public void disconnect() {
        running = false;
        connected = false;
        
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("[FSM] Disconnected");
            game.controls.chat.write("<game> Disconnected from FSM server");
        } catch (Exception e) {
            System.err.println("[FSM] Error during disconnect: " + e.getMessage());
        }
    }
    
    /**
     * Main listening loop - receives commands from FSM server
     */
    @Override
    public void run() {
        System.out.println("[FSM] Listening thread started");
        
        while (running && connected) {
            try {
                String command = in.readLine();
                
                if (command == null) {
                    // Connection closed
                    System.out.println("[FSM] Server closed connection");
                    break;
                }
                
                System.out.println("[FSM] <- Received: " + command);
                
                // Handle valid events list from FSM
                if (command.startsWith("<valid_events>")) {
                    handleValidEvents(command);
                } 
                // Handle phase announcements - update status bar
                else if (command.startsWith("<auto>")) {
                    // Extract phase name from message
                    String message = command.substring(6).trim();
                    updateStatusBar(message);
                    
                    // Also show in chat
                    game.interpreter.execute(command);
                } else {
                    // Execute command through interpreter
                    // FSM sends existing macro commands like "F7", "Ctrl+R", etc.
                    game.interpreter.execute(command);
                }
                
            } catch (Exception e) {
                if (running) {
                    System.err.println("[FSM] Error reading from server: " + e.getMessage());
                }
                break;
            }
        }
        
        // Clean up after disconnect
        connected = false;
        System.out.println("[FSM] Listening thread stopped");
    }
    
    /**
     * Send event notification to FSM server
     * Events inform the FSM about player actions (die selected, character moved, etc.)
     */
    public void sendEvent(String eventName) {
        if (!connected || out == null) {
            return;
        }
        
        try {
            String message = "EVENT:" + eventName;
            out.println(message);
            System.out.println("[FSM] -> Sent: " + message);
        } catch (Exception e) {
            System.err.println("[FSM] Error sending event: " + e.getMessage());
        }
    }
    
    /**
     * Check if currently connected to FSM server
     */
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }
    
    /**
     * Update status with phase information (logs to console)
     */
    private void updateStatusBar(String message) {
        System.out.println("[FSM] Phase: " + message);
    }
    
    /**
     * Handle valid events message from FSM
     */
    private void handleValidEvents(String command) {
        // Format: "<valid_events> EVENT1,EVENT2,EVENT3"
        String eventsStr = command.substring(14).trim();
        
        if (eventsStr.isEmpty()) {
            validEvents = new String[0];
            return;
        }
        
        validEvents = eventsStr.split(",");
        
        System.out.println("[FSM] Valid events updated: " + eventsStr);
        
        // Display in chat
        StringBuilder message = new StringBuilder("<auto> Valid actions: ");
        for (int i = 0; i < validEvents.length; i++) {
            if (i > 0) message.append(", ");
            message.append(validEvents[i]);
        }
        
        game.interpreter.execute(message.toString());
    }
    
    /**
     * Get current valid events from FSM
     */
    public String[] getValidEvents() {
        return validEvents;
    }
    
    /**
     * Get server address
     */
    public String getServerAddress() {
        if (connected) {
            return serverHost + ":" + serverPort;
        }
        return "Not connected";
    }
}
