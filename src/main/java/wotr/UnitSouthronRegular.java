package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSouthronRegular */
public class UnitSouthronRegular extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/SouthronRegular.png"));
    private String type = Messages.getString("UnitSouthronRegular.0");

    UnitSouthronRegular(Area startLoc) {
        super(startLoc);
    }

    UnitSouthronRegular(Area startLoc, String newType) {
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
        return 70;
    }

    public int nation() {
        return -4;
    }
}
