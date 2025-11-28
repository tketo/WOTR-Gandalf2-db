package wotr.rules;

import wotr.services.*;
import wotr.models.*;
import java.sql.SQLException;

/**
 * VictoryRules - Check victory conditions
 * 
 * Victory can occur through:
 * 1. Ring destroyed (FP wins)
 * 2. Ring claimed by Shadow (Shadow wins)
 * 3. Military victory (capturing key cities)
 * 4. Victory points (political track)
 */
public class VictoryRules {
    private GameStateService gameState;
    private GameDataService gameData;
    
    public VictoryRules(GameStateService gameState, GameDataService gameData) {
        this.gameState = gameState;
        this.gameData = gameData;
    }
    
    /**
     * Check all victory conditions
     */
    public VictoryResult checkVictory() throws SQLException {
        // Check ring victory
        VictoryResult ringVictory = checkRingVictory();
        if (ringVictory.isGameOver()) {
            return ringVictory;
        }
        
        // Check military victory
        VictoryResult militaryVictory = checkMilitaryVictory();
        if (militaryVictory.isGameOver()) {
            return militaryVictory;
        }
        
        // Check victory points
        try {
            if (checkVictoryPoints()) {
                GameState state = gameState.getGameState();
                if (state.getVictoryPointsShadow() >= 10) {
                    return VictoryResult.shadowVictory("political", 
                        "Shadow has " + state.getVictoryPointsShadow() + " victory points");
                }
                if (state.getVictoryPointsFP() >= 4) {
                    return VictoryResult.freePeoplesVictory("political", 
                        "Free Peoples has " + state.getVictoryPointsFP() + " victory points");
                }
            }
        } catch (IllegalStateException e) {
            // No game loaded, no victory
        }
        
        return VictoryResult.noVictory();
    }
    
    /**
     * Check ring-related victory conditions
     */
    public VictoryResult checkRingVictory() throws SQLException {
        // TODO: Implement getFellowshipState() in GameStateService
        return VictoryResult.noVictory();
        /* TEMPORARILY COMMENTED OUT - Missing getFellowshipState() method
        FellowshipState fellowship = null;
        try {
            fellowship = gameState.getFellowshipState();
        } catch (IllegalStateException e) {
            // No game loaded
            return VictoryResult.noVictory();
        }
        
        if (fellowship == null) {
            return VictoryResult.noVictory();
        }
        
        // Check if Ring reached Mount Doom
        if ("mount_doom".equals(fellowship.getRegionId())) {
            // FP wins if corruption is low enough to destroy ring
            if (fellowship.getCorruptionLevel() <= 11) {
                return VictoryResult.freePeoplesVictory("ring_destroyed", 
                    "The Ring has been destroyed in Mount Doom!");
            }
        }
        
        // Check if Ring bearer fully corrupted
        if (fellowship.getCorruptionLevel() >= 12) {
            return VictoryResult.shadowVictory("corruption", 
                "The Ring bearer has been corrupted!");
        }
        
        // TODO: Check if Ring bearer was killed by combat/hunt
        
        return VictoryResult.noVictory();
        */
    }
    
    /**
     * Check military victory conditions
     */
    public VictoryResult checkMilitaryVictory() throws SQLException {
        // Military victory: capture 4 Free Peoples strongholds (for Shadow)
        // or 4 Shadow strongholds (for FP)
        
        // TODO: Implement by querying settlements and region control
        // For now, return no victory
        
        return VictoryResult.noVictory();
    }
    
    /**
     * Check if victory points trigger a win
     */
    public boolean checkVictoryPoints() throws SQLException {
        GameState state = gameState.getGameState();
        return state.getVictoryPointsShadow() >= 10 || state.getVictoryPointsFP() >= 4;
    }
    
    /**
     * Calculate current victory points for a side
     */
    public int getVictoryPoints(String side) throws SQLException {
        GameState state = gameState.getGameState();
        if ("free_peoples".equals(side)) {
            return state.getVictoryPointsFP();
        } else if ("shadow".equals(side)) {
            return state.getVictoryPointsShadow();
        }
        return 0;
    }
    
    /**
     * Award victory points
     */
    public void awardVictoryPoints(String side, int points) throws SQLException {
        // TODO: Implement update to game_state table
        System.out.println("Awarding " + points + " VP to " + side);
    }
    
    /**
     * Get summary of victory conditions for UI
     */
    public String getVictorySummary() throws SQLException {
        // TODO: Implement getFellowshipState() in GameStateService
        return "Victory tracking not yet implemented";
        /* TEMPORARILY COMMENTED OUT
        GameState state = gameState.getGameState();
        FellowshipState fellowship = gameState.getFellowshipState();
        
        return String.format(
            "Victory Status:\n" +
            "  FP Victory Points: %d/4\n" +
            "  Shadow Victory Points: %d/10\n" +
            "  Ring Corruption: %d/12\n" +
            "  Fellowship Location: %s",
            state.getVictoryPointsFP(),
            state.getVictoryPointsShadow(),
            fellowship != null ? fellowship.getCorruptionLevel() : 0,
            fellowship != null ? fellowship.getRegionId() : "unknown"
        );
        */
    }
}
