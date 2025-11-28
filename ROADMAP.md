# WOTR-GANDALF Development Roadmap

## Project Vision

Transform WOTR-GANDALF into a fully rules-enforced, turn-based strategy game with proper UI controls for game phases and turns.

### Core Architecture: "Database as Board"

**Key Concept:** The database represents the physical game board. Game state queries mirror how you'd look at a physical board:
- "Where is Strider?" → `SELECT region_id FROM character_positions WHERE character_id = 'strider'`
- "What armies are in Minas Tirith?" → `SELECT * FROM army_units WHERE region_id = 'minas_tirith'`
- "Is the Fellowship revealed?" → `SELECT revealed FROM fellowship_state`

This eliminates complex in-memory state management and makes rules validation straightforward database queries.

---

## Current Status: Phase 6 Complete - Ready for Polish 🎉

**Progress: ~90% Complete**

### ✅ COMPLETED PHASES (Phases 1-4)

#### Phase 1: Database Foundation (COMPLETE)
- SQLite database with 16 tables
- DatabaseManager with auto-initialization  
- Model classes and DAO layer
- 9 nations, 104 regions, 13 characters in database

#### Phase 2: Game State Schema & Integration (COMPLETE)
- Game state tables (character_positions, army_units, region_control, etc.)
- GameDataService: Static reference data cache
- GameStateService: Dynamic board queries
- ScenarioService: Database-driven scenario loading

#### Phase 3: Rules Engine Foundation (COMPLETE)
- RulesEngine: Central validation coordinator
- MovementRules: Character & army movement validation
- CombatRules: Strength calculation and resolution
- VictoryRules: Victory condition checking

#### Phase 4: Turn & Phase Management (COMPLETE)
- GamePhase enum: 6 official War of the Ring phases
- TurnManager: State machine for turn progression
- Phase-specific action validation

### ✅ FOUNDATION WORK (6/6 COMPLETE - 100%)

**All foundational systems built before UI development:**

1. **Region Adjacency** ✅
   - Complete game map: 104 regions, 209 connections
   - Movement validation with terrain types
   - Database-driven adjacency queries

2. **Nation Factions** ✅
   - Free Peoples vs Shadow tracking
   - 9 nations with faction data
   - Political state management

3. **Action Dice System** ✅
   - Complete dice pool management
   - Hunt Box mechanics
   - Die rolling and allocation
   - Faction-specific results (Will vs Eye)

4. **Complete Movement Rules** ✅
   - Full Middle-earth map topology
   - Real adjacency validation
   - Movement cost calculation

5. **Expand Combat Rules** ✅
   - Combat dice rolling (max 5 dice)
   - Natural 1/6 mechanics
   - Leader re-roll system
   - Terrain modifiers (field, city, fortification, siege)

6. **Basic Card Mechanics** ✅
   - 4 complete decks (FP Character/Strategy, Shadow Character/Strategy)
   - 96 event cards + 70 combat cards loaded from JSON
   - Hand management with limits
   - Draw and shuffle mechanics
   - Die requirement validation

**Foundation Statistics:**
- **30 new files created**
- **3,645 lines of foundation code**
- **All tests passing**
- **Build: SUCCESS**

#### Phase 5: Turn & Phase Controls (SUPERSEDED BY FSM ⚠️)
- TurnControlPanel: Turn/phase display and controls
- PhaseIndicatorPanel: Visual phase progress indicator
- TurnOrchestrator: Integration layer between TurnManager and UI
- PhaseActionValidator: Phase-specific action validation
- TurnPhaseDemo: Standalone demo application

**Phase 5 Statistics:**
- **5 new UI files created**
- **960 lines of UI code**
- **All components tested**
- **Build: SUCCESS**

**Note:** Phase 5 components remain in codebase for demo/reference purposes but are not used in production. They were superseded by:
- **FsmPhasePanel** - FSM-driven UI integrated into Controls.java
- **FsmConnection** - External FSM server for turn/phase arbitration
- Phase-specific UI configs (Phase1Config - Phase6Config) in wotr.ui.fsm package

---

## ✅ COMPLETE: Phase 6 - Full Gameplay Integration

**Goal:** Complete gameplay integration with action workflows

**Completed:** November 16, 2025  
**Progress:** 100% Complete

