package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitThranduil */
public class UnitThranduil extends GamePiece implements Leader, Special, Flippable, ExtendedLeader {
    private static String type = Messages.getString("UnitThranduil.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Thranduil-5.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Thranduilactive-5.png"));
    private boolean state;

    UnitThranduil(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
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
}
