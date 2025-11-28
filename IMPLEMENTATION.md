# WOTR-GANDALF Implementation Guide

**Version:** 4.1  
**Last Updated:** November 20, 2025  
**Status:** Phase 7 Complete

---

## Quick Start

**Building:**
1. Prerequisites: Java 8+, Maven 3.6+
2. Clone repository
3. Run `mvn clean package`
4. JAR outputs to `target/WOTR-GANDALF.jar`

**Running:**
```bash
java -jar target/WOTR-GANDALF.jar
```

**With FSM Server:**
```bash
# Terminal 1: Start FSM server
cd fsm/server
npm install
npm start

# Terminal 2: Run game
java -jar target/WOTR-GANDALF.jar
```

---

## How to Build

### Prerequisites

- **Java JDK 8** or higher
- **Maven 3.6+** for build management
- **Git** for version control
- **Node.js 16+** (optional, for FSM server)

### Build Steps

```bash
# 1. Clone repository
git clone <repository-url>
cd WOTR-GANDALF-Windsurf

# 2. Build with Maven
mvn clean package

# Output: target/WOTR-GANDALF.jar (~8MB with dependencies)
```

### Build Profiles

```bash
# Default build (includes all dependencies)
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run tests only
mvn test
```

### Troubleshooting Build

**"Java version mismatch"**
```bash
# Check Java version
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/java8
```

**"Dependencies not found"**
```bash
# Force dependency refresh
mvn clean install -U
```

---

## How to Run

### Run from JAR

```bash
java -jar target/WOTR-GANDALF.jar
```

### Run from IDE

**Eclipse:**
1. Import as Maven project
2. Right-click `Game.java`
3. Run As → Java Application

**IntelliJ IDEA:**
1. Open project
2. Maven sync automatically
3. Run `Game.java` main method

### Run with Debugging

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar target/WOTR-GANDALF.jar
```

Then attach debugger to port 5005.

---

## Running the FSM Server

### Prerequisites

- **Node.js 16+** (check: `node --version`)
- **npm** or **pnpm** (comes with Node.js)
- **SQLite3** (auto-installed via npm)

### Quick Start

```bash
# Navigate to FSM server
cd fsm/server

# Install dependencies (first time only)
npm install

# Start server
npm start

# Server listens on TCP port 8080
```

### How the Java Client Connects

**Via Menu:**
1. Start game client
2. Go to **Multiplayer** menu
3. Select **Connect to FSM Server...**
4. Enter host: `localhost`
5. Enter port: `8080`
6. Click Connect

**Via Code:**
```java
// In Game.java
FsmConnection fsm = new FsmConnection("localhost", 8080);
fsm.connect();
```

**Connection Flow:**
```
1. Java client starts → connects to TCP port 8080
2. FSM server accepts connection
3. Client shows: "Connected to FSM server at localhost:8080"
4. Status bar updates: "FSM: Connected - waiting..."
5. FSM sends initial state + valid events
6. Client executes commands from FSM
7. Client sends events back to FSM
8. Loop continues based on FSM state machine
```

**Disconnecting:**
1. **Multiplayer** menu
2. **Disconnect from FSM Server**

Game continues normally without FSM orchestration.

### Database Access

The FSM server **reads** from the same `wotr_game.db` database:
- Queries game state to make decisions
- Does NOT write to database (game client handles that)
- Acts as orchestrator and AI decision maker

**Schema:**
```javascript
// fsm/server/index.js
const db = new sqlite3.Database('../../wotr_game.db');
db.get("SELECT * FROM game_state WHERE game_id = ?", ...);
```

### TCP Protocol

**FSM → Client Commands:**
```
FSM_ROLL_FP_DICE           # Roll Free Peoples dice
FSM_EXECUTE_ACTION 5       # Execute specific action
<valid_events> EVENT1,EVENT2  # List valid user actions
```

**Client → FSM Events:**
```
EVENT:DICE_ROLLED          # Notify dice rolled
EVENT:ACTION_COMPLETE      # Notify action finished
EVENT:PHASE_ADVANCED       # Notify phase changed
```

See **[docs/FSM_PROTOCOL.md](docs/FSM_PROTOCOL.md)** for complete protocol specification.

### How to Modify the FSM

#### 1. Edit FSM Schema
```bash
# Edit state machine definition
nano fsm/server/fsm-schema.json
```

```json
{
  "states": {
    "YOUR_STATE": {
      "valid_events": ["EVENT1", "EVENT2"],
      "transitions": {
        "EVENT1": "NEXT_STATE"
      }
    }
  }
}
```

#### 2. Add Client Handler
```java
// In Interpreter.java
if (command.startsWith("FSM_NEW_EVENT")) {
    // Handle the event
    executeNewEvent();
}
```

#### 3. Test
```bash
# Terminal 1: Start FSM server
cd fsm/server && npm start

