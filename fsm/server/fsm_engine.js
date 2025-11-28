const fs = require('fs');
const path = require('path');

/**
 * FSM Engine - Core state machine for turn/phase management
 * 
 * Responsibilities:
 * - Load FSM definition
 * - Track current state
 * - Evaluate guard conditions
 * - Execute state transitions
 * - Trigger entry/exit actions
 */
class FsmEngine {
  constructor(fsmPath, database, commandCallback = null) {
    this.db = database;
    this.fsm = this.loadFsm(fsmPath);
    this.currentState = this.fsm.config.initial_state;
    this.context = {
      turn: 0,
      phase: 1,
      currentActor: 'FP'
    };
    
    // Callback to send commands to game client
    this.sendCommand = commandCallback || (() => {});
    
    console.log('[FSM] Initialized with state:', this.currentState);
  }
  
  loadFsm(fsmPath) {
    const fsmFile = path.resolve(fsmPath);
    console.log('[FSM] Loading FSM from:', fsmFile);
    
    const data = fs.readFileSync(fsmFile, 'utf8');
    const fsm = JSON.parse(data);
    
    // Build state lookup map
    this.stateMap = new Map();
    for (const module of fsm.modules || []) {
      if (module.type === 'phase_group') {
        for (const phase of module.phases || []) {
          for (const state of phase.states || []) {
            this.stateMap.set(state.id, state);
          }
        }
      } else if (module.type === 'subsystem') {
        for (const state of module.states || []) {
          this.stateMap.set(state.id, state);
        }
      }
    }
    
    console.log(`[FSM] Loaded ${this.stateMap.size} states`);
    return fsm;
  }
  
  getCurrentState() {
    return this.stateMap.get(this.currentState);
  }
  
  getPromptForState(stateId) {
    const state = this.stateMap.get(stateId);
    if (!state || !state.ui || !state.ui.on_enter_prompt_id) {
      return null;
    }
    
    const promptId = state.ui.on_enter_prompt_id;
    let prompt = this.fsm.prompts[promptId];
    
    // Replace template variables
    if (prompt) {
      prompt = prompt.replace('{{turn}}', this.context.turn);
      prompt = prompt.replace('{{phase}}', this.context.phase);
    }
    
    return prompt;
  }
  
  /**
   * Evaluate guard condition against database state
   */
  async evaluateGuard(guardExpression) {
    if (!guardExpression) return true;
    
    console.log('[FSM] Evaluating guard:', guardExpression);
    
    try {
      // Parse and evaluate guard
      // Guards are boolean expressions like: "CORRUPTION >= 12"
      const evaluated = await this.parseGuard(guardExpression);
      console.log('[FSM] Guard result:', evaluated);
      return evaluated;
    } catch (error) {
      console.error('[FSM] Error evaluating guard:', error);
      return false;
    }
  }
  
