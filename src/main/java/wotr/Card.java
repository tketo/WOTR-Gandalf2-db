package wotr;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/* renamed from: Card */
public abstract class Card extends GamePiece implements Oversized, Brightness {
    private ImageIcon _bigimg;
    private String _imageLocation;
    private String _bigImageLocation;
    private ImageIcon _img;
    private int _index;
    private String _name;
    public int level;
    public boolean played;

    public abstract Image getNeutralBigPic();

    public abstract Image getNeutralPic();

    public abstract int nation();

    public abstract String type();

    Card(Area startLoc, String smallimgLocation, String bigimgLocation, String name) {
        super(startLoc);
        if (smallimgLocation != null) {
            this._img = new ImageIcon(smallimgLocation);
        }
        this._imageLocation = smallimgLocation;
        if (bigimgLocation != null) {
            this._bigimg = new ImageIcon(bigimgLocation);
        }
        this._bigImageLocation = bigimgLocation;
        this._name = name;
        this._index = -1;
        this.level = 1000;
    }

    public int GetIndex() {
        return this._index;
    }

    public void SetIndex(int index) {
        this._index = index;
    }

    public String name() {
        if (this._name != null) {
            return this._name;
        }
        return type();
    }

    public Image getPic() {
        if (this._img != null) {
            return this._img.getImage();
        }
        return getNeutralPic();
    }

    public Image getBigPic() {
        if (this._bigimg != null) {
            return this._bigimg.getImage();
        }
        return getNeutralBigPic();
    }

    public String smallImageLocation() {
        return this._imageLocation;
    }

    public String bigImageLocation() {
        return this._bigImageLocation;
    }

    public void CopyCardHiddenDataFrom(Card c) {
        if (c != null) {
            this._img = c._img;
            this._bigimg = c._bigimg;
            this._imageLocation = c._imageLocation;
            this._bigImageLocation = c._bigImageLocation;
            this._name = c._name;
        }
    }

    public void DeleteHiddenData() {
        this._img = null;
        this._bigimg = null;
        this._imageLocation = null;
        this._bigImageLocation = null;
        this._name = null;
    }

    public int level() {
        return this.level;
    }

    public void toggleBrightness() {
        if (this._imageLocation != null) {
            if (this._imageLocation.contains("bright")) {
                this._imageLocation = String.valueOf(this._imageLocation.substring(0, this._imageLocation.lastIndexOf("bright"))) + ".png";
            } else if (new File(String.valueOf(this._imageLocation.substring(0, this._imageLocation.indexOf(".png"))) + "bright" + ".png").exists()) {
                this._imageLocation = String.valueOf(this._imageLocation.substring(0, this._imageLocation.indexOf(".png"))) + "bright" + ".png";
            }
            this._img = new ImageIcon(this._imageLocation);
        }
    }

    public void makeDull() {
        if (this._imageLocation != null) {
            if (this._imageLocation.contains("bright")) {
                this._imageLocation = String.valueOf(this._imageLocation.substring(0, this._imageLocation.lastIndexOf("bright"))) + ".png";
            }
            this._img = new ImageIcon(this._imageLocation);
        }
    }
}
