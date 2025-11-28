package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGothmog */
public class UnitGothmog extends GamePiece implements Leader, Special, ExtendedLeader {
    private static ImageIcon pic = new ImageIcon("expansion2/images/units/Gothmog-E.png");
    private static String type = Messages.getString("UnitGothmog.0");
    private int additionalLeadership = 0;
    private int leadership = 1;

    UnitGothmog(Area startLoc) {
        super(startLoc);
    }

    UnitGothmog(Area startLoc, String newType) {
        super(startLoc);
        type = newType;
    }

    UnitGothmog(Area startLoc, int Leadership, int Additionalleadership) {
        super(startLoc);
        this.leadership = Leadership;
        this.additionalLeadership = Additionalleadership;
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
