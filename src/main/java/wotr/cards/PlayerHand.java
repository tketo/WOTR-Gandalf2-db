package wotr.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerHand - Manages a player's hand of cards
 * 
 * Based on War of the Ring rules:
 * - Players have a hand limit (typically 6 cards)
 * - Can contain both Character and Strategy cards
 * - Cards played are moved to discard pile
 */
public class PlayerHand {
    private String faction; // "free_peoples" or "shadow"
    private List<EventCard> cards;
    private int handLimit;
    
    public PlayerHand(String faction) {
        this.faction = faction;
        this.cards = new ArrayList<>();
        this.handLimit = 6; // Default hand limit
    }
    
    /**
     * Add a card to hand
     */
    public boolean addCard(EventCard card) {
        if (cards.size() >= handLimit) {
            return false; // Hand full
        }
        cards.add(card);
        card.setInHand(true);
        return true;
    }
    
    /**
     * Remove a card from hand
     */
    public boolean removeCard(EventCard card) {
        boolean removed = cards.remove(card);
        if (removed) {
            card.setInHand(false);
        }
        return removed;
    }
    
    /**
     * Get all cards in hand
     */
    public List<EventCard> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Get cards of a specific type
     */
    public List<EventCard> getCardsByType(CardType type) {
        List<EventCard> result = new ArrayList<>();
        for (EventCard card : cards) {
            if (card.getType() == type) {
                result.add(card);
            }
        }
        return result;
    }
    
    /**
     * Get cards playable with a specific die
     */
    public List<EventCard> getPlayableCards(String dieType) {
        List<EventCard> result = new ArrayList<>();
        for (EventCard card : cards) {
            if (card.canPlayWith(dieType)) {
                result.add(card);
            }
        }
        return result;
    }
    
    /**
     * Find a card by ID
     */
    public EventCard findCard(String cardId) {
        for (EventCard card : cards) {
            if (card.getId().equals(cardId)) {
                return card;
            }
        }
        return null;
    }
    
    /**
     * Get hand size
     */
    public int getSize() {
        return cards.size();
    }
    
    /**
     * Is hand full?
     */
    public boolean isFull() {
        return cards.size() >= handLimit;
    }
    
    /**
     * Get hand limit
     */
    public int getHandLimit() {
        return handLimit;
    }
    
    /**
     * Set hand limit
     */
    public void setHandLimit(int handLimit) {
        this.handLimit = handLimit;
    }
    
    /**
     * Get faction
     */
    public String getFaction() {
        return faction;
    }
    
    /**
     * Get hand summary
     */
    public String getSummary() {
        int characterCount = getCardsByType(CardType.CHARACTER).size();
        int strategyCount = getCardsByType(CardType.STRATEGY).size();
        return String.format("%s Hand: %d cards (%d Character, %d Strategy)",
            faction.replace("_", " "),
            cards.size(),
            characterCount,
            strategyCount);
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}
