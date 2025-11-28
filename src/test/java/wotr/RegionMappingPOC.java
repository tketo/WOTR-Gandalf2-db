package wotr;

import wotr.mapping.RegionMapper;
import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.models.Region;
import wotr.models.ArmyUnit;
import java.util.List;

/**
 * Proof of Concept: Map Regions to Database
 * 
 * Tests that we can:
 * 1. Map Game.java area indices to database region IDs
 * 2. Query units in a region from database
 * 3. Update unit positions via database
 */
public class RegionMappingPOC {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Region Mapping POC ===\n");
            
            // Initialize services
            GameDataService gameData = GameDataService.getInstance();
            GameStateService gameState = new GameStateService();
            RegionMapper mapper = RegionMapper.getInstance();
            
            System.out.println("✓ Services initialized");
            System.out.println("  - " + mapper.getMappedRegionCount() + " regions mapped\n");
            
            // Test 1: Verify region mapping
            System.out.println("--- Test 1: Region Mapping ---");
            testRegionMapping(mapper, gameData);
            
            // Test 2: Query units from database
            System.out.println("\n--- Test 2: Query Units from Database ---");
            String gameId = gameState.createNewGame("free_peoples");
            testQueryUnits(gameState, mapper);
            
            // Test 3: Add units via database
            System.out.println("\n--- Test 3: Add Units via Database ---");
            testAddUnits(gameState, mapper);
            
            System.out.println("\n✓ POC completed successfully!");
            System.out.println("\n=== Next Steps ===");
            System.out.println("1. Complete all 104 region mappings");
            System.out.println("2. Integrate with Game.java rendering");
            System.out.println("3. Test moving units updates database");
            
        } catch (Exception e) {
            System.err.println("✗ POC failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testRegionMapping(RegionMapper mapper, GameDataService gameData) {
        // Test area 48 → minas_tirith
        int minasTirithArea = 48;
        String regionId = mapper.getRegionId(minasTirithArea);
        
        System.out.println("Area " + minasTirithArea + " maps to region: " + regionId);
        
        // Verify region exists in database
        Region region = gameData.getRegion(regionId);
        if (region != null) {
            System.out.println("✓ Region found in database: " + region.getName());
        } else {
            System.out.println("✗ Region NOT found in database");
        }
        
        // Test reverse mapping
        int areaIndex = mapper.getAreaIndex("minas_tirith");
        System.out.println("Region 'minas_tirith' maps to area: " + areaIndex);
        
        // Show all mapped regions
        System.out.println("\nMapped regions:");
        for (String regId : mapper.getAllRegionIds()) {
            int area = mapper.getAreaIndex(regId);
            Region r = gameData.getRegion(regId);
            String name = (r != null) ? r.getName() : "NOT_FOUND";
            System.out.println("  Area " + area + " → " + regId + " (" + name + ")");
        }
    }
    
    private static void testQueryUnits(GameStateService gameState, RegionMapper mapper) 
            throws Exception {
        // Query units in Minas Tirith from database
        String regionId = mapper.getRegionId(48);
        System.out.println("Querying units in " + regionId + "...");
        
        List<ArmyUnit> units = gameState.getArmyUnits(regionId);
        System.out.println("Found " + units.size() + " units");
        
        for (ArmyUnit unit : units) {
            System.out.println("  - Nation " + unit.getNationId() + 
                             ": " + unit.getCount() + " " + unit.getUnitType());
        }
        
        if (units.isEmpty()) {
            System.out.println("  (No units in this region yet)");
        }
    }
    
    private static void testAddUnits(GameStateService gameState, RegionMapper mapper) 
            throws Exception {
        // Add 3 Gondor regulars to Minas Tirith via database
        String regionId = mapper.getRegionId(48);
        int gondorId = 3; // From database
        
        System.out.println("Adding 3 Gondor regulars to " + regionId + "...");
        gameState.addUnits(regionId, gondorId, "regular", 3);
        
        // Query back to verify
        List<ArmyUnit> units = gameState.getArmyUnits(regionId);
        System.out.println("Units now in " + regionId + ": " + units.size());
        
        for (ArmyUnit unit : units) {
            System.out.println("  ✓ Nation " + unit.getNationId() + 
                             ": " + unit.getCount() + " " + unit.getUnitType());
        }
        
        System.out.println("\n=== Database State ===");
        System.out.println("Units are now persisted in army_units table");
        System.out.println("Game.java can query this anytime to render");
    }
}
