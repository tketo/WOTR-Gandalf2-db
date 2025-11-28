package wotr.cards;

/**
 * CardLoaderTest - Test loading real card data
 * 
 * Demonstrates loading cards from JSON files
 */
public class CardLoaderTest {
    
    public static void main(String[] args) {
        System.out.println("=== WOTR Card Loader Test ===\n");
        
        try {
            // Check card counts
            int eventCardCount = CardLoader.getCardCount("data/eventcards.json");
            int combatCardCount = CardLoader.getCardCount("data/combatcards.json");
            
            System.out.println("Found in JSON files:");
            System.out.println("  Event cards: " + eventCardCount);
            System.out.println("  Combat cards: " + combatCardCount);
            System.out.println();
            
            // Create card manager and load cards
            CardManager manager = new CardManager();
            
            System.out.println("Loading cards from JSON...");
            CardLoader.loadCards(manager);
            System.out.println("✓ Cards loaded successfully\n");
            
            // Display deck statistics
            System.out.println(manager.getSummary());
            
            // Test drawing cards
            System.out.println("\n--- Testing Card Draw ---");
            
            EventCard fpChar = manager.getDeck("free_peoples", CardType.CHARACTER).draw();
            if (fpChar != null) {
                System.out.println("Drew FP Character card: " + fpChar.getName());
                System.out.println("  Event: " + fpChar.getEventText().substring(0, Math.min(50, fpChar.getEventText().length())) + "...");
                if (fpChar.hasCombatCard()) {
                    System.out.println("  Combat: " + fpChar.getCombatText().substring(0, Math.min(50, fpChar.getCombatText().length())) + "...");
                }
            }
            
            System.out.println();
            
            EventCard shadowChar = manager.getDeck("shadow", CardType.CHARACTER).draw();
            if (shadowChar != null) {
                System.out.println("Drew Shadow Character card: " + shadowChar.getName());
                System.out.println("  Event: " + shadowChar.getEventText().substring(0, Math.min(50, shadowChar.getEventText().length())) + "...");
                if (shadowChar.hasCombatCard()) {
                    System.out.println("  Combat: " + shadowChar.getCombatText().substring(0, Math.min(50, shadowChar.getCombatText().length())) + "...");
                }
            }
            
            System.out.println();
            
            // Test drawing strategy cards
            EventCard fpStrat = manager.getDeck("free_peoples", CardType.STRATEGY).draw();
            if (fpStrat != null) {
                System.out.println("Drew FP Strategy card: " + fpStrat.getName());
                System.out.println("  Event: " + fpStrat.getEventText().substring(0, Math.min(50, fpStrat.getEventText().length())) + "...");
            }
            
            System.out.println();
            
            EventCard shadowStrat = manager.getDeck("shadow", CardType.STRATEGY).draw();
            if (shadowStrat != null) {
                System.out.println("Drew Shadow Strategy card: " + shadowStrat.getName());
                System.out.println("  Event: " + shadowStrat.getEventText().substring(0, Math.min(50, shadowStrat.getEventText().length())) + "...");
            }
            
            System.out.println("\n✓ Card system working with real game data!");
            System.out.println("\n=== All Tests Passed! ===");
            
        } catch (Exception e) {
            System.err.println("\n=== Test Failed ===");
            e.printStackTrace();
        }
    }
}
