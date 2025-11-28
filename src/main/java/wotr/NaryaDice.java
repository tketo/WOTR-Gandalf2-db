package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: NaryaDice */
public class NaryaDice extends ActionDie implements Special {
    public static final int ARMY = 2;
    public static final ImageIcon ARMY_ = new ImageIcon("expansion2/images/NaryaArmy-E.png");
    public static final int CHARACTER = 0;
    public static final ImageIcon CHARACTER_ = new ImageIcon("expansion2/images/NaryaCharacter-E.png");
    public static final int EYE = 5;
    public static final ImageIcon EYE_ = new ImageIcon("expansion2/images/NaryaEye-E.png");
    public static final int HALF_PALANTIR = 4;
    public static final ImageIcon HALF_PALANTIR_ = new ImageIcon("expansion2/images/NaryaCardDraw-E.png");
    public static final int MUSTER = 3;
    public static final ImageIcon MUSTER_ = new ImageIcon("expansion2/images/NaryaMuster-E.png");
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static final int PALANTIR = 1;
    public static final ImageIcon PALANTIR_ = new ImageIcon("expansion2/images/NaryaEvent-E.png");
    public static String reportResults = "";
    public static int[] stats = new int[6];
    private static String type = Messages.getString("NaryaDice.0");

    /* renamed from: g */
    Game f9g;
    public boolean visible = true;

    NaryaDice(Area startLoc, Game g) {
        super(startLoc, 0, 5);
        this.f9g = g;
    }

    public void setstate(int s) {
        this._state = s;
    }

    public void setstateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f9g.turn > 1) {
            char[] chars = new char[(this.f9g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public char getChar(int s) {
        if (s == 5) {
            return 'E';
        }
        if (s == 1) {
            return 'P';
        }
        if (s == 2) {
            return 'A';
        }
        if (s == 3) {
            return 'M';
        }
        if (s == 4) {
            return 'D';
        }
        if (s == 0) {
            return 'C';
        }
        return 'X';
    }

    public String type() {
        return String.valueOf(type) + " (" + getChar(this._state) + ")";
    }

    public char charState() {
        return getChar(this._state);
    }

    public Image getPic() {
        if (!this.visible) {
            return NONE_.getImage();
        }
        if (this._state == 0) {
            return CHARACTER_.getImage();
        }
        if (this._state == 1) {
            return PALANTIR_.getImage();
        }
        if (this._state == 2) {
            return ARMY_.getImage();
        }
        if (this._state == 3) {
            return MUSTER_.getImage();
        }
        if (this._state == 5) {
            return EYE_.getImage();
        }
        if (this._state == 4) {
            return HALF_PALANTIR_.getImage();
        }
        return EYE_.getImage();
    }

    public int level() {
        return 1001;
    }

    public int nation() {
        return 11;
    }

    public void toggleAppearance() {
        this.visible = !this.visible;
    }

    public void setVisible() {
        this.visible = true;
    }

    public int mapRoll(int D6result) {
        return D6result - 1;
    }

    public int GetStats(int i) {
        return 0;
    }

    public void SetStats(int i, int value) {
    }
}