  async parseGuard(expr) {
    // Handle common guard patterns
    
    // "CORRUPTION >= 12"
    if (expr.includes('CORRUPTION')) {
      const corruption = await this.db.getCorruption();
      const operator = expr.includes('>=') ? '>=' : expr.includes('>') ? '>' : '==';
      const value = parseInt(expr.match(/\d+/)[0]);
      
      switch(operator) {
        case '>=': return corruption >= value;
        case '>': return corruption > value;
        case '==': return corruption == value;
        default: return false;
      }
    }
    
    // "FP_DICE_REMAINING > 0" or "SP_DICE_REMAINING > 0"
    if (expr.includes('DICE_REMAINING')) {
      const faction = expr.includes('FP') ? 'FP' : 'SP';
      const count = await this.db.getDiceRemaining(faction);
      const operator = expr.includes('>') ? '>' : expr.includes('==') ? '==' : '>=';
      const value = parseInt(expr.match(/\d+/)[0]);
      
      switch(operator) {
        case '>': return count > value;
        case '>=': return count >= value;
        case '==': return count == value;
        default: return false;
      }
    }
    
    // "FP_DICE_REMAINING == 0 AND SP_DICE_REMAINING == 0"
    if (expr.includes('AND')) {
      const parts = expr.split('AND').map(p => p.trim());
      const results = await Promise.all(parts.map(p => this.parseGuard(p)));
      return results.every(r => r === true);
    }
    
    // "FP_PASSED_FLAG == true AND SP_PASSED_FLAG == true"
    if (expr.includes('PASSED_FLAG')) {
      const faction = expr.includes('FP') ? 'FP' : 'SP';
      const passed = await this.db.hasPassed(faction);
      return expr.includes('true') ? passed : !passed;
    }
    
    // "FELLOWSHIP_AT_CRACK_OF_DOOM_AND_CORRUPTION_LT_12"
    if (expr.includes('FELLOWSHIP_AT_CRACK_OF_DOOM')) {
      const location = await this.db.getFellowshipLocation();
      const atCrackOfDoom = location === 'crack_of_doom';
      
      if (expr.includes('AND_CORRUPTION_LT_12')) {
        const corruption = await this.db.getCorruption();
        return atCrackOfDoom && corruption < 12;
      }
      
      return atCrackOfDoom;
    }
    
    // "SHADOW_VP >= 10" or "FP_VP >= 4"
    if (expr.includes('_VP')) {
      const faction = expr.includes('SHADOW') ? 'shadow' : 'free_peoples';
      const vp = await this.db.getVictoryPoints(faction);
      const operator = expr.includes('>=') ? '>=' : '==';
      const value = parseInt(expr.match(/\d+/)[0]);
      
      return operator === '>=' ? vp >= value : vp == value;
    }
    
    // "NEXT_ACTOR == 'FP'" or "NEXT_ACTOR == 'SP'"
    if (expr.includes('NEXT_ACTOR')) {
      const expectedActor = expr.includes("'FP'") ? 'FP' : 'SP';
      return this.context.currentActor === expectedActor;
    }
    
    // "COMBAT_FLAG == true"
    if (expr.includes('COMBAT_FLAG')) {
      return await this.db.isCombatActive();
    }
    
    // "FELLOWSHIP_MOVED_FLAG == true"
    if (expr.includes('FELLOWSHIP_MOVED_FLAG')) {
      return await this.db.hasFellowshipMoved();
    }
    
    console.warn('[FSM] Unknown guard expression:', expr);
    return true; // Default to allowing transition
  }
  
  /**
   * Attempt to transition to a new state
   */
  async transitionTo(targetStateId, event = 'AUTO') {
    const currentState = this.getCurrentState();
    const targetState = this.stateMap.get(targetStateId);
    
    if (!targetState) {
      console.error('[FSM] Target state not found:', targetStateId);
      return false;
    }
    
    console.log(`[FSM] Transitioning: ${this.currentState} --[${event}]--> ${targetStateId}`);
    
    // Execute exit actions (if any)
    if (currentState && currentState.exit_actions) {
      for (const action of currentState.exit_actions) {
        await this.executeAction(action);
      }
    }
    
    // Change state
    this.currentState = targetStateId;
    
    // Execute entry actions
    if (targetState.entry_actions) {
      for (const action of targetState.entry_actions) {
        await this.executeAction(action);
      }
    }
    
    return true;
  }
  
  /**
   * Try all transitions from current state
   */
  async tryAutoTransitions() {
    const state = this.getCurrentState();
    if (!state || !state.transitions) return false;
    
    // SAFETY: Block auto-transitions from states that should require player confirmation
    const blockAutoTransitionStates = [
      'PHASE_4_ACTION_ROLL_START',
      'PHASE_4_END',
      'PHASE_5_ACTION_RESOLUTION_START',
      'PHASE_5_FP_ACTION',
      'PHASE_5_SP_ACTION'
    ];
    
    if (blockAutoTransitionStates.includes(this.currentState)) {
      console.log('[FSM] Auto-transitions blocked for state:', this.currentState);
      return false;
    }
    
    for (const transition of state.transitions) {
      // Only process AUTO transitions
      if (transition.event !== 'AUTO') continue;
      
      // Evaluate guard
      const guardPassed = await this.evaluateGuard(transition.guard);
      if (!guardPassed) continue;
      
      // Execute transition
      await this.transitionTo(transition.target, 'AUTO');
      return true;
    }
    
    return false;
  }
  
