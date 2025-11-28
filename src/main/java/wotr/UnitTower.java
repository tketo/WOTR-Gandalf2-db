package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitTower */
public class UnitTower extends GamePiece {
    private static String type = Messages.getString("UnitTower.0");
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Tower.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Tower2.png"));

    UnitTower(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public Image getPic2() {
        return this.pic2.getImage();
    }

    public int level() {
        return 0;
    }

    public int nation() {
        return -8;
    }
}
