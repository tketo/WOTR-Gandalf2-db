package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowStrategyCard */
public class ShadowStrategyCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/SAAC.jpg"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/SAAtextcard.gif"));

    public ShadowStrategyCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowStrategyCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("ShadowArmyCard.2");
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
