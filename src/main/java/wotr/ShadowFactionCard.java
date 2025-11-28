package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowFactionCard */
public class ShadowFactionCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("wome/images/cards/sa_faction_back.jpg"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("wome/images/smallcards/sa_Faction_back.png"));

    ShadowFactionCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    ShadowFactionCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
        this.level = 200;
    }

    public String type() {
        return Messages.getString("ShadowFactionCard.0");
    }

    public Image getNeutralPic() {
        return smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return bigBack.getImage();
    }

    public int nation() {
        return 4;
    }
}
