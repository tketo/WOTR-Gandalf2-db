package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitRohanElite */
public class UnitRohanElite extends GamePiece implements Elite {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/RohanElite.png"));
    private String type = Messages.getString("UnitRohanElite.0");

    UnitRohanElite(Area startLoc) {
        super(startLoc);
    }

    UnitRohanElite(Area startLoc, String newType) {
        super(startLoc);
        this.type = newType;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 130;
    }

    public int nation() {
        return 7;
    }
}
