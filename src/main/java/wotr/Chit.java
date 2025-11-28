package wotr;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/* renamed from: Chit */
public class Chit extends GamePiece implements Special, Brightness {
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    private String imageLocation;
    private int nation;
    private ImageIcon pic;
    private String type;
    public boolean visible = true;

    Chit(Area startLoc, String type2, String pic2) {
        super(startLoc);
        this.type = type2;
        this.pic = new ImageIcon(pic2);
        this.nation = 0;
        this.imageLocation = pic2;
    }

    Chit(Area startLoc, String type2, String pic2, int nation2) {
        super(startLoc);
        this.type = type2;
        this.pic = new ImageIcon(pic2);
        this.nation = nation2;
        this.imageLocation = pic2;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        if (!this.visible) {
            return NONE_.getImage();
        }
        return this.pic.getImage();
    }

    public int level() {
        return 950;
    }

    public int nation() {
        return this.nation;
    }

    public void setNation(int nationno) {
        this.nation = nationno;
    }

    public void toggleBrightness() {
        if (this.imageLocation.contains("bright")) {
            this.imageLocation = String.valueOf(this.imageLocation.substring(0, this.imageLocation.lastIndexOf("bright"))) + ".png";
        } else if (new File(String.valueOf(this.imageLocation.substring(0, this.imageLocation.indexOf(".png"))) + "bright" + ".png").exists()) {
            this.imageLocation = String.valueOf(this.imageLocation.substring(0, this.imageLocation.indexOf(".png"))) + "bright" + ".png";
        }
        this.pic = new ImageIcon(this.imageLocation);
    }

    public void makeDull() {
        if (this.imageLocation.contains("bright")) {
            this.imageLocation = String.valueOf(this.imageLocation.substring(0, this.imageLocation.lastIndexOf("bright"))) + ".png";
        }
        this.pic = new ImageIcon(this.imageLocation);
    }

    public void toggleAppearance() {
        this.visible = !this.visible;
    }

    public void setVisible() {
        this.visible = true;
    }
}
