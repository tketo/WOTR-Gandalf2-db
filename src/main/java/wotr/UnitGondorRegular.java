package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGondorRegular */
public class UnitGondorRegular extends GamePiece implements Regular {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/GondorRegular.png"));
    private String type = Messages.getString("UnitGondorRegular.0");

    UnitGondorRegular(Area startLoc) {
        super(startLoc);
    }

    UnitGondorRegular(Area startLoc, String newType, boolean totalReset) {
        super(startLoc, totalReset);
        this.type = newType;
    }

    UnitGondorRegular(Area startLoc, String newType) {
        super(startLoc);
        this.type = newType;
    }

    UnitGondorRegular(Area startLoc, String newType, String newPic, boolean totalReset) {
        super(startLoc, totalReset);
        this.type = newType;
        this.pic = new ImageIcon(newPic);
    }

    UnitGondorRegular(Area startLoc, String newType, String newPic) {
        super(startLoc);
        this.type = newType;
        this.pic = new ImageIcon(newPic);
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 40;
    }

    public int nation() {
        return 8;
    }
}