# Terminal 2: Run game client
java -jar target/WOTR-GANDALF.jar

# Trigger event in game, watch FSM console
```

### Debugging

**Enable verbose logging:**
```javascript
// In fsm/server/index.js
const DEBUG = true;
```

**Monitor FSM state:**
```bash
npm start
# Watch console for:
# - State transitions
# - Commands sent to client
# - Events received from client
```

**Check database queries:**
```bash
# FSM server logs all SQL queries in debug mode
# Look for: [FSM] Querying game_state...
```

### FSM Status Bar & Control Panel

When connected to FSM, you have two UI options:

**Option 1: Status Bar (Simple)**
```
FSM: 🌅 Turn 1 — Phase 1: Recover Dice & Draw Cards
```

- Bottom of window
- Click to send `EVENT:PHASE_COMPLETE`
- Shows current phase
- Non-invasive

**Option 2: FSM Control Panel (Comprehensive)**

Replaces status bar with full phase-aware control center on right side:

**Panel Structure:**
- **Header** - Turn, Phase, Active Player, FSM State
- **Body** - Phase-specific content (checklists, game state, instructions)
- **Actions** - Dynamic buttons based on `<valid_events>`
- **Footer** - "Done With Phase" button, Help link

**Phase Examples:**

**Phase 1 (Recover & Draw):**
```
☑ Recovered all FP action dice
☑ Recovered all Shadow action dice
☑ Drew 1 FP character card
☑ Drew 1 FP strategy card
☑ Drew 1 Shadow character card
☑ Drew 1 Shadow strategy card

Hand Sizes:
✅ Free Peoples: 6 / 6
⚠️  Shadow: 7 / 6 (discard 1)
```

**Phase 2 (Fellowship):**
```
Fellowship Status:
- Status: Hidden
- Location: Rivendell
- Corruption: 0/12
- Guide: Gandalf the Grey
- Companions: 8

[📍 Declare Fellowship]
[💚 Heal Ring-bearer]
[⚔️ Activate Nation]
[👤 Change Guide]
[Done]
```

**Phase 5 (Action Resolution):**
```
Available Action Dice:
🎲 Character die
🎲 Army die
⚔️ Muster die

Drag a die to area 152 to select it...
```

**Benefits:**
- Clear "do this now" instructions
- Context-aware (shows game state)
- Action buttons (no guessing valid events)
- Phase-specific help

### Card Drawing Fix (November 2025)

**Issue:** Card drawing didn't work when FSM sent keyboard macros

**Root Cause:** FSM sent `F7`, `F8`, `F11`, `F12` commands but Interpreter had no handler

**Solution:** Added `Interpreter.handleMacroCommand()` method

**Implementation:**
```java
// Maps keyboard shortcuts to button actions
private void handleMacroCommand(String cmd) {
    switch (cmd) {
        case "F7":   // Draw FP character card
            actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fdrawc");
            break;
        case "F8":   // Draw FP strategy card
            actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fdraws");
            break;
        case "F11":  // Draw Shadow character card
            actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "sdrawc");
            break;
        case "F12":  // Draw Shadow strategy card
            actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "sdraws");
            break;
    }
    game.controls.cbh.actionPerformed(actionEvent);
}
```

**Result:**
- Phase 1 card drawing now fully automatic
- Cards drawn to area 180 (FP Hand) and 181 (Shadow Hand)
- Database synced automatically
- UI updates correctly

---

## How to Generate Documentation

### JavaDoc Generation

```bash
# Generate JavaDoc HTML
mvn javadoc:javadoc