### What Already Exists:
✅ Game board with all regions  
✅ Player hand display  
✅ Dice visualization  
✅ Unit and character display  
✅ All action buttons and controls  
✅ Game state displays  
✅ Turn & Phase UI (Phase 5 ✅)

### ✅ COMPLETED (Phase 6.1):

1. **Action Resolution Workflows** ✅
   - ✅ ActionExecutor system for all die types
   - ✅ Character movement with Hunt mechanics
   - ✅ Army movement and mustering
   - ✅ Event card execution framework
   - ✅ Combat initiation workflow

2. **Hunt Mechanic System** ✅
   - ✅ HuntMechanic class with full dice rolling
   - ✅ Hunt outcomes (Safe, Revealed, Damage, Heavy Damage)
   - ✅ Corruption tracking
   - ✅ Terrain modifiers
   - ✅ Fellowship state management (reveal/hide)

3. **Combat Resolution System** ✅
   - ✅ CombatResolver with multi-round combat
   - ✅ Combat dice rolling (max 5 dice, hits on 5-6)
   - ✅ Terrain modifiers (city/fortification bonuses)
   - ✅ Casualty allocation (regulars first, then elites)
   - ✅ Combat state tracking
   - ✅ Retreat framework

4. **TurnOrchestrator Integration** ✅
   - ✅ All action methods wired to UI
   - ✅ Notifications for combat and Hunt events
   - ✅ Error handling and validation

**Phase 6.1 Statistics:**
- **7 new files created**
- **1,350+ lines of action workflow code**
- **ActionExecutor, HuntMechanic, CombatResolver complete**
- **Full integration with TurnManager and UI**

### ✅ COMPLETED (Phase 6.2):

1. **Card Effect System** ✅
   - ✅ CardEffectExecutor framework
   - ✅ Category-based effect execution
   - ✅ Card validation and prerequisites
   - 🚧 Specific card implementations (Phase 7)

2. **Fellowship Management System** ✅ (Nov 16, 2025)
   - ✅ Database-backed Fellowship queries
   - ✅ Fellowship guide management (with Ring Bearer validation)
   - ✅ Companion addition and removal
   - ✅ Character elimination to FP/Shadow casualties
   - ✅ Spare pieces handling (character transformations)
   - ✅ Fellowship composition tracking (Fellowship box area)
   - ✅ All 15 FellowshipManager tests passing

3. **Political Track** ✅
   - ✅ Nation activation system
   - ✅ Political position tracking (Not at War / At War)
   - ✅ Action dice allocation
   - ✅ Track advancement mechanics

### Technical Approach:
- ✅ Wire action buttons to TurnOrchestrator
- ✅ Implement full action workflows
- ✅ Complete combat resolution system
- ✅ Integrate Fellowship Hunt mechanics
- ✅ Execute card effects via RulesEngine
- ✅ Leader re-roll mechanics (Nov 16, 2025)
- ✅ Database-driven architecture Phase 1 (Nov 16, 2025)

### Database Architecture Evolution:
- ✅ **Phase 1 Complete:** Database refresh capability (Nov 16, 2025)
  - `refreshFromDatabase()` syncs memory from database
  - `verifyDatabaseSync()` validates consistency
  - Foundation for "dumb client" architecture
- ✅ **Phase 2 Complete:** Hybrid initialization + Image Assets (Nov 16, 2025)
  - `scenario_setup` table with deployed state (V008 migration)
  - Runtime dump captures exact game state (428 pieces)
  - `ScenarioLoader` service loads pieces from database
  - `hybridPieceInit()` tries database first, falls back to bitInit()
  - Python generator creates migrations from runtime state
  - **Image asset system:** All pieces store image paths in database
  - Convention-based image generation for units, cards, chits, tiles
  - Database-driven asset management ready for modding
  - Zero code changes needed - graceful degradation
  - **✅ Index preservation verified:** `bits[i]` → `piece_id = i` → `bits[i]` (100% match)
