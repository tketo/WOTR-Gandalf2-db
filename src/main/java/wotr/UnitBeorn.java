package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBeorn */
public class UnitBeorn extends GamePiece implements Special {
    private static String type = Messages.getString("UnitBeorn.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Beorn-5.png"));

    UnitBeorn(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 375;
    }

    public int nation() {
        return 10;
    }
}
