package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitDeadmen */
public class UnitDeadmen extends GamePiece implements Special {
    private static String type = "Dead Men of Dunharrow";
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("wome/images/units/Deadmen-F.png"));

    UnitDeadmen(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 1;
    }

    public int nation() {
        return 4;
    }
}
