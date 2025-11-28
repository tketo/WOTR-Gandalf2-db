# FSM Server Protocol

**Version:** 1.0  
**Last Updated:** November 18, 2025

---

## Overview

The FSM Server uses a **simple TCP text protocol** to orchestrate game actions and enable AI decision-making. It connects to the Java game client on port 8080.

---

## Connection

```
Java Client         FSM Server
    |                   |
    |--- CONNECT TCP -->|
    |<-- CONNECTED -----|
    |<-- STATE INFO ----|
    |<-- VALID EVENTS --|
```

**Port:** 8080 (TCP)  
**Format:** Plain text, newline-delimited

---

## Message Types

### FSM → Client (Commands)

#### Action Commands
```
FSM_ROLL_FP_DICE
```
Tells client to roll Free Peoples action dice.

```
FSM_ROLL_SHADOW_DICE
```
Tells client to roll Shadow action dice.

```
FSM_EXECUTE_ACTION <dice_index>
```
Execute action using die at specified index.

**Example:**
```
FSM_EXECUTE_ACTION 2
```

#### State Information
```
<state> STATE_NAME
```
Announces current FSM state.

**Example:**
```
<state> FP_ACTION_RESOLUTION
```

#### Valid Events
```
<valid_events> EVENT1,EVENT2,EVENT3
```
Lists events the user can trigger in current state.

**Example:**
```
<valid_events> FP_FELLOWSHIP_DECISIONS_READY,SKIP_PHASE
```

The Java client can query these:
```java
String[] events = game.fsmConnection.getValidEvents();
```

---

### Client → FSM (Events)

#### Event Notification
```
EVENT:<event_name>
```

**Common Events:**

```
EVENT:DICE_ROLLED
```
Notify dice have been rolled.

```
EVENT:ACTION_COMPLETE
```
Notify action execution finished.

```
EVENT:PHASE_ADVANCED
```
Notify game advanced to next phase.

```
EVENT:CARD_PLAYED
```
Notify card was played.

```
EVENT:COMBAT_RESOLVED
```
Notify combat finished.

#### Event with Data
```
EVENT:<event_name> { "key": "value" }
```

**Example:**
```
EVENT:DICE_ROLLED { "results": ["Army", "Character", "Muster"] }
```

---

## Protocol Flow

### Typical Turn Flow

```
1. FSM → Client: <state> FP_ACTION_ROLL
2. FSM → Client: <valid_events> READY_TO_ROLL
3. FSM → Client: FSM_ROLL_FP_DICE
4. Client rolls dice
5. Client → FSM: EVENT:DICE_ROLLED
6. FSM → Client: <state> FP_ACTION_RESOLUTION
7. FSM → Client: <valid_events> EXECUTE_ACTION_1,EXECUTE_ACTION_2
8. User selects action or FSM decides
9. FSM → Client: FSM_EXECUTE_ACTION 1
10. Client executes action
11. Client → FSM: EVENT:ACTION_COMPLETE
12. Loop continues...
```

---

## Command Reference

### Self-Contained Commands (Recommended)

These work **without UI pre-selection**:

| Command | Parameters | Description |
|---------|------------|-------------|
| `FSM_ROLL_FP_DICE` | None | Auto-selects and rolls all FP dice from area 178 |
| `FSM_ROLL_SP_DICE` | None | Auto-selects and rolls all Shadow dice from area 179 |

**Why self-contained?** FSM can't interact with UI to select pieces. These commands automatically select all dice from the pool, then roll them.

### Menu Shortcut Commands

These are **existing menu shortcuts** that work without UI state:

| Command | Shortcut | Description |
|---------|----------|-------------|
| `F7` | F7 | Draw FP character card |
| `F8` | F8 | Draw FP strategy card |
| `F11` | F11 | Draw Shadow character card |
| `F12` | F12 | Draw Shadow strategy card |
| `Ctrl+H` | Ctrl+H | Draw Hunt tile |
| `Ctrl+F` | Ctrl+F | Reveal/hide Fellowship |
| `Ctrl+Esc` | Ctrl+Esc | End combat round |
| `Ctrl+9` | Ctrl+9 | Recover all FP action dice |
| `Ctrl+0` | Ctrl+0 | Recover all Shadow action dice |

### UI-Dependent Commands (Avoid)

