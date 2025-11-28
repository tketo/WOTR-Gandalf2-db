# WOTR-GANDALF Architecture

**Version:** 4.0  
**Last Updated:** November 18, 2025  
**Status:** Phase 7 Complete

---

## High-Level Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Game Client                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │   UI     │  │  Game    │  │  Rules   │  │   FSM    │  │
│  │ (Swing)  │◄─┤ (State)  │◄─┤  Engine  │◄─┤  Client  │  │
│  └──────────┘  └─────┬────┘  └──────────┘  └─────┬────┘  │
│                      │                             │        │
│                      ▼                             │        │
│              ┌──────────────┐                      │        │
│              │   Database   │                      │        │
│              │   (SQLite)   │                      │        │
│              └──────────────┘                      │        │
└────────────────────────────────────────────────────┼────────┘
                                                     │
                                                     ▼ TCP
                                              ┌─────────────┐
                                              │ FSM Server  │
                                              │  (Node.js)  │
                                              └─────────────┘
```

---

## Core Architectural Principles

### 1. Database as Board

**Philosophy:** The SQLite database represents the physical game board.

**Why:** 
- Eliminates complex in-memory state management
- Natural mental model (matches physical game)
- Enables replay, undo, network sync
- Single source of truth

**How:**
```sql
-- Query game state like looking at physical board
SELECT * FROM game_pieces WHERE area_id = 74;  -- "What's in Minas Tirith?"
SELECT area_id FROM game_pieces WHERE piece_id = 'gandalf';  -- "Where is Gandalf?"
```

### 2. 1:1 Piece Mapping

**Philosophy:** One physical game piece = one database row = one `bits[]` entry

**Why:**
- Direct correspondence between code and board
- Simple state restoration
- No aggregation complexity

**Structure:**
```
Physical Piece ←→ bits[pieceId] ←→ game_pieces[pieceId]
```

### 3. State Inference

**Philosophy:** Derive state from piece positions, don't store separately

**Why:**
- Eliminates sync issues
- Reduces redundancy
- Enforces single source of truth

**Example:**
```java
// ❌ Wrong: Store fellowship state separately
fellowshipState.setCorruption(5);

// ✅ Right: Corruption = position of corruption counter
int corruption = corruptionCounterArea - 131;  // Areas 131-143
```

---

## Module Architecture

### Layer 1: UI & Presentation

```
┌──────────────────────────────────────┐
│  Game.java (Main Window)             │
│  ├─ BoardLabel (Board Rendering)     │
│  ├─ Controls (Menus & Buttons)       │
│  └─ Chat (Messages & FSM Output)     │
└──────────────────────────────────────┘
```

**Responsibility:** Display game state, capture user input

### Layer 2: Game State & Logic

```
┌──────────────────────────────────────┐
│  Game.java (State Container)         │
│  ├─ bits[] (All GamePieces)          │
│  ├─ areas[] (All Areas)              │
│  └─ Movement Sync (to Database)      │
└──────────────────────────────────────┘
```

**Responsibility:** Hold in-memory game state, sync to database

### Layer 3: Rules & Validation

```
┌──────────────────────────────────────┐
│  RulesEngine                          │
│  ├─ MovementRules                     │
│  ├─ CombatRules                       │
│  ├─ VictoryRules                      │
│  └─ PhaseActionValidator              │
└──────────────────────────────────────┘
```

**Responsibility:** Enforce War of the Ring rules

### Layer 4: Actions & Resolution

```
┌──────────────────────────────────────┐
│  ActionExecutor                       │
│  ├─ Character Movement                │
│  ├─ Army Movement                     │
│  ├─ Muster Actions                    │
│  └─ Event Card Execution              │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│  Specialized Systems                  │
│  ├─ CombatResolver                    │
│  ├─ HuntMechanic                      │
│  ├─ FellowshipManager                 │
│  ├─ PoliticalTrack                    │
│  └─ OnTableCardManager                │
└──────────────────────────────────────┘
```

**Responsibility:** Execute game actions, resolve mechanics

### Layer 5: Card Effects

```
┌──────────────────────────────────────┐
│  CardEffectExecutor                   │
│  ├─ FellowshipCardEffects (14)        │
│  ├─ PoliticalCardEffects (5)          │
│  ├─ ShadowCardEffects (14)            │
│  ├─ HuntCardEffects (13)              │
│  ├─ ArmyCardEffects (9)               │
│  └─ SpecialCardEffects (8)            │
└──────────────────────────────────────┘
```

**Responsibility:** Implement 96 event card effects

### Layer 6: Database & Persistence

```
┌──────────────────────────────────────┐
│  Database Layer                       │
│  ├─ DatabaseManager (Connection)     │
│  ├─ GameStateDAO (CRUD Operations)   │
│  ├─ GameStateService (Query API)     │
│  ├─ GameDataService (Reference Data) │
│  └─ ScenarioLoader (Piece Init)      │
└──────────────────────────────────────┘
```

**Responsibility:** Persist and query game state

#### Database Schema (V8)

**Philosophy:** Direct mapping - database columns → constructor parameters (no JSON parsing)

**Core Tables:**

```sql
-- Scenario definitions
CREATE TABLE scenarios (
    id TEXT PRIMARY KEY,              -- 'base', 'lome_t', 'wome_lome_t'
    name TEXT NOT NULL,
    piece_count INTEGER NOT NULL
);