  /**
   * Get valid events for current state
   */
  getValidEvents() {
    const state = this.getCurrentState();
    if (!state || !state.transitions) return [];
    
    return state.transitions
      .filter(t => t.event !== 'AUTO')
      .map(t => t.event);
  }
  
  /**
   * Handle player event
   */
  async handleEvent(event) {
    const state = this.getCurrentState();
    if (!state || !state.transitions) return false;
    
    // Find matching transition
    const transition = state.transitions.find(t => t.event === event);
    if (!transition) {
      console.log('[FSM] No transition for event:', event);
      return false;
    }
    
    // Evaluate guard
    const guardPassed = await this.evaluateGuard(transition.guard);
    if (!guardPassed) {
      console.log('[FSM] Guard failed for event:', event);
      return false;
    }
    
    // Execute transition
    await this.transitionTo(transition.target, event);
    return true;
  }
  
  /**
   * Execute FSM action (sends to game client or updates context)
   */
  async executeAction(action) {
    console.log('[FSM] Executing action:', action);
    
    // Setup Phase Actions
    if (action === 'PROMPT_SETUP_GAME_OPTIONS') {
      console.log('[Action] Prompting game options setup...');
      this.sendCommand('<auto> ⚙️ Setup: Choose expansions, Treebeard, and player count');
      return;
    }
    
    if (action === 'PROMPT_SETUP_SOLO_SIDE_SELECTION') {
      console.log('[Action] Prompting solo side selection...');
      this.sendCommand('<auto> 🎮 Solo game: Choose Free Peoples or Shadow');
      return;
    }
    
    if (action === 'PROMPT_SETUP_CONNECTION_MODE') {
      console.log('[Action] Prompting connection mode...');
      this.sendCommand('<auto> 🌐 Multiplayer: Host or join a game');
      return;
    }
    
    if (action === 'PROMPT_SETUP_CLIENT_CONNECT') {
      console.log('[Action] Prompting client connection...');
      this.sendCommand('<auto> 🔌 Enter host information to join');
      return;
    }
    
    if (action === 'PROMPT_SETUP_LOBBY_HOST') {
      console.log('[Action] Showing host lobby...');
      this.sendCommand('<auto> 🛡️ Host Lobby: Waiting for players to join');
      return;
    }
    
    if (action === 'PROMPT_SETUP_LOBBY_CLIENT') {
      console.log('[Action] Showing client lobby...');
      this.sendCommand('<auto> 👥 Client Lobby: Waiting for host to start');
      return;
    }
    
    if (action === 'PROMPT_SETUP_SIDE_ASSIGNMENT') {
      console.log('[Action] Prompting side assignment...');
      this.sendCommand('<auto> ⚔️ Side Assignment: Assign players to factions');
      return;
    }
    
    if (action === 'ASSIGN_SOLO_PLAYER_TO_FP') {
      console.log('[Action] Assigning solo player to Free Peoples...');
      this.sendCommand('<auto> ✓ You will play as Free Peoples');
      // TODO: Store assignment in database
      return;
    }
    
    if (action === 'ASSIGN_SOLO_PLAYER_TO_SHADOW') {
      console.log('[Action] Assigning solo player to Shadow...');
      this.sendCommand('<auto> ✓ You will play as Shadow');
      // TODO: Store assignment in database
      return;
    }
    
    // Game Initialization Actions
    if (action === 'PROMPT_GAME_INITIALIZATION') {
      this.sendCommand('<auto> ═══════════════════════════════════');
      this.sendCommand('<auto> 🎲 Initializing Game...');
      this.sendCommand('<auto> ═══════════════════════════════════');
      return;
    }
    
    if (action === 'VALIDATE_SIDE_ASSIGNMENTS') {
      console.log('[Action] Validating side assignments...');
      this.sendCommand('<auto> ✓ Validating player assignments...');
      // Note: Validation happens in Java GameInitializationService
      // FSM just logs progress messages
      return;
    }
    
    if (action === 'PERSIST_PLAYER_FACTION_ASSIGNMENTS') {
      console.log('[Action] Persisting player-faction assignments...');
      this.sendCommand('<auto> ✓ Saving player assignments to database...');
      // Note: Persistence happens in Java GameInitializationService.persistPlayerFactionAssignments()
      // Game client calls this after setup phase
      return;
    }
    
    if (action === 'INITIALIZE_FP_RESOURCES') {
      console.log('[Action] Initializing Free Peoples resources...');
      this.sendCommand('<auto> ✓ Setting up Free Peoples cards and dice...');
      // Note: Resources created in Game.bitInit()
      // Service method validates they exist
      return;
    }
    
    if (action === 'INITIALIZE_SP_RESOURCES') {
      console.log('[Action] Initializing Shadow resources...');
      this.sendCommand('<auto> ✓ Setting up Shadow cards and dice...');
      // Note: Resources created in Game.bitInit()
      // Service method validates they exist
      return;
    }
    
    if (action === 'SETUP_INITIAL_BOARD_STATE') {
      console.log('[Action] Setting up initial board state...');
      this.sendCommand('<auto> ✓ Initializing regions and political tracks...');
      // Note: Board setup happens in Game.areaInit() and Game.bitInit()
      // Service method validates state is correct
      return;
    }
    
    if (action === 'PLACE_INITIAL_PIECES') {
      console.log('[Action] Placing initial pieces...');
      this.sendCommand('<auto> ✓ Placing starting units and Fellowship...');
      // Note: Pieces placed in Game.hybridPieceInit()
      // Service method validates placement
      return;
    }
    
    if (action === 'INITIALIZATION_COMPLETE') {
      console.log('[Action] Game initialization complete!');
      this.sendCommand('<auto> ═══════════════════════════════════');
      this.sendCommand('<auto> ✅ Game initialization complete!');
      this.sendCommand('<auto> ═══════════════════════════════════');
      this.sendCommand('<auto> Ready to begin Turn 1');
      // Note: Completion steps in Game.AnonymizeDecks() and GameInitializationService.completeInitialization()
      return;
    }
    
    // Context updates
    if (action === 'INCREMENT_TURN_COUNTER') {
      this.context.turn++;
      return;
    }
    
    if (action === 'RESET_PHASE_TRACKER') {
      this.context.phase = 1;
      return;
    }
    
    if (action === 'INIT_ACTION_TURN_ORDER_FP_FIRST') {
      this.context.currentActor = 'FP';
      return;
    }
    
    if (action === 'SET_FP_PASSED_FLAG') {
      await this.db.setPassedFlag('FP', true);
      return;
    }
    
    if (action === 'SET_SP_PASSED_FLAG') {
      await this.db.setPassedFlag('SP', true);
      return;
    }
    
    if (action === 'CLEAR_ACTION_FLAGS') {
      await this.db.clearActionFlags();
      return;
    }
    
    // Send interactive menus for phases
    if (action === 'PROMPT_PHASE_1_START') {
      console.log('[Action] PROMPT_PHASE_1_START - sending menu');
      await this.sendPhase1Menu();
      return;
    }
    
    if (action === 'PROMPT_PHASE_2_START') {
      await this.sendPhase2Menu();
      return;
    }
    
    if (action === 'PROMPT_PHASE_3_START') {
      await this.sendPhase3Menu();
      return;
    }
    
    if (action === 'PROMPT_PHASE_4_START') {
      await this.sendPhase4Menu();
      return;
    }
    
    // Map FSM actions to game client commands
    const commandMap = {
      // Phase 1: Draw cards
      'FP_DRAW_CHARACTER_CARD': 'F7',
      'FP_DRAW_STRATEGY_CARD': 'F8',
      'SP_DRAW_CHARACTER_CARD': 'F11',
      'SP_DRAW_STRATEGY_CARD': 'F12',
      
      // Phase 4: Roll action dice
      'ROLL_FP_ACTION_DICE': 'FSM_ROLL_FP_DICE',
      'ROLL_SP_ACTION_DICE': 'FSM_ROLL_SP_DICE',
      
      // Hunt mechanics
      'DRAW_HUNT_TILE': 'Ctrl+H',
      'REVEAL_FELLOWSHIP': 'Ctrl+F',
      
      // Combat
      'END_COMBAT_ROUND': 'Ctrl+Esc',
      'CHOOSE_COMBAT_CARD': 'F1',
      'PLAY_COMBAT_CARD': 'F2',
      
      // End of turn
      'RECOVER_ALL_FP_ACTION_DICE': 'Ctrl+9',
      'RECOVER_ALL_SP_ACTION_DICE': 'Ctrl+0',
      
      // Prompts (special handling)
      'PROMPT_PHASE_1_START': '<auto> 🌅 Turn ${turn} — Phase 1: Recover Dice & Draw Cards',
      'PROMPT_PHASE_2_START': '<auto> 🌍 Phase 2: Fellowship Phase',
      'PROMPT_PHASE_3_START': '<auto> 👁️ Phase 3: Hunt Allocation',
      'PROMPT_PHASE_4_START': '<auto> 🎲 Phase 4: Action Resolution',
      'PROMPT_PHASE_5_START': '<auto> ⚔️ Phase 5: Victory Check',
    };
    
    const command = commandMap[action];
    
    if (command) {
      // Handle template strings (replace ${turn} with actual turn number)
      const finalCommand = command.includes('${turn}') 
        ? command.replace('${turn}', this.context.turn)
        : command;
      
      console.log('[Action →] Executing:', action, '→', finalCommand);
      this.sendCommand(finalCommand);
      
      // Add small delay to ensure commands don't get batched/lost
      await new Promise(resolve => setTimeout(resolve, 100));
      return;
    }
    
    // Unhandled action
    console.log('[Action] Unhandled (no mapping):', action);
  }
  
