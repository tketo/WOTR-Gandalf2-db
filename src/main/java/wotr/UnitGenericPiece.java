package wotr;

import java.awt.Image;

/* renamed from: UnitGenericPiece */
public class UnitGenericPiece extends GamePiece implements Flippable {
    boolean state;

    public UnitGenericPiece(Area location) {
        super(true, location);
    }

    public String type() {
        return null;
    }

    public Image getPic() {
        return null;
    }

    public int level() {
        return 0;
    }

    public int nation() {
        return 0;
    }

    public void flip() {
    }

    public void defaultSide() {
    }

    public boolean currentState() {
        return this.state;
    }

    public void setState(boolean newState) {
        this.state = newState;
    }
}
