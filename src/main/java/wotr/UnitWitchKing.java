package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitWitchKing */
public class UnitWitchKing extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitWitchKing.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/WitchKing.png"));

    UnitWitchKing(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 1320;
    }

    public int nation() {
        return -1;
    }

    public int AdditionalLeadership() {
        return 2;
    }

    public int Leadership() {
        return 2;
    }
}
