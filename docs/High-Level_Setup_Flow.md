## High-Level Setup Flow

### Variant Format Reference

Menu actions map to saved `varianttype` strings as follows:
- `base2` → `"base2"` (saved as `[WOTR-Gandalf2 wotr base2]`)
- `base2[T]` → `"base2[T]"` (saved as `[WOTR-Gandalf2 wotr base2[T]]`)
- `newLOME` → `"expansion2[L]"` (saved as `[WOTR-Gandalf2 wotr expansion2[L]]`)
- `newLOME[T]` → `"expansion2[LT]"` (saved as `[WOTR-Gandalf2 wotr expansion2[LT]]`)
- `newWOME` → `"base[WT]"` (saved as `[WOTR-Gandalf2 wotr base[WT]]`)
- `newWOME[L]` → `"expansion2[WLT]"` (saved as `[WOTR-Gandalf2 wotr expansion2[WLT]]`)

For this client, the initialization flow for a base game is:

### Base Game 2nd Edition Flow

```
Game() [Constructor - Entry Point]
  ↓
newBase2() [Resets game for base game 2nd edition]
  ↓
gameInit() [Initializes the board]
  ├── areaInit() [Builds areas[] array and loads board labels]
  ├── hybridPieceInit() → bitInit() [Creates pieces in initial non-deployed state]
  └── refreshBoard() [Resets piece display]
  ↓
resetBoard() [Moves pieces to their deployed positions on the map]
  ↓
baseSetup() [Moves B5A expansion pieces to Spare, adds base2 specifics like dwarf to Erebor]
```

### Lords of Middle-earth (LOME) Flow

```
Game() [Constructor - Entry Point]
  ↓
newLOME() [Resets game for LOME expansion - variants: expansion2[L] or expansion2[LT]]
  ↓
gameInit() [Initializes the board]
  ├── areaInit() [Builds areas[] array and loads board labels]
  ├── hybridPieceInit() → bitInit() [Creates base pieces in initial non-deployed state]
  └── refreshBoard() [Resets piece display]
  ↓
resetBoard() [Moves base pieces to their deployed positions on the map]
  ↓
baseSetup() [Moves B5A expansion pieces to Spare]
  ↓
bitLOMEinit() [Adds LOME expansion pieces: Elven ring dice, characters, cards]
  ├── Creates: Narya/Nenya/Vilya dice, Elrond, Gothmog, Mouth
  ├── Moves: Galadriel to FP Reinforcements, Balrog/CotR to Shadow Reinforcements
  └── Adds: LOME character/strategy cards, hunt tiles, action chits
```

### Warriors of Middle-earth (WOME) Flow

```
Game() [Constructor - Entry Point]
  ↓
newWOME() [Resets game for WOME expansion - variants: base[WT] or expansion2[WLT]]
  ↓
gameInit() [Initializes the board with WOME map]
  ├── areaInit() [Builds areas[] array and loads board labels]
  ├── hybridPieceInit() → bitInit() [Creates base pieces in initial non-deployed state]
  └── refreshBoard() [Resets piece display]
  ↓
resetBoard() [Moves base pieces to their deployed positions on the map]
  ↓
baseSetup() [Moves B5A expansion pieces to Spare, removes base companion cards]
  ↓
bitLOMEinit() [Optional - only if variant includes [L] flag]
  ↓
bitWOMEinit() [Adds WOME expansion pieces: Faction dice, Ents, Eagles, Deadmen, cards]
  ├── Creates: Shadow/Free faction dice
  ├── Adds: 8× Ents, 8× Eagles, 8× Deadmen (all to FP/Shadow reinforcements)
  ├── Creates: Towers (6×), Trebuchets (6×), Corsairs (6×), Dunlendings (12×)
  └── Adds: WOME character/strategy cards
```

### Key Concepts

1. **`bitInit()` — Initial State (Non-Deployed)**
   * Creates **all** pieces and assigns each a **fixed index** in `bits[]`
   * These indices are **permanent IDs** that never change
   * Pieces start in **reinforcement areas**: `areas[174]` (FP), `areas[176]` (Shadow)
   * This is the **initial, non-deployed state** where pieces wait to be deployed
   * **DATABASE (V008)**: Represents this initial state with all units in reinforcement areas
   * **PRESERVATION**: Array indices preserved exactly:
     - `bits[i]` → JSON `"index": i` → SQL `piece_id = i` → `bits[i]` restored

