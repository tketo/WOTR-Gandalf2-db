package wotr;

import wotr.services.GameDataService;
import wotr.models.Region;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Parse Game.java areaInit() + messages.properties + database regions
 * to generate the complete Area → Region ID mapping
 */
public class CompleteRegionMapping {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Complete Region Mapping Generator ===\n");
            
            // 1. Load database regions
            Map<String, String> regionNameToId = loadDatabaseRegions();
            
            // 2. Load messages.properties
            Map<String, String> messages = parseMessages("messages.properties");
            
            // 3. Parse Game.java areaInit()
            Map<Integer, String> areaToMessageKey = parseAreaInit("src/main/java/wotr/Game.java");
            
            // 4. Generate complete mappings
            System.out.println("\n=== COMPLETE MAPPINGS FOR RegionMapper.java ===\n");
            System.out.println("// Copy this into RegionMapper.initializeRegionMappings():\n");
            
            int mappedCount = 0;
            for (Map.Entry<Integer, String> entry : areaToMessageKey.entrySet()) {
                int areaIndex = entry.getKey();
                String messageKey = entry.getValue();
                
                String regionName = messages.get(messageKey);
                if (regionName != null) {
                    String normalizedName = normalizeName(regionName);
                    String regionId = regionNameToId.get(normalizedName);
                    
                    if (regionId != null) {
                        System.out.printf("mapRegion(%d, \"%s\");  // %s\n", 
                                        areaIndex, regionId, regionName);
                        mappedCount++;
                    } else {
                        // Not a map region - probably a special area
                        System.out.printf("// Area %d: %s (NOT_A_MAP_REGION)\n", 
                                        areaIndex, regionName);
                    }
                }
            }
            
            System.out.println("\n// Total map regions mapped: " + mappedCount);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load database regions into name → ID map
     */
    private static Map<String, String> loadDatabaseRegions() {
        GameDataService gameData = GameDataService.getInstance();
        Collection<Region> regions = gameData.getAllRegions();
        
        Map<String, String> map = new HashMap<>();
        for (Region region : regions) {
            String normalizedName = normalizeName(region.getName());
            map.put(normalizedName, String.valueOf(region.getId()));
        }
        
        System.out.println("Loaded " + regions.size() + " database regions");
        return map;
    }
    
    /**
     * Parse messages.properties
     */
    private static Map<String, String> parseMessages(String filename) throws IOException {
        Map<String, String> messages = new HashMap<>();
        
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return messages;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                int equals = line.indexOf('=');
                if (equals > 0) {
                    String key = line.substring(0, equals).trim();
                    String value = line.substring(equals + 1).trim();
                    messages.put(key, value);
                }
            }
        }
        
        System.out.println("Loaded " + messages.size() + " message keys");
        return messages;
    }
    
    /**
     * Parse Game.java areaInit() to extract area → message key mappings
     */
    private static Map<Integer, String> parseAreaInit(String filename) throws IOException {
        Map<Integer, String> map = new TreeMap<>();  // TreeMap for sorted output
        
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return map;
        }
        
        // Pattern: areas[NUMBER] = new Area(Messages.getKeyString("Game.XXXX")
        Pattern pattern = Pattern.compile("areas\\[(\\d+)\\]\\s*=\\s*new\\s+Area\\(Messages\\.(?:getKeyString|getString)\\(\"(Game\\.\\d+)\"\\)");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int areaIndex = Integer.parseInt(matcher.group(1));
                    String messageKey = matcher.group(2);
                    map.put(areaIndex, messageKey);
                }
            }
        }
        
        System.out.println("Parsed " + map.size() + " area mappings from Game.java");
        return map;
    }
    
    /**
     * Normalize region name for matching
     */
    private static String normalizeName(String name) {
        return name.toLowerCase()
                  .replace(" ", "_")
                  .replace("'", "")
                  .replace("-", "_")
                  .replace("(", "")
                  .replace(")", "");
    }
}