- 🔮 **Phase 3 Future:** Full database-driven
  - Remove hardcoded bitInit() (save 10,000 lines)
  - **Database-driven areas with coordinates:** Migrate `areas[]` and all coordinate arrays to database with index preservation
    - **Single areas table** with all coordinate data:
      - `area_id` (PRIMARY KEY) - Preserves array index: `areas[i]` → `area_id = i`
      - `name` - Area name
      - `type` - Area type (region, city, track, off-board, etc.)
      - `coord_x`, `coord_y` - Primary unit display (`areaCoords[i]`)
      - `chit_x`, `chit_y` - Chit/marker placement (`areaChits[i]`)
      - `control_x`, `control_y` - Control marker position (`areaControlPoints[i]`)
      - `properties` - JSON for additional area-specific data
      - Guaranteed alignment: All coordinates in same row, no joins needed
    - **Migration tooling:**
      - Dump tool for areas and coordinates (similar to piece dumper)
      - Migration generator for V012+ area and coordinate data
      - Validation: All coordinate arrays must align with areas array length
    - **Hybrid loading:**
      - `hybridAreaInit()` loads areas and all coordinates from single table
      - Falls back to areaInit() if database is empty or unavailable
      - Single query populates: areas[], areaCoords[], areaChits[], areaControlPoints[]
    - **Code reduction:** Remove hardcoded areaInit() (~1,000+ lines)
  - Game.java becomes pure view layer

---

## ✅ COMPLETE: Phase 7 - Card Effects & Testing

**Goal:** Complete testing, implement essential card effects, and polish UI

**Completed:** November 2025  
**Progress:** 100% Complete (96 event cards, 100% coverage ✅)

### ✅ COMPLETED (Phase 7.1): Essential Card Effect Framework

**Created:** November 16, 2025

1. **Specialized Effect Handlers** ✅
   - `FellowshipCardEffects` - 8 Fellowship cards implemented
   - `PoliticalCardEffects` - 5 political/nation cards implemented
   - `ShadowCardEffects` - 8 Shadow cards implemented
   - Integrated with `CardEffectExecutor`

2. **Tier 1 Cards Implemented** ✅ (21 cards total)
   - Athelas, Bilbo's Song, There is Another Way
   - I Will Go Alone, We Prove the Swifter, Gwaihir
   - Challenge of the King, Mirror of Galadriel
   - Wisdom of Elrond, Book of Mazarbul, Fear! Fire! Foes!
   - The Red Arrow, There and Back Again
   - The Ringwraiths Are Abroad, The Black Captain Commands
   - Dreadful Spells, Wormtongue
   - The Shadow is Moving, The Shadow Lengthens
   - Return of the Witch-king, Corsairs of Umbar

3. **GameStateService Extensions** ✅
   - Added `adjustCorruption()` for healing/adding corruption
   - Added `setFellowshipRevealed()` for reveal/hide
   - Added `changeDieResult()` for die manipulation

**Phase 7.1 Statistics:**
- **3 new effect handler files**
- **~800 lines of card effect code**
- **21 Tier 1 cards fully implemented**
- **4 DAO methods added** for political piece queries
- **Documentation:** CARD_EFFECTS_IMPLEMENTATION.md created
- **Build:** SUCCESS ✅

### ✅ COMPLETED (Phase 7.2): Tier 2 Card Effects

**Created:** November 16, 2025

1. **Hunt & Fellowship Protection Cards** ✅ (13 cards)
   - Elven Cloaks, Elven Rope, Phial of Galadriel
   - Sméagol Helps Nice Master, Mithril Coat and Sting
   - Axe and Bow, Horn of Gondor, Wizard's Staff
   - Shelob's Lair, The Ring is Mine!, On On They Went
   - "They Are Here!", Lure of the Ring

2. **Army Movement & Recruitment Cards** ✅ (9 cards)
   - Through a Day and a Night, Paths of the Woses
   - Help Unlooked For, Cirdan's Ships, Faramir's Rangers
   - Rage of the Dunlendings, The Fighting Uruk-hai
   - Grond Hammer of the Underworld, Shadows Gather

3. **Special & Unique Cards** ✅ (8 cards)
   - The Ents Awake (3 variants: Treebeard, Huorns, Entmoot)
   - Dead Men of Dunharrow, The Eagles are Coming!
   - House of the Stewards, The Grey Company
   - The Last Battle, A Power too Great

**Phase 7.2 Statistics:**
- **3 new effect handler files** (HuntCardEffects, ArmyCardEffects, SpecialCardEffects)
- **~700 lines of card effect code**
- **30 Tier 2 cards fully implemented**
- **Grand Total: 51 cards (~53% of all event cards)**
- **Build:** SUCCESS (240 source files) ✅

