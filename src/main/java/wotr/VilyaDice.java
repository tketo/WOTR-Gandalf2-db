package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: VilyaDice */
public class VilyaDice extends ActionDie implements Special {
    public static final int ARMY = 0;
    public static final ImageIcon ARMY_ = new ImageIcon("expansion2/images/VilyaArmy-E.png");
    public static final int EYE = 3;
    public static final ImageIcon EYE_ = new ImageIcon("expansion2/images/VilyaEye-E.png");
    public static final int HALF_PALANTIR = 4;
    public static final ImageIcon HALF_PALANTIR_ = new ImageIcon("expansion2/images/VilyaCardDraw-E.png");
    public static final int MUSTER = 1;
    public static final ImageIcon MUSTER_ = new ImageIcon("expansion2/images/VilyaMuster-E.png");
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static final int PALANTIR = 2;
    public static final ImageIcon PALANTIR_ = new ImageIcon("expansion2/images/VilyaEvent-E.png");
    public static String reportResults = "";
    public static int[] stats = new int[5];
    private static String type = Messages.getString("VilyaDice.0");

    /* renamed from: g */
    Game f19g;
    public boolean visible = true;

    VilyaDice(Area startLoc, Game g) {
        super(startLoc, 0, 3);
        this.f19g = g;
    }

    public void setState(int s) {
        this._state = s;
    }

    public void setStateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f19g.turn > 1) {
            char[] chars = new char[(this.f19g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public char getChar(int s) {
        if (s == 3) {
            return 'E';
        }
        if (s == 2) {
            return 'P';
        }
        if (s == 4) {
            return 'D';
        }
        if (s == 1) {
            return 'M';
        }
        if (s == 0) {
            return 'A';
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
            return ARMY_.getImage();
        }
        if (this._state == 2) {
            return PALANTIR_.getImage();
        }
        if (this._state == 4) {
            return HALF_PALANTIR_.getImage();
        }
        if (this._state == 3) {
            return EYE_.getImage();
        }
        if (this._state == 1) {
            return MUSTER_.getImage();
        }
        return PALANTIR_.getImage();
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
            return 4;
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
