package wotr.models;

public class Region {
    private String id;
    private String name;
    private Integer nationId;
    private Settlement settlement;
    
    public Region() {}
    
    public Region(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getNationId() {
        return nationId;
    }
    
    public void setNationId(Integer nationId) {
        this.nationId = nationId;
    }
    
    public Settlement getSettlement() {
        return settlement;
    }
    
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }
    
    public String toString() {
        return "Region{id='" + id + "', name='" + name + "', nationId=" + nationId + "}";
    }
}
