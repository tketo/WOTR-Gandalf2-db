package wotr;

import wotr.services.GameDataService;
import wotr.models.Region;
import java.util.*;

/**
 * Check which region IDs (1-105) are in the database
 */
public class CheckRegionIds {
    
    public static void main(String[] args) {
        System.out.println("=== Checking Region IDs 1-105 ===\n");
        
        GameDataService gameData = GameDataService.getInstance();
        Collection<Region> regions = gameData.getAllRegions();
        
        // Get all IDs
        Set<Integer> existingIds = new HashSet<>();
        for (Region region : regions) {
            existingIds.add(Integer.parseInt(region.getId()));
        }
        
        System.out.println("Total regions in database: " + regions.size());
        System.out.println("Expected (IDs 1-105): 105 regions");
        
        // Check for missing IDs
        System.out.println("\n=== Missing Region IDs ===");
        List<Integer> missing = new ArrayList<>();
        for (int i = 1; i <= 105; i++) {
            if (!existingIds.contains(i)) {
                missing.add(i);
            }
        }
        
        if (missing.isEmpty()) {
            System.out.println("No missing IDs - all 105 regions present!");
        } else {
            System.out.println("Missing " + missing.size() + " region ID(s):");
            for (int id : missing) {
                System.out.println("  Region ID: " + id);
            }
        }
        
        // Check if region 0 exists
        if (existingIds.contains(0)) {
            System.out.println("\n✓ Region ID 0 exists in database");
        } else {
            System.out.println("\n✗ Region ID 0 does NOT exist in database");
        }
        
        // Show ID range
        int min = Collections.min(existingIds);
        int max = Collections.max(existingIds);
        System.out.println("\nRegion ID range: " + min + " - " + max);
    }
}
