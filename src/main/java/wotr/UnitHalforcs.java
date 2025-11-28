package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitHalforcs */
public class UnitHalforcs extends GamePiece implements Regular {
    private ImageIcon pic;
    private String type;

    UnitHalforcs(Area startLoc) {
        super(startLoc);
        this.type = Messages.getString("UnitHalforcs.0");
        this.pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/Unithalforc.png"));
    }

    UnitHalforcs(Area startLoc, String newType) {
        super(startLoc);
        this.type = Messages.getString("UnitHalforcs.0");
        this.type = newType;
        this.pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/Unithalforc.png"));
    }

    public String type() {
        return this.type;
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
