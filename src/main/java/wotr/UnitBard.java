package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitBard */
public class UnitBard extends GamePiece implements Leader, Flippable {
    private static String type = Messages.getString("UnitBard.0");
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Bard-5.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/BardActive-5.png"));
    private boolean state;

    UnitBard(Area startLoc) {
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
