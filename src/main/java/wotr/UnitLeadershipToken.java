package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitLeadershipToken */
public class UnitLeadershipToken extends GamePiece implements Special, Leader, Recoverable {
    private static ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/expansion/LeadershipToken.png"));
    private static ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("b5a/images/tokens/LeadershipTokenFree.png"));
    private static String type = Messages.getString("UnitLeadershipToken.0");
    int nation = -10;

    UnitLeadershipToken(Area startLoc) {
        super(startLoc);
    }

    public String type() {
        return type;
    }

    public Image getPic() {
        if (this.nation < 0) {
            return pic.getImage();
        }
        return pic2.getImage();
    }

    public int level() {
        return 0;
    }

    public int nation() {
        return this.nation;
    }

    public void setNation(int newNation) {
        this.nation = newNation;
    }
}
