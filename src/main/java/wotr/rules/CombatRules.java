package wotr.rules;

import wotr.services.*;
import wotr.models.*;
import java.sql.SQLException;
import java.util.List;

/**
 * CombatRules - Validate and resolve combat
 * 
 * Combat occurs when opposing armies are in the same region.
 * Rules query database for:
 * - What units are in the region?
 * - What characters (leaders) are present?
 * - What is the combat strength?
 */
public class CombatRules {
    private GameStateService gameState;
    private GameDataService gameData;
    
    public CombatRules(GameStateService gameState, GameDataService gameData) {
        this.gameState = gameState;
        this.gameData = gameData;
    }
    
    /**
     * Can combat occur in this region?
     * 
     * Combat requires opposing forces in the same region
     */
    public ValidationResult validateCombat(String regionId) throws SQLException {
        Region region = gameData.getRegion(regionId);
        if (region == null) {
            return ValidationResult.failure("Region not found: " + regionId);
        }
        
        // Check if there are armies from different sides
        List<ArmyUnit> units = gameState.getArmyUnits(regionId);
        if (units.isEmpty()) {
            return ValidationResult.failure("No armies in " + region.getName());
        }
        
        boolean hasFreePeoples = false;
        boolean hasShadow = false;
        
        for (ArmyUnit unit : units) {
            Nation nation = gameData.getNation(unit.getNationId());
            if (nation == null) continue;
            
            // TODO: Get nation faction from database
            // For now, assume nations 1-4 are Free Peoples, 5-9 are Shadow
            if (unit.getNationId() <= 4) {
                hasFreePeoples = true;
            } else {
                hasShadow = true;
            }
        }
        
        if (!hasFreePeoples || !hasShadow) {
            return ValidationResult.failure("No opposing forces in " + region.getName());
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Calculate combat strength for a side in a region
     * 
     * Strength = base units + leaders + bonuses
     */
    public int calculateCombatStrength(String regionId, String side) throws SQLException {
        int strength = 0;
        
        // Get all units in region
        List<ArmyUnit> units = gameState.getArmyUnits(regionId);
        
        for (ArmyUnit unit : units) {
            Nation nation = gameData.getNation(unit.getNationId());
            if (nation == null) continue;
            
            // TODO: Get nation faction from database
            // For now, assume nations 1-4 are Free Peoples, 5-9 are Shadow
            boolean isFreePeoples = unit.getNationId() <= 4;
            String unitSide = isFreePeoples ? "free_peoples" : "shadow";
            
            if (!unitSide.equals(side)) {
                continue; // Not this side's units
            }
            
            // Add unit strength
            if ("regular".equals(unit.getUnitType())) {
                strength += unit.getCount(); // 1 strength per regular
            } else if ("elite".equals(unit.getUnitType())) {
                strength += unit.getCount() * 2; // 2 strength per elite
            } else if ("leader".equals(unit.getUnitType())) {
                strength += unit.getCount(); // Leaders add 1 base strength
            }
        }
        
        // Get characters (leaders) in region
        List<String> characters = gameState.getCharactersInRegion(regionId);
        for (String characterId : characters) {
            wotr.models.Character character = gameData.getCharacter(characterId);
            if (character == null) continue;
            
            String characterFaction = character.getFaction().toLowerCase().replace(" ", "_");
            if (characterFaction.equals(side)) {
                // Add leadership value
                strength += character.getLeadership();
            }
        }
        
        return strength;
    }
    
    /**
     * Resolve combat (simplified)
     * 
     * Returns the winning side based on combat strength
     */
    public String resolveCombat(String regionId) throws SQLException {
        int fpStrength = calculateCombatStrength(regionId, "free_peoples");
        int shadowStrength = calculateCombatStrength(regionId, "shadow");
        
        if (fpStrength > shadowStrength) {
            return "free_peoples";
        } else if (shadowStrength > fpStrength) {
            return "shadow";
        } else {
            return "tie"; // Defender wins on tie
        }
    }
    
    /**
     * Get combat summary for display
     */
    public String getCombatSummary(String regionId) throws SQLException {
        Region region = gameData.getRegion(regionId);
        int fpStrength = calculateCombatStrength(regionId, "free_peoples");
        int shadowStrength = calculateCombatStrength(regionId, "shadow");
        
        return String.format("Combat in %s:\n  Free Peoples: %d strength\n  Shadow: %d strength",
            region != null ? region.getName() : regionId,
            fpStrength,
            shadowStrength
        );
    }
}
