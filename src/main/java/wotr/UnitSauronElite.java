package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSauronElite */
public class UnitSauronElite extends GamePiece implements Elite {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/SauronElite.png"));
    private String type = Messages.getString("UnitSauronElite.0");

    UnitSauronElite(Area startLoc) {
        super(startLoc);
    }

    UnitSauronElite(Area startLoc, String newType) {
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
        return 160;
    }

    public int nation() {
        return -3;
    }
}
