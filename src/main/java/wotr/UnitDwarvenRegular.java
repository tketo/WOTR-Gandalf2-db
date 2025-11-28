package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDwarvenRegular */
public class UnitDwarvenRegular extends GamePiece implements Regular {
    private static String type = Messages.getString("UnitDwarvenRegular.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/DwarvenRegular.png"));

    UnitDwarvenRegular(Area startLoc) {
        super(startLoc);
    }

    UnitDwarvenRegular(Area startLoc, String newType) {
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
        return 10;
    }

    public int nation() {
        return 5;
    }
}
