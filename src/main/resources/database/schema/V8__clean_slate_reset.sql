-- V8: Clean slate - Drop all old tables and start fresh
-- Drop all existing tables to eliminate obsolete data

-- Drop old tables in reverse dependency order
DROP TABLE IF EXISTS on_table_cards;
DROP TABLE IF EXISTS game_pieces;
DROP TABLE IF EXISTS scenario_setup;
DROP TABLE IF EXISTS scenarios;
DROP TABLE IF EXISTS hunt_tiles;
DROP TABLE IF EXISTS fellowship_members;
DROP TABLE IF EXISTS fellowship;
DROP TABLE IF EXISTS political_track;
DROP TABLE IF EXISTS army_units;
DROP TABLE IF EXISTS game_state;
DROP TABLE IF EXISTS turns;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS adjacencies;
DROP TABLE IF EXISTS settlements;
DROP TABLE IF EXISTS regions;
DROP TABLE IF EXISTS characters;
DROP TABLE IF EXISTS nations;

-- Create core reference tables
CREATE TABLE IF NOT EXISTS scenarios (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    piece_count INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create simplified scenario_setup table with card-friendly schema
CREATE TABLE IF NOT EXISTS scenario_setup (
    piece_id INTEGER NOT NULL,           -- Index in bits[] array
    scenario_id TEXT NOT NULL,           -- Reference to scenario
    piece_class TEXT NOT NULL,           -- Class name: FreeStrategyCard, UnitGondorRegular, etc.
    initial_area_id INTEGER NOT NULL,    -- Starting area index
    initial_area_name TEXT,              -- Human-readable area name (for reference)
    faction TEXT,                        -- 'free_peoples' or 'shadow'
    
    -- Card-specific fields (null for non-cards)
    small_image TEXT,                    -- Path to small card image
    big_image TEXT,                      -- Path to big card image
    card_name TEXT,                      -- Card name (with dual names if applicable)
    
    -- BattleCard-specific fields (null for non-BattleCards)
    small_back_image TEXT,               -- Path to small back card image (BattleCards only)
    big_back_image TEXT,                 -- Path to big back card image (BattleCards only)
    card_type TEXT,                      -- Card type string (BattleCards only)
    
    -- Unit-specific fields (null for cards)
    nation TEXT,                         -- Unit nation for military pieces
    unit_type TEXT,                      -- 'regular', 'elite', 'leader', 'nazgul'
    
    -- Metadata
    display_order INTEGER,
    notes TEXT,
    
    PRIMARY KEY (piece_id, scenario_id),
    FOREIGN KEY (scenario_id) REFERENCES scenarios(id)
);

-- Create index for faster lookups
CREATE INDEX IF NOT EXISTS idx_scenario_setup_lookup ON scenario_setup(scenario_id, initial_area_id);

-- Insert scenario metadata
INSERT INTO scenarios (id, name, description, piece_count) VALUES
    ('base', 'Base Game (2nd Edition)', 'Standard 2nd edition setup', 428),
    ('base_t', 'Base + Treebeard', 'Base game with Treebeard variant', 429),
    ('lome', 'Lords of Middle Earth', 'LOME expansion (includes base)', 452),
    ('lome_t', 'LOME + Treebeard', 'LOME expansion with Treebeard', 453),
    ('wome_t', 'Warriors + Treebeard', 'WOME expansion with Treebeard', 540),
    ('wome_lome_t', 'Full Game + Treebeard', 'All expansions with Treebeard', 564);

-- Note: scenario_setup data will be populated by running import_json_to_sql.py
-- which reads from the JSON exports and generates INSERT statements
