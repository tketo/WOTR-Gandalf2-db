package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitMouth */
public class UnitMouth extends GamePiece implements Leader, Special, ExtendedLeader, Flippable {
    private int additionalLeadership = 0;
    private int leadership = 2;
    private ImageIcon pic = new ImageIcon(Messages.getLanguageLocation("images/units/Mouth.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/MouthFlipped.png"));
    private boolean state;
    private String type = Messages.getString("UnitMouth.0");

    UnitMouth(Area startLoc) {
        super(startLoc);
    }

    UnitMouth(Area startLoc, String newType) {
        super(startLoc);
        this.type = newType;
        this.state = true;
    }

    UnitMouth(Area startLoc, int Leadership, int Additionalleadership) {
        super(startLoc);
        this.leadership = Leadership;
        this.additionalLeadership = Additionalleadership;
        this.state = true;
    }

    public String type() {
        return this.type;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic.getImage();
        }
        return this.pic2.getImage();
    }

    public int level() {
        return 1300;
    }

    public int nation() {
        return -1;
    }

    public int AdditionalLeadership() {
        return this.additionalLeadership;
    }

    public int Leadership() {
        return this.leadership;
    }

    public void flip() {
        this.state = !this.state;
    }

    public void defaultSide() {
        this.state = true;
    }

    public boolean currentState() {
        return this.state;
    }
}
