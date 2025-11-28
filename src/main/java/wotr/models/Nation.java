package wotr.models;

public class Nation {
    private int id;
    private String name;
    private String faction; // 'free_peoples' or 'shadow'
    
    public Nation() {}
    
    public Nation(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Nation(int id, String name, String faction) {
        this.id = id;
        this.name = name;
        this.faction = faction;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getFaction() {
        return faction;
    }
    
    public void setFaction(String faction) {
        this.faction = faction;
    }
    
    public boolean isFreePeoples() {
        return "free_peoples".equals(faction);
    }
    
    public boolean isShadow() {
        return "shadow".equals(faction);
    }
    
    public String toString() {
        return "Nation{id=" + id + ", name='" + name + "', faction='" + faction + "'}";
    }
}
