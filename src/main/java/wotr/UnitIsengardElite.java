package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitIsengardElite */
public class UnitIsengardElite extends GamePiece implements Elite {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/IsengardElite.png"));
    private String type = Messages.getString("UnitIsengardElite.0");

    UnitIsengardElite(Area startLoc) {
        super(startLoc);
    }

    UnitIsengardElite(Area startLoc, String newType) {
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
        return 180;
    }

    public int nation() {
        return -2;
    }
}
