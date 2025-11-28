package wotr.cards.effects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wotr.services.GameStateService;
import wotr.fellowship.FellowshipManager;
import wotr.actions.ActionResult;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for FellowshipCardEffects
 * Tests corruption healing, companion separation, and Fellowship movement cards
 */
@ExtendWith(MockitoExtension.class)
public class FellowshipCardEffectsTest {
    
    @Mock
    private GameStateService mockGameState;
    
    @Mock
    private FellowshipManager mockFellowship;
    
    private FellowshipCardEffects cardEffects;
    
    @BeforeEach
    public void setUp() {
        cardEffects = new FellowshipCardEffects(mockGameState, mockFellowship);
    }
    
    // ===== ATHELAS TESTS =====
    
    @Test
    public void testAthelas_HealsSomeCorruption() throws SQLException {
        // Athelas rolls 3 dice and heals on 5+
        ActionResult result = cardEffects.athelas();
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Athelas"));
        assertTrue(result.getMessage().contains("Dice rolls:"));
        
        // Verify corruption was adjusted (could be 0-3 points)
        verify(mockGameState, atMost(3)).adjustCorruption(anyInt());
    }
    
    // ===== BILBO'S SONG TESTS =====
    
    @Test
    public void testBilbosSong_HealsOneCorruption() throws SQLException {
        when(mockFellowship.getFellowshipGuide()).thenReturn("frodo");
        
        ActionResult result = cardEffects.bilbosSong();
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Bilbo's Song"));
        assertTrue(result.getMessage().contains("Healed 1 corruption"));
        
        verify(mockGameState).adjustCorruption(-1);
    }
    
    @Test
    public void testBilbosSong_HealsTwoWithGollum() throws SQLException {
        when(mockFellowship.getFellowshipGuide()).thenReturn("gollum");
        
        ActionResult result = cardEffects.bilbosSong();
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Healed 2 corruption"));
        assertTrue(result.getMessage().contains("Gollum is Guide"));
        
        verify(mockGameState).adjustCorruption(-2);
    }
    
    // ===== THERE IS ANOTHER WAY TESTS =====
    
    @Test
    public void testThereIsAnotherWay_HealsCorruption() throws SQLException {
        when(mockFellowship.getFellowshipGuide()).thenReturn("frodo");
        
        ActionResult result = cardEffects.thereIsAnotherWay(false, null);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Healed 1 corruption"));
        
        verify(mockGameState).adjustCorruption(-1);
        verify(mockGameState, never()).moveFellowship(anyString());
    }
    
    @Test
    public void testThereIsAnotherWay_MovesWithGollumGuide() throws SQLException {
        when(mockFellowship.getFellowshipGuide()).thenReturn("gollum");
        
        ActionResult result = cardEffects.thereIsAnotherWay(true, "moria");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("moved to moria"));
        
