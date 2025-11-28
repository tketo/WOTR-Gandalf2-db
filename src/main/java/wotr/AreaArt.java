package wotr;

import java.awt.Image;
import java.awt.Point;

/* renamed from: AreaArt */
/* compiled from: Area */
class AreaArt {
    String chatText;
    Point[] offsets;
    boolean overStacked;
    boolean overStackedFactions;
    Image[] pics;
    String text;
    String text2;

    AreaArt(Image[] pics2, Point[] offsets2, String t, String t2, String t3, boolean overStacked2, boolean overStackedFactions2) {
        this.text = t;
        this.chatText = t2;
        this.text2 = t3;
        this.pics = pics2;
        this.offsets = offsets2;
        this.overStacked = overStacked2;
        this.overStackedFactions = overStackedFactions2;
    }
}
