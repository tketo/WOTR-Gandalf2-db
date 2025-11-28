package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSouthronElite */
public class UnitSouthronElite extends GamePiece implements Elite {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/SouthronElite.png"));
    private String type = Messages.getString("UnitSouthronElite.0");

    UnitSouthronElite(Area startLoc) {
        super(startLoc);
    }

    UnitSouthronElite(Area startLoc, String newType) {
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
        return 170;
    }

    public int nation() {
        return -4;
    }
}
