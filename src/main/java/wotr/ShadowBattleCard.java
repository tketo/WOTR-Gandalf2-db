package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowBattleCard */
public class ShadowBattleCard extends Card {
    public ImageIcon bigBack;
    public String cardType;
    public ImageIcon smallBack;

    ShadowBattleCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowBattleCard(Area startLoc, String smallimg, String bigimg, String name, String smallback, String bigback, String cardType2) {
        super(startLoc, smallimg, bigimg, name);
        this.smallBack = new ImageIcon(smallback);
        this.bigBack = new ImageIcon(bigback);
        this.cardType = cardType2;
        this.level = 100;
    }

    public String type() {
        return this.cardType;
    }

    public Image getNeutralPic() {
        return this.smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return this.bigBack.getImage();
    }

    public int nation() {
        return -5;
    }
}