### ✅ COMPLETED (Phase 7.3): Testing & Additional Cards

**Created:** November 16, 2025

1. **Additional Card Effects** ✅ (12 cards)
   - Fellowship: Gandalf's Cart, Hobbit Stealth, Use Palantír, Shadowfax, Kindred of Glorfindel, Anduril
   - Shadow: Balrog of Moria, Snaga! Snaga!, Hordes from the East, Morgul Sorcery, Await the Call, Black Sails

2. **Comprehensive Unit Tests** ✅ (44 tests, 95.5% passing)
   - FellowshipCardEffectsTest (19 tests)
   - PoliticalCardEffectsTest (8 tests)
   - ShadowCardEffectsTest (17 tests)
   - Full mocking infrastructure with Mockito
   - Java 25 compatibility configured

**Phase 7.3 Statistics:**
- **Test files:** 3 (~700 lines)
- **Tests created:** 44 (44 passing - 100% ✅)
- **Additional cards:** 12
- **Grand Total: 63 cards (~65% of all event cards)**
- **Test coverage:** Core card effects validated ✅
- **Bug fixes:** Piece removal errors resolved, test mocking improved

### ✅ COMPLETE (Phase 7.4): All Remaining Cards

**Completed:** November 2025

#### All Batches Complete:
1. ✅ **Batch 1: On-Table Cards** (6 cards)
   - OnTableCardManager infrastructure (131 lines)
   - Database tracking for on-table card effects
   
2. ✅ **Batch 2: Combat Cards** (7 cards)
   - Integrated with CombatResolver
   - Combat bonuses and special effects

3. ✅ **Batch 3: Less-Common Cards** (20 cards)
   - Fellowship, Political, Army, Hunt, and Nazgûl cards

**Phase 7.4 Complete:**
- **Total Cards:** 96/96 (100% coverage ✅)
- **Build:** SUCCESS (241 source files)
- **Tests:** 44 unit tests (95.5% passing)

### Phase 7 Achievements:

#### 1. **Essential Card Effects** ✅
   - 96/96 event cards implemented (100% coverage)
   - 44 unit tests created (95.5% passing)
   - 6 specialized effect handler classes
   - OnTableCardManager for persistent card effects
   - Combat card integration with CombatResolver
   - Database-as-board architecture for political track

---

## 🎯 NEXT: Phase 8 - Testing & Polish

**Goal:** Complete testing, bug fixes, and final polish for v1.0 release

**Estimated Time:** 1-2 weeks

### Priority Tasks:

#### 1. **Integration Testing** (1 week)
   - End-to-end gameplay validation
   - Multi-step action workflows
   - Hunt mechanic edge cases
   - Combat round progression testing
   - Card effect integration tests

#### 2. **Bug Fixes & Stability** (1 week)
   - Address known edge cases
   - Performance optimization
   - Memory leak detection
   - Error handling improvements

#### 3. **Documentation & Release Prep**
   - User guide for gameplay
   - Developer documentation updates
   - Release notes preparation
   - Version 1.0 milestone verification

---

## Future Phases (Post-v1.0)

### Phase 9: Enhanced Features
- **3 & 4 Player Support**: Expand beyond 2-player games
- **Victory Conditions**: Automated victory detection and game end
- **Tutorial System**: Interactive tutorial for new players
- **Advanced Scenarios**: Lords of Middle-earth, Warriors expansion

### Phase 10: AI & Automation
- **FSM AI Enhancements**:
  - Smarter AI decision-making algorithms
  - Difficulty levels (Easy, Medium, Hard)
  - AI evaluates board position before choosing actions
  - AI plays both factions for full automation
- **More FSM Automation**:
  - Auto-execute simple actions (draw cards, advance phase)
  - Suggest optimal moves to human players
  - Replay mode with FSM control
- **Network Play via FSM**:
  - FSM server acts as game server
  - Multiple clients connect to same FSM
  - Synchronize game state across clients
  - Spectator mode

### Phase 11: Polish & Expansion
- **Sound Effects**: Audio feedback for actions
- **Animations**: Smooth transitions and effects
- **UI Themes**: Customizable visual themes
- **Replay System**: Game replay and analysis features
- **Mod Support**: Custom scenarios and house rules

