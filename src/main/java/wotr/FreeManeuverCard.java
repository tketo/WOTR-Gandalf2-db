package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: FreeManeuverCard */
public class FreeManeuverCard extends Card {
    public static ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackFPEvent.png"));
    public static ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackFPEventSmall.png"));

    public FreeManeuverCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    FreeManeuverCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc, smallimg, bigimg, name);
    }

    FreeManeuverCard(Area startLoc, String smallimg, String bigimg, String name, String newsmallback, String newbigback) {
        super(startLoc, smallimg, bigimg, name);
        smallBack = new ImageIcon(newsmallback);
        bigBack = new ImageIcon(newbigback);
    }

    public String type() {
        return Messages.getString("FreeManeuverCard.0");
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

    public int GetIndex() {
        return 0;
    }
}
