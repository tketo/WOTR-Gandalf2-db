package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBalrog */
public class UnitBalrog extends GamePiece implements Leader, Special, Flippable, ExtendedLeader {
    private static String type = Messages.getString("UnitBalrog.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/units/Balrog.png"));
    private ImageIcon pic2 = new ImageIcon("expansion2/images/units/BalrogInactive-E.png");
    private boolean state = true;

    UnitBalrog(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        if (!this.state) {
            return this.pic1.getImage();
        }
        if (Game.varianttype.startsWith("expansion2")) {
            return this.pic2.getImage();
        }
        return this.pic1.getImage();
    }

    public int level() {
        return 1301;
    }

    public int nation() {
        return -1;
    }

    public int AdditionalLeadership() {
        return 3;
    }

    public int Leadership() {
        return 3;
    }

    public void flip() {
        this.state = !this.state;
    }

    public void defaultSide() {
        this.state = true;
    }

    public boolean currentState() {
        return this.state;
    }
}
