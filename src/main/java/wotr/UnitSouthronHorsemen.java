package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSouthronHorsemen */
public class UnitSouthronHorsemen extends GamePiece implements Regular {
    private static String type = Messages.getString("UnitSouthronHorsemen.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/UnitSouthronHorsemen.png"));

    UnitSouthronHorsemen(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 60;
    }

    public int nation() {
        return -7;
    }
}
