package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSauronRegular */
public class UnitSauronRegular extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/SauronRegular.png"));
    private String type = Messages.getString("UnitSauronRegular.0");

    UnitSauronRegular(Area startLoc) {
        super(startLoc);
    }

    UnitSauronRegular(Area startLoc, String newType) {
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
        return 60;
    }

    public int nation() {
        return -3;
    }
}
