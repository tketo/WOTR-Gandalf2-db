package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitGandalf */
public class UnitGandalf extends GamePiece implements Leader, ExtendedLeader, Special, Flippable, AlternativeType {
    private static String alttype = "";
    private static String type1 = Messages.getString("UnitGandalf.0");
    private static String type2 = Messages.getString("UnitGandalf.1");
    private int additionalLeadership;
    private int leadership;
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/units/Gandalf.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Gandalfthewhite.png"));
    private ImageIcon pic3 = new ImageIcon(Messages.getLanguageLocation("b5a/images/units/Gandalfactive-5.png"));
    private boolean state = true;

    UnitGandalf(Area startLoc) {
        super(startLoc);
    }

    UnitGandalf(Area startLoc, int Leadership, int Additionalleadership) {
        super(startLoc);
        this.leadership = Leadership;
        this.additionalLeadership = Additionalleadership;
    }

    public void flip() {
        this.state = !this.state;
    }

    public void defaultSide() {
        this.state = true;
    }

    public String type() {
        boolean z = false;
        if (!this.state) {
            return type2;
        }
        boolean z2 = !alttype.equals("");
        if (!super.currentLocation().name().equals(Messages.getKeyString("Game.1998"))) {
            z = true;
        }
        if (z2 && z) {
            return alttype;
        }
        return type1;
    }

    public Image getPic() {
        if (this.state) {
            return this.pic1.getImage();
        }
        if (alttype.equals("Gandalf")) {
            return this.pic3.getImage();
        }
        return this.pic2.getImage();
    }

    public int level() {
        return 390;
    }

    public int nation() {
        return 10;
    }

    public int AdditionalLeadership() {
        return this.additionalLeadership;
    }

    public int Leadership() {
        return this.leadership;
    }

    public boolean currentState() {
        return this.state;
    }

    public String alternativetype() {
        return alttype;
    }

    public void setalternativetype(String AlternativeType) {
        alttype = AlternativeType;
    }
}
