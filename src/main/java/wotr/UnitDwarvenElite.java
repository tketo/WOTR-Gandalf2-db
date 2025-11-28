package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDwarvenElite */
public class UnitDwarvenElite extends GamePiece implements Elite {
    private static String type = Messages.getString("UnitDwarvenElite.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/DwarvenElite.png"));

    UnitDwarvenElite(Area startLoc) {
        super(startLoc);
    }

    UnitDwarvenElite(Area startLoc, String newType) {
        super(startLoc);
        type = newType;
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 110;
    }

    public int nation() {
        return 5;
    }
}
