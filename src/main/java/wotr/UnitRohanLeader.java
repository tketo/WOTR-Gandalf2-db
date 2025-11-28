package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitRohanLeader */
public class UnitRohanLeader extends GamePiece implements Leader, ExtendedLeader {
    private static String type = Messages.getString("UnitRohanLeader.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/RohanLeader.png"));

    UnitRohanLeader(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 230;
    }

    public int nation() {
        return 7;
    }

    public int Leadership() {
        return 1;
    }

    public int AdditionalLeadership() {
        return 1;
    }
}
