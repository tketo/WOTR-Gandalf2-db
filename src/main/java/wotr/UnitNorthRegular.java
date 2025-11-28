package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitNorthRegular */
public class UnitNorthRegular extends GamePiece implements Regular {
    private ImageIcon pic;
    private String type;

    UnitNorthRegular(Area startLoc) {
        super(startLoc);
        this.type = Messages.getString("UnitNorthRegular.0");
        this.pic = new ImageIcon(Messages.getLanguageLocation("images/units/NorthRegular.png"));
    }

    UnitNorthRegular(Area startLoc, String newType) {
        super(startLoc);
        this.type = Messages.getString("UnitNorthRegular.0");
        this.type = newType;
        this.pic = new ImageIcon(Messages.getLanguageLocation("images/units/NorthRegular.png"));
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 20;
    }

    public int nation() {
        return 6;
    }
}
