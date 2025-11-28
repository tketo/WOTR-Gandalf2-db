package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitEnt */
public class UnitEnt extends GamePiece implements Special {
    private static String type = Messages.getString("UnitEnt.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Ent.png"));

    UnitEnt(Area startLoc) {
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
        return 4;
    }
}
