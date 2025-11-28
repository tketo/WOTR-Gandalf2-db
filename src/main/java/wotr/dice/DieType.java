package wotr.dice;

/**
 * DieType - Action die face results
 * 
 * Based on War of the Ring official rules:
 * - Free Peoples dice: Character, Army, Muster, Event, Will of the West
 * - Shadow dice: Character, Army, Muster, Event, Eye (replaces Will)
 * 
 * Eye results automatically go to Hunt Box when rolled
 */
public enum DieType {
    /**
     * Character Action - Move characters, play character cards
     */
    CHARACTER("Character", "Move characters, use character abilities"),
    
    /**
     * Army Action - Move armies, attack
     */
    ARMY("Army", "Move armies, initiate attacks"),
    
    /**
     * Muster Action - Recruit units, activate nations
     */
    MUSTER("Muster", "Recruit units, bring reinforcements"),
    
    /**
     * Event Action - Play event cards
     */
    EVENT("Event", "Play event cards"),
    
    /**
     * Will of the West - Free Peoples special (advance Fellowship, play cards)
     */
    WILL_OF_WEST("Will of the West", "Advance Fellowship, special FP actions"),
    
    /**
     * Eye - Shadow special (automatically goes to Hunt Box)
     */
    EYE("Eye", "Goes to Hunt Box, hunts the Fellowship");
    
    private final String displayName;
    private final String description;
    
    DieType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Is this a Free Peoples exclusive die type?
     */
    public boolean isFreePeoplesOnly() {
        return this == WILL_OF_WEST;
    }
    
    /**
     * Is this a Shadow exclusive die type?
     */
    public boolean isShadowOnly() {
        return this == EYE;
    }
    
    /**
     * Does this die result go to Hunt Box automatically?
     */
    public boolean goesToHuntBox() {
        return this == EYE;
    }
    
    /**
     * Can this die type be used for Fellowship movement?
     */
    public boolean canMoveFellowship() {
        return this == CHARACTER || this == WILL_OF_WEST;
    }
}
