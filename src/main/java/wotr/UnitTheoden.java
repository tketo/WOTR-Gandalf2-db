package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitTheoden */
public class UnitTheoden extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitTheoden.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/UnitRohanCaptain.png"));

    UnitTheoden(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 360;
    }

    public int nation() {
        return 10;
    }

    public int AdditionalLeadership() {
        return 0;
    }

    public int Leadership() {
        return 2;
    }
}
