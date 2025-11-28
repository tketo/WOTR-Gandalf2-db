package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDwarvenLeader */
public class UnitDwarvenLeader extends GamePiece implements Leader {
    private static String type = Messages.getString("UnitDwarvenLeader.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/DwarvenLeader.png"));

    UnitDwarvenLeader(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 210;
    }

    public int nation() {
        return 5;
    }
}
