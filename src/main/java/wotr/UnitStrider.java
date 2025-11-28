package wotr;

import java.awt.Image;
import javax.swing.ImageIcon;

/* renamed from: UnitStrider */
public class UnitStrider extends GamePiece implements Leader, Special, Flippable, ExtendedLeader, AlternativeType {
    private static String alttype = "";
    private static String type1 = Messages.getString("UnitStrider.0");
    private static String type2 = Messages.getString("UnitStrider.1");
    private int additionalLeadership;
    private int leadership;
    private ImageIcon pic1 = new ImageIcon(Messages.getLanguageLocation("images/units/Strider.png"));
    private ImageIcon pic2 = new ImageIcon(Messages.getLanguageLocation("images/units/Aragorn.png"));
    private boolean state = true;

    UnitStrider(Area startLoc) {
        super(startLoc);
    }

    UnitStrider(Area startLoc, int Leadership, int Additionalleadership) {
        super(startLoc);
        this.leadership = Leadership;
        this.additionalLeadership = Additionalleadership;
    }

    public void flip() {
        this.state = !this.state;
        if (!Game.boardtype.equals("wotr")) {
            return;
        }
        if (this.state) {
            this.leadership = 1;
            this.additionalLeadership = 1;
            return;
        }
        this.leadership = 2;
        this.additionalLeadership = 2;
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
        return this.pic2.getImage();
    }

    public int level() {
        return 385;
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
