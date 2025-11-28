package wotr.cards.effects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wotr.services.GameStateService;
import wotr.politics.PoliticalTrack;
import wotr.actions.ActionResult;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for PoliticalCardEffects
 * Tests nation activation and political track advancement
 */
@ExtendWith(MockitoExtension.class)
public class PoliticalCardEffectsTest {
    
    @Mock
    private GameStateService mockGameState;
    
    @Mock
    private PoliticalTrack mockPoliticalTrack;
    
    private PoliticalCardEffects cardEffects;
    
    @BeforeEach
    public void setUp() {
        cardEffects = new PoliticalCardEffects(mockGameState, mockPoliticalTrack);
    }
    
    // ===== WISDOM OF ELROND TESTS =====
    
    @Test
    public void testWisdomOfElrond_ActivatesAndAdvances() {
        // Mock the advanceNation method to return a success result
        when(mockPoliticalTrack.advanceNation("dwarves", 1))
            .thenReturn(ActionResult.success("Advanced 1 step"));
        
        ActionResult result = cardEffects.wisdomOfElrond("dwarves");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Wisdom of Elrond"));
        assertTrue(result.getMessage().contains("Activated"));
        
        // Verify politicalTrack methods were called
        verify(mockPoliticalTrack).activateNation("dwarves");
        verify(mockPoliticalTrack).advanceNation("dwarves", 1);
    }
    
    // ===== BOOK OF MAZARBUL TESTS =====
    
    @Test
    public void testBookOfMazarbul_WithoutCompanion() throws SQLException {
        ActionResult result = cardEffects.bookOfMazarbul(false);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Book of Mazarbul"));
        assertTrue(result.getMessage().contains("Companions may move"));
        assertFalse(result.getMessage().contains("Dwarven"));
    }
    
    @Test
    public void testBookOfMazarbul_WithCompanionInDwarvenRegion() throws SQLException {
        ActionResult result = cardEffects.bookOfMazarbul(true);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Dwarven Nation activated"));
        assertTrue(result.getMessage().contains("At War"));
        
        verify(mockGameState).movePoliticalMarker("dwarves", 0);
    }
    
    // ===== FEAR! FIRE! FOES! TESTS =====
    
    @Test
    public void testFearFireFoes_WithoutCompanion() throws SQLException {
        ActionResult result = cardEffects.fearFireFoes(false);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Fear! Fire! Foes!"));
        assertFalse(result.getMessage().contains("North Nation"));
    }
    
    @Test
    public void testFearFireFoes_WithCompanionInNorthRegion() throws SQLException {
        ActionResult result = cardEffects.fearFireFoes(true);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("North Nation activated"));
        assertTrue(result.getMessage().contains("At War"));
        
        verify(mockGameState).movePoliticalMarker("north", 0);
    }
    
    // ===== THE RED ARROW TESTS =====
    
    @Test
    public void testTheRedArrow_AdvancesRohan() throws SQLException {
        // Mock Gondor as active (required for card to work)
        when(mockPoliticalTrack.isNationActive("gondor")).thenReturn(true);
        
        ActionResult result = cardEffects.theRedArrow();
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Red Arrow"));
        assertTrue(result.getMessage().contains("Rohan"));
    }
    
    // ===== THERE AND BACK AGAIN TESTS =====
    
    @Test
    public void testThereAndBackAgain_WithoutGimliOrLegolas() {
        ActionResult result = cardEffects.thereAndBackAgain(false);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("There and Back Again"));
        assertTrue(result.getMessage().contains("Companion may separate"));
        assertFalse(result.getMessage().contains("Activated"));
    }
    
    @Test
    public void testThereAndBackAgain_WithGimliOrLegolas() {
        ActionResult result = cardEffects.thereAndBackAgain(true);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Activated Dwarves and North"));
        assertTrue(result.getMessage().contains("Advanced"));
    }
}