2. **`resetBoard()` — Deployment to Map**
   * Moves pieces from reinforcement areas to their **starting positions on the map**
   * Creates the **deployed board state** for actual gameplay
   * Example: Isengard units from `areas[176]` → Orthanc (40), Moria (30), North Dunland (26)
   * **NOTE**: Not stored in database - happens at runtime during initialization

3. **`baseSetup()` — Scenario-Specific Cleanup**
   * Moves **Battle of the Five Armies** expansion pieces to **Spare** (no longer used)
   * Adds scenario-specific units (e.g., Dwarf to Erebor for base2)
   * Moves LOME/WOME expansion cards and units to Spare if not in use
   * **NOTE**: Also not stored in database - happens at runtime after `resetBoard()`

4. **Variant Overlays**
   * **`newBase2()`**: Base game 2nd edition (optional Treebeard + Erebor dwarf)
   * **`newLOME()` / `bitLOMEinit()`**: Lords of Middle-earth expansion (Elven rings, characters)
   * **`newWOME()` / `bitWOMEinit()`**: Warriors of Middle-earth expansion (Ents/Eagles/Deadmen, faction dice)

### Execution Order Summary

**Base Game:**
```
bitInit()      → Create all pieces in reinforcement areas (V008 initial state)
resetBoard()   → Deploy pieces to their starting map positions (runtime only)
baseSetup()    → Clean up expansion pieces, add scenario-specific content (runtime only)
```

**LOME Expansion:**
```
bitInit()      → Create base pieces in reinforcement areas (V008 initial state)
resetBoard()   → Deploy base pieces to their starting map positions (runtime only)
baseSetup()    → Clean up B5A expansion pieces (runtime only)
bitLOMEinit()  → Add LOME pieces (V009 scenario) at numBits offset
```

**WOME Expansion:**
```
bitInit()      → Create base pieces in reinforcement areas (V008 initial state)
resetBoard()   → Deploy base pieces to their starting map positions (runtime only)
baseSetup()    → Clean up B5A expansion pieces and companion cards (runtime only)
bitLOMEinit()  → [Optional if [L] flag] Add LOME pieces (V009) at numBits offset
bitWOMEinit()  → Add WOME pieces (V010 scenario) at numBits offset
```

**Database Storage vs Runtime:**
- **V008 (base scenario)**: Stores initial state with pieces in reinforcement areas
- **V009 (lome scenario)**: Stores LOME expansion pieces in their initial areas
- **V010 (wome scenario)**: Stores WOME expansion pieces in their initial areas
- **Runtime (`resetBoard()`, `baseSetup()`)**: Deployment and cleanup happen in memory, not stored

### Variant Comparison Table

| Variant | `varianttype` | Entry Point | Base Pieces | +LOME | +WOME | Total Pieces | Database Scenarios |
|---------|---------------|-------------|-------------|-------|-------|--------------|-------------------|
| Base 2nd Ed | `base2` | `newBase2()` | 428 | - | - | 428 | `base` |
| Base 2nd Ed + Treebeard | `base2[T]` | `newBase2()` | 428 | +1 | - | 429 | `base` |
| LOME | `expansion2[L]` | `newLOME()` | 428 | +24 | - | 452 | `base`, `lome` |
| LOME + Treebeard | `expansion2[LT]` | `newLOME()` | 428 | +25 | - | 453 | `base`, `lome` |
| WOME | `base[WT]` | `newWOME()` | 428 | - | +111 | 539 | `base`, `wome` |
| WOME + LOME | `expansion2[WLT]` | `newWOME()` | 428 | +25 | +111 | 564 | `base`, `lome`, `wome` |

**Notes:**
- All variants start with the same 428 base pieces from `bitInit()`
- LOME adds 24-25 pieces (Treebeard is optional)
- WOME adds 111 pieces (units, dice, cards)
- Expansions preserve base piece indices by starting at `numBits`

---

## Piece Setup

### `bitInit()` — Initial Piece Creation (Non-Deployed)

* Creates **all** pieces in the game universe:

  * Base armies and leaders
  * Companions and minions
  * Nazgûl
  * Special characters
  * Dice (action dice, Elven ring dice, faction dice, etc.)
  * Expansion units (e.g., Ents, Eagles, Deadmen, Treebeard, LOME characters)
  
* For each piece:

  * Inserted into `bits[<index>]` at a fixed position
  * The index is its **permanent identity** for the entire runtime
  * Pieces start in **reinforcement areas**:
    - Free Peoples pieces → `areas[174]` (FP Reinforcements)
    - Shadow pieces → `areas[176]` (Shadow Reinforcements)
  
