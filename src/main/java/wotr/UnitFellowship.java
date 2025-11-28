package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitFellowship */
public class UnitFellowship extends GamePiece implements Special, Flippable {
    private static String type = Messages.getString("UnitFellowship.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/units/Fellowship.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Fellowship_revealed.png"));
    private boolean state = true;

    UnitFellowship(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic1.getImage();
        }
        return this.pic2.getImage();
    }

    public int level() {
        return 10000;
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
