package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitElrond */
public class UnitElrond extends GamePiece implements Leader, Special, ExtendedLeader {
    private static ImageIcon pic = new ImageIcon("expansion2/images/units/Elrond-E.png");
    private static String type = Messages.getString("UnitElrond.0");
    private int additionalLeadership = 2;
    private int leadership = 2;

    UnitElrond(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return pic.getImage();
    }

    public int level() {
        return 388;
    }

    public int nation() {
        return 10;
    }

    public int AdditionalLeadership() {
        return this.additionalLeadership;
    }

    public int Leadership() {
        return this.leadership;
    }
}
