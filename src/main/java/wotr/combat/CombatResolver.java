package wotr.combat;

import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.rules.CombatRules;
import wotr.rules.CombatDice;
import wotr.models.ArmyUnit;
import wotr.models.Region;
import wotr.models.Character;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * CombatResolver - Manages multi-round combat resolution
 * 
 * Based on War of the Ring rules:
 * - Combat occurs when opposing forces occupy same region
 * - Multiple rounds until one side eliminated or retreats
 * - Combat dice (max 5) with leader re-rolls
 * - Terrain modifiers (field, city, fortification, siege)
 */
public class CombatResolver {
    private GameStateService gameState;
    private GameDataService gameData;
    private CombatRules combatRules;
    private Random random;
    
    public CombatResolver(GameStateService gameState) {
        this.gameState = gameState;
        this.gameData = GameDataService.getInstance();
        this.combatRules = new CombatRules(gameState, gameData);
        this.random = new Random();
    }
    
    /**
     * Initiate combat in a region
     */
    public CombatState initiateCombat(String regionId) throws SQLException {
        // Validate combat can occur
        if (!combatRules.validateCombat(regionId).isValid()) {
            return null;
        }
        
        // Calculate initial strength for both sides
        int fpStrength = combatRules.calculateCombatStrength(regionId, "free_peoples");
        int shadowStrength = combatRules.calculateCombatStrength(regionId, "shadow");
        
        // Create combat state
        CombatState combat = new CombatState();
        combat.setRegionId(regionId);
        combat.setRound(1);
        combat.setFpStrength(fpStrength);
        combat.setShadowStrength(shadowStrength);
        combat.setAttacker(determineAttacker(regionId));
        combat.setDefender(combat.getAttacker().equals("free_peoples") ? "shadow" : "free_peoples");
        
        return combat;
    }
    
    /**
     * Determine who is attacker (who moved into the region)
     */
    private String determineAttacker(String regionId) throws SQLException {
        // For now, use region control to determine defender
        String control = gameState.getRegionControl(regionId);
        if ("free_peoples".equals(control)) {
            return "shadow"; // Shadow attacking FP region
        } else if ("shadow".equals(control)) {
            return "free_peoples"; // FP attacking Shadow region
        }
        return "shadow"; // Default
    }
    
    /**
     * Execute one round of combat with leader re-rolls
     */
    public CombatRoundResult executeCombatRound(CombatState combat) throws SQLException {
        CombatRoundResult result = new CombatRoundResult();
        result.setRound(combat.getRound());
        
        // Get terrain type for modifiers
        Region region = gameData.getRegion(combat.getRegionId());
        String terrainType = getTerrainType(region);
        boolean isFirstRound = (combat.getRound() == 1);
        
        // Determine target numbers based on terrain and round
        boolean fpIsAttacker = combat.getAttacker().equals("free_peoples");
        int fpTargetNumber = CombatDice.getTargetNumber(fpIsAttacker, terrainType, isFirstRound, false);
        int shadowTargetNumber = CombatDice.getTargetNumber(!fpIsAttacker, terrainType, isFirstRound, false);
        
        // Get leadership values for both sides
        int fpLeadership = getLeadershipValue(combat.getRegionId(), "free_peoples");
        int shadowLeadership = getLeadershipValue(combat.getRegionId(), "shadow");
        
        // Roll combat dice for both sides (max 5 dice)
        int fpDice = Math.min(5, combat.getFpStrength());
        int shadowDice = Math.min(5, combat.getShadowStrength());
        
        // Roll with leader re-rolls
        int fpRoll = rollCombatDiceWithLeader(fpDice, fpTargetNumber, fpLeadership);
        int shadowRoll = rollCombatDiceWithLeader(shadowDice, shadowTargetNumber, shadowLeadership);
        
        result.setFpDiceCount(fpDice);
        result.setFpRoll(fpRoll);
        result.setFpLeadership(fpLeadership);
        result.setShadowDiceCount(shadowDice);
        result.setShadowRoll(shadowRoll);
        result.setShadowLeadership(shadowLeadership);
        
        // Apply terrain modifiers (no longer needed as target numbers already account for terrain)
        int fpModifier = 0;
        int shadowModifier = 0;
        
        result.setFpModifier(fpModifier);
        result.setShadowModifier(shadowModifier);
        
        // Calculate hits (roll already includes terrain via target number)
        int fpHits = fpRoll + fpModifier;
        int shadowHits = shadowRoll + shadowModifier;
        
        result.setFpHits(fpHits);
        result.setShadowHits(shadowHits);
        
        // Apply casualties
        applyCasualties(combat.getRegionId(), "free_peoples", shadowHits);
        applyCasualties(combat.getRegionId(), "shadow", fpHits);
        
        // Recalculate strength
        int newFpStrength = combatRules.calculateCombatStrength(combat.getRegionId(), "free_peoples");
        int newShadowStrength = combatRules.calculateCombatStrength(combat.getRegionId(), "shadow");
        
        combat.setFpStrength(newFpStrength);
        combat.setShadowStrength(newShadowStrength);
        combat.setRound(combat.getRound() + 1);
        
        // Determine if combat continues
        if (newFpStrength == 0) {
            result.setWinner("shadow");
            result.setCombatEnded(true);
        } else if (newShadowStrength == 0) {
            result.setWinner("free_peoples");
            result.setCombatEnded(true);
        } else {
            result.setCombatEnded(false);
        }
        
        return result;
    }
    
