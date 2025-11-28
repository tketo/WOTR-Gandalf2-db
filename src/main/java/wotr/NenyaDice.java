package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: NenyaDice */
public class NenyaDice extends ActionDie implements Special {
    public static final int CHARACTER = 0;
    public static final ImageIcon CHARACTER_ = new ImageIcon("expansion2/images/NenyaCharacter-E.png");
    public static final int EYE = 4;
    public static final ImageIcon EYE_ = new ImageIcon("expansion2/images/NenyaEye-E.png");
    public static final int HALF_PALANTIR = 3;
    public static final ImageIcon HALF_PALANTIR_ = new ImageIcon("expansion2/images/NenyaCardDraw-E.png");
    public static final int MUSTER = 1;
    public static final ImageIcon MUSTER_ = new ImageIcon("expansion2/images/NenyaMuster-E.png");
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static final int PALANTIR = 2;
    public static final ImageIcon PALANTIR_ = new ImageIcon("expansion2/images/NenyaEvent-E.png");
    public static String reportResults = "";
    public static int[] stats = new int[5];
    private static String type = Messages.getString("NenyaDice.0");

    /* renamed from: g */
    Game f10g;
    public boolean visible = true;

    NenyaDice(Area startLoc, Game g) {
        super(startLoc, 0, 4);
        this.f10g = g;
    }

    public void setState(int s) {
        this._state = s;
    }

    public void setStateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f10g.turn > 1) {
            char[] chars = new char[(this.f10g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public char getChar(int s) {
        if (s == 4) {
            return 'E';
        }
        if (s == 3) {
            return 'D';
        }
        if (s == 1) {
            return 'M';
        }
        if (s == 0) {
            return 'C';
        }
        if (s == 2) {
            return 'P';
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
        if (this._state == 3) {
            return HALF_PALANTIR_.getImage();
        }
        if (this._state == 4) {
            return EYE_.getImage();
        }
        if (this._state == 1) {
            return MUSTER_.getImage();
        }
        if (this._state == 2) {
            return PALANTIR_.getImage();
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

    public int mapRoll(int D6result) {
        int result = D6result - 1;
        if (result == 5) {
            return 3;
        }
        return result;
    }

    public int GetStats(int i) {
        return 0;
    }

    public void SetStats(int i, int value) {
    }

    public void setVisible() {
        this.visible = true;
    }
}
