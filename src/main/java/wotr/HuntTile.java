package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: HuntTile */
public class HuntTile extends GamePiece implements Special {
    private int nationno = 0;
    private ImageIcon pic;
    private String type;

    HuntTile(Area startLoc, String type2, String pic2) {
        super(startLoc);
        this.type = type2;
        this.pic = new ImageIcon(pic2);
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        return this.pic.getImage();
    }

    public int level() {
        return 900;
    }

    public int nation() {
        return this.nationno;
    }

    public void setNation(int nationno2) {
        this.nationno = nationno2;
    }
}