-- Piece initialization data
CREATE TABLE scenario_setup (
    piece_id INTEGER NOT NULL,        -- Index in bits[] array
    scenario_id TEXT NOT NULL,
    piece_class TEXT NOT NULL,        -- Java class name: 'FreeStrategyCard', 'UnitGondorRegular'
    initial_area_id INTEGER NOT NULL,
    initial_area_name TEXT,           -- Human-readable reference
    faction TEXT,                     -- 'free_peoples', 'shadow', 'neutral'
    
    -- Card-specific (NULL for non-cards)
    small_image TEXT,                 -- 'images/smallcards/fp001.png'
    big_image TEXT,                   -- 'images/cards/fp001.png'
    card_name TEXT,                   -- 'Athelas/Anduril'
    
    -- Unit-specific (NULL for cards)
    nation TEXT,                      -- 'gondor', 'rohan', 'sauron'
    unit_type TEXT,                   -- 'regular', 'elite', 'leader', 'nazgul'
    
    PRIMARY KEY (piece_id, scenario_id)
);
```

**Data Flow:**

```
ScenarioLoader.loadScenarioData(scenarioId)
  → SELECT * FROM scenario_setup WHERE scenario_id = ?
  → List<PieceSetup> with direct field mapping
  → Game.createPieceFromSetup(setup)
  → switch (setup.pieceClass) {
      case "FreeStrategyCard":
        return new FreeStrategyCard(area, setup.smallImage, setup.bigImage, setup.cardName);
      case "UnitGondorRegular":
        return new UnitGondorRegular(area);
    }
