-- Add faction information to nations table
-- Nations belong to either Free Peoples or Shadow

-- Add faction column to nations
ALTER TABLE nations ADD COLUMN faction TEXT CHECK(faction IN ('free_peoples', 'shadow'));

-- Update existing nations with their factions
-- Free Peoples nations
UPDATE nations SET faction = 'free_peoples' WHERE id IN (1, 2, 3, 4);

-- Shadow nations  
UPDATE nations SET faction = 'shadow' WHERE id IN (5, 6, 7, 8, 9);

-- If we have specific nations, update by name
-- Free Peoples
UPDATE nations SET faction = 'free_peoples' WHERE name IN ('Gondor', 'Rohan', 'Elves', 'Dwarves', 'North');

-- Shadow
UPDATE nations SET faction = 'shadow' WHERE name IN ('Isengard', 'Sauron', 'Southrons', 'Easterlings');

-- Schema version
INSERT INTO schema_version (version, applied_date, description)
VALUES (3, datetime('now'), 'Add nation factions');
