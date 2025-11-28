package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowDarknessCard */
public class ShadowDarknessCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackDarkness.png"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackDarknessSmall.png"));

    public ShadowDarknessCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowDarknessCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("ShadowDarknessCard.2");
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
