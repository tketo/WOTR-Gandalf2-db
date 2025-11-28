package wotr.mapping;

import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.models.*;
import java.sql.SQLException;
import java.util.*;

/**
 * GameBoardQuery - Bridge between Game.java and database services
 * 
 * Provides methods for Game.java to query game state from database
 * Maps between area indices (Game.java) and region IDs (database)
 * 
 * This allows Game.java to become database-driven without massive refactoring
 */
public class GameBoardQuery {
    private static GameBoardQuery instance;
    
    private RegionMapper mapper;
    private GameStateService gameState;
    private GameDataService gameData;
    
    private GameBoardQuery() {
        this.mapper = RegionMapper.getInstance();
        this.gameData = GameDataService.getInstance();
    }
    
    public static synchronized GameBoardQuery getInstance() {
        if (instance == null) {
            instance = new GameBoardQuery();
        }
        return instance;
    }
    
    /**
     * Set the active game state service
     * Call this after creating/loading a game
     */
    public void setGameState(GameStateService gameState) {
        this.gameState = gameState;
    }
    
    /**
     * Check if an area is a map region
     */
    public boolean isMapRegion(int areaIndex) {
        return mapper.isMapRegion(areaIndex);
    }
    
    /**
     * Get database region ID for an area
     */
    public String getRegionId(int areaIndex) {
        return mapper.getRegionId(areaIndex);
    }
    
    /**
     * Get area index for a region ID
     */
    public int getAreaIndex(String regionId) {
        return mapper.getAreaIndex(regionId);
    }
    
    /**
     * Get all army units in an area
     * Returns empty list if not a map region or no game loaded
     */
    public List<ArmyUnit> getUnitsInArea(int areaIndex) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return new ArrayList<>();
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            return gameState.getArmyUnits(regionId);
        } catch (SQLException e) {
            System.err.println("Error querying units in area " + areaIndex + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get all characters in an area
     * Returns empty list if not a map region or no game loaded
     */
    public List<String> getCharactersInArea(int areaIndex) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return new ArrayList<>();
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            return gameState.getCharactersInRegion(regionId);
        } catch (SQLException e) {
            System.err.println("Error querying characters in area " + areaIndex + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get region control for an area
     * Returns "neutral" if not a map region or no game loaded
     */
    public String getRegionControl(int areaIndex) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return "neutral";
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            return gameState.getRegionControl(regionId);
        } catch (SQLException e) {
            System.err.println("Error querying control in area " + areaIndex + ": " + e.getMessage());
            return "neutral";
        }
    }
    
    /**
     * Add units to an area
     * Does nothing if not a map region or no game loaded
     */
    public void addUnitsToArea(int areaIndex, int nationId, String unitType, int count) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return;
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            gameState.addUnits(regionId, nationId, unitType, count);
        } catch (SQLException e) {
            System.err.println("Error adding units to area " + areaIndex + ": " + e.getMessage());
        }
    }
    
    /**
     * Remove units from an area
     * Does nothing if not a map region or no game loaded
     */
    public void removeUnitsFromArea(int areaIndex, int nationId, String unitType, int count) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return;
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            gameState.removeUnits(regionId, nationId, unitType, count);
        } catch (SQLException e) {
            System.err.println("Error removing units from area " + areaIndex + ": " + e.getMessage());
        }
    }
    
    /**
     * Move character to an area
     * Does nothing if not a map region or no game loaded
     */
    public void moveCharacterToArea(String characterId, int areaIndex) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return;
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            gameState.moveCharacter(characterId, regionId);
        } catch (SQLException e) {
            System.err.println("Error moving character to area " + areaIndex + ": " + e.getMessage());
        }
    }
    
    /**
     * Set region control for an area
     * Does nothing if not a map region or no game loaded
     */
    public void setRegionControl(int areaIndex, String controller) {
        if (gameState == null || !mapper.isMapRegion(areaIndex)) {
            return;
        }
        
        try {
            String regionId = mapper.getRegionId(areaIndex);
            gameState.setRegionControl(regionId, controller);
        } catch (SQLException e) {
            System.err.println("Error setting control for area " + areaIndex + ": " + e.getMessage());
        }
    }
    
    /**
     * Get region name from area index
     */
    public String getRegionName(int areaIndex) {
        if (!mapper.isMapRegion(areaIndex)) {
            return null;
        }
        
        String regionId = mapper.getRegionId(areaIndex);
        Region region = gameData.getRegion(regionId);
        return region != null ? region.getName() : null;
    }
    
    /**
     * Get statistics for debugging
     */
    public String getStats() {
        return String.format(
            "GameBoardQuery Stats:\n" +
            "  Mapped regions: %d\n" +
            "  Game loaded: %s",
            mapper.getMappedRegionCount(),
            gameState != null ? "yes" : "no"
        );
    }
}
