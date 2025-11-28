package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBoromir */
public class UnitBoromir extends GamePiece implements Leader, Special, AlternativeType {
    private static String alttype = "";
    private static String type = Messages.getString("UnitBoromir.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Boromir.png"));

    UnitBoromir(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        boolean z;
        boolean z2 = false;
        if (alttype.equals("")) {
            z = false;
        } else {
            z = true;
        }
        if (!super.currentLocation().name().equals(Messages.getKeyString("Game.1998"))) {
            z2 = true;
        }
        if (z && z2) {
            return alttype;
        }
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 370;
    }

    public int nation() {
        return 10;
    }

    public String alternativetype() {
        return alttype;
    }

    public void setalternativetype(String AlternativeType) {
        alttype = AlternativeType;
    }
}
