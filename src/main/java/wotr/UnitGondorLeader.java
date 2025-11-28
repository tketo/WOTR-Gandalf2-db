package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGondorLeader */
public class UnitGondorLeader extends GamePiece implements Leader {
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/GondorLeader.png"));
    private String type = Messages.getString("UnitGondorLeader.0");

    UnitGondorLeader(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 240;
    }

    public int nation() {
        return 8;
    }
}
