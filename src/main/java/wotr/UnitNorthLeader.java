package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitNorthLeader */
public class UnitNorthLeader extends GamePiece implements Leader {
    private static String type = Messages.getString("UnitNorthLeader.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/NorthLeader.png"));

    UnitNorthLeader(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 220;
    }

    public int nation() {
        return 6;
    }
}
