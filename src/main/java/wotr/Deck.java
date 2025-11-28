package wotr;

import java.awt.Image;
import java.awt.Point;

/* renamed from: Deck */
public class Deck extends Area {
    public Deck(String name) {
        super(name, false, false, true);
    }

    /* access modifiers changed from: package-private */
    public AreaArt getAreaPic() {
        if (super.name() == Messages.getString("Deck.0")) {
            return new AreaArt(new Image[0], new Point[0], "  (" + (numPieces() + Game.areas[196].numPieces() + Game.areas[197].numPieces()) + ")", "", "", false, false);
        }
        return new AreaArt(new Image[0], new Point[0], "  (" + numPieces() + ")", "", "", false, false);
    }
}
