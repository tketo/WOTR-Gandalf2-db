package wotr.cards;

import java.util.HashMap;
import java.util.Map;

/**
 * CardManager - Manages all card decks and player hands
 * 
 * Based on War of the Ring rules:
 * - 4 decks total: FP Character, FP Strategy, Shadow Character, Shadow Strategy
 * - Each side has a hand (or multiple hands in multiplayer)
 * - Handles draw mechanics for different player counts
 */
public class CardManager {
    private Map<String, CardDeck> decks;
    private Map<String, PlayerHand> hands;
    
    public CardManager() {
        this.decks = new HashMap<>();
        this.hands = new HashMap<>();
        initializeDecks();
    }
    
    /**
     * Initialize the 4 card decks
     */
    private void initializeDecks() {
        decks.put("free_peoples_character", new CardDeck("free_peoples", CardType.CHARACTER));
        decks.put("free_peoples_strategy", new CardDeck("free_peoples", CardType.STRATEGY));
        decks.put("shadow_character", new CardDeck("shadow", CardType.CHARACTER));
        decks.put("shadow_strategy", new CardDeck("shadow", CardType.STRATEGY));
        
        // Initialize hands for each faction
        hands.put("free_peoples", new PlayerHand("free_peoples"));
        hands.put("shadow", new PlayerHand("shadow"));
    }
    
    /**
     * Get a specific deck
     */
    public CardDeck getDeck(String faction, CardType type) {
        String key = faction.replace("_", "") + "_" + type.name().toLowerCase();
        CardDeck deck = decks.get(key);
        if (deck == null) {
            // Try without modifying faction name
            key = faction + "_" + type.name().toLowerCase();
            deck = decks.get(key);
        }
        return deck;
    }
    
    /**
     * Get a player's hand
     */
    public PlayerHand getHand(String faction) {
        return hands.get(faction);
    }
    
    /**
     * Draw cards at start of turn (2-player game)
     * Each player draws 1 Character and 1 Strategy card
     */
    public void drawStartOfTurn2Player() {
        // Free Peoples draws
        PlayerHand fpHand = hands.get("free_peoples");
        CardDeck fpChar = getDeck("free_peoples", CardType.CHARACTER);
        CardDeck fpStrat = getDeck("free_peoples", CardType.STRATEGY);
        
        EventCard card1 = fpChar.draw();
        if (card1 != null) fpHand.addCard(card1);
        
        EventCard card2 = fpStrat.draw();
        if (card2 != null) fpHand.addCard(card2);
        
        // Shadow draws
        PlayerHand shadowHand = hands.get("shadow");
        CardDeck shadowChar = getDeck("shadow", CardType.CHARACTER);
        CardDeck shadowStrat = getDeck("shadow", CardType.STRATEGY);
        
        EventCard card3 = shadowChar.draw();
        if (card3 != null) shadowHand.addCard(card3);
        
        EventCard card4 = shadowStrat.draw();
        if (card4 != null) shadowHand.addCard(card4);
    }
    
    /**
     * Draw a card from a specific deck
     */
    public EventCard drawCard(String faction, CardType type) {
        CardDeck deck = getDeck(faction, type);
        if (deck == null) return null;
        
        EventCard card = deck.draw();
        if (card != null) {
            PlayerHand hand = getHand(faction);
            if (hand != null) {
                hand.addCard(card);
            }
        }
        return card;
    }
    
    /**
     * Play a card from hand
     */
    public boolean playCard(String faction, String cardId, String dieType) {
        PlayerHand hand = getHand(faction);
        if (hand == null) return false;
        
        EventCard card = hand.findCard(cardId);
        if (card == null) return false;
        
        // Check if card can be played with this die
        if (!card.canPlayWith(dieType)) {
            return false;
        }
        
        // Remove from hand
        hand.removeCard(card);
        
        // Add to discard pile
        CardDeck deck = getDeck(faction, card.getType());
        if (deck != null) {
            deck.discard(card);
        }
        
        return true;
    }
    
    /**
     * Get summary of all decks and hands
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Card Manager Status ===\n");
        sb.append("\nDecks:\n");
        for (CardDeck deck : decks.values()) {
            sb.append("  ").append(deck.getSummary()).append("\n");
        }
        sb.append("\nHands:\n");
        for (PlayerHand hand : hands.values()) {
            sb.append("  ").append(hand.getSummary()).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}
