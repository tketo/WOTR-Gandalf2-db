package wotr.dice;

/**
 * ActionDie - Represents a single action die
 * 
 * Each die belongs to a nation and shows one of several possible results
 * Dice can be: available, used, or in the Hunt Box
 */
public class ActionDie {
    private String id;
    private int nationId;
    private DieType result;
    private DieStatus status;
    private boolean inHuntBox;
    
    public ActionDie() {
        this.status = DieStatus.AVAILABLE;
        this.inHuntBox = false;
    }
    
    public ActionDie(String id, int nationId) {
        this.id = id;
        this.nationId = nationId;
        this.status = DieStatus.AVAILABLE;
        this.inHuntBox = false;
    }
    
    // Getters and setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getNationId() {
        return nationId;
    }
    
    public void setNationId(int nationId) {
        this.nationId = nationId;
    }
    
    public DieType getResult() {
        return result;
    }
    
    public void setResult(DieType result) {
        this.result = result;
    }
    
    public DieStatus getStatus() {
        return status;
    }
    
    public void setStatus(DieStatus status) {
        this.status = status;
    }
    
    public boolean isInHuntBox() {
        return inHuntBox;
    }
    
    public void setInHuntBox(boolean inHuntBox) {
        this.inHuntBox = inHuntBox;
    }
    
    /**
     * Is this die available to be used?
     */
    public boolean isAvailable() {
        return status == DieStatus.AVAILABLE && !inHuntBox;
    }
    
    /**
     * Roll this die (determine result)
     */
    public void roll(boolean isShadow) {
        // Simulate rolling a die
        // FP dice: Character, Army, Muster, Event, Will (5 faces)
        // Shadow dice: Character, Army, Muster, Event, Eye (5 faces)
        
        int roll = (int) (Math.random() * 5);
        
        switch (roll) {
            case 0:
                result = DieType.CHARACTER;
                break;
            case 1:
                result = DieType.ARMY;
                break;
            case 2:
                result = DieType.MUSTER;
                break;
            case 3:
                result = DieType.EVENT;
                break;
            case 4:
                // 5th face depends on faction
                result = isShadow ? DieType.EYE : DieType.WILL_OF_WEST;
                break;
        }
        
        // Eye results automatically go to Hunt Box
        if (result == DieType.EYE) {
            inHuntBox = true;
        }
    }
    
    /**
     * Use this die for an action
     */
    public void use() {
        status = DieStatus.USED;
    }
    
    /**
     * Place in Hunt Box
     */
    public void placeInHuntBox() {
        inHuntBox = true;
    }
    
    /**
     * Remove from Hunt Box
     */
    public void removeFromHuntBox() {
        inHuntBox = false;
    }
    
    /**
     * Recover this die (make available again)
     */
    public void recover() {
        status = DieStatus.AVAILABLE;
        result = null;
        inHuntBox = false;
    }
    
    @Override
    public String toString() {
        return "ActionDie{" +
                "id='" + id + '\'' +
                ", nation=" + nationId +
                ", result=" + (result != null ? result.getDisplayName() : "unrolled") +
                ", status=" + status +
                ", huntBox=" + inHuntBox +
                '}';
    }
}
