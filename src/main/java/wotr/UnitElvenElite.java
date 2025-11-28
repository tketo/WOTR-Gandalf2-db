package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitElvenElite */
public class UnitElvenElite extends GamePiece implements Elite {
    private static String type = Messages.getString("UnitElvenElite.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/ElvenElite.png"));

    UnitElvenElite(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 150;
    }

    public int nation() {
        return 9;
    }
}
