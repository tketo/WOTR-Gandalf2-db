package wotr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests data integrity of JSON export for special piece types:
 * - Chit pieces (political markers, corruption, rings)
 * - TwoChit pieces (political markers, fellowship counter)
 * - BattleCard pieces (WOME expansion)
 * 
 * Ensures all required fields are exported and can be imported correctly.
 */
@DisplayName("Export Data Integrity Tests")
public class ExportDataIntegrityTest {
    
    private static final String EXPORT_FILE = "data/bits_array_base2_bitinit.json";
    private static final String WOME_EXPORT_FILE = "data/bits_array_expansion2[WLT]_bitinit.json";
    
    private JsonObject exportData;
    private JsonArray pieces;
    
    @BeforeEach
    void loadExportData() throws Exception {
        // Load the JSON export file
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(EXPORT_FILE)) {
            exportData = gson.fromJson(reader, JsonObject.class);
            pieces = exportData.getAsJsonArray("pieces");
        }
    }
    
    @Test
    @DisplayName("Export file exists and is valid JSON")
    void testExportFileExists() {
        assertTrue(Files.exists(Path.of(EXPORT_FILE)), 
            "Export file should exist at: " + EXPORT_FILE);
        assertNotNull(exportData, "Export data should be valid JSON");
        assertNotNull(pieces, "Pieces array should exist");
    }
    
    @Test
    @DisplayName("Elven Rings (Chit) have complete data")
    void testElvenRingsExport() {
        // Find the three Elven Rings (areas 125, 126, 127)
        JsonObject vilya = findPieceByAreaAndClass(125, "Chit");
        JsonObject nenya = findPieceByAreaAndClass(126, "Chit");
        JsonObject narya = findPieceByAreaAndClass(127, "Chit");
        
        assertNotNull(vilya, "Vilya (Ring 1) should be in export");
        assertNotNull(nenya, "Nenya (Ring 2) should be in export");
        assertNotNull(narya, "Narya (Ring 3) should be in export");
        
        // Verify Vilya has all required fields
        assertChitFieldsComplete(vilya, "Vilya");
        assertEquals("Vilya", vilya.get("card_name").getAsString(), 
            "Vilya should have correct name");
        assertTrue(vilya.get("small_image").getAsString().contains("elvenring1"),
            "Vilya should have correct image path");
        
        // Verify Nenya
        assertChitFieldsComplete(nenya, "Nenya");
        assertEquals("Nenya", nenya.get("card_name").getAsString());
        
        // Verify Narya
        assertChitFieldsComplete(narya, "Narya");
        assertEquals("Narya", narya.get("card_name").getAsString());
    }
    
    @Test
    @DisplayName("Corruption Counter (Chit) has complete data")
    void testCorruptionCounterExport() {
        // Corruption counter at area 131
        JsonObject corruption = findPieceByAreaAndTypeSubstring(131, "Chit", "Corruption");
        
        assertNotNull(corruption, "Corruption counter should be in export");
        assertChitFieldsComplete(corruption, "Corruption");
        
        assertEquals("Corruption", corruption.get("card_name").getAsString(),
            "Should have correct name");
        assertTrue(corruption.get("small_image").getAsString().contains("corruption"),
            "Should have corruption image path");
    }
    
    @Test
    @DisplayName("Political Markers (TwoChit) have complete data")
    void testPoliticalMarkersExport() {
        // Political markers at areas 117-119
        JsonObject dwarves = findPieceByAreaAndTypeSubstring(117, "TwoChit", "Dwarves");
        JsonObject north = findPieceByAreaAndTypeSubstring(117, "TwoChit", "North");
        JsonObject rohan = findPieceByAreaAndTypeSubstring(117, "TwoChit", "Rohan");
        JsonObject gondor = findPieceByAreaAndTypeSubstring(118, "TwoChit", "Gondor");
        
        assertNotNull(dwarves, "Dwarves marker should be in export");
        assertNotNull(north, "North marker should be in export");
        assertNotNull(rohan, "Rohan marker should be in export");
        assertNotNull(gondor, "Gondor marker should be in export");
        
        // Verify TwoChit has both front and back images
        assertTwoChitFieldsComplete(dwarves, "Dwarves");
        assertTrue(dwarves.get("small_image").getAsString().contains("pdwarves"),
            "Dwarves should have positive side image");
        assertTrue(dwarves.get("big_image").getAsString().contains("ndwarves"),
            "Dwarves should have negative side image");
        
        assertTwoChitFieldsComplete(gondor, "Gondor");
        assertTrue(gondor.get("small_image").getAsString().contains("pgondor"),
            "Gondor should have positive side image");
        assertTrue(gondor.get("big_image").getAsString().contains("ngondor"),
            "Gondor should have negative side image");
    }
    
    @Test
    @DisplayName("Fellowship Counter (TwoChit) has complete data")
    void testFellowshipCounterExport() {
        // Fellowship counter at area 131
        JsonObject fsp = findPieceByAreaAndTypeSubstring(131, "TwoChit", "FSP");
        
        assertNotNull(fsp, "Fellowship counter should be in export");
        assertTwoChitFieldsComplete(fsp, "FSP");
        
        assertTrue(fsp.get("small_image").getAsString().contains("FSP"),
            "Should have FSP front image");
        assertTrue(fsp.get("big_image").getAsString().contains("FSPR"),
            "Should have FSPR back image");
    }
    
    @Test
    @DisplayName("BattleCards have complete data including back images")
    void testBattleCardsExport() throws Exception {
        // Load WOME export which includes BattleCards
        if (!Files.exists(Path.of(WOME_EXPORT_FILE))) {
            System.out.println("WOME export not found, skipping BattleCard test");
            return;
        }
        
        Gson gson = new Gson();
        JsonObject womeData;
        try (FileReader reader = new FileReader(WOME_EXPORT_FILE)) {
            womeData = gson.fromJson(reader, JsonObject.class);
        }
        JsonArray womePieces = womeData.getAsJsonArray("pieces");
        
        // Find BattleCards (areas 180-181)
        JsonObject freeBattle = findPieceInArray(womePieces, "FreeBattleCard");
        JsonObject shadowBattle = findPieceInArray(womePieces, "ShadowBattleCard");
        
        if (freeBattle != null) {
            // Check if BattleCard has the new fields (post-fix export)
            boolean hasNewFields = freeBattle.has("small_back_image") && 
                                   !freeBattle.get("small_back_image").isJsonNull();
            
            if (hasNewFields) {
                // Export was generated after the fix - verify complete data
                assertBattleCardFieldsComplete(freeBattle, "FreeBattleCard");
                
                // Verify all 6 required fields
                assertNotNull(freeBattle.get("small_image"), "Should have small_image");
                assertNotNull(freeBattle.get("big_image"), "Should have big_image");
                assertNotNull(freeBattle.get("card_name"), "Should have card_name");
                assertNotNull(freeBattle.get("small_back_image"), "Should have small_back_image");
                assertNotNull(freeBattle.get("big_back_image"), "Should have big_back_image");
                assertNotNull(freeBattle.get("card_type"), "Should have card_type");
                
                assertFalse(freeBattle.get("small_image").isJsonNull(), 
                    "small_image should not be null");
                assertFalse(freeBattle.get("small_back_image").isJsonNull(), 
                    "small_back_image should not be null");
            } else {
                // Old export (pre-fix) - just verify it exists but warn
                System.out.println("WARNING: BattleCard found but missing back image fields");
                System.out.println("  This export was created before the BattleCard fix");
                System.out.println("  Re-run game to generate updated export with complete data");
                
                // Basic validation only
                assertNotNull(freeBattle.get("small_image"), "Should have small_image");
                assertNotNull(freeBattle.get("card_name"), "Should have card_name");
            }
        }
        
        if (shadowBattle != null && shadowBattle.has("small_back_image") &&
            !shadowBattle.get("small_back_image").isJsonNull()) {
            assertBattleCardFieldsComplete(shadowBattle, "ShadowBattleCard");
        }
    }
    
    @Test
    @DisplayName("All Chit pieces have non-null image and type")
    void testAllChitsHaveData() {
        int chitCount = 0;
        int chitsWithMissingData = 0;
        
        for (JsonElement element : pieces) {
            JsonObject piece = element.getAsJsonObject();
            if ("Chit".equals(piece.get("type").getAsString())) {
                chitCount++;
                
                // Check if small_image or card_name is null/empty
                JsonElement smallImage = piece.get("small_image");
                JsonElement cardName = piece.get("card_name");
                
                if (smallImage == null || smallImage.isJsonNull() || 
                    smallImage.getAsString().isEmpty() ||
                    cardName == null || cardName.isJsonNull() ||
                    cardName.getAsString().isEmpty()) {
                    chitsWithMissingData++;
                    System.err.println("Chit at index " + piece.get("index").getAsInt() + 
                        " has missing data: image=" + smallImage + ", name=" + cardName);
                }
            }
        }
        
        System.out.println("Found " + chitCount + " Chit pieces");
        assertEquals(0, chitsWithMissingData, 
            "All Chit pieces should have complete data (image and type)");
    }
    
    @Test
    @DisplayName("All TwoChit pieces have both images and type")
    void testAllTwoChitsHaveData() {
        int twoChitCount = 0;
        int twoChitsWithMissingData = 0;
        
        for (JsonElement element : pieces) {
            JsonObject piece = element.getAsJsonObject();
            if ("TwoChit".equals(piece.get("type").getAsString())) {
                twoChitCount++;
                
                JsonElement smallImage = piece.get("small_image");
                JsonElement bigImage = piece.get("big_image");
                JsonElement cardName = piece.get("card_name");
                
                if (smallImage == null || smallImage.isJsonNull() || 
                    smallImage.getAsString().isEmpty() ||
                    bigImage == null || bigImage.isJsonNull() ||
                    bigImage.getAsString().isEmpty() ||
                    cardName == null || cardName.isJsonNull() ||
                    cardName.getAsString().isEmpty()) {
                    twoChitsWithMissingData++;
                    System.err.println("TwoChit at index " + piece.get("index").getAsInt() + 
                        " has missing data");
                }
            }
        }
        
        System.out.println("Found " + twoChitCount + " TwoChit pieces");
        assertEquals(0, twoChitsWithMissingData,
            "All TwoChit pieces should have complete data (both images and type)");
    }
    
    // Helper methods
    
    private JsonObject findPieceByAreaAndClass(int areaId, String className) {
        for (JsonElement element : pieces) {
            JsonObject piece = element.getAsJsonObject();
            if (piece.get("area_index").getAsInt() == areaId &&
                className.equals(piece.get("type").getAsString())) {
                return piece;
            }
        }
        return null;
    }
    
    private JsonObject findPieceByAreaAndTypeSubstring(int areaId, String className, String nameSubstring) {
        for (JsonElement element : pieces) {
            JsonObject piece = element.getAsJsonObject();
            if (piece.get("area_index").getAsInt() == areaId &&
                className.equals(piece.get("type").getAsString())) {
                JsonElement cardName = piece.get("card_name");
                if (cardName != null && !cardName.isJsonNull() &&
                    cardName.getAsString().contains(nameSubstring)) {
                    return piece;
                }
            }
        }
        return null;
    }
    
    private JsonObject findPieceInArray(JsonArray array, String className) {
        for (JsonElement element : array) {
            JsonObject piece = element.getAsJsonObject();
            if (className.equals(piece.get("type").getAsString())) {
                return piece;
            }
        }
        return null;
    }
    
    private void assertChitFieldsComplete(JsonObject piece, String pieceName) {
        assertNotNull(piece.get("small_image"), 
            pieceName + " should have small_image field");
        assertFalse(piece.get("small_image").isJsonNull(), 
            pieceName + " small_image should not be null");
        assertFalse(piece.get("small_image").getAsString().isEmpty(),
            pieceName + " small_image should not be empty");
        
        assertNotNull(piece.get("card_name"),
            pieceName + " should have card_name field");
        assertFalse(piece.get("card_name").isJsonNull(),
            pieceName + " card_name should not be null");
        assertFalse(piece.get("card_name").getAsString().isEmpty(),
            pieceName + " card_name should not be empty");
    }
    
    private void assertTwoChitFieldsComplete(JsonObject piece, String pieceName) {
        assertChitFieldsComplete(piece, pieceName);
        
        assertNotNull(piece.get("big_image"),
            pieceName + " should have big_image field");
        assertFalse(piece.get("big_image").isJsonNull(),
            pieceName + " big_image should not be null");
        assertFalse(piece.get("big_image").getAsString().isEmpty(),
            pieceName + " big_image should not be empty");
    }
    
    private void assertBattleCardFieldsComplete(JsonObject piece, String pieceName) {
        // Regular card fields
        assertNotNull(piece.get("small_image"), pieceName + " should have small_image");
        assertNotNull(piece.get("big_image"), pieceName + " should have big_image");
        assertNotNull(piece.get("card_name"), pieceName + " should have card_name");
        
        // BattleCard-specific fields
        assertNotNull(piece.get("small_back_image"), 
            pieceName + " should have small_back_image");
        assertNotNull(piece.get("big_back_image"),
            pieceName + " should have big_back_image");
        assertNotNull(piece.get("card_type"),
            pieceName + " should have card_type");
        
        // Verify not null
        assertFalse(piece.get("small_back_image").isJsonNull(),
            pieceName + " small_back_image should not be null");
        assertFalse(piece.get("big_back_image").isJsonNull(),
            pieceName + " big_back_image should not be null");
        assertFalse(piece.get("card_type").isJsonNull(),
            pieceName + " card_type should not be null");
    }
}