### Long-term Enhancements
- **Online Multiplayer**: Network play with lobby system
- **Campaign Mode**: Linked scenario campaigns
- **Statistics & Analytics**: Game statistics tracking
- **Tournament Support**: Organized play and rankings

---

## Technology Stack

**Backend:**
- Java 1.8
- SQLite database
- GSON for JSON parsing

**Frontend:**
- Java Swing (existing)
- Custom game board rendering

**Testing:**
- JUnit for unit tests
- Manual integration testing

**Build:**
- Maven
- Git version control

---

## Success Metrics

**Phase 5 Goals:**
- ✅ All game phases accessible via UI
- ✅ Dice rolling and allocation UI functional
- ✅ Card drawing and playing via UI
- ✅ Basic board visualization complete
- ✅ Turn progression smooth and intuitive

**User Experience:**
- < 3 clicks to perform any action
- Clear indication of valid moves
- Responsive UI (< 100ms for actions)
- Zero confusion about game state

---

## Timeline Summary

| Phase | Duration | Status |
|-------|----------|--------|
| Phase 1: Database | 2 weeks | ✅ Nov 2025 |
| Phase 2: Integration | 1 week | ✅ Nov 2025 |
| Phase 3: Rules Engine | 1 week | ✅ Nov 2025 |
| Phase 4: Turn System | 3 days | ✅ Nov 2025 |
| Foundation Work (6 systems) | 2 weeks | ✅ Nov 2025 |
| Phase 5: Turn & Phase Controls | 1 week | ✅ Nov 2025 |
| Phase 6.1: Action Workflows | 1 week | ✅ Nov 2025 |
| Phase 6.2: Card Effects & Fellowship | 1 week | ✅ Nov 2025 |
| Phase 7: Card Effects & Testing | 3-4 weeks | ✅ Nov 2025 |
| **Phase 8: Testing & Polish** | **1-2 weeks** | **NEXT** |

**Current Progress:** ~95% Complete  
**Estimated to v1.0:** ~1-2 weeks

---

## Key Achievements

**Code Statistics:**
- 234 source files
- ~13,700+ lines of game logic
- 8 schema files, 18 database tables (V008: 428 pieces)
- Complete War of the Ring card set (166 cards)
- Full game map (104 regions, 209 connections)
- Phase 5: 5 UI components (960 lines)
- Phase 6: 10+ action/fellowship files (1,800+ lines)
- Database Architecture Phase 2: 
  - ScenarioLoader service (129 lines)
  - Hybrid initialization (170 lines)
  - Runtime dump tooling with image capture (250 lines)
  - Python migration generator with image paths (370 lines)
  - Image asset system documentation (350 lines)
- All Phase 6 tests passing (15/15 Fellowship tests ✅)
- Build: SUCCESS ✅

**System Capabilities:**
- ✅ Complete database-driven game state ("database as board")
- ✅ Hybrid initialization: Database-first with graceful bitInit() fallback
- ✅ **Image asset system:** All 428 pieces store image paths in database
- ✅ Convention-based asset management (units, cards, chits, tiles)
- ✅ Unified game_pieces table (1:1 mapping to physical pieces)
- ✅ Rules validation for movement, combat, victory
- ✅ Turn and phase management with orchestration layer
- ✅ Action dice system with Hunt Box mechanics
- ✅ Card management (draw, play, shuffle, hand limits)
- ✅ Complete Middle-earth map topology (104 regions, 209 connections)
- ✅ Combat resolution (dice rolling, casualties, terrain modifiers)
- ✅ Hunt mechanics with corruption tracking and outcomes
- ✅ Fellowship management (guide, companions, casualties, spare)
- ✅ Political track system with nation activation
- ✅ Card effect execution framework (96/96 event cards)
- ✅ Save/Load system with complete state persistence
- ✅ 2-player multiplayer support
- ✅ FSM server for AI orchestration

---

**Document Version:** 4.1  
**Last Updated:** November 20, 2025  
**Status:** Phase 7 Complete - All Card Effects Implemented 🎉  
**Latest:** Database index preservation verified for `bits[]` array; `areas[]` database migration planned for Phase 3