```

**Benefits:**
- ✅ No JSON state parsing needed
- ✅ Direct column → parameter mapping
- ✅ Card names/images preserved
- ✅ Easy to understand and extend
- ✅ Mirrors JSON export exactly

**Migrations:** Managed by Flyway in `src/main/resources/database/schema/`

### Layer 7: Turn Management

```
┌──────────────────────────────────────┐
│  Turn System                          │
│  ├─ TurnManager (State Machine)      │
│  ├─ TurnOrchestrator (UI Bridge)     │
│  └─ DicePool (Action Dice)           │
└──────────────────────────────────────┘
```

**Responsibility:** Manage turn phases, dice allocation

### Layer 8: FSM Integration

```
┌──────────────────────────────────────┐
│  FSM System                           │
│  ├─ FsmConnection (TCP Client)       │
│  ├─ Interpreter (Command Execution)  │
│  └─ FsmPhasePanel (UI Control)       │
└──────────────────────────────────────┘
```

**Responsibility:** AI orchestration, automation, phase-aware UI

---

## FSM Server

### Overview

The FSM (Finite State Machine) Server is a **separate Node.js process** that orchestrates game flow and enables AI decision-making. It runs independently from the Java game client.

```
┌─────────────────────────────────────┐
│         Java Game Client            │
│  ┌────────┐  ┌─────────┐           │
│  │   UI   │  │  Game   │           │
│  └───┬────┘  └────┬────┘           │
│      │            │                 │
│      └────────┬───┘                 │
│               │                     │
│        ┌──────▼──────┐              │
│        │ FSM Client  │              │
│        └──────┬──────┘              │
└───────────────┼────────────────────┘
                │ TCP
                ▼ Port 8080
         ┌─────────────┐
         │ FSM Server  │
         │  (Node.js)  │
         │      │      │
         │      ▼      │
         │   SQLite    │ (read-only)
         └─────────────┘
```

### Purpose

**What it does:**
- Orchestrates turn flow (roll dice → execute actions → advance phase)
- Makes AI decisions for Shadow player
- Automates repetitive game actions
- Enables network play (future)

**What it doesn't do:**
- Does NOT render UI (Java client does that)
- Does NOT write to database (Java client handles all writes)
- Does NOT validate rules (Java rules engine does that)

### Communication Protocol

**Protocol:** Simple TCP text protocol (port 8080)

**FSM → Client Commands:**
```
# Keyboard macros (mapped via Interpreter.handleMacroCommand())
F7                         # Draw FP character card
F8                         # Draw FP strategy card
F11                        # Draw Shadow character card
F12                        # Draw Shadow strategy card
Ctrl+9                     # Recover FP action dice
Ctrl+0                     # Recover Shadow action dice

# Self-contained commands
FSM_ROLL_FP_DICE           # Roll dice internally
FSM_EXECUTE_ACTION 2       # Execute specific action

# State updates
<state> PHASE1_RECOVER_AND_DRAW    # Current FSM state
<valid_events> EVENT1,EVENT2        # Valid user actions
<auto> Turn 1 — Phase 1...         # Display message
```

**Client → FSM Events:**
```
EVENT:PIECE_MOVED          # Piece moved on board
EVENT:DICE_ROLLED          # Dice rolled
EVENT:ACTION_COMPLETE      # Action finished
EVENT:PHASE_COMPLETE       # Phase done
```

**Keyboard Macro Mapping:**

The Interpreter now handles FSM keyboard shortcuts via `handleMacroCommand()`:
- Maps F7-F12, Ctrl+9, Ctrl+0 to button action commands
- Creates ActionEvent and delegates to CtrlButtonHandler
- Enables automatic card drawing in Phase 1

See **[docs/FSM_PROTOCOL.md](docs/FSM_PROTOCOL.md)** for complete protocol specification.

### State Machine

FSM loads state machine from `fsm/wotr_fsm.yml` (YAML) or `fsm/wotr_fsm.json` (compiled):

```yaml
states:
  - id: PHASE_1_RECOVER_AND_DRAW
    kind: normal
    entry_actions:
      - RECOVER_ALL_FP_ACTION_DICE    # → Sends "Ctrl+9"
      - RECOVER_ALL_SP_ACTION_DICE    # → Sends "Ctrl+0"
      - FP_DRAW_CHARACTER_CARD        # → Sends "F7"
      - FP_DRAW_STRATEGY_CARD         # → Sends "F8"
      - SP_DRAW_CHARACTER_CARD        # → Sends "F11"
      - SP_DRAW_STRATEGY_CARD         # → Sends "F12"
    transitions:
      - event: FP_DISCARDS_DONE
        target: PHASE_2_FELLOWSHIP_START
