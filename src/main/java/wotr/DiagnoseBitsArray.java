package wotr;

import java.util.HashMap;
import java.util.Map;

/**
 * Diagnostic utility to compare bits[] array contents for WOME variants
 * to identify missing pieces in database scenarios.
 */
public class DiagnoseBitsArray {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("bits[] Array Diagnostic for WOME Variants");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Test WOME without LOME
        System.out.println("Testing: WOME without LOME (base[WT])");
        System.out.println("-".repeat(80));
        testVariant("base[WT]", false);
        
        System.out.println("\n" + "=".repeat(80) + "\n");
        
        // Test WOME with LOME
        System.out.println("Testing: WOME with LOME (expansion2[WLT])");
        System.out.println("-".repeat(80));
        testVariant("expansion2[WLT]", true);
    }
    
    private static void testVariant(String variantType, boolean hasLOME) {
        try {
            // Create game instance
            Game game = new Game();
            
            // Set variant type
            Game.varianttype = variantType;
            Game.isWOME = true;
            Game.boardtype = "wotr";
            
            // Initialize game (this sizes bits[] and loads from DB)
            game.gameInit();
            
            // Analyze bits[] array
            System.out.println("Array Analysis:");
            System.out.println("  bits[] length: " + game.bits.length);
            System.out.println("  numBits (loaded): " + game.numBits);
            System.out.println("  Missing: " + (game.bits.length - game.numBits));
            System.out.println();
            
            // Count by type
            Map<String, Integer> typeCount = new HashMap<>();
            
            for (int i = 0; i < game.numBits; i++) {
                GamePiece piece = game.bits[i];
                if (piece != null) {
                    String className = piece.getClass().getSimpleName();
                    typeCount.put(className, typeCount.getOrDefault(className, 0) + 1);
                }
            }
            
            System.out.println("Piece Breakdown by Type:");
            typeCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> {
                    System.out.printf("  %-30s : %3d pieces\n", entry.getKey(), entry.getValue());
                });
            
            System.out.println();
            System.out.println("Last 10 Pieces in bits[]:");
            for (int i = Math.max(0, game.numBits - 10); i < game.numBits; i++) {
                GamePiece piece = game.bits[i];
                if (piece != null) {
                    String type = piece.getClass().getSimpleName();
                    String desc = piece.toString();
                    if (desc.length() > 40) {
                        desc = desc.substring(0, 37) + "...";
                    }
                    int areaId = -1;
                    if (piece.currentLocation != null) {
                        for (int a = 0; a < game.areas.length; a++) {
                            if (game.areas[a] == piece.currentLocation) {
                                areaId = a;
                                break;
                            }
                        }
                    }
                    System.out.printf("  [%3d] %-25s %-40s Area: %3d\n", i, type, desc, areaId);
                }
            }
            
            System.out.println();
            System.out.println("Empty slots at end of array:");
            int emptyCount = 0;
            for (int i = game.numBits; i < game.bits.length; i++) {
                if (game.bits[i] == null) {
                    emptyCount++;
                }
            }
            System.out.println("  " + emptyCount + " empty slots (bits[" + game.numBits + "] through bits[" + (game.bits.length - 1) + "])");
            System.out.println();
            
            // Show which scenarios were loaded
            System.out.println("Expected Scenarios for variant '" + variantType + "':");
            if (variantType.startsWith("expansion2")) {
                System.out.println("  - base (428 pieces)");
                if (variantType.contains("[L")) {
                    System.out.println("  - lome (25 pieces expected)");
                }
                if (variantType.contains("W")) {
                    System.out.println("  - wome (111 pieces expected)");
                }
            } else {
                System.out.println("  - base (428 pieces)");
                if (variantType.contains("W")) {
                    System.out.println("  - wome (111 pieces expected)");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error testing variant: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
