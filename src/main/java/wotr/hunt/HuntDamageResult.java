package wotr.hunt;

import java.util.ArrayList;
import java.util.List;

/**
 * HuntDamageResult - Result of resolving drawn Hunt tiles
 */
public class HuntDamageResult {
    private int totalDamage;
    private boolean hasReveal;
    private int eyeTileCount;
    private List<String> specialTiles;
    
    public HuntDamageResult() {
        this.specialTiles = new ArrayList<>();
    }
    
    public int getTotalDamage() {
        return totalDamage;
    }
    
    public void setTotalDamage(int damage) {
        this.totalDamage = damage;
    }
    
    public boolean hasReveal() {
        return hasReveal;
    }
    
    public void setHasReveal(boolean reveal) {
        this.hasReveal = reveal;
    }
    
    public int getEyeTileCount() {
        return eyeTileCount;
    }
    
    public void addEyeTile() {
        this.eyeTileCount++;
    }
    
    public List<String> getSpecialTiles() {
        return specialTiles;
    }
    
    public void addSpecialTile(String tileName) {
        this.specialTiles.add(tileName);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hunt Damage: ").append(totalDamage);
        if (eyeTileCount > 0) {
            sb.append(" + ").append(eyeTileCount).append(" Eye tile(s)");
        }
        if (hasReveal) {
            sb.append(" [REVEAL]");
        }
        if (!specialTiles.isEmpty()) {
            sb.append(" Special: ").append(specialTiles);
        }
        return sb.toString();
    }
}
