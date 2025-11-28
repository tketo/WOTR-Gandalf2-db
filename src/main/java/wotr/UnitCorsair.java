package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitCorsair */
public class UnitCorsair extends GamePiece implements Special {
    private static String type = Messages.getString("UnitCorsair.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Corsair.png"));

    UnitCorsair(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 2;
    }

    public int nation() {
        return -8;
    }
}
