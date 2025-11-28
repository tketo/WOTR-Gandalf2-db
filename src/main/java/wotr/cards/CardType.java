package wotr.cards;

/**
 * CardType - Types of event cards
 * 
 * Based on War of the Ring rules:
 * - Character cards: Character-themed events
 * - Strategy cards: Strategy and tactical events
 * - Each faction has both types
 */
public enum CardType {
    /**
     * Character event card
     */
    CHARACTER("Character"),
    
    /**
     * Strategy event card
     */
    STRATEGY("Strategy");
    
    private final String displayName;
    
    CardType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
