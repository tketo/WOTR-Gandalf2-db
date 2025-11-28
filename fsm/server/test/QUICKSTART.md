# FSM Test Quick Start

## Run Tests

```bash
cd fsm/server
npm test
```

## Expected Output

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

--- Test Group: Phase 3 - Hunt Allocation ---
✅ PASS: Auto-transition to Phase 3
✅ PASS: Phase 3 waits for hunt allocation
✅ PASS: Complete hunt allocation

--- Test Group: Phase 4 - Action Roll ---
✅ PASS: Auto-transition to Phase 4
✅ PASS: Phase 4 dice roll commands sent
✅ PASS: Complete action roll
✅ PASS: Complete Shadow action roll

--- Test Group: State Validation ---
✅ PASS: Cannot send event from different phase
✅ PASS: Valid events list is not empty for waiting states
✅ PASS: Current state is always defined

--- Test Group: Context Management ---
✅ PASS: Context is maintained
✅ PASS: Turn counter is 0 or higher
✅ PASS: Current actor is valid

--- Test Group: Guard Evaluation ---
✅ PASS: Guards evaluate to boolean
✅ PASS: Simple true guard passes
✅ PASS: Simple false guard fails
✅ PASS: Undefined guard defaults to true

--- Test Group: Command Broadcasting ---
✅ PASS: Commands were sent during turn
✅ PASS: Phase announcements were sent
✅ PASS: Card draw commands were sent

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

## What Gets Tested

### ✅ Valid Moves
- Auto-transitions through phases
- Valid event acceptance (FP_FELLOWSHIP_DECISIONS_READY, SP_HUNT_ALLOCATION_DONE, etc.)
- Command broadcasting
- Context management

### ❌ Invalid Moves
- **INVALID_EVENT** - Made-up event name → Rejected
- **SP_ACTION_COMPLETE** in Phase 2 → Rejected (wrong phase)
- **FP_FELLOWSHIP_DECISIONS_READY** after phase ends → Rejected (out of order)

## Test Coverage

- **9 test groups**
- **32 total tests**
- **Phases 1-4 covered**
- **State validation**
- **Guard evaluation**
- **Command verification**

## Common Issues

### Test fails: "Database not found"
Make sure `wotr_game.db` exists at project root:
```bash
ls ../../wotr_game.db
```

### Test fails: "FSM definition not found"
Check that `wotr_fsm.json` was generated:
```bash
cd ../generator
node build_fsm.js
```

### All tests pass but you want more
Add new tests to `fsm_turn_test.js`:
```javascript
await this.testAsync('My custom test', async () => {
  const success = await this.fsm.handleEvent('MY_EVENT');
  assert.strictEqual(success, true);
});
```

## Quick Commands

```bash
# Run tests
npm test

# Run tests with verbose output
node test/fsm_turn_test.js

# Check exit code (0 = pass, 1 = fail)
npm test; echo $?
```

---

**All tests passing? FSM is ready for integration!** ✅