  /**
   * Send Phase 1 interactive menu
   */
  async sendPhase1Menu() {
    const menu = [];
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto> 🌅 PHASE 1: Recover Dice & Draw Cards');
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto>');
    
    // Check hand limits
    const fpHandSize = await this.db.getHandSize('FP');
    const spHandSize = await this.db.getHandSize('SP');
    const handLimit = this.fsm.config.hand_limit || 6;
    
    // FP hand status
    if (fpHandSize > handLimit) {
      menu.push(`<auto> ⚠️  FREE PEOPLES: Hand size ${fpHandSize}/${handLimit} - DISCARD REQUIRED`);
    } else {
      menu.push(`<auto> ✅ FREE PEOPLES: Hand size ${fpHandSize}/${handLimit}`);
    }
    
    // SP hand status
    if (spHandSize > handLimit) {
      menu.push(`<auto> ⚠️  SHADOW: Hand size ${spHandSize}/${handLimit} - DISCARD REQUIRED`);
    } else {
      menu.push(`<auto> ✅ SHADOW: Hand size ${spHandSize}/${handLimit}`);
    }
    
    menu.push('<auto>');
    menu.push('<auto> Actions automatically executed:');
    menu.push('<auto>  • Recovered all FP action dice');
    menu.push('<auto>  • Recovered all Shadow action dice');
    menu.push('<auto>  • Drew 1 FP character card');
    menu.push('<auto>  • Drew 1 FP strategy card');
    menu.push('<auto>  • Drew 1 Shadow character card');
    menu.push('<auto>  • Drew 1 Shadow strategy card');
    menu.push('<auto>');
    
    if (fpHandSize > handLimit || spHandSize > handLimit) {
      menu.push('<auto> ⚠️  DISCARD CARDS TO HAND LIMIT BEFORE CONTINUING');
      menu.push('<auto>');
    }
    
    menu.push('<auto> When ready, click status bar to continue');
    menu.push('<auto> ═══════════════════════════════════');
    
    // Send each line
    for (const line of menu) {
      this.sendCommand(line);
    }
  }
  
