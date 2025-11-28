package wotr.models;

public class ArmyUnit {
    private String gameId;
    private String regionId;
    private int nationId;
    private String unitType;
    private int count;
    
    public ArmyUnit() {
    }
    
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    
    public int getNationId() { return nationId; }
    public void setNationId(int nationId) { this.nationId = nationId; }
    
    public String getUnitType() { return unitType; }
    public void setUnitType(String unitType) { this.unitType = unitType; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    public String toString() {
        return "ArmyUnit{region='" + regionId + "', nation=" + nationId + 
               ", type='" + unitType + "', count=" + count + "}";
    }
}
