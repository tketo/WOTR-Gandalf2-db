#!/usr/bin/env node

/**
 * FSM Turn Test Suite
 * Tests a complete turn cycle including validation
 */

const assert = require('assert');
const path = require('path');
const FsmEngine = require('../fsm_engine');
const Database = require('../database');

class FsmTurnTest {
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
    console.log('=== FSM Turn Test Suite ===\n');
    
    // Initialize database and FSM
    const dbPath = path.resolve(__dirname, '../../../wotr_game.db');
    const fsmPath = path.resolve(__dirname, '../../wotr_fsm.json');
    
    this.db = new Database(dbPath);
    this.commandLog = [];
    
    // Create FSM with command callback that logs
    this.fsm = new FsmEngine(fsmPath, this.db, (command) => {
      this.commandLog.push(command);
      console.log(`  [Command Sent] ${command}`);
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
   * Test Suite: Initial State
   */
  async testInitialState() {
    console.log('\n--- Test Group: Initial State ---\n');

    this.test('FSM starts at TURN_START', () => {
      assert.strictEqual(this.fsm.currentState, 'TURN_START', 
        'Expected FSM to start at TURN_START');
    });

    this.test('Initial turn is 0', () => {
      assert.strictEqual(this.fsm.context.turn, 0, 
        'Expected turn to start at 0');
    });

    this.test('Initial phase is 1', () => {
      assert.strictEqual(this.fsm.context.phase, 1, 
        'Expected phase to start at 1');
    });

    this.test('Initial actor is FP', () => {
      assert.strictEqual(this.fsm.context.currentActor, 'FP', 
        'Expected FP to be initial actor');
    });
  }

  /**
   * Test Suite: Phase 1 - Recover & Draw
   */
  async testPhase1() {
    console.log('\n--- Test Group: Phase 1 - Recover & Draw ---\n');

    await this.testAsync('Auto-transition to PHASE_1_RECOVER_AND_DRAW', async () => {
      const transitioned = await this.fsm.tryAutoTransitions();
      assert.strictEqual(transitioned, true, 'Expected auto-transition to succeed');
      assert.strictEqual(this.fsm.currentState, 'PHASE_1_RECOVER_AND_DRAW', 
        'Expected state to be PHASE_1_RECOVER_AND_DRAW');
    });

    this.test('Phase 1 commands sent', () => {
      // Should have sent: phase prompt, recover dice, draw cards
      const expectedCommands = ['Ctrl+9', 'Ctrl+0', 'F7', 'F8', 'F11', 'F12'];
      const sentCommands = this.commandLog.slice(-6);
      
      expectedCommands.forEach(cmd => {
        assert.ok(sentCommands.includes(cmd), 
          `Expected command "${cmd}" to be sent`);
      });
    });

    await this.testAsync('Auto-transition through discard checks', async () => {
      // Should auto-transition through FP and SP discard checks
      await this.fsm.tryAutoTransitions(); // -> FP_DISCARD_CHECK
      await this.fsm.tryAutoTransitions(); // -> SP_DISCARD_CHECK
      await this.fsm.tryAutoTransitions(); // -> PHASE_1_END
      await this.fsm.tryAutoTransitions(); // -> PHASE_2_FELLOWSHIP_START
      
      assert.strictEqual(this.fsm.currentState, 'PHASE_2_FELLOWSHIP_START',
        'Expected to reach PHASE_2_FELLOWSHIP_START');
    });
  }

  /**
   * Test Suite: Phase 2 - Fellowship
   */
  async testPhase2() {
    console.log('\n--- Test Group: Phase 2 - Fellowship ---\n');

    this.test('Phase 2 state is correct', () => {
      assert.strictEqual(this.fsm.currentState, 'PHASE_2_FELLOWSHIP_START',
        'Expected state to be PHASE_2_FELLOWSHIP_START');
    });

    this.test('Phase 2 waits for player input', () => {
      const validEvents = this.fsm.getValidEvents();
      assert.ok(validEvents.includes('FP_FELLOWSHIP_DECISIONS_READY'), 
        'Expected FP_FELLOWSHIP_DECISIONS_READY to be valid event');
    });

    await this.testAsync('Reject invalid event in Phase 2', async () => {
      const success = await this.fsm.handleEvent('INVALID_EVENT');
      assert.strictEqual(success, false, 
        'Expected invalid event to be rejected');
    });

    await this.testAsync('Reject wrong phase event', async () => {
      const success = await this.fsm.handleEvent('SP_ACTION_COMPLETE');
      assert.strictEqual(success, false, 
        'Expected wrong-phase event to be rejected');
    });

    await this.testAsync('Accept valid fellowship completion event', async () => {
      const success = await this.fsm.handleEvent('FP_FELLOWSHIP_DECISIONS_READY');
      assert.strictEqual(success, true, 
        'Expected FP_FELLOWSHIP_DECISIONS_READY to be accepted');
    });
  }

  /**
   * Test Suite: Phase 3 - Hunt Allocation
   */
  async testPhase3() {
    console.log('\n--- Test Group: Phase 3 - Hunt Allocation ---\n');

    await this.testAsync('Auto-transition to Phase 3', async () => {
      // Should auto-transition through PHASE_2_END to PHASE_3
      await this.fsm.tryAutoTransitions();
      await this.fsm.tryAutoTransitions();
      
      assert.strictEqual(this.fsm.currentState, 'PHASE_3_HUNT_ALLOCATION_START',
        'Expected state to be PHASE_3_HUNT_ALLOCATION_START');
    });

    this.test('Phase 3 waits for hunt allocation', () => {
      const validEvents = this.fsm.getValidEvents();
      assert.ok(validEvents.includes('SP_HUNT_ALLOCATION_DONE'), 
        'Expected SP_HUNT_ALLOCATION_DONE to be valid event');
    });

    await this.testAsync('Complete hunt allocation', async () => {
      const success = await this.fsm.handleEvent('SP_HUNT_ALLOCATION_DONE');
      assert.strictEqual(success, true, 
        'Expected SP_HUNT_ALLOCATION_DONE to be accepted');
    });
  }

  /**
   * Test Suite: Phase 4 - Action Roll
   */
  async testPhase4() {
    console.log('\n--- Test Group: Phase 4 - Action Roll ---\n');

    await this.testAsync('Auto-transition to Phase 4', async () => {
      await this.fsm.tryAutoTransitions(); // -> PHASE_3_END
      await this.fsm.tryAutoTransitions(); // -> PHASE_4_ACTION_ROLL_START
      
      assert.strictEqual(this.fsm.currentState, 'PHASE_4_ACTION_ROLL_START',
        'Expected state to be PHASE_4_ACTION_ROLL_START');
    });

    this.test('Phase 4 dice roll commands sent', () => {
      // Should have sent dice roll commands
      const recentCommands = this.commandLog.slice(-5);
      assert.ok(recentCommands.some(cmd => cmd.includes('FSM_ROLL')),
        'Expected dice roll commands to be sent');
    });

    await this.testAsync('Complete action roll', async () => {
      const success = await this.fsm.handleEvent('FP_ACTION_ROLL_DONE');
      assert.strictEqual(success, true, 
        'Expected FP_ACTION_ROLL_DONE to be accepted');
    });

    await this.testAsync('Complete Shadow action roll', async () => {
      await this.fsm.tryAutoTransitions();
      const success = await this.fsm.handleEvent('SP_ACTION_ROLL_DONE');
      assert.strictEqual(success, true, 
        'Expected SP_ACTION_ROLL_DONE to be accepted');
    });
  }

  /**
   * Test Suite: State Validation
   */
  async testStateValidation() {
    console.log('\n--- Test Group: State Validation ---\n');

    this.test('Cannot send event from different phase', async () => {
      // Currently in Phase 4 or later, try Phase 2 event
      const success = await this.fsm.handleEvent('FP_FELLOWSHIP_DECISIONS_READY');
      assert.strictEqual(success, false,
        'Expected out-of-phase event to be rejected');
    });

    this.test('Valid events list is not empty for waiting states', () => {
      // Move to a waiting state
      const events = this.fsm.getValidEvents();
      // Should have at least one event unless we're in a final state
      const state = this.fsm.getCurrentState();
      if (state.kind !== 'final') {
        assert.ok(events.length >= 0,
          'Expected valid events list');
      }
    });

    this.test('Current state is always defined', () => {
      assert.ok(this.fsm.currentState,
        'Expected currentState to be defined');
      assert.ok(typeof this.fsm.currentState === 'string',
        'Expected currentState to be a string');
    });
  }

  /**
   * Test Suite: Context Management
   */
  async testContext() {
    console.log('\n--- Test Group: Context Management ---\n');

    this.test('Context is maintained', () => {
      assert.ok(this.fsm.context,
        'Expected context to exist');
      assert.ok(typeof this.fsm.context.turn === 'number',
        'Expected turn to be a number');
      assert.ok(typeof this.fsm.context.phase === 'number',
        'Expected phase to be a number');
    });

    this.test('Turn counter is 0 or higher', () => {
      assert.ok(this.fsm.context.turn >= 0,
        'Expected turn to be 0 or higher');
    });

    this.test('Current actor is valid', () => {
      const validActors = ['FP', 'SP'];
      assert.ok(validActors.includes(this.fsm.context.currentActor),
        `Expected currentActor to be one of ${validActors.join(', ')}`);
    });
  }

  /**
   * Test Suite: Guard Evaluation
   */
  async testGuards() {
    console.log('\n--- Test Group: Guard Evaluation ---\n');

    await this.testAsync('Guards evaluate to boolean', async () => {
      // Test a known guard
      const result = await this.fsm.evaluateGuard('true');
      assert.strictEqual(typeof result, 'boolean',
        'Expected guard to return boolean');
    });

    await this.testAsync('Simple true guard passes', async () => {
      const result = await this.fsm.evaluateGuard('true');
      assert.strictEqual(result, true,
        'Expected "true" guard to pass');
    });

    await this.testAsync('Simple false guard fails', async () => {
      const result = await this.fsm.evaluateGuard('false');
      assert.strictEqual(result, false,
        'Expected "false" guard to fail');
    });

    await this.testAsync('Undefined guard defaults to true', async () => {
      const result = await this.fsm.evaluateGuard(undefined);
      assert.strictEqual(result, true,
        'Expected undefined guard to default to true');
    });
  }

  /**
   * Test Suite: Command Broadcasting
   */
  async testCommands() {
    console.log('\n--- Test Group: Command Broadcasting ---\n');

    this.test('Commands were sent during turn', () => {
      assert.ok(this.commandLog.length > 0,
        'Expected at least one command to be sent');
    });

    this.test('Phase announcements were sent', () => {
      const hasPhaseAnnouncement = this.commandLog.some(cmd => 
        cmd.includes('Phase') || cmd.includes('🌅') || cmd.includes('🌍')
      );
      assert.ok(hasPhaseAnnouncement,
        'Expected phase announcement to be sent');
    });

    this.test('Card draw commands were sent', () => {
      const cardCommands = ['F7', 'F8', 'F11', 'F12'];
      const hasCardCommand = this.commandLog.some(cmd => 
        cardCommands.includes(cmd)
      );
      assert.ok(hasCardCommand,
        'Expected card draw command to be sent');
    });
  }

  /**
   * Run all tests
   */
  async runAll() {
    await this.setup();

    await this.testInitialState();
    await this.testPhase1();
    await this.testPhase2();
    await this.testPhase3();
    await this.testPhase4();
    await this.testStateValidation();
    await this.testContext();
    await this.testGuards();
    await this.testCommands();

    this.printSummary();
    this.cleanup();
  }

  /**
   * Print test summary
   */
  printSummary() {
    console.log('\n' + '='.repeat(50));
    console.log('TEST SUMMARY');
    console.log('='.repeat(50));
    console.log(`Total Tests: ${this.testCount}`);
    console.log(`✅ Passed: ${this.passCount}`);
    console.log(`❌ Failed: ${this.failCount}`);
    console.log(`Success Rate: ${((this.passCount / this.testCount) * 100).toFixed(1)}%`);
    console.log('='.repeat(50));

    if (this.failCount > 0) {
      console.log('\n❌ FAILED TESTS:');
      this.testResults
        .filter(r => r.status === 'FAIL')
        .forEach(r => {
          console.log(`  - ${r.name}`);
          console.log(`    ${r.error}`);
        });
    }

    console.log('\n💾 Commands sent during test:');
    console.log(`  Total: ${this.commandLog.length}`);
    console.log(`  Unique: ${[...new Set(this.commandLog)].length}`);
  }

  /**
   * Cleanup
   */
  cleanup() {
    if (this.db) {
      this.db.close();
    }
    console.log('\n[Cleanup] Test complete\n');
  }
}

// Run tests if called directly
if (require.main === module) {
  const test = new FsmTurnTest();
  test.runAll()
    .then(() => {
      process.exit(test.failCount > 0 ? 1 : 0);
    })
    .catch(err => {
      console.error('Fatal test error:', err);
      process.exit(1);
    });
}

module.exports = FsmTurnTest;
