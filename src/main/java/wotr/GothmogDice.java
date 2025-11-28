package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: GothmogDice */
public class GothmogDice extends ActionDie implements Special {
    public static final int ARMY = 0;
    public static final int ARMY2 = 4;
    public static final ImageIcon ARMY2_ = new ImageIcon("expansion2/images/GothmogArmyRemove-E.png");
    public static final ImageIcon ARMY_ = new ImageIcon("expansion2/images/GothmogArmy-E.png");
    public static final int EYE = 3;
    public static final ImageIcon EYE_ = new ImageIcon("expansion2/images/GothmogEye-E.png");
    public static final int HALF_PALANTIR = 2;
    public static final ImageIcon HALF_PALANTIR_ = new ImageIcon("expansion2/images/GothmogCardDraw-E.png");
    public static final int MUSTER = 1;
    public static final ImageIcon MUSTER_ = new ImageIcon("expansion2/images/GothmogMuster-E.png");
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static String reportResults = "";
    private static String type = "Gothmog Die";

    /* renamed from: g */
    Game f8g;
    public boolean visible = true;

    GothmogDice(Area startLoc, Game g) {
        super(startLoc, 0, 3);
        this.f8g = g;
    }

    public void SetStateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f8g.turn > 1) {
            char[] chars = new char[(this.f8g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public int mapRoll(int D6result) {
        int result = D6result;
        if (result == 6) {
            result = 1;
        }
        return result - 1;
    }

    public char getChar(int s) {
        if (s == 3) {
            return 'E';
        }
        if (s == 2) {
            return 'D';
        }
        if (s == 1) {
            return 'M';
        }
        if (s == 0 || s == 4) {
            return 'A';
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
            return ARMY_.getImage();
        }
        if (this._state == 4) {
            return ARMY2_.getImage();
        }
        if (this._state == 2) {
            return HALF_PALANTIR_.getImage();
        }
        if (this._state == 3) {
            return EYE_.getImage();
        }
        if (this._state == 1) {
            return MUSTER_.getImage();
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
