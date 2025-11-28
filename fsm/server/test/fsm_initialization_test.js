#!/usr/bin/env node

/**
 * FSM Initialization Test Suite
 * Tests the granular game initialization state transitions
 */

const assert = require('assert');
const path = require('path');
const FsmEngine = require('../fsm_engine');
const Database = require('../database');

class FsmInitializationTest {
  constructor() {
    this.testResults = [];
    this.testCount = 0;
    this.passCount = 0;
    this.failCount = 0;
  }

  /**
   * Initialize FSM for testing
   */
  async setup() {
    console.log('=== FSM Initialization Test Suite ===\n');
    
    // Initialize database and FSM
    const dbPath = path.resolve(__dirname, '../../../wotr_game.db');
    const fsmPath = path.resolve(__dirname, '../../wotr_fsm.json');
    
    this.db = new Database(dbPath);
    this.commandLog = [];
    
    // Create FSM with command callback that logs
    this.fsm = new FsmEngine(fsmPath, this.db, (command) => {
      this.commandLog.push(command);
      console.log(`  [Command] ${command}`);
    });
    
    console.log('[Setup] FSM initialized\n');
  }

  /**
   * Helper: Assert with test tracking
   */
  test(name, fn) {
    this.testCount++;
    try {
      fn();
      this.passCount++;
      console.log(`✅ PASS: ${name}`);
      this.testResults.push({ name, status: 'PASS' });
    } catch (error) {
      this.failCount++;
      console.log(`❌ FAIL: ${name}`);
      console.log(`   Error: ${error.message}\n`);
      this.testResults.push({ name, status: 'FAIL', error: error.message });
    }
  }

  /**
   * Helper: Async test wrapper
   */
  async testAsync(name, fn) {
    this.testCount++;
    try {
      await fn();
      this.passCount++;
      console.log(`✅ PASS: ${name}`);
      this.testResults.push({ name, status: 'PASS' });
    } catch (error) {
      this.failCount++;
      console.log(`❌ FAIL: ${name}`);
      console.log(`   Error: ${error.message}\n`);
      this.testResults.push({ name, status: 'FAIL', error: error.message });
    }
  }

  /**
   * Test Suite: Initialization States Exist
   */
  async testInitializationStatesExist() {
    console.log('\n--- Test Group: Initialization States Exist ---\n');

    const expectedStates = [
      'PROMPT_GAME_INITIALIZATION',
      'VALIDATE_SIDE_ASSIGNMENTS',
      'PERSIST_PLAYER_FACTION_ASSIGNMENTS',
      'INITIALIZE_FP_RESOURCES',
      'INITIALIZE_SP_RESOURCES',
      'SETUP_INITIAL_BOARD_STATE',
      'PLACE_INITIAL_PIECES',
      'INITIALIZATION_COMPLETE'
    ];

    for (const stateId of expectedStates) {
      this.test(`State "${stateId}" exists in FSM`, () => {
        const state = this.fsm.stateMap.get(stateId);
        assert.ok(state, `State ${stateId} should exist in FSM`);
      });
    }
  }

