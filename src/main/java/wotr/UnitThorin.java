package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitThorin */
public class UnitThorin extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitThorin.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Thorin-5.png"));

    UnitThorin(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 375;
    }

    public int nation() {
        return 10;
    }

    public int Leadership() {
        return 2;
    }

    public int AdditionalLeadership() {
        return 2;
    }
}
