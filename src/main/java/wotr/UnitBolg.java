package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBolg */
public class UnitBolg extends GamePiece implements Leader, Special, ExtendedLeader {
    private static ImageIcon pic = new ImageIcon("b5a/images/units/Bolg-5.png");
    private static String type = Messages.getString("UnitBolg.0");
    private int additionalLeadership = 3;
    private int leadership = 3;

    UnitBolg(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return pic.getImage();
    }

    public int level() {
        return 1300;
    }

    public int nation() {
        return -1;
    }

    public int AdditionalLeadership() {
        return this.additionalLeadership;
    }

    public int Leadership() {
        return this.leadership;
    }
}
