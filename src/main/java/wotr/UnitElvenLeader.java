package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitElvenLeader */
public class UnitElvenLeader extends GamePiece implements Leader {
    private static String type = Messages.getString("UnitElvenLeader.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/ElvenLeader.png"));

    UnitElvenLeader(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 250;
    }

    public int nation() {
        return 9;
    }
}
