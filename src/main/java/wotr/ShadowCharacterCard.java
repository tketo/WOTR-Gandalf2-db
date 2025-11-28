package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowCharacterCard */
public class ShadowCharacterCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/SACC.jpg"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/SACtextcard.gif"));

    ShadowCharacterCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowCharacterCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("ShadowCharacterCard.2");
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
