package wotr;

import wotr.services.GameDataService;
import wotr.models.Region;
import java.io.*;
import java.util.*;

/**
 * Generate complete region mappings by parsing messages.properties
 * and matching to database regions
 * 
 * Reads Game.java areaInit() and messages.properties to create
 * the complete mapping of area indices to database region IDs
 */
public class GenerateRegionMappings {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Generate Region Mappings ===\n");
            
            // Load database regions
            GameDataService gameData = GameDataService.getInstance();
            Collection<Region> regions = gameData.getAllRegions();
            
            // Create name → ID lookup
            Map<String, String> regionNameToId = new HashMap<>();
            for (Region region : regions) {
                String normalizedName = normalizeName(region.getName());
                regionNameToId.put(normalizedName, String.valueOf(region.getId()));
                System.out.println("DB Region: " + normalizedName + " → " + region.getId());
            }
            
            System.out.println("\n=== Parsing messages.properties ===\n");
            
            // Parse messages.properties
            Map<String, String> messageKeys = parseMessages("messages.properties");
            
            System.out.println("Found " + messageKeys.size() + " message keys\n");
            
            // Look for Game.18XX keys (map regions are typically Game.1883 - Game.2000+)
            System.out.println("=== Region Message Keys ===\n");
            
            for (Map.Entry<String, String> entry : messageKeys.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Focus on Game.18XX to Game.20XX range (map regions)
                if (key.startsWith("Game.") && isInRange(key, 1883, 2050)) {
                    String normalizedValue = normalizeName(value);
                    
                    // Try to match to database region
                    String regionId = regionNameToId.get(normalizedValue);
                    
                    if (regionId != null) {
                        System.out.printf("%-20s | %-30s | Region ID: %s\n", 
                                        key, value, regionId);
                    } else {
                        // Might be a special area (not a map region)
                        System.out.printf("%-20s | %-30s | NOT_A_REGION\n", key, value);
                    }
                }
            }
            
            System.out.println("\n=== Next: Match to Area Indices ===");
            System.out.println("Look at Game.java areaInit() to see which Game.XXXX maps to which area[]");
            System.out.println("Example: areas[7] = new Area(Messages.getKeyString(\"Game.1890\"));");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Parse messages.properties file
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
                
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse key=value
                int equals = line.indexOf('=');
                if (equals > 0) {
                    String key = line.substring(0, equals).trim();
                    String value = line.substring(equals + 1).trim();
                    messages.put(key, value);
                }
            }
        }
        
        return messages;
    }
    
    /**
     * Normalize region name for matching
     */
    private static String normalizeName(String name) {
        return name.toLowerCase()
                  .replace(" ", "_")
                  .replace("'", "")
                  .replace("-", "_");
    }
    
    /**
     * Check if Game.XXXX key is in range
     */
    private static boolean isInRange(String key, int min, int max) {
        try {
            String numStr = key.substring(5); // Remove "Game."
            int num = Integer.parseInt(numStr);
            return num >= min && num <= max;
        } catch (Exception e) {
            return false;
        }
    }
}