# Output: target/site/apidocs/
```

**View:**
```bash
open target/site/apidocs/index.html
```

### Database Schema Documentation

```bash
# Database schema in database/schema/*.sql
# Auto-applied via Flyway on startup
```

### Update Documentation

**When adding features:**
1. Update `IMPLEMENTATION.md` (this file) - how to use
2. Update `ARCHITECTURE.md` - design decisions
3. Update `CHANGELOG.md` - record the change

---

## Database Migrations & Schema (V8)

### Overview

Database uses **V8 schema** with direct piece mapping - database columns → constructor parameters (no JSON parsing). Managed by **Flyway** migrations in `src/main/resources/database/schema/`.

### Current Schema (V8)

**Migration File:** `V8__clean_slate_reset.sql`

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
    piece_class TEXT NOT NULL,        -- Java class name: 'FreeStrategyCard'
    initial_area_id INTEGER NOT NULL,
    initial_area_name TEXT,           -- Human-readable reference
    faction TEXT,                     -- 'free_peoples', 'shadow'
    
    -- Card-specific (NULL for non-cards)
    small_image TEXT,
    big_image TEXT,
    card_name TEXT,
    
    -- BattleCard-specific (NULL for non-BattleCards)
    small_back_image TEXT,            -- Small card back image (BattleCards)
    big_back_image TEXT,              -- Large card back image (BattleCards)
    card_type TEXT,                   -- Card type string (BattleCards)
    
    -- Chit/TwoChit-specific (uses small_image, big_image, card_name)
    -- Chit: small_image = image path, card_name = type string
    -- TwoChit: small_image = front, big_image = back, card_name = type
    
    -- Unit-specific (NULL for cards)
    nation TEXT,                      -- 'gondor', 'rohan', 'sauron'
    unit_type TEXT,                   -- 'regular', 'elite', 'leader'
    
    PRIMARY KEY (piece_id, scenario_id)
);
```

### Regenerating Database from Game State

**When to regenerate:** After modifying `bitInit()` or changing piece initialization logic.

#### Step 1: Export Current State

Game auto-exports during initialization to `data/bits_array_<variant>.json`

```bash
# Run game to generate exports
java -jar target/WOTR-GANDALF.jar

# Exports created:
# data/bits_array_expansion2[WLT].json (WOME+LOME+Treebeard)
# data/bits_array_expansion2[LT].json   (LOME+Treebeard)
# etc.
```

#### Step 2: Generate SQL from JSON

```bash
# Python script converts JSON → SQL
python scripts/import_json_to_sql.py

# Output: src/main/resources/database/imports/scenario_imports.sql
# Contains INSERT statements for all scenarios
```

#### Step 3: Reset Database

```powershell
# Delete old database
rm wotr_game.db

# Apply V8 schema
sqlite3 wotr_game.db ".read src/main/resources/database/schema/V8__clean_slate_reset.sql"

# Import data
sqlite3 wotr_game.db ".read src/main/resources/database/imports/scenario_imports.sql"
```

#### Step 4: Verify

```bash
# Build and run
mvn clean package -DskipTests
java -jar target/WOTR-GANDALF.jar

# Check console for:
# [DB] Database initialization complete: 564 pieces loaded
```

### Python Script (import_json_to_sql.py)

**Purpose:** Converts JSON exports to SQL INSERT statements

**Key Functions:**
- `get_faction(class_name)` - Determines faction from Java class
- `get_nation_from_class()` - Extracts nation for units
- `get_unit_type_from_class()` - Extracts unit type
- `process_json_file()` - Converts JSON → SQL

**Piece Type Handling:**
- **Cards:** Extracts `small_image`, `big_image`, `card_name`
- **BattleCards:** Also extracts `small_back_image`, `big_back_image`, `card_type`
- **Chits:** Extracts `small_image` (path), `card_name` (type string)
- **TwoChits:** Extracts `small_image` (front), `big_image` (back), `card_name` (type)
- **Units:** Extracts `nation`, `unit_type` from class name

**Variant Mapping:**
```python
VARIANT_TO_SCENARIO = {
    "base2": "base",
    "base2[T]": "base_t",
    "expansion2[L]": "lome",
    "expansion2[LT]": "lome_t",
    "base[WT]": "wome_t",
    "expansion2[WLT]": "wome_lome_t"
}
```

