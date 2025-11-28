package wotr;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;

/* renamed from: FreeFactionDice */
public class FreeFactionDice extends ActionDie implements Special {
    public static final ImageIcon NONE_ = new ImageIcon("expansion2/images/NoDice-E.png");
    public static final int PLAYDRAW = 1;
    public static final int PLAYDRAW2 = 5;
    public static final ImageIcon PLAYDRAW2_ = new ImageIcon("wome/images/FDie_fp2.png");
    public static final ImageIcon PLAYDRAW_ = new ImageIcon("wome/images/FDie_fp2.png");
    public static final int RECRUIT = 0;
    public static final int RECRUIT2 = 4;
    public static final ImageIcon RECRUIT2_ = new ImageIcon("wome/images/FDie_fp1.png");
    public static final int RECRUITDRAW = 3;
    public static final ImageIcon RECRUITDRAW_ = new ImageIcon("wome/images/FDie_fp4.png");
    public static final int RECRUITPLAY = 2;
    public static final ImageIcon RECRUITPLAY_ = new ImageIcon("wome/images/FDie_fp3.png");
    public static final ImageIcon RECRUIT_ = new ImageIcon("wome/images/FDie_fp1.png");
    public static String reportResults = "";
    public static int[] stats = new int[6];
    private static String type = Messages.getString("FreeFactionDice.0");

    /* renamed from: g */
    Game f5g;
    public boolean visible = true;

    FreeFactionDice(Area startLoc, Game g) {
        super(startLoc, 0, 0);
        this.f5g = g;
    }

    public void setstate(int s) {
        this._state = s;
    }

    public void setstateWithStats(int s) {
        this._state = s;
        if (reportResults.equals("") && this.f5g.turn > 1) {
            char[] chars = new char[(this.f5g.turn - 1)];
            Arrays.fill(chars, '-');
            reportResults = new String(chars);
        }
        reportResults = String.valueOf(reportResults) + getChar(s);
    }

    public char getChar(int s) {
        return String.valueOf(s).charAt(0);
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
            return RECRUIT_.getImage();
        }
        if (this._state == 1) {
            return PLAYDRAW_.getImage();
        }
        if (this._state == 2) {
            return RECRUITPLAY_.getImage();
        }
        if (this._state == 3) {
            return RECRUITDRAW_.getImage();
        }
        if (this._state == 4) {
            return RECRUIT2_.getImage();
        }
        if (this._state == 5) {
            return PLAYDRAW2_.getImage();
        }
        return RECRUIT_.getImage();
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