* This represents the **initial, non-deployed state** where all pieces exist but are not yet on the board

Everything that follows (resetBoard, baseSetup, variants) operates on this initial state.

---

### `resetBoard()` — Deploy to Starting Positions

* Called after `bitInit()` to move pieces from reinforcement areas to the map
* Creates the **deployed board state** where pieces are positioned for gameplay
* Example movements:
  * Isengard units from `areas[176]` → Orthanc, Moria, North Dunland, etc.
  * Gondor units from `areas[174]` → Minas Tirith, Pelargir, Dol Amroth, etc.
  * Fellowship from `areas[174]` → Rivendell

---

### `baseSetup()` — Scenario Cleanup

* Called after `resetBoard()` to finalize the specific scenario
* Primary functions:

  * Moves **Battle of the Five Armies** expansion pieces to **Spare** (no longer supported)
  * Moves LOME/WOME expansion content to Spare if not in the active variant
  * Adds scenario-specific content (e.g., Dwarf to Erebor for base2)
  
* The important conceptual role:

  > `baseSetup()` cleans up the deployed board by removing unused expansion content and adding scenario-specific pieces.

---

### `newBase2()` — Base2 Extras (Treebeard + Erebor Dwarf)

* After `baseSetup()` has produced the base game layout, `newBase2()` adds **base2-specific overlays**:

  * Enables **Treebeard** and brings him onto the board (or into the appropriate starting area).
  * Places a **Dwarf unit in Erebor**.
* This is currently done through interpreter commands like:
  `$TEST<~Scribe.141~>312<~Scribe.142~><~Game.2038~><~Scribe.143~><~Game.1965~><~Scribe.144~>`
* Implementation-wise, the key behavior is:

  * Identify Treebeard and the Erebor dwarf by their `bits[]` indices.
  * Move them from Reinforcements or default positions into their correct starting areas for the base2 variant.

---

### `newLOME()` / `bitLOMEinit()` — LOME Expansion

**`newLOME()`** - Entry point for LOME variant:

  * Sets `boardtype = "wotr"` and `varianttype = "expansion2[L]"` or `"expansion2[LT]"`
  * Calls standard initialization: `gameInit()` → `resetBoard()` → `baseSetup()`
  * Moves Dwarf to Erebor (piece 312) and Treebeard to Fellowship (piece 240)
  * Calls `bitLOMEinit()` to add expansion content

**`bitLOMEinit()`** - Adds LOME expansion pieces:

  * **Creates new pieces starting at `numBits` index** (preserves existing piece indices):
    * 3× Elven ring dice: `Narya`, `Nenya`, `Vilya` → `areas[174]` (FP Reinforcements)
    * `UnitElrond` → `areas[174]` (FP Reinforcements)
    * 4× LOME FP character cards → `areas[121]` (FP Character Deck)
    * 2× LOME FP strategy cards → `areas[122]` (FP Strategy Deck)
    * 2× Hunt tiles (special) → `areas[184]` (Hunt Pool)
    * `BalrogDice`, `GothmogDice` → `areas[176]` (Shadow Reinforcements)
    * `UnitGothmog`, `UnitMouth2` → `areas[176]` (Shadow Reinforcements)
    * 4× LOME Shadow character cards → `areas[123]` (SA Character Deck)
    * 2× LOME Shadow strategy cards → `areas[124]` (SA Strategy Deck)
    * 2× Action chits → `areas[176]` (Shadow Reinforcements)
    * Optional: `UnitTreebeard` if variant ends with "T]"
  
  * **Moves existing pieces** (already in `bits[]` from `bitInit()`):
    * `UnitGaladriel` → `areas[174]` (FP Reinforcements)
    * `UnitBalrog` → `areas[176]` (Shadow Reinforcements)
    * `UnitCotR` (Witch-King) → `areas[176]` (Shadow Reinforcements), sets leadership=1
  
  * **Updates `numBits`** to include all new LOME pieces (base 428 + 25 LOME = 453 total)

---

### `newWOME()` / `bitWOMEinit()` — WOME Expansion

