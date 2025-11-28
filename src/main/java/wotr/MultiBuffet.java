package wotr;

import java.awt.Image;
import java.awt.Point;

/* renamed from: MultiBuffet */
public class MultiBuffet extends Area {
    Point[][] pointses;

    public MultiBuffet(String name, Point[][] p) {
        super(name);
        this.pointses = p;
    }

    /* access modifiers changed from: package-private */
    public AreaArt getAreaPic() {
        int n;
        if (!this.updatePic) {
            return this.picture;
        }
        this.updatePic = false;
        int k = 0;
        while (k + 1 < this.pointses.length && this.pieces.size() < this.pointses[k].length) {
            k++;
        }
        Point[] points = this.pointses[k];
        if (points.length < this.pieces.size()) {
            n = points.length;
        } else {
            n = this.pieces.size();
        }
        Image[] pics = new Image[n];
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pics[i] = ((GamePiece) this.pieces.get(i)).getPic();
            pts[i] = points[i];
        }
        this.picture = new AreaArt(pics, pts, "", "", "", false, false);
        return this.picture;
    }
}