```

**How it works:**
1. FSM enters state
2. Executes `entry_actions` (maps to commands)
3. Sends commands to client with 100ms delay between each
4. Broadcasts `valid_events` to UI
5. Waits for event from client
6. Transitions to next state based on event

**Turn 0 Setup Phase:**

The FSM now includes a complete Turn 0 setup phase with 7 states:
- **SETUP_GAME_OPTIONS** - Choose expansions, Treebeard, player count
- **SETUP_SOLO_SIDE_SELECTION** - Solo player picks FP or Shadow
- **SETUP_CONNECTION_MODE** - Host or join multiplayer
- **SETUP_CLIENT_CONNECT** - Client connection details
- **SETUP_LOBBY_HOST** - Host lobby (manage options)
- **SETUP_LOBBY_CLIENT** - Client lobby (view-only)
- **SETUP_SIDE_ASSIGNMENT** - Assign players to factions

**Phase Workflow:**

1. **Phase 1** - Recover & Draw (automatic via keyboard macros)
2. **Phase 2** - Fellowship decisions (4 actions: Declare, Heal, Activate Nation, Change Guide)
3. **Phase 3** - Hunt Allocation (Shadow allocates Eye dice)
4. **Phase 4** - Action Roll (automatic dice roll)
5. **Phase 5** - Action Resolution (drag-and-drop die selection)
6. **Phase 6** - Victory Check / End Turn

### Database Access

FSM server **reads** from `wotr_game.db`:
```javascript
db.get("SELECT * FROM game_state WHERE game_id = ?", ...);
db.all("SELECT * FROM game_pieces WHERE area_id = ?", ...);
```

Uses queries to:
- Check game state before making decisions
- Evaluate board position for AI moves
- Determine valid actions based on pieces

**Does NOT write** - All database writes handled by Java client.

### Running the FSM

See **[IMPLEMENTATION.md](IMPLEMENTATION.md)** "Running the FSM Server" section for:
- Prerequisites (Node.js 16+)
- How to start server
- How client connects
- Debugging and configuration

### FSM Control Panel (UI)

**Purpose:** Replace simple status bar with comprehensive phase-aware control center

**Architecture:**
```
┌─────────────────────────────────┐
│ NORTH: Header                   │
│  - Turn/Phase                   │
│  - Active Player                │
│  - FSM State (debug)            │
├─────────────────────────────────┤
│ CENTER: Body (CardLayout)       │
│  - Phase-specific content       │
│  - Context widgets (checklists) │
│  - Game state info (hand sizes) │
├─────────────────────────────────┤
│ SOUTH: Actions + Footer         │
│  - Dynamic buttons              │
│  - "Done With Phase"            │
│  - Help link                    │
└─────────────────────────────────┘
```

**Components:**
- `FsmPhasePanel.java` - Main panel (330 lines)
- `PhaseConfig` interface - Phase configuration contract
- 7 phase configurations:
  - `NotConnectedConfig` - Default state
  - `Phase1Config` - Recover & Draw
  - `Phase2Config` - Fellowship Phase
  - `Phase3Config` - Hunt Allocation
  - `Phase4Config` - Action Roll
  - `Phase5Config` - Action Resolution  
  - `Phase6Config` - Victory Check

**Integration:**
- Panel updates when FSM sends `<state>` message
- Buttons enabled/disabled based on `<valid_events>`
- Clicking buttons sends events to FSM server
- Provides "do this now" guidance for each phase

**Benefits:**
- Clear phase instructions
- Context-aware UI (shows relevant game state)
- Action buttons instead of hidden events
- No guessing what FSM expects

---

## Data Flow Patterns

### Pattern 1: User Action → Database Sync

```
User Moves Piece
    ↓
Game.moveTo(area)
    ↓
MovementListener.onPieceMoved()
    ↓
DatabaseManager.syncPieceLocation()
    ↓
