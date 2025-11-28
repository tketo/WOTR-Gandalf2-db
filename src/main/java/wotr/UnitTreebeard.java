package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitTreebeard */
public class UnitTreebeard extends GamePiece implements Special, Flippable {
    private static String type = Messages.getString("UnitTreebeard.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/expansion/UnitTreebeard.png"));
    private ImageIcon pic2 = new ImageIcon("expansion2/images/units/TreebeardInactive-E.png");
    private boolean state = true;

    UnitTreebeard(Area startLoc) {
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
        return 360;
    }

    public int nation() {
        return 10;
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
