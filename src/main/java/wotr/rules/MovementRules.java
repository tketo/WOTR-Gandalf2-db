package wotr.rules;

import wotr.services.*;
import wotr.models.*;
import java.sql.SQLException;
import java.util.List;

/**
 * MovementRules - Validate character and army movement
 * 
 * Rules check current board state via database queries:
 * - Is the character in play?
 * - Are regions adjacent?
 * - Is the path blocked by enemies?
 * - Does the army have enough units?
 */
public class MovementRules {
    private GameStateService gameState;
    private GameDataService gameData;
    
    public MovementRules(GameStateService gameState, GameDataService gameData) {
        this.gameState = gameState;
        this.gameData = gameData;
    }
    
    /**
     * Validate character movement
     * 
     * Example: Can Strider move to Minas Tirith?
     * 1. Query: Where is Strider? (database)
     * 2. Query: Is that adjacent to Minas Tirith? (database)
     * 3. Check: Is Strider in play? (database)
     */
    public ValidationResult validateCharacterMove(String characterId, String toRegionId) throws SQLException {
        // Get character info
        wotr.models.Character character = gameData.getCharacter(characterId);
        if (character == null) {
            return ValidationResult.failure("Character not found: " + characterId);
        }
        
        // Check if character is in play
        if (!gameState.isCharacterInPlay(characterId)) {
            return ValidationResult.failure(character.getName() + " is not in play");
        }
        
        // Get current location
        String fromRegionId = gameState.getCharacterLocation(characterId);
        if (fromRegionId == null) {
            return ValidationResult.failure(character.getName() + " has no location");
        }
        
        // Check if already at destination
        if (fromRegionId.equals(toRegionId)) {
            return ValidationResult.failure(character.getName() + " is already at " + toRegionId);
        }
        
        // Check adjacency
        if (!gameData.areRegionsAdjacent(fromRegionId, toRegionId)) {
            Region fromRegion = gameData.getRegion(fromRegionId);
            Region toRegion = gameData.getRegion(toRegionId);
            return ValidationResult.failure(
                String.format("%s and %s are not adjacent", 
                    fromRegion.getName(), toRegion.getName())
            );
        }
        
        // Check if destination is enemy-controlled (simple check for now)
        String destControl = gameState.getRegionControl(toRegionId);
        String characterFaction = character.getFaction().toLowerCase().replace(" ", "_");
        
        // For now, allow movement anywhere (full rules would check enemy control)
        // TODO: Implement full movement restrictions based on faction and control
        
        return ValidationResult.success();
    }
    
    /**
     * Validate army movement
     * 
     * Checks:
     * - Regions are adjacent
     * - Army has enough units
     * - Destination allows army entry
     */
    public ValidationResult validateArmyMove(String fromRegionId, String toRegionId, 
                                             int nationId, int regularCount, int eliteCount) 
                                             throws SQLException {
        // Check regions exist
        Region fromRegion = gameData.getRegion(fromRegionId);
        Region toRegion = gameData.getRegion(toRegionId);
        
        if (fromRegion == null) {
            return ValidationResult.failure("Source region not found: " + fromRegionId);
        }
        if (toRegion == null) {
            return ValidationResult.failure("Destination region not found: " + toRegionId);
        }
        
        // Check adjacency
        if (!gameData.areRegionsAdjacent(fromRegionId, toRegionId)) {
            return ValidationResult.failure(
                String.format("%s and %s are not adjacent", 
                    fromRegion.getName(), toRegion.getName())
            );
        }
        
        // Check if army exists and has enough units
        List<ArmyUnit> units = gameState.getArmyUnits(fromRegionId);
        int availableRegular = 0;
        int availableElite = 0;
        
        for (ArmyUnit unit : units) {
            if (unit.getNationId() == nationId) {
                if ("regular".equals(unit.getUnitType())) {
                    availableRegular += unit.getCount();
                } else if ("elite".equals(unit.getUnitType())) {
                    availableElite += unit.getCount();
                }
            }
        }
        
        if (regularCount > availableRegular) {
            return ValidationResult.failure(
                String.format("Not enough regular units: requested %d, available %d", 
                    regularCount, availableRegular)
            );
        }
        
        if (eliteCount > availableElite) {
            return ValidationResult.failure(
                String.format("Not enough elite units: requested %d, available %d", 
                    eliteCount, availableElite)
            );
        }
        
        // TODO: Check if movement would split army illegally
        // TODO: Check if destination allows army entry (enemy territory, etc.)
        
        return ValidationResult.success();
    }
    
    /**
     * Check if a region can be moved into
     */
    public boolean canMoveInto(String regionId, String faction) throws SQLException {
        String control = gameState.getRegionControl(regionId);
        
        // For now, simple rule: can always move into your own or neutral territory
        // TODO: Implement full rules for enemy territory
        return !isEnemyControlled(control, faction);
    }
    
    /**
     * Check if a region is enemy-controlled
     */
    private boolean isEnemyControlled(String control, String faction) {
        if (control == null || "neutral".equals(control)) {
            return false;
        }
        
        if ("free_peoples".equals(faction)) {
            return "shadow".equals(control);
        } else if ("shadow".equals(faction)) {
            return "free_peoples".equals(control);
        }
        
        return false;
    }
}
