package wotr.cards;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * CardLoader - Load card data from JSON files
 * 
 * Reads eventcards.json and combatcards.json to populate card decks
 */
public class CardLoader {
    
    /**
     * Load all cards from JSON files into a CardManager
     */
    public static void loadCards(CardManager manager) throws Exception {
        // Load combat cards first (for lookup)
        Map<String, JsonObject> combatCards = loadCombatCards();
        
        // Load event cards and populate decks
        loadEventCards(manager, combatCards);
        
        // Shuffle all decks
        manager.getDeck("free_peoples", CardType.CHARACTER).shuffle();
        manager.getDeck("free_peoples", CardType.STRATEGY).shuffle();
        manager.getDeck("shadow", CardType.CHARACTER).shuffle();
        manager.getDeck("shadow", CardType.STRATEGY).shuffle();
    }
    
    /**
     * Load combat cards into a map for lookup
     */
    private static Map<String, JsonObject> loadCombatCards() throws Exception {
        Map<String, JsonObject> combatMap = new HashMap<>();
        
        Gson gson = new Gson();
        FileReader reader = new FileReader("data/combatcards.json");
        JsonArray cards = gson.fromJson(reader, JsonArray.class);
        reader.close();
        
        for (int i = 0; i < cards.size(); i++) {
            JsonObject combat = cards.get(i).getAsJsonObject();
            String combatId = combat.get("combatId").getAsString();
            combatMap.put(combatId, combat);
        }
        
        return combatMap;
    }
    
    /**
     * Load event cards and add to appropriate decks
     */
    private static void loadEventCards(CardManager manager, Map<String, JsonObject> combatCards) throws Exception {
        Gson gson = new Gson();
        FileReader reader = new FileReader("data/eventcards.json");
        JsonArray cards = gson.fromJson(reader, JsonArray.class);
        reader.close();
        
        for (int i = 0; i < cards.size(); i++) {
            JsonObject cardData = cards.get(i).getAsJsonObject();
            
            // Parse event card
            EventCard card = parseEventCard(cardData, combatCards);
            
            // Determine which deck this card belongs to
            String faction = card.getFaction();
            CardType type = card.getType();
            
            CardDeck deck = manager.getDeck(faction, type);
            if (deck != null) {
                deck.addCard(card);
            }
        }
    }
    
    /**
     * Parse a single event card from JSON
     */
    private static EventCard parseEventCard(JsonObject cardData, Map<String, JsonObject> combatCards) {
        EventCard card = new EventCard();
        
        // Basic fields
        String cardNumber = cardData.get("cardNumber").getAsString();
        card.setId(cardNumber);
        
        String side = cardData.get("side").getAsString();
        // Convert "freePeoples" to "free_peoples" for consistency
        String faction = side.equals("freePeoples") ? "free_peoples" : side;
        card.setFaction(faction);
        
        String typeStr = cardData.get("type").getAsString();
        // Map "muster" to STRATEGY
        CardType type = typeStr.equals("muster") ? CardType.STRATEGY : CardType.CHARACTER;
        card.setType(type);
        
        // Event portion
        JsonObject event = cardData.getAsJsonObject("event");
        if (event != null) {
            String title = event.get("title").getAsString();
            card.setName(title);
            
            String effectText = event.get("effectText").getAsString();
            card.setEventText(effectText);
        }
        
        // Combat card portion
        if (cardData.has("combatId") && cardData.has("combatTitle")) {
            String combatId = cardData.get("combatId").getAsString();
            String combatTitle = cardData.get("combatTitle").getAsString();
            
            // Look up combat card details
            JsonObject combat = combatCards.get(combatId);
            if (combat != null) {
                String combatEffect = combat.get("effectText").getAsString();
                card.setCombatText(combatTitle + ": " + combatEffect);
                card.setHasCombatCard(true);
            } else {
                card.setCombatText(combatTitle);
                card.setHasCombatCard(true);
            }
        }
        
        // Determine required die type
        // Character cards require character die, Strategy cards require muster/army die
        if (type == CardType.CHARACTER) {
            card.setRequiredDie("character");
        } else {
            card.setRequiredDie("muster"); // or "army" - strategy cards can be played with either
        }
        
        return card;
    }
    
    /**
     * Get count of cards in JSON file
     */
    public static int getCardCount(String filename) throws Exception {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filename);
        JsonArray cards = gson.fromJson(reader, JsonArray.class);
        reader.close();
        return cards.size();
    }
}
