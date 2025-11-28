package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGaladriel */
public class UnitGaladriel extends GamePiece implements Leader, Special, ExtendedLeader {
    private static String type = Messages.getString("UnitGaladriel.0");
    private int additionalLeadership = 2;
    private int leadership = 2;
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Galadriel.png"));

    UnitGaladriel(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
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
