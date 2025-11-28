package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitFreeControlLarge */
public class UnitFreeControlLarge extends GamePiece implements Special {
    private static ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/FreeControlLarge.png"));
    private static String type = Messages.getString("UnitFreeControlLarge.0");

    UnitFreeControlLarge(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return pic.getImage();
    }

    public int level() {
        return 0;
    }

    public int nation() {
        return 3;
    }
}
