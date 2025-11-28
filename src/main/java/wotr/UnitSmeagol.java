package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSmeagol */
public class UnitSmeagol extends GamePiece implements Leader, Special, Flippable {
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/units/Smeagol.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Gollum.png"));
    private boolean state = true;
    private String type1 = Messages.getString("UnitSmeagol.0");
    private String type2 = Messages.getString("UnitSmeagol.1");

    UnitSmeagol(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        if (this.state) {
            return this.type1;
        }
        return this.type2;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic1.getImage();
        }
        return this.pic2.getImage();
    }

    public int level() {
        return 400;
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
