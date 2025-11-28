package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: FreeActionDie */
public class FreeActionDie extends ActionDie implements Special {
    public static final int ARMY_MUSTER = 4;
    public static final ImageIcon ARMY_MUSTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADFParmymuster.png"));
    public static final int CHARACTER = 1;
    public static final ImageIcon CHARACTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADFPcharacter.png"));
    public static final int MUSTER = 3;
    public static final ImageIcon MUSTER_ = new ImageIcon(Messages.getLanguageLocation("images/ADFPmuster.png"));
    public static final int PALANTIR = 2;
    public static final ImageIcon PALANTIR_ = new ImageIcon(Messages.getLanguageLocation("images/ADFPevent.png"));
    public static final int WILL = 0;
    public static final ImageIcon WILL_ = new ImageIcon(Messages.getLanguageLocation("images/ADFPwill.png"));
    public static int[] stats = new int[5];
    private static String type = Messages.getString("FreeActionDie.5");

    FreeActionDie(Area startLoc) {
        super(startLoc, 0, 0);
    }

    public int mapRoll(int D6result) {
        int result = D6result - 1;
        if (result == 5) {
            return 1;
        }
        return result;
    }

    public char getChar(int s) {
        return GetChar(s);
    }

    public static char GetChar(int s) {
        if (s == 0) {
            return 'W';
        }
        if (s == 2) {
            return 'P';
        }
        if (s == 3) {
            return 'M';
        }
        if (s == 4) {
            return 'H';
        }
        if (s == 1) {
            return 'C';
        }
        return 'X';
    }

    public void setChar(String cmd) {
        int state = -1;
        if (cmd.equals("W")) {
            state = 0;
        }
        if (cmd.equals("P")) {
            state = 2;
        }
        if (cmd.equals("M")) {
            state = 3;
        }
        if (cmd.equals("H")) {
            state = 4;
        }
        if (cmd.equals("C")) {
            state = 1;
        }
        if (state >= 0) {
            SetState(state);
        }
    }

    public String type() {
        return String.valueOf(type) + " (" + getChar(this._state) + ")";
    }

    public Image getPic() {
        if (this._state == 0) {
            return WILL_.getImage();
        }
        if (this._state == 1) {
            return CHARACTER_.getImage();
        }
        if (this._state == 3) {
            return MUSTER_.getImage();
        }
        if (this._state == 4) {
            return ARMY_MUSTER_.getImage();
        }
        return PALANTIR_.getImage();
    }

    public int level() {
        return 1001;
    }

    public int nation() {
        return 11;
    }

    public int GetStats(int i) {
        return stats[i];
    }

    public void SetStats(int i, int value) {
        stats[i] = value;
    }
}
