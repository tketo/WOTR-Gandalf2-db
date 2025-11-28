-- ============================================
-- Performance Indexes
-- ============================================

-- Regions indexes
CREATE INDEX IF NOT EXISTS idx_regions_nation 
ON regions(nation_id);

-- Settlements indexes
CREATE INDEX IF NOT EXISTS idx_settlements_region 
ON settlements(region_id);

-- Character indexes
CREATE INDEX IF NOT EXISTS idx_character_abilities_char 
ON character_abilities(character_id);

CREATE INDEX IF NOT EXISTS idx_character_effects_char 
ON character_effects(character_id);

CREATE INDEX IF NOT EXISTS idx_characters_faction 
ON characters(faction);

CREATE INDEX IF NOT EXISTS idx_characters_type 
ON characters(type);

-- Card indexes
CREATE INDEX IF NOT EXISTS idx_combat_cards_faction 
ON combat_cards(faction);

CREATE INDEX IF NOT EXISTS idx_event_cards_faction 
ON event_cards(faction);

-- Army indexes
CREATE INDEX IF NOT EXISTS idx_army_setup_nation 
ON army_setup(nation_id);

CREATE INDEX IF NOT EXISTS idx_deployed_units_region 
ON deployed_units(region_id);

CREATE INDEX IF NOT EXISTS idx_deployed_units_army 
ON deployed_units(army_setup_id);
