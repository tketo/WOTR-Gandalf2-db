package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitIsengardRegular */
public class UnitIsengardRegular extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/IsengardRegular.png"));
    private String type = Messages.getString("UnitIsengardRegular.0");

    UnitIsengardRegular(Area startLoc) {
        super(startLoc);
    }

    UnitIsengardRegular(Area startLoc, String newType, boolean totalReset) {
        super(startLoc, totalReset);
        this.type = newType;
    }

    UnitIsengardRegular(Area startLoc, String newType) {
        super(startLoc);
        this.type = newType;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 80;
    }

    public int nation() {
        return -2;
    }
}
