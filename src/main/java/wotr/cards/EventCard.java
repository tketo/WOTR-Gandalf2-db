package wotr.cards;

/**
 * EventCard - Represents a single event card
 * 
 * Based on War of the Ring rules:
 * - Each card has an Event portion (top) and Combat Card portion (bottom)
 * - Event requires matching die: Character, Muster, Army, or Palantir
 * - Combat Card played during battle (no die required)
 * - Cards belong to a faction (Free Peoples or Shadow)
 * - Cards may have nation-specific icons
 */
public class EventCard {
    private String id;
    private String name;
    private CardType type; // CHARACTER or STRATEGY
    private String faction; // "free_peoples" or "shadow"
    private String nation; // Optional: specific nation (e.g., "gondor", "rohan")
    
    // Event portion
    private String eventText;
    private String requiredDie; // "character", "muster", "army", "event", or "any" (Palantir)
    
    // Combat Card portion
    private String combatText;
    private boolean hasCombatCard;
    
    // Game state
    private boolean inHand;
    private boolean played;
    
    public EventCard() {
        this.inHand = false;
        this.played = false;
        this.hasCombatCard = false;
    }
    
    public EventCard(String id, String name, CardType type, String faction) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.faction = faction;
        this.inHand = false;
        this.played = false;
        this.hasCombatCard = false;
    }
    
    // Getters and setters
    
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
    
    public CardType getType() {
        return type;
    }
    
    public void setType(CardType type) {
        this.type = type;
    }
    
    public String getFaction() {
        return faction;
    }
    
    public void setFaction(String faction) {
        this.faction = faction;
    }
    
    public String getNation() {
        return nation;
    }
    
    public void setNation(String nation) {
        this.nation = nation;
    }
    
    public String getEventText() {
        return eventText;
    }
    
    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
    
    public String getRequiredDie() {
        return requiredDie;
    }
    
    public void setRequiredDie(String requiredDie) {
        this.requiredDie = requiredDie;
    }
    
    public String getCombatText() {
        return combatText;
    }
    
    public void setCombatText(String combatText) {
        this.combatText = combatText;
    }
    
    public boolean hasCombatCard() {
        return hasCombatCard;
    }
    
    public void setHasCombatCard(boolean hasCombatCard) {
        this.hasCombatCard = hasCombatCard;
    }
    
    public boolean isInHand() {
        return inHand;
    }
    
    public void setInHand(boolean inHand) {
        this.inHand = inHand;
    }
    
    public boolean isPlayed() {
        return played;
    }
    
    public void setPlayed(boolean played) {
        this.played = played;
    }
    
    /**
     * Can this card be played with the given die type?
     */
    public boolean canPlayWith(String dieType) {
        if ("event".equals(dieType)) {
            // Event die can play any card
            return true;
        }
        if ("any".equals(requiredDie)) {
            // Palantir cards
            return true;
        }
        return requiredDie != null && requiredDie.equals(dieType);
    }
    
    /**
     * Is this card playable by the given nation?
     */
    public boolean isPlayableBy(String playerNation) {
        if (nation == null || nation.isEmpty()) {
            // No specific nation requirement - any player of faction can play
            return true;
        }
        return nation.equals(playerNation);
    }
    
    @Override
    public String toString() {
        return "EventCard{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", faction='" + faction + '\'' +
                ", requiredDie='" + requiredDie + '\'' +
                ", hasCombat=" + hasCombatCard +
                '}';
    }
}
