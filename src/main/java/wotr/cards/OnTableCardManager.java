package wotr.cards;

import wotr.services.GameStateService;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.List;

/**
 * OnTableCardManager - Manages cards that stay in play with ongoing effects
 * 
 * On-table cards occupy areas 146-151 and provide persistent effects until discarded.
 * Examples: Wormtongue, The Last Battle, A Power too Great
 */
public class OnTableCardManager {
    
    private final GameStateService gameState;
    
    // Area IDs for on-table cards (generic - both factions can use any slot)
    public static final int CARD_ON_TABLE_1 = 146;
    public static final int CARD_ON_TABLE_2 = 147;
    public static final int CARD_ON_TABLE_3 = 148;
    public static final int CARD_ON_TABLE_4 = 149;
    public static final int CARD_ON_TABLE_5 = 150;
    public static final int CARD_ON_TABLE_6 = 151;
    
    public OnTableCardManager(GameStateService gameState) {
        this.gameState = gameState;
    }
    
    /**
     * Place a card on the table
     * @param cardName Name of the card
     * @param faction "free_peoples" or "shadow"
     * @return ActionResult indicating success or failure
     */
    public ActionResult placeCardOnTable(String cardName, String faction) {
        try {
            // Find first available on-table slot for this faction
            int areaId = getNextAvailableSlot(faction);
            if (areaId == -1) {
                return ActionResult.error("No available on-table slots for " + faction);
            }
            
            // In a real implementation, would move actual card piece to area
            // For now, we track it in database
            gameState.addOnTableCard(cardName, faction, areaId);
            
            return ActionResult.success(
                cardName + " placed on table for " + faction + " (area " + areaId + ")"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to place card on table: " + e.getMessage());
        }
    }
    
    /**
     * Remove a card from the table (discard)
     * @param cardName Name of the card to remove
     * @return ActionResult indicating success or failure
     */
    public ActionResult removeCardFromTable(String cardName) {
        try {
            gameState.removeOnTableCard(cardName);
            return ActionResult.success(cardName + " removed from table");
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to remove card from table: " + e.getMessage());
        }
    }
    
    /**
     * Check if a specific card is on the table
     * @param cardName Name of the card
     * @return true if card is on table
     */
    public boolean isCardOnTable(String cardName) {
        try {
            List<String> onTableCards = gameState.getOnTableCards();
            return onTableCards.contains(cardName);
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Get all cards currently on the table for a faction
     * @param faction "free_peoples" or "shadow"
     * @return List of card names
     */
    public List<String> getOnTableCards(String faction) {
        try {
            return gameState.getOnTableCardsByFaction(faction);
        } catch (SQLException e) {
            return List.of(); // Empty list on error
        }
    }
    
    /**
     * Find next available on-table slot (generic - any faction can use any slot)
     * @param faction "free_peoples" or "shadow" (tracked in database, not by area)
     * @return Area ID of available slot, or -1 if none available
     */
    private int getNextAvailableSlot(String faction) throws SQLException {
        // Check all 6 generic slots (146-151)
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_1)) return CARD_ON_TABLE_1;
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_2)) return CARD_ON_TABLE_2;
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_3)) return CARD_ON_TABLE_3;
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_4)) return CARD_ON_TABLE_4;
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_5)) return CARD_ON_TABLE_5;
        if (!gameState.isAreaOccupied(CARD_ON_TABLE_6)) return CARD_ON_TABLE_6;
        
        return -1; // No available slots (all 6 occupied)
    }
    
    /**
     * Clear all on-table cards (used for game reset)
     */
    public void clearAllOnTableCards() {
        try {
            gameState.clearOnTableCards();
        } catch (SQLException e) {
            // Log error but don't throw
            System.err.println("Error clearing on-table cards: " + e.getMessage());
        }
    }
}
