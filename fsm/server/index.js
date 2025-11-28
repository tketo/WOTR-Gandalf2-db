#!/usr/bin/env node

const net = require('net');
const path = require('path');
const FsmEngine = require('./fsm_engine');
const Database = require('./database');

/**
 * WOTR FSM Server - Turn/Phase Orchestrator
 * 
 * Minimal implementation focused on:
 * - Loading FSM definition
 * - Managing turn/phase transitions
 * - Evaluating guards against database
 * - (Future: Sending commands to game client)
 */
class WotrFsmServer {
  constructor(options = {}) {
    this.dbPath = options.dbPath || '../../wotr_game.db';
    this.fsmPath = options.fsmPath || '../wotr_fsm.json';
    this.port = options.port || 8080;
    
    console.log('=== WOTR FSM Server Starting ===');
    console.log(`Database: ${this.dbPath}`);
    console.log(`FSM: ${this.fsmPath}`);
    console.log(`Port: ${this.port}`);
    console.log('');
    
    // Initialize database
    this.db = new Database(this.dbPath);
    
    // Initialize FSM engine with command callback
    this.fsm = new FsmEngine(this.fsmPath, this.db, (command) => {
      // Callback: send command to all connected clients
      this.broadcastToClients(command);
    });
    
    // Track if server is running
    this.running = false;
    
    // Track if game has started
    this.gameStarted = false;
    
    // TCP server and client connections
    this.tcpServer = null;
    this.clients = []; // Array of {socket, role, clientId}
  }
  
  /**
   * Start the FSM server
   */
  async start() {
    this.running = true;
    
    // Start TCP server for client connections
    this.startTcpServer();
    
    console.log('[Server] Waiting for client connection before starting game...\n');
    
    // Enter initial state but DON'T start main loop yet
    await this.enterCurrentState();
    
    console.log('[Server] FSM ready. Connect a client to begin.\n');
    
    // Main loop will be started when client connects
    // (see startTcpServer where we call startGameAfterConnection)
  }
  
  /**
   * Enter current state and show prompt
   */
  async enterCurrentState() {
    const state = this.fsm.getCurrentState();
    console.log(`\n=== STATE: ${this.fsm.currentState} ===`);
    console.log(`Label: ${state.label || 'N/A'}`);
    console.log(`Description: ${state.description || 'N/A'}`);
    
    // Show prompt if available
    const prompt = this.fsm.getPromptForState(this.fsm.currentState);
    if (prompt) {
      console.log(`\n ${prompt}`);
    }
    
    // Show valid events
    const events = this.fsm.getValidEvents();
    if (events.length > 0) {
      console.log(`\nWaiting for events: ${events.join(', ')}`);
      
      // Send valid events to client
      this.broadcastToClients(`<valid_events> ${events.join(',')}`);
    }
  }
  
  /**
   * Main FSM loop - try auto transitions
   */
  async mainLoop() {
    while (this.running) {
      // Try to process AUTO transitions
      const transitioned = await this.fsm.tryAutoTransitions();
      
      if (transitioned) {
        // We transitioned, show new state
        await this.enterCurrentState();
        
        // Delay between auto-transitions for visibility
        await this.sleep(2000); // 2 seconds between phases
      } else {
        // No auto transition available, wait for player input
        const events = this.fsm.getValidEvents();
        
        if (events.length === 0) {
          // No events possible, FSM is stuck or ended
          const state = this.fsm.getCurrentState();
          if (state.kind === 'final') {
            console.log('\n Game ended!');
            this.stop();
          } else {
            console.log('\n  No transitions available from this state');
            this.stop();
          }
          break;
        }
        
        // Wait for external input (will be implemented with game client connection)
        console.log('\n  Waiting for player input...');
        await this.sleep(5000); // Poll every 5 seconds
      }
    }
  }
  
  /**
   * Handle player event (called externally or via input)
   */
  async handlePlayerEvent(event) {
    console.log(`\n[Input] Received event: ${event}`);
    
    const success = await this.fsm.handleEvent(event);
    
    if (success) {
      await this.enterCurrentState();
    } else {
      console.log(` Event '${event}' not valid in current state`);
    }
    
    return success;
  }
  
