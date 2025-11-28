package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGondorElite */
public class UnitGondorElite extends GamePiece implements Elite {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/GondorElite.png"));
    private String type = Messages.getString("UnitGondorElite.0");

    UnitGondorElite(Area startLoc) {
        super(startLoc);
    }

    UnitGondorElite(Area startLoc, String newType) {
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
        return 140;
    }

    public int nation() {
        return 8;
    }
}
