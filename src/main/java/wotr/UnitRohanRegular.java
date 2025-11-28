package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitRohanRegular */
public class UnitRohanRegular extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/RohanRegular.png"));
    private String type = Messages.getString("UnitRohanRegular.0");

    UnitRohanRegular(Area startLoc) {
        super(startLoc);
    }

    UnitRohanRegular(Area startLoc, String newtype, boolean totalReset) {
        super(startLoc, totalReset);
        this.type = newtype;
    }

    UnitRohanRegular(Area startLoc, String newtype) {
        super(startLoc);
        this.type = newtype;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 30;
    }

    public int nation() {
        return 7;
    }
}
