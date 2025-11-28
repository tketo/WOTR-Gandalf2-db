package wotr;

import wotr.services.GameDataService;
import wotr.models.Region;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * List all regions from database to create proper mapping
 */
public class ListDatabaseRegions {
    
    public static void main(String[] args) {
        System.out.println("=== Database Regions ===\n");
        
        GameDataService gameData = GameDataService.getInstance();
        Collection<Region> regions = gameData.getAllRegions();
        
        // Sort by ID for easier viewing
        List<Region> sortedRegions = new ArrayList<>(regions);
        Collections.sort(sortedRegions, (a, b) -> a.getId().compareTo(b.getId()));
        
        System.out.println("Total regions: " + regions.size() + "\n");
        
        System.out.println("Region ID                    | Name");
        System.out.println("----------------------------|------------------------");
        
        for (Region region : sortedRegions) {
            System.out.printf("%-28s | %s\n", region.getId(), region.getName());
        }
        
        System.out.println("\n=== Next Step ===");
        System.out.println("Use these IDs to update RegionMapper.initializeRegionMappings()");
    }
}
