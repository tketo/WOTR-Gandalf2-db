package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: FreeCharacterCard */
public class FreeCharacterCard extends Card {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/FPCC.jpg"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/FPCtextcard.gif"));

    public FreeCharacterCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    public FreeCharacterCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    public String type() {
        return Messages.getString("FreeCharacterCard.2");
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