UPDATE game_pieces SET area_id = ?
```

### Pattern 2: Query Game State

```
Need Combat Strength
    ↓
GameStateService.getCombatStrength(region)
    ↓
GameStateDAO.query("SELECT * FROM game_pieces WHERE area_id = ?")
    ↓
Count military units
    ↓
Return total strength
```

### Pattern 3: Card Effect Execution

```
Play Event Card
    ↓
CardEffectExecutor.executeCard(cardName)
    ↓
Route to appropriate handler
    ↓
FellowshipCardEffects.executeAthelas()
    ↓
GameStateService.adjustCorruption(-2)
    ↓
Database updated, UI refreshes
```

### Pattern 4: FSM Orchestration

```
FSM Server: "ROLL_FP_DICE"
    ↓
FsmConnection receives command
    ↓
Interpreter.execute("FSM_ROLL_FP_DICE")
    ↓
Auto-select dice from pool
    ↓
Roll all dice
    ↓
Display results
    ↓
Send "EVENT:DICE_ROLLED" back to FSM
```

---

## Key Architectural Decisions

### Decision 1: SQLite Over In-Memory State

**Alternatives Considered:**
- Pure in-memory state with manual serialization
- Complex event sourcing system
- Separate state management framework

**Chose:** SQLite as single source of truth

**Rationale:**
- Simple, battle-tested technology
- Natural query model
- Built-in persistence
- Zero-config deployment
- Enables save/load without custom serialization

### Decision 2: Unified game_pieces Table

**Alternatives Considered:**
- Separate tables per piece type (army_units, fellowship_state, etc.)
- EAV (Entity-Attribute-Value) model
- Document store approach

**Chose:** Single `game_pieces` table with piece_type column

**Rationale:**
- Simpler queries across all pieces
- Consistent API for all operations
- Easier state restoration
- Natural 1:1 mapping to bits[]

### Decision 3: Specialized Card Effect Handlers

**Alternatives Considered:**
- Single monolithic CardEffects class
- Per-card classes (96 separate files)
- DSL for card definitions

**Chose:** 6 category-based handler classes

**Rationale:**
- Balances organization and maintainability
- Groups related effects together
- Easier to find specific cards
- Shared logic per category (e.g., all political cards use PoliticalTrack)

### Decision 4: FSM Server Separation

**Alternatives Considered:**
- Embedded AI in Java client
- Python AI server
- Cloud-based AI service

**Chose:** Separate Node.js FSM server

**Rationale:**
- Clean separation of concerns
- FSM server can orchestrate without game logic
- Easy to add AI logic without modifying client
- TCP allows network play
- Node.js fast for I/O and state machines

### Decision 5: Movement-Based Sync

**Alternatives Considered:**
- Periodic full state sync
- Transaction-based commits
- Event sourcing with replay

**Chose:** Automatic sync on piece movement

**Rationale:**
- Real-time consistency
- Simple mental model
- No batching complexity
- Works naturally with drag-and-drop UI

---

## Database Schema Design

### game_pieces Table

```sql
CREATE TABLE game_pieces (
    game_id TEXT NOT NULL,
    piece_id INTEGER NOT NULL,
    area_id INTEGER NOT NULL,
    piece_type TEXT NOT NULL,
    state_data TEXT,  -- JSON for piece-specific state
    PRIMARY KEY (game_id, piece_id)
);
CREATE INDEX idx_game_area ON game_pieces(game_id, area_id);
```

**Design Rationale:**
- Composite primary key (game_id, piece_id) supports multiple games
- area_id indexed for fast "what's in this region" queries
- piece_type for filtering (e.g., "only military units")
- state_data JSON for flexible piece-specific state (TwoChit active/passive, etc.)

### Area Numbering System

```
0-104     Map Regions (Middle-earth geography)
105-114   Reserved for expansion
115       Fellowship Box
116       Reserved
117-120   Political Track
121-130   Card Decks & Discards
131-143   Fellowship Track
144-145   Player Hands
146-151   On-Table Cards
152       Reserved
153-154   Current Card Display
...
174-180   Dice & Reinforcements
182-184   Hunt Tile System
185       Spare Pieces
194-211   WOME Expansion Areas (see below)
```

**Design Rationale:**
- Map regions first (primary gameplay)
- Special areas grouped by function
- Gaps for future expansion
- Consistent numbering makes queries predictable

### Expansion Variant Areas

**Base Game & LOME:**
- **areas[] Size**: 194 (indices 0-193)
- **Note**: LOME uses same board as base (no new areas required)

**WOME Expansion:**
- **areas[] Size**: 212 (indices 0-211)
- **Added Areas**: 18 extra slots (194-211)
- **Active Areas**: 16 used, 2 reserved
  - **194-201**: Faction card management (8 areas)
    - 194: FP Faction Deck
    - 195: FP Faction Discard
    - 196: FP Faction Bottom
    - 197: FP Faction Top
    - 198: Shadow Faction Deck
    - 199: Shadow Faction Discard
    - 200: Shadow Faction Bottom
    - 201: Shadow Faction Top
  - **202-203**: Reserved (unused)
  - **204-211**: Battle mechanics (8 areas)
- **Conditional**: Only initialized if `isWOME.booleanValue()` is true

---

## Expansion Variants and Piece Sizing

### Overview

The game supports multiple expansion variants:
1. **Base Game** - Standard WOTR 2nd Edition
2. **LOME** (Lords of Middle-Earth) - Adds characters, cards, and mechanics
3. **WOME** (Warriors of Middle-Earth) - Adds faction units and battle system
4. **Combined** - Both expansions together

Arrays must be sized correctly based on variant:
- **`bits[]` array** - Holds all game pieces (units, cards, dice)
- **`areas[]` array** - Holds board regions and off-board areas

### Variant Sizing Table

| Variant | varianttype | bits[] | areas[] | Database Scenarios |
|---------|-------------|--------|---------|-------------------|
| Base 2nd Ed | `"base2"` | 428 | 194 | `["base"]` |
| Base 2nd Ed + Treebeard | `"base2[T]"` | 429 | 194 | `["base"]` |
| LOME | `"expansion2[L]"` | 453 | 194 | `["base", "lome"]` |
| LOME + Treebeard | `"expansion2[LT]"` | 453 | 194 | `["base", "lome"]` |
| WOME | `"base[WT]"` | 539 | 212 | `["base", "wome"]` |
| WOME + LOME + Treebeard | `"expansion2[WLT]"` | 564 | 212 | `["base", "lome", "wome"]` |

### Menu Action → Saved Format Mapping

The `lastGame` preference saves game variants in format: `[versionno boardtype varianttype]`

| Menu Action | Saved Format | Description |
|-------------|--------------|-------------|
| `base2` | `[WOTR-Gandalf2 wotr base2]` | 2nd edition without Treebeard |
| `base2[T]` | `[WOTR-Gandalf2 wotr base2[T]]` | 2nd edition with Treebeard |
| `newLOME` | `[WOTR-Gandalf2 wotr expansion2[L]]` | LOME without Treebeard |
| `newLOME[T]` | `[WOTR-Gandalf2 wotr expansion2[LT]]` | LOME with Treebeard |
| `newWOME` | `[WOTR-Gandalf2 wotr base[WT]]` | Warriors + base (Treebeard always included) |
| `newWOME[L]` | `[WOTR-Gandalf2 wotr expansion2[WLT]]` | Warriors + LOME (Treebeard always included) |

**Key Points:**
- LOME adds 24 pieces (no Treebeard) to base (428 + 24 = 452, +1 array slot for optional Treebeard = 453)
- WOME adds 111 pieces and 18 areas
- Treebeard is conditionally used based on `[T]` suffix in variant string
- Database loads scenarios sequentially: base → lome → wome

### Piece Breakdown

**LOME Expansion (22-24 pieces):**
- 3 Elven Ring dice (Vilya, Nenya, Narya)
- 1 Elrond unit
- 4 FP character cards
- 2 FP strategy cards
- 2-4 Hunt tiles (Smeagol variants)
- 2 Shadow dice (Balrog, Gothmog)
- 2 Shadow character units (Gothmog, Mouth of Sauron)
- 4 Shadow character cards
- 2 Shadow strategy cards
- 2 Shadow action chits
- Optional: 1 Treebeard unit (conditionally active based on `[T]` suffix)

**WOME Expansion (111 pieces):**
- 2 Faction dice (1 FP, 1 Shadow)
- 48 Faction units (8 each of 6 types):
  - Ents (FP)
  - Eagles (FP)
  - Dead Men of Dunharrow (FP)
  - Dunlendings (Shadow)
  - Corsairs (Shadow)
  - Giant Spiders (Shadow)
- 12 Battle cards (6 FP, 6 Shadow)
- 20 FP Faction cards
- 20 Shadow Faction cards
- 6 FP character cards
- 3 Shadow cards (1 character, 2 strategy)

### Array Sizing Code

**bits[] Sizing (Game.java ~line 1560):**
```java
int TreeBeard = 0;
if (varianttype.endsWith("T]")) {
    TreeBeard = 1;  // Extra slot for Treebeard
}
int Factioncount = 0;
if (varianttype.contains("W")) {
    Factioncount = WOME_PIECE_COUNT; // 111
}
if (varianttype.startsWith("expansion2")) {
    // LOME: 428 base + 24 LOME + 1 Treebeard slot = 453
    this.bits = new GamePiece[(TreeBeard + 453 + Factioncount)];
} else {
    // Base: 428 + optional Treebeard + optional WOME
    this.bits = new GamePiece[(TreeBeard + 428 + Factioncount)];
}
```

**areas[] Sizing (Game.java ~line 1513):**
```java
if (isWOME.booleanValue()) {
    areas = new Area[212];  // Base 194 + 18 WOME areas
} else {
    areas = new Area[FPFACTION];  // 194 (FPFACTION constant)
}
```

### Database-Driven Initialization

Pieces are loaded from database `scenario_setup` table:

1. **Variant Detection**: Parse `varianttype` string
2. **Scenario Selection**: `getScenariosForVariant()` returns list
3. **Sequential Loading**: Load each scenario in order
4. **Piece Creation**: Create pieces from database rows
5. **Validation**: Verify `bits.length` matches expected count
6. **Fallback**: Use hardcoded `bitInit()` if database unavailable

**Scenario Loading Logic:**
```java
private List<String> getScenariosForVariant() {
    List<String> scenarios = new ArrayList<>();
    scenarios.add("base");  // Always load base (428 pieces)
    
    if (varianttype != null) {
        if (varianttype.contains("[L]")) {
            scenarios.add("lome");  // +22-24 pieces
        }
        if (varianttype.contains("W")) {
            scenarios.add("wome");  // +111 pieces
        }
    }
    return scenarios;
}
```

**Migration Files:**
- `V008__deployed_board_state.sql` - Base scenario (428 pieces)
- `V009__lome_scenario.sql` - LOME expansion (22 pieces, Treebeard handled by Java)
- `V010__wome_scenario.sql` - WOME expansion (105 pieces filtered from dumps)

---

## Performance Considerations

### Query Optimization

**Indexed Queries:**
- game_pieces(game_id, area_id) - Primary access pattern
- game_pieces(piece_id) - Direct piece lookup

**Typical Performance:**
- Single piece query: < 1ms
- Area query (all pieces): < 5ms
- Full board state: < 50ms

### Memory Management

**bits[] Array:**
- Fixed size (~650 pieces max)
- O(1) piece lookup by ID
- ~10MB memory footprint

**Database Connection:**
- Single persistent connection
- Connection pooling not needed (single-player primarily)

---

## Extensibility Points

### Adding New Card Effects

1. Add card to appropriate handler class
2. Implement effect method
3. Add routing in CardEffectExecutor
4. Write unit test

### Adding New Game Pieces

1. Define piece type in database
2. Create GamePiece subclass if needed
3. Add to bitInit() or scenario_setup
4. Implement rendering if unique

### Adding New Board Areas

1. Define area ID (follow numbering convention)
2. Add to areas[] initialization
3. Update BOARD_REFERENCE.md
4. Add rendering coordinates if on visual board

### Adding FSM Events

1. Define event in FSM JSON
2. Add handler in Interpreter
3. Document in FIX-Valid-Actions-Communication.md

---

## Testing Architecture

### Unit Tests

```
src/test/java/wotr/
├── cards/effects/
│   ├── FellowshipCardEffectsTest
│   ├── PoliticalCardEffectsTest
│   └── ShadowCardEffectsTest
└── fellowship/
    └── FellowshipManagerTest
