package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: ShadowActionDie */
public class ShadowActionDie extends ActionDie implements Special {
    public static final int ARMY = 12;
    public static final ImageIcon ARMY_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAarmy.png"));
    public static final int ARMY_MUSTER = 11;
    public static final ImageIcon ARMY_MUSTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAarmymuster.png"));
    public static final int CHARACTER = 13;
    public static final ImageIcon CHARACTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAcharacter.png"));
    public static final int EYE = 15;
    public static final ImageIcon EYE_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAeye.png"));
    public static final int MUSTER = 10;
    public static final ImageIcon MUSTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAmuster.png"));
    public static final int PALANTIR = 14;
    public static final ImageIcon PALANTIR_ = new ImageIcon(Messages.getLanguageLocation("images/ADSAevent.png"));
    public static int allocations;
    public static int[] stats = new int[6];
    private static String type = Messages.getString("ShadowActionDie.6");

    ShadowActionDie(Area startLoc) {
        super(startLoc, 10, 15);
    }

    public int mapRoll(int D6result) {
        return D6result + 9;
    }

    public char getChar(int s) {
        return GetChar(s);
    }

    public static char GetChar(int s) {
        if (s == 15) {
            return 'E';
        }
        if (s == 14) {
            return 'P';
        }
        if (s == 10) {
            return 'M';
        }
        if (s == 11) {
            return 'H';
        }
        if (s == 12) {
            return 'A';
        }
        if (s == 13) {
            return 'C';
        }
        return 'X';
    }

    public void setChar(String cmd) {
        if (cmd.equals("E")) {
            this._state = 15;
        }
        if (cmd.equals("P")) {
            this._state = 14;
        }
        if (cmd.equals("M")) {
            this._state = 10;
        }
        if (cmd.equals("H")) {
            this._state = 11;
        }
        if (cmd.equals("A")) {
            this._state = 12;
        }
        if (cmd.equals("C")) {
            this._state = 13;
        }
    }

    public String type() {
        return String.valueOf(type) + " (" + getChar(this._state) + ")";
    }

    public Image getPic() {
        if (this._state == 15) {
            return EYE_.getImage();
        }
        if (this._state == 13) {
            return CHARACTER_.getImage();
        }
        if (this._state == 10) {
            return MUSTER_.getImage();
        }
        if (this._state == 11) {
            return ARMY_MUSTER_.getImage();
        }
        if (this._state == 14) {
            return PALANTIR_.getImage();
        }
        return ARMY_.getImage();
    }

    public int level() {
        return 1001;
    }

    public int nation() {
        return 0;
    }

    public int GetStats(int i) {
        return stats[i];
    }

    public void SetStats(int i, int value) {
        stats[i] = value;
    }
}
