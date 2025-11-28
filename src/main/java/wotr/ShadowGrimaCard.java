package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowGrimaCard */
public class ShadowGrimaCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackGrima.png"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackGrimaSmall.png"));

    public ShadowGrimaCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowGrimaCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("ShadowGrimaCard.0");
    }

    public Image getNeutralPic() {
        return smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return bigBack.getImage();
    }

    public int nation() {
        return -5;
    }
}
