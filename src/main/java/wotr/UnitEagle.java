package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitEagle */
public class UnitEagle extends TwoChit implements Special {
    private static String type = Messages.getString("UnitEagle.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Eagle-5.png"));

    UnitEagle(Area startLoc) {
        super(startLoc, type, Messages.getLanguageLocation("b5a/images/units/Eagle-5.png"), Messages.getLanguageLocation("b5a/images/units/Eagle-5.png"));
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
