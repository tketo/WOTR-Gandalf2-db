package wotr.rules;

/**
 * VictoryResult - Result of victory condition check
 * 
 * Indicates if the game is over and who won
 */
public class VictoryResult {
    private final boolean gameOver;
    private final String winner;
    private final String victoryType;
    private final String description;
    
    private VictoryResult(boolean gameOver, String winner, String victoryType, String description) {
        this.gameOver = gameOver;
        this.winner = winner;
        this.victoryType = victoryType;
        this.description = description;
    }
    
    /**
     * Game continues - no victory
     */
    public static VictoryResult noVictory() {
        return new VictoryResult(false, null, null, "Game continues");
    }
    
    /**
     * Free Peoples victory
     */
    public static VictoryResult freePeoplesVictory(String victoryType, String description) {
        return new VictoryResult(true, "free_peoples", victoryType, description);
    }
    
    /**
     * Shadow victory
     */
    public static VictoryResult shadowVictory(String victoryType, String description) {
        return new VictoryResult(true, "shadow", victoryType, description);
    }
    
    /**
     * Is the game over?
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Who won? ("free_peoples" or "shadow", null if no victory)
     */
    public String getWinner() {
        return winner;
    }
    
    /**
     * Type of victory (e.g., "ring_destroyed", "military", "corruption")
     */
    public String getVictoryType() {
        return victoryType;
    }
    
    /**
     * Description of victory
     */
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        if (gameOver) {
            return String.format("VICTORY: %s wins by %s - %s", winner, victoryType, description);
        } else {
            return "NO VICTORY: " + description;
        }
    }
}
