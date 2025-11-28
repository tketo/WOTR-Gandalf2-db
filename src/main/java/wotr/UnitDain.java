package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDain */
public class UnitDain extends GamePiece implements Leader, Special, Flippable, ExtendedLeader {
    private static String type = Messages.getString("UnitDain.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Dain-5.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Dainactive-5.png"));
    private boolean state;

    UnitDain(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public int level() {
        return 375;
    }

    public int nation() {
        return 10;
    }

    public int Leadership() {
        return 2;
    }

    public int AdditionalLeadership() {
        return 2;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic2.getImage();
        }
        return this.pic1.getImage();
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
