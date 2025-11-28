package wotr.cards;

import java.util.List;

/**
 * CardSystemTest - Test card mechanics
 * 
 * Demonstrates deck management, drawing, and hand management
 */
public class CardSystemTest {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Card System Test ===\n");
        
        try {
            testCardCreation();
            testDeckManagement();
            testHandManagement();
            testCardManager();
            testCardPlay();
            
            System.out.println("\n=== All Card Tests Passed! ===");
            
        } catch (Exception e) {
            System.err.println("\n=== Test Failed ===");
            e.printStackTrace();
        }
    }
    
    private static void testCardCreation() {
        System.out.println("--- Testing Card Creation ---");
        
        EventCard card = new EventCard("fp_char_01", "Test Card", CardType.CHARACTER, "free_peoples");
        card.setEventText("Test event effect");
        card.setRequiredDie("character");
        card.setHasCombatCard(true);
        card.setCombatText("Test combat effect");
        
        System.out.println("✓ Created card: " + card.toString());
        
        // Test die requirements
        if (card.canPlayWith("character")) {
            System.out.println("✓ Card playable with Character die");
        }
        if (card.canPlayWith("event")) {
            System.out.println("✓ Card playable with Event die");
        }
        if (!card.canPlayWith("muster")) {
            System.out.println("✓ Card not playable with Muster die (correct)");
        }
        
        System.out.println();
    }
    
    private static void testDeckManagement() {
        System.out.println("--- Testing Deck Management ---");
        
        CardDeck deck = new CardDeck("free_peoples", CardType.CHARACTER);
        
        // Add sample cards
        for (int i = 1; i <= 10; i++) {
            EventCard card = new EventCard("fp_char_" + i, "Test Card " + i, 
                                          CardType.CHARACTER, "free_peoples");
            card.setRequiredDie("character");
            deck.addCard(card);
        }
        
        System.out.println("✓ Added 10 cards to deck");
        System.out.println("  " + deck.getSummary());
        
        // Shuffle
        deck.shuffle();
        System.out.println("✓ Shuffled deck");
        
        // Draw cards
        EventCard card1 = deck.draw();
        EventCard card2 = deck.draw();
        System.out.println("✓ Drew 2 cards: " + card1.getName() + ", " + card2.getName());
        System.out.println("  " + deck.getSummary());
        
        // Discard
        deck.discard(card1);
        System.out.println("✓ Discarded 1 card");
        System.out.println("  " + deck.getSummary());
        
        // Draw all remaining
        while (deck.getDrawPileSize() > 0) {
            deck.draw();
        }
        System.out.println("✓ Drew all remaining cards");
        System.out.println("  " + deck.getSummary());
        
        // Auto-reshuffle test
        EventCard card3 = deck.draw();
        if (card3 != null) {
            System.out.println("✓ Auto-reshuffled discard pile: drew " + card3.getName());
        }
        
        System.out.println();
    }
    
    private static void testHandManagement() {
        System.out.println("--- Testing Hand Management ---");
        
        PlayerHand hand = new PlayerHand("free_peoples");
        hand.setHandLimit(6);
        
        // Add cards to hand
        for (int i = 1; i <= 4; i++) {
            EventCard card = new EventCard("card_" + i, "Card " + i,
                                          i % 2 == 0 ? CardType.STRATEGY : CardType.CHARACTER,
                                          "free_peoples");
            card.setRequiredDie(i % 2 == 0 ? "army" : "character");
            hand.addCard(card);
        }
        
        System.out.println("✓ Added 4 cards to hand");
        System.out.println("  " + hand.getSummary());
        
        // Get playable cards
        List<EventCard> charCards = hand.getPlayableCards("character");
        System.out.println("✓ Found " + charCards.size() + " cards playable with Character die");
        
        List<EventCard> armyCards = hand.getPlayableCards("army");
        System.out.println("✓ Found " + armyCards.size() + " cards playable with Army die");
        
        // Get cards by type
        List<EventCard> characters = hand.getCardsByType(CardType.CHARACTER);
        List<EventCard> strategies = hand.getCardsByType(CardType.STRATEGY);
        System.out.println("✓ Hand contains " + characters.size() + " Character, " + 
                          strategies.size() + " Strategy cards");
        
        // Test hand limit
        hand.addCard(new EventCard("card_5", "Card 5", CardType.CHARACTER, "free_peoples"));
        hand.addCard(new EventCard("card_6", "Card 6", CardType.STRATEGY, "free_peoples"));
        
        if (hand.isFull()) {
            System.out.println("✓ Hand is full (6 cards)");
        }
        
        boolean added = hand.addCard(new EventCard("card_7", "Card 7", CardType.CHARACTER, "free_peoples"));
        if (!added) {
            System.out.println("✓ Cannot add card to full hand (correct)");
        }
        
        System.out.println();
    }
    
    private static void testCardManager() {
        System.out.println("--- Testing Card Manager ---");
        
        CardManager manager = new CardManager();
        
        // Add sample cards to decks
        for (int i = 1; i <= 5; i++) {
            EventCard fpChar = new EventCard("fp_char_" + i, "FP Character " + i,
                                             CardType.CHARACTER, "free_peoples");
            fpChar.setRequiredDie("character");
            manager.getDeck("free_peoples", CardType.CHARACTER).addCard(fpChar);
            
            EventCard fpStrat = new EventCard("fp_strat_" + i, "FP Strategy " + i,
                                              CardType.STRATEGY, "free_peoples");
            fpStrat.setRequiredDie("army");
            manager.getDeck("free_peoples", CardType.STRATEGY).addCard(fpStrat);
            
            EventCard shChar = new EventCard("sh_char_" + i, "Shadow Character " + i,
                                            CardType.CHARACTER, "shadow");
            shChar.setRequiredDie("character");
            manager.getDeck("shadow", CardType.CHARACTER).addCard(shChar);
            
            EventCard shStrat = new EventCard("sh_strat_" + i, "Shadow Strategy " + i,
                                             CardType.STRATEGY, "shadow");
            shStrat.setRequiredDie("muster");
            manager.getDeck("shadow", CardType.STRATEGY).addCard(shStrat);
        }
        
        System.out.println("✓ Populated 4 decks with 5 cards each");
        
        // Shuffle all decks
        manager.getDeck("free_peoples", CardType.CHARACTER).shuffle();
        manager.getDeck("free_peoples", CardType.STRATEGY).shuffle();
        manager.getDeck("shadow", CardType.CHARACTER).shuffle();
        manager.getDeck("shadow", CardType.STRATEGY).shuffle();
        System.out.println("✓ Shuffled all decks");
        
        // Draw at start of turn (2-player)
        manager.drawStartOfTurn2Player();
        System.out.println("✓ Drew start of turn cards (2-player)");
        
        System.out.println("\n" + manager.getSummary());
    }
    
    private static void testCardPlay() {
        System.out.println("\n--- Testing Card Play ---");
        
        CardManager manager = new CardManager();
        
        // Add a card
        EventCard card = new EventCard("test_card", "Test Card",
                                       CardType.CHARACTER, "free_peoples");
        card.setRequiredDie("character");
        manager.getDeck("free_peoples", CardType.CHARACTER).addCard(card);
        
        // Draw it
        manager.drawCard("free_peoples", CardType.CHARACTER);
        System.out.println("✓ Drew card into hand");
        
        PlayerHand hand = manager.getHand("free_peoples");
        System.out.println("  Hand size: " + hand.getSize());
        
        // Play it with correct die
        boolean played = manager.playCard("free_peoples", "test_card", "character");
        if (played) {
            System.out.println("✓ Successfully played card with Character die");
            System.out.println("  Hand size after: " + hand.getSize());
        }
        
        // Try to play non-existent card
        boolean failedPlay = manager.playCard("free_peoples", "fake_card", "army");
        if (!failedPlay) {
            System.out.println("✓ Cannot play non-existent card (correct)");
        }
        
        System.out.println();
    }
}