These require **UI pre-selection** and don't work well with FSM:
- `Ctrl+R` - Roll selected dice (must select first)
- `Delete` - Remove selected pieces
- Move commands - require piece selection

### Chat Messages

| Command | Description |
|---------|-------------|
| `<auto> [message]` | Display message in chat as system prompt |

**Example:**
```javascript
this.broadcastToClients('<auto> 🌅 Turn 1 — Phase 1: Recover & Draw');
```

### Client Events (Complete List)

| Event | Data | Description |
|-------|------|-------------|
| `DICE_ROLLED` | results[] | Dice have been rolled |
| `ACTION_COMPLETE` | None | Action finished |
| `PHASE_COMPLETE` | None | User clicked status bar to complete phase |
| `PHASE_ADVANCED` | phase_name | Phase changed |
| `CARD_PLAYED` | card_name | Card played |
| `COMBAT_RESOLVED` | winner | Combat finished |
| `FELLOWSHIP_MOVED` | region_id | Fellowship moved |
| `ARMY_MOVED` | from,to | Army moved |
| `HUNT_RESOLVED` | result | Hunt result |
| `POLITICAL_ADVANCED` | nation | Nation activated |
| `PIECE_MOVED` | None | Any piece moved (generic) |
| `DIE_SELECTED` | None | User selected die |
| `GAME_OVER` | winner | Game ended |

**PHASE_COMPLETE** is sent when user clicks the status bar at bottom of UI to signal they're done with the current phase.

---

## FSM State Machine

### Loading the FSM

```javascript
// fsm/server/index.js
const fsm = JSON.parse(fs.readFileSync('fsm-schema.json'));
```

### State Structure

```json
{
  "initial_state": "GAME_START",
  "states": {
    "GAME_START": {
      "valid_events": ["START_GAME"],
      "transitions": {
        "START_GAME": "FP_ACTION_ROLL"
      }
    },
    "FP_ACTION_ROLL": {
      "valid_events": ["READY_TO_ROLL"],
      "on_enter": "send_command('FSM_ROLL_FP_DICE')",
      "transitions": {
        "DICE_ROLLED": "FP_ACTION_RESOLUTION"
      }
    }
  }
}
```

### How FSM Decides Next Action

```javascript
// Pseudocode
function enterState(stateName) {
    const state = fsm.states[stateName];
    
    // Send valid events to client
    sendToClient(`<valid_events> ${state.valid_events.join(',')}`);
    
    // Execute on_enter command if present
    if (state.on_enter) {
        sendToClient(state.on_enter);
    }
    
    // Wait for event from client
    // Then transition to next state based on transitions map
}
```

---

## Database Queries

The FSM server queries `wotr_game.db` to make decisions:

```javascript
// Example: Check if nation is at war
db.get(`
    SELECT area_id 
    FROM game_pieces 
    WHERE piece_id = 'gondor_political_marker'
`, (err, row) => {
    const atWar = (row.area_id === 120);
    if (atWar) {
        // Make different FSM decision
    }
});
```

**FSM reads:**
- Game state (turn, phase, active player)
- Piece positions (armies, fellowship)
- Card hands
- Dice pool

**FSM does NOT write** - Game client handles all database updates.

---

## Error Handling

### Client Errors

If client sends malformed event:
```
ERROR: Unknown event 'INVALID_EVENT'
```

### FSM Errors

If FSM enters invalid state:
```
ERROR: Invalid state transition from 'STATE_A' via 'EVENT_X'
```

### Connection Loss

If connection drops:
```
1. Client attempts reconnect (3 retries)
2. FSM maintains state for 30 seconds
3. After timeout, FSM resets to GAME_START
```

---

## Testing

### Manual Testing

```bash
# Terminal 1: Start FSM server
cd fsm/server && npm start

# Terminal 2: Telnet to test
telnet localhost 8080

# Send test event
EVENT:TEST_EVENT

# Watch FSM response
```

### Automated Testing

```bash
# Run FSM unit tests
cd fsm/server
npm test
```

---

## Extending the Protocol

### Adding New Command

**1. Update FSM Schema:**
```json
"on_enter": "send_command('FSM_NEW_COMMAND param')"
```

**2. Add Handler in Java Client:**
```java
// In Interpreter.java
if (command.startsWith("FSM_NEW_COMMAND")) {
    String param = command.substring(16).trim();
    executeNewCommand(param);
}
```

