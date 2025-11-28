package wotr.models;

public class GameState {
    private String gameId;
    private int turnNumber;
    private String phase;
    private String activePlayer;
    private int victoryPointsFP;
    private int victoryPointsShadow;
    private String createdAt;
    private String updatedAt;
    
    public GameState() {
    }
    
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    
    public int getTurnNumber() { return turnNumber; }
    public void setTurnNumber(int turnNumber) { this.turnNumber = turnNumber; }
    
    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }
    
    public String getActivePlayer() { return activePlayer; }
    public void setActivePlayer(String activePlayer) { this.activePlayer = activePlayer; }
    
    public int getVictoryPointsFP() { return victoryPointsFP; }
    public void setVictoryPointsFP(int victoryPointsFP) { this.victoryPointsFP = victoryPointsFP; }
    
    public int getVictoryPointsShadow() { return victoryPointsShadow; }
    public void setVictoryPointsShadow(int victoryPointsShadow) { this.victoryPointsShadow = victoryPointsShadow; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    public String toString() {
        return "GameState{gameId='" + gameId + "', turn=" + turnNumber + 
               ", phase='" + phase + "', player='" + activePlayer + 
               "', VP(FP/Shadow)=" + victoryPointsFP + "/" + victoryPointsShadow + "}";
    }
}