### Troubleshooting Migrations

**"Migration failed to apply"**
```bash
# Check Flyway history
sqlite3 wotr_game.db "SELECT * FROM flyway_schema_history"

# If needed, clean and reapply
rm wotr_game.db
mvn clean compile
```

**"Wrong piece count"**
- Verify JSON has all pieces
- Check classification logic
- Review migration SQL

**"Pieces in wrong areas"**
- Validate areaId in JSON
- Check area mapping in Python script

---

## Database Quick Reference

### Core Tables (V8 Schema)

| Table | Purpose | Key Fields |
|-------|---------|------------|
| `scenarios` | Scenario definitions | id, name, piece_count |
| `scenario_setup` | Piece initialization | piece_id, piece_class, initial_area_id, small_image, card_name, nation |
| `on_table_cards` | Persistent card effects | card_name, faction, area_id |

### V8 Schema Benefits

✅ **Direct mapping** - Database columns → Constructor parameters  
✅ **No JSON parsing** - Simple field access  
✅ **Card data preserved** - Images and names stored directly  
✅ **Type-specific fields** - Cards use `small_image`/`big_image`/`card_name`, Units use `nation`/`unit_type`

### Example Queries

```sql
-- Get all pieces in a scenario
SELECT * FROM scenario_setup WHERE scenario_id = 'wome_lome_t';

-- Find all Free People cards
SELECT piece_id, card_name FROM scenario_setup 
WHERE piece_class LIKE '%Free%Card' AND scenario_id = 'base';

-- Count units by nation
SELECT nation, COUNT(*) FROM scenario_setup 
WHERE nation IS NOT NULL GROUP BY nation;
```

---

## Query Methods

### Get Pieces in Area
```java
List<Integer> pieces = game.getPiecesInArea(areaId);
// Returns all piece IDs in that area
```

### Count Military Units
```java
int military = game.countMilitaryUnitsInArea(areaId);
// Returns total military strength (regular + elite + leader)
```

### Get Piece Location
```java
int location = game.getPieceLocation(pieceId);
// Returns area ID where piece is located
```

### Filter by Type
```java
List<Integer> elites = game.getPiecesInAreaByType(areaId, "elite");
// Returns only pieces of specific type
```

### Fellowship State (Inferred)
```sql
-- Fellowship location
SELECT area_id FROM game_pieces 
WHERE game_id = ? AND piece_type = 'fellowship';

-- Corruption level (counter position on track 131-143)
SELECT area_id - 131 as corruption FROM game_pieces 
WHERE game_id = ? AND piece_type = 'corruption_counter';

-- Companions (characters in Fellowship box, area 115)
SELECT piece_id FROM game_pieces 
WHERE game_id = ? AND piece_type = 'character' AND area_id = 115;
```

---

## Board Areas

### Map Regions: 0-104
- 104 regions representing Middle-earth
- Each has terrain type, faction control, nation ownership

### Special Areas: 105-250

**Fellowship System:**
- 115 - Fellowship Box (companions)
- 131-143 - Fellowship Track (0-12 positions)

**Card System:**
- 121-124 - Card Decks (FP/Shadow Strategy/Character)
- 125-128 - Discard Piles
- 144-145 - Player Hands
- 146-151 - On-Table Cards (persistent effects)

**Hunt System:**
- 182 - Hunt Pool (available tiles)
- 183 - Hunt Used (return when pool empty)
- 184 - Hunt Removed (permanently removed)

**Political Track:**
- 117-120 - Political positions (3=passive, 0=at war)

**Dice & Actions:**
- 178-179 - Action Dice Pools (FP/Shadow)
- 180 - Hunt Box (dice allocated to hunt)

**Casualties:**
- 175 - FP Casualties
- 177 - Shadow Casualties
- 185 - Spare Pieces (transformed characters)

---

## Card Effects System

### Architecture

**6 Specialized Handler Classes:**
1. `FellowshipCardEffects` - Corruption, guide, companion effects
2. `PoliticalCardEffects` - Nation activation, political track
3. `ShadowCardEffects` - Nazgûl, shadow armies, on-table cards
4. `HuntCardEffects` - Hunt tiles, protection, hunt manipulation
5. `ArmyCardEffects` - Army movement, recruitment, combat
6. `SpecialCardEffects` - Unique cards (Ents, Dead Men, Eagles)

