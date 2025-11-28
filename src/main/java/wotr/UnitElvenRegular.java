package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitElvenRegular */
public class UnitElvenRegular extends GamePiece implements Regular {
    private static String type = Messages.getString("UnitElvenRegular.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/ElvenRegular.png"));

    UnitElvenRegular(Area startLoc) {
        super(startLoc);
    }

    UnitElvenRegular(Area startLoc, String newType) {
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
        return 50;
    }

    public int nation() {
        return 9;
    }
}
