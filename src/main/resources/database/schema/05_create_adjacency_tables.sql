-- Region Adjacency Tables
-- Defines which regions are adjacent for movement validation

-- Region adjacency mapping (bidirectional)
-- terrain_type options: 'normal', 'mountain', 'river', 'ford'
-- Ensures no duplicate adjacencies via UNIQUE constraint
CREATE TABLE IF NOT EXISTS region_adjacency (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    region_a TEXT NOT NULL,
    region_b TEXT NOT NULL,
    terrain_type TEXT,
    movement_cost INTEGER DEFAULT 1,
    FOREIGN KEY (region_a) REFERENCES regions(id),
    FOREIGN KEY (region_b) REFERENCES regions(id),
    UNIQUE(region_a, region_b)
);

-- Create index for fast adjacency lookups
CREATE INDEX IF NOT EXISTS idx_adjacency_region_a ON region_adjacency(region_a);
CREATE INDEX IF NOT EXISTS idx_adjacency_region_b ON region_adjacency(region_b);

-- Helper view for bidirectional queries
CREATE VIEW IF NOT EXISTS v_region_adjacency AS
SELECT region_a, region_b, terrain_type, movement_cost FROM region_adjacency
UNION
SELECT region_b AS region_a, region_a AS region_b, terrain_type, movement_cost FROM region_adjacency;

-- Note: Adjacency data will be imported from metadata.json using AdjacencyImporter tool

-- Schema version update
INSERT INTO schema_version (version, applied_date, description)
VALUES (2, datetime('now'), 'Add region adjacency tables');
