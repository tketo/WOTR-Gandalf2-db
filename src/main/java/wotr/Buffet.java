package wotr;

import java.awt.Image;
import java.awt.Point;

/* renamed from: Buffet */
public class Buffet extends Area {
    Game game;
    Point[] points;
    public boolean scalable;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Buffet(String name, Point[] p, boolean secret) {
        super(name, false, !secret, secret);
        this.points = p;
    }

    public Buffet(String name, Point[] p) {
        this(name, p, false);
    }

    public Buffet(String name, Point[] p, Game game2) {
        super(name, false, true, false);
        this.scalable = false;
        this.game = null;
        this.points = p;
        this.game = game2;
        this.scalable = true;
    }

    /* access modifiers changed from: package-private */
    public AreaArt getAreaPic() {
        int n;
        this.updatePic = false;
        if (this.points.length < this.pieces.size()) {
            n = this.points.length;
        } else {
            n = this.pieces.size();
        }
        Image[] pics = new Image[n];
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pics[i] = ((GamePiece) this.pieces.get(i)).getPic();
            pts[i] = this.points[i];
        }
        this.picture = new AreaArt(pics, pts, "", "", "", false, false);
        return this.picture;
    }
}
