package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitActionToken */
public class UnitActionToken extends GamePiece implements Recoverable, Special {
    private static String type = Messages.getString("UnitActionToken.0");
    private ImageIcon pic;

    UnitActionToken(Area startLoc, String Image) {
        super(startLoc);
        this.pic = new ImageIcon(Image);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 0;
    }

    public int nation() {
        return 6;
    }
}