        verify(mockGameState).adjustCorruption(-1);
        verify(mockGameState).moveFellowship("moria");
    }
    
    @Test
    public void testThereIsAnotherWay_HidesWithGollumGuide() throws SQLException {
        when(mockFellowship.getFellowshipGuide()).thenReturn("gollum");
        
        ActionResult result = cardEffects.thereIsAnotherWay(true, null);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("hidden"));
        
        verify(mockGameState).adjustCorruption(-1);
        verify(mockGameState).setFellowshipRevealed(false);
    }
    
    // ===== I WILL GO ALONE TESTS =====
    
    @Test
    public void testIWillGoAlone_SeparatesAndHeals() throws SQLException {
        ActionResult separateResult = ActionResult.success("Separated");
        when(mockFellowship.separateCompanion("aragorn", "rivendell")).thenReturn(separateResult);
        
        ActionResult result = cardEffects.iWillGoAlone("aragorn", "rivendell");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("aragorn separated"));
        assertTrue(result.getMessage().contains("Healed 1 corruption"));
        
        verify(mockGameState).adjustCorruption(-1);
    }
    
    // ===== MIRROR OF GALADRIEL TESTS =====
    
    @Test
    public void testMirrorOfGaladriel_ChangesDieToWill() throws SQLException {
        when(mockGameState.getFellowshipLocation()).thenReturn("shire");
        
        ActionResult result = cardEffects.mirrorOfGaladriel("die1");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Will of the West"));
        
        verify(mockGameState).changeDieResult("die1", "will");
    }
    
    @Test
    public void testMirrorOfGaladriel_HealsInLorien() throws SQLException {
        when(mockGameState.getFellowshipLocation()).thenReturn("lorien");
        
        ActionResult result = cardEffects.mirrorOfGaladriel("die1");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Lórien"));
        assertTrue(result.getMessage().contains("healed 1 corruption"));
        
        verify(mockGameState).changeDieResult("die1", "will");
        verify(mockGameState).adjustCorruption(-1);
    }
    
    // ===== GANDALF'S CART TESTS =====
    
    @Test
    public void testGandalfsCart_MovesAndHides() throws SQLException {
        ActionResult result = cardEffects.gandalfsCart("bree");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("moved to bree"));
        assertTrue(result.getMessage().contains("hidden"));
        
        verify(mockGameState).moveFellowship("bree");
        verify(mockGameState).setFellowshipRevealed(false);
    }
    
    // ===== HOBBIT STEALTH TESTS =====
    
    @Test
    public void testHobbitStealth_HidesOnly() throws SQLException {
        ActionResult result = cardEffects.hobbitStealth(false);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("hidden"));
        assertFalse(result.getMessage().contains("corruption"));
        
        verify(mockGameState).setFellowshipRevealed(false);
        verify(mockGameState, never()).adjustCorruption(anyInt());
    }
    
    @Test
    public void testHobbitStealth_HealsWithHobbitGuide() throws SQLException {
        ActionResult result = cardEffects.hobbitStealth(true);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("hidden"));
        assertTrue(result.getMessage().contains("Removed 1 corruption"));
        
        verify(mockGameState).setFellowshipRevealed(false);
        verify(mockGameState).adjustCorruption(-1);
    }
    
    // ===== USE PALANTIR TESTS =====
    
    @Test
    public void testUsePalantir_ChangesDieAndAddsCorruption() throws SQLException {
        ActionResult result = cardEffects.usePalantir("die1", "muster");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Changed die to muster"));
        assertTrue(result.getMessage().contains("Added 1 corruption"));
        
        verify(mockGameState).changeDieResult("die1", "muster");
        verify(mockGameState).adjustCorruption(1);
    }
    
    // ===== SHADOWFAX TESTS =====
    
    @Test
    public void testShadowfax_MovesGandalf() throws SQLException {
        ActionResult result = cardEffects.shadowfax("minas_tirith", null, null);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Gandalf moved to minas_tirith"));
        
        verify(mockGameState).moveCharacter("gandalf_white", "minas_tirith");
    }
    
    @Test
    public void testShadowfax_MovesGandalfAndChangesDie() throws SQLException {
        ActionResult result = cardEffects.shadowfax("edoras", "die2", "character");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("moved to edoras"));
        assertTrue(result.getMessage().contains("Changed die to character"));
        
        verify(mockGameState).moveCharacter("gandalf_white", "edoras");
        verify(mockGameState).changeDieResult("die2", "character");
    }
    
    // ===== KINDRED OF GLORFINDEL TESTS =====
    
    @Test
    public void testKindredOfGlorfindel_RecruitsElves() throws SQLException {
        ActionResult result = cardEffects.kindredOfGlorfindel(true);
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Recruited 2 Elven units"));
        
        verify(mockGameState).addUnits("rivendell", 4, "regular", 2);
    }
    
    @Test
    public void testKindredOfGlorfindel_FailsWithoutCompanion() {
        ActionResult result = cardEffects.kindredOfGlorfindel(false);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("No companion in Rivendell"));
    }
    
    // ===== ANDURIL TESTS =====
    
    @Test
    public void testAnduril_RecruitsGondorUnit() throws SQLException {
        ActionResult result = cardEffects.andurilFlameOfTheWest("osgiliath");
        
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Aragorn +3 Leadership"));
        assertTrue(result.getMessage().contains("Recruited 1 Gondor unit"));
        
        verify(mockGameState).addUnits("osgiliath", 1, "regular", 1);
    }
    
    // ===== ERROR HANDLING TESTS =====
    
    @Test
    public void testBilbosSong_HandlesException() throws SQLException {
        doThrow(new SQLException("Database error")).when(mockGameState).adjustCorruption(anyInt());
        
        ActionResult result = cardEffects.bilbosSong();
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Failed to play Bilbo's Song"));
        assertTrue(result.getMessage().contains("Database error"));
    }
}
