-- Scenario System Tables
-- Defines different game scenarios (wotr, base, gondor, etc.) in database

-- Piece setup table for V008/V009/V010 migrations
-- This stores the complete piece setup for each scenario
-- piece_id = bits[] array index (explicit positioning)
-- initial_area_id = areas[] array index (explicit location)
-- Java class names: UnitGondorRegular, FreeStrategyCard, etc.
-- Card fields (small_image, big_image, card_name, etc.) are NULL for non-cards
-- Unit fields (nation, unit_type) are NULL for non-units
CREATE TABLE IF NOT EXISTS scenario_setup (
    piece_id INTEGER NOT NULL,
    scenario_id TEXT NOT NULL,
    piece_class TEXT NOT NULL,
    initial_area_id INTEGER NOT NULL,
    initial_area_name TEXT,
    faction TEXT,
    small_image TEXT,
    big_image TEXT,
    card_name TEXT,
    small_back_image TEXT,
    big_back_image TEXT,
    card_type TEXT,
    nation TEXT,
    unit_type TEXT,
    PRIMARY KEY (piece_id, scenario_id)
);

CREATE INDEX IF NOT EXISTS idx_scenario_setup_scenario ON scenario_setup(scenario_id);
CREATE INDEX IF NOT EXISTS idx_scenario_setup_area ON scenario_setup(initial_area_id);
CREATE INDEX IF NOT EXISTS idx_scenario_setup_piece ON scenario_setup(piece_id);