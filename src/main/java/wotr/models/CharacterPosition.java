package wotr.models;

public class CharacterPosition {
    private String gameId;
    private String characterId;
    private String regionId;
    private String status;
    
    public CharacterPosition() {
    }
    
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    
    public String getCharacterId() { return characterId; }
    public void setCharacterId(String characterId) { this.characterId = characterId; }
    
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String toString() {
        return "CharacterPosition{character='" + characterId + "', region='" + regionId + 
               "', status='" + status + "'}";
    }
}