### Implementing a Card Effect

```java
// In appropriate handler class
public ActionResult executeCardName(String faction) {
    // 1. Validate prerequisites
    if (!checkConditions()) {
        return ActionResult.failure("Prerequisites not met");
    }
    
    // 2. Execute effect using database queries
    gameStateService.someOperation();
    
    // 3. Return result
    return ActionResult.success("Effect applied");
}
```

### Card Effect Routing

`CardEffectExecutor` routes by card name:
```java
if (cardName.contains("Athelas")) {
    return fellowshipEffects.executeAthelas(faction);
}
```

### Database Operations in Card Effects

```java
// Move pieces
gameStateService.moveCharacter(characterId, destination);

// Political track
gameStateService.movePoliticalMarker(nationId, newPosition);
gameStateService.activateNationTwoChit(nationId);

// Fellowship
gameStateService.adjustCorruption(amount);
gameStateService.setFellowshipRevealed(true);

// Units
gameStateService.addUnits(region, nationId, type, count);
gameStateService.removeUnits(region, nationId, type, count);
```

### On-Table Cards

```java
// Place card on table
onTableManager.placeCardOnTable(gameId, cardName, faction);

// Check if card is on table
boolean isActive = onTableManager.isCardOnTable(gameId, cardName);

// Remove card
onTableManager.removeCardFromTable(gameId, cardName);
```

---

## FSM Integration

### Purpose
FSM (Finite State Machine) server orchestrates game flow and AI decisions.

### Communication

**Java Client → FSM Server:**
```java
// Send events
fsmConnection.sendEvent("PIECE_MOVED");
fsmConnection.sendEvent("CARD_PLAYED");
```

**FSM Server → Java Client:**
```javascript
// Broadcast commands
broadcastToClients("<valid_events> EVENT1,EVENT2");
broadcastToClients("EXECUTE_ACTION");
```

### Valid Events

FSM sends list of valid events/actions when entering a new state:
```
<valid_events> FP_FELLOWSHIP_DECISIONS_READY,SKIP_PHASE
```

Client displays these and can query programmatically:
```java
String[] validEvents = game.fsmConnection.getValidEvents();
```

### Game Initialization Sequence

The FSM manages game initialization through 8 granular states that execute in sequence:

**State Flow:**
```
PROMPT_GAME_INITIALIZATION
    ↓ (AUTO)
VALIDATE_SIDE_ASSIGNMENTS
    ↓ (AUTO)
PERSIST_PLAYER_FACTION_ASSIGNMENTS
    ↓ (AUTO)
INITIALIZE_FP_RESOURCES
    ↓ (AUTO)
INITIALIZE_SP_RESOURCES
    ↓ (AUTO)
SETUP_INITIAL_BOARD_STATE
    ↓ (AUTO)
PLACE_INITIAL_PIECES
    ↓ (AUTO)
INITIALIZATION_COMPLETE
    ↓ (AUTO)
TURN_START (Begin Turn 1)
```

**Implementation:**

1. **FSM Orchestration** (`fsm/server/fsm_engine.js`):
   - Sends progress messages to game client
   - Transitions automatically between states
   - Each state logs console output and sends UI feedback

2. **Java Service** (`wotr/services/GameInitializationService.java`):
   - Validates each initialization step
   - Persists player-faction assignments to database
   - Logs detailed initialization progress

3. **Integration Points:**
   - Resources created by `Game.gameInit()` and `Game.bitInit()`
   - Pieces placed by `Game.hybridPieceInit()`
   - FSM validates and logs each step
   - Database synced automatically

**Testing:**

Run FSM initialization tests:
```bash
cd fsm/server
node test/fsm_initialization_test.js
# Expected: 22/22 tests passing
```

Test coverage includes:
- State existence verification (8 tests)
- Individual transition validation (8 tests)
- Entry action execution (3 tests)
- Complete sequence validation (3 tests)

**Entry Points:**

Initialization sequence triggered from:
1. **Solo Game:** `SOLO_CHOOSE_FP` or `SOLO_CHOOSE_SHADOW` events
2. **Multiplayer:** `HOST_START_GAME` event from side assignment

