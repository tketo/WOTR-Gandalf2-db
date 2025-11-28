-- Game State Tables
-- These tables represent the physical game board state
-- Query these tables to see "where pieces are" like looking at a physical board

-- Character positions (where are the character pieces?)
CREATE TABLE IF NOT EXISTS character_positions (
    game_id TEXT NOT NULL,
    character_id TEXT NOT NULL,
    region_id TEXT,
    status TEXT NOT NULL DEFAULT 'not_entered',
    PRIMARY KEY (game_id, character_id),
    FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (region_id) REFERENCES regions(id)
);

-- ============================================
-- DEPRECATED: army_units table
-- ============================================
-- This table is DEPRECATED as of Nov 16, 2024
-- Replaced by: game_pieces table (schema 07_create_unified_game_pieces.sql)
-- 
-- The new game_pieces table provides:
-- - 1:1 mapping to bits[] array (each piece tracked individually)
-- - Support for all piece types (not just military units)
-- - Real-time sync with game state
-- - Better performance (indexed lookups)
-- 
-- DO NOT USE THIS TABLE FOR NEW CODE
-- Use the query methods in Game.java instead:
-- - getPiecesInArea(areaId)
-- - getPiecesInAreaByType(areaId, type)
-- - countPiecesInArea(areaId)
-- - countMilitaryUnitsInArea(areaId)
-- ============================================

-- Army units (what tokens are on the board?) [DEPRECATED]
CREATE TABLE IF NOT EXISTS army_units (
    game_id TEXT NOT NULL,
    region_id TEXT NOT NULL,
    nation_id INTEGER NOT NULL,
    unit_type TEXT NOT NULL,
    count INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (game_id, region_id, nation_id, unit_type),
    FOREIGN KEY (region_id) REFERENCES regions(id),
    FOREIGN KEY (nation_id) REFERENCES nations(id),
    CHECK (unit_type IN ('regular', 'elite', 'leader')),
    CHECK (count >= 0)
);

-- Region control (who owns what territory?)
CREATE TABLE IF NOT EXISTS region_control (
    game_id TEXT NOT NULL,
    region_id TEXT NOT NULL,
    controlled_by TEXT NOT NULL DEFAULT 'neutral',
    under_siege BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (game_id, region_id),
    FOREIGN KEY (region_id) REFERENCES regions(id),
    CHECK (controlled_by IN ('free_peoples', 'shadow', 'neutral'))
);

-- Fellowship state (where is the ring?)
CREATE TABLE IF NOT EXISTS fellowship_state (
    game_id TEXT PRIMARY KEY,
    region_id TEXT NOT NULL,
    corruption_level INTEGER NOT NULL DEFAULT 0,
    revealed BOOLEAN NOT NULL DEFAULT 0,
    guide_character_id TEXT,
    FOREIGN KEY (region_id) REFERENCES regions(id),
    FOREIGN KEY (guide_character_id) REFERENCES characters(id),
    CHECK (corruption_level >= 0 AND corruption_level <= 12)
);

-- Current game state (turn/phase tracker)
CREATE TABLE IF NOT EXISTS game_state (
    game_id TEXT PRIMARY KEY,
    turn_number INTEGER NOT NULL DEFAULT 1,
    phase TEXT NOT NULL DEFAULT 'recover_action_dice',
    active_player TEXT NOT NULL,
    victory_points_fp INTEGER NOT NULL DEFAULT 0,
    victory_points_shadow INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    CHECK (active_player IN ('free_peoples', 'shadow')),
    CHECK (turn_number >= 1 AND turn_number <= 16),
    CHECK (victory_points_fp >= 0 AND victory_points_fp <= 4),
    CHECK (victory_points_shadow >= 0 AND victory_points_shadow <= 10)
);

-- Action dice (what dice are available?)
CREATE TABLE IF NOT EXISTS action_dice (
    game_id TEXT NOT NULL,
    die_id TEXT NOT NULL,
    nation_id INTEGER NOT NULL,
    die_type TEXT NOT NULL,
    used BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (game_id, die_id),
    FOREIGN KEY (nation_id) REFERENCES nations(id),
    CHECK (die_type IN ('character', 'army', 'muster', 'event', 'will_of_west', 'eye'))
);

-- Cards in play (where are the cards?)
CREATE TABLE IF NOT EXISTS cards_in_play (
    game_id TEXT NOT NULL,
    card_id TEXT NOT NULL,
    card_type TEXT NOT NULL,
    owner TEXT NOT NULL,
    location TEXT NOT NULL DEFAULT 'deck',
    PRIMARY KEY (game_id, card_id),
    CHECK (card_type IN ('character', 'strategy', 'event')),
    CHECK (owner IN ('free_peoples', 'shadow')),
    CHECK (location IN ('deck', 'hand', 'discard', 'table', 'removed'))
);

-- Turn history (action log for undo/replay)
CREATE TABLE IF NOT EXISTS turn_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    game_id TEXT NOT NULL,
    turn_number INTEGER NOT NULL,
    player TEXT NOT NULL,
    action_type TEXT NOT NULL,
    action_data_json TEXT NOT NULL,
    timestamp TEXT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game_state(game_id)
);

-- Nation political state (at war, activated)
CREATE TABLE IF NOT EXISTS nation_politics (
    game_id TEXT NOT NULL,
    nation_id INTEGER NOT NULL,
    at_war BOOLEAN NOT NULL DEFAULT 0,
    activated BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (game_id, nation_id),
    FOREIGN KEY (nation_id) REFERENCES nations(id)
);