```

**Strategy:** Mock GameStateService, test logic in isolation

### Integration Tests

**Manual testing currently** - Run game, verify:
- Piece movement syncs to database
- Card effects work end-to-end
- FSM commands execute correctly

---

## Security & Multiplayer Considerations

### Current State
- Single-player or local 2-player
- No authentication
- Local SQLite database

### Future Considerations
- Network protocol (currently raw TCP)
- State validation (prevent cheating)
- Save game encryption
- Player authentication for online play

---

## File Organization

```
src/main/java/wotr/
├── Game.java                    # Main game state
├── actions/                     # Action execution
│   └── ActionExecutor.java
├── cards/                       # Card system
│   ├── CardEffectExecutor.java
│   └── effects/                 # 6 handler classes
├── combat/                      # Combat system
│   └── CombatResolver.java
├── dao/                         # Database access
│   └── GameStateDAO.java
├── database/                    # DB management
│   └── DatabaseManager.java
├── dice/                        # Action dice
│   └── DicePool.java
├── fellowship/                  # Fellowship system
│   └── FellowshipManager.java
├── rules/                       # Rules engine
│   └── RulesEngine.java
├── services/                    # Service layer
│   ├── GameStateService.java
│   └── GameDataService.java
├── turn/                        # Turn management
│   └── TurnManager.java
└── ui/                          # UI components
    └── TurnControlPanel.java

