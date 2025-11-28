package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBat */
public class UnitBat extends TwoChit implements Regular, Recoverable {
    private static ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Bat-5.png"));
    private static String type = Messages.getString("UnitBat.0");

    UnitBat(Area startLoc) {
        super(startLoc, type, Messages.getLanguageLocation("b5a/images/units/Bat-5.png"), Messages.getLanguageLocation("b5a/images/units/Bat-5.png"));
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return pic.getImage();
    }

    public int level() {
        return 60;
    }

    public int nation() {
        return -8;
    }
}
