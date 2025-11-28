package wotr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Point;

/* renamed from: BoardLabel */
public class BoardLabel extends JLabel {
    private Game game;

    /* renamed from: ii */
    private ImageIcon f1ii;
    BufferedImage image;

    public BoardLabel(ImageIcon im, Game game2) {
        super(im);
        this.f1ii = im;
        this.game = game2;
    }

    public void setImage(Image img) {
        this.f1ii = new ImageIcon(img);
        setIcon(this.f1ii);
    }

    public void paint(Graphics g) {
        BoardLabel.super.paint(g);
        AreaArt aa = null;
        double scale = ((double) Game.prefs.zoom) / 100.0d;
        for (int j = 0; j < this.game.siegeOverlays.length; j++) {
            if (this.game.siegeOverlays[j] != null) {
                g.drawImage(this.game.siegeOverlays[j], 0, 0, (int) (((double) this.game.siegeOverlays[j].getWidth((ImageObserver) null)) * scale), (int) (((double) this.game.siegeOverlays[j].getHeight((ImageObserver) null)) * scale), (ImageObserver) null);
            }
        }
        for (int i = 0; i < this.game.areaCoords.length; i++) {
            try {
                aa = Game.areas[i].getAreaPic();
            } catch (Exception e) {
            }
            try {
                Point coords = this.game.areaCoords[i];
                Point coords2 = new Point((int) (((double) coords.x) * scale), (int) (((double) coords.y) * scale));
                int j2 = 0;
                while (j2 < aa.pics.length) {
                    try {
                        int x = aa.pics[j2].getWidth(this) / 2;
                        int y = aa.pics[j2].getHeight(this) / 2;
                        if (!(Game.areas[i] instanceof Buffet)) {
                            g.drawImage(aa.pics[j2], (coords2.x - x) + aa.offsets[j2].x, (coords2.y - y) + aa.offsets[j2].y, this);
                        } else if (((Buffet) Game.areas[i]).scalable) {
                            g.drawImage(aa.pics[j2], (coords2.x - x) + ((int) (((double) aa.offsets[j2].x) * scale)), (coords2.y - y) + ((int) (((double) aa.offsets[j2].y) * scale)), this);
                        } else {
                            g.drawImage(aa.pics[j2], (coords2.x - x) + aa.offsets[j2].x, (coords2.y - y) + aa.offsets[j2].y, this);
                        }
                        j2++;
                    } catch (Exception e2) {
                        Point pt = coords2;
                    }
                }
                Point pt2 = coords2;
            } catch (Exception e3) {
            }
        }
        for (int i2 = 0; i2 < this.game.areaChits.length; i2++) {
            Point coordsChit = this.game.areaChits[i2];
            Point coordsControl = this.game.areaControlPoints[i2];
            if (coordsChit != null) {
                AreaArt aaChit = Game.areas[i2].getChitPic();
                Point coordsChit2 = new Point((int) (((double) coordsChit.x) * scale), (int) (((double) coordsChit.y) * scale));
                if (coordsControl != null) {
                    coordsControl = new Point((int) (((double) coordsControl.x) * scale), (int) (((double) coordsControl.y) * scale));
                }
                for (int j3 = 0; j3 < aaChit.pics.length; j3++) {
                    int x2 = aaChit.pics[j3].getWidth(this) / 2;
                    int y2 = aaChit.pics[j3].getHeight(this) / 2;
                    if (aaChit.pics[j3] != null) {
                        if (x2 != 10 || coordsControl == null) {
                            g.drawImage(aaChit.pics[j3], (coordsChit2.x - x2) + aaChit.offsets[j3].x, (coordsChit2.y - y2) + aaChit.offsets[j3].y, this);
                        } else {
                            g.drawImage(aaChit.pics[j3], (coordsControl.x - x2) + aaChit.offsets[j3].x, (coordsControl.y - y2) + aaChit.offsets[j3].y, this);
                        }
                    }
                }
                Point pt3 = coordsChit2;
            }
        }
        for (int i3 = 0; i3 < this.game.areaCoords.length; i3++) {
            try {
                if (!Game.isWOME.booleanValue() || !(i3 == 196 || i3 == 197 || i3 == 200 || i3 == 201)) {
                    AreaArt aa2 = Game.areas[i3].getAreaPic();
                    Point coords3 = this.game.areaCoords[i3];
                    Point coords4 = new Point((int) (((double) coords3.x) * scale), (int) (((double) coords3.y) * scale));
                    try {
                        g.setColor(Color.black);
                        g.setFont(new Font("Monospaced", 0, Game.prefs.boardfontsize));
                        g.drawString(aa2.text, coords4.x - 23, coords4.y + 28);
                        if (aa2.overStacked) {
                            g.setColor(Color.red);
                        } else {
                            g.setColor(Color.white);
                        }
                        g.drawString(aa2.text, coords4.x - 24, coords4.y + 27);
                        if (aa2.text2 != "") {
                            g.setColor(Color.black);
                            g.drawString(aa2.text2, coords4.x - 24, coords4.y + 27 + g.getFontMetrics().getHeight());
                            if (aa2.overStackedFactions) {
                                g.setColor(Color.red);
                            } else {
                                g.setColor(Color.white);
                            }
                            g.drawString(aa2.text2, coords4.x - 24, coords4.y + 27 + g.getFontMetrics().getHeight());
                            Point pt4 = coords4;
                        }
                    } catch (Exception e4) {
                        Point pt5 = coords4;
                    }
                }
            } catch (Exception e5) {
            }
        }
        if (this.game.circle) {
            g.setColor(Color.black);
            g.drawOval(this.game.circleX - ((int) (239.0d * scale)), this.game.circleY - ((int) (239.0d * scale)), (int) (435.0d * scale), (int) (435.0d * scale));
        }
    }
}
