package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitTrebuchet */
public class UnitTrebuchet extends GamePiece {
    private static String type = Messages.getString("UnitTrebuchet.0");
    private int nation = 4;
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Trebuchet.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Trebuchet2.png"));

    UnitTrebuchet(Area startLoc) {
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
        return this.nation;
    }

    public void setNation(int nationno) {
        this.nation = nationno;
    }
}
