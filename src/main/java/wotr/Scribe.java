package wotr;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/* renamed from: Scribe */
public class Scribe extends MouseAdapter implements KeyListener {

    /* renamed from: g */
    Graphics2D f16g;
    private Game game;
    private BufferedImage imageMap;
    boolean normalCursor = true;
    Point pressPoint;

    public Scribe(String mapName, Component f, Game game2) {
        Image i = new ImageIcon(mapName).getImage();
        this.imageMap = (BufferedImage)f.createImage(i.getWidth(f), i.getHeight(f));
        this.f16g = this.imageMap.createGraphics();
        this.f16g.drawImage(i, 0, 0, f);
        this.game = game2;
    }

    public void mouseClicked(MouseEvent e) {
        int pixel = 0;
        int x = (int) ((((double) e.getX()) * 100.0d) / ((double) Game.prefs.zoom));
        int y = (int) ((((double) e.getY()) * 100.0d) / ((double) Game.prefs.zoom));
        try {
            pixel = this.imageMap.getRGB(x, y);
        } catch (Exception e1) {
            System.out.print("x=" + x);
            System.out.print("y=" + y);
            System.out.print(this.imageMap.toString());
            e1.printStackTrace();
        }
        int pixel2 = pixel >>> 4;
        int p1 = pixel2 % 16;
        int pixel3 = pixel2 >>> 8;
        int pixel4 = ((pixel3 % 16) * 16) + p1 + (((pixel3 >>> 8) % 16) * 256);
        int i = 0;
        while (i < this.game.areaIDs.length && pixel4 != this.game.areaIDs[i]) {
            i++;
        }
        if (i < this.game.areaIDs.length) {
            Area clickedA = Game.areas[i];
            if (e.getClickCount() == 2) {
                if (clickedA == Game.areas[123]) {
                    this.game.controls.cbh.actionPerformed_sdrawc();
                }
                if (clickedA == Game.areas[124]) {
                    this.game.controls.cbh.actionPerformed_sdraws();
                }
                if (clickedA == Game.areas[121]) {
                    this.game.controls.cbh.actionPerformed_fdrawc();
                }
                if (clickedA == Game.areas[122]) {
                    this.game.controls.cbh.actionPerformed_fdraws();
                }
                if (Game.boardtype.equals("wotr")) {
                    if (clickedA == Game.areas[44] && Game.areas[44].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[155].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[155] && Game.areas[155].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[44].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[82] && Game.areas[82].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[105].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[105] && Game.areas[105].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[82].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[27] && Game.areas[27].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[107].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[107] && Game.areas[107].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[27].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[70] && Game.areas[70].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[108].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[108] && Game.areas[108].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[70].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[4] && Game.areas[4].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[106].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[106] && Game.areas[106].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[4].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[35] && Game.areas[35].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[156].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[156] && Game.areas[156].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[35].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[37] && Game.areas[37].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[109].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[109] && Game.areas[109].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[37].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[63] && Game.areas[63].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[157].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[157] && Game.areas[157].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[63].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[40] && Game.areas[40].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[158].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[158] && Game.areas[158].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[40].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[45] && Game.areas[45].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[110].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[110] && Game.areas[110].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[45].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[91] && Game.areas[91].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[159].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[159] && Game.areas[159].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[91].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[96] && Game.areas[96].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[160].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[160] && Game.areas[160].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[96].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[74] && Game.areas[74].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[111].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[111] && Game.areas[111].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[74].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[86] && Game.areas[86].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[161].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[161] && Game.areas[161].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[86].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[42] && Game.areas[42].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[112].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[112] && Game.areas[112].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[42].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[49] && Game.areas[49].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[162].name() + Messages.getKeyString("Scribe.144"));
                    }
                    if (clickedA == Game.areas[162] && Game.areas[162].numPieces() > 0) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents() + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + Game.areas[49].name() + Messages.getKeyString("Scribe.144"));
                    }
                }
                if (clickedA == Game.areas[114]) {
                    int c2 = 0;
                    while (true) {
                        if (c2 >= Game.areas[179].numPieces()) {
                            break;
                        } else if (Game.areas[179].get(c2) instanceof ShadowActionDie) {
                            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.372") + Game.areas[179].get(c2) + " ");
                            break;
                        } else {
                            c2++;
                        }
                    }
                } else if (this.game.selection.numPieces() == 0 && this.game.hasSPpassword()) {
                    int c3 = 0;
                    while (true) {
                        if (c3 >= Game.areas[176].numPieces()) {
                            break;
                        } else if (Game.areas[176].get(c3).type().equals(Messages.getString("UnitShadowControlMarker.0"))) {
                            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + Game.areas[176].get(c3) + " " + Messages.getKeyString("Scribe.142") + Game.areas[176].name() + Messages.getKeyString("Scribe.143") + this.game.highlight.name() + Messages.getKeyString("Scribe.144"));
                            break;
                        } else {
                            c3++;
                        }
                    }
                } else if (this.game.selection.numPieces() == 0 && this.game.hasFPpassword()) {
                    int c4 = 0;
                    while (true) {
                        if (c4 >= Game.areas[174].numPieces()) {
                            break;
                        } else if (Game.areas[174].get(c4).type().equals(Messages.getString("UnitFreeControlMarker.0"))) {
                            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + Game.areas[174].get(c4) + " " + Messages.getKeyString("Scribe.142") + Game.areas[174].name() + Messages.getKeyString("Scribe.143") + this.game.highlight.name() + Messages.getKeyString("Scribe.144"));
                            break;
                        } else {
                            c4++;
                        }
                    }
                }
            }
            if ((!e.isControlDown() && (e.getModifiers() & 4) != 4) || this.game.selection.numPieces() <= 0) {
                this.game.highlightArea(Game.areas[i], false);
            } else if (i == 144) {
            } else {
                if (this.game.highlight.name().equals(clickedA.name())) {
                    JOptionPane.showMessageDialog(Game.win, Messages.getString("Scribe.93"));
                } else {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.141") + this.game.selection.contents(this.game.highlight.IsHiddenFromOpponent() && !clickedA.IsHiddenFromOpponent(), this.game) + Messages.getKeyString("Scribe.142") + this.game.highlight.name() + Messages.getKeyString("Scribe.143") + clickedA.name() + Messages.getKeyString("Scribe.144"));
                }
            }
        }
    }

    private void expansionMoves(MouseEvent e, int i) {
        if (this.game.highlight == Game.areas[187] || this.game.highlight == Game.areas[185]) {
            Area gamehighlight = this.game.highlight;
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.7") + this.game.selection.contents(gamehighlight.IsHiddenFromOpponent() && !Game.areas[i].IsHiddenFromOpponent(), this.game) + Messages.getKeyString("Scribe.8") + gamehighlight.name() + Messages.getKeyString("Scribe.9") + Game.areas[i].name() + " ");
            this.game.talker.waitforemptyqueue();
            if (e.getY() > (Game.prefs.zoom * 720) / 100) {
                for (int j = 0; j < this.game.selection.numPieces(); j++) {
                    if (this.game.selection.get(j) instanceof UnitRecruitmentToken) {
                        ((UnitRecruitmentToken) this.game.selection.get(j)).flip();
                        ((UnitRecruitmentToken) this.game.selection.get(j)).localflipped = true;
                    }
                }
                this.game.talker.enqueue("<auto>" + Game.prefs.nick + Messages.getString("Scribe.1"));
            }
        } else if (this.game.selection.containsPiece(this.game.bits[this.game.SarumanNo])) {
            if (this.game.hasSPpassword() || !Game.areas[i].name().equals(Messages.getString("Scribe.4"))) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.6") + this.game.selection.contents() + Messages.getKeyString("Scribe.10") + this.game.highlight.name() + Messages.getKeyString("Scribe.12") + Game.areas[i].name() + " ");
                if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.13"))) {
                    ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.14") + Game.areas[179].get(1) + Messages.getKeyString("Scribe.15") + Game.areas[179].name() + Messages.getKeyString("Scribe.16") + Messages.getKeyString("Game.40") + " ");
                    int t = 0;
                    while (t < Game.areas[176].numPieces() && !Game.areas[176].get(t).type().equals(Messages.getString("Scribe.30"))) {
                        t++;
                    }
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.17") + Game.areas[176].get(t) + Messages.getKeyString("Scribe.18") + Game.areas[176].name() + Messages.getKeyString("Scribe.19") + Game.areas[84].name() + " ");
                    DrawGrimaCard();
                } else if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.23"))) {
                    Area poweroftheshadow = new Area("temp");
                    ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                    poweroftheshadow.addPiece(Game.areas[179].get(1));
                    ((ShadowActionDie) Game.areas[179].get(2)).SetState(13);
                    poweroftheshadow.addPiece(Game.areas[179].get(2));
                    ((ShadowActionDie) poweroftheshadow.get(0)).SetState(13);
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.24") + poweroftheshadow.contents() + Messages.getKeyString("Scribe.25") + Game.areas[179].name() + Messages.getKeyString("Scribe.26") + Game.areas[4].name() + " ");
                    poweroftheshadow.clearAllPieces();
                    for (int t2 = 0; t2 < Game.areas[176].numPieces(); t2++) {
                        if (Game.areas[176].get(t2).type().equals(Messages.getString("Scribe.27"))) {
                            poweroftheshadow.addPiece(Game.areas[176].get(t2));
                            if (poweroftheshadow.pieces().size() == 4) {
                                break;
                            }
                        }
                    }
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.28") + poweroftheshadow.contents() + Messages.getKeyString("Scribe.29") + Game.areas[176].name() + Messages.getKeyString("Scribe.31") + Game.areas[84].name() + " ");
                } else if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.32"))) {
                    Area poweroftheshadow2 = new Area("temp");
                    Area poweroftheshadow22 = new Area("temp2");
                    ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                    poweroftheshadow2.addPiece(Game.areas[179].get(1));
                    ((ShadowActionDie) Game.areas[179].get(2)).SetState(13);
                    poweroftheshadow2.addPiece(Game.areas[179].get(2));
                    ((ShadowActionDie) Game.areas[179].get(3)).SetState(13);
                    poweroftheshadow2.addPiece(Game.areas[179].get(3));
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.33") + poweroftheshadow2.contents() + Messages.getKeyString("Scribe.34") + Game.areas[179].name() + Messages.getKeyString("Scribe.35") + Game.areas[4].name() + " ");
                    poweroftheshadow2.clearAllPieces();
                    boolean UrukFound = false;
                    boolean Halforcfound = false;
                    boolean WargFound = false;
                    int leadershiptokens = 0;
                    for (int t3 = 0; t3 < Game.areas[176].numPieces(); t3++) {
                        if (Game.areas[176].get(t3).type().equals(Messages.getString("Scribe.36")) && !UrukFound) {
                            poweroftheshadow2.addPiece(Game.areas[176].get(t3));
                            UrukFound = true;
                        }
                        if (Game.areas[176].get(t3).type().equals(Messages.getString("Scribe.37")) && !Halforcfound) {
                            poweroftheshadow2.addPiece(Game.areas[176].get(t3));
                            Halforcfound = true;
                        }
                        if (Game.areas[176].get(t3).type().equals(Messages.getString("Scribe.38")) && !WargFound) {
                            poweroftheshadow2.addPiece(Game.areas[176].get(t3));
                            WargFound = true;
                        }
                        if (Game.areas[176].get(t3).type().equals(Messages.getString("Scribe.39")) && leadershiptokens < 3) {
                            poweroftheshadow22.addPiece(Game.areas[176].get(t3));
                            leadershiptokens++;
                        }
                    }
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.40") + poweroftheshadow2.contents() + Messages.getKeyString("Scribe.41") + Game.areas[176].name() + Messages.getKeyString("Scribe.42") + Game.areas[83].name() + " ");
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.40") + poweroftheshadow22.contents() + Messages.getKeyString("Scribe.41") + Game.areas[176].name() + Messages.getKeyString("Scribe.42") + Game.areas[84].name() + " ");
                }
            } else {
                JOptionPane.showMessageDialog(Game.win, Messages.getString("Scribe.5"));
            }
        } else if (!this.game.selection.containsPiece(this.game.bits[this.game.WitckKingNo]) && !this.game.selection.containsPiece(this.game.bits[this.game.GothmogNo])) {
            for (int t4 = 0; t4 < this.game.selection.numPieces(); t4++) {
                if ((this.game.selection.get(t4) instanceof UnitRecruitmentToken) && ((UnitRecruitmentToken) this.game.selection.get(t4)).localflipped) {
                    ((UnitRecruitmentToken) this.game.selection.get(t4)).flip();
                    ((UnitRecruitmentToken) this.game.selection.get(t4)).localflipped = false;
                }
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.88") + this.game.selection.contents() + Messages.getKeyString("Scribe.89") + this.game.highlight.name() + Messages.getKeyString("Scribe.90") + Game.areas[i].name() + " ");
        } else if (this.game.hasSPpassword() || !Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.43"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Scribe.45") + this.game.selection.contents() + Messages.getKeyString("Scribe.46") + this.game.highlight.name() + Messages.getKeyString("Scribe.47") + Game.areas[i].name() + " ");
            if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.49"))) {
                ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.50") + Game.areas[179].get(1) + Messages.getKeyString("Scribe.51") + Game.areas[179].name() + Messages.getKeyString("Scribe.52") + Game.areas[4].name() + " ");
                int t5 = 0;
                while (t5 < Game.areas[176].numPieces() && !Game.areas[176].get(t5).type().equals(Messages.getString("Scribe.53"))) {
                    t5++;
                }
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.54") + Game.areas[176].get(t5) + Messages.getKeyString("Scribe.55") + Game.areas[176].name() + Messages.getKeyString("Scribe.56") + Game.areas[84].name() + " ");
                int t6 = 0;
                while (t6 < Game.areas[176].numPieces() && !Game.areas[176].get(t6).type().equals(Messages.getString("Scribe.57"))) {
                    t6++;
                }
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.58") + Game.areas[176].get(t6) + Messages.getKeyString("Scribe.59") + Game.areas[176].name() + Messages.getKeyString("Scribe.60") + Game.areas[83].name() + " ");
                DrawGrimaCard();
            } else if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.64"))) {
                Area poweroftheshadow3 = new Area("temp");
                ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                poweroftheshadow3.addPiece(Game.areas[179].get(1));
                ((ShadowActionDie) Game.areas[179].get(2)).SetState(13);
                poweroftheshadow3.addPiece(Game.areas[179].get(2));
                ((ShadowActionDie) poweroftheshadow3.get(0)).SetState(13);
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.65") + poweroftheshadow3.contents() + Messages.getKeyString("Scribe.66") + Game.areas[179].name() + Messages.getKeyString("Scribe.67") + Game.areas[4].name() + " ");
                poweroftheshadow3.clearAllPieces();
                for (int t7 = 0; t7 < Game.areas[176].numPieces(); t7++) {
                    if (Game.areas[176].get(t7).type().equals(Messages.getString("Scribe.68"))) {
                        poweroftheshadow3.addPiece(Game.areas[176].get(t7));
                        if (poweroftheshadow3.pieces().size() == 2) {
                            break;
                        }
                    }
                }
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.69") + poweroftheshadow3.contents() + Messages.getKeyString("Scribe.70") + Game.areas[176].name() + Messages.getKeyString("Scribe.71") + Game.areas[84].name() + " ");
                poweroftheshadow3.clearAllPieces();
                for (int t8 = 0; t8 < Game.areas[176].numPieces(); t8++) {
                    if (Game.areas[176].get(t8).type().equals(Messages.getString("Scribe.72"))) {
                        poweroftheshadow3.addPiece(Game.areas[176].get(t8));
                        if (poweroftheshadow3.pieces().size() == 2) {
                            break;
                        }
                    }
                }
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.73") + poweroftheshadow3.contents() + Messages.getKeyString("Scribe.74") + Game.areas[176].name() + Messages.getKeyString("Scribe.75") + Game.areas[83].name() + " ");
                this.game.talker.waitforemptyqueue();
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.65") + Game.areas[2].contents() + Messages.getKeyString("Scribe.66") + Game.areas[2].name() + Messages.getKeyString("Scribe.67") + Game.areas[83].name() + " ");
            } else if (Messages.removeKeyString(Game.areas[i].name()).equals(Messages.getString("Scribe.76"))) {
                Area poweroftheshadow4 = new Area("temp");
                Area poweroftheshadow23 = new Area("temp2");
                ((ShadowActionDie) Game.areas[179].get(1)).SetState(13);
                poweroftheshadow4.addPiece(Game.areas[179].get(1));
                ((ShadowActionDie) Game.areas[179].get(2)).SetState(13);
                poweroftheshadow4.addPiece(Game.areas[179].get(2));
                ((ShadowActionDie) Game.areas[179].get(3)).SetState(13);
                poweroftheshadow4.addPiece(Game.areas[179].get(3));
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.77") + poweroftheshadow4.contents() + Messages.getKeyString("Scribe.78") + Game.areas[179].name() + Messages.getKeyString("Scribe.79") + Game.areas[4].name() + " ");
                poweroftheshadow4.clearAllPieces();
                boolean OlaghaiFound = false;
                int leadershiptokens2 = 0;
                int orcs = 0;
                int nazgul = 0;
                for (int t9 = 0; t9 < Game.areas[176].numPieces(); t9++) {
                    if (Game.areas[176].get(t9).type().equals(Messages.getString("Scribe.80")) && !OlaghaiFound) {
                        poweroftheshadow4.addPiece(Game.areas[176].get(t9));
                        OlaghaiFound = true;
                    }
                    if (Game.areas[176].get(t9).type().equals(Messages.getString("Scribe.81")) && orcs < 3) {
                        poweroftheshadow4.addPiece(Game.areas[176].get(t9));
                        orcs++;
                    }
                    if (Game.areas[176].get(t9).type().equals(Messages.getString("Scribe.82")) && nazgul < 3) {
                        poweroftheshadow4.addPiece(Game.areas[176].get(t9));
                        nazgul++;
                    }
                    if (Game.areas[176].get(t9).type().equals(Messages.getString("Scribe.83")) && leadershiptokens2 < 3) {
                        poweroftheshadow23.addPiece(Game.areas[176].get(t9));
                        leadershiptokens2++;
                    }
                }
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.85") + poweroftheshadow4.contents() + Messages.getKeyString("Scribe.86") + Game.areas[176].name() + Messages.getKeyString("Scribe.87") + Game.areas[83].name() + " ");
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.85") + poweroftheshadow23.contents() + Messages.getKeyString("Scribe.86") + Game.areas[176].name() + Messages.getKeyString("Scribe.87") + Game.areas[84].name() + " ");
            }
        } else {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Scribe.44"));
        }
    }

    private void DrawGrimaCard() {
        if (Game.areas[0].numPieces() <= 0) {
            return;
        }
        if (this.game.talker.connected) {
            this.game.controls.cbh.actionPerformed_SecureRoll(1, Game.areas[0].numPieces(), "draw Grima card");
        } else {
            this.game.talker.enqueue("<auto>" + Messages.getKeyString("Scribe.20") + Game.areas[0].GetRandomPiece() + Messages.getKeyString("Scribe.21") + Game.areas[0].name() + Messages.getKeyString("Scribe.22") + Game.areas[181].name() + " ");
        }
    }

    public void mousePressed(MouseEvent e) {
        this.pressPoint = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        if (this.pressPoint != null) {
            Point releasePoint = e.getPoint();
            if (!this.pressPoint.equals(releasePoint) && releasePoint.distanceSq(this.pressPoint) < 200.0d) {
                e.translatePoint((-e.getX()) + this.pressPoint.x, (-e.getY()) + this.pressPoint.y);
                mouseClicked(e);
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        String zoomLevels = Game.prefs.zoomLevels;
        int newzoom = Game.prefs.zoom;
        String[] zooms = zoomLevels.split(",");
        if (notches < 0) {
            int t = 0;
            while (true) {
                if (t >= zooms.length) {
                    break;
                } else if (newzoom < Integer.parseInt(zooms[t])) {
                    newzoom = Integer.parseInt(zooms[t]);
                    break;
                } else {
                    t++;
                }
            }
        }
        if (notches > 0) {
            int t2 = zooms.length - 1;
            while (true) {
                if (t2 < 0) {
                    break;
                } else if (newzoom > Integer.parseInt(zooms[t2])) {
                    newzoom = Integer.parseInt(zooms[t2]);
                    break;
                } else {
                    t2--;
                }
            }
        }
        Game.prefs.zoom = newzoom;
        this.game.zoomBoard(newzoom);
        this.game.refreshBoard();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