**`newWOME()`** - Entry point for WOME variant:

  * Sets `boardtype = "wotr"` and `isWOME = true`
  * Sets `varianttype = "base[WT]"` (WOME only) or `"expansion2[WLT]"` (WOME+LOME)
  * Uses WOME board images: `images/board-w.jpg` and `images/map_mask_w.png`
  * Calls standard initialization: `gameInit()` → `resetBoard()` → `baseSetup()`
  * Moves additional base pieces to Spare (companion cards: 123, 115, 127, 136, 141, 144, 185, 191, 195)
  * Calls `bitLOMEinit()` **if variant contains "[L"** (for WOME+LOME combination)
  * Calls `bitWOMEinit()` to add WOME expansion content

**`bitWOMEinit()`** - Adds WOME expansion pieces:

  * **Creates new pieces starting at `numBits` index** (or after LOME pieces if combined):
    * `ShadowFactionDice` → `areas[176]` (Shadow Reinforcements)
    * `FreeFactionDice` → `areas[174]` (FP Reinforcements)
    * 8× `UnitEnt` → `areas[174]` (FP Reinforcements)
    * 8× `UnitEagle` → `areas[174]` (FP Reinforcements)
    * 8× `UnitDeadmen` → `areas[174]` (FP Reinforcements)
    * 6× `UnitTower` → `areas[176]` (Shadow Reinforcements)
    * 6× `UnitTrebuchet` → `areas[174]` (FP Reinforcements)
    * 6× `UnitCorsair` → `areas[176]` (Shadow Reinforcements)
    * 12× `UnitDunlending` → `areas[176]` (Shadow Reinforcements)
    * 26× WOME FP character/strategy cards → FP card decks
    * 35× WOME Shadow character/strategy cards → Shadow card decks
  
  * **Updates `numBits`** to include all new WOME pieces:
    * Base only: 428 + 111 WOME = 539 total
    * With LOME: 428 + 25 LOME + 111 WOME = 564 total
  
  * **Important**: WOME uses a different board layout (`board-w.jpg`) with modified area positions

---

## Board & Area Arrays

The game uses a set of parallel arrays to represent **board regions and key coordinate points**. These arrays are all indexed by the same **area index**, which acts as the stable **ID** for each region.

### `areas[]`

* `areas[i]` is the **logical Area object** for region `i`.
* Each Area represents a named region or space on the board:

  * Cities, strongholds, regions, tracks, off-board boxes (Spare, eliminated), etc.
* The **index `i` is the Area ID**:

  * Game rules, piece placement, and macros refer to areas by index (e.g., `areas[174]`, `areas[176]`).
  * This index must remain stable; other data and logic assume it.

> Think of `areas[]` as “the master list of board regions, keyed by integer ID.”

---

### `areaCoords[]`

* `areaCoords[i]` is the **primary display coordinate** for `areas[i]` on the board image.
* Typically used as the point where units are drawn or stacked on the map for that area.
* This coordinate ties the logical Area (in `areas[]`) to its **visual position** on the bitmap board.

> “Where do I draw the armies for Area i?” → look at `areaCoords[i]`.

---

### `areaIDs[]` (if present)

Naming can vary across codebases, but typically:

* `areaIDs[i]` provides a way to associate the **internal area index** with some external or logical ID:

  * For example: a numeric or string ID used in logs, save files, network protocols, or scripts.
* It acts as a **mapping layer** between:

  * The internal `areas[]` index (used by the Java engine), and
  * Whatever ID system is used externally (e.g., FSM server, replay logs, AI, or human-readable script IDs).

Even if it is simple or redundant in places, the important conceptual role is:

> “Given internal area index i, what external ID or lookup value should we use for other systems?”

---

### `areaChits[]`

* `areaChits[i]` is the **coordinate for placing chits or markers** associated with `areas[i]`.
* Chits include things like:

  * Special markers
  * Tokens that sit **near** the area rather than directly on the army stack
  * Additional indicators that shouldn't overlap with units

This lets the client draw chits at a visually distinct point:

> “Where do I draw the chit for Area i?” → `areaChits[i]`.

This separation avoids clutter and overlapping graphics when multiple artifact types share the same logical area.

---

### `areaControlPoints[]`

* `areaControlPoints[i]` is the **coordinate for control/ownership markers** for `areas[i]`.
* Used to show:

  * Which side controls a region (Free Peoples, Shadow, neutral).
  * Political or conquest markers that are typically placed in a **fixed small icon spot** on or near the area.
* This lets the game draw control icons separately from armies and chits.

> “Where do I draw the control marker for Area i?” → `areaControlPoints[i]`.

---

## Why These Arrays Matter for the Refactor

When you move hardcoded data into a database or configuration layer, the key constraints are:

* **Area IDs are the indices** of `areas[]`.
  Everything else (`areaCoords`, `areaChits`, `areaControlPoints`, and any external references) must be keyed off the same index.

