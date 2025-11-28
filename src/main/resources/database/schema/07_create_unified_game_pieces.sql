-- ============================================
-- Unified Game Pieces Table
-- Replaces army_units aggregation with 1:1 piece mapping
-- Each row = one physical piece in bits[] array
-- ============================================

-- Drop old aggregated table (keep for now during migration)
-- DROP TABLE IF EXISTS army_units;

-- Create unified game pieces table
-- piece_id: Index in bits[] array
-- game_id: Which game this piece belongs to
-- piece_type: Type of piece (see types below)
-- area_id: Current location (index in areas[])
-- nation_id: Nation (0-8), NULL for non-nation pieces
-- state_data: JSON for piece-specific state
CREATE TABLE IF NOT EXISTS game_pieces (
    piece_id INTEGER NOT NULL,
    game_id TEXT NOT NULL,
    piece_type TEXT NOT NULL,
    area_id INTEGER NOT NULL,
    nation_id INTEGER,
    state_data TEXT,
    PRIMARY KEY (game_id, piece_id),
    FOREIGN KEY (game_id) REFERENCES game_state(game_id) ON DELETE CASCADE
);

-- Performance indexes
CREATE INDEX IF NOT EXISTS idx_pieces_game ON game_pieces(game_id);
CREATE INDEX IF NOT EXISTS idx_pieces_area ON game_pieces(area_id);
CREATE INDEX IF NOT EXISTS idx_pieces_nation ON game_pieces(nation_id);
CREATE INDEX IF NOT EXISTS idx_pieces_type ON game_pieces(piece_type);
CREATE INDEX IF NOT EXISTS idx_pieces_game_area ON game_pieces(game_id, area_id);

-- ============================================
-- Piece Types Reference
-- ============================================
-- Military Units:
--   'regular'           - Regular units
--   'elite'             - Elite units  
--   'leader'            - Leader units
--   'nazgul'            - Nazgul units
--
-- Characters:
--   'character'         - Named characters (Gandalf, Aragorn, etc.)
--   'fellowship'        - Ring bearers (UnitFellowship)
--
-- Fellowship System:
--   'fellowship_counter' - TwoChit "FSP" on track (areas 131-143)
--   'corruption_counter' - Corruption chit on track (areas 131-143)
--
-- Political:
--   'nation_chit'       - Nation political track chits (areas 117-120)
--
-- Other:
--   'action_die'        - Action dice
--   'card'              - Event/character cards
--   'marker'            - Various game markers
--
-- ============================================
-- State Data JSON Examples
-- ============================================
-- Regular unit (minimal state):
--   {}
--
-- Fellowship counter:
--   {"revealed": true}
--
-- Nation chit:
--   {"active": false, "nation": "Gondor"}
--
-- Character:
--   {"exhausted": false, "wounded": false}
--
-- ============================================

-- Update schema version
INSERT OR IGNORE INTO schema_version (version, applied_date, description) 
VALUES (7, datetime('now'), 'Add unified game_pieces table');
