package wotr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JViewport;

/* renamed from: HighlightLabel */
public class HighlightLabel extends JPanel implements MouseListener {
    private static final int BORDER_X = 3;
    private static final int BORDER_Y = 22;
    private static final int BOX_X = 44;
    private static final int BOX_Y = 48;
    public static final Point OVERSIZED = new Point(1, 0);
    public static final Point OVERUNSEL = new Point(-1, 0);
    public static final Point SELECTED = new Point(1, 1);
    private static final int SEP_X = 2;
    private static final int SEP_Y = 2;
    public static final Point UNSELECTED = new Point(-1, -1);
    private Game game;
    Point pressPoint;

    public HighlightLabel(Game game2) {
        this.game = game2;
    }

    public void paint(Graphics g) {
        HighlightLabel.super.paint(g);
        AreaArt aa = getAreaPic(this.game.highlight.pieces());
        g.setFont(new Font("Serif", 0, 9));
        for (int i = 0; i < aa.pics.length; i++) {
            Point p = getPoint(i);
            if (aa.offsets[i] == SELECTED) {
                g.drawRect(p.x - 22, p.y - 24, 44, 48);
                Point pt = new Point(p.x - (aa.pics[i].getWidth(this) / 2), p.y - (aa.pics[i].getHeight(this) / 2));
                g.drawImage(aa.pics[i], pt.x, pt.y, this);
            } else {
                if (aa.offsets[i] == UNSELECTED) {
                    Point pt2 = new Point(p.x - (aa.pics[i].getWidth(this) / 2), p.y - (aa.pics[i].getHeight(this) / 2));
                    g.drawImage(aa.pics[i], pt2.x, pt2.y, this);
                } else {
                    if (aa.offsets[i] == OVERSIZED) {
                        g.drawRect(p.x - 3, p.y - 24, 44, 0);
                        g.drawRect(p.x - 3, p.y - 24, 0, 48);
                        Point pt3 = new Point(p.x, (p.y - 24) + 3);
                        g.drawImage(aa.pics[i], pt3.x, pt3.y, this);
                    } else {
                        if (aa.offsets[i] == OVERUNSEL) {
                            Point pt4 = new Point(p.x, (p.y - 24) + 3);
                            g.drawImage(aa.pics[i], pt4.x, pt4.y, this);
                        }
                    }
                }
            }
        }
        g.setFont(new Font("Monospaced", 1, 12));
        int textoffset = 0;
        if (this.game.highlight.getTerrain() != null) {
            if (this.game.highlight.getTerrain().contains("Settlement")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainSettlement.png")).getImage(), 0, 0, this);
                textoffset = 0 + 30;
            }
            if (this.game.highlight.getTerrain().contains("Fortress")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainFortress.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("Hills")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainHills.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("Woods")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainWoods.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("Swamps")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainSwamps.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("Forts")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/expansion/TerrainForts.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("Mountains")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("b5a/images/TerrainMountains.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("FreeDwarvesTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeDwarvesTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("FreeNorthTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeNorthTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("FreeNorthCity")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeNorthCity.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 41;
            }
            if (this.game.highlight.getTerrain().contains("FreeRohanTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeNorthTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("FreeRohanCity")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeRohanCity.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 41;
            }
            if (this.game.highlight.getTerrain().contains("FreeFortification")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeFortification.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 45;
            }
            if (this.game.highlight.getTerrain().contains("FreeGondorTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeGondorTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("FreeGondorCity")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainFreeGondorCity.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("ShadowIsengardTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainShadowIsengardTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("ShadowSauronCity")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainShadowSauronCity.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 41;
            }
            if (this.game.highlight.getTerrain().contains("ShadowSauronTown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainShadowSauronTown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("ShadowSETown")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainShadowSETown.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 30;
            }
            if (this.game.highlight.getTerrain().contains("ShadowSECity")) {
                g.drawImage(new ImageIcon(Messages.getLanguageLocation("images/TerrainShadowSECity.png")).getImage(), textoffset + 0, 0, this);
                textoffset += 42;
            }
        }
        g.drawString(aa.text, textoffset + 6, 16);
    }

    static String getfightingterrain(Area battlearea) {
        String terrain = "Plains";
        if (battlearea.getTerrain() == null) {
            String str = terrain;
            return terrain;
        }
        if (battlearea.getTerrain().contains("Fortress")) {
            terrain = "Fortress";
        }
        if (battlearea.getTerrain().contains("Hills")) {
            terrain = "Hills";
        }
        if (battlearea.getTerrain().contains("Woods")) {
            terrain = "Woods";
        }
        if (battlearea.getTerrain().contains("Swamps")) {
            terrain = "Swamps";
        }
        if (battlearea.getTerrain().contains("Forts")) {
            terrain = "Forts";
        }
        if (battlearea.getTerrain().contains("Mountains")) {
            terrain = "Mountains";
        }
        String str2 = terrain;
        return terrain;
    }

    /* access modifiers changed from: package-private */
    public int getTSup(GamePiece gp, String terrain, boolean defender) {
        int TSup = 0;
        if (gp.type().equals(Messages.getString("Game.430")) && (terrain.equals("Plains") || terrain.equals("Hills"))) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.548")) && terrain.equals("Hills")) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.549")) && terrain.equals("Mountains")) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.552")) && (terrain.equals("Mountains") || terrain.equals("Forts"))) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.553")) && terrain.equals("Forts")) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.556")) && !defender) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("Game.557")) && terrain.equals("Swamps")) {
            return 1;
        }
        if (gp.type().equals(Messages.getString("HighlightLabel.85")) && terrain.equals("Mountains")) {
            return 1;
        }
        if (!gp.type().equals(Messages.getString("HighlightLabel.90")) || !terrain.equals("Plains")) {
            return TSup;
        }
        return 1;
    }

    static Point getPoint(int i) {
        return new Point(((i % getPieceWidth()) * 46) + 23 + 3, ((i / getPieceWidth()) * 50) + 25 + 22);
    }

    /* access modifiers changed from: package-private */
    public Dimension happyDimension(int n) {
        if ((n <= 0 || !(this.game.highlight.pieces().get(0) instanceof Card)) && (n <= 1 || !(this.game.highlight.pieces().get(1) instanceof Card))) {
            return new Dimension((getPieceWidth() * 46) + 3 + 2, ((((getPieceWidth() + n) - 1) / getPieceWidth()) * 50) + 22 + 2);
        }
        return new Dimension(260, 400);
    }

    /* access modifiers changed from: package-private */
    public AreaArt getAreaPic(ArrayList<GamePiece> pieces) {
        Image[] pics = new Image[pieces.size()];
        Point[] selbits = new Point[pieces.size()];
        for (int i = 0; i < pieces.size(); i++) {
            GamePiece gp = pieces.get(i);
            pics[i] = gp.getPic();
            if (gp instanceof Card) {
                if (!gp.currentLocation().GetViewFronts()) {
                    pics[i] = ((Card) gp).getNeutralBigPic();
                } else {
                    pics[i] = ((Oversized) gp).getBigPic();
                }
            } else if (!(gp instanceof GenericCard)) {
                pics[i] = gp.getPic();
            } else if (!gp.currentLocation().GetViewFronts()) {
                pics[i] = ((GenericCard) gp).getNeutralBigPic();
            } else {
                pics[i] = ((Oversized) gp).getBigPic();
            }
            if (this.game.selection.containsPiece(gp)) {
                if (gp instanceof Oversized) {
                    selbits[i] = OVERSIZED;
                } else {
                    selbits[i] = SELECTED;
                }
            } else if (gp instanceof Oversized) {
                selbits[i] = OVERUNSEL;
            } else {
                selbits[i] = UNSELECTED;
            }
        }
        return new AreaArt(pics, selbits, Messages.removeKeyString(this.game.highlight.name()), "", "", false, false);
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        GamePiece gp = this.game.highlight.get((getPieceWidth() * ((e.getY() - 22) / 50)) + ((x - 3) / 46));
        if (gp != null) {
            if (e.getClickCount() == 2 && !e.isControlDown() && (e.getModifiers() & 4) != 4) {
                if ((gp instanceof ActionDie) || (gp instanceof UnitActionToken)) {
                    if (gp.currentLocation().equals(Game.areas[179])) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("HighlightLabel.81") + gp + Messages.getKeyString("HighlightLabel.82") + Game.areas[179].name() + Messages.getKeyString("HighlightLabel.83") + Game.areas[152].name() + " ");
                        return;
                    } else if (gp.currentLocation().equals(Game.areas[178])) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("HighlightLabel.86") + gp + Messages.getKeyString("HighlightLabel.87") + Game.areas[178].name() + Messages.getKeyString("HighlightLabel.88") + Game.areas[152].name() + " ");
                        return;
                    } else {
                        return;
                    }
                } else if ((gp instanceof Chit) || (gp instanceof TwoChit)) {
                    if (gp.currentLocation().equals(Game.areas[117])) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("HighlightLabel.91") + gp + Messages.getKeyString("HighlightLabel.92") + Game.areas[117] + Messages.getKeyString("HighlightLabel.93") + Game.areas[118] + " ");
                        return;
                    } else if (gp.currentLocation().equals(Game.areas[118])) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("HighlightLabel.96") + gp + Messages.getKeyString("HighlightLabel.97") + Game.areas[118] + Messages.getKeyString("HighlightLabel.98") + Game.areas[119] + " ");
                        return;
                    } else if (gp.currentLocation().equals(Game.areas[119])) {
                        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("HighlightLabel.101") + gp + Messages.getKeyString("HighlightLabel.102") + Game.areas[119] + Messages.getKeyString("HighlightLabel.103") + Game.areas[120] + " ");
                        return;
                    }
                }
            }
            if (!e.isControlDown() && (e.getModifiers() & 4) != 4) {
                this.game.selection.clearAllPieces();
                this.game.selection.addPiece(gp);
            } else if (this.game.selection.containsPiece(gp)) {
                this.game.selection.removePiece(gp);
            } else {
                this.game.selection.addPiece(gp);
            }
            this.game.updateStatus();
            validate();
            repaint();
        }
    }

    static int getPieceWidth() {
        return (Game.prefs.highlightWidth - 3) / 46;
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

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
}