**3. Test:**
```bash
# Update tests in fsm/server/test/
npm test
```

### Adding New Event

**1. Update FSM Transitions:**
```json
"transitions": {
    "NEW_EVENT": "NEXT_STATE"
}
```

**2. Send from Client:**
```java
fsmConnection.sendEvent("NEW_EVENT");
```

---

## Interactive Menus

### Overview

The FSM sends **formatted text menus** that display game state and actionable choices for each phase. These appear in the chat window.

### Menu Format

```
<auto> ═══════════════════════════════════
<auto> 🌅 PHASE 1: Recover Dice & Draw Cards
<auto> ═══════════════════════════════════
<auto>
<auto> ✅ FREE PEOPLES: Hand size 4/6
<auto> ⚠️  SHADOW: Hand size 7/6 - DISCARD REQUIRED
<auto>
<auto> Actions automatically executed:
<auto>  • Recovered all FP action dice
<auto>  • Drew 1 FP character card
<auto>  ...
<auto>
<auto> When ready, click status bar to continue
<auto> ═══════════════════════════════════
```

Each line is sent as a separate `<auto>` message.

### Phase-Specific Menus

**Phase 1: Recover & Draw**
- Shows hand sizes for both factions
- Indicates if discard is required (⚠️)
- Lists automatically executed actions

**Phase 2: Fellowship**
- Shows Fellowship location
- Shows status (🌑 Hidden or 🌞 Revealed)
- Shows corruption level (0-12)
- Lists available decisions

**Phase 3: Hunt Allocation**
- Shows Eye dice rolled
- Shows dice currently in Hunt Box
- Prompts Shadow player for allocation

**Phase 4: Action Roll**
- Shows dice counts for both factions
- Automatically rolls dice
- Explains die results

### Game State Queries

FSM queries database to build menus:

```javascript
// Get hand size
SELECT COUNT(*) FROM game_pieces 
WHERE game_id = ? AND piece_type = 'card' AND area_id = 144; -- FP hand

// Get Fellowship location
SELECT area_id FROM game_pieces 
WHERE game_id = ? AND piece_type = 'fellowship';

// Get corruption
SELECT area_id - 131 as corruption FROM game_pieces
WHERE game_id = ? AND piece_type = 'corruption_counter';

// Count Eye dice
SELECT COUNT(*) FROM game_pieces
WHERE game_id = ? AND area_id = 179 AND state_data LIKE '%"result":"eye"%';
```

### Valid Events Display

Along with menus, FSM sends valid events:

```
<valid_events> FP_FELLOWSHIP_DECISIONS_READY,SKIP_PHASE
```

Client displays: "Valid actions: FP_FELLOWSHIP_DECISIONS_READY, SKIP_PHASE"

---

## Status Bar Integration

### Status Bar Display

A clickable status bar at the bottom of the UI shows current phase:

```
┌─────────────────────────────────────┐
│         Game Board                  │
├─────────────────────────────────────┤
│  Chat Window                        │
├─────────────────────────────────────┤
│  FSM: 🌅 Phase 1: Recover & Draw    │  ← Clickable
└─────────────────────────────────────┘
```

### Status Bar States

- **Gray** - `FSM: Not connected`
- **Blue** - `FSM: Connected - waiting...`
- **Black** - `FSM: 🌅 Phase 1...` (active, clickable)

### User Interaction

1. FSM sends: `<auto> 🌅 Turn 1 — Phase 1: Recover & Draw`
2. Status bar updates: `FSM: 🌅 Turn 1 — Phase 1: Recover & Draw`
3. User completes phase actions (draw cards, discard, etc.)
4. User clicks status bar
5. Client sends: `EVENT:PHASE_COMPLETE`
6. FSM transitions to next phase

### Hover Effect

- Mouse over → turns blue
- Shows tooltip: "Click to complete phase"

---

## See Also

- **[IMPLEMENTATION.md](../IMPLEMENTATION.md)** - How to run FSM server
- **[ARCHITECTURE.md](../ARCHITECTURE.md)** - FSM architectural decisions
- **[fsm/README.md](../fsm/README.md)** - FSM folder overview

---

**Last Updated:** November 18, 2025  
**Maintained By:** Development Team
