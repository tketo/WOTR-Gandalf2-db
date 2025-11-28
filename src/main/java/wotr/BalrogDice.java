package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: BalrogDice */
public class BalrogDice extends ActionDie implements Special {
    public static final int ACTIVATE = 2;
    public static final ImageIcon ACTIVATE_ = new ImageIcon("expansion2/images/BalrogActivate-E.png");
    public static final int CHARACTER = 0;
    public static final ImageIcon CHARACTER_ = new ImageIcon("expansion2/images/BalrogCharacter-E.png");
    public static final int EYE = 4;
    public static final ImageIcon EYE_ = new ImageIcon("expansion2/images/BalrogEye-E.png");
    public static final int HALF_PALANTIR = 3;
    public static final ImageIcon HALF_PALANTIR_ = new ImageIcon("expansion2/images/BalrogCardDraw-E.png");
    public static final int MUSTER = 1;
    public static final ImageIcon MUSTER_ = new ImageIcon("expansion2/images/BalrogMuster-E.png");
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static String reportResults = "";
    private static String type = "Balrog Die";

    /* renamed from: _g */
    private Game f0_g;
    public boolean visible = true;

    BalrogDice(Area startLoc, Game g) {
        super(startLoc, 0, 4);
        this.f0_g = g;
    }

    public void SetStateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f0_g.turn > 1) {
            char[] chars = new char[(this.f0_g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public int mapRoll(int D6result) {
        if (D6result == 6) {
            return 2;
        }
        return D6result - 1;
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
            return 'B';
        }
        return 'X';
    }

    public String type() {
        return String.valueOf(type) + " (" + getChar(this._state) + ")";
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
            return ACTIVATE_.getImage();
        }
        return EYE_.getImage();
    }

    public int level() {
        return 1001;
    }

    public int nation() {
        return 0;
    }

    public void toggleAppearance() {
        this.visible = !this.visible;
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
