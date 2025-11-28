package wotr;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/* renamed from: AreaPanel */
public class AreaPanel extends JPanel {
    private static int BOX_Y = 32;
    private static int SEP_X = 2;
    private static int SEP_Y = 2;
    private int BOX_X;
    Area area;

    public AreaPanel(Area a, int pieceWidth) {
        this.area = a;
        this.BOX_X = pieceWidth;
        SEP_X = Game.prefs.horizontalSpacing;
        SEP_Y = Game.prefs.verticalSpacing;
    }

    public void paint(Graphics g) {
        AreaPanel.super.paint(g);
        AreaArt aa = this.area.getAreaPic();
        if (aa.pics.length > 0 && aa.pics[0] != null) {
            BOX_Y = aa.pics[0].getHeight(this);
            this.BOX_X = aa.pics[0].getWidth(this);
        }
        for (int j = 0; j < aa.pics.length; j++) {
            if (aa.pics[j] != null) {
                Point p = getPoint(j);
                g.drawImage(aa.pics[j], p.x % getWidth(), p.y + ((j / getPieceWidth()) * SEP_Y), this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Point getPoint(int i) {
        return new Point((i % getPieceWidth()) * (this.BOX_X + SEP_X), (i / getPieceWidth()) * (BOX_Y + SEP_Y));
    }

    /* access modifiers changed from: package-private */
    public int getPieceWidth() {
        return getWidth() / (this.BOX_X + SEP_X);
    }
}