**Progress Messages:**

Players see step-by-step feedback:
```
═══════════════════════════════════
🎲 Initializing Game...
═══════════════════════════════════
✓ Validating player assignments...
✓ Saving player assignments to database...
✓ Setting up Free Peoples cards and dice...
✓ Setting up Shadow cards and dice...
✓ Initializing regions and political tracks...
✓ Placing starting units and Fellowship...
═══════════════════════════════════
✅ Game initialization complete!
═══════════════════════════════════
Ready to begin Turn 1
```

---

## Movement and Sync

### Automatic Database Sync

When a piece moves:
```java
piece.moveTo(targetArea);
// Automatically triggers:
// → MovementListener.onPieceMoved()
// → Database UPDATE via syncPieceLocation()
```

### No Manual Sync Required

❌ **Old way (deprecated):**
```java
gameStateDAO.addUnits(gameId, regionId, nationId, "regular", 3);
```

✅ **New way (automatic):**
```java
piece.moveTo(targetArea);
// Database synced automatically
```

---

## Combat System

### Combat Resolution

```java
CombatResolver resolver = new CombatResolver(gameStateService);
CombatState combat = resolver.initiateCombat(regionId);
CombatRoundResult result = resolver.executeCombatRound(combat);
```

### Leader Re-rolls

Automatically applied based on leaders present:
```java
// Detects Gandalf (Leadership 3) in region
// Applies 3 re-rolls to failed dice
int leadership = resolver.getLeadershipValue(regionId, faction);
```

### Combat Cards

Integrated via timing hooks:
```java
// Before rolling combat dice
if (cardOnTable("Charge of the Rohirrim")) {
    // All Rohan units count as elite
}
```

---

## Hunt System

### Hunt Resolution

```java
HuntMechanic hunt = new HuntMechanic(gameStateService);
HuntResult result = hunt.executeHunt(dicePool);
// Roll: 1d6 + Hunt dice + terrain modifier
// Outcomes: Safe, Revealed, Damage, Heavy Damage
```

### Hunt Tiles

- Draw from area 182 (HUNTPOOL)
- Display in area 153 (CURRENTSA)
- After resolution → area 183 (HUNTUSED)
- When pool empty → move from HUNTUSED back to HUNTPOOL

---

## Fellowship Management

### FellowshipManager

```java
FellowshipManager manager = new FellowshipManager(gameStateService);

// Get companions
List<String> companions = manager.getFellowshipCompanions();

// Change guide
manager.setFellowshipGuide("gandalf");

// Separate companion
manager.separateCompanion("aragorn", "minas_tirith");

// Eliminate character
manager.eliminateCharacter("boromir", "free_peoples");
```

### Guide Rules

- Ring Bearer (Frodo) cannot be guide
- Guide determines special abilities (e.g., Gollum can remove Hunt tiles)

---

## Political Track

### Nation Activation

```java
PoliticalTrack track = new PoliticalTrack(gameStateService);

// Check status
boolean atWar = track.isNationAtWar("gondor");

// Activate nation
track.activateNation("elves");

// Advance position
track.advanceNation("rohan", 2); // Move 2 steps toward war
```

### Positions

- **Position 3** - Passive (not at war)
- **Position 2** - Not at war
- **Position 1** - Not at war  
- **Position 0** - At war (can use action dice)

---

## Testing

### Unit Tests

**Card Effects:** 44 tests, 95.5% passing
```bash
mvn test -Dtest=*CardEffectsTest
```

**Fellowship:** 15 tests, 100% passing
```bash
mvn test -Dtest=FellowshipManagerTest
```

### Test Framework

- JUnit 5
- Mockito 5.3.1 for mocking
- Java 25 compatible (mockito-inline + ByteBuddy experimental)

### Mocking Example

```java
@Mock
private GameStateService gameStateService;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    cardEffects = new FellowshipCardEffects(gameStateService);
}

@Test
void testCardEffect() {
    when(gameStateService.getCorruption()).thenReturn(5);
    ActionResult result = cardEffects.executeAthelas("free_peoples");
    assertTrue(result.isSuccess());
    verify(gameStateService).adjustCorruption(-2);
}
```

---

## Best Practices