  /**
   * Start TCP server to listen for client connections
   */
  startTcpServer() {
    this.tcpServer = net.createServer((socket) => {
      console.log(`[TCP] Client connected: ${socket.remoteAddress}:${socket.remotePort}`);
      
      // Determine role: first client is host, others are clients
      const role = this.clients.length === 0 ? 'host' : 'client';
      const clientId = `player${this.clients.length + 1}`;
      const clientInfo = { socket, role, clientId };
      
      this.clients.push(clientInfo);
      console.log(`[Server] Client assigned role: ${role} (${clientId})`);
      
      // Start game on first client connection
      if (!this.gameStarted) {
        this.gameStarted = true;
        console.log('[Server] First client connected - starting game!\n');
        // Start the main loop asynchronously
        this.mainLoop();
      }
      
      // Set up data handler
      let buffer = '';
      socket.on('data', (data) => {
        buffer += data.toString();
        
        // Process complete lines
        let newlineIndex;
        while ((newlineIndex = buffer.indexOf('\n')) !== -1) {
          const line = buffer.substring(0, newlineIndex).trim();
          buffer = buffer.substring(newlineIndex + 1);
          
          if (line.length > 0) {
            this.handleClientMessage(socket, line);
          }
        }
      });
      
      socket.on('end', () => {
        console.log('[TCP] Client disconnected');
        this.clients = this.clients.filter(c => c.socket !== socket);
      });
      
      socket.on('error', (err) => {
        console.error('[TCP] Socket error:', err.message);
      });
      
      // Send welcome message
      this.sendToClient(socket, '<game> Connected to FSM server');
      
      // Send current FSM state and valid events to newly connected client
      const state = this.fsm.getCurrentState();
      const prompt = this.fsm.getPromptForState(this.fsm.currentState);
      if (prompt) {
        this.sendToClient(socket, '<auto> ' + prompt);
      }
      
      // Send valid events (will be filtered for non-host clients)
      const events = this.fsm.getValidEvents();
      if (events.length > 0) {
        // Filter for this specific client
        if (role === 'client' && this.fsm.currentState === 'SETUP_GAME_OPTIONS') {
          // Clients can view sides and confirm, but not toggle expansions
          const filteredEvents = events.filter(e => 
            !e.includes('TOGGLE')
          );
          if (filteredEvents.length > 0) {
            this.sendToClient(socket, `<valid_events> ${filteredEvents.join(',')}`);
          }
        } else {
          this.sendToClient(socket, `<valid_events> ${events.join(',')}`);
        }
      }
    });
    
    this.tcpServer.listen(this.port, () => {
      console.log(`[TCP] Server listening on port ${this.port}`);
    });
  }
  
  /**
   * Handle incoming message from client
   */
  handleClientMessage(socket, message) {
    console.log(`[TCP] <-`, message);
    
    // Parse EVENT: messages
    if (message.startsWith('EVENT:')) {
      let event = message.substring(6);
      
      // Map generic PHASE_COMPLETE to current state's expected event
      if (event === 'PHASE_COMPLETE') {
        event = this.mapPhaseCompleteToExpectedEvent();
      }
      
      this.handlePlayerEvent(event);
    }
  }
  
  /**
   * Map PHASE_COMPLETE to the specific event the current state expects
   */
  mapPhaseCompleteToExpectedEvent() {
    const validEvents = this.fsm.getValidEvents();
    
    if (validEvents.length === 0) {
      console.log('[Server] No valid events in current state');
      return 'PHASE_COMPLETE';
    }
    
    // Return the first valid event (usually there's only one per waiting state)
    const mappedEvent = validEvents[0];
    console.log(`[Server] Mapping PHASE_COMPLETE → ${mappedEvent}`);
    return mappedEvent;
  }
  
  /**
   * Send command to all connected clients
   */
  broadcastToClients(command) {
    this.clients.forEach(client => {
      // Filter valid_events for non-host clients in SETUP_GAME_OPTIONS
      if (command.startsWith('<valid_events>') && 
          client.role === 'client' && 
          this.fsm.currentState === 'SETUP_GAME_OPTIONS') {
        // Remove expansion toggles for clients (they can still view sides)
        let events = command.substring(14).trim().split(',');
        events = events.filter(e => !e.includes('TOGGLE'));
        if (events.length > 0) {
          this.sendToClient(client.socket, `<valid_events> ${events.join(',')}`);
        }
      } else {
        this.sendToClient(client.socket, command);
      }
    });
  }
  
  /**
   * Send command to specific client
   */
  sendToClient(socket, command) {
    if (socket && !socket.destroyed) {
      socket.write(command + '\n');
      console.log(`[TCP] ->`, command);
    }
  }
  
  /**
   * Stop the server
   */
  stop() {
    console.log('\n[Server] Stopping...');
    this.running = false;
    
    // Close all client connections
    this.clients.forEach(client => {
      client.socket.end();
    });
    
    // Close TCP server
    if (this.tcpServer) {
      this.tcpServer.close();
    }
    
    this.db.close();
  }
  
  /**
   * Helper for delays
   */
  sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

// ===== CLI Entry Point =====

if (require.main === module) {
  // Parse command line args
  const args = process.argv.slice(2);
  const options = {};
  
  for (let i = 0; i < args.length; i += 2) {
    const key = args[i].replace('--', '');
    const value = args[i + 1];
    options[key] = value;
  }
  
  // Create and start server
  const server = new WotrFsmServer(options);
  
  // Handle shutdown gracefully
  process.on('SIGINT', () => {
    console.log('\n\nReceived SIGINT, shutting down...');
    server.stop();
    process.exit(0);
  });
  
  // Start!
  server.start().catch(err => {
    console.error('Fatal error:', err);
    process.exit(1);
  });
}

module.exports = WotrFsmServer;