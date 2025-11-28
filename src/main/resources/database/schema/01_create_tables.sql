-- ============================================
-- WOTR-GANDALF Database Schema
-- Version: 1.0
-- ============================================

-- Nations table
CREATE TABLE IF NOT EXISTS nations (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

-- Regions table  
CREATE TABLE IF NOT EXISTS regions (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    nation_id INTEGER,
    FOREIGN KEY (nation_id) REFERENCES nations(id)
);

-- Settlements table
CREATE TABLE IF NOT EXISTS settlements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    region_id TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL CHECK(type IN ('city', 'town', 'stronghold', 'fortification')),
    vp INTEGER DEFAULT 0,
    can_muster BOOLEAN DEFAULT 0,
    FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE CASCADE
);

-- Characters table
CREATE TABLE IF NOT EXISTS characters (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    title TEXT,
    faction TEXT NOT NULL CHECK(faction IN ('Free Peoples', 'Shadow')),
    type TEXT NOT NULL CHECK(type IN ('companion', 'minion')),
    level TEXT,
    leadership INTEGER DEFAULT 0,
    action_die_bonus INTEGER DEFAULT 0,
    can_guide BOOLEAN DEFAULT 0,
    playable_by TEXT
);

-- Character Abilities table (one-to-many)
CREATE TABLE IF NOT EXISTS character_abilities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    character_id TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE
);

-- Character Effects table (complex triggers stored as JSON)
CREATE TABLE IF NOT EXISTS character_effects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    character_id TEXT NOT NULL,
    name TEXT NOT NULL,
    triggers_json TEXT,
    FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE
);

-- Combat Cards table
CREATE TABLE IF NOT EXISTS combat_cards (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    faction TEXT NOT NULL,
    card_type TEXT,
    description TEXT,
    effects_json TEXT,
    image_small TEXT,
    image_large TEXT
);

-- Event Cards table
CREATE TABLE IF NOT EXISTS event_cards (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    faction TEXT NOT NULL,
    card_type TEXT,
    description TEXT,
    effects_json TEXT,
    image_small TEXT,
    image_large TEXT
);

-- Army Setup table
CREATE TABLE IF NOT EXISTS army_setup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nation_id INTEGER NOT NULL UNIQUE,
    total_regular INTEGER DEFAULT 0,
    total_elite INTEGER DEFAULT 0,
    total_leaders INTEGER DEFAULT 0,
    reserve_regular INTEGER DEFAULT 0,
    reserve_elite INTEGER DEFAULT 0,
    reserve_leaders INTEGER DEFAULT 0,
    FOREIGN KEY (nation_id) REFERENCES nations(id)
);

-- Deployed Units table
CREATE TABLE IF NOT EXISTS deployed_units (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    army_setup_id INTEGER NOT NULL,
    region_id TEXT NOT NULL,
    regular INTEGER DEFAULT 0,
    elite INTEGER DEFAULT 0,
    leaders INTEGER DEFAULT 0,
    owner TEXT CHECK(owner IN ('Free', 'Shadow')),
    control TEXT,
    siege_status TEXT CHECK(siege_status IN ('in', 'out')),
    FOREIGN KEY (army_setup_id) REFERENCES army_setup(id) ON DELETE CASCADE,
    FOREIGN KEY (region_id) REFERENCES regions(id)
);

-- Database version tracking
CREATE TABLE IF NOT EXISTS schema_version (
    version INTEGER PRIMARY KEY,
    applied_date TEXT NOT NULL,
    description TEXT
);

-- Insert initial version
INSERT OR IGNORE INTO schema_version (version, applied_date, description) 
VALUES (1, datetime('now'), 'Initial schema creation');
