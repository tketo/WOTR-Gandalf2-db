package wotr.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardDeck - Manages a deck of event cards
 * 
 * Based on War of the Ring rules:
 * - Each side has 2 decks: Character (24 cards) and Strategy (24 cards)
 * - Decks are shuffled at game start
 * - Cards drawn from top, discarded to discard pile
 * - When deck empty, reshuffle discard pile
 */
public class CardDeck {
    private String faction; // "free_peoples" or "shadow"
    private CardType type; // CHARACTER or STRATEGY
    private List<EventCard> drawPile;
    private List<EventCard> discardPile;
    
    public CardDeck(String faction, CardType type) {
        this.faction = faction;
        this.type = type;
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }
    
    /**
     * Add a card to the deck
     */
    public void addCard(EventCard card) {
        drawPile.add(card);
    }
    
    /**
     * Shuffle the draw pile
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }
    
    /**
     * Draw a card from the top of the deck
     * Automatically reshuffles discard pile if deck is empty
     */
    public EventCard draw() {
        if (drawPile.isEmpty()) {
            if (discardPile.isEmpty()) {
                return null; // No cards available
            }
            // Reshuffle discard pile into draw pile
            drawPile.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }
        
        if (drawPile.isEmpty()) {
            return null;
        }
        
        EventCard card = drawPile.remove(0);
        card.setInHand(true);
        return card;
    }
    
    /**
     * Discard a card
     */
    public void discard(EventCard card) {
        card.setInHand(false);
        card.setPlayed(true);
        discardPile.add(card);
    }
    
    /**
     * Get number of cards in draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }
    
    /**
     * Get number of cards in discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
    
    /**
     * Get faction
     */
    public String getFaction() {
        return faction;
    }
    
    /**
     * Get deck type
     */
    public CardType getType() {
        return type;
    }
    
    /**
     * Get deck summary
     */
    public String getSummary() {
        return String.format("%s %s Deck: %d cards, %d discarded",
            faction.replace("_", " "),
            type.getDisplayName(),
            drawPile.size(),
            discardPile.size());
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}
