package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitNazgul */
public class UnitNazgul extends GamePiece implements Leader, ExtendedLeader, Special {
    private static String type = Messages.getString("UnitNazgul.0");
    private int additionalLeadership;
    private int leadership;
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Nazgul.png"));

    UnitNazgul(Area startLoc) {
        super(startLoc);
    }

    UnitNazgul(Area startLoc, int Leadership, int Additionalleadership) {
        super(startLoc);
        this.leadership = Leadership;
        this.additionalLeadership = Additionalleadership;
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 1260;
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