* Each of those parallel arrays is simply **a different visual or logical “layer” on top of the same area index**:

  * `areas[i]` → logical board region.
  * `areaCoords[i]` → where units are drawn for that region.
  * `areaChits[i]` → where chits/markers are drawn.
  * `areaControlPoints[i]` → where control markers are drawn.

As you externalize data, the main rule is:

> **Never change the meaning of index i.**
> Instead, load all the properties (name, type, coordinates) for area *i* from data, and keep the same `i` across `areas[]`, `areaCoords[]`, `areaChits[]`, `areaControlPoints[]`, and any piece/variant setup that references it.

**Future Database Migration (Phase 3):**

When `areas[]` and coordinate arrays move to the database, the same index preservation pattern used for `bits[]` will apply:

**Single `areas` table** with all data aligned by `area_id`:
```sql
CREATE TABLE areas (
    area_id INTEGER PRIMARY KEY,  -- Preserves areas[i] index
    name TEXT,
    type TEXT,
    coord_x INTEGER,               -- areaCoords[i].x
    coord_y INTEGER,               -- areaCoords[i].y
    chit_x INTEGER,                -- areaChits[i].x
    chit_y INTEGER,                -- areaChits[i].y
    control_x INTEGER,             -- areaControlPoints[i].x
    control_y INTEGER,             -- areaControlPoints[i].y
    properties TEXT                -- JSON for additional data
);
```

**Benefits:**
- All coordinates in one row - no joins needed
- Guaranteed alignment - can't have missing coordinate records
- Same pipeline: Runtime → JSON dump → SQL migration → Runtime load
- Perfect preservation: `areas[42]` → `area_id = 42` → `areas[42]`

---

## Array Index Preservation Pipeline

The system preserves `bits[]` array indices through the entire dump-to-database-to-load cycle:

### 1. Runtime → JSON Dump

**File**: `Game.java::dumpGameStateToFile()`

```java
for (int i = 0; i < numBits; i++) {
    GamePiece piece = bits[i];
    writer.println("    \"index\": " + i + ",");  // Array index preserved
    // ... other piece data
}
```

**Output**: `base_dump.json`, `lome_dump.json`, etc.

### 2. JSON → SQL Migration

**File**: `scripts/generate_v008.py`

```python
bits_index = piece['index']  # Read from JSON
sql = f"INSERT INTO scenario_setup (piece_id, ...) VALUES ({bits_index}, ...)"
# piece_id = bits[] array index
```

**Output**: 
- `V008__deployed_board_state.sql` (base scenario - 428 pieces in **initial state**, all units in reinforcement areas)
- `V009__lome_scenario.sql` (LOME expansion pieces 428-452 in initial state)
- `V010__wome_scenario.sql` (WOME expansion pieces 428-538 or 453-563 if with LOME)

### 3. Database → Runtime Load

**File**: `ScenarioLoader.java::loadScenarioData()`

```java
setup.pieceId = rs.getInt("piece_id");  // Database piece_id
```

**File**: `Game.java::hybridPieceInit()`

```java
int arrayIndex = setup.pieceId;   // Use explicit bits[] index from DB
dbPieces[arrayIndex] = piece;     // Place at exact position
```

### 4. Scenario Loading Strategy

**File**: `Game.java::hybridPieceInit()` → `getScenariosForVariant()`

The game loads different scenario combinations based on `varianttype`:

```java
// Base game variants
if (varianttype.equals("base2") || varianttype.equals("base2[T]")) {
    return Arrays.asList("base");
}

// LOME variants  
if (varianttype.equals("expansion2[L]") || varianttype.equals("expansion2[LT]")) {
    return Arrays.asList("base", "lome");
}

// WOME only
if (varianttype.equals("base[WT]")) {
    return Arrays.asList("base", "wome");
}

// WOME + LOME
if (varianttype.equals("expansion2[WLT]")) {
    return Arrays.asList("base", "lome", "wome");
}
```

Scenarios are loaded **sequentially** - each adds pieces at specific indices without overwriting previous scenarios.

### Result

**Perfect index preservation**: `bits[42]` in runtime → `piece_id = 42` in database → `bits[42]` when loaded

This guarantees:
- Piece identity remains stable across sessions
- No renumbering or reordering required
- Database state exactly matches runtime state
- All piece references (e.g., in game logs, FSM state) remain valid
- Expansion scenarios can be layered on top of base scenario without conflicts
