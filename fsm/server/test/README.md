# FSM Test Suite

Comprehensive tests for the WOTR FSM Server turn orchestration.

## Running Tests

```bash
# From fsm/server directory
npm test

# Or run directly
node test/fsm_turn_test.js
```

## What's Tested

### 1. **Initial State** (4 tests)
- FSM starts at TURN_START
- Turn counter starts at 0
- Phase counter starts at 1
- Initial actor is Free Peoples

### 2. **Phase 1: Recover & Draw** (3 tests)
- Auto-transitions to PHASE_1_RECOVER_AND_DRAW
- Sends correct commands (Ctrl+9, Ctrl+0, F7, F8, F11, F12)
- Auto-transitions through discard checks to Phase 2

### 3. **Phase 2: Fellowship** (5 tests)
- Reaches PHASE_2_FELLOWSHIP_START correctly
- Waits for FP_FELLOWSHIP_DECISIONS_READY
- **❌ Rejects invalid events**
- **❌ Rejects wrong-phase events**
- ✅ Accepts valid fellowship completion event

### 4. **Phase 3: Hunt Allocation** (3 tests)
- Auto-transitions to PHASE_3_HUNT_ALLOCATION_START
- Waits for SP_HUNT_ALLOCATION_DONE
- Accepts valid hunt allocation completion

### 5. **Phase 4: Action Roll** (4 tests)
- Auto-transitions to PHASE_4_ACTION_ROLL_START
- Sends dice roll commands (FSM_ROLL_FP_DICE, FSM_ROLL_SP_DICE)
- Accepts FP_ACTION_ROLL_DONE
- Accepts SP_ACTION_ROLL_DONE

### 6. **State Validation** (3 tests)
- **❌ Cannot send events from different phase**
- Valid events list exists for waiting states
- Current state is always defined

### 7. **Context Management** (3 tests)
- Context object is maintained
- Turn counter is valid (≥ 0)
- Current actor is valid (FP or SP)

### 8. **Guard Evaluation** (4 tests)
- Guards return boolean values
- Simple "true" guard passes
- Simple "false" guard fails
- Undefined guards default to true

### 9. **Command Broadcasting** (3 tests)
- Commands were sent during turn
- Phase announcements were sent
- Card draw commands were sent

## Test Output Example

```
=== FSM Turn Test Suite ===

[Setup] FSM initialized

--- Test Group: Initial State ---

✅ PASS: FSM starts at TURN_START
✅ PASS: Initial turn is 0
✅ PASS: Initial phase is 1
✅ PASS: Initial actor is FP

--- Test Group: Phase 1 - Recover & Draw ---

  [Command Sent] <auto> 🌅 Turn 0 — Phase 1: Recover Dice & Draw Cards
  [Command Sent] Ctrl+9
  [Command Sent] Ctrl+0
  [Command Sent] F7
  [Command Sent] F8
  [Command Sent] F11
  [Command Sent] F12
✅ PASS: Auto-transition to PHASE_1_RECOVER_AND_DRAW
✅ PASS: Phase 1 commands sent
✅ PASS: Auto-transition through discard checks

--- Test Group: Phase 2 - Fellowship ---

✅ PASS: Phase 2 state is correct
✅ PASS: Phase 2 waits for player input
✅ PASS: Reject invalid event in Phase 2
✅ PASS: Reject wrong phase event
✅ PASS: Accept valid fellowship completion event

==================================================
TEST SUMMARY
==================================================
Total Tests: 32
✅ Passed: 32
❌ Failed: 0
Success Rate: 100.0%
==================================================

💾 Commands sent during test:
  Total: 18
  Unique: 12

[Cleanup] Test complete
```

## Invalid Move Tests

The test suite includes several **validation tests** that verify invalid moves are rejected:

### ❌ Invalid Events Tested:

1. **`INVALID_EVENT`** - Completely made-up event name
   - Expected: Rejected ✅
   
2. **`SP_ACTION_COMPLETE`** - Valid event but wrong phase
   - Currently in Phase 2, but event is for Phase 5
   - Expected: Rejected ✅

3. **`FP_FELLOWSHIP_DECISIONS_READY`** - Valid event but used after phase complete
   - Event valid in Phase 2, but used later in turn
   - Expected: Rejected ✅

### Why These Tests Matter:

- **Prevents out-of-order actions** - Can't skip phases
- **Enforces turn structure** - Each phase must complete before next
- **Guards game state integrity** - Invalid transitions are blocked
- **Validates FSM definition** - Ensures state machine is well-formed

## Test Architecture

### Test Class Structure

```javascript
class FsmTurnTest {
  constructor()      // Initialize test tracking
  setup()            // Create FSM instance
  test(name, fn)     // Synchronous test wrapper
  testAsync(name, fn) // Asynchronous test wrapper
  
  // Test groups
  testInitialState()
  testPhase1()
  testPhase2()
  testPhase3()
  testPhase4()
  testStateValidation()
  testContext()
  testGuards()
  testCommands()
  
  runAll()           // Execute all tests
  printSummary()     // Show results
  cleanup()          // Close database
}
```

### Command Logging

The test suite logs all commands sent by the FSM:

```javascript
this.fsm = new FsmEngine(fsmPath, this.db, (command) => {
  this.commandLog.push(command);
  console.log(`  [Command Sent] ${command}`);
});
```

This allows testing that:
- Correct commands are sent
- Commands are sent in order
- Phase announcements appear
- No duplicate commands

## Adding More Tests

### Example: Test a New Phase

```javascript
async testPhase5() {
  console.log('\n--- Test Group: Phase 5 - Action Resolution ---\n');

  this.test('Phase 5 state is correct', () => {
    assert.strictEqual(this.fsm.currentState, 'PHASE_5_ACTION_RESOLUTION_START');
  });

  await this.testAsync('Reject out-of-turn action', async () => {
    // Try to use action die when it's not your turn
    const success = await this.fsm.handleEvent('SP_USE_ACTION_DIE');
    assert.strictEqual(success, false, 'Expected rejection');
  });

  await this.testAsync('Accept valid action', async () => {
    const success = await this.fsm.handleEvent('FP_USE_ACTION_DIE');
    assert.strictEqual(success, true, 'Expected acceptance');
  });
}
```

### Example: Test Guard Conditions

```javascript
await this.testAsync('Guard blocks invalid move', async () => {
  // Create a transition with a failing guard
  const mockTransition = {
    event: 'TEST_EVENT',
    guard: 'FP_ACTION_DICE_REMAINING > 0',
    target: 'NEXT_STATE'
  };
  
  // Assuming no dice remaining
  const guardResult = await this.fsm.evaluateGuard(mockTransition.guard);
  assert.strictEqual(guardResult, false, 'Expected guard to fail');
});
```

## Exit Codes

- **0** - All tests passed ✅
- **1** - One or more tests failed ❌

Use in CI/CD:
```bash
npm test || exit 1
```

## Dependencies

- Node.js built-in `assert` module
- FSM engine and database from `../fsm_engine` and `../database`
- Access to `wotr_game.db` for database queries

## Future Enhancements

- [ ] Test complete multi-turn game
- [ ] Test combat resolution
- [ ] Test hunt mechanics
- [ ] Test victory conditions
- [ ] Add performance benchmarks
- [ ] Add stress tests (1000s of transitions)
- [ ] Mock database for isolated unit tests
- [ ] Add code coverage reporting

---

**Run the tests to verify FSM server integrity before deploying!**
