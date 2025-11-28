# WOTR FSM Server - Turn/Phase Orchestrator

Minimal FSM server for managing game turns and phases.

## Current Features

✅ **Phase 1: Core Orchestration** (MVP)
- Loads FSM definition from `wotr_fsm.json`
- Tracks current game state
- Evaluates guard conditions from database
- Handles AUTO transitions
- Shows prompts and waits for player events

## Architecture

```
┌──────────────────────────┐
│  FSM Server (Node.js)    │
│  - Turn/Phase management │
│  - Guard evaluation      │
│  - State transitions     │
└────────┬─────────────────┘
         │ Reads from
         ↓
┌──────────────────────────┐
│  Database (SQLite)       │
│  - Fellowship state      │
│  - Dice state            │
│  - Victory points        │
└──────────────────────────┘
```

## Installation

```bash
cd fsm/server
npm install
```

## Running

```bash
# Basic run (uses defaults)
node index.js

# Specify database and FSM paths
node index.js --dbPath ../../wotr_game.db --fsmPath ../wotr_fsm.json
```

## What It Does

1. **Loads FSM**: Reads the state machine definition
2. **Enters TURN_START**: Begins at initial state
3. **Processes AUTO transitions**: 
   - Evaluates guards against database
   - Transitions automatically when guards pass
4. **Shows prompts**: Displays FSM prompts from `wotr_fsm.yml`
5. **Waits for events**: When manual events needed, waits for player input

## Example Output

```
=== WOTR FSM Server Starting ===
Database: ../../wotr_game.db
FSM: ../wotr_fsm.json

[DB] Opening database: ../../wotr_game.db
[DB] Database opened successfully
[FSM] Loading FSM from: ../wotr_fsm.json
[FSM] Loaded 89 states
[FSM] Initialized with state: TURN_START
[Server] Starting FSM orchestration...

=== STATE: TURN_START ===
Label: Turn Start
Description: Entry point for each game turn.

[FSM] Executing action: INCREMENT_TURN_COUNTER
[FSM] Executing action: RESET_PHASE_TRACKER
[FSM] Transitioning: TURN_START --[AUTO]--> PHASE_1_RECOVER_AND_DRAW

=== STATE: PHASE_1_RECOVER_AND_DRAW ===
Label: Recover & Draw
Description: Recover action dice and draw cards.

📢 🌅 Turn 1 — Phase 1: Recover Dice & Draw Cards

[FSM] Executing action: PROMPT_PHASE_1_START
[FSM] Executing action: RECOVER_ALL_FP_ACTION_DICE
...
```

## Components

### index.js
Main server entry point. Handles:
- Initialization
- Main loop
- CLI interface

### fsm_engine.js
Core state machine. Handles:
- State transitions
- Guard evaluation
- Entry/exit actions
- Event processing

### database.js
Database read interface. Provides:
- Guard query methods
- Read-only database access
- No write operations

## Guard Examples

The FSM evaluates these guards from your `wotr_fsm.yml`:

```yaml
# Check corruption level
guard: "CORRUPTION >= 12"

# Check dice remaining
guard: "FP_DICE_REMAINING > 0"

# Check both conditions
guard: "FP_DICE_REMAINING == 0 AND SP_DICE_REMAINING == 0"

# Check fellowship position
guard: "FELLOWSHIP_AT_CRACK_OF_DOOM_AND_CORRUPTION_LT_12"

# Check victory points
guard: "SHADOW_VP >= 10"
```

## Next Steps

### Phase 2: Game Client Integration
- Add WebSocket connection to Java game client
- Send prompts to chat box
- Receive player events
- Execute entry actions via game client

### Phase 3: Full Turn Management
- Dice rolling via game client
- Card drawing
- Phase progression
- Combat triggers
- Hunt triggers

### Phase 4: AI Opponent (Future)
- Add AI decision module
- Make Shadow moves automatically
- Keep FP human-controlled

## Limitations (MVP)

❌ No connection to game client yet (standalone mode)
❌ Some guards return default values (need DB tables)
❌ Entry actions logged but not executed
❌ No player input mechanism yet

## Testing

Currently runs in standalone mode and processes AUTO transitions until it hits a state requiring player input.

Watch it transition through phases automatically based on guards!

## Development

```bash
# Install dependencies
npm install

# Run in development
node index.js

# Test with specific database
node index.js --dbPath /path/to/test.db
```

## License

Part of WOTR-GANDALF project
