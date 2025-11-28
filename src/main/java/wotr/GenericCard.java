package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: GenericCard */
public class GenericCard extends GamePiece implements Oversized {
    public static final ImageIcon bigBack = new ImageIcon(Messages.getLanguageLocation("images/expansion/BackGeneric.png"));
    public static final ImageIcon smallBack = new ImageIcon(Messages.getLanguageLocation("b5a/images/smallcards/GenericSmallCardBack.png"));
    private ImageIcon _bigimg;
    private String _imageLocation;
    private ImageIcon _img;
    private int _index;
    private String _name;
    private int nation;
    public boolean played;

    public GenericCard(Area startLoc, int index) {
        this(startLoc, (String) null, (String) null, (String) null);
        SetIndex(index);
    }

    GenericCard(Area startLoc, String smallimg, String bigimg, String name) {
        super(startLoc);
        this.nation = -5;
        if (smallimg != null) {
            this._img = new ImageIcon(smallimg);
        }
        this._imageLocation = smallimg;
        if (bigimg != null) {
            this._bigimg = new ImageIcon(bigimg);
        }
        this._name = name;
    }

    public String type() {
        return Messages.getString("GenericCard.0");
    }

    public Image getNeutralPic() {
        return smallBack.getImage();
    }

    public Image getNeutralBigPic() {
        return bigBack.getImage();
    }

    public int nation() {
        return this.nation;
    }

    public void setnation(int nation2) {
        this.nation = nation2;
    }

    public void SetIndex(int index) {
        this._index = index;
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

    public int level() {
        return 1000;
    }

    public String name() {
        if (this._name != null) {
            return this._name;
        }
        return type();
    }
}