    /**
     * Roll combat dice (d6 each, count 5s and 6s as hits)
     * @deprecated Use CombatDice.rollDice() and countHits() instead
     */
    @Deprecated
    private int rollCombatDice(int diceCount) {
        int hits = 0;
        for (int i = 0; i < diceCount; i++) {
            int roll = random.nextInt(6) + 1;
            if (roll >= 5) {
                hits++;
            }
        }
        return hits;
    }
    
    /**
     * Get leadership value for a side in combat
     * 
     * @param regionId Combat region
     * @param faction Faction ("free_peoples" or "shadow")
     * @return Total leadership value (sum of all leaders)
     */
    private int getLeadershipValue(String regionId, String faction) throws SQLException {
        // Get characters in the region
        List<String> charactersInRegion = gameState.getCharactersInRegion(regionId);
        
        int totalLeadership = 0;
        for (String characterId : charactersInRegion) {
            Character character = gameData.getCharacter(characterId);
            if (character != null && character.getFaction().equalsIgnoreCase(faction)) {
                totalLeadership += character.getLeadership();
            }
        }
        
        return totalLeadership;
    }
    
    /**
     * Roll combat dice with leader re-rolls
     * 
     * @param diceCount Number of dice to roll
     * @param targetNumber Target number for hits (5 or 6)
     * @param leadership Leadership value for re-rolls
     * @return Number of hits after re-rolls
     */
    private int rollCombatDiceWithLeader(int diceCount, int targetNumber, int leadership) {
        // Initial roll
        List<Integer> results = CombatDice.rollDice(diceCount);
        
        // Apply leader re-rolls if available
        if (leadership > 0) {
            results = CombatDice.rerollFailed(results, targetNumber, leadership);
        }
        
        // Count hits
        return CombatDice.countHits(results, targetNumber);
    }
    
    /**
     * Apply casualties to a side
     */
    private void applyCasualties(String regionId, String side, int hits) throws SQLException {
        if (hits == 0) return;
        
        // Get units in region
        List<ArmyUnit> units = gameState.getArmyUnits(regionId);
        
        // Filter by side
        int casualtiesRemaining = hits;
        for (ArmyUnit unit : units) {
            if (casualtiesRemaining == 0) break;
            
            // Check if unit belongs to this side
            // TODO: Use nation faction lookup
            boolean isThisSide = false;
            if (side.equals("free_peoples") && unit.getNationId() <= 4) {
                isThisSide = true;
            } else if (side.equals("shadow") && unit.getNationId() > 4) {
                isThisSide = true;
            }
            
            if (!isThisSide) continue;
            
            // Remove regular units first
            if ("regular".equals(unit.getUnitType())) {
                int toRemove = Math.min(casualtiesRemaining, unit.getCount());
                gameState.removeUnits(regionId, unit.getNationId(), "regular", toRemove);
                casualtiesRemaining -= toRemove;
            }
        }
        
        // Then elites if needed
        if (casualtiesRemaining > 0) {
            for (ArmyUnit unit : units) {
                if (casualtiesRemaining == 0) break;
                
                boolean isThisSide = false;
                if (side.equals("free_peoples") && unit.getNationId() <= 4) {
                    isThisSide = true;
                } else if (side.equals("shadow") && unit.getNationId() > 4) {
                    isThisSide = true;
                }
                
                if (!isThisSide) continue;
                
                if ("elite".equals(unit.getUnitType())) {
                    int toRemove = Math.min(casualtiesRemaining, unit.getCount());
                    gameState.removeUnits(regionId, unit.getNationId(), "elite", toRemove);
                    casualtiesRemaining -= toRemove;
                }
            }
        }
    }
    
    /**
     * Get terrain type for combat modifiers
     */
    private String getTerrainType(Region region) {
        if (region == null) return "field";
        
        // TODO: Add terrain type to Region model
        // For now, infer from region name
        String name = region.getName().toLowerCase();
        if (name.contains("city") || name.contains("stronghold") || 
            name.contains("tirith") || name.contains("orthanc")) {
            return "city";
        }
        return "field";
    }
    
    /**
     * Can the losing side retreat?
     */
    public boolean canRetreat(CombatState combat, String side) throws SQLException {
        // Check if there are adjacent friendly regions
        // TODO: Implement retreat validation
        return true;
    }
    
    /**
     * Execute retreat
     */
    public void executeRetreat(CombatState combat, String side, String retreatRegionId) throws SQLException {
        // Move all units from combat region to retreat region
        List<ArmyUnit> units = gameState.getArmyUnits(combat.getRegionId());
        
        for (ArmyUnit unit : units) {
            // Check if unit belongs to retreating side
            boolean isRetreating = false;
            if (side.equals("free_peoples") && unit.getNationId() <= 4) {
                isRetreating = true;
            } else if (side.equals("shadow") && unit.getNationId() > 4) {
                isRetreating = true;
            }
            
            if (isRetreating) {
                // Move units
                gameState.removeUnits(combat.getRegionId(), unit.getNationId(), unit.getUnitType(), unit.getCount());
                gameState.addUnits(retreatRegionId, unit.getNationId(), unit.getUnitType(), unit.getCount());
            }
        }
    }
}
