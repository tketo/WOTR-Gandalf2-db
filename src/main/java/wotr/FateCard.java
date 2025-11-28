package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: FateCard */
public class FateCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("b5a/images/cards/FateBack.png"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("b5a/images/smallcards/FateSmallCardBack.png"));

    public FateCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    FateCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("FateCard.0");
    }

    public Image getNeutralPic() {
        return smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return bigBack.getImage();
    }

    public int nation() {
        return 5;
    }
}
