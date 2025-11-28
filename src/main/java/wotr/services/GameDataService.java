package wotr.services;

import wotr.database.DatabaseManager;
import wotr.dao.*;
import wotr.models.*;
import java.sql.SQLException;
import java.util.*;

/**
 * GameDataService - Static reference data cache
 * 
 * Caches reference data (nations, regions, characters, settlements) on startup
 * Provides fast lookup without database queries
 * 
 * This is "read-only" game data - the rulebook, not the board state
 */
public class GameDataService {
    private static GameDataService instance;
    
    // Cached reference data
    private Map<Integer, Nation> nationsById;
    private Map<String, Nation> nationsByName;
    private Map<String, Region> regionsById;
    private Map<String, wotr.models.Character> charactersById;
    private Map<String, Settlement> settlementsByRegionId;
    
    // DAOs
    private NationDAO nationDAO;
    private RegionDAO regionDAO;
    private CharacterDAO characterDAO;
    private SettlementDAO settlementDAO;
    
    private GameDataService() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        this.nationDAO = new NationDAO(dbManager.getConnection());
        this.regionDAO = new RegionDAO(dbManager.getConnection());
        this.characterDAO = new CharacterDAO(dbManager.getConnection());
        this.settlementDAO = new SettlementDAO(dbManager.getConnection());
        
        loadReferenceData();
    }
    
    public static synchronized GameDataService getInstance() {
        if (instance == null) {
            instance = new GameDataService();
        }
        return instance;
    }
    
    /**
     * Load all reference data into memory on startup
     */
    private void loadReferenceData() {
        try {
            System.out.println("Loading reference data...");
            
            // Load nations
            nationsById = new HashMap<>();
            nationsByName = new HashMap<>();
            List<Nation> nations = nationDAO.findAll();
            for (Nation nation : nations) {
                nationsById.put(nation.getId(), nation);
                nationsByName.put(nation.getName().toLowerCase(), nation);
            }
            System.out.println("  - Loaded " + nations.size() + " nations");
            
            // Load regions
            regionsById = new HashMap<>();
            List<Region> regions = regionDAO.findAll();
            for (Region region : regions) {
                regionsById.put(region.getId(), region);
            }
            System.out.println("  - Loaded " + regions.size() + " regions");
            
            // Load characters
            charactersById = new HashMap<>();
            List<wotr.models.Character> characters = characterDAO.findAll();
            for (wotr.models.Character character : characters) {
                charactersById.put(character.getId(), character);
            }
            System.out.println("  - Loaded " + characters.size() + " characters");
            
            // Load settlements
            settlementsByRegionId = new HashMap<>();
            List<Settlement> settlements = settlementDAO.findAll();
            for (Settlement settlement : settlements) {
                settlementsByRegionId.put(settlement.getRegionId(), settlement);
            }
            System.out.println("  - Loaded " + settlements.size() + " settlements");
            
            System.out.println("Reference data loaded successfully!");
            
        } catch (SQLException e) {
            System.err.println("Failed to load reference data!");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize GameDataService", e);
        }
    }
    
    // ===== NATION LOOKUPS =====
    
    public Nation getNation(int id) {
        return nationsById.get(id);
    }
    
    public Nation getNationByName(String name) {
        return nationsByName.get(name.toLowerCase());
    }
    
    public Collection<Nation> getAllNations() {
        return nationsById.values();
    }
    
    // ===== REGION LOOKUPS =====
    
    public Region getRegion(String id) {
        return regionsById.get(id);
    }
    
    public Collection<Region> getAllRegions() {
        return regionsById.values();
    }
    
    public List<Region> getRegionsByNation(int nationId) {
        List<Region> result = new ArrayList<>();
        for (Region region : regionsById.values()) {
            if (region.getNationId() != null && region.getNationId() == nationId) {
                result.add(region);
            }
        }
        return result;
    }
    
    // ===== CHARACTER LOOKUPS =====
    
    public wotr.models.Character getCharacter(String id) {
        return charactersById.get(id);
    }
    
    public Collection<wotr.models.Character> getAllCharacters() {
        return charactersById.values();
    }
    
    public List<wotr.models.Character> getCharactersByFaction(String faction) {
        List<wotr.models.Character> result = new ArrayList<>();
        for (wotr.models.Character character : charactersById.values()) {
            if (faction.equalsIgnoreCase(character.getFaction())) {
                result.add(character);
            }
        }
        return result;
    }
    
    // ===== SETTLEMENT LOOKUPS =====
    
    public Settlement getSettlement(String regionId) {
        return settlementsByRegionId.get(regionId);
    }
    
    public boolean hasSettlement(String regionId) {
        return settlementsByRegionId.containsKey(regionId);
    }
    
    public Collection<Settlement> getAllSettlements() {
        return settlementsByRegionId.values();
    }
    
    public List<Settlement> getSettlementsByType(String type) {
        List<Settlement> result = new ArrayList<>();
        for (Settlement settlement : settlementsByRegionId.values()) {
            if (type.equalsIgnoreCase(settlement.getType())) {
                result.add(settlement);
            }
        }
        return result;
    }
    
    // ===== ADJACENCY CHECKS (requires database query) =====
    
    /**
     * Check if two regions are adjacent
     * This requires a database query as adjacency is in the regions table
     */
    public boolean areRegionsAdjacent(String regionId1, String regionId2) {
        try {
            return regionDAO.areAdjacent(regionId1, regionId2);
        } catch (SQLException e) {
            System.err.println("Error checking adjacency: " + e.getMessage());
            return false;
        }
    }
    
    // ===== UTILITY METHODS =====
    
    /**
     * Get all companion character IDs that can be in Fellowship
     * Returns Free Peoples companions (excludes Ring Bearers and non-Fellowship versions)
     * 
     * Note:
     * - Strider can be in Fellowship (transforms to Aragorn later)
     * - Aragorn cannot be in Fellowship (only as leader on map)
     * - Gandalf_grey can be in Fellowship (transforms to Gandalf_white later)
     * - Gandalf_white cannot be in Fellowship (only as leader on map)
     * - Gollum is special (can be guide but not normal companion)
     */
    public List<String> getAllCompanions() {
        List<String> companions = new ArrayList<>();
        for (wotr.models.Character character : charactersById.values()) {
            String id = character.getId().toLowerCase();
            // Include FP companions that can be in Fellowship
            // Exclude: Ring Bearers, non-Fellowship versions (Aragorn, Gandalf_white), Gollum
            if (character.getFaction().equals("fp") && 
                !id.equals("frodo") && 
                !id.equals("sam") &&
                !id.equals("aragorn") &&        // Aragorn never in Fellowship (Strider transforms to him)
                !id.equals("gandalf_white") &&  // Gandalf White never in Fellowship (Grey transforms to him)
                !id.equals("gollum")) {         // Gollum is special case
                companions.add(character.getId());
            }
        }
        return companions;
    }
    
    /**
     * Reload all reference data (useful for mods/updates)
     */
    public void reload() {
        System.out.println("Reloading reference data...");
        loadReferenceData();
    }
    
    /**
     * Get cache statistics for debugging
     */
    public String getCacheStats() {
        return String.format(
            "Cache Stats:\n" +
            "  Nations: %d\n" +
            "  Regions: %d\n" +
            "  Characters: %d\n" +
            "  Settlements: %d",
            nationsById.size(),
            regionsById.size(),
            charactersById.size(),
            settlementsByRegionId.size()
        );
    }
}
