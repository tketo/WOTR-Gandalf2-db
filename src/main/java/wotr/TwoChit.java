package wotr;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/* renamed from: TwoChit */
public class TwoChit extends GamePiece implements Special, Flippable, Brightness {
    private String imageLocation1;
    private String imageLocation2;
    private int nationno = 0;
    private ImageIcon pic1;
    private ImageIcon pic2;
    private String revealedtype;
    private boolean state = true;
    private String type;

    TwoChit(Area startLoc, String type2, String pic12, String pic22) {
        super(startLoc);
        this.type = type2;
        this.pic1 = new ImageIcon(pic12);
        this.pic2 = new ImageIcon(pic22);
        this.imageLocation1 = pic12;
        this.imageLocation2 = pic22;
    }

    public void flip() {
        this.state = !this.state;
    }

    public void defaultSide() {
        this.state = true;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic1.getImage();
        }
        return this.pic2.getImage();
    }

    public Image getReversePic() {
        return this.pic2.getImage();
    }

    public int level() {
        return 955;
    }

    public boolean state() {
        return this.state;
    }

    public int nation() {
        return this.nationno;
    }

    public void setNation(int nationno2) {
        this.nationno = nationno2;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String revealedtype() {
        return this.revealedtype;
    }

    public void setRevealedType(String type2) {
        this.revealedtype = type2;
    }

    public boolean currentState() {
        return this.state;
    }

    public void toggleBrightness() {
        if (this.state) {
            if (this.imageLocation1.contains("bright")) {
                this.imageLocation1 = String.valueOf(this.imageLocation1.substring(0, this.imageLocation1.lastIndexOf("bright"))) + ".png";
            } else if (new File(String.valueOf(this.imageLocation1.substring(0, this.imageLocation1.indexOf(".png"))) + "bright" + ".png").exists()) {
                this.imageLocation1 = String.valueOf(this.imageLocation1.substring(0, this.imageLocation1.indexOf(".png"))) + "bright" + ".png";
            }
            this.pic1 = new ImageIcon(this.imageLocation1);
            return;
        }
        if (this.imageLocation2.contains("bright")) {
            this.imageLocation2 = String.valueOf(this.imageLocation2.substring(0, this.imageLocation2.lastIndexOf("bright"))) + ".png";
        } else if (new File(String.valueOf(this.imageLocation2.substring(0, this.imageLocation2.indexOf(".png"))) + "bright" + ".png").exists()) {
            this.imageLocation2 = String.valueOf(this.imageLocation2.substring(0, this.imageLocation2.indexOf(".png"))) + "bright" + ".png";
        }
        this.pic2 = new ImageIcon(this.imageLocation2);
    }

    public void makeDull() {
        if (this.state) {
            if (this.imageLocation1.contains("bright")) {
                this.imageLocation1 = String.valueOf(this.imageLocation1.substring(0, this.imageLocation1.lastIndexOf("bright"))) + ".png";
            }
            this.pic1 = new ImageIcon(this.imageLocation1);
            return;
        }
        if (this.imageLocation2.contains("bright")) {
            this.imageLocation2 = String.valueOf(this.imageLocation2.substring(0, this.imageLocation2.lastIndexOf("bright"))) + ".png";
        }
        this.pic2 = new ImageIcon(this.imageLocation2);
    }
}
