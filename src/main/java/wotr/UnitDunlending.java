package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDunlending */
public class UnitDunlending extends GamePiece implements Special {
    private static String type = Messages.getString("UnitDunlending.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Dunlending.png"));

    UnitDunlending(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 1;
    }

    public int nation() {
        return -5;
    }
}