### 1. Use Database Queries, Not In-Memory State

❌ **Wrong:**
```java
int corruption = fellowshipState.getCorruption();
```

✅ **Right:**
```java
int corruption = gameStateService.getCorruption();
// Queries database, always accurate
```

### 2. Let Pieces Sync Automatically

❌ **Wrong:**
```java
piece.moveTo(area);
gameStateDAO.updatePieceLocation(pieceId, areaId);
```

✅ **Right:**
```java
piece.moveTo(area);
// Syncs automatically via MovementListener
```

### 3. Infer State from Piece Positions

❌ **Wrong:**
```java
fellowshipState.setRevealed(true);
```

✅ **Right:**
```java
// Fellowship counter position determines revealed state
// Counter in certain areas = revealed
```

### 4. Use Specialized Effect Handlers

❌ **Wrong:**
```java
// All card effects in one massive class
```

✅ **Right:**
```java
// Separate handlers by category:
// FellowshipCardEffects, PoliticalCardEffects, etc.
```

### 5. Write Tests with Mocks

```java
// Mock dependencies to test logic in isolation
@Mock private GameStateService mockService;
```

---

## Performance

### Query Optimization

- All queries indexed on `game_id` and `area_id`
- Typical query time: < 1ms
- COUNT queries optimized with indexes

### Batch Operations

When moving multiple pieces:
```java
// Good - single transaction
for (GamePiece piece : pieces) {
    piece.moveTo(targetArea);
}
// All sync at once
```

---

## Common Patterns

### Check if Region Has Units

```java
int military = game.countMilitaryUnitsInArea(areaId);
if (military > 0) {
    // Region is occupied
}
```

### Find Character Location

```java
List<Integer> pieces = game.getPiecesInAreaByType(areaId, "character");
for (Integer pieceId : pieces) {
    GamePiece piece = game.bits[pieceId];
    if (piece.toString().contains("Gandalf")) {
        // Found Gandalf
    }
}
```

### Check Card Prerequisites

```java
public ActionResult executeCard(String faction) {
    // Check companion present
    if (!isCompanionInFellowship("gandalf")) {
        return ActionResult.failure("Gandalf not in Fellowship");
    }
    
    // Check location
    if (!isFellowshipInRegion("lorien")) {
        return ActionResult.failure("Fellowship not in Lórien");
    }
    
    // Execute effect
    return ActionResult.success("Effect applied");
}
```

---

## Internal Code Norms

### Naming Conventions

**Classes:**
- Managers: `FellowshipManager`, `OnTableCardManager`
- Services: `GameStateService`, `GameDataService`
- DAOs: `GameStateDAO`, `GameDataDAO`
- Executors: `CardEffectExecutor`, `ActionExecutor`

**Methods:**
- Queries: `getPiecesInArea()`, `countMilitaryUnits()`
- Actions: `movePiece()`, `executeCard()`, `resolveCombat()`
- Validators: `isValidMove()`, `canPlayCard()`

**Database:**
- Tables: `snake_case` (game_pieces, on_table_cards)
- Columns: `snake_case` (piece_id, area_id)
- Java fields: `camelCase` (pieceId, areaId)

### Code Organization

**Package Structure:**
```
wotr/
├── actions/          # User action handlers
├── cards/            # Card system
│   └── effects/      # Card effect implementations
├── combat/           # Combat mechanics
├── dao/              # Data access objects
├── database/         # DB management
├── fellowship/       # Fellowship system
├── rules/            # Rules validation
├── services/         # Business logic layer
└── turn/             # Turn management
```

**File Size Guidelines:**
- Keep classes under 500 lines
- Extract specialized logic into new classes
- Use inheritance for shared behavior

### Testing Standards

**Unit Test Pattern:**
```java
@Mock
private GameStateService mockService;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
}

@Test
void testFeature() {
    // Arrange
    when(mockService.getState()).thenReturn(expectedState);
    
    // Act
    Result result = systemUnderTest.execute();
    
    // Assert
    assertTrue(result.isSuccess());
    verify(mockService).updateState();
}
```

**Test Coverage Goals:**
- New features: 80%+ coverage
- Critical systems (card effects, combat): 90%+
- Integration tests for end-to-end flows

### Git Commit Messages

