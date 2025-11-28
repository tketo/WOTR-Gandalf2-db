package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSaruman */
public class UnitSaruman extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitSaruman.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Saruman.png"));

    UnitSaruman(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 1305;
    }

    public int nation() {
        return -1;
    }

    public int AdditionalLeadership() {
        return 1;
    }

    public int Leadership() {
        return 1;
    }
}
