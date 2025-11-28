package wotr;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * BoardInit handles initialization of game pieces and areas.
 * Extracted from Game.java to improve code organization.
 */
public class BoardInit {
    private final Game game;
    
    public BoardInit(Game game) {
        this.game = game;
    }
    
    public void bitInit() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0 + 1;
        game.bits[0] = new UnitIsengardRegular(game.areas[176], Messages.getString("Game.1487"), true);
        int j = 0;
        while (j < 5) {
            game.bits[i5] = new UnitIsengardRegular(game.areas[176]);
            j++;
            i5++;
        }
        int i6 = i5 + 1;
        game.bits[i5] = new UnitIsengardElite(game.areas[176]);
        int j2 = 0;
        while (true) {
            i = i6;
            if (j2 >= 28) {
                break;
            }
            i6 = i + 1;
            game.bits[i] = new UnitSauronRegular(game.areas[176]);
            j2++;
        }
        int j3 = 0;
        while (j3 < 2) {
            game.bits[i] = new UnitSauronElite(game.areas[176]);
            j3++;
            i++;
        }
        int j4 = 0;
        while (j4 < 4) {
            game.bits[i] = new UnitNazgul(game.areas[176], 1, 1);
            j4++;
            i++;
        }
        int j5 = 0;
        while (j5 < 14) {
            game.bits[i] = new UnitSouthronRegular(game.areas[176]);
            j5++;
            i++;
        }
        int j6 = 0;
        while (j6 < 3) {
            game.bits[i] = new UnitSouthronElite(game.areas[176]);
            j6++;
            i++;
        }
        int j7 = 0;
        while (j7 < 3) {
            game.bits[i] = new UnitDwarvenRegular(game.areas[174]);
            j7++;
            i++;
        }
        int i7 = i + 1;
        game.bits[i] = new UnitDwarvenElite(game.areas[174]);
        int i8 = i7 + 1;
        game.bits[i7] = new UnitDwarvenLeader(game.areas[174]);
        int j8 = 0;
        while (j8 < 4) {
            game.bits[i8] = new UnitNorthRegular(game.areas[174]);
            j8++;
            i8++;
        }
        int i9 = i8 + 1;
        game.bits[i8] = new UnitNorthElite(game.areas[174]);
        int i10 = i9 + 1;
        game.bits[i9] = new UnitNorthLeader(game.areas[174]);
        int j9 = 0;
        while (j9 < 4) {
            game.bits[i10] = new UnitRohanRegular(game.areas[174]);
            j9++;
            i10++;
        }
        int i11 = i10 + 1;
        game.bits[i10] = new UnitRohanElite(game.areas[174]);
        int i12 = i11 + 1;
        game.bits[i11] = new UnitRohanLeader(game.areas[174]);
        int j10 = 0;
        while (j10 < 9) {
            game.bits[i12] = new UnitGondorRegular(game.areas[174]);
            j10++;
            i12++;
        }
        int i13 = i12 + 1;
        game.bits[i12] = new UnitGondorElite(game.areas[174]);
        int i14 = i13 + 1;
        game.bits[i13] = new UnitGondorLeader(game.areas[174]);
        int j11 = 0;
        while (j11 < 3) {
            game.bits[i14] = new UnitElvenRegular(game.areas[174]);
            j11++;
            i14++;
        }
        int j12 = 0;
        while (j12 < 6) {
            game.bits[i14] = new UnitElvenElite(game.areas[174]);
            j12++;
            i14++;
        }
        int j13 = 0;
        while (j13 < 4) {
            game.bits[i14] = new UnitElvenLeader(game.areas[174]);
            j13++;
            i14++;
        }
        int j14 = 0;
        while (j14 < 7) {
            game.bits[i14] = new ShadowActionDie(game.areas[179]);
            j14++;
            i14++;
        }
        int i15 = i14 + 1;
        game.bits[i14] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp001.png"), Messages.getLanguageLocation("images/cards/fp001.png"), Messages.getString("Game.1490"));
        int i16 = i15 + 1;
        game.bits[i15] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp002.png"), Messages.getLanguageLocation("images/cards/fp002.png"), Messages.getString("Game.1493"));
        int i17 = i16 + 1;
        game.bits[i16] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp003.png"), Messages.getLanguageLocation("images/cards/fp003.png"), Messages.getString("Game.1496"));
        int i18 = i17 + 1;
        game.bits[i17] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp004.png"), Messages.getLanguageLocation("images/cards/fp004.png"), Messages.getString("Game.1499"));
        int i19 = i18 + 1;
        game.bits[i18] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp005.png"), Messages.getLanguageLocation("images/cards/fp005.png"), Messages.getString("Game.1502"));
        int i20 = i19 + 1;
        game.bits[i19] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp006.png"), Messages.getLanguageLocation("images/cards/fp006.png"), Messages.getString("Game.1505"));
        int i21 = i20 + 1;
        game.bits[i20] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp007.png"), Messages.getLanguageLocation("images/cards/fp007.png"), Messages.getString("Game.1508"));
        int i22 = i21 + 1;
        game.bits[i21] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp008.png"), Messages.getLanguageLocation("images/cards/fp008.png"), Messages.getString("Game.1511"));
        int i23 = i22 + 1;
        game.bits[i22] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp009.png"), Messages.getLanguageLocation("images/cards/fp009.png"), Messages.getString("Game.1514"));
        int i24 = i23 + 1;
        game.bits[i23] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp010.png"), Messages.getLanguageLocation("images/cards/fp010.png"), Messages.getString("Game.1517"));
        int i25 = i24 + 1;
        game.bits[i24] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp011.png"), Messages.getLanguageLocation("images/cards/fp011.png"), Messages.getString("Game.1520"));
        int i26 = i25 + 1;
        game.bits[i25] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp012.png"), Messages.getLanguageLocation("images/cards/fp012.png"), Messages.getString("Game.1523"));
        int i27 = i26 + 1;
        game.bits[i26] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp013.png"), Messages.getLanguageLocation("images/cards/fp013.png"), Messages.getString("Game.1526"));
        int i28 = i27 + 1;
        game.bits[i27] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp014.png"), Messages.getLanguageLocation("images/cards/fp014.png"), Messages.getString("Game.1529"));
        int i29 = i28 + 1;
        game.bits[i28] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp015.png"), Messages.getLanguageLocation("images/cards/fp015.png"), Messages.getString("Game.1532"));
        int i30 = i29 + 1;
        game.bits[i29] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp016.png"), Messages.getLanguageLocation("images/cards/fp016.png"), Messages.getString("Game.1535"));
        int i31 = i30 + 1;
        game.bits[i30] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp017.png"), Messages.getLanguageLocation("images/cards/fp017.png"), Messages.getString("Game.1538"));
        int i32 = i31 + 1;
        game.bits[i31] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp018.png"), Messages.getLanguageLocation("images/cards/fp018.png"), Messages.getString("Game.1541"));
        int i33 = i32 + 1;
        game.bits[i32] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp019.png"), Messages.getLanguageLocation("images/cards/fp019.png"), Messages.getString("Game.1544"));
        int i34 = i33 + 1;
        game.bits[i33] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp020.png"), Messages.getLanguageLocation("images/cards/fp020.png"), Messages.getString("Game.1547"));
        int i35 = i34 + 1;
        game.bits[i34] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp021.png"), Messages.getLanguageLocation("images/cards/fp021.png"), Messages.getString("Game.1550"));
        int i36 = i35 + 1;
        game.bits[i35] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp022.png"), Messages.getLanguageLocation("images/cards/fp022.png"), Messages.getString("Game.1553"));
        int i37 = i36 + 1;
        game.bits[i36] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp023.png"), Messages.getLanguageLocation("images/cards/fp023.png"), Messages.getString("Game.1556"));
        int i38 = i37 + 1;
        game.bits[i37] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp024.png"), Messages.getLanguageLocation("images/cards/fp024.png"), Messages.getString("Game.1559"));
        int i39 = i38 + 1;
        game.bits[i38] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp025.png"), Messages.getLanguageLocation("images/cards/fp025.png"), Messages.getString("Game.1562"));
        int i40 = i39 + 1;
        game.bits[i39] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp026.png"), Messages.getLanguageLocation("images/cards/fp026.png"), Messages.getString("Game.1565"));
        int i41 = i40 + 1;
        game.bits[i40] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp027.png"), Messages.getLanguageLocation("images/cards/fp027.png"), Messages.getString("Game.1568"));
        int i42 = i41 + 1;
        game.bits[i41] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp028.png"), Messages.getLanguageLocation("images/cards/fp028.png"), Messages.getString("Game.1571"));
        int i43 = i42 + 1;
        game.bits[i42] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp029.png"), Messages.getLanguageLocation("images/cards/fp029.png"), Messages.getString("Game.1574"));
        int i44 = i43 + 1;
        game.bits[i43] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp030.png"), Messages.getLanguageLocation("images/cards/fp030.png"), Messages.getString("Game.1577"));
        int i45 = i44 + 1;
        game.bits[i44] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp031.png"), Messages.getLanguageLocation("images/cards/fp031.png"), Messages.getString("Game.1580"));
        int i46 = i45 + 1;
        game.bits[i45] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp032.png"), Messages.getLanguageLocation("images/cards/fp032.png"), Messages.getString("Game.1583"));
        int i47 = i46 + 1;
        game.bits[i46] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp033.png"), Messages.getLanguageLocation("images/cards/fp033.png"), Messages.getString("Game.1586"));
        int i48 = i47 + 1;
        game.bits[i47] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp034.png"), Messages.getLanguageLocation("images/cards/fp034.png"), Messages.getString("Game.1589"));
        int i49 = i48 + 1;
        game.bits[i48] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp035.png"), Messages.getLanguageLocation("images/cards/fp035.png"), Messages.getString("Game.1592"));
        int i50 = i49 + 1;
        game.bits[i49] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp036.png"), Messages.getLanguageLocation("images/cards/fp036.png"), Messages.getString("Game.1595"));
        int i51 = i50 + 1;
        game.bits[i50] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp037.png"), Messages.getLanguageLocation("images/cards/fp037.png"), Messages.getString("Game.1598"));
        int i52 = i51 + 1;
        game.bits[i51] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp038.png"), Messages.getLanguageLocation("images/cards/fp038.png"), Messages.getString("Game.1601"));
        int i53 = i52 + 1;
        game.bits[i52] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp039.png"), Messages.getLanguageLocation("images/cards/fp039.png"), Messages.getString("Game.1604"));
        int i54 = i53 + 1;
        game.bits[i53] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp040.png"), Messages.getLanguageLocation("images/cards/fp040.png"), Messages.getString("Game.1607"));
        int i55 = i54 + 1;
        game.bits[i54] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp041.png"), Messages.getLanguageLocation("images/cards/fp041.png"), Messages.getString("Game.1610"));
        int i56 = i55 + 1;
        game.bits[i55] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp042.png"), Messages.getLanguageLocation("images/cards/fp042.png"), Messages.getString("Game.1613"));
        int i57 = i56 + 1;
        game.bits[i56] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp043.png"), Messages.getLanguageLocation("images/cards/fp043.png"), Messages.getString("Game.1616"));
        int i58 = i57 + 1;
        game.bits[i57] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp044.png"), Messages.getLanguageLocation("images/cards/fp044.png"), Messages.getString("Game.1619"));
        int i59 = i58 + 1;
        game.bits[i58] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp045.png"), Messages.getLanguageLocation("images/cards/fp045.png"), Messages.getString("Game.1622"));
        int i60 = i59 + 1;
        game.bits[i59] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp046.png"), Messages.getLanguageLocation("images/cards/fp046.png"), Messages.getString("Game.1625"));
        int i61 = i60 + 1;
        game.bits[i60] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp047.png"), Messages.getLanguageLocation("images/cards/fp047.png"), Messages.getString("Game.1628"));
        int i62 = i61 + 1;
        game.bits[i61] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp048.png"), Messages.getLanguageLocation("images/cards/fp048.png"), Messages.getString("Game.1631"));
        int i63 = i62 + 1;
        game.bits[i62] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp049.png"), Messages.getLanguageLocation("images/cards/fp049.png"), Messages.getString("Game.1634"));
        int i64 = i63 + 1;
        game.bits[i63] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp050.png"), Messages.getLanguageLocation("images/cards/fp050.png"), Messages.getString("Game.1637"));
        int i65 = i64 + 1;
        game.bits[i64] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp051.png"), Messages.getLanguageLocation("images/cards/fp051.png"), Messages.getString("Game.1640"));
        int i66 = i65 + 1;
        game.bits[i65] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp052.png"), Messages.getLanguageLocation("images/cards/fp052.png"), Messages.getString("Game.1643"));
        int i67 = i66 + 1;
        game.bits[i66] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa001.png"), Messages.getLanguageLocation("images/cards/sa001.png"), Messages.getString("Game.1646"));
        int i68 = i67 + 1;
        game.bits[i67] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa002.png"), Messages.getLanguageLocation("images/cards/sa002.png"), Messages.getString("Game.1649"));
        int i69 = i68 + 1;
        game.bits[i68] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa003.png"), Messages.getLanguageLocation("images/cards/sa003.png"), Messages.getString("Game.1652"));
        int i70 = i69 + 1;
        game.bits[i69] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa004.png"), Messages.getLanguageLocation("images/cards/sa004.png"), Messages.getString("Game.1655"));
        int i71 = i70 + 1;
        game.bits[i70] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa005.png"), Messages.getLanguageLocation("images/cards/sa005.png"), Messages.getString("Game.1658"));
        int i72 = i71 + 1;
        game.bits[i71] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa006.png"), Messages.getLanguageLocation("images/cards/sa006.png"), Messages.getString("Game.1661"));
        int i73 = i72 + 1;
        game.bits[i72] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa007.png"), Messages.getLanguageLocation("images/cards/sa007.png"), Messages.getString("Game.1664"));
        int i74 = i73 + 1;
        game.bits[i73] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa008.png"), Messages.getLanguageLocation("images/cards/sa008.png"), Messages.getString("Game.1667"));
        int i75 = i74 + 1;
        game.bits[i74] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa009.png"), Messages.getLanguageLocation("images/cards/sa009.png"), Messages.getString("Game.1670"));
        int i76 = i75 + 1;
        game.bits[i75] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa010.png"), Messages.getLanguageLocation("images/cards/sa010.png"), Messages.getString("Game.1673"));
        int i77 = i76 + 1;
        game.bits[i76] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa011.png"), Messages.getLanguageLocation("images/cards/sa011.png"), Messages.getString("Game.1676"));
        int i78 = i77 + 1;
        game.bits[i77] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa012.png"), Messages.getLanguageLocation("images/cards/sa012.png"), Messages.getString("Game.1679"));
        int i79 = i78 + 1;
        game.bits[i78] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa013.png"), Messages.getLanguageLocation("images/cards/sa013.png"), Messages.getString("Game.1682"));
        int i80 = i79 + 1;
        game.bits[i79] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa014.png"), Messages.getLanguageLocation("images/cards/sa014.png"), Messages.getString("Game.1685"));
        int i81 = i80 + 1;
        game.bits[i80] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa015.png"), Messages.getLanguageLocation("images/cards/sa015.png"), Messages.getString("Game.1688"));
        int i82 = i81 + 1;
        game.bits[i81] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa016.png"), Messages.getLanguageLocation("images/cards/sa016.png"), Messages.getString("Game.1691"));
        int i83 = i82 + 1;
        game.bits[i82] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa017.png"), Messages.getLanguageLocation("images/cards/sa017.png"), Messages.getString("Game.1694"));
        int i84 = i83 + 1;
        game.bits[i83] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa018.png"), Messages.getLanguageLocation("images/cards/sa018.png"), Messages.getString("Game.1697"));
        int i85 = i84 + 1;
        game.bits[i84] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa019.png"), Messages.getLanguageLocation("images/cards/sa019.png"), Messages.getString("Game.1700"));
        int i86 = i85 + 1;
        game.bits[i85] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa020.png"), Messages.getLanguageLocation("images/cards/sa020.png"), Messages.getString("Game.1703"));
        int i87 = i86 + 1;
        game.bits[i86] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa021.png"), Messages.getLanguageLocation("images/cards/sa021.png"), Messages.getString("Game.1706"));
        int i88 = i87 + 1;
        game.bits[i87] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa022.png"), Messages.getLanguageLocation("images/cards/sa022.png"), Messages.getString("Game.1709"));
        int i89 = i88 + 1;
        game.bits[i88] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa023.png"), Messages.getLanguageLocation("images/cards/sa023.png"), Messages.getString("Game.1712"));
        int i90 = i89 + 1;
        game.bits[i89] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa024.png"), Messages.getLanguageLocation("images/cards/sa024.png"), Messages.getString("Game.1715"));
        int i91 = i90 + 1;
        game.bits[i90] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa025.png"), Messages.getLanguageLocation("images/cards/sa025.png"), Messages.getString("Game.1718"));
        int i92 = i91 + 1;
        game.bits[i91] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa026.png"), Messages.getLanguageLocation("images/cards/sa026.png"), Messages.getString("Game.1721"));
        int i93 = i92 + 1;
        game.bits[i92] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa027.png"), Messages.getLanguageLocation("images/cards/sa027.png"), Messages.getString("Game.1724"));
        int i94 = i93 + 1;
        game.bits[i93] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa028.png"), Messages.getLanguageLocation("images/cards/sa028.png"), Messages.getString("Game.1727"));
        int i95 = i94 + 1;
        game.bits[i94] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa029.png"), Messages.getLanguageLocation("images/cards/sa029.png"), Messages.getString("Game.1730"));
        int i96 = i95 + 1;
        game.bits[i95] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa030.png"), Messages.getLanguageLocation("images/cards/sa030.png"), Messages.getString("Game.1733"));
        int i97 = i96 + 1;
        game.bits[i96] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa031.png"), Messages.getLanguageLocation("images/cards/sa031.png"), Messages.getString("Game.1736"));
        int i98 = i97 + 1;
        game.bits[i97] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa032.png"), Messages.getLanguageLocation("images/cards/sa032.png"), Messages.getString("Game.1739"));
        int i99 = i98 + 1;
        game.bits[i98] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa033.png"), Messages.getLanguageLocation("images/cards/sa033.png"), Messages.getString("Game.1742"));
        int i100 = i99 + 1;
        game.bits[i99] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa034.png"), Messages.getLanguageLocation("images/cards/sa034.png"), Messages.getString("Game.1745"));
        int i101 = i100 + 1;
        game.bits[i100] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa035.png"), Messages.getLanguageLocation("images/cards/sa035.png"), Messages.getString("Game.1748"));
        int i102 = i101 + 1;
        game.bits[i101] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa036.png"), Messages.getLanguageLocation("images/cards/sa036.png"), Messages.getString("Game.1751"));
        int i103 = i102 + 1;
        game.bits[i102] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa037.png"), Messages.getLanguageLocation("images/cards/sa037.png"), Messages.getString("Game.1754"));
        int i104 = i103 + 1;
        game.bits[i103] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa038.png"), Messages.getLanguageLocation("images/cards/sa038.png"), Messages.getString("Game.1757"));
        int i105 = i104 + 1;
        game.bits[i104] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa039.png"), Messages.getLanguageLocation("images/cards/sa039.png"), Messages.getString("Game.1760"));
        int i106 = i105 + 1;
        game.bits[i105] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa040.png"), Messages.getLanguageLocation("images/cards/sa040.png"), Messages.getString("Game.1763"));
        int i107 = i106 + 1;
        game.bits[i106] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa041.png"), Messages.getLanguageLocation("images/cards/sa041.png"), Messages.getString("Game.1766"));
        int i108 = i107 + 1;
        game.bits[i107] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa042.png"), Messages.getLanguageLocation("images/cards/sa042.png"), Messages.getString("Game.1769"));
        int i109 = i108 + 1;
        game.bits[i108] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa043.png"), Messages.getLanguageLocation("images/cards/sa043.png"), Messages.getString("Game.1772"));
        int i110 = i109 + 1;
        game.bits[i109] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa044.png"), Messages.getLanguageLocation("images/cards/sa044.png"), Messages.getString("Game.1775"));
        int i111 = i110 + 1;
        game.bits[i110] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa045.png"), Messages.getLanguageLocation("images/cards/sa045.png"), Messages.getString("Game.1778"));
        int i112 = i111 + 1;
        game.bits[i111] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa046.png"), Messages.getLanguageLocation("images/cards/sa046.png"), Messages.getString("Game.1781"));
        int i113 = i112 + 1;
        game.bits[i112] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa047.png"), Messages.getLanguageLocation("images/cards/sa047.png"), Messages.getString("Game.1784"));
        int i114 = i113 + 1;
        game.bits[i113] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa048.png"), Messages.getLanguageLocation("images/cards/sa048.png"), Messages.getString("Game.1787"));
        int i115 = i114 + 1;
        game.bits[i114] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa049.png"), Messages.getLanguageLocation("images/cards/sa049.png"), Messages.getString("Game.1790"));
        int i116 = i115 + 1;
        game.bits[i115] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa050.png"), Messages.getLanguageLocation("images/cards/sa050.png"), Messages.getString("Game.1793"));
        int i117 = i116 + 1;
        game.bits[i116] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa051.png"), Messages.getLanguageLocation("images/cards/sa051.png"), Messages.getString("Game.1796"));
        int i118 = i117 + 1;
        game.bits[i117] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa052.png"), Messages.getLanguageLocation("images/cards/sa052.png"), Messages.getString("Game.1799"));
        int j15 = 0;
        while (j15 < 4) {
            game.bits[i118] = new FreeActionDie(game.areas[178]);
            j15++;
            i118++;
        }
        int i119 = i118 + 1;
        game.bits[i118] = new HuntTile(game.areas[182], "0r", Messages.getLanguageLocation("images/tiles/0r.png"));
        int i120 = i119 + 1;
        game.bits[i119] = new HuntTile(game.areas[182], "0r", Messages.getLanguageLocation("images/tiles/0r.png"));
        int i121 = i120 + 1;
        game.bits[i120] = new HuntTile(game.areas[182], "1r", Messages.getLanguageLocation("images/tiles/1r.png"));
        int i122 = i121 + 1;
        game.bits[i121] = new HuntTile(game.areas[182], "1r", Messages.getLanguageLocation("images/tiles/1r.png"));
        int i123 = i122 + 1;
        game.bits[i122] = new HuntTile(game.areas[182], "2r", Messages.getLanguageLocation("images/tiles/2r.png"));
        int i124 = i123 + 1;
        game.bits[i123] = new HuntTile(game.areas[182], Messages.getString("Game.1810"), Messages.getLanguageLocation("images/tiles/eyer.png"));
        int i125 = i124 + 1;
        game.bits[i124] = new HuntTile(game.areas[182], Messages.getString("Game.1812"), Messages.getLanguageLocation("images/tiles/eyer.png"));
        int i126 = i125 + 1;
        game.bits[i125] = new HuntTile(game.areas[182], Messages.getString("Game.1814"), Messages.getLanguageLocation("images/tiles/eyer.png"));
        int i127 = i126 + 1;
        game.bits[i126] = new HuntTile(game.areas[182], Messages.getString("Game.1816"), Messages.getLanguageLocation("images/tiles/eyer.png"));
        int i128 = i127 + 1;
        game.bits[i127] = new HuntTile(game.areas[182], "1", Messages.getLanguageLocation("images/tiles/1.png"));
        int i129 = i128 + 1;
        game.bits[i128] = new HuntTile(game.areas[182], "1", Messages.getLanguageLocation("images/tiles/1.png"));
        int i130 = i129 + 1;
        game.bits[i129] = new HuntTile(game.areas[182], "2", Messages.getLanguageLocation("images/tiles/2.png"));
        int i131 = i130 + 1;
        game.bits[i130] = new HuntTile(game.areas[182], "2", Messages.getLanguageLocation("images/tiles/2.png"));
        int i132 = i131 + 1;
        game.bits[i131] = new HuntTile(game.areas[182], "3", Messages.getLanguageLocation("images/tiles/3.png"));
        int i133 = i132 + 1;
        game.bits[i132] = new HuntTile(game.areas[182], "3", Messages.getLanguageLocation("images/tiles/3.png"));
        int i134 = i133 + 1;
        game.bits[i133] = new HuntTile(game.areas[182], "3", Messages.getLanguageLocation("images/tiles/3.png"));
        int i135 = i134 + 1;
        game.bits[i134] = new HuntTile(game.areas[182], Messages.getString("Game.1832"), Messages.getLanguageLocation("images/tiles/s.png"));
        int i136 = i135 + 1;
        game.bits[i135] = new HuntTile(game.areas[182], Messages.getString("Game.1834"), Messages.getLanguageLocation("images/tiles/s.png"));
        int i137 = i136 + 1;
        game.bits[i136] = new HuntTile(game.areas[184], Messages.getString("Game.1836"), Messages.getLanguageLocation("images/tiles/b-2.png"));
        int i138 = i137 + 1;
        game.bits[i137] = new HuntTile(game.areas[184], Messages.getString("Game.1838"), Messages.getLanguageLocation("images/tiles/b-1.png"));
        int i139 = i138 + 1;
        game.bits[i138] = new HuntTile(game.areas[184], Messages.getString("Game.1840"), Messages.getLanguageLocation("images/tiles/b0.png"));
        int i140 = i139 + 1;
        game.bits[i139] = new HuntTile(game.areas[184], Messages.getString("Game.1842"), Messages.getLanguageLocation("images/tiles/b0.png"));
        int i141 = i140 + 1;
        game.bits[i140] = new HuntTile(game.areas[184], Messages.getString("Game.1844"), Messages.getLanguageLocation("images/tiles/reyers.png"));
        int i142 = i141 + 1;
        game.bits[i141] = new HuntTile(game.areas[184], Messages.getString("Game.1846"), Messages.getLanguageLocation("images/tiles/rds.png"));
        int i143 = i142 + 1;
        game.bits[i142] = new HuntTile(game.areas[184], Messages.getString("Game.1848"), Messages.getLanguageLocation("images/tiles/r3s.png"));
        int i144 = i143 + 1;
        game.bits[i143] = new HuntTile(game.areas[184], Messages.getString("Game.1850"), Messages.getLanguageLocation("images/tiles/r1rs.png"));
        int i145 = i144 + 1;
        game.bits[i144] = new UnitGandalf(game.areas[116], 1, 1);
        int i146 = i145 + 1;
        game.bits[i145] = new UnitStrider(game.areas[115], 1, 1);
        int i147 = i146 + 1;
        game.bits[i146] = new UnitLegolas(game.areas[115]);
        int i148 = i147 + 1;
        game.bits[i147] = new UnitGimli(game.areas[115]);
        int i149 = i148 + 1;
        game.bits[i148] = new UnitBoromir(game.areas[115]);
        int i150 = i149 + 1;
        game.bits[i149] = new UnitMerry(game.areas[115]);
        int i151 = i150 + 1;
        game.bits[i150] = new UnitPippin(game.areas[115]);
        int i152 = i151 + 1;
        game.bits[i151] = new UnitFellowship(game.areas[27]);
        int i153 = i152 + 1;
        game.bits[i152] = new TwoChit(game.areas[131], Messages.getString("Game.1852"), Messages.getLanguageLocation("images/FSP.png"), Messages.getLanguageLocation("images/FSPR.png"));
        if (game.varianttype.startsWith("expansion2")) {
            int i154 = i153 + 1;
            game.bits[i153] = new TwoChit(game.areas[125], Messages.getString("Game.1855"), "expansion2/images/elvenring1-E.png", "expansion2/images/elvenringback1-E.png");
            int i155 = i154 + 1;
            game.bits[i154] = new TwoChit(game.areas[126], Messages.getString("Game.1857"), "expansion2/images/elvenring2-E.png", "expansion2/images/elvenringback2-E.png");
            game.bits[i155] = new TwoChit(game.areas[127], Messages.getString("Game.1859"), "expansion2/images/elvenring3-E.png", "expansion2/images/elvenringback3-E.png");
            i2 = i155 + 1;
        } else {
            int i156 = i153 + 1;
            game.bits[i153] = new Chit(game.areas[125], Messages.getString("Game.1855"), Messages.getLanguageLocation("images/elvenring1.png"));
            int i157 = i156 + 1;
            game.bits[i156] = new Chit(game.areas[126], Messages.getString("Game.1857"), Messages.getLanguageLocation("images/elvenring2.png"));
            game.bits[i157] = new Chit(game.areas[127], Messages.getString("Game.1859"), Messages.getLanguageLocation("images/elvenring3.png"));
            i2 = i157 + 1;
        }
        int i158 = i2 + 1;
        game.bits[i2] = new Chit(game.areas[131], Messages.getString("Game.1861"), Messages.getLanguageLocation("images/corruption.png"));
        int i159 = i158 + 1;
        game.bits[i158] = new UnitWitchKing(game.areas[176]);
        game.SarumanNo = i159;
        int i160 = i159 + 1;
        game.bits[i159] = new UnitSaruman(game.areas[176]);
        int i161 = i160 + 1;
        game.bits[i160] = new UnitMouth(game.areas[176], 2, 2);
        int i162 = i161 + 1;
        game.bits[i161] = new Chit(game.areas[117], Messages.getString("Game.1863"), Messages.getLanguageLocation("images/nelves.png"));
        int i163 = i162 + 1;
        game.bits[i162] = new TwoChit(game.areas[117], Messages.getString("Game.1865"), Messages.getLanguageLocation("images/pdwarves.png"), Messages.getLanguageLocation("images/ndwarves.png"));
        int i164 = i163 + 1;
        game.bits[i163] = new TwoChit(game.areas[117], Messages.getString("Game.1868"), Messages.getLanguageLocation("images/pnorth.png"), Messages.getLanguageLocation("images/nnorth.png"));
        int i165 = i164 + 1;
        game.bits[i164] = new TwoChit(game.areas[117], Messages.getString("Game.1871"), Messages.getLanguageLocation("images/prohan.png"), Messages.getLanguageLocation("images/nrohan.png"));
        int i166 = i165 + 1;
        game.bits[i165] = new TwoChit(game.areas[118], Messages.getString("Game.1874"), Messages.getLanguageLocation("images/pgondor.png"), Messages.getLanguageLocation("images/ngondor.png"));
        int i167 = i166 + 1;
        game.bits[i166] = new Chit(game.areas[118], Messages.getString("Game.1877"), Messages.getLanguageLocation("images/nsouthron.png"));
        int i168 = i167 + 1;
        game.bits[i167] = new Chit(game.areas[119], Messages.getString("Game.1879"), Messages.getLanguageLocation("images/nsauron.png"));
        int i169 = i168 + 1;
        game.bits[i168] = new Chit(game.areas[119], Messages.getString("Game.1881"), Messages.getLanguageLocation("images/nisengard.png"));
        int j16 = 0;
        while (true) {
            i3 = i169;
            if (j16 >= 4) {
                break;
            }
            i169 = i3 + 1;
            game.bits[i3] = new UnitElvenElite(game.areas[174]);
            j16++;
        }
        int j17 = 0;
        while (j17 < 2) {
            game.bits[i3] = new UnitElvenRegular(game.areas[174]);
            j17++;
            i3++;
        }
        int j18 = 0;
        while (j18 < 3) {
            game.bits[i3] = new UnitGondorLeader(game.areas[174]);
            j18++;
            i3++;
        }
        int j19 = 0;
        while (j19 < 4) {
            game.bits[i3] = new UnitGondorElite(game.areas[174]);
            j19++;
            i3++;
        }
        int j20 = 0;
        while (j20 < 6) {
            game.bits[i3] = new UnitGondorRegular(game.areas[174]);
            j20++;
            i3++;
        }
        int j21 = 0;
        while (j21 < 3) {
            game.bits[i3] = new UnitRohanLeader(game.areas[174]);
            j21++;
            i3++;
        }
        int j22 = 0;
        while (j22 < 4) {
            game.bits[i3] = new UnitRohanElite(game.areas[174]);
            j22++;
            i3++;
        }
        int j23 = 0;
        while (j23 < 6) {
            game.bits[i3] = new UnitRohanRegular(game.areas[174]);
            j23++;
            i3++;
        }
        int j24 = 0;
        while (j24 < 3) {
            game.bits[i3] = new UnitNorthLeader(game.areas[174]);
            j24++;
            i3++;
        }
        int j25 = 0;
        while (j25 < 4) {
            game.bits[i3] = new UnitNorthElite(game.areas[174]);
            j25++;
            i3++;
        }
        int j26 = 0;
        while (j26 < 6) {
            game.bits[i3] = new UnitNorthRegular(game.areas[174]);
            j26++;
            i3++;
        }
        int j27 = 0;
        while (j27 < 3) {
            game.bits[i3] = new UnitDwarvenLeader(game.areas[174]);
            j27++;
            i3++;
        }
        int j28 = 0;
        while (j28 < 4) {
            game.bits[i3] = new UnitDwarvenElite(game.areas[174]);
            j28++;
            i3++;
        }
        int j29 = 0;
        while (j29 < 2) {
            game.bits[i3] = new UnitDwarvenRegular(game.areas[174]);
            j29++;
            i3++;
        }
        int j30 = 0;
        while (j30 < 4) {
            game.bits[i3] = new UnitNazgul(game.areas[176], 1, 1);
            j30++;
            i3++;
        }
        int j31 = 0;
        while (j31 < 5) {
            game.bits[i3] = new UnitIsengardElite(game.areas[176]);
            j31++;
            i3++;
        }
        int j32 = 0;
        while (j32 < 6) {
            game.bits[i3] = new UnitIsengardRegular(game.areas[176]);
            j32++;
            i3++;
        }
        int j33 = 0;
        while (j33 < 4) {
            game.bits[i3] = new UnitSauronElite(game.areas[176]);
            j33++;
            i3++;
        }
        int j34 = 0;
        while (j34 < 8) {
            game.bits[i3] = new UnitSauronRegular(game.areas[176]);
            j34++;
            i3++;
        }
        int j35 = 0;
        while (j35 < 3) {
            game.bits[i3] = new UnitSouthronElite(game.areas[176]);
            j35++;
            i3++;
        }
        int j36 = 0;
        while (j36 < 10) {
            game.bits[i3] = new UnitSouthronRegular(game.areas[176]);
            j36++;
            i3++;
        }
        int i170 = i3 + 1;
        game.bits[i3] = new FreeActionDie(game.areas[174]);
        int i171 = i170 + 1;
        game.bits[i170] = new FreeActionDie(game.areas[174]);
        int i172 = i171 + 1;
        game.bits[i171] = new ShadowActionDie(game.areas[176]);
        int i173 = i172 + 1;
        game.bits[i172] = new ShadowActionDie(game.areas[176]);
        int i174 = i173 + 1;
        game.bits[i173] = new ShadowActionDie(game.areas[176]);
        int i175 = i174 + 1;
        game.bits[i174] = new UnitGaladriel(game.areas[174]);
        int i176 = i175 + 1;
        game.bits[i175] = new UnitSmeagol(game.areas[174]);
        int i177 = i176 + 1;
        game.bits[i176] = new UnitCotR(game.areas[176], 2, 2);
        int i178 = i177 + 1;
        game.bits[i177] = new UnitBalrog(game.areas[176]);
        int j37 = 0;
        while (true) {
            i4 = i178;
            if (j37 >= 6) {
                break;
            }
            i178 = i4 + 1;
            game.bits[i4] = new UnitTower(game.areas[176]);
            j37++;
        }
        int j38 = 0;
        while (j38 < 6) {
            game.bits[i4] = new UnitTrebuchet(game.areas[174]);
            j38++;
            i4++;
        }
        int j39 = 0;
        while (j39 < 6) {
            game.bits[i4] = new UnitCorsair(game.areas[176]);
            j39++;
            i4++;
        }
        int j40 = 0;
        while (j40 < 12) {
            game.bits[i4] = new UnitDunlending(game.areas[176]);
            j40++;
            i4++;
        }
        int j41 = 0;
        while (j41 < 8) {
            game.bits[i4] = new UnitEnt(game.areas[174]);
            j41++;
            i4++;
        }
        int j42 = 0;
        while (j42 < 6) {
            game.bits[i4] = new UnitFreeControlLarge(game.areas[174]);
            j42++;
            i4++;
        }
        int j43 = 0;
        while (j43 < 10) {
            game.bits[i4] = new UnitShadowControlLarge(game.areas[176]);
            j43++;
            i4++;
        }
        int i179 = i4 + 1;
        game.bits[i4] = new ShadowCharacterCard(game.areas[185], Messages.getLanguageLocation("images/smallcards/backWSGI.png"), Messages.getLanguageLocation("images/cards/WSGI.png"), Messages.getString("Game.18"));
        int i180 = i179 + 1;
        game.bits[i179] = new Chit(game.areas[163], Messages.getString("Game.13"), Messages.getLanguageLocation("images/vp_free.png"));
        game.bits[i180] = new TwoChit(game.areas[163], Messages.getString("Game.17"), Messages.getLanguageLocation("images/vp_shadow.png"), Messages.getLanguageLocation("images/vp_shadowreverse.png"));
        game.numBits = i180 + 1;
    }

    public void areaInit() {
        game.areas[0] = new Area(Messages.getKeyString("Game.1883"));
        game.areaIDs[0] = 0;
        game.areas[1] = new Area(Messages.getKeyString("Game.1884"));
        game.areaIDs[1] = 819;
        game.areas[2] = new Area(Messages.getKeyString("Game.1885"));
        game.areaIDs[2] = 1638;
        game.areas[3] = new Area(Messages.getKeyString("Game.1886"));
        game.areaIDs[3] = 2457;
        game.areas[4] = new Area(Messages.getKeyString("Game.1887"));
        game.areaIDs[4] = 2730;
        game.areas[5] = new Area(Messages.getKeyString("Game.1888"));
        game.areaIDs[5] = 3276;
        game.areas[6] = new Area(Messages.getKeyString("Game.1889"));
        game.areas[6].tall = true;
        game.areaIDs[6] = 4095;
        game.areas[7] = new Area(Messages.getKeyString("Game.1890"));
        game.areaIDs[7] = 3;
        game.areas[7].setTerrain("FreeNorthCity");
        game.areas[8] = new Area(Messages.getKeyString("Game.1891"));
        game.areaIDs[8] = 6;
        game.areas[8].setTerrain("FreeDwarvesTown");
        game.areas[9] = new Area(Messages.getKeyString("Game.1892"));
        game.areaIDs[9] = 9;
        game.areas[10] = new Area(Messages.getKeyString("Game.1893"));
        game.areaIDs[10] = 10;
        game.areas[34] = new Area(Messages.getKeyString("Game.1894"));
        game.areaIDs[34] = 250;
        game.areas[11] = new Area(Messages.getKeyString("Game.1895"));
        game.areaIDs[11] = 15;
        game.areas[12] = new Area(Messages.getKeyString("Game.1896"));
        game.areaIDs[12] = 48;
        game.areas[13] = new Area(Messages.getKeyString("Game.1897"));
        game.areaIDs[13] = 96;
        game.areas[14] = new Area(Messages.getKeyString("Game.1898"));
        game.areaIDs[14] = 144;
        game.areas[14].setTerrain("ShadowSauronCity");
        game.areas[15] = new Area(Messages.getKeyString("Game.1899"));
        game.areaIDs[15] = 160;
        game.areas[16] = new Area(Messages.getKeyString("Game.1900"));
        game.areaIDs[16] = 192;
        game.areas[17] = new Area(Messages.getKeyString("Game.1901"));
        game.areaIDs[17] = 240;
        game.areas[18] = new Area(Messages.getKeyString("Game.1902"));
        game.areaIDs[18] = 768;
        game.areas[18].setTerrain("FreeNorthTown");
        game.areas[19] = new Area(Messages.getKeyString("Game.1903"));
        game.areaIDs[19] = 1536;
        game.areas[20] = new Area(Messages.getKeyString("Game.1904"));
        game.areaIDs[20] = 2304;
        game.areas[21] = new Area(Messages.getKeyString("Game.1905"));
        game.areaIDs[21] = 2560;
        game.areas[22] = new Area(Messages.getKeyString("Game.1906"));
        game.areaIDs[22] = 3072;
        game.areas[23] = new Area(Messages.getKeyString("Game.1907"));
        game.areaIDs[23] = 3840;
        game.areas[24] = new Area(Messages.getKeyString("Game.1908"));
        game.areaIDs[24] = 51;
        game.areas[25] = new Area(Messages.getKeyString("Game.1909"));
        game.areaIDs[25] = 54;
        game.areas[26] = new Area(Messages.getKeyString("Game.1910"));
        game.areaIDs[26] = 57;
        game.areas[26].setTerrain("ShadowIsengardTown");
        game.areas[27] = new Area(Messages.getKeyString("Game.1911"));
        game.areaIDs[27] = 58;
        game.areas[28] = new Area(Messages.getKeyString("Game.1912"));
        game.areaIDs[28] = 60;
        game.areas[29] = new Area(Messages.getKeyString("Game.1913"));
        game.areaIDs[29] = 63;
        game.areas[30] = new Area(Messages.getKeyString("Game.1914"));
        game.areaIDs[30] = 99;
        game.areas[30].setTerrain("ShadowIsengardTown");
        game.areas[31] = new Area(Messages.getKeyString("Game.1915"));
        game.areas[31].tall = true;
        game.areaIDs[31] = 147;
        game.areas[32] = new Area(Messages.getKeyString("Game.1916"));
        game.areas[32].tall = true;
        game.areaIDs[32] = 163;
        game.areas[35] = new Area(Messages.getKeyString("Game.1917"));
        game.areaIDs[35] = 195;
        game.areas[41] = new Area(Messages.getKeyString("Game.1918"));
        game.areaIDs[41] = 243;
        game.areas[41].setTerrain("FreeFortification");
        game.areas[40] = new Area(Messages.getKeyString("Game.1919"));
        game.areaIDs[40] = 102;
        game.areas[42] = new Area(Messages.getKeyString("Game.1920"));
        game.areaIDs[42] = 105;
        game.areas[39] = new Area(Messages.getKeyString("Game.1921"));
        game.areaIDs[39] = 106;
        game.areas[44] = new Area(Messages.getKeyString("Game.1922"));
        game.areaIDs[44] = 108;
        game.areas[45] = new Area(Messages.getKeyString("Game.1923"));
        game.areaIDs[45] = 111;
        game.areas[46] = new Area(Messages.getKeyString("Game.1924"));
        game.areaIDs[46] = 150;
        game.areas[46].setTerrain("FreeGondorTown");
        game.areas[47] = new Area(Messages.getKeyString("Game.1925"));
        game.areaIDs[47] = 166;
        game.areas[47].setTerrain("FreeRohanTown");
        game.areas[33] = new Area(Messages.getKeyString("Game.1926"));
        game.areas[33].tall = true;
        game.areaIDs[33] = 198;
        game.areas[36] = new Area(Messages.getKeyString("Game.1927"));
        game.areaIDs[36] = 156;
        game.areas[37] = new Area(Messages.getKeyString("Game.1928"));
        game.areaIDs[37] = 153;
        game.areas[43] = new Area(Messages.getKeyString("Game.1929"));
        game.areaIDs[43] = 154;
        game.areas[43].setTerrain("FreeRohanCity");
        game.areas[38] = new Area(Messages.getKeyString("Game.1930"));
        game.areaIDs[38] = 246;
        game.areas[48] = new Area(Messages.getKeyString("Game.1931"));
        game.areaIDs[48] = 159;
        game.areas[48].setTerrain("FreeGondorCity");
        game.areas[49] = new Area(Messages.getKeyString("Game.1932"));
        game.areaIDs[49] = 169;
        game.areas[50] = new Area(Messages.getKeyString("Game.1933"));
        game.areas[50].tall = true;
        game.areaIDs[50] = 201;
        game.areas[50].setTerrain("FreeRohanTown");
        game.areas[51] = new Area(Messages.getKeyString("Game.1934"));
        game.areas[51].tall = true;
        game.areaIDs[51] = 249;
        game.areas[52] = new Area(Messages.getKeyString("Game.1935"));
        game.areaIDs[52] = 170;
        game.areas[52].setTerrain("FreeGondorTown");
        game.areas[53] = new Area(Messages.getKeyString("Game.1936"));
        game.areas[53].tall = true;
        game.areaIDs[53] = 172;
        game.areas[54] = new Area(Messages.getKeyString("Game.1937"));
        game.areas[54].tall = true;
        game.areaIDs[54] = 175;
        game.areas[55] = new Area(Messages.getKeyString("Game.1938"));
        game.areaIDs[55] = 202;
        game.areas[56] = new Area(Messages.getKeyString("Game.1939"));
        game.areaIDs[56] = 12;
        game.areas[57] = new Area(Messages.getKeyString("Game.1940"));
        game.areas[57].tall = true;
        game.areaIDs[57] = 204;
        game.areas[58] = new Area(Messages.getKeyString("Game.1941"));
        game.areas[58].tall = true;
        game.areaIDs[58] = 207;
        game.areas[58].setTerrain("FreeNorthTown");
        game.areas[59] = new Area(Messages.getKeyString("Game.1942"));
        game.areaIDs[59] = 252;
        game.areas[73] = new Area(Messages.getKeyString("Game.1943"));
        game.areas[73].tall = true;
        game.areaIDs[73] = 255;
        game.areas[73].setTerrain("FreeFortification");
        game.areas[74] = new Area(Messages.getKeyString("Game.1944"));
        game.areaIDs[74] = 816;
        game.areas[60] = new Area(Messages.getKeyString("Game.1945"));
        game.areas[60].tall = true;
        game.areaIDs[60] = 864;
        game.areas[61] = new Area(Messages.getKeyString("Game.1946"));
        game.areaIDs[61] = 912;
        game.areas[61].setTerrain("ShadowSETown");
        game.areas[62] = new Area(Messages.getKeyString("Game.1947"));
        game.areaIDs[62] = 928;
        game.areas[67] = new Area(Messages.getKeyString("Game.1948"));
        game.areaIDs[67] = 960;
        game.areas[64] = new Area(Messages.getKeyString("Game.1949"));
        game.areaIDs[64] = 1008;
        game.areas[65] = new Area(Messages.getKeyString("Game.1950"));
        game.areaIDs[65] = 1584;
        game.areas[66] = new Area(Messages.getKeyString("Game.1951"));
        game.areaIDs[66] = 2352;
        game.areas[68] = new Area(Messages.getKeyString("Game.1952"));
        game.areaIDs[68] = 2608;
        game.areas[69] = new Area(Messages.getKeyString("Game.1953"));
        game.areas[69].tall = true;
        game.areaIDs[69] = 3120;
        game.areas[70] = new Area(Messages.getKeyString("Game.1954"));
        game.areaIDs[70] = 1696;
        game.areas[75] = new Area(Messages.getKeyString("Game.1955"));
        game.areas[75].tall = true;
        game.areaIDs[75] = 1632;
        game.areas[63] = new Area(Messages.getKeyString("Game.1956"));
        game.areaIDs[63] = 1680;
        game.areas[76] = new Area(Messages.getKeyString("Game.1957"));
        game.areaIDs[76] = 3888;
        game.areas[77] = new Area(Messages.getKeyString("Game.1958"));
        game.areaIDs[77] = 1728;
        game.areas[71] = new Area(Messages.getKeyString("Game.1959"));
        game.areaIDs[71] = 1776;
        game.areas[78] = new Area(Messages.getKeyString("Game.1960"));
        game.areaIDs[78] = 2400;
        game.areas[79] = new Area(Messages.getKeyString("Game.1961"));
        game.areaIDs[79] = 2656;
        game.areas[80] = new Area(Messages.getKeyString("Game.1962"));
        game.areas[80].tall = true;
        game.areaIDs[80] = 3168;
        game.areas[81] = new Area(Messages.getKeyString("Game.1963"));
        game.areaIDs[81] = 3936;
        game.areas[72] = new Area(Messages.getKeyString("Game.1964"));
        game.areas[72].tall = true;
        game.areaIDs[72] = 2448;
        game.areas[82] = new Area(Messages.getKeyString("Game.1965"));
        game.areaIDs[82] = 2464;
        game.areas[83] = new Area(Messages.getKeyString("Game.1966"));
        game.areaIDs[83] = 2496;
        game.areas[84] = new Area(Messages.getKeyString("Game.1967"));
        game.areaIDs[84] = 2544;
        game.areas[85] = new Area(Messages.getKeyString("Game.1968"));
        game.areaIDs[85] = 2704;
        game.areas[85].setTerrain("ShadowSECity");
        game.areas[86] = new Area(Messages.getKeyString("Game.1969"));
        game.areas[86].tall = true;
        game.areaIDs[86] = 3216;
        game.areas[87] = new Area(Messages.getKeyString("Game.1970"));
        game.areaIDs[87] = 3984;
        game.areas[87].setTerrain("FreeNorthCity");
        game.areas[88] = new Area(Messages.getKeyString("Game.1971"));
        game.areaIDs[88] = 2720;
        game.areas[88].setTerrain("ShadowSauronTown");
        game.areas[89] = new Area(Messages.getKeyString("Game.1972"));
        game.areaIDs[89] = 2752;
        game.areas[90] = new Area(Messages.getKeyString("Game.1973"));
        game.areaIDs[90] = 2800;
        game.areas[90].setTerrain("FreeDwarvesTown");
        game.areas[91] = new Area(Messages.getKeyString("Game.1974"));
        game.areaIDs[91] = 3232;
        game.areas[92] = new Area(Messages.getKeyString("Game.1975"));
        game.areaIDs[92] = 4000;
        game.areas[93] = new Area(Messages.getKeyString("Game.1976"));
        game.areaIDs[93] = 3264;
        game.areas[94] = new Area(Messages.getKeyString("Game.1977"));
        game.areaIDs[94] = 3312;
        game.areas[95] = new Area(Messages.getKeyString("Game.1978"));
        game.areaIDs[95] = 4032;
        game.areas[96] = new Area(Messages.getKeyString("Game.1979"));
        game.areaIDs[96] = 4080;
        game.areas[97] = new Area(Messages.getKeyString("Game.1980"));
        game.areaIDs[97] = 771;
        game.areas[98] = new Area(Messages.getKeyString("Game.1981"));
        game.areaIDs[98] = 1539;
        game.areas[99] = new Area(Messages.getKeyString("Game.1982"));
        game.areaIDs[99] = 2307;
        game.areas[100] = new Area(Messages.getKeyString("Game.1983"));
        game.areaIDs[100] = 2563;
        game.areas[101] = new Area(Messages.getKeyString("Game.1984"));
        game.areaIDs[101] = 3075;
        game.areas[102] = new Area(Messages.getKeyString("Game.1985"));
        game.areaIDs[102] = 3843;
        game.areas[103] = new Area(Messages.getKeyString("Game.1986"));
        game.areaIDs[103] = 774;
        game.areas[103].setTerrain("ShadowSETown");
        game.areas[104] = new Area(Messages.getKeyString("Game.1987"));
        game.areaIDs[104] = 777;
        game.areas[104].setTerrain("ShadowSETown");
        game.areas[105] = new Area(Messages.getKeyString("Game.1988"));
        game.areaIDs[105] = 778;
        game.areas[106] = new Area(Messages.getKeyString("Game.1989"));
        game.areaIDs[106] = 780;
        game.areas[107] = new Area(Messages.getKeyString("Game.1990"));
        game.areaIDs[107] = 783;
        game.areas[108] = new Area(Messages.getKeyString("Game.1991"));
        game.areaIDs[108] = 1542;
        game.areas[109] = new Area(Messages.getKeyString("Game.1992"));
        game.areaIDs[109] = 2310;
        game.areas[110] = new Area(Messages.getKeyString("Game.1993"));
        game.areaIDs[110] = 2566;
        game.areas[111] = new Area(Messages.getKeyString("Game.1994"));
        game.areaIDs[111] = 3078;
        game.areas[112] = new Area(Messages.getKeyString("Game.1995"));
        game.areaIDs[112] = 3846;
        game.areas[113] = new Area(Messages.getKeyString("Game.1996"));
        game.areas[114] = new MultiBuffet(Messages.getKeyString("Game.1997"), new Point[][]{new Point[]{new Point(54, -25), new Point(18, -25), new Point(-18, -25), new Point(-54, -25), new Point(35, 18), new Point(0, 18), new Point(-35, 18)}, new Point[]{new Point(40, -25), new Point(0, -25), new Point(-40, -25), new Point(35, 18), new Point(0, 18), new Point(-35, 18)}, new Point[]{new Point(35, -25), new Point(0, -25), new Point(-35, -25), new Point(-18, 18), new Point(18, 18)}, new Point[]{new Point(35, -22), new Point(0, -22), new Point(-35, -22), new Point(0, 18)}, new Point[]{new Point(18, -20), new Point(-18, -20), new Point(0, 18)}, new Point[]{new Point(18, -5), new Point(-18, -5)}, new Point[]{new Point(0, -5)}});
        game.areaIDs[114] = 1546;
        game.areas[115] = new Buffet(Messages.getKeyString("Game.1998"), new Point[]{new Point(-16, -30), new Point(30, -30), new Point(-25, 0), new Point(21, 0), new Point(-16, 30), new Point(30, 30)});
        game.areaIDs[115] = 1548;
        game.areas[116] = new Area(Messages.getKeyString("Game.1999"));
        game.areaIDs[116] = 1551;
        Point[] p3 = {new Point(-16, -16), new Point(16, -16), new Point(-16, 16), new Point(16, 16), new Point(0, 0), new Point(-32, 0), new Point(0, 32), new Point(0, -32)};
        game.areas[117] = new Buffet(Messages.getKeyString("Game.2000"), p3);
        game.areaIDs[117] = 2313;
        game.areas[118] = new Buffet(Messages.getKeyString("Game.2001"), p3);
        game.areaIDs[118] = 2569;
        game.areas[119] = new Buffet(Messages.getKeyString("Game.2002"), p3);
        game.areaIDs[119] = 3081;
        game.areas[120] = new Buffet(Messages.getKeyString("Game.2003"), p3);
        game.areaIDs[120] = 3849;
        game.areas[121] = new Deck(Messages.getKeyString("Game.2004"));
        game.areaIDs[121] = 2314;
        game.areas[122] = new Deck(Messages.getKeyString("Game.2005"));
        game.areaIDs[122] = 2316;
        game.areas[123] = new Deck(Messages.getKeyString("Game.2006"));
        game.areaIDs[123] = 2319;
        game.areas[124] = new Deck(Messages.getKeyString("Game.2007"));
        game.areaIDs[124] = 2570;
        game.areas[125] = new Area(Messages.getKeyString("Game.2008"));
        game.areaIDs[125] = 3082;
        game.areas[126] = new Area(Messages.getKeyString("Game.2009"));
        game.areaIDs[126] = 3850;
        game.areas[127] = new Area(Messages.getKeyString("Game.2010"));
        game.areaIDs[127] = 2572;
        game.areas[128] = new Area(Messages.getKeyString("Game.2011"));
        game.areaIDs[128] = 2575;
        game.areas[129] = new Area(Messages.getKeyString("Game.2012"));
        game.areaIDs[129] = 3084;
        game.areas[130] = new Area(Messages.getKeyString("Game.2013"));
        game.areaIDs[130] = 3852;
        Point[] trackpts = {new Point(0, 0), new Point(0, 18)};
        game.areas[131] = new Buffet(Messages.getKeyString("Game.2014"), trackpts);
        game.areaIDs[131] = 3087;
        game.areas[132] = new Buffet(Messages.getKeyString("Game.2015"), trackpts);
        game.areaIDs[132] = 3855;
        game.areas[133] = new Buffet(Messages.getKeyString("Game.2016"), trackpts);
        game.areaIDs[133] = 1587;
        game.areas[134] = new Buffet(Messages.getKeyString("Game.2017"), trackpts);
        game.areaIDs[134] = 2355;
        game.areas[135] = new Buffet(Messages.getKeyString("Game.2018"), trackpts);
        game.areaIDs[135] = 2611;
        game.areas[136] = new Buffet(Messages.getKeyString("Game.2019"), trackpts);
        game.areaIDs[136] = 3123;
        game.areas[137] = new Buffet(Messages.getKeyString("Game.2020"), trackpts);
        game.areaIDs[137] = 3891;
        game.areas[138] = new Buffet(Messages.getKeyString("Game.2021"), trackpts);
        game.areaIDs[138] = 1635;
        game.areas[139] = new Buffet(Messages.getKeyString("Game.2022"), trackpts);
        game.areaIDs[139] = 2403;
        game.areas[140] = new Buffet(Messages.getKeyString("Game.2023"), trackpts);
        game.areaIDs[140] = 2659;
        game.areas[141] = new Buffet(Messages.getKeyString("Game.2024"), trackpts);
        game.areaIDs[141] = 3171;
        game.areas[142] = new Buffet(Messages.getKeyString("Game.2025"), trackpts);
        game.areaIDs[142] = 3939;
        game.areas[143] = new Buffet(Messages.getKeyString("Game.2026"), trackpts);
        game.areaIDs[143] = 2451;
        game.areas[144] = new Area(Messages.getKeyString("Game.2027"));
        game.areaIDs[144] = 3279;
        game.areas[145] = new Area(Messages.getKeyString("Game.2028"));
        game.areas[146] = new Area(Messages.getKeyString("Game.2029"));
        game.areaIDs[146] = 1641;
        game.areas[147] = new Area(Messages.getKeyString("Game.2030"));
        game.areaIDs[147] = 1642;
        game.areas[148] = new Area(Messages.getKeyString("Game.2031"));
        game.areaIDs[148] = 1644;
        game.areas[149] = new Area(Messages.getKeyString("Game.2032"));
        game.areaIDs[149] = 1647;
        game.areas[150] = new Area(Messages.getKeyString("Game.2033"));
        game.areaIDs[150] = 1686;
        game.areas[151] = new Area(Messages.getKeyString("Game.2034"));
        game.areaIDs[151] = 1702;
        game.areas[152] = new Buffet(Messages.getKeyString("Game.2035"), new Point[]{new Point(0, 0), new Point(-66, 0), new Point(66, 0)});
        game.areaIDs[152] = 1782;
        game.areas[153] = new Area(Messages.getKeyString("Game.2036"));
        game.areaIDs[153] = 2406;
        game.areas[154] = new Area(Messages.getKeyString("Game.2037"));
        game.areaIDs[154] = 2662;
        game.areas[155] = new Area(Messages.getKeyString("Game.19"));
        game.areaIDs[155] = 1545;
        game.areas[156] = new Area(Messages.getKeyString("Game.20"));
        game.areaIDs[156] = 1734;
        game.areas[157] = new Area(Messages.getKeyString("Game.21"));
        game.areaIDs[157] = 1287;
        game.areas[158] = new Area(Messages.getKeyString("Game.22"));
        game.areaIDs[158] = 3316;
        game.areas[159] = new Area(Messages.getKeyString("Game.23"));
        game.areaIDs[159] = 3303;
        game.areas[160] = new Area(Messages.getKeyString("Game.24"));
        game.areaIDs[160] = 3028;
        game.areas[161] = new Area(Messages.getKeyString("Game.25"));
        game.areaIDs[161] = 2755;
        game.areas[162] = new Area(Messages.getKeyString("Game.26"));
        game.areaIDs[162] = 2483;
        Point[] markerpoints = {new Point(0, 0), new Point(0, 18)};
        game.areas[163] = new Buffet(Messages.getKeyString("Game.27"), markerpoints);
        game.areaIDs[163] = 2287;
        game.areas[164] = new Buffet(Messages.getKeyString("Game.28"), markerpoints);
        game.areaIDs[164] = 1767;
        game.areas[165] = new Buffet(Messages.getKeyString("Game.29"), markerpoints);
        game.areaIDs[165] = 1485;
        game.areas[166] = new Buffet(Messages.getKeyString("Game.30"), markerpoints);
        game.areaIDs[166] = 711;
        game.areas[167] = new Buffet(Messages.getKeyString("Game.31"), markerpoints);
        game.areaIDs[167] = 956;
        game.areas[168] = new Buffet(Messages.getKeyString("Game.32"), markerpoints);
        game.areaIDs[168] = 3499;
        game.areas[169] = new Buffet(Messages.getKeyString("Game.33"), markerpoints);
        game.areaIDs[169] = 921;
        game.areas[170] = new Buffet(Messages.getKeyString("Game.388"), markerpoints);
        game.areaIDs[170] = 3458;
        game.areas[171] = new Buffet(Messages.getKeyString("Game.389"), markerpoints);
        game.areaIDs[171] = 2918;
        game.areas[172] = new Buffet(Messages.getKeyString("Game.391"), markerpoints);
        game.areaIDs[172] = 1368;
        game.areas[173] = new Buffet(Messages.getKeyString("Game.392"), markerpoints);
        game.areaIDs[173] = 1331;
        game.areas[186] = new Area("Northern Noman Lands");
        game.areaIDs[186] = 3265;
        game.areas[187] = new Area("Southern Noman Lands");
        game.areaIDs[187] = 2993;
        game.areas[188] = new Area(Messages.getKeyString("Game.524"));
        game.areaIDs[188] = 865;
        game.areas[189] = new Area(Messages.getKeyString("Game.525"));
        game.areaIDs[189] = 2128;
        game.areas[190] = new Area(Messages.getKeyString("Game.528"));
        game.areaIDs[190] = 1948;
        game.areas[191] = new Area(Messages.getKeyString("Game.529"));
        game.areaIDs[191] = 1338;
        game.areas[192] = new Area(Messages.getKeyString("Game.532"));
        game.areaIDs[192] = 1064;
        game.areas[193] = new Area(Messages.getKeyString("Game.533"));
        game.areaIDs[193] = 806;
        if (Game.isWOME.booleanValue()) {
            game.areas[194] = new Deck(Messages.getString("Game.868"));
            game.areaIDs[194] = 3456;
            game.areas[195] = new Deck(Messages.getString("Game.871"));
            game.areaIDs[195] = 3440;
            game.areas[196] = new Deck(Messages.getString("Game.872"));
            game.areaIDs[196] = 3408;
            game.areas[197] = new Deck(Messages.getString("Game.876"));
            game.areaIDs[197] = 3424;
            game.areas[198] = new Deck(Messages.getString("Game.879"));
            game.areaIDs[198] = 3200;
            game.areas[199] = new Deck(Messages.getString("Game.884"));
            game.areaIDs[199] = 3184;
            game.areas[200] = new Deck(Messages.getString("Game.885"));
            game.areaIDs[200] = 3273;
            game.areas[201] = new Deck(Messages.getString("Game.890"));
            game.areaIDs[201] = 3088;
            game.areas[204] = new Area(Messages.getString("Game.862"));
            game.areaIDs[204] = 1975;
            game.areas[205] = new Area(Messages.getString("Game.863"));
            game.areaIDs[205] = 2233;
            game.areas[206] = new Area(Messages.getString("Game.866"));
            game.areaIDs[206] = 2249;
            game.areas[207] = new Area(Messages.getString("Game.893"));
            game.areaIDs[207] = 2521;
            game.areas[208] = new Area(Messages.getString("Game.897"));
            game.areaIDs[208] = 2536;
            game.areas[209] = new Area(Messages.getString("Game.903"));
            game.areaIDs[209] = 2296;
            game.areas[210] = new Area(Messages.getString("Game.921"));
            game.areaIDs[210] = 2552;
            game.areas[211] = new Area(Messages.getString("Game.927"));
            game.areaIDs[211] = 2559;
        }
        game.areaCoords[0] = new Point(305, 308);
        game.areaCoords[1] = new Point(280, 229);
        game.areaCoords[2] = new Point(369, 224);
        game.areaCoords[3] = new Point(176, 230);
        game.areaCoords[4] = new Point(215, 164);
        game.areaCoords[5] = new Point(173, 111);
        game.areaCoords[6] = new Point(261, 197);
        game.areaCoords[7] = new Point(304, 152);
        game.areaCoords[8] = new Point(235, 110);
        game.areaCoords[9] = new Point(347, 180);
        game.areaCoords[10] = new Point(247, 78);
        game.areaCoords[34] = new Point(576, 174);
        game.areaCoords[11] = new Point(349, 355);
        game.areaCoords[12] = new Point(324, 39);
        game.areaCoords[13] = new Point(347, 133);
        game.areaCoords[14] = new Point(361, 17);
        game.areaCoords[15] = new Point(350, 88);
        game.areaCoords[16] = new Point(383, 308);
        game.areaCoords[17] = new Point(425, 71);
        game.areaCoords[18] = new Point(390, 117);
        game.areaCoords[19] = new Point(436, 34);
        game.areaCoords[20] = new Point(429, 120);
        game.areaCoords[21] = new Point(369, 436);
        game.areaCoords[22] = new Point(426, 180);
        game.areaCoords[23] = new Point(337, 489);
        game.areaCoords[24] = new Point(462, 134);
        game.areaCoords[25] = new Point(430, 497);
        game.areaCoords[26] = new Point(461, 264);
        game.areaCoords[27] = new Point(506, 100);
        game.areaCoords[28] = new Point(470, 216);
        game.areaCoords[29] = new Point(512, 476);
        game.areaCoords[30] = new Point(451, 316);
        game.areaCoords[31] = new Point(489, 160);
        game.areaCoords[32] = new Point(415, 375);
        game.areaCoords[35] = new Point(526, 200);
        game.areaCoords[41] = new Point(470, 404);
        game.areaCoords[40] = new Point(482, 366);
        game.areaCoords[42] = new Point(541, 536);
        game.areaCoords[39] = new Point(528, 315);
        game.areaCoords[44] = new Point(486, 34);
        game.areaCoords[45] = new Point(501, 426);
        game.areaCoords[46] = new Point(586, 489);
        game.areaCoords[47] = new Point(564, 384);
        game.areaCoords[33] = new Point(518, 143);
        game.areaCoords[36] = new Point(582, 216);
        game.areaCoords[37] = new Point(554, 244);
        game.areaCoords[43] = new Point(578, 427);
        game.areaCoords[38] = new Point(580, 296);
        game.areaCoords[48] = new Point(616, 555);
        game.areaCoords[49] = new Point(598, 654);
        game.areaCoords[50] = new Point(612, 420);
        game.areaCoords[51] = new Point(556, 78);
        game.areaCoords[52] = new Point(636, 513);
        game.areaCoords[53] = new Point(555, 127);
        game.areaCoords[54] = new Point(603, 351);
        game.areaCoords[55] = new Point(650, 610);
        game.areaCoords[56] = new Point(293, 81);
        game.areaCoords[57] = new Point(587, 134);
        game.areaCoords[58] = new Point(607, 98);
        game.areaCoords[59] = new Point(646, 436);
        game.areaCoords[73] = new Point(691, 523);
        game.areaCoords[74] = new Point(667, 478);
        game.areaCoords[60] = new Point(620, 160);
        game.areaCoords[61] = new Point(743, 628);
        game.areaCoords[62] = new Point(638, 60);
        game.areaCoords[67] = new Point(663, 364);
        game.areaCoords[64] = new Point(625, 207);
        game.areaCoords[65] = new Point(640, 107);
        game.areaCoords[66] = new Point(657, 308);
        game.areaCoords[68] = new Point(722, 598);
        game.areaCoords[69] = new Point(610, 257);
        game.areaCoords[70] = new Point(685, 123);
        game.areaCoords[75] = new Point(725, 528);
        game.areaCoords[63] = new Point(655, 250);
        game.areaCoords[76] = new Point(678, 154);
        game.areaCoords[77] = new Point(662, 187);
        game.areaCoords[71] = new Point(692, 400);
        game.areaCoords[78] = new Point(714, 299);
        game.areaCoords[79] = new Point(693, 193);
        game.areaCoords[80] = new Point(685, 70);
        game.areaCoords[81] = new Point(696, 250);
        game.areaCoords[72] = new Point(728, 434);
        game.areaCoords[82] = new Point(736, 69);
        game.areaCoords[83] = new Point(733, 195);
        game.areaCoords[84] = new Point(725, 345);
        game.areaCoords[85] = new Point(820, 627);
        game.areaCoords[86] = new Point(775, 481);
        game.areaCoords[87] = new Point(742, 132);
        game.areaCoords[88] = new Point(809, 555);
        game.areaCoords[89] = new Point(770, 308);
        game.areaCoords[90] = new Point(807, 62);
        game.areaCoords[91] = new Point(783, 421);
        game.areaCoords[92] = new Point(753, 245);
        game.areaCoords[93] = new Point(785, 369);
        game.areaCoords[94] = new Point(813, 518);
        game.areaCoords[95] = new Point(822, 591);
        game.areaCoords[96] = new Point(861, 421);
        game.areaCoords[97] = new Point(791, 148);
        game.areaCoords[98] = new Point(839, 363);
        game.areaCoords[99] = new Point(785, 197);
        game.areaCoords[100] = new Point(816, 297);
        game.areaCoords[101] = new Point(905, 191);
        game.areaCoords[102] = new Point(814, 235);
        game.areaCoords[103] = new Point(897, 349);
        game.areaCoords[104] = new Point(853, 183);
        game.areaCoords[105] = new Point(26, 162);
        game.areaCoords[106] = new Point(26, 218);
        game.areaCoords[107] = new Point(26, 272);
        game.areaCoords[108] = new Point(26, 326);
        game.areaCoords[109] = new Point(26, 382);
        game.areaCoords[110] = new Point(26, 436);
        game.areaCoords[111] = new Point(26, 488);
        game.areaCoords[112] = new Point(26, 543);
        game.areaCoords[113] = new Point(30, 551);
        game.areaCoords[114] = new Point(68, 631);
        game.areaCoords[115] = new Point(898, 70);
        game.areaCoords[116] = new Point(972, 70);
        game.areaCoords[117] = new Point(960, 231);
        game.areaCoords[118] = new Point(960, 305);
        game.areaCoords[119] = new Point(960, 377);
        game.areaCoords[120] = new Point(960, 450);
        game.areaCoords[121] = new Point(36, 85);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[121] = new Point(30, 69);
        }
        game.areaCoords[122] = new Point(113, 85);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[122] = new Point(80, 69);
        }
        game.areaCoords[123] = new Point(883, 643);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[123] = new Point(868, 655);
        }
        game.areaCoords[124] = new Point(959, 643);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[124] = new Point(918, 655);
        }
        game.areaCoords[125] = new Point(223, 23);
        game.areaCoords[126] = new Point(245, 23);
        game.areaCoords[127] = new Point(266, 23);
        game.areaCoords[128] = new Point(783, 662);
        game.areaCoords[129] = new Point(804, 662);
        game.areaCoords[130] = new Point(825, 662);
        game.areaCoords[131] = new Point(574, 22);
        game.areaCoords[132] = new Point(596, 22);
        game.areaCoords[133] = new Point(617, 22);
        game.areaCoords[134] = new Point(639, 22);
        game.areaCoords[135] = new Point(661, 22);
        game.areaCoords[136] = new Point(682, 22);
        game.areaCoords[137] = new Point(704, 22);
        game.areaCoords[138] = new Point(726, 22);
        game.areaCoords[139] = new Point(748, 22);
        game.areaCoords[140] = new Point(769, 22);
        game.areaCoords[141] = new Point(790, 22);
        game.areaCoords[142] = new Point(812, 22);
        game.areaCoords[143] = new Point(834, 22);
        game.areaCoords[144] = new Point(468, 600);
        game.areaCoords[145] = new Point(101, 548);
        game.areaCoords[146] = new Point(174, 345);
        game.areaCoords[147] = new Point(174, 383);
        game.areaCoords[148] = new Point(174, 420);
        game.areaCoords[149] = new Point(174, 458);
        game.areaCoords[150] = new Point(174, 495);
        game.areaCoords[151] = new Point(174, 533);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[146] = new Point(154, 320);
            game.areaCoords[147] = new Point(154, 358);
            game.areaCoords[148] = new Point(154, 395);
            game.areaCoords[149] = new Point(154, 433);
            game.areaCoords[150] = new Point(154, 471);
            game.areaCoords[151] = new Point(154, 509);
        }
        game.areaCoords[152] = new Point(279, 613);
        game.areaCoords[153] = new Point(205, 613);
        game.areaCoords[154] = new Point(355, 613);
        game.areaCoords[155] = new Point(82, 166);
        game.areaCoords[156] = new Point(82, 219);
        game.areaCoords[157] = new Point(82, 274);
        game.areaCoords[158] = new Point(82, 328);
        game.areaCoords[159] = new Point(82, 383);
        game.areaCoords[160] = new Point(82, 437);
        game.areaCoords[161] = new Point(82, 492);
        game.areaCoords[162] = new Point(82, 545);
        game.areaCoords[163] = new Point(187, 662);
        game.areaCoords[164] = new Point(210, 662);
        game.areaCoords[165] = new Point(231, 662);
        game.areaCoords[166] = new Point(253, 662);
        game.areaCoords[167] = new Point(275, 662);
        game.areaCoords[168] = new Point(297, 662);
        game.areaCoords[169] = new Point(318, 662);
        game.areaCoords[170] = new Point(340, 662);
        game.areaCoords[171] = new Point(361, 662);
        game.areaCoords[172] = new Point(383, 662);
        game.areaCoords[173] = new Point(404, 662);
        game.areaCoords[186] = new Point(722, 281);
        game.areaCoords[187] = new Point(754, 319);
        game.areaCoords[188] = new Point(849, 523);
        game.areaCoords[189] = new Point(883, 524);
        game.areaCoords[190] = new Point(911, 505);
        game.areaCoords[191] = new Point(904, 472);
        game.areaCoords[192] = new Point(875, 455);
        game.areaCoords[193] = new Point(844, 464);
        if (Game.isWOME.booleanValue()) {
            game.areaCoords[194] = new Point(128, 69);
            game.areaCoords[195] = new Point(176, 69);
            game.areaCoords[196] = new Point(128, 100);
            game.areaCoords[197] = new Point(128, 17);
            game.areaCoords[198] = new Point(968, 655);
            game.areaCoords[199] = new Point(968, 561);
            game.areaCoords[200] = new Point(968, 671);
            game.areaCoords[201] = new Point(968, 593);
            game.areaCoords[204] = new Point(154, 547);
            game.areaCoords[205] = new Point(241, 320);
            game.areaCoords[206] = new Point(241, 358);
            game.areaCoords[207] = new Point(241, 395);
            game.areaCoords[208] = new Point(241, 433);
            game.areaCoords[209] = new Point(241, 471);
            game.areaCoords[210] = new Point(241, 509);
            game.areaCoords[211] = new Point(241, 547);
        }
        game.areas[174] = new Area(Messages.getKeyString("Game.2038"), true, false, true);
        game.areas[175] = new Area(Messages.getKeyString("Game.2039"), true, false, true);
        game.areas[176] = new Area(Messages.getKeyString("Game.2040"), true, false, true);
        game.areas[177] = new Area(Messages.getKeyString("Game.2041"), true, false, true);
        game.areas[178] = new Buffet(Messages.getKeyString("Game.2042"), new Point[]{new Point(-85, -17), new Point(-51, -17), new Point(-17, -17), new Point(17, -17), new Point(51, -17), new Point(85, -17), new Point(-85, 17), new Point(-51, 17), new Point(-17, 17), new Point(17, 17), new Point(51, 17), new Point(85, 17)});
        game.areas[179] = new Buffet(Messages.getKeyString("Game.2043"), new Point[]{new Point(-85, -17), new Point(-51, -17), new Point(-17, -17), new Point(17, -17), new Point(51, -17), new Point(85, -17), new Point(-85, 17), new Point(-51, 17), new Point(-17, 17), new Point(17, 17), new Point(51, 17), new Point(85, 17)});
        game.areas[182] = new Area(Messages.getKeyString("Game.2044"));
        game.areas[183] = new Area(Messages.getKeyString("Game.2045"));
        game.areas[184] = new Area(Messages.getKeyString("Game.2046"));
        for (int i = game.FP_REINFORCEMENTS; i <= game.SA_CASUALTIES; i++) {
            game.areas[i].selectme = false;
        }
        game.areas[185] = new Area("Spare");
        for (int i2 = 0; i2 <= game.areaCoords.length - 1; i2++) {
            game.areaChits[i2] = game.areaCoords[i2];
            game.areaControlPoints[i2] = game.areaCoords[i2];
        }
        game.areaControlPoints[18] = new Point(383, 106);
        game.areaControlPoints[7] = new Point(300, 145);
        game.areaControlPoints[4] = new Point(218, 157);
        game.areaControlPoints[14] = new Point(384, 16);
        game.areaControlPoints[44] = new Point(493, 21);
        game.areaControlPoints[27] = new Point(497, 88);
        game.areaControlPoints[35] = new Point(535, 184);
        game.areaControlPoints[37] = new Point(548, 232);
        game.areaControlPoints[58] = new Point(606, 92);
        game.areaControlPoints[82] = new Point(735, 52);
        game.areaControlPoints[90] = new Point(804, 59);
        game.areaControlPoints[70] = new Point(677, 110);
        game.areaControlPoints[87] = new Point(734, 127);
        game.areaControlPoints[104] = new Point(854, 182);
        game.areaControlPoints[63] = new Point(663, 241);
        game.areaControlPoints[26] = new Point(461, 262);
        game.areaControlPoints[30] = new Point(452, 307);
        game.areaControlPoints[40] = new Point(473, 352);
        game.areaControlPoints[103] = new Point(882, 330);
        game.areaControlPoints[45] = new Point(492, 414);
        game.areaControlPoints[47] = new Point(564, 381);
        game.areaControlPoints[43] = new Point(571, 422);
        game.areaControlPoints[50] = new Point(610, 404);
        game.areaControlPoints[91] = new Point(792, 413);
        game.areaControlPoints[96] = new Point(872, 409);
        game.areaControlPoints[86] = new Point(783, 472);
        game.areaControlPoints[74] = new Point(655, 467);
        game.areaControlPoints[46] = new Point(584, 488);
        game.areaControlPoints[42] = new Point(533, 521);
        game.areaControlPoints[52] = new Point(636, 508);
        game.areaControlPoints[48] = new Point(608, 553);
        game.areaControlPoints[88] = new Point(807, 554);
        game.areaControlPoints[49] = new Point(582, 656);
        game.areaControlPoints[61] = new Point(697, 668);
        game.areaControlPoints[85] = new Point(820, 620);
    }

    public void addObserver(ObserverHost observerHost) {
        game.observersExist = true;
        game.observers.add(observerHost);
    }

    public void removeObserver(ObserverHost observerHost) {
        game.observers.remove(observerHost);
        if (game.observers.size() == 0) {
            game.observersExist = false;
        }
    }

    public void sendToObservers(String cmd, ObserverHost source) {
        if (game.observersExist && cmd != null && !Messages.getString("Game.2047").equals(cmd)) {
            for (ObserverHost host : game.observers) {
                if (!host.equals(source)) {
                    host.enqueue(cmd);
                }
            }
        }
    }

    public void sendAsObserver(String cmd) {
        if (game.observerClient.connected && cmd != null) {
            game.observerClient.enqueue(cmd);
        }
    }

    public void disconnectAllObservers() {
        for (int i = game.observers.size() - 1; i >= 0; i--) {
            if (game.observers.get(i).connected) {
                game.observers.get(i).disconnect(false);
            }
        }
        if (game.observerClient.connected) {
            game.observerClient.disconnect(false);
        }
    }

    public void stopBrightObjects() {
        game.interpreter.cardBrighterTimer = false;
        game.interpreter.balrogBrighterTimer = false;
        game.interpreter.witchkingBrighterTimer = false;
        game.interpreter.preHuntTimer = false;
        game.interpreter.diceTimer = false;
        for (int i = 0; i <= game.bits.length - 1; i++) {
            GamePiece gp = game.bits[i];
            if (gp instanceof Brightness) {
                ((Brightness) gp).makeDull();
            }
            if (gp instanceof NaryaDice) {
                ((NaryaDice) gp).setVisible();
            }
            if (gp instanceof NenyaDice) {
                ((NenyaDice) gp).setVisible();
            }
            if (gp instanceof VilyaDice) {
                ((VilyaDice) gp).setVisible();
            }
            if (gp instanceof BalrogDice) {
                ((BalrogDice) gp).setVisible();
            }
            if (gp instanceof GothmogDice) {
                ((GothmogDice) gp).setVisible();
            }
        }
    }

    public void newBase2(String action) {
        game.boardtype = "wotr";
        game.varianttype = "base2";
        game.board = Messages.getLanguageLocation("images/board.jpg");
        game.overlay = Messages.getLanguageLocation("images/map_mask.png");
        game.boardtype = "wotr";
        if (action.equals("base2[T]")) {
            game.varianttype = "base2[T]";
        } else {
            game.varianttype = "base2";
        }
        game.gameInit();
        game.interpreter.setLogFile(game.generateLogName());
        game.zoomBoard(Game.prefs.zoom);
        game.gameloading = true;
        if ( game.piecesLoadedFromDatabase == false ) {
            resetBoard();
            baseSetup();
        
            int i = game.numBits;
            if (game.varianttype.endsWith("T]")) {
                game.bits[i] = new UnitTreebeard(game.areas[174]);
                i++;
            }
            game.numBits = i;
            game.interpreter.execute("$base silent ");
            game.interpreter.execute("$TEST<~Scribe.141~>312 <~Scribe.142~><~Game.2038~><~Scribe.143~><~Game.1965~><~Scribe.144~>");
            game.interpreter.execute("$base noisy ");
        }
        game.history = new ArrayList<>();
        game.historyactions = new ArrayList<>();
        game.historyactionpointer = 0;
        game.historypointer = 0;
        game.gameloading = false;
        game.interpreter.record("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.408") + "[" + game.versionno + " " + game.boardtype + " " + game.varianttype + "]");
    }

    public void AnonymizeDecks() {
        game.setFPCcards(new ArrayList<>());
        game.setAbstractFPCcards(new ArrayList<>());
        int i = 0;
        Iterator<GamePiece> it = game.areas[121].pieces.iterator();
        while (it.hasNext()) {
            GamePiece p = it.next();
            game.getFPCcards().add((FreeCharacterCard) p);
            FreeCharacterCard f = new FreeCharacterCard((Area) null, i);
            f.CopyCardHiddenDataFrom((Card) p);
            ((Card) p).DeleteHiddenData();
            ((Card) p).SetIndex(i);
            game.getAbstractFPCcards().add(f);
            i++;
        }
        game.setFPScards(new ArrayList<>());
        game.setAbstractFPScards(new ArrayList<>());
        int i2 = 0;
        Iterator<GamePiece> it2 = game.areas[122].pieces.iterator();
        while (it2.hasNext()) {
            GamePiece p2 = it2.next();
            game.getFPScards().add((FreeStrategyCard) p2);
            FreeStrategyCard f2 = new FreeStrategyCard((Area) null, i2);
            f2.CopyCardHiddenDataFrom((Card) p2);
            ((Card) p2).DeleteHiddenData();
            ((Card) p2).SetIndex(i2);
            game.getAbstractFPScards().add(f2);
            i2++;
        }
        game.setAbstractFPFcards(new ArrayList<>());
        if (Game.isWOME.booleanValue()) {
            game.setFPFcards(new ArrayList<>());
            int i3 = 0;
            Iterator<GamePiece> it3 = game.areas[194].pieces.iterator();
            while (it3.hasNext()) {
                GamePiece p3 = it3.next();
                game.getFPFcards().add((FreeFactionCard) p3);
                FreeFactionCard f3 = new FreeFactionCard((Area) null, i3);
                f3.CopyCardHiddenDataFrom((Card) p3);
                ((Card) p3).DeleteHiddenData();
                ((Card) p3).SetIndex(i3);
                game.getAbstractFPFcards().add(f3);
                i3++;
            }
        }
        game.setSPCcards(new ArrayList<>());
        game.setAbstractSPCcards(new ArrayList<>());
        int i4 = 0;
        Iterator<GamePiece> it4 = game.areas[123].pieces.iterator();
        while (it4.hasNext()) {
            GamePiece p4 = it4.next();
            game.getSPCcards().add((ShadowCharacterCard) p4);
            ShadowCharacterCard f4 = new ShadowCharacterCard((Area) null, i4);
            f4.CopyCardHiddenDataFrom((Card) p4);
            ((Card) p4).DeleteHiddenData();
            ((Card) p4).SetIndex(i4);
            game.getAbstractSPCcards().add(f4);
            i4++;
        }
        game.setSPScards(new ArrayList<>());
        game.setAbstractSPScards(new ArrayList<>());
        int i5 = 0;
        Iterator<GamePiece> it5 = game.areas[124].pieces.iterator();
        while (it5.hasNext()) {
            GamePiece p5 = it5.next();
            game.getSPScards().add((ShadowStrategyCard) p5);
            ShadowStrategyCard f5 = new ShadowStrategyCard((Area) null, i5);
            f5.CopyCardHiddenDataFrom((Card) p5);
            ((Card) p5).DeleteHiddenData();
            ((Card) p5).SetIndex(i5);
            game.getAbstractSPScards().add(f5);
            i5++;
        }
        game.setAbstractSPFcards(new ArrayList<>());
        if (Game.isWOME.booleanValue()) {
            game.setSPFcards(new ArrayList<>());
            int i6 = 0;
            Iterator<GamePiece> it6 = game.areas[198].pieces.iterator();
            while (it6.hasNext()) {
                GamePiece p6 = it6.next();
                game.getSPFcards().add((ShadowFactionCard) p6);
                ShadowFactionCard f6 = new ShadowFactionCard((Area) null, i6);
                f6.CopyCardHiddenDataFrom((Card) p6);
                ((Card) p6).DeleteHiddenData();
                ((Card) p6).SetIndex(i6);
                game.getAbstractSPFcards().add(f6);
                i6++;
            }
        }
        // OBSOLETE: _GenericCards removed (Gondor scenario)
        // game._GenericCards = new ArrayList<>();
        // game._abstractGenericCards = new ArrayList<>();
    }

    public void baseSetup() {
        game.controls.chat.silent = true;
       
        game.interpreter.execute("<auto> silent ");
        game.interpreter.execute("<auto><~Scribe.141~>363 397 398 402 401 400 399 403 404 376 375 374 373 377 378 <~Scribe.142~><~Game.2038~><~Scribe.143~>Spare<~Scribe.144~>");
        game.interpreter.execute("<auto><~Scribe.141~>365 366 379 380 381 382 383 384 385 386 387 388 389 390 391 392 393 394 395 396 367 368 369 370 371 372 <~Scribe.142~><~Game.2040~><~Scribe.143~>Spare<~Scribe.144~>");
        if (!game.varianttype.startsWith("expansion2")) {
            game.interpreter.execute("<auto><~Scribe.141~>230 231 <~Scribe.142~><~Game.2044~><~Scribe.143~>Spare<~Scribe.144~>");
        }
        game.interpreter.execute("<auto><~Scribe.141~>155 152 <~Scribe.142~><~Game.2004~><~Scribe.143~>Spare<~Scribe.144~>");
        game.interpreter.execute("<auto><~Scribe.141~>137 151 <~Scribe.142~><~Game.2005~><~Scribe.143~>Spare<~Scribe.144~>");
        game.interpreter.execute("<auto><~Scribe.141~>161 198 <~Scribe.142~><~Game.2006~><~Scribe.143~>Spare<~Scribe.144~>");
        game.interpreter.execute("<auto><~Scribe.141~>163 188 <~Scribe.142~><~Game.2007~><~Scribe.143~>Spare<~Scribe.144~>");
        ((UnitSmeagol) game.bits[364]).defaultSide();
        if (!game.varianttype.startsWith("expansion2")) {
            game.interpreter.execute("<auto><~Controls.323~>364 ");
        }
        game.interpreter.execute("<auto> noisy ");
        game.controls.chat.silent = false;
    }

    public void newWOME(String action) {
        game.boardtype = "wotr";
        game.isWOME = true;
        game.board = Messages.getLanguageLocation("images/board-w.jpg");
        game.overlay = Messages.getLanguageLocation("images/map_mask_w.png");
        if (action.contains("[L")) {
            game.varianttype = "expansion2[WLT]";  // WOME + LOME
        } else {
            game.varianttype = "base[WT]";  // WOME only (base + wome scenarios)
        }
        game.gameInit();
        game.interpreter.setLogFile(game.generateLogName());
        game.gameloading = true;
        if ( game.piecesLoadedFromDatabase == false ) {
            resetBoard();
            baseSetup();
            game.interpreter.execute("$base silent ");
            game.interpreter.execute("<auto><~Scribe.141~>312 <~Scribe.142~><~Game.2038~><~Scribe.143~><~Game.1965~><~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>240 <~Scribe.142~><~Game.1999~><~Scribe.143~><~Game.1998~><~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>123 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>115 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>127 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>136 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>141 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>144 <~Scribe.142~><~Game.2014~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>185 <~Scribe.142~><~Game.1999~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>191 <~Scribe.142~><~Game.1999~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("<auto><~Scribe.141~>195 <~Scribe.142~><~Game.1999~><~Scribe.143~>Spare<~Scribe.144~>");
            game.interpreter.execute("$base noisy ");
            bitWOMEinit();
            if (action.contains("[L")) {
                // WOME + LOME variant - also load LOME pieces
                bitLOMEinit();
            }
        }
        game.zoomBoard(game.prefs.zoom);
        game.history = new ArrayList<>();
        game.historyactions = new ArrayList<>();
        game.historyactionpointer = 0;
        game.historypointer = 0;
        game.gameloading = false;
        game.interpreter.record("<game> " + game.prefs.nick + Messages.getKeyString("Controls.408") + "[" + game.versionno + " " + game.boardtype + " " + game.varianttype + "]");
    }

    public void newLOME(String action) {
        game.boardtype = "wotr";
        game.varianttype = "expansion2";
        game.board = Messages.getLanguageLocation("images/board.jpg");
        game.overlay = Messages.getLanguageLocation("images/map_mask.png");
        game.boardtype = "wotr";
        if (action.endsWith("T]")) {
            game.varianttype = "expansion2[LT]";
        } else {
            game.varianttype = "expansion2[L]";
        }
        game.gameInit();
        game.interpreter.setLogFile(game.generateLogName());
        game.gameloading = true;
        if ( game.piecesLoadedFromDatabase == false ) {
            resetBoard();
            baseSetup();
            game.interpreter.execute("$base silent ");
            game.interpreter.execute("$TEST<~Scribe.141~>312 <~Scribe.142~><~Game.2038~><~Scribe.143~><~Game.1965~><~Scribe.144~>");
            game.interpreter.execute("$base noisy ");
            bitLOMEinit();
        }
        
        game.zoomBoard(game.prefs.zoom);
        game.history = new ArrayList<>();
        game.historyactions = new ArrayList<>();
        game.historyactionpointer = 0;
        game.historypointer = 0;
        game.gameloading = false;
        game.interpreter.record("<game> " + game.prefs.nick + Messages.getKeyString("Controls.408") + "[" + game.versionno + " " + game.boardtype + " " + game.varianttype + "]");
    }
    
    public void bitLOMEinit() {
        int i = game.numBits;
        game.interpreter.execute("$base silent ");
        int i2 = i + 1;
        game.bits[i] = new NaryaDice(game.areas[174], game);
        int i3 = i2 + 1;
        game.bits[i2] = new NenyaDice(game.areas[174], game);
        int i4 = i3 + 1;
        game.bits[i3] = new VilyaDice(game.areas[174], game);
        int i5 = i4 + 1;
        game.bits[i4] = new UnitElrond(game.areas[174]);
        Area swaplocation = null;
        int i1 = 0;
        while (true) {
            if (i1 >= game.bits.length) {
                break;
            } else if (game.bits[i1] instanceof UnitGaladriel) {
                swaplocation = game.bits[i1].currentLocation();
                break;
            } else {
                i1++;
            }
        }
        game.interpreter.execute("<auto><~Controls.281~>" + i1 + " <~Controls.282~>" + swaplocation + "<~Controls.283~>" + game.areas[174]);
        int i12 = 0;
        while (true) {
            if (i12 >= game.bits.length) {
                break;
            } else if (game.bits[i12] instanceof UnitBalrog) {
                swaplocation = game.bits[i12].currentLocation();
                break;
            } else {
                i12++;
            }
        }
        game.interpreter.execute("<auto><~Controls.281~>" + i12 + " <~Controls.282~>" + swaplocation + "<~Controls.283~>" + game.areas[176]);
        int i13 = 0;
        while (true) {
            if (i13 >= game.bits.length) {
                break;
            } else if (game.bits[i13] instanceof UnitCotR) {
                swaplocation = game.bits[i13].currentLocation();
                ((UnitCotR) game.bits[i13]).setLeadership(1);
                break;
            } else {
                i13++;
            }
        }
        game.interpreter.execute("<auto><~Controls.281~>" + i13 + " <~Controls.282~>" + swaplocation + "<~Controls.283~>" + game.areas[176]);
        int i6 = i5 + 1;
        game.bits[i5] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp00a.png"), Messages.getLanguageLocation("images/cards/fp00a.png"), Messages.getString("Game.407"));
        int i7 = i6 + 1;
        game.bits[i6] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp00b.png"), Messages.getLanguageLocation("images/cards/fp00b.png"), Messages.getString("Game.409"));
        int i8 = i7 + 1;
        game.bits[i7] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp00c.png"), Messages.getLanguageLocation("images/cards/fp00c.png"), Messages.getString("Game.410"));
        int i9 = i8 + 1;
        game.bits[i8] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("images/smallcards/fp00d.png"), Messages.getLanguageLocation("images/cards/fp00d.png"), Messages.getString("Game.412"));
        int i10 = i9 + 1;
        game.bits[i9] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp00e.png"), Messages.getLanguageLocation("images/cards/fp00e.png"), Messages.getString("Game.413"));
        int i11 = i10 + 1;
        game.bits[i10] = new FreeStrategyCard(game.areas[122], Messages.getLanguageLocation("images/smallcards/fp00f.png"), Messages.getLanguageLocation("images/cards/fp00f.png"), Messages.getString("Game.415"));
        int i14 = i11 + 1;
        game.bits[i11] = new HuntTile(game.areas[184], Messages.getString("Game.1832"), Messages.getLanguageLocation("images/tiles/s.png"));
        int i15 = i14 + 1;
        game.bits[i14] = new HuntTile(game.areas[184], Messages.getString("Game.1834"), Messages.getLanguageLocation("images/tiles/s.png"));
        int i16 = i15 + 1;
        game.bits[i15] = new BalrogDice(game.areas[176], game);
        int i17 = i16 + 1;
        game.bits[i16] = new GothmogDice(game.areas[176], game);
        int i18 = i17 + 1;
        game.bits[i17] = new UnitGothmog(game.areas[176], 1, 1);
        int i19 = i18 + 1;
        game.bits[i18] = new UnitMouth2(game.areas[176]);
        int i20 = i19 + 1;
        game.bits[i19] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa00a.png"), Messages.getLanguageLocation("images/cards/sa00a.png"), Messages.getString("Game.416"));
        int i21 = i20 + 1;
        game.bits[i20] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa00b.png"), Messages.getLanguageLocation("images/cards/sa00b.png"), Messages.getString("Game.418"));
        int i22 = i21 + 1;
        game.bits[i21] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa00c.png"), Messages.getLanguageLocation("images/cards/sa00c.png"), Messages.getString("Game.419"));
        int i23 = i22 + 1;
        game.bits[i22] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("images/smallcards/sa00d.png"), Messages.getLanguageLocation("images/cards/sa00d.png"), Messages.getString("Game.421"));
        int i24 = i23 + 1;
        game.bits[i23] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa00e.png"), Messages.getLanguageLocation("images/cards/sa00e.png"), Messages.getString("Game.422"));
        int i25 = i24 + 1;
        game.bits[i24] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("images/smallcards/sa00f.png"), Messages.getLanguageLocation("images/cards/sa00f.png"), Messages.getString("Game.423"));
        int i26 = i25 + 1;
        game.bits[i25] = new Chit(game.areas[176], Messages.getString("Game.2048"), "expansion2/images/shadowaction1-E.png");
        int i27 = i26 + 1;
        game.bits[i26] = new Chit(game.areas[176], Messages.getString("Game.2049"), "expansion2/images/shadowaction2-E.png");
        if (game.varianttype.endsWith("T]")) {
            game.bits[i27] = new UnitTreebeard(game.areas[174]);
            i27++;
        }
        game.interpreter.execute("$base noisy ");
        game.numBits = i27;
    }

    public void bitWOMEinit() {
        int i;
        int i2 = game.numBits;
        game.interpreter.execute("$base silent ");
        int i3 = i2 + 1;
        game.bits[i2] = new ShadowFactionDice(game.areas[176], game);
        int i4 = i3 + 1;
        game.bits[i3] = new FreeFactionDice(game.areas[174], game);
        int x = 0;
        while (true) {
            i = i4;
            if (x >= 8) {
                break;
            }
            i4 = i + 1;
            game.bits[i] = new UnitEnt(game.areas[174]);
            x++;
        }
        int x2 = 0;
        while (x2 < 8) {
            game.bits[i] = new UnitEagle(game.areas[174]);
            x2++;
            i++;
        }
        int x3 = 0;
        while (x3 < 8) {
            game.bits[i] = new UnitDeadmen(game.areas[174]);
            x3++;
            i++;
        }
        int x4 = 0;
        while (x4 < 8) {
            game.bits[i] = new UnitDunlending(game.areas[176]);
            x4++;
            i++;
        }
        int x5 = 0;
        while (x5 < 8) {
            game.bits[i] = new UnitCorsair(game.areas[176]);
            x5++;
            i++;
        }
        int x6 = 0;
        while (x6 < 8) {
            game.bits[i] = new UnitSpider(game.areas[176]);
            x6++;
            i++;
        }
        int i5 = i + 1;
        game.bits[i] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle01.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle01.png"), Messages.getString("Game.899"), Messages.getLanguageLocation("images/FPAtextcard.gif"), Messages.getLanguageLocation("images/FPAC.jpg"), Messages.getString("FreeArmyCard.2"));
        int i6 = i5 + 1;
        game.bits[i5] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle02.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle02.png"), Messages.getString("Game.936"), Messages.getLanguageLocation("images/FPCtextcard.gif"), Messages.getLanguageLocation("images/FPCC.jpg"), Messages.getString("FreeCharacterCard.2"));
        int i7 = i6 + 1;
        game.bits[i6] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle03.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle03.png"), Messages.getString("Game.1032"), Messages.getLanguageLocation("images/FPAtextcard.gif"), Messages.getLanguageLocation("images/FPAC.jpg"), Messages.getString("FreeArmyCard.2"));
        int i8 = i7 + 1;
        game.bits[i7] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle04.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle04.png"), Messages.getString("Game.1045"), Messages.getLanguageLocation("images/FPCtextcard.gif"), Messages.getLanguageLocation("images/FPCC.jpg"), Messages.getString("FreeCharacterCard.2"));
        int i9 = i8 + 1;
        game.bits[i8] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle05.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle05.png"), Messages.getString("Game.1059"), Messages.getLanguageLocation("images/FPAtextcard.gif"), Messages.getLanguageLocation("images/FPAC.jpg"), Messages.getString("FreeArmyCard.2"));
        int i10 = i9 + 1;
        game.bits[i9] = new FreeBattleCard(game.areas[180], Messages.getLanguageLocation("wome/images/smallcards/fp_battle06.png"), Messages.getLanguageLocation("wome/images/cards/fp_battle06.png"), Messages.getString("Game.1072"), Messages.getLanguageLocation("images/FPCtextcard.gif"), Messages.getLanguageLocation("images/FPCC.jpg"), Messages.getString("FreeCharacterCard.2"));
        int i11 = i10 + 1;
        game.bits[i10] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle01.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle01.png"), Messages.getString("Game.1086"), Messages.getLanguageLocation("images/SAAtextcard.gif"), Messages.getLanguageLocation("images/SAAC.jpg"), Messages.getString("ShadowArmyCard.2"));
        int i12 = i11 + 1;
        game.bits[i11] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle02.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle02.png"), Messages.getString("Game.859"), Messages.getLanguageLocation("images/SACtextcard.gif"), Messages.getLanguageLocation("images/SACC.jpg"), Messages.getString("ShadowCharacterCard.2"));
        int i13 = i12 + 1;
        game.bits[i12] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle03.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle03.png"), Messages.getString("Game.1113"), Messages.getLanguageLocation("images/SAAtextcard.gif"), Messages.getLanguageLocation("images/SAAC.jpg"), Messages.getString("ShadowArmyCard.2"));
        int i14 = i13 + 1;
        game.bits[i13] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle04.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle04.png"), Messages.getString("Game.1127"), Messages.getLanguageLocation("images/SACtextcard.gif"), Messages.getLanguageLocation("images/SACC.jpg"), Messages.getString("ShadowCharacterCard.2"));
        int i15 = i14 + 1;
        game.bits[i14] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle05.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle05.png"), Messages.getString("Game.1144"), Messages.getLanguageLocation("images/SAAtextcard.gif"), Messages.getLanguageLocation("images/SAAC.jpg"), Messages.getString("ShadowArmyCard.2"));
        int i16 = i15 + 1;
        game.bits[i15] = new ShadowBattleCard(game.areas[181], Messages.getLanguageLocation("wome/images/smallcards/sa_battle06.png"), Messages.getLanguageLocation("wome/images/cards/sa_battle06.png"), Messages.getString("Game.1160"), Messages.getLanguageLocation("images/SACtextcard.gif"), Messages.getLanguageLocation("images/SACC.jpg"), Messages.getString("ShadowCharacterCard.2"));
        int i17 = i16 + 1;
        game.bits[i16] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction01.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction01.png"), Messages.getString("Game.1173"));
        int i18 = i17 + 1;
        game.bits[i17] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction02.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction02.png"), Messages.getString("Game.1181"));
        int i19 = i18 + 1;
        game.bits[i18] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction03.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction03.png"), Messages.getString("Game.1187"));
        int i20 = i19 + 1;
        game.bits[i19] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction04.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction04.png"), Messages.getString("Game.1194"));
        int i21 = i20 + 1;
        game.bits[i20] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction05.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction05.png"), Messages.getString("Game.1200"));
        int i22 = i21 + 1;
        game.bits[i21] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction06.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction06.png"), Messages.getString("Game.1223"));
        int i23 = i22 + 1;
        game.bits[i22] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction07.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction07.png"), Messages.getString("Game.1230"));
        int i24 = i23 + 1;
        game.bits[i23] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction08.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction08.png"), Messages.getString("Game.1238"));
        int i25 = i24 + 1;
        game.bits[i24] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction09.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction09.png"), Messages.getString("Game.1242"));
        int i26 = i25 + 1;
        game.bits[i25] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction10.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction10.png"), Messages.getString("Game.1247"));
        int i27 = i26 + 1;
        game.bits[i26] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction11.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction11.png"), Messages.getString("Game.1251"));
        int i28 = i27 + 1;
        game.bits[i27] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction12.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction12.png"), Messages.getString("Game.1256"));
        int i29 = i28 + 1;
        game.bits[i28] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction13.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction13.png"), Messages.getString("Game.1263"));
        int i30 = i29 + 1;
        game.bits[i29] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction14.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction14.png"), Messages.getString("Game.1271"));
        int i31 = i30 + 1;
        game.bits[i30] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction15.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction15.png"), Messages.getString("Game.1277"));
        int i32 = i31 + 1;
        game.bits[i31] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction16.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction16.png"), Messages.getString("Game.1283"));
        int i33 = i32 + 1;
        game.bits[i32] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction17.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction17.png"), Messages.getString("Game.1289"));
        int i34 = i33 + 1;
        game.bits[i33] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction18.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction18.png"), Messages.getString("Game.1295"));
        int i35 = i34 + 1;
        game.bits[i34] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction19.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction19.png"), Messages.getString("Game.1301"));
        int i36 = i35 + 1;
        game.bits[i35] = new FreeFactionCard(game.areas[194], Messages.getLanguageLocation("wome/images/smallcards/fp_faction20.png"), Messages.getLanguageLocation("wome/images/cards/fp_faction20.png"), Messages.getString("Game.1307"));
        int i37 = i36 + 1;
        game.bits[i36] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction01.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction01.png"), Messages.getString("Game.1311"));
        int i38 = i37 + 1;
        game.bits[i37] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction02.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction02.png"), Messages.getString("Game.1314"));
        int i39 = i38 + 1;
        game.bits[i38] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction03.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction03.png"), Messages.getString("Game.1317"));
        int i40 = i39 + 1;
        game.bits[i39] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction04.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction04.png"), Messages.getString("Game.1320"));
        int i41 = i40 + 1;
        game.bits[i40] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction05.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction05.png"), Messages.getString("Game.1323"));
        int i42 = i41 + 1;
        game.bits[i41] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction06.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction06.png"), Messages.getString("Game.1327"));
        int i43 = i42 + 1;
        game.bits[i42] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction07.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction07.png"), Messages.getString("Game.1331"));
        int i44 = i43 + 1;
        game.bits[i43] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction08.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction08.png"), Messages.getString("Game.1341"));
        int i45 = i44 + 1;
        game.bits[i44] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction09.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction09.png"), Messages.getString("Game.1344"));
        int i46 = i45 + 1;
        game.bits[i45] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction10.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction10.png"), Messages.getString("Game.1352"));
        int i47 = i46 + 1;
        game.bits[i46] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction11.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction11.png"), Messages.getString("Game.1355"));
        int i48 = i47 + 1;
        game.bits[i47] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction12.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction12.png"), Messages.getString("Game.1358"));
        int i49 = i48 + 1;
        game.bits[i48] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction13.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction13.png"), Messages.getString("Game.1361"));
        int i50 = i49 + 1;
        game.bits[i49] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction14.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction14.png"), Messages.getString("Game.1364"));
        int i51 = i50 + 1;
        game.bits[i50] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction15.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction15.png"), Messages.getString("Game.1367"));
        int i52 = i51 + 1;
        game.bits[i51] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction16.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction16.png"), Messages.getString("Game.1370"));
        int i53 = i52 + 1;
        game.bits[i52] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction17.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction17.png"), Messages.getString("Game.1373"));
        int i54 = i53 + 1;
        game.bits[i53] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction18.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction18.png"), Messages.getString("Game.1376"));
        int i55 = i54 + 1;
        game.bits[i54] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction19.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction19.png"), Messages.getString("Game.1379"));
        int i56 = i55 + 1;
        game.bits[i55] = new ShadowFactionCard(game.areas[198], Messages.getLanguageLocation("wome/images/smallcards/sa_faction20.png"), Messages.getLanguageLocation("wome/images/cards/sa_faction20.png"), Messages.getString("Game.1382"));
        int i57 = i56 + 1;
        game.bits[i56] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp01-F.png"), Messages.getLanguageLocation("wome/images/cards/fp01-F.png"), Messages.getString("Game.1385"));
        int i58 = i57 + 1;
        game.bits[i57] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp02-F.png"), Messages.getLanguageLocation("wome/images/cards/fp02-F.png"), Messages.getString("Game.1388"));
        int i59 = i58 + 1;
        game.bits[i58] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp03-F.png"), Messages.getLanguageLocation("wome/images/cards/fp03-F.png"), Messages.getString("Game.1391"));
        int i60 = i59 + 1;
        game.bits[i59] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp04-F.png"), Messages.getLanguageLocation("wome/images/cards/fp04-F.png"), Messages.getString("Game.1394"));
        int i61 = i60 + 1;
        game.bits[i60] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp05-F.png"), Messages.getLanguageLocation("wome/images/cards/fp05-F.png"), Messages.getString("Game.1397"));
        int i62 = i61 + 1;
        game.bits[i61] = new FreeCharacterCard(game.areas[121], Messages.getLanguageLocation("wome/images/smallcards/fp06-F.png"), Messages.getLanguageLocation("wome/images/cards/fp06-F.png"), Messages.getString("Game.1400"));
        int i63 = i62 + 1;
        game.bits[i62] = new ShadowCharacterCard(game.areas[123], Messages.getLanguageLocation("wome/images/smallcards/sa01-F.png"), Messages.getLanguageLocation("wome/images/cards/sa01-F.png"), Messages.getString("Game.1403"));
        int i64 = i63 + 1;
        game.bits[i63] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("wome/images/smallcards/sa04-F.png"), Messages.getLanguageLocation("wome/images/cards/sa04-F.png"), Messages.getString("Game.1406"));
        game.bits[i64] = new ShadowStrategyCard(game.areas[124], Messages.getLanguageLocation("wome/images/smallcards/sa05-F.png"), Messages.getLanguageLocation("wome/images/cards/sa05-F.png"), Messages.getString("Game.1409"));
        game.interpreter.execute("$base noisy ");
        game.numBits = i64 + 1;
        if (!Game.prefs.lastGame.contains("[L")) {
            game.bits[game.numBits] = new UnitTreebeard(game.areas[174]);
            game.numBits++;
        }
    }

    public void bitHuntForTheRing() {
        int i = game.numBits;
        game.interpreter.execute("$base silent ");
        int i2 = i + 1;
        game.bits[i] = new Chit(game.areas[176], Messages.getString("Game.929"), "expansion2/images/shadowaction1-E.png");
        ((Chit) game.bits[i2 - 1]).setNation(-11);
        int i3 = i2 + 1;
        game.bits[i2] = new Chit(game.areas[176], Messages.getString("Game.1021"), "expansion2/images/shadowaction2-E.png");
        ((Chit) game.bits[i3 - 1]).setNation(-11);
        int i4 = i3 + 1;
        game.bits[i3] = new Chit(game.areas[174], Messages.getString("Game.1026"), "images/freeaction1.png");
        ((Chit) game.bits[i4 - 1]).setNation(1);
        int i5 = i4 + 1;
        game.bits[i4] = new Chit(game.areas[174], Messages.getString("Game.1030"), "images/freeaction2.png");
        game.numBits = i5;
    }

    /**
     * Hybrid piece initialization - Phase 2 of Database Architecture Evolution
     * 
     * Tries to load pieces from scenario_setup table in database.
     * Falls back to hardcoded bitInit() if database is empty or unavailable.
     * 
     * Each scenario contains a complete piece set (not additive).
     */
    public void hybridPieceInit() {
        // Check if database loading is disabled
        if (game.prefs.disableDatabase) {
            System.out.println("[INIT] Database loading disabled - using hardcoded bitInit()");
            bitInit();
            game.piecesLoadedFromDatabase = false;  // Use resetBoard() for hardcoded initialization
            return;
        }
        
        // Try loading from database first
        try {
            wotr.services.ScenarioLoader loader = new wotr.services.ScenarioLoader();
            String scenarioId = getScenarioForVariant();
            
            System.out.println("[DB] Loading scenario for variant '" + Game.varianttype + "': " + scenarioId);
            
            // Check if scenario exists in database
            if (loader.hasScenarioData(scenarioId)) {
                System.out.println("[DB] Scenario found - loading from database as authoritative");
                
                List<wotr.services.ScenarioLoader.PieceSetup> setups = loader.loadScenarioData(scenarioId);
                
                if (setups != null && !setups.isEmpty()) {
                    int maxPieceId = 0;
                    
                    // Load all pieces from complete scenario directly into bits[]
                    for (wotr.services.ScenarioLoader.PieceSetup setup : setups) {
                        GamePiece piece = createPieceFromSetup(setup);
                        if (piece != null) {
                            int arrayIndex = setup.pieceId;  // Use explicit bits[] index from DB
                            game.bits[arrayIndex] = piece;   // Place directly in bits array
                            maxPieceId = Math.max(maxPieceId, arrayIndex);
                        }
                    }
                    
                    game.numBits = maxPieceId + 1;  // Update piece count
                    System.out.println("[DB] Database initialization complete: " + game.numBits + " pieces loaded");
                    game.piecesLoadedFromDatabase = true;  // Set flag to skip card verification
                    return;  // Success - skip hardcoded fallback
                }
            } else {
                System.out.println("[DB] Scenario '" + scenarioId + "' not found in database");
            }
            
        } catch (Exception e) {
            System.err.println("[DB] Database loading failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback to hardcoded initialization if database load failed
        System.out.println("[INIT] Database unavailable - using hardcoded initialization as fallback");
        bitInit();
        game.piecesLoadedFromDatabase = false;  // Use resetBoard() for hardcoded initialization
    }
    
    /**
     * Ensures special game pieces (rings, counters, political markers) exist in bits array.
     * These pieces are always needed regardless of scenario, but may be missing from database scenarios.
     * Creates missing pieces at the end of the bits array if they don't exist at their expected locations.
     */
    private void ensureSpecialPiecesExist() {
        System.out.println("[SPECIAL] Checking for special game pieces...");
        int piecesAdded = 0;
        
        // Track the actual end of bits array for adding missing pieces
        int nextFreeIndex = game.numBits;
        
        // Check and create Fellowship Counter (TwoChit at area 131)
        if (!hasPieceAtArea(131, "Fellowship")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[131], Messages.getString("Game.1852"), 
                Messages.getLanguageLocation("images/FSP.png"), 
                Messages.getLanguageLocation("images/FSPR.png"));
            System.out.println("[SPECIAL] Added Fellowship Counter at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        
        // Check and create Elven Rings (areas 125, 126, 127)
        if (game.varianttype.startsWith("expansion2")) {
            // Expansion 2 uses TwoChit for rings
            if (!hasPieceAtArea(125, "Ring")) {
                game.bits[nextFreeIndex] = new TwoChit(game.areas[125], Messages.getString("Game.1855"), 
                    "expansion2/images/elvenring1-E.png", "expansion2/images/elvenringback1-E.png");
                System.out.println("[SPECIAL] Added Elven Ring 1 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
            if (!hasPieceAtArea(126, "Ring")) {
                game.bits[nextFreeIndex] = new TwoChit(game.areas[126], Messages.getString("Game.1857"), 
                    "expansion2/images/elvenring2-E.png", "expansion2/images/elvenringback2-E.png");
                System.out.println("[SPECIAL] Added Elven Ring 2 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
            if (!hasPieceAtArea(127, "Ring")) {
                game.bits[nextFreeIndex] = new TwoChit(game.areas[127], Messages.getString("Game.1859"), 
                    "expansion2/images/elvenring3-E.png", "expansion2/images/elvenringback3-E.png");
                System.out.println("[SPECIAL] Added Elven Ring 3 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
        } else {
            // Base game uses Chit for rings
            if (!hasPieceAtArea(125, "Ring")) {
                game.bits[nextFreeIndex] = new Chit(game.areas[125], Messages.getString("Game.1855"), 
                    Messages.getLanguageLocation("images/elvenring1.png"));
                System.out.println("[SPECIAL] Added Elven Ring 1 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
            if (!hasPieceAtArea(126, "Ring")) {
                game.bits[nextFreeIndex] = new Chit(game.areas[126], Messages.getString("Game.1857"), 
                    Messages.getLanguageLocation("images/elvenring2.png"));
                System.out.println("[SPECIAL] Added Elven Ring 2 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
            if (!hasPieceAtArea(127, "Ring")) {
                game.bits[nextFreeIndex] = new Chit(game.areas[127], Messages.getString("Game.1859"), 
                    Messages.getLanguageLocation("images/elvenring3.png"));
                System.out.println("[SPECIAL] Added Elven Ring 3 at index " + nextFreeIndex);
                nextFreeIndex++;
                piecesAdded++;
            }
        }
        
        // Check and create Corruption Counter (Chit at area 131)
        if (!hasPieceAtArea(131, "corruption")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[131], Messages.getString("Game.1861"), 
                Messages.getLanguageLocation("images/corruption.png"));
            System.out.println("[SPECIAL] Added Corruption Counter at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        
        // Check and create Political Markers (areas 117-119)
        // Elves marker (area 117)
        if (!hasPieceAtArea(117, "Elves")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[117], Messages.getString("Game.1863"), 
                Messages.getLanguageLocation("images/nelves.png"));
            System.out.println("[SPECIAL] Added Elves political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Dwarves marker (area 117)
        if (!hasPieceAtArea(117, "Dwarves")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[117], Messages.getString("Game.1865"), 
                Messages.getLanguageLocation("images/pdwarves.png"), Messages.getLanguageLocation("images/ndwarves.png"));
            System.out.println("[SPECIAL] Added Dwarves political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // North marker (area 117)
        if (!hasPieceAtArea(117, "North")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[117], Messages.getString("Game.1868"), 
                Messages.getLanguageLocation("images/pnorth.png"), Messages.getLanguageLocation("images/nnorth.png"));
            System.out.println("[SPECIAL] Added North political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Rohan marker (area 117)
        if (!hasPieceAtArea(117, "Rohan")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[117], Messages.getString("Game.1871"), 
                Messages.getLanguageLocation("images/prohan.png"), Messages.getLanguageLocation("images/nrohan.png"));
            System.out.println("[SPECIAL] Added Rohan political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Gondor marker (area 118)
        if (!hasPieceAtArea(118, "Gondor")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[118], Messages.getString("Game.1874"), 
                Messages.getLanguageLocation("images/pgondor.png"), Messages.getLanguageLocation("images/ngondor.png"));
            System.out.println("[SPECIAL] Added Gondor political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Southrons marker (area 118)
        if (!hasPieceAtArea(118, "Southron")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[118], Messages.getString("Game.1877"), 
                Messages.getLanguageLocation("images/nsouthron.png"));
            System.out.println("[SPECIAL] Added Southrons political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Sauron marker (area 119)
        if (!hasPieceAtArea(119, "Sauron")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[119], Messages.getString("Game.1879"), 
                Messages.getLanguageLocation("images/nsauron.png"));
            System.out.println("[SPECIAL] Added Sauron political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        // Isengard marker (area 119)
        if (!hasPieceAtArea(119, "Isengard")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[119], Messages.getString("Game.1881"), 
                Messages.getLanguageLocation("images/nisengard.png"));
            System.out.println("[SPECIAL] Added Isengard political marker at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        
        // Check and create Victory Point counters (area 163)
        if (!hasPieceAtArea(163, "Free")) {
            game.bits[nextFreeIndex] = new Chit(game.areas[163], Messages.getString("Game.13"), 
                Messages.getLanguageLocation("images/vp_free.png"));
            System.out.println("[SPECIAL] Added Free Peoples VP counter at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        if (!hasPieceAtArea(163, "Shadow")) {
            game.bits[nextFreeIndex] = new TwoChit(game.areas[163], Messages.getString("Game.17"), 
                Messages.getLanguageLocation("images/vp_shadow.png"), 
                Messages.getLanguageLocation("images/vp_shadowreverse.png"));
            System.out.println("[SPECIAL] Added Shadow VP counter at index " + nextFreeIndex);
            nextFreeIndex++;
            piecesAdded++;
        }
        
        // Update numBits if we added any pieces
        if (piecesAdded > 0) {
            game.numBits = nextFreeIndex;
            System.out.println("[SPECIAL] Added " + piecesAdded + " missing special pieces, new numBits: " + game.numBits);
        } else {
            System.out.println("[SPECIAL] All special pieces already exist");
        }
    }
    
    /**
     * Check if a piece exists at the given area with a type/name containing the search string
     */
    private boolean hasPieceAtArea(int areaIndex, String nameFragment) {
        for (int i = 0; i < game.numBits; i++) {
            GamePiece piece = game.bits[i];
            if (piece != null && piece.currentLocation() == game.areas[areaIndex]) {
                String pieceType = piece.type();
                if (pieceType != null && pieceType.contains(nameFragment)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void redoTitle() {
        String players;
        String type = "";
        if (game.talker.connected) {
            players = String.valueOf(game.prefs.nick) + game.handViewing + Messages.getString("Game.1446") + game.opponent + game.opponentHandViewing;
        } else {
            players = String.valueOf(game.prefs.nick) + game.handViewing + Messages.getString("Game.1447") + game.prefs.nick;
        }
        if (game.boardtype.equals("wotr") && game.varianttype.equals("base")) {
            type = Messages.getString("Game.12");
        }
        if (game.varianttype.startsWith("base2")) {
            type = "[Base rules 2nd edition]";
        }
        if (game.varianttype.startsWith("expansion2")) {
            type = "[Expansion rules 2nd edition]";
        }
        game.windowTitle = String.valueOf(Messages.getString("Game.1449")) + type + " " + players + Messages.getString("Game.1450") + game.turn;
        game.win.setTitle(game.windowTitle);
        game.win.repaint();
    }

    public void resetBoard() {
        game.maxboardwidth = 996;
        game.maxboardheight = 688;
        game.chosen1 = null;
        game.chosen1player = null;
        game.chosen2 = null;
        game.chosen2player = null;
        game.history = new ArrayList<>();
        game.historyactions = new ArrayList<>();
        game.historyactionpointer = 0;
        game.historypointer = 0;
        for (int i = 0; i < FreeActionDie.stats.length; i++) {
            FreeActionDie.stats[i] = 0;
        }
        for (int i2 = 0; i2 < ShadowActionDie.stats.length; i2++) {
            ShadowActionDie.stats[i2] = 0;
        }
        for (int i3 = 0; i3 < game.getPlayerD6stats().length; i3++) {
            game.setPlayerD6stats(i3,  0);
        }
        for (int i4 = 0; i4 < game.getOpponentD6stats().length; i4++) {
            game.setOpponentD6stats(i4, 0);
        }
        ShadowActionDie.allocations = 0;
        
        // Skip piece movements if pieces were loaded from database
        // Database already has pieces in correct initial positions
        if (game.piecesLoadedFromDatabase) {
            return;
        }
        
        // Hardcoded piece movements for bitInit() compatibility
        int i5 = 0 + 1;
        game.bits[0].moveTo(game.areas[30]);
        int i6 = i5 + 1;
        game.bits[i5].moveTo(game.areas[26]);
        int i7 = i6 + 1;
        game.bits[i6].moveTo(game.areas[40]);
        int i8 = i7 + 1;
        game.bits[i7].moveTo(game.areas[40]);
        int i9 = i8 + 1;
        game.bits[i8].moveTo(game.areas[40]);
        int i10 = i9 + 1;
        game.bits[i9].moveTo(game.areas[40]);
        int i11 = i10 + 1;
        game.bits[i10].moveTo(game.areas[40]);
        int i12 = i11 + 1;
        game.bits[i11].moveTo(game.areas[35]);
        int i13 = i12 + 1;
        game.bits[i12].moveTo(game.areas[35]);
        int i14 = i13 + 1;
        game.bits[i13].moveTo(game.areas[44]);
        int i15 = i14 + 1;
        game.bits[i14].moveTo(game.areas[44]);
        int i16 = i15 + 1;
        game.bits[i15].moveTo(game.areas[63]);
        int i17 = i16 + 1;
        game.bits[i16].moveTo(game.areas[63]);
        int i18 = i17 + 1;
        game.bits[i17].moveTo(game.areas[63]);
        int i19 = i18 + 1;
        game.bits[i18].moveTo(game.areas[63]);
        int i20 = i19 + 1;
        game.bits[i19].moveTo(game.areas[63]);
        int i21 = i20 + 1;
        game.bits[i20].moveTo(game.areas[86]);
        int i22 = i21 + 1;
        game.bits[i21].moveTo(game.areas[86]);
        int i23 = i22 + 1;
        game.bits[i22].moveTo(game.areas[86]);
        int i24 = i23 + 1;
        game.bits[i23].moveTo(game.areas[86]);
        int i25 = i24 + 1;
        game.bits[i24].moveTo(game.areas[86]);
        int i26 = i25 + 1;
        game.bits[i25].moveTo(game.areas[91]);
        int i27 = i26 + 1;
        game.bits[i26].moveTo(game.areas[91]);
        int i28 = i27 + 1;
        game.bits[i27].moveTo(game.areas[91]);
        int i29 = i28 + 1;
        game.bits[i28].moveTo(game.areas[91]);
        int i30 = i29 + 1;
        game.bits[i29].moveTo(game.areas[91]);
        int i31 = i30 + 1;
        game.bits[i30].moveTo(game.areas[96]);
        int i32 = i31 + 1;
        game.bits[i31].moveTo(game.areas[96]);
        int i33 = i32 + 1;
        game.bits[i32].moveTo(game.areas[96]);
        int i34 = i33 + 1;
        game.bits[i33].moveTo(game.areas[96]);
        int i35 = i34 + 1;
        game.bits[i34].moveTo(game.areas[94]);
        int i36 = i35 + 1;
        game.bits[i35].moveTo(game.areas[94]);
        int i37 = i36 + 1;
        game.bits[i36].moveTo(game.areas[94]);
        int i38 = i37 + 1;
        game.bits[i37].moveTo(game.areas[88]);
        int i39 = i38 + 1;
        game.bits[i38].moveTo(game.areas[88]);
        int i40 = i39 + 1;
        game.bits[i39].moveTo(game.areas[63]);
        int i41 = i40 + 1;
        game.bits[i40].moveTo(game.areas[96]);
        int i42 = i41 + 1;
        game.bits[i41].moveTo(game.areas[86]);
        int i43 = i42 + 1;
        game.bits[i42].moveTo(game.areas[91]);
        int i44 = i43 + 1;
        game.bits[i43].moveTo(game.areas[96]);
        int i45 = i44 + 1;
        game.bits[i44].moveTo(game.areas[63]);
        int i46 = i45 + 1;
        game.bits[i45].moveTo(game.areas[49]);
        int i47 = i46 + 1;
        game.bits[i46].moveTo(game.areas[49]);
        int i48 = i47 + 1;
        game.bits[i47].moveTo(game.areas[49]);
        int i49 = i48 + 1;
        game.bits[i48].moveTo(game.areas[61]);
        int i50 = i49 + 1;
        game.bits[i49].moveTo(game.areas[61]);
        int i51 = i50 + 1;
        game.bits[i50].moveTo(game.areas[61]);
        int i52 = i51 + 1;
        game.bits[i51].moveTo(game.areas[85]);
        int i53 = i52 + 1;
        game.bits[i52].moveTo(game.areas[85]);
        int i54 = i53 + 1;
        game.bits[i53].moveTo(game.areas[85]);
        int i55 = i54 + 1;
        game.bits[i54].moveTo(game.areas[103]);
        int i56 = i55 + 1;
        game.bits[i55].moveTo(game.areas[103]);
        int i57 = i56 + 1;
        game.bits[i56].moveTo(game.areas[103]);
        int i58 = i57 + 1;
        game.bits[i57].moveTo(game.areas[104]);
        int i59 = i58 + 1;
        game.bits[i58].moveTo(game.areas[104]);
        int i60 = i59 + 1;
        game.bits[i59].moveTo(game.areas[85]);
        int i61 = i60 + 1;
        game.bits[i60].moveTo(game.areas[103]);
        int i62 = i61 + 1;
        game.bits[i61].moveTo(game.areas[61]);
        int i63 = i62 + 1;
        game.bits[i62].moveTo(game.areas[8]);
        int i64 = i63 + 1;
        game.bits[i63].moveTo(game.areas[90]);
        int i65 = i64 + 1;
        game.bits[i64].moveTo(game.areas[82]);
        int i66 = i65 + 1;
        game.bits[i65].moveTo(game.areas[82]);
        int i67 = i66 + 1;
        game.bits[i66].moveTo(game.areas[82]);
        int i68 = i67 + 1;
        game.bits[i67].moveTo(game.areas[7]);
        int i69 = i68 + 1;
        game.bits[i68].moveTo(game.areas[18]);
        int i70 = i69 + 1;
        game.bits[i69].moveTo(game.areas[58]);
        int i71 = i70 + 1;
        game.bits[i70].moveTo(game.areas[87]);
        int i72 = i71 + 1;
        game.bits[i71].moveTo(game.areas[15]);
        int i73 = i72 + 1;
        game.bits[i72].moveTo(game.areas[87]);
        int i74 = i73 + 1;
        game.bits[i73].moveTo(game.areas[41]);
        int i75 = i74 + 1;
        game.bits[i74].moveTo(game.areas[41]);
        int i76 = i75 + 1;
        game.bits[i75].moveTo(game.areas[45]);
        int i77 = i76 + 1;
        game.bits[i76].moveTo(game.areas[43]);
        int i78 = i77 + 1;
        game.bits[i77].moveTo(game.areas[43]);
        int i79 = i78 + 1;
        game.bits[i78].moveTo(game.areas[41]);
        int i80 = i79 + 1;
        game.bits[i79].moveTo(game.areas[42]);
        int i81 = i80 + 1;
        game.bits[i80].moveTo(game.areas[42]);
        int i82 = i81 + 1;
        game.bits[i81].moveTo(game.areas[42]);
        int i83 = i82 + 1;
        game.bits[i82].moveTo(game.areas[48]);
        int i84 = i83 + 1;
        game.bits[i83].moveTo(game.areas[73]);
        int i85 = i84 + 1;
        game.bits[i84].moveTo(game.areas[73]);
        int i86 = i85 + 1;
        game.bits[i85].moveTo(game.areas[74]);
        int i87 = i86 + 1;
        game.bits[i86].moveTo(game.areas[74]);
        int i88 = i87 + 1;
        game.bits[i87].moveTo(game.areas[74]);
        int i89 = i88 + 1;
        game.bits[i88].moveTo(game.areas[74]);
        int i90 = i89 + 1;
        game.bits[i89].moveTo(game.areas[74]);
        int i91 = i90 + 1;
        game.bits[i90].moveTo(game.areas[4]);
        int i92 = i91 + 1;
        game.bits[i91].moveTo(game.areas[37]);
        int i93 = i92 + 1;
        game.bits[i92].moveTo(game.areas[70]);
        int i94 = i93 + 1;
        game.bits[i93].moveTo(game.areas[4]);
        int i95 = i94 + 1;
        game.bits[i94].moveTo(game.areas[27]);
        int i96 = i95 + 1;
        game.bits[i95].moveTo(game.areas[27]);
        int i97 = i96 + 1;
        game.bits[i96].moveTo(game.areas[37]);
        int i98 = i97 + 1;
        game.bits[i97].moveTo(game.areas[37]);
        int i99 = i98 + 1;
        game.bits[i98].moveTo(game.areas[70]);
        int i100 = i99 + 1;
        game.bits[i99].moveTo(game.areas[4]);
        int i101 = i100 + 1;
        game.bits[i100].moveTo(game.areas[27]);
        int i102 = i101 + 1;
        game.bits[i101].moveTo(game.areas[37]);
        int i103 = i102 + 1;
        game.bits[i102].moveTo(game.areas[70]);
        int i104 = i103 + 1;
        game.bits[i103].moveTo(game.areas[179]);
        int i105 = i104 + 1;
        game.bits[i104].moveTo(game.areas[179]);
        int i106 = i105 + 1;
        game.bits[i105].moveTo(game.areas[179]);
        int i107 = i106 + 1;
        game.bits[i106].moveTo(game.areas[179]);
        int i108 = i107 + 1;
        game.bits[i107].moveTo(game.areas[179]);
        int i109 = i108 + 1;
        game.bits[i108].moveTo(game.areas[179]);
        int i110 = i109 + 1;
        game.bits[i109].moveTo(game.areas[179]);
        int i111 = i110 + 1;
        game.bits[i110].moveTo(game.areas[122]);
        int i112 = i111 + 1;
        game.bits[i111].moveTo(game.areas[121]);
        int i113 = i112 + 1;
        game.bits[i112].moveTo(game.areas[121]);
        int i114 = i113 + 1;
        game.bits[i113].moveTo(game.areas[121]);
        int i115 = i114 + 1;
        game.bits[i114].moveTo(game.areas[122]);
        int i116 = i115 + 1;
        game.bits[i115].moveTo(game.areas[122]);
        int i117 = i116 + 1;
        game.bits[i116].moveTo(game.areas[121]);
        int i118 = i117 + 1;
        game.bits[i117].moveTo(game.areas[122]);
        int i119 = i118 + 1;
        game.bits[i118].moveTo(game.areas[122]);
        int i120 = i119 + 1;
        game.bits[i119].moveTo(game.areas[121]);
        int i121 = i120 + 1;
        game.bits[i120].moveTo(game.areas[121]);
        int i122 = i121 + 1;
        game.bits[i121].moveTo(game.areas[121]);
        int i123 = i122 + 1;
        game.bits[i122].moveTo(game.areas[122]);
        int i124 = i123 + 1;
        game.bits[i123].moveTo(game.areas[122]);
        int i125 = i124 + 1;
        game.bits[i124].moveTo(game.areas[122]);
        int i126 = i125 + 1;
        game.bits[i125].moveTo(game.areas[122]);
        int i127 = i126 + 1;
        game.bits[i126].moveTo(game.areas[122]);
        int i128 = i127 + 1;
        game.bits[i127].moveTo(game.areas[121]);
        int i129 = i128 + 1;
        game.bits[i128].moveTo(game.areas[122]);
        int i130 = i129 + 1;
        game.bits[i129].moveTo(game.areas[121]);
        int i131 = i130 + 1;
        game.bits[i130].moveTo(game.areas[121]);
        int i132 = i131 + 1;
        game.bits[i131].moveTo(game.areas[121]);
        int i133 = i132 + 1;
        game.bits[i132].moveTo(game.areas[121]);
        int i134 = i133 + 1;
        game.bits[i133].moveTo(game.areas[122]);
        int i135 = i134 + 1;
        game.bits[i134].moveTo(game.areas[122]);
        int i136 = i135 + 1;
        game.bits[i135].moveTo(game.areas[122]);
        int i137 = i136 + 1;
        game.bits[i136].moveTo(game.areas[121]);
        int i138 = i137 + 1;
        game.bits[i137].moveTo(game.areas[121]);
        int i139 = i138 + 1;
        game.bits[i138].moveTo(game.areas[122]);
        int i140 = i139 + 1;
        game.bits[i139].moveTo(game.areas[121]);
        int i141 = i140 + 1;
        game.bits[i140].moveTo(game.areas[121]);
        int i142 = i141 + 1;
        game.bits[i141].moveTo(game.areas[122]);
        int i143 = i142 + 1;
        game.bits[i142].moveTo(game.areas[122]);
        int i144 = i143 + 1;
        game.bits[i143].moveTo(game.areas[121]);
        int i145 = i144 + 1;
        game.bits[i144].moveTo(game.areas[122]);
        int i146 = i145 + 1;
        game.bits[i145].moveTo(game.areas[121]);
        int i147 = i146 + 1;
        game.bits[i146].moveTo(game.areas[121]);
        int i148 = i147 + 1;
        game.bits[i147].moveTo(game.areas[122]);
        int i149 = i148 + 1;
        game.bits[i148].moveTo(game.areas[121]);
        int i150 = i149 + 1;
        game.bits[i149].moveTo(game.areas[122]);
        int i151 = i150 + 1;
        game.bits[i150].moveTo(game.areas[122]);
        int i152 = i151 + 1;
        game.bits[i151].moveTo(game.areas[122]);
        int i153 = i152 + 1;
        game.bits[i152].moveTo(game.areas[121]);
        int i154 = i153 + 1;
        game.bits[i153].moveTo(game.areas[121]);
        int i155 = i154 + 1;
        game.bits[i154].moveTo(game.areas[122]);
        int i156 = i155 + 1;
        game.bits[i155].moveTo(game.areas[122]);
        int i157 = i156 + 1;
        game.bits[i156].moveTo(game.areas[121]);
        int i158 = i157 + 1;
        game.bits[i157].moveTo(game.areas[122]);
        int i159 = i158 + 1;
        game.bits[i158].moveTo(game.areas[121]);
        int i160 = i159 + 1;
        game.bits[i159].moveTo(game.areas[121]);
        int i161 = i160 + 1;
        game.bits[i160].moveTo(game.areas[122]);
        int i162 = i161 + 1;
        game.bits[i161].moveTo(game.areas[121]);
        int i163 = i162 + 1;
        game.bits[i162].moveTo(game.areas[123]);
        int i164 = i163 + 1;
        game.bits[i163].moveTo(game.areas[124]);
        int i165 = i164 + 1;
        game.bits[i164].moveTo(game.areas[123]);
        int i166 = i165 + 1;
        game.bits[i165].moveTo(game.areas[123]);
        int i167 = i166 + 1;
        game.bits[i166].moveTo(game.areas[123]);
        int i168 = i167 + 1;
        game.bits[i167].moveTo(game.areas[124]);
        int i169 = i168 + 1;
        game.bits[i168].moveTo(game.areas[124]);
        int i170 = i169 + 1;
        game.bits[i169].moveTo(game.areas[123]);
        int i171 = i170 + 1;
        game.bits[i170].moveTo(game.areas[123]);
        int i172 = i171 + 1;
        game.bits[i171].moveTo(game.areas[123]);
        int i173 = i172 + 1;
        game.bits[i172].moveTo(game.areas[123]);
        int i174 = i173 + 1;
        game.bits[i173].moveTo(game.areas[123]);
        int i175 = i174 + 1;
        game.bits[i174].moveTo(game.areas[124]);
        int i176 = i175 + 1;
        game.bits[i175].moveTo(game.areas[124]);
        int i177 = i176 + 1;
        game.bits[i176].moveTo(game.areas[124]);
        int i178 = i177 + 1;
        game.bits[i177].moveTo(game.areas[123]);
        int i179 = i178 + 1;
        game.bits[i178].moveTo(game.areas[123]);
        int i180 = i179 + 1;
        game.bits[i179].moveTo(game.areas[124]);
        int i181 = i180 + 1;
        game.bits[i180].moveTo(game.areas[124]);
        int i182 = i181 + 1;
        game.bits[i181].moveTo(game.areas[123]);
        int i183 = i182 + 1;
        game.bits[i182].moveTo(game.areas[124]);
        int i184 = i183 + 1;
        game.bits[i183].moveTo(game.areas[123]);
        int i185 = i184 + 1;
        game.bits[i184].moveTo(game.areas[124]);
        int i186 = i185 + 1;
        game.bits[i185].moveTo(game.areas[123]);
        int i187 = i186 + 1;
        game.bits[i186].moveTo(game.areas[123]);
        int i188 = i187 + 1;
        game.bits[i187].moveTo(game.areas[124]);
        int i189 = i188 + 1;
        game.bits[i188].moveTo(game.areas[124]);
        int i190 = i189 + 1;
        game.bits[i189].moveTo(game.areas[124]);
        int i191 = i190 + 1;
        game.bits[i190].moveTo(game.areas[124]);
        int i192 = i191 + 1;
        game.bits[i191].moveTo(game.areas[124]);
        int i193 = i192 + 1;
        game.bits[i192].moveTo(game.areas[124]);
        int i194 = i193 + 1;
        game.bits[i193].moveTo(game.areas[124]);
        int i195 = i194 + 1;
        game.bits[i194].moveTo(game.areas[124]);
        int i196 = i195 + 1;
        game.bits[i195].moveTo(game.areas[123]);
        int i197 = i196 + 1;
        game.bits[i196].moveTo(game.areas[124]);
        int i198 = i197 + 1;
        game.bits[i197].moveTo(game.areas[123]);
        int i199 = i198 + 1;
        game.bits[i198].moveTo(game.areas[123]);
        int i200 = i199 + 1;
        game.bits[i199].moveTo(game.areas[124]);
        int i201 = i200 + 1;
        game.bits[i200].moveTo(game.areas[124]);
        int i202 = i201 + 1;
        game.bits[i201].moveTo(game.areas[124]);
        int i203 = i202 + 1;
        game.bits[i202].moveTo(game.areas[123]);
        int i204 = i203 + 1;
        game.bits[i203].moveTo(game.areas[124]);
        int i205 = i204 + 1;
        game.bits[i204].moveTo(game.areas[123]);
        int i206 = i205 + 1;
        game.bits[i205].moveTo(game.areas[123]);
        int i207 = i206 + 1;
        game.bits[i206].moveTo(game.areas[123]);
        int i208 = i207 + 1;
        game.bits[i207].moveTo(game.areas[123]);
        int i209 = i208 + 1;
        game.bits[i208].moveTo(game.areas[123]);
        int i210 = i209 + 1;
        game.bits[i209].moveTo(game.areas[124]);
        int i211 = i210 + 1;
        game.bits[i210].moveTo(game.areas[124]);
        int i212 = i211 + 1;
        game.bits[i211].moveTo(game.areas[124]);
        int i213 = i212 + 1;
        game.bits[i212].moveTo(game.areas[123]);
        int i214 = i213 + 1;
        game.bits[i213].moveTo(game.areas[123]);
        int i215 = i214 + 1;
        game.bits[i214].moveTo(game.areas[178]);
        int i216 = i215 + 1;
        game.bits[i215].moveTo(game.areas[178]);
        int i217 = i216 + 1;
        game.bits[i216].moveTo(game.areas[178]);
        int i218 = i217 + 1;
        game.bits[i217].moveTo(game.areas[178]);
        int i219 = i218 + 1;
        game.bits[i218].moveTo(game.areas[182]);
        int i220 = i219 + 1;
        game.bits[i219].moveTo(game.areas[182]);
        int i221 = i220 + 1;
        game.bits[i220].moveTo(game.areas[182]);
        int i222 = i221 + 1;
        game.bits[i221].moveTo(game.areas[182]);
        int i223 = i222 + 1;
        game.bits[i222].moveTo(game.areas[182]);
        int i224 = i223 + 1;
        game.bits[i223].moveTo(game.areas[182]);
        int i225 = i224 + 1;
        game.bits[i224].moveTo(game.areas[182]);
        int i226 = i225 + 1;
        game.bits[i225].moveTo(game.areas[182]);
        int i227 = i226 + 1;
        game.bits[i226].moveTo(game.areas[182]);
        int i228 = i227 + 1;
        game.bits[i227].moveTo(game.areas[182]);
        int i229 = i228 + 1;
        game.bits[i228].moveTo(game.areas[182]);
        int i230 = i229 + 1;
        game.bits[i229].moveTo(game.areas[182]);
        int i231 = i230 + 1;
        game.bits[i230].moveTo(game.areas[182]);
        int i232 = i231 + 1;
        game.bits[i231].moveTo(game.areas[182]);
        int i233 = i232 + 1;
        game.bits[i232].moveTo(game.areas[182]);
        int i234 = i233 + 1;
        game.bits[i233].moveTo(game.areas[182]);
        int i235 = i234 + 1;
        game.bits[i234].moveTo(game.areas[182]);
        int i236 = i235 + 1;
        game.bits[i235].moveTo(game.areas[182]);
        int i237 = i236 + 1;
        game.bits[i236].moveTo(game.areas[184]);
        int i238 = i237 + 1;
        game.bits[i237].moveTo(game.areas[184]);
        int i239 = i238 + 1;
        game.bits[i238].moveTo(game.areas[184]);
        int i240 = i239 + 1;
        game.bits[i239].moveTo(game.areas[184]);
        int i241 = i240 + 1;
        game.bits[i240].moveTo(game.areas[184]);
        int i242 = i241 + 1;
        game.bits[i241].moveTo(game.areas[184]);
        int i243 = i242 + 1;
        game.bits[i242].moveTo(game.areas[184]);
        int i244 = i243 + 1;
        game.bits[i243].moveTo(game.areas[184]);
        ((Flippable) game.bits[i244]).defaultSide();
        int i245 = i244 + 1;
        game.bits[i244].moveTo(game.areas[116]);
        ((Flippable) game.bits[i245]).defaultSide();
        int i246 = i245 + 1;
        game.bits[i245].moveTo(game.areas[115]);
        int i247 = i246 + 1;
        game.bits[i246].moveTo(game.areas[115]);
        int i248 = i247 + 1;
        game.bits[i247].moveTo(game.areas[115]);
        int i249 = i248 + 1;
        game.bits[i248].moveTo(game.areas[115]);
        int i250 = i249 + 1;
        game.bits[i249].moveTo(game.areas[115]);
        int i251 = i250 + 1;
        game.bits[i250].moveTo(game.areas[115]);
        int i252 = i251 + 1;
        game.bits[i251].moveTo(game.areas[27]);
        ((Flippable) game.bits[i252]).defaultSide();
        int i253 = i252 + 1;
        game.bits[i252].moveTo(game.areas[131]);
        int i254 = i253 + 1;
        game.bits[i253].moveTo(game.areas[125]);
        int i255 = i254 + 1;
        game.bits[i254].moveTo(game.areas[126]);
        int i256 = i255 + 1;
        game.bits[i255].moveTo(game.areas[127]);
        int i257 = i256 + 1;
        game.bits[i256].moveTo(game.areas[131]);
        int i258 = i257 + 1;
        game.bits[i257].moveTo(game.areas[176]);
        int i259 = i258 + 1;
        game.bits[i258].moveTo(game.areas[176]);
        int i260 = i259 + 1;
        game.bits[i259].moveTo(game.areas[176]);
        int i261 = i260 + 1;
        game.bits[i260].moveTo(game.areas[117]);
        ((Flippable) game.bits[i261]).defaultSide();
        int i262 = i261 + 1;
        game.bits[i261].moveTo(game.areas[117]);
        ((Flippable) game.bits[i262]).defaultSide();
        int i263 = i262 + 1;
        game.bits[i262].moveTo(game.areas[117]);
        ((Flippable) game.bits[i263]).defaultSide();
        int i264 = i263 + 1;
        game.bits[i263].moveTo(game.areas[117]);
        ((Flippable) game.bits[i264]).defaultSide();
        int i265 = i264 + 1;
        game.bits[i264].moveTo(game.areas[118]);
        int i266 = i265 + 1;
        game.bits[i265].moveTo(game.areas[118]);
        int i267 = i266 + 1;
        game.bits[i266].moveTo(game.areas[119]);
        int i268 = i267 + 1;
        game.bits[i267].moveTo(game.areas[119]);
        int i269 = i268 + 1;
        game.bits[i268].moveTo(game.areas[174]);
        int i270 = i269 + 1;
        game.bits[i269].moveTo(game.areas[174]);
        int i271 = i270 + 1;
        game.bits[i270].moveTo(game.areas[174]);
        int i272 = i271 + 1;
        game.bits[i271].moveTo(game.areas[174]);
        int i273 = i272 + 1;
        game.bits[i272].moveTo(game.areas[174]);
        int i274 = i273 + 1;
        game.bits[i273].moveTo(game.areas[174]);
        int i275 = i274 + 1;
        game.bits[i274].moveTo(game.areas[174]);
        int i276 = i275 + 1;
        game.bits[i275].moveTo(game.areas[174]);
        int i277 = i276 + 1;
        game.bits[i276].moveTo(game.areas[174]);
        int i278 = i277 + 1;
        game.bits[i277].moveTo(game.areas[174]);
        int i279 = i278 + 1;
        game.bits[i278].moveTo(game.areas[174]);
        int i280 = i279 + 1;
        game.bits[i279].moveTo(game.areas[174]);
        int i281 = i280 + 1;
        game.bits[i280].moveTo(game.areas[174]);
        int i282 = i281 + 1;
        game.bits[i281].moveTo(game.areas[174]);
        int i283 = i282 + 1;
        game.bits[i282].moveTo(game.areas[174]);
        int i284 = i283 + 1;
        game.bits[i283].moveTo(game.areas[174]);
        int i285 = i284 + 1;
        game.bits[i284].moveTo(game.areas[174]);
        int i286 = i285 + 1;
        game.bits[i285].moveTo(game.areas[174]);
        int i287 = i286 + 1;
        game.bits[i286].moveTo(game.areas[174]);
        int i288 = i287 + 1;
        game.bits[i287].moveTo(game.areas[174]);
        int i289 = i288 + 1;
        game.bits[i288].moveTo(game.areas[174]);
        int i290 = i289 + 1;
        game.bits[i289].moveTo(game.areas[174]);
        int i291 = i290 + 1;
        game.bits[i290].moveTo(game.areas[174]);
        int i292 = i291 + 1;
        game.bits[i291].moveTo(game.areas[174]);
        int i293 = i292 + 1;
        game.bits[i292].moveTo(game.areas[174]);
        int i294 = i293 + 1;
        game.bits[i293].moveTo(game.areas[174]);
        int i295 = i294 + 1;
        game.bits[i294].moveTo(game.areas[174]);
        int i296 = i295 + 1;
        game.bits[i295].moveTo(game.areas[174]);
        int i297 = i296 + 1;
        game.bits[i296].moveTo(game.areas[174]);
        int i298 = i297 + 1;
        game.bits[i297].moveTo(game.areas[174]);
        int i299 = i298 + 1;
        game.bits[i298].moveTo(game.areas[174]);
        int i300 = i299 + 1;
        game.bits[i299].moveTo(game.areas[174]);
        int i301 = i300 + 1;
        game.bits[i300].moveTo(game.areas[174]);
        int i302 = i301 + 1;
        game.bits[i301].moveTo(game.areas[174]);
        int i303 = i302 + 1;
        game.bits[i302].moveTo(game.areas[174]);
        int i304 = i303 + 1;
        game.bits[i303].moveTo(game.areas[174]);
        int i305 = i304 + 1;
        game.bits[i304].moveTo(game.areas[174]);
        int i306 = i305 + 1;
        game.bits[i305].moveTo(game.areas[174]);
        int i307 = i306 + 1;
        game.bits[i306].moveTo(game.areas[174]);
        int i308 = i307 + 1;
        game.bits[i307].moveTo(game.areas[174]);
        int i309 = i308 + 1;
        game.bits[i308].moveTo(game.areas[174]);
        int i310 = i309 + 1;
        game.bits[i309].moveTo(game.areas[174]);
        int i311 = i310 + 1;
        game.bits[i310].moveTo(game.areas[174]);
        int i312 = i311 + 1;
        game.bits[i311].moveTo(game.areas[174]);
        int i313 = i312 + 1;
        game.bits[i312].moveTo(game.areas[174]);
        int i314 = i313 + 1;
        game.bits[i313].moveTo(game.areas[174]);
        int i315 = i314 + 1;
        game.bits[i314].moveTo(game.areas[174]);
        int i316 = i315 + 1;
        game.bits[i315].moveTo(game.areas[174]);
        int i317 = i316 + 1;
        game.bits[i316].moveTo(game.areas[174]);
        int i318 = i317 + 1;
        game.bits[i317].moveTo(game.areas[174]);
        int i319 = i318 + 1;
        game.bits[i318].moveTo(game.areas[174]);
        int i320 = i319 + 1;
        game.bits[i319].moveTo(game.areas[174]);
        int i321 = i320 + 1;
        game.bits[i320].moveTo(game.areas[174]);
        int i322 = i321 + 1;
        game.bits[i321].moveTo(game.areas[174]);
        int i323 = i322 + 1;
        game.bits[i322].moveTo(game.areas[176]);
        int i324 = i323 + 1;
        game.bits[i323].moveTo(game.areas[176]);
        int i325 = i324 + 1;
        game.bits[i324].moveTo(game.areas[176]);
        int i326 = i325 + 1;
        game.bits[i325].moveTo(game.areas[176]);
        int i327 = i326 + 1;
        game.bits[i326].moveTo(game.areas[176]);
        int i328 = i327 + 1;
        game.bits[i327].moveTo(game.areas[176]);
        int i329 = i328 + 1;
        game.bits[i328].moveTo(game.areas[176]);
        int i330 = i329 + 1;
        game.bits[i329].moveTo(game.areas[176]);
        int i331 = i330 + 1;
        game.bits[i330].moveTo(game.areas[176]);
        int i332 = i331 + 1;
        game.bits[i331].moveTo(game.areas[176]);
        int i333 = i332 + 1;
        game.bits[i332].moveTo(game.areas[176]);
        int i334 = i333 + 1;
        game.bits[i333].moveTo(game.areas[176]);
        int i335 = i334 + 1;
        game.bits[i334].moveTo(game.areas[176]);
        int i336 = i335 + 1;
        game.bits[i335].moveTo(game.areas[176]);
        int i337 = i336 + 1;
        game.bits[i336].moveTo(game.areas[176]);
        int i338 = i337 + 1;
        game.bits[i337].moveTo(game.areas[176]);
        int i339 = i338 + 1;
        game.bits[i338].moveTo(game.areas[176]);
        int i340 = i339 + 1;
        game.bits[i339].moveTo(game.areas[176]);
        int i341 = i340 + 1;
        game.bits[i340].moveTo(game.areas[176]);
        int i342 = i341 + 1;
        game.bits[i341].moveTo(game.areas[176]);
        int i343 = i342 + 1;
        game.bits[i342].moveTo(game.areas[176]);
        int i344 = i343 + 1;
        game.bits[i343].moveTo(game.areas[176]);
        int i345 = i344 + 1;
        game.bits[i344].moveTo(game.areas[176]);
        int i346 = i345 + 1;
        game.bits[i345].moveTo(game.areas[176]);
        int i347 = i346 + 1;
        game.bits[i346].moveTo(game.areas[176]);
        int i348 = i347 + 1;
        game.bits[i347].moveTo(game.areas[176]);
        int i349 = i348 + 1;
        game.bits[i348].moveTo(game.areas[176]);
        int i350 = i349 + 1;
        game.bits[i349].moveTo(game.areas[176]);
        int i351 = i350 + 1;
        game.bits[i350].moveTo(game.areas[176]);
        int i352 = i351 + 1;
        game.bits[i351].moveTo(game.areas[176]);
        int i353 = i352 + 1;
        game.bits[i352].moveTo(game.areas[176]);
        int i354 = i353 + 1;
        game.bits[i353].moveTo(game.areas[176]);
        int i355 = i354 + 1;
        game.bits[i354].moveTo(game.areas[176]);
        int i356 = i355 + 1;
        game.bits[i355].moveTo(game.areas[176]);
        int i357 = i356 + 1;
        game.bits[i356].moveTo(game.areas[176]);
        int i358 = i357 + 1;
        game.bits[i357].moveTo(game.areas[176]);
        int i359 = i358 + 1;
        game.bits[i358].moveTo(game.areas[176]);
        int i360 = i359 + 1;
        game.bits[i359].moveTo(game.areas[176]);
        int i361 = i360 + 1;
        game.bits[i360].moveTo(game.areas[176]);
        int i362 = i361 + 1;
        game.bits[i361].moveTo(game.areas[176]);
        int i363 = i362 + 1;
        game.bits[i362].moveTo(game.areas[174]);
        int i364 = i363 + 1;
        game.bits[i363].moveTo(game.areas[174]);
        int i365 = i364 + 1;
        game.bits[i364].moveTo(game.areas[176]);
        int i366 = i365 + 1;
        game.bits[i365].moveTo(game.areas[176]);
        int i367 = i366 + 1;
        game.bits[i366].moveTo(game.areas[176]);
        int i368 = i367 + 1;
        game.bits[i367].moveTo(game.areas[174]);
        int i369 = i368 + 1;
        game.bits[i368].moveTo(game.areas[174]);
        int i370 = i369 + 1;
        game.bits[i369].moveTo(game.areas[176]);
        int i371 = i370 + 1;
        game.bits[i370].moveTo(game.areas[176]);
        int i372 = i371 + 1;
        game.bits[i371].moveTo(game.areas[176]);
        int i373 = i372 + 1;
        game.bits[i372].moveTo(game.areas[176]);
        int i374 = i373 + 1;
        game.bits[i373].moveTo(game.areas[176]);
        int i375 = i374 + 1;
        game.bits[i374].moveTo(game.areas[176]);
        int i376 = i375 + 1;
        game.bits[i375].moveTo(game.areas[176]);
        int i377 = i376 + 1;
        game.bits[i376].moveTo(game.areas[176]);
        int i378 = i377 + 1;
        game.bits[i377].moveTo(game.areas[174]);
        int i379 = i378 + 1;
        game.bits[i378].moveTo(game.areas[174]);
        int i380 = i379 + 1;
        game.bits[i379].moveTo(game.areas[174]);
        int i381 = i380 + 1;
        game.bits[i380].moveTo(game.areas[174]);
        int i382 = i381 + 1;
        game.bits[i381].moveTo(game.areas[174]);
        int i383 = i382 + 1;
        game.bits[i382].moveTo(game.areas[174]);
        int i384 = i383 + 1;
        game.bits[i383].moveTo(game.areas[176]);
        int i385 = i384 + 1;
        game.bits[i384].moveTo(game.areas[176]);
        int i386 = i385 + 1;
        game.bits[i385].moveTo(game.areas[176]);
        int i387 = i386 + 1;
        game.bits[i386].moveTo(game.areas[176]);
        int i388 = i387 + 1;
        game.bits[i387].moveTo(game.areas[176]);
        int i389 = i388 + 1;
        game.bits[i388].moveTo(game.areas[176]);
        int i390 = i389 + 1;
        game.bits[i389].moveTo(game.areas[176]);
        int i391 = i390 + 1;
        game.bits[i390].moveTo(game.areas[176]);
        int i392 = i391 + 1;
        game.bits[i391].moveTo(game.areas[176]);
        int i393 = i392 + 1;
        game.bits[i392].moveTo(game.areas[176]);
        int i394 = i393 + 1;
        game.bits[i393].moveTo(game.areas[176]);
        int i395 = i394 + 1;
        game.bits[i394].moveTo(game.areas[176]);
        int i396 = i395 + 1;
        game.bits[i395].moveTo(game.areas[176]);
        int i397 = i396 + 1;
        game.bits[i396].moveTo(game.areas[176]);
        int i398 = i397 + 1;
        game.bits[i397].moveTo(game.areas[176]);
        int i399 = i398 + 1;
        game.bits[i398].moveTo(game.areas[176]);
        int i400 = i399 + 1;
        game.bits[i399].moveTo(game.areas[176]);
        int i401 = i400 + 1;
        game.bits[i400].moveTo(game.areas[176]);
        int i402 = i401 + 1;
        game.bits[i401].moveTo(game.areas[174]);
        int i403 = i402 + 1;
        game.bits[i402].moveTo(game.areas[174]);
        int i404 = i403 + 1;
        game.bits[i403].moveTo(game.areas[174]);
        int i405 = i404 + 1;
        game.bits[i404].moveTo(game.areas[174]);
        int i406 = i405 + 1;
        game.bits[i405].moveTo(game.areas[174]);
        int i407 = i406 + 1;
        game.bits[i406].moveTo(game.areas[174]);
        int i408 = i407 + 1;
        game.bits[i407].moveTo(game.areas[174]);
        int i409 = i408 + 1;
        game.bits[i408].moveTo(game.areas[174]);
        int i410 = i409 + 1;
        game.bits[i409].moveTo(game.areas[174]);
        int i411 = i410 + 1;
        game.bits[i410].moveTo(game.areas[174]);
        int i412 = i411 + 1;
        game.bits[i411].moveTo(game.areas[174]);
        int i413 = i412 + 1;
        game.bits[i412].moveTo(game.areas[174]);
        int i414 = i413 + 1;
        game.bits[i413].moveTo(game.areas[174]);
        int i415 = i414 + 1;
        game.bits[i414].moveTo(game.areas[174]);
        int i416 = i415 + 1;
        game.bits[i415].moveTo(game.areas[176]);
        int i417 = i416 + 1;
        game.bits[i416].moveTo(game.areas[176]);
        int i418 = i417 + 1;
        game.bits[i417].moveTo(game.areas[176]);
        int i419 = i418 + 1;
        game.bits[i418].moveTo(game.areas[176]);
        int i420 = i419 + 1;
        game.bits[i419].moveTo(game.areas[176]);
        int i421 = i420 + 1;
        game.bits[i420].moveTo(game.areas[176]);
        int i422 = i421 + 1;
        game.bits[i421].moveTo(game.areas[176]);
        int i423 = i422 + 1;
        game.bits[i422].moveTo(game.areas[176]);
        int i424 = i423 + 1;
        game.bits[i423].moveTo(game.areas[176]);
        int i425 = i424 + 1;
        game.bits[i424].moveTo(game.areas[176]);
        int i426 = i425 + 1;
        game.bits[i425].moveTo(game.areas[185]);
        int i427 = i426 + 1;
        game.bits[i426].moveTo(game.areas[163]);
        int i428 = i427 + 1;
        game.bits[i427].moveTo(game.areas[163]);
        game.newgame = true;
        game.mod.reset();
        if (game.varianttype.equals("base") || game.varianttype.startsWith("base2")) {
            game.gameloading = true;
            baseSetup();
            game.gameloading = false;
        }
        if (game.varianttype.startsWith("base2")) {
            game.interpreter.execute("<auto> silent ");
            game.interpreter.execute("<auto><~Scribe.141~>312 <~Scribe.142~><~Game.2038~><~Scribe.143~><~Game.1965~><~Scribe.144~>");
            game.interpreter.execute("<auto> noisy ");
        }
        game.setTurn(1);
    }

    public synchronized void refreshBoard() {
        int i = 1;
        synchronized (this) {
            int CCards = 0;
            int SCards = 0;
            int FCards = 0;
            game.updateStatus();
            if (game.boardtype.equals("wotr")) {
                if (game.areas[180].numPieces() > 0) {
                    for (int t = 0; t < game.areas[180].numPieces(); t++) {
                        if (game.areas[180].get(t) instanceof FreeCharacterCard) {
                            CCards++;
                        }
                        if (game.areas[180].get(t) instanceof FreeStrategyCard) {
                            SCards++;
                        }
                        if (game.areas[180].get(t) instanceof FreeFactionCard) {
                            FCards++;
                        }
                    }
                    if (CCards + SCards > 6 || FCards > 4) {
                        game.controls.jtp.setForegroundAt(0, new Color(255, 0, 0));
                    } else {
                        game.controls.jtp.setForegroundAt(0, new Color(0, 0, 0));
                    }
                    if (game.isWOME.booleanValue()) {
                        game.controls.jtp.setTitleAt(0, String.valueOf(Messages.getString("Controls.27")) + " " + CCards + Messages.getString("Game.1226") + SCards + Messages.getString("Game.1227") + FCards + "F");
                    } else {
                        game.controls.jtp.setTitleAt(0, String.valueOf(Messages.getString("Controls.27")) + " " + CCards + Messages.getString("Game.1226") + SCards + Messages.getString("Game.1227"));
                    }
                } else {
                    game.controls.jtp.setTitleAt(0, Messages.getString("Controls.27"));
                }
                CCards = 0;
                SCards = 0;
                int FCards2 = 0;
                if (game.areas[181].numPieces() > 0) {
                    for (int t2 = 0; t2 < game.areas[181].numPieces(); t2++) {
                        if (game.areas[181].get(t2) instanceof ShadowCharacterCard) {
                            CCards++;
                        }
                        if (game.areas[181].get(t2) instanceof ShadowStrategyCard) {
                            SCards++;
                        }
                        if (game.areas[181].get(t2) instanceof ShadowFactionCard) {
                            FCards2++;
                        }
                    }
                    if (CCards + SCards > 6 || FCards2 > 4) {
                        game.controls.jtp.setForegroundAt((game.isWOME.booleanValue() ? 1 : 0) + 1, new Color(255, 0, 0));
                    } else {
                        game.controls.jtp.setForegroundAt((game.isWOME.booleanValue() ? 1 : 0) + 1, new Color(0, 0, 0));
                    }
                    if (game.isWOME.booleanValue()) {
                        JTabbedPane jTabbedPane = game.controls.jtp;
                        if (!game.isWOME.booleanValue()) {
                            i = 0;
                        }
                        jTabbedPane.setTitleAt(i + 1, String.valueOf(Messages.getString("Controls.28")) + " " + CCards + Messages.getString("Game.1233") + SCards + Messages.getString("Game.1235") + FCards2 + "F");
                    } else {
                        JTabbedPane jTabbedPane2 = game.controls.jtp;
                        if (!game.isWOME.booleanValue()) {
                            i = 0;
                        }
                        jTabbedPane2.setTitleAt(i + 1, String.valueOf(Messages.getString("Controls.28")) + " " + CCards + Messages.getString("Game.1233") + SCards + Messages.getString("Game.1235"));
                    }
                } else {
                    JTabbedPane jTabbedPane3 = game.controls.jtp;
                    if (!game.isWOME.booleanValue()) {
                        i = 0;
                    }
                    jTabbedPane3.setTitleAt(i + 1, Messages.getString("Controls.28"));
                }
                if (game.isWOME.booleanValue()) {
                    if (game.areas[202].numPieces() > 0) {
                        game.controls.jtp.setTitleAt(1, String.valueOf(game.areas[202].numPieces()));
                    } else {
                        game.controls.jtp.setTitleAt(1, "F+");
                    }
                    if (game.areas[203].numPieces() > 0) {
                        game.controls.jtp.setTitleAt(3, String.valueOf(game.areas[203].numPieces()));
                    } else {
                        game.controls.jtp.setTitleAt(3, "S+");
                    }
                }
            }
            if (false) {
                if (CCards + SCards > 0) {
                    game.controls.jtp.setTitleAt(0, String.valueOf(Messages.getString("Controls.27")) + " " + CCards + "/" + SCards);
                } else {
                    game.controls.jtp.setTitleAt(0, Messages.getString("Controls.27"));
                }
                int CCards2 = 0;
                int SCards2 = 0;
                for (int t4 = 0; t4 < game.areas[181].numPieces(); t4++) {
                    if (game.areas[181].get(t4) instanceof ShadowStoryCard) {
                        CCards2++;
                    }
                    if (game.areas[181].get(t4) instanceof GenericCard) {
                        SCards2++;
                    }
                }
                if (CCards2 + SCards2 > 6) {
                    game.controls.jtp.setForegroundAt(1, new Color(255, 0, 0));
                } else {
                    game.controls.jtp.setForegroundAt(1, new Color(0, 0, 0));
                }
                if (CCards2 + SCards2 > 0) {
                    game.controls.jtp.setTitleAt(1, String.valueOf(Messages.getString("Controls.28")) + " " + CCards2 + "/" + SCards2);
                } else {
                    game.controls.jtp.setTitleAt(1, Messages.getString("Controls.28"));
                }
            }
            if (game.boardtype.equals("wotr")) {
                if (game.areas[105].numPieces() > 0) {
                    game.siegeOverlays[0] = new ImageIcon("images/OverlayEreborSiege.png").getImage();
                } else {
                    game.siegeOverlays[0] = null;
                }
                if (game.areas[106].numPieces() > 0) {
                    game.siegeOverlays[1] = new ImageIcon("images/OverlayGreyHavensSiege.png").getImage();
                } else {
                    game.siegeOverlays[1] = null;
                }
                if (game.areas[107].numPieces() > 0) {
                    game.siegeOverlays[2] = new ImageIcon("images/OverlayRivendellSiege.png").getImage();
                } else {
                    game.siegeOverlays[2] = null;
                }
                if (game.areas[108].numPieces() > 0) {
                    game.siegeOverlays[3] = new ImageIcon("images/OverlayWoodlandRealmSiege.png").getImage();
                } else {
                    game.siegeOverlays[3] = null;
                }
                if (game.areas[109].numPieces() > 0) {
                    game.siegeOverlays[4] = new ImageIcon("images/OverlayLorienSiege.png").getImage();
                } else {
                    game.siegeOverlays[4] = null;
                }
                if (game.areas[110].numPieces() > 0) {
                    game.siegeOverlays[5] = new ImageIcon("images/OverlayHelmsDeepSiege.png").getImage();
                } else {
                    game.siegeOverlays[5] = null;
                }
                if (game.areas[111].numPieces() > 0) {
                    game.siegeOverlays[6] = new ImageIcon("images/OverlayMinasTirithSiege.png").getImage();
                } else {
                    game.siegeOverlays[6] = null;
                }
                if (game.areas[112].numPieces() > 0) {
                    game.siegeOverlays[7] = new ImageIcon("images/OverlayDolAmrothSiege.png").getImage();
                } else {
                    game.siegeOverlays[7] = null;
                }
                if (game.areas[155].numPieces() > 0) {
                    game.siegeOverlays[8] = new ImageIcon("images/OverlayMtGundabadSiege.png").getImage();
                } else {
                    game.siegeOverlays[8] = null;
                }
                if (game.areas[156].numPieces() > 0) {
                    game.siegeOverlays[9] = new ImageIcon("images/OverlayMoriaSiege.png").getImage();
                } else {
                    game.siegeOverlays[9] = null;
                }
                if (game.areas[157].numPieces() > 0) {
                    game.siegeOverlays[10] = new ImageIcon("images/OverlayDolGuldurSiege.png").getImage();
                } else {
                    game.siegeOverlays[10] = null;
                }
                if (game.areas[158].numPieces() > 0) {
                    game.siegeOverlays[11] = new ImageIcon("images/OverlayOrthancSiege.png").getImage();
                } else {
                    game.siegeOverlays[11] = null;
                }
                if (game.areas[159].numPieces() > 0) {
                    game.siegeOverlays[12] = new ImageIcon("images/OverlayMorannonSiege.png").getImage();
                } else {
                    game.siegeOverlays[12] = null;
                }
                if (game.areas[160].numPieces() > 0) {
                    game.siegeOverlays[13] = new ImageIcon("images/OverlayBaradDurSiege.png").getImage();
                } else {
                    game.siegeOverlays[13] = null;
                }
                if (game.areas[161].numPieces() > 0) {
                    game.siegeOverlays[14] = new ImageIcon("images/OverlayMinasMorgulSiege.png").getImage();
                } else {
                    game.siegeOverlays[14] = null;
                }
                if (game.areas[162].numPieces() > 0) {
                    game.siegeOverlays[15] = new ImageIcon("images/OverlayUmbarSiege.png").getImage();
                } else {
                    game.siegeOverlays[15] = null;
                }
            }
            game.win.validate();
            game.win.repaint();
            game.boardlabel.repaint();
        }
    }
    /**
     * Determine which scenario to load from database based on variant type.
     * 
     * Each scenario contains a complete piece set for that variant (not additive).
     * 
     * Variant mappings:
     * - "base2" → "base" (428 pieces)
     * - "base2[T]" → "base_t" (429 pieces)
     * - "expansion2[L]" → "lome" (452 pieces, includes base)
     * - "expansion2[LT]" → "lome_t" (453 pieces, includes base + Treebeard)
     * - "base[WT]" → "wome_t" (540 pieces, includes base + WOME)
     * - "expansion2[WLT]" → "wome_lome_t" (564 pieces, includes base + WOME + LOME)
     * 
     * @return Scenario ID to load
     */
    private String getScenarioForVariant() {
        if (Game.varianttype == null) {
            return "base";
        }
        
        // Map variant string to scenario_id
        switch (Game.varianttype) {
            case "base2":
                return "base";
            case "base2[T]":
                return "base_t";
            case "expansion2[L]":
                return "lome";
            case "expansion2[LT]":
                return "lome_t";
            case "base[WT]":
                return "wome_t";
            case "expansion2[WLT]":
                return "wome_lome_t";
            default:
                System.err.println("[INIT] Unknown variant type: " + Game.varianttype + ", defaulting to base");
                return "base";
        }
    }

    private GamePiece createPieceFromSetup(wotr.services.ScenarioLoader.PieceSetup setup) {
        Area area = game.areas[setup.areaId];
        
        switch (setup.pieceClass) {
            // === CARDS ===
            case "FreeCharacterCard":
                return new FreeCharacterCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            case "FreeStrategyCard":
                return new FreeStrategyCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            case "ShadowCharacterCard":
                return new ShadowCharacterCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            case "ShadowStrategyCard":
                return new ShadowStrategyCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            
            // === BATTLE CARDS ===
            case "FreeBattleCard":
                // Provide defaults for NULL database fields
                String fpSmallBack = (setup.smallBackImage != null) ? setup.smallBackImage : Messages.getLanguageLocation("images/FPAtextcard.gif");
                String fpBigBack = (setup.bigBackImage != null) ? setup.bigBackImage : Messages.getLanguageLocation("images/FPAC.jpg");
                String fpCardType = (setup.cardType != null) ? setup.cardType : Messages.getString("FreeArmyCard.2");
                return new FreeBattleCard(area, 
                    setup.smallImage, setup.bigImage, setup.cardName,
                    fpSmallBack, fpBigBack, fpCardType);
            case "ShadowBattleCard":
                // Provide defaults for NULL database fields
                String saSmallBack = (setup.smallBackImage != null) ? setup.smallBackImage : Messages.getLanguageLocation("images/SAAtextcard.gif");
                String saBigBack = (setup.bigBackImage != null) ? setup.bigBackImage : Messages.getLanguageLocation("images/SAAC.jpg");
                String saCardType = (setup.cardType != null) ? setup.cardType : Messages.getString("ShadowArmyCard.2");
                return new ShadowBattleCard(area,
                    setup.smallImage, setup.bigImage, setup.cardName,
                    saSmallBack, saBigBack, saCardType);
            
            // === ACTION DICE ===
            case "FreeActionDie":
                return new FreeActionDie(area);
            case "ShadowActionDie":
                return new ShadowActionDie(area);
            
            // === HUNT TILES ===
            case "HuntTile":
                String tileType = (setup.cardName != null) ? setup.cardName : "";
                String tileImage = (setup.smallImage != null) ? setup.smallImage : "";
                return new HuntTile(area, tileType, tileImage);
            
            // === SPECIAL DICE ===
            case "NaryaDice":
                return new NaryaDice(area, game);
            case "NenyaDice":
                return new NenyaDice(area, game);
            case "VilyaDice":
                return new VilyaDice(area, game);
            case "BalrogDice":
                return new BalrogDice(area, game);
            case "GothmogDice":
                return new GothmogDice(area, game);
            
            // === CHITS ===
            case "Chit":
                return new Chit(area, setup.cardName != null ? setup.cardName : "", 
                    setup.smallImage != null ? setup.smallImage : "");
            case "TwoChit":
                return new TwoChit(area, setup.cardName != null ? setup.cardName : "",
                    setup.smallImage != null ? setup.smallImage : "",
                    setup.bigImage != null ? setup.bigImage : "");
            
            // === UNITS (by class name) ===
            case "UnitGondorRegular": return new UnitGondorRegular(area);
            case "UnitGondorElite": return new UnitGondorElite(area);
            case "UnitGondorLeader": return new UnitGondorLeader(area);
            case "UnitRohanRegular": return new UnitRohanRegular(area);
            case "UnitRohanElite": return new UnitRohanElite(area);
            case "UnitRohanLeader": return new UnitRohanLeader(area);
            case "UnitElvenRegular": return new UnitElvenRegular(area);
            case "UnitElvenElite": return new UnitElvenElite(area);
            case "UnitElvenLeader": return new UnitElvenLeader(area);
            case "UnitDwarvenRegular": return new UnitDwarvenRegular(area);
            case "UnitDwarvenElite": return new UnitDwarvenElite(area);
            case "UnitDwarvenLeader": return new UnitDwarvenLeader(area);
            case "UnitNorthRegular": return new UnitNorthRegular(area);
            case "UnitNorthElite": return new UnitNorthElite(area);
            case "UnitNorthLeader": return new UnitNorthLeader(area);
            case "UnitSauronRegular": return new UnitSauronRegular(area);
            case "UnitSauronElite": return new UnitSauronElite(area);
            case "UnitIsengardRegular": return new UnitIsengardRegular(area);
            case "UnitIsengardElite": return new UnitIsengardElite(area);
            case "UnitSouthronRegular": return new UnitSouthronRegular(area);
            case "UnitSouthronElite": return new UnitSouthronElite(area);
            
            // === NAMED CHARACTERS ===
            case "UnitGandalf": return new UnitGandalf(area, 3, 3);
            case "UnitStrider": return new UnitStrider(area, 2, 2);
            case "UnitLegolas": return new UnitLegolas(area);
            case "UnitGimli": return new UnitGimli(area);
            case "UnitBoromir": return new UnitBoromir(area);
            case "UnitMerry": return new UnitMerry(area);
            case "UnitPippin": return new UnitPippin(area);
            case "UnitGaladriel": return new UnitGaladriel(area);
            case "UnitElrond": return new UnitElrond(area);
            case "UnitWitchKing": return new UnitWitchKing(area);
            case "UnitSmeagol": return new UnitSmeagol(area);
            case "UnitSaruman":
                game.SarumanNo = 0;
                return new UnitSaruman(area);
            case "UnitMouth": return new UnitMouth(area, 2, 2);
            case "UnitMouth2": return new UnitMouth2(area);
            case "UnitGothmog": return new UnitGothmog(area, 1, 1);
            case "UnitNazgul": return new UnitNazgul(area, 1, 1);
            
            // === FELLOWSHIP ===
            case "UnitFellowship":
                return new UnitFellowship(area);
            
            // === CONTROL MARKERS ===
            case "UnitFreeControlLarge":
                return new UnitFreeControlLarge(area);
            case "UnitShadowControlLarge":
                return new UnitShadowControlLarge(area);
            
            // === SPECIAL UNITS ===
            case "UnitCotR": return new UnitCotR(area);
            case "UnitBalrog": return new UnitBalrog(area);
            case "UnitTower": return new UnitTower(area);
            case "UnitTrebuchet": return new UnitTrebuchet(area);
            case "UnitEnt": return new UnitEnt(area);
            case "UnitEagle": return new UnitEagle(area);
            case "UnitDeadmen": return new UnitDeadmen(area);
            case "UnitDunlending": return new UnitDunlending(area);
            case "UnitCorsair": return new UnitCorsair(area);
            case "UnitSpider": return new UnitSpider(area);
            case "UnitTreebeard": return new UnitTreebeard(area);
            
            // === FACTION CARDS (WOME) ===
            case "FreeFactionCard":
                return new FreeFactionCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            case "ShadowFactionCard":
                return new ShadowFactionCard(area, setup.smallImage, setup.bigImage, setup.cardName);
            
            // === FACTION DICE (WOME) ===
            case "FreeFactionDice":
                return new FreeFactionDice(area, game);
            case "ShadowFactionDice":
                return new ShadowFactionDice(area, game);
        }
        
        System.err.println("[DB Load] Unknown piece class: " + setup.pieceClass + " (ID: " + setup.pieceId + ")");
        return null;
    }

    /**
     * Export bits array to JSON for debugging/analysis
     * Outputs to specified filename in data/ directory
     */
    public void exportBitsArrayToJson(String filename) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"variant\": \"").append(game.varianttype).append("\",\n");
        json.append("  \"piece_count\": ").append(game.numBits).append(",\n");
        json.append("  \"array_size\": ").append(game.bits.length).append(",\n");
        json.append("  \"source\": \"").append(game.prefs.disableDatabase ? "bitInit()\"" : "wotr_game.db\"").append(",\n");
        json.append("  \"pieces\": [\n");
        
        for (int i = 0; i < game.bits.length; i++) {
            GamePiece piece = game.bits[i];
            
            if (i > 0) {
                json.append(",\n");
            }
            
            json.append("    {\n");
            json.append("      \"index\": ").append(i).append(",\n");
            
            if (piece == null) {
                json.append("      \"type\": null,\n");
                json.append("      \"class\": null,\n");
                json.append("      \"area_index\": null,\n");
                json.append("      \"area_game_id\": null,\n");
                json.append("      \"area_name\": null,\n");
                json.append("      \"small_image\": null,\n");
                json.append("      \"big_image\": null,\n");
                json.append("      \"card_name\": null\n");
            } else {
                json.append("      \"type\": \"").append(piece.getClass().getSimpleName()).append("\",\n");
                json.append("      \"class\": \"").append(piece.getClass().getName()).append("\",\n");
                
                Area location = piece.currentLocation();
                if (location != null) {
                    // Find area index
                    int areaIndex = -1;
                    for (int j = 0; j < game.areas.length; j++) {
                        if (game.areas[j] == location) {
                            areaIndex = j;
                            break;
                        }
                    }
                    // Get raw area name (e.g., "Area.8") and localized name from messages.properties
                    String areaGameId = location.name();
                    String gameId = areaGameId.replace("<~", "").replace("~>", "");
                    String areaName = Messages.getString(gameId);
                    
                    json.append("      \"area_index\": ").append(areaIndex).append(",\n");
                    json.append("      \"area_game_id\": \"").append(escapeJson(areaGameId)).append("\",\n");
                    json.append("      \"area_name\": \"").append(escapeJson(areaName)).append("\",\n");
                } else {
                    json.append("      \"area_index\": null,\n");
                    json.append("      \"area_game_id\": null,\n");
                    json.append("      \"area_name\": null,\n");
                }
                
                // Add card-specific fields - check BattleCards FIRST (most specific)
                if (piece instanceof FreeBattleCard || piece instanceof ShadowBattleCard) {
                    // BattleCards need 6 fields: front images, back images, name, and type
                    Card card = (Card) piece;
                    String smallImg = card.smallImageLocation();
                    String bigImg = card.bigImageLocation();
                    String cardName = card.name();
                    String cardType = extractBattleCardType(piece);
                    String smallBack = extractBattleCardBackImage(piece, true);
                    String bigBack = extractBattleCardBackImage(piece, false);
                    
                    json.append("      \"small_image\": \"").append(escapeJson(smallImg != null ? smallImg : "")).append("\",\n");
                    json.append("      \"big_image\": \"").append(escapeJson(bigImg != null ? bigImg : "")).append("\",\n");
                    json.append("      \"card_name\": \"").append(escapeJson(cardName != null ? cardName : "")).append("\",\n");
                    json.append("      \"small_back_image\": \"").append(escapeJson(smallBack != null ? smallBack : "")).append("\",\n");
                    json.append("      \"big_back_image\": \"").append(escapeJson(bigBack != null ? bigBack : "")).append("\",\n");
                    json.append("      \"card_type\": \"").append(escapeJson(cardType != null ? cardType : "")).append("\"\n");
                } else if (piece instanceof Card) {
                    Card card = (Card) piece;
                    String smallImg = card.smallImageLocation();
                    String bigImg = card.bigImageLocation();
                    String cardName = card.name();
                    
                    // If no specific image paths, extract from class static backs
                    if (smallImg == null || smallImg.isEmpty()) {
                        smallImg = extractCardBackImagePath(card, true);
                    }
                    if (bigImg == null || bigImg.isEmpty()) {
                        bigImg = extractCardBackImagePath(card, false);
                    }
                    
                    json.append("      \"small_image\": \"").append(escapeJson(smallImg != null ? smallImg : "")).append("\",\n");
                    json.append("      \"big_image\": \"").append(escapeJson(bigImg != null ? bigImg : "")).append("\",\n");
                    json.append("      \"card_name\": \"").append(escapeJson(cardName != null ? cardName : "")).append("\"\n");
                } else if (piece instanceof TwoChit) {
                    // Handle TwoChit specially - needs type, front image, and back image
                    TwoChit chit = (TwoChit) piece;
                    String type = chit.type();
                    // TwoChit stores two images - need to access via reflection or add getters
                    String frontImg = extractChitImage(chit, true);
                    String backImg = extractChitImage(chit, false);
                    
                    json.append("      \"small_image\": \"").append(escapeJson(frontImg != null ? frontImg : "")).append("\",\n");
                    json.append("      \"big_image\": \"").append(escapeJson(backImg != null ? backImg : "")).append("\",\n");
                    json.append("      \"card_name\": \"").append(escapeJson(type != null ? type : "")).append("\"\n");
                } else if (piece instanceof Chit) {
                    // Handle Chit specially - needs type and image
                    Chit chit = (Chit) piece;
                    String type = chit.type();
                    String image = extractChitImage(chit, true);
                    
                    json.append("      \"small_image\": \"").append(escapeJson(image != null ? image : "")).append("\",\n");
                    json.append("      \"big_image\": null,\n");
                    json.append("      \"card_name\": \"").append(escapeJson(type != null ? type : "")).append("\"\n");
                } else {
                    json.append("      \"small_image\": null,\n");
                    json.append("      \"big_image\": null,\n");
                    json.append("      \"card_name\": null\n");
                }
            }
            
            json.append("    }");
        }
        
        json.append("\n  ]\n");
        json.append("}\n");
        
        // Write to file
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Write JSON to file
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(json.toString());
            }
            System.out.println("[EXPORT] Bits array exported to: " + filename);
        } catch (IOException e) {
            System.err.println("[EXPORT] Failed to write bits array to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    /**
     * Extract image path from Chit or TwoChit using reflection
     * @param chit The Chit or TwoChit instance
     * @param frontImage true for front/main image, false for back image (TwoChit only)
     * @return The image file path
     */
    private String extractChitImage(GamePiece chit, boolean frontImage) {
        try {
            if (chit instanceof TwoChit) {
                String fieldName = frontImage ? "imageLocation1" : "imageLocation2";
                java.lang.reflect.Field field = TwoChit.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (String) field.get(chit);
            } else if (chit instanceof Chit) {
                java.lang.reflect.Field field = Chit.class.getDeclaredField("imageLocation");
                field.setAccessible(true);
                return (String) field.get(chit);
            }
        } catch (Exception e) {
            System.err.println("[EXPORT] Failed to extract image from Chit: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Extract cardType from BattleCard using reflection
     */
    private String extractBattleCardType(GamePiece battleCard) {
        try {
            Class<?> clazz = battleCard.getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField("cardType");
            field.setAccessible(true);
            return (String) field.get(battleCard);
        } catch (Exception e) {
            System.err.println("[EXPORT] Failed to extract cardType from BattleCard: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Extract back image from BattleCard using reflection
     * @param battleCard The FreeBattleCard or ShadowBattleCard instance
     * @param isSmall true for smallBack, false for bigBack
     */
    private String extractBattleCardBackImage(GamePiece battleCard, boolean isSmall) {
        try {
            Class<?> clazz = battleCard.getClass();
            String fieldName = isSmall ? "smallBack" : "bigBack";
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            javax.swing.ImageIcon icon = (javax.swing.ImageIcon) field.get(battleCard);
            if (icon != null) {
                // Extract path from ImageIcon description
                return icon.getDescription();
            }
        } catch (Exception e) {
            System.err.println("[EXPORT] Failed to extract back image from BattleCard: " + e.getMessage());
        }
        return null;
    }
    /**
     * Extract card back image path from Card class static fields
     * @param card The card instance
     * @param isSmall true for small image, false for big image
     * @return Image path extracted from static ImageIcon field
     */
    private String extractCardBackImagePath(Card card, boolean isSmall) {
        try {
            // Get the static field name based on card class
            String fieldName = isSmall ? "smallBack" : "bigBack";
            java.lang.reflect.Field field = card.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            
            // Get the ImageIcon from the static field
            Object fieldValue = field.get(null);  // null for static field
            if (fieldValue instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) fieldValue;
                String desc = icon.getDescription();
                if (desc != null && !desc.isEmpty() && !desc.contains("@")) {
                    return desc;
                }
            }
        } catch (Exception e) {
            // Field doesn't exist or can't access - use fallback based on class name
        }
        
        // Fallback: infer path from class name
        String className = card.getClass().getSimpleName();
        if (className.contains("FreeStrategy")) {
            return isSmall ? "images/FPAtextcard.gif" : "images/FPAC.jpg";
        } else if (className.contains("FreeCharacter")) {
            return isSmall ? "images/FPCtextcard.gif" : "images/FPCC.jpg";
        } else if (className.contains("ShadowStrategy")) {
            return isSmall ? "images/SAAtextcard.gif" : "images/SAAC.jpg";
        } else if (className.contains("ShadowCharacter")) {
            return isSmall ? "images/SACtextcard.gif" : "images/SACC.jpg";
        }
        
        return null;
    }
}
