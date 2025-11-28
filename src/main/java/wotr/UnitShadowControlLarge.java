package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitShadowControlLarge */
public class UnitShadowControlLarge extends GamePiece implements Special {
    private static String type = Messages.getString("UnitShadowControlLarge.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/ShadowControlLarge.png"));

    UnitShadowControlLarge(Area startLoc) {
        super(startLoc);
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
        return -9;
    }
}
