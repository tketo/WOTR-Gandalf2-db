package wotr.models;

public class Settlement {
    private int id;
    private String regionId;
    private String type;
    private int vp;
    private boolean canMuster;
    
    public Settlement() {}
    
    public Settlement(String regionId, String type, int vp, boolean canMuster) {
        this.regionId = regionId;
        this.type = type;
        this.vp = vp;
        this.canMuster = canMuster;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getRegionId() {
        return regionId;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public int getVp() {
        return vp;
    }
    
    public void setVp(int vp) {
        this.vp = vp;
    }
    
    public boolean isCanMuster() {
        return canMuster;
    }
    
    public void setCanMuster(boolean canMuster) {
        this.canMuster = canMuster;
    }
    
    public String toString() {
        return "Settlement{type='" + type + "', vp=" + vp + ", canMuster=" + canMuster + "}";
    }
}