database/schema/                 # SQL migrations
data/                           # JSON game data
docs/                           # Documentation
fsm/                            # FSM server (Node.js)
```

---

## Dependencies

### Java Runtime
- Java 1.8+ (target: Java 1.8)
- Swing (UI framework, built-in)

### Libraries
- SQLite JDBC 3.x (database)
- GSON 2.x (JSON parsing)
- JUnit 5 (testing)
- Mockito 5.3.1 (mocking)

### Build Tools
- Maven 3.6+
- Flyway (database migrations, embedded)

### FSM Server
- Node.js 16+
- sqlite3 (npm package)

---

## Deployment

### Current
- JAR file (shaded, includes all dependencies)
- Local SQLite file (wotr_game.db)
- Optional FSM server (separate Node.js process)

### Future
- Installer packages (Windows, macOS, Linux)
- Standalone executable (jlink/jpackage)
- Docker container for FSM server

---

## See Also

- **[IMPLEMENTATION.md](IMPLEMENTATION.md)** - How to build and run
- **[ROADMAP.md](ROADMAP.md)** - Future development plans
- **[CHANGELOG.md](CHANGELOG.md)** - Version history
- **[README.md](README.md)** - Project overview

---

**Document Version:** 1.0  
**Last Updated:** November 18, 2025  
**Maintained By:** Development Team
