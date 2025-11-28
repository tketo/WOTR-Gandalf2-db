package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitWeakWarg */
public class UnitWeakWarg extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/IsengardElite.png"));
    private String type = Messages.getString("UnitIsengardElite.0");

    UnitWeakWarg(Area startLoc) {
        super(startLoc);
    }

    UnitWeakWarg(Area startLoc, String newType) {
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
