package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitNorthElite */
public class UnitNorthElite extends GamePiece implements Elite {
    private static String type = Messages.getString("UnitNorthElite.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/NorthElite.png"));

    UnitNorthElite(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 120;
    }

    public int nation() {
        return 6;
    }
}
