package wotr;

import wotr.services.GameDataService;
import wotr.models.Region;
import wotr.mapping.RegionMapper;
import java.util.*;

/**
 * Find regions in database that aren't mapped in RegionMapper
 */
public class FindMissingRegions {
    
    public static void main(String[] args) {
        System.out.println("=== Finding Missing Regions ===\n");
        
        GameDataService gameData = GameDataService.getInstance();
        RegionMapper mapper = RegionMapper.getInstance();
        
        Collection<Region> allRegions = gameData.getAllRegions();
        Set<String> mappedRegionIds = mapper.getAllRegionIds();
        
        System.out.println("Total regions in database: " + allRegions.size());
        System.out.println("Total regions mapped: " + mappedRegionIds.size());
        System.out.println("Missing: " + (allRegions.size() - mappedRegionIds.size()));
        
        System.out.println("\n=== Missing Regions ===\n");
        
        List<Region> missing = new ArrayList<>();
        for (Region region : allRegions) {
            String id = String.valueOf(region.getId());
            if (!mappedRegionIds.contains(id)) {
                missing.add(region);
            }
        }
        
        // Sort by ID
        Collections.sort(missing, new Comparator<Region>() {
            public int compare(Region a, Region b) {
                return Integer.compare(Integer.parseInt(a.getId()), Integer.parseInt(b.getId()));
            }
        });
        
        for (Region region : missing) {
            System.out.printf("Region %s: %s\n", region.getId(), region.getName());
        }
        
        System.out.println("\n=== Now Search messages.properties for These Names ===");
        System.out.println("Look for variations like:");
        System.out.println("  'Grey Havens' vs 'The Grey Havens'");
        System.out.println("  'Woodland Realms' vs 'Woodland Realm'");
        System.out.println("  Hyphenation differences");
        System.out.println("  Spacing differences");
    }
}