  /**
   * Test Suite: Initialization Flow
   */
  async testInitializationFlow() {
    console.log('\n--- Test Group: Initialization State Transitions ---\n');

    // Reset FSM to setup state (simulate solo game start)
    this.fsm.currentState = 'PROMPT_GAME_INITIALIZATION';
    this.commandLog = [];

    await this.testAsync('Transition from PROMPT_GAME_INITIALIZATION to VALIDATE_SIDE_ASSIGNMENTS', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'VALIDATE_SIDE_ASSIGNMENTS',
        'Should transition to VALIDATE_SIDE_ASSIGNMENTS');
    });

    await this.testAsync('Transition from VALIDATE_SIDE_ASSIGNMENTS to PERSIST_PLAYER_FACTION_ASSIGNMENTS', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'PERSIST_PLAYER_FACTION_ASSIGNMENTS',
        'Should transition to PERSIST_PLAYER_FACTION_ASSIGNMENTS');
    });

    await this.testAsync('Transition from PERSIST_PLAYER_FACTION_ASSIGNMENTS to INITIALIZE_FP_RESOURCES', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'INITIALIZE_FP_RESOURCES',
        'Should transition to INITIALIZE_FP_RESOURCES');
    });

    await this.testAsync('Transition from INITIALIZE_FP_RESOURCES to INITIALIZE_SP_RESOURCES', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'INITIALIZE_SP_RESOURCES',
        'Should transition to INITIALIZE_SP_RESOURCES');
    });

    await this.testAsync('Transition from INITIALIZE_SP_RESOURCES to SETUP_INITIAL_BOARD_STATE', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'SETUP_INITIAL_BOARD_STATE',
        'Should transition to SETUP_INITIAL_BOARD_STATE');
    });

    await this.testAsync('Transition from SETUP_INITIAL_BOARD_STATE to PLACE_INITIAL_PIECES', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'PLACE_INITIAL_PIECES',
        'Should transition to PLACE_INITIAL_PIECES');
    });

    await this.testAsync('Transition from PLACE_INITIAL_PIECES to INITIALIZATION_COMPLETE', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'INITIALIZATION_COMPLETE',
        'Should transition to INITIALIZATION_COMPLETE');
    });

    await this.testAsync('Transition from INITIALIZATION_COMPLETE to TURN_START', async () => {
      const success = await this.fsm.tryAutoTransitions();
      assert.strictEqual(success, true, 'Should auto-transition');
      assert.strictEqual(this.fsm.currentState, 'TURN_START',
        'Should transition to TURN_START');
    });
  }

  /**
   * Test Suite: Entry Actions
   */
  async testEntryActions() {
    console.log('\n--- Test Group: Entry Actions Execute ---\n');

    // Reset and test each state's entry action
    this.commandLog = [];
    this.fsm.currentState = 'PROMPT_GAME_INITIALIZATION';

    await this.testAsync('PROMPT_GAME_INITIALIZATION sends initialization message', async () => {
      const state = this.fsm.getCurrentState();
      for (const action of state.entry_actions) {
        await this.fsm.executeAction(action);
      }
      
      const hasInitMessage = this.commandLog.some(cmd => 
        cmd.includes('Initializing Game') || cmd.includes('🎲')
      );
      assert.ok(hasInitMessage, 'Should send initialization message');
    });

    this.commandLog = [];
    this.fsm.currentState = 'VALIDATE_SIDE_ASSIGNMENTS';

    await this.testAsync('VALIDATE_SIDE_ASSIGNMENTS sends validation message', async () => {
      const state = this.fsm.getCurrentState();
      for (const action of state.entry_actions) {
        await this.fsm.executeAction(action);
      }
      
      const hasValidationMessage = this.commandLog.some(cmd => 
        cmd.includes('Validating player assignments')
      );
      assert.ok(hasValidationMessage, 'Should send validation message');
    });

    this.commandLog = [];
    this.fsm.currentState = 'INITIALIZATION_COMPLETE';

    await this.testAsync('INITIALIZATION_COMPLETE sends completion message', async () => {
      const state = this.fsm.getCurrentState();
      for (const action of state.entry_actions) {
        await this.fsm.executeAction(action);
      }
      
      const hasCompleteMessage = this.commandLog.some(cmd => 
        cmd.includes('initialization complete') || cmd.includes('✅')
      );
      assert.ok(hasCompleteMessage, 'Should send completion message');
    });
  }

  /**
   * Test Suite: Complete Initialization Sequence
   */
  async testCompleteSequence() {
    console.log('\n--- Test Group: Complete Initialization Sequence ---\n');

    // Reset to start of initialization
    this.fsm.currentState = 'PROMPT_GAME_INITIALIZATION';
    this.commandLog = [];

    const stateSequence = [];
    let iterations = 0;
    const maxIterations = 20; // Safety limit

    // Run through entire initialization sequence
    stateSequence.push(this.fsm.currentState);

    while (this.fsm.currentState !== 'TURN_START' && iterations < maxIterations) {
      const success = await this.fsm.tryAutoTransitions();
      if (!success) break;
      stateSequence.push(this.fsm.currentState);
      iterations++;
    }

    await this.testAsync('Complete sequence reaches TURN_START', async () => {
      assert.strictEqual(this.fsm.currentState, 'TURN_START',
        'Should end at TURN_START state');
    });

    await this.testAsync('Sequence follows correct order', async () => {
      const expectedSequence = [
        'PROMPT_GAME_INITIALIZATION',
        'VALIDATE_SIDE_ASSIGNMENTS',
        'PERSIST_PLAYER_FACTION_ASSIGNMENTS',
        'INITIALIZE_FP_RESOURCES',
        'INITIALIZE_SP_RESOURCES',
        'SETUP_INITIAL_BOARD_STATE',
        'PLACE_INITIAL_PIECES',
        'INITIALIZATION_COMPLETE',
        'TURN_START'
      ];

      assert.deepStrictEqual(stateSequence, expectedSequence,
        'State sequence should match expected order');
    });

    this.test('Initialization sends expected commands', () => {
      // Check for commands that should be sent during auto-transitions
      // (PROMPT_GAME_INITIALIZATION entry action is not executed when we just set currentState)
      const hasValidation = this.commandLog.some(cmd => cmd.includes('Validating'));
      const hasPersist = this.commandLog.some(cmd => cmd.includes('Saving player assignments'));
      const hasFPResources = this.commandLog.some(cmd => cmd.includes('Free Peoples cards'));
      const hasSPResources = this.commandLog.some(cmd => cmd.includes('Shadow cards'));
      const hasBoardState = this.commandLog.some(cmd => cmd.includes('regions'));
      const hasPieces = this.commandLog.some(cmd => cmd.includes('Placing starting units'));
      const hasComplete = this.commandLog.some(cmd => cmd.includes('initialization complete'));

      assert.ok(hasValidation, 'Should send validation message');
      assert.ok(hasPersist, 'Should send persist message');
      assert.ok(hasFPResources, 'Should send FP resource message');
      assert.ok(hasSPResources, 'Should send SP resource message');
      assert.ok(hasBoardState, 'Should send board state message');
      assert.ok(hasPieces, 'Should send pieces message');
      assert.ok(hasComplete, 'Should send completion message');
      
      console.log(`  → Total commands sent: ${this.commandLog.length}`);
    });
  }

  /**
   * Print test summary
   */
  printSummary() {
    console.log('\n=== Test Summary ===');
    console.log(`Total Tests: ${this.testCount}`);
    console.log(`✅ Passed: ${this.passCount}`);
    console.log(`❌ Failed: ${this.failCount}`);
    
    if (this.failCount > 0) {
      console.log('\nFailed Tests:');
      this.testResults
        .filter(r => r.status === 'FAIL')
        .forEach(r => console.log(`  - ${r.name}: ${r.error}`));
    }
    
    console.log('\n' + (this.failCount === 0 ? '🎉 All tests passed!' : '⚠️ Some tests failed'));
    
    return this.failCount === 0;
  }

  /**
   * Run all tests
   */
  async runAll() {
    await this.setup();
    await this.testInitializationStatesExist();
    await this.testInitializationFlow();
    await this.testEntryActions();
    await this.testCompleteSequence();
    const success = this.printSummary();
    
    // Cleanup
    if (this.db) {
      this.db.close();
    }
    
    process.exit(success ? 0 : 1);
  }
}

// Run tests if executed directly
if (require.main === module) {
  const test = new FsmInitializationTest();
  test.runAll().catch(error => {
    console.error('Test suite error:', error);
    process.exit(1);
  });
}

module.exports = FsmInitializationTest;