  /**
   * Send Phase 2 interactive menu
   */
  async sendPhase2Menu() {
    const menu = [];
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto> 🌍 PHASE 2: Fellowship Phase');
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto>');
    
    // Check Fellowship status
    const fellowship = await this.db.getFellowshipLocation();
    const isRevealed = await this.db.isFellowshipRevealed();
    const corruption = await this.db.getCorruption();
    
    menu.push(`<auto> Fellowship Location: ${fellowship.location || 'Rivendell'}`);
    menu.push(`<auto> Status: ${isRevealed ? '👁️ REVEALED' : '🌑 Hidden'}`);
    menu.push(`<auto> Corruption: ${corruption}/12`);
    menu.push('<auto>');
    menu.push('<auto> Available Actions:');
    
    if (isRevealed) {
      menu.push('<auto>  1. Hide Fellowship (if possible)');
    } else {
      menu.push('<auto>  1. Declare Fellowship (reveal)');
    }
    
    menu.push('<auto>  2. Change Guide (if Fellowship has moved)');
    menu.push('<auto>  3. Move Fellowship (during action phase)');
    menu.push('<auto>  4. No changes - Continue');
    menu.push('<auto>');
    menu.push('<auto> Make your decisions, then click status bar');
    menu.push('<auto> ═══════════════════════════════════');
    
    for (const line of menu) {
      this.sendCommand(line);
    }
  }
  
