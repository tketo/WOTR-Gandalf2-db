package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowManeuverCard */
public class ShadowManeuverCard extends Card {
    public static ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackSAEvent.png"));
    public static ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackSAEventSmall.png"));

    public ShadowManeuverCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowManeuverCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    ShadowManeuverCard(Area startLoc, String smallimg, String bigimg, String name, String newsmallback, String newbigback) {
        super(startLoc, smallimg, bigimg, name);
        smallBack = new ImageIcon(newsmallback);
        bigBack = new ImageIcon(newbigback);
    }

    public String type() {
        return Messages.getString("ShadowManeuverCard.0");
    }

    public Image getNeutralPic() {
        return smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return bigBack.getImage();
    }

    public int nation() {
        return -4;
    }
}
