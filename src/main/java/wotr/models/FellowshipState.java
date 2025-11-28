package wotr.models;

public class FellowshipState {
    private String gameId;
    private String regionId;
    private int corruptionLevel;
    private boolean revealed;
    private String guideCharacterId;
    
    public FellowshipState() {
    }
    
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    
    public int getCorruptionLevel() { return corruptionLevel; }
    public void setCorruptionLevel(int corruptionLevel) { this.corruptionLevel = corruptionLevel; }
    
    public boolean isRevealed() { return revealed; }
    public void setRevealed(boolean revealed) { this.revealed = revealed; }
    
    public String getGuideCharacterId() { return guideCharacterId; }
    public void setGuideCharacterId(String guideCharacterId) { this.guideCharacterId = guideCharacterId; }
    
    public String toString() {
        return "FellowshipState{region='" + regionId + "', corruption=" + corruptionLevel + 
               ", revealed=" + revealed + ", guide='" + guideCharacterId + "'}";
    }
}
