package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBilbo */
public class UnitBilbo extends GamePiece implements Special {
    private static String type = Messages.getString("UnitBilbo.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Bilbo-5.png"));

    UnitBilbo(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 360;
    }

    public int nation() {
        return 10;
    }
}
