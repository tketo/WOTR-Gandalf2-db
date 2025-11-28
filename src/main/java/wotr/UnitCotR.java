package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitCotR */
public class UnitCotR extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitCotR.0");
    private int additionalLeadership = 0;
    private int leadership = 3;
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Cotr.png"));

    UnitCotR(Area startLoc) {
        super(startLoc);
    }

    UnitCotR(Area startLoc, int Leadership, int Additionalleadership) {
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
        return 1319;
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

    public void setLeadership(int newLeaderShip) {
        this.leadership = newLeaderShip;
        this.additionalLeadership = newLeaderShip;
    }
}
