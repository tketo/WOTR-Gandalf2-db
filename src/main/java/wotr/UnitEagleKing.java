package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitEagleKing */
public class UnitEagleKing extends GamePiece implements Special, Flippable {
    private static String type = Messages.getString("UnitEagleKing.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/EagleKing-5.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/EagleKingactive-5.png"));
    private boolean state;

    UnitEagleKing(Area startLoc) {
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
