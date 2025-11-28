package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitSpider */
public class UnitSpider extends GamePiece implements Special {
    private static String type = "Giant Spider";
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("wome/images/units/Spider-F.png"));

    UnitSpider(Area startLoc) {
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
        return -6;
    }
}