```
<type>: <subject>

<body>

Types: feat, fix, docs, refactor, test, chore
```

**Examples:**
```
feat: Add Athelas card effect
fix: Correct corruption calculation in Hunt
docs: Update DATABASE_REFERENCE with new queries
refactor: Extract political track to separate class
```

---

## File Layout Explanation

### Project Root
```
WOTR-GANDALF-Windsurf/
├── ARCHITECTURE.md          # High-level design
├── IMPLEMENTATION.md         # This file (how-to guide)
├── README.md                 # Project overview
├── ROADMAP.md                # Development plan
├── CHANGELOG.md              # Change history
├── pom.xml                   # Maven build config
├── src/                      # Java source code
├── database/                 # SQL schema files
├── data/                     # JSON game data
├── docs/                     # Technical documentation
└── fsm/                      # FSM server (Node.js)
```

### Source Code Organization
```
src/main/java/wotr/
├── Game.java                 # Main class, game state container
├── actions/                  # Action execution layer
├── cards/                    # Card system & effects
├── combat/                   # Combat resolution
├── dao/                      # Database access layer
├── database/                 # Connection management
├── fellowship/               # Fellowship mechanics
├── rules/                    # Rules engine
├── services/                 # Business logic
└── turn/                     # Turn management

src/test/java/wotr/           # Unit tests (mirror structure)
```

### Database Files
```
database/schema/
├── 01_create_base_tables.sql       # Foundation tables
├── 02_create_static_data.sql       # Reference data
├── 03_create_game_state_tables.sql # Game state
├── 07_create_unified_game_pieces.sql # Unified pieces
└── 08_create_on_table_cards.sql    # On-table cards
```

### Data Files
```
data/
├── eventcards.json           # 96 event cards
├── combatcards.json          # 70 combat cards
├── characters.json           # 13 characters
├── nations.json              # 9 nations
├── regions.json              # 104 regions
└── region_connections.json   # 209 adjacencies
```

### Documentation
```
docs/
├── BOARD_REFERENCE.md        # Board areas guide
├── DATABASE_REFERENCE.md     # Database guide
├── CARD_EFFECTS_IMPLEMENTATION.md # Card details
├── FIX-Valid-Actions-Communication.md # FSM protocol
└── archive/                  # Historical docs
```

---

## Technology Stack

**Runtime:**
- Java 1.8+ (compatible with Java 25)
- SQLite 3.x (embedded, file-based)

**Libraries:**
- GSON 2.x (JSON parsing)
- Flyway (database migrations)
- JUnit 5 + Mockito 5.3.1 (testing)

**Build:**
- Maven 3.6+
- Maven Shade (fat JAR packaging)

**FSM Server:**
- Node.js 16+
- sqlite3 npm package

---

## Project Statistics

**Code:**
- 241 Java source files
- ~15,800+ lines of game logic
- 8 database schema files
- 96 event cards (100% implemented)
- 70 combat cards

**Data:**
- 104 map regions
- 209 region connections
- 9 nations
- 13 characters
- 428 game pieces tracked

**Testing:**
- 44 card effect tests (95.5% passing)
- 15 fellowship tests (100% passing)

---

## Quick Command Reference

### Build Commands
```bash
mvn clean package              # Full build
mvn clean install              # Build + install to local repo
mvn test                       # Run tests only
mvn javadoc:javadoc            # Generate docs
```

### Run Commands
```bash
java -jar target/WOTR-GANDALF.jar              # Run game
java -jar target/WOTR-GANDALF.jar --debug      # Debug mode
```

### Database Commands
```bash
sqlite3 wotr_game.db                           # Open database
sqlite3 wotr_game.db "SELECT * FROM game_pieces LIMIT 10"
```

### FSM Commands
```bash
cd fsm/server
npm install                    # Setup
npm start                      # Run FSM server
npm test                       # Test FSM
```

---

## See Also

- [ARCHITECTURE.md](ARCHITECTURE.md) - System design & rationale
- [ROADMAP.md](ROADMAP.md) - Development phases
- [CHANGELOG.md](CHANGELOG.md) - Change history
- [README.md](README.md) - Project overview

---

**Last Updated:** November 18, 2025  
**Maintained By:** Development Team