  /**
   * Send Phase 3 interactive menu
   */
  async sendPhase3Menu() {
    const menu = [];
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto> 👁️ PHASE 3: Hunt Allocation');
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto>');
    
    // Check Eye results
    const eyeResults = await this.db.getEyeDiceCount('SP');
    const currentHunt = await this.db.getHuntDiceAllocated();
    
    menu.push(`<auto> Eye results rolled: ${eyeResults}`);
    menu.push(`<auto> Currently allocated to Hunt: ${currentHunt}`);
    menu.push('<auto>');
    menu.push('<auto> SHADOW PLAYER Actions:');
    menu.push(`<auto>  1. Allocate ${eyeResults} dice to Hunt Box`);
    menu.push('<auto>  2. Allocate some dice to Hunt (partial)');
    menu.push('<auto>  3. Allocate no dice to Hunt');
    menu.push('<auto>');
    menu.push('<auto> Note: Eye dice in Hunt Box will be used when');
    menu.push('<auto> Fellowship moves during action resolution.');
    menu.push('<auto>');
    menu.push('<auto> Make allocation, then click status bar');
    menu.push('<auto> ═══════════════════════════════════');
    
    for (const line of menu) {
      this.sendCommand(line);
    }
  }
  
  /**
   * Send Phase 4 interactive menu
   */
  async sendPhase4Menu() {
    const menu = [];
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto> 🎲 PHASE 4: Action Roll');
    menu.push('<auto> ═══════════════════════════════════');
    menu.push('<auto>');
    
    // Get dice counts
    const fpDiceCount = await this.db.getDiceCount('FP');
    const spDiceCount = await this.db.getDiceCount('SP');
    
    menu.push(`<auto> FP Action Dice: ${fpDiceCount}`);
    menu.push(`<auto> Shadow Action Dice: ${spDiceCount}`);
    menu.push('<auto>');
    menu.push('<auto> Rolling dice now...');
    menu.push('<auto>');
    
    for (const line of menu) {
      this.sendCommand(line);
    }
    
    // Actually roll the dice
    this.sendCommand('FSM_ROLL_FP_DICE');
    this.sendCommand('FSM_ROLL_SP_DICE');
    
    // Show results prompt
    menu.length = 0;
    menu.push('<auto>');
    menu.push('<auto> Dice rolled! Review your results:');
    menu.push('<auto>  • Character = Use for character/army movement');
    menu.push('<auto>  • Army = Recruit units or attack');
    menu.push('<auto>  • Muster = Political track or reinforcements');
    menu.push('<auto>  • Eye = Shadow only - Hunt allocation');
    menu.push('<auto>  • Will of West = FP only - Political power');
    menu.push('<auto>');
    menu.push('<auto> When ready to use dice, click status bar');
    menu.push('<auto> ═══════════════════════════════════');
    
    for (const line of menu) {
      this.sendCommand(line);
    }
  }
}

module.exports = FsmEngine;
