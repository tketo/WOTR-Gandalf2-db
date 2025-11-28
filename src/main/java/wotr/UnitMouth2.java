package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitMouth2 */
public class UnitMouth2 extends GamePiece implements Leader, Special, ExtendedLeader {
    private int additionalLeadership = 0;
    private int leadership = 2;
    private ImageIcon pic = new ImageIcon("expansion2/images/units/Mouth2-E.png");
    private String type = Messages.getString("UnitMouth2.0");

    UnitMouth2(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
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
