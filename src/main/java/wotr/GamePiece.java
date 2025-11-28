package wotr;

import java.awt.Image;

/* renamed from: GamePiece */
public abstract class GamePiece {
    public static final int ID_DIGITS = 3;
    private static int totalCreated = 0;
    
    // Phase 6: Callback for database sync
    private static MovementListener movementListener = null;
    
    public interface MovementListener {
        void onPieceMoved(GamePiece piece, Area from, Area to);
    }
    
    public static void setMovementListener(MovementListener listener) {
        movementListener = listener;
    }

    /* renamed from: ID */
    protected String id;
    protected Area currentLocation;

    public abstract Image getPic();

    public abstract int level();

    public abstract int nation();

    public abstract String type();

    public GamePiece(boolean dummy, Area location) {
        this.currentLocation = location;
    }

    public GamePiece(Area startLoc) {
        if (startLoc != null) {
            moveTo(startLoc);
        }
        int i = totalCreated;
        totalCreated = i + 1;
        this.id = Integer.toString(i);
        while (this.id.length() < 3) {
            this.id = "0" + this.id;
        }
    }

    public GamePiece(Area startLoc, boolean totalReset) {
        if (totalReset) {
            totalCreated = 0;
        }
        moveTo(startLoc);
        int i = totalCreated;
        totalCreated = i + 1;
        this.id = Integer.toString(i);
        while (this.id.length() < 3) {
            this.id = "0" + this.id;
        }
    }

    public void moveTo(Area dest) {
        Area oldLocation = this.currentLocation;
        
        // Only remove from old location if piece is actually there
        // During replay/sync, currentLocation might not match the actual area
        if (this.currentLocation != null && this.currentLocation.pieces().contains(this)) {
            currentLocation().removePiece(this);
        }
        
        // Don't re-add if already in destination (avoid duplicates during sync)
        if (!dest.pieces().contains(this)) {
            dest.addPiece(this);
        }
        this.currentLocation = dest;
        
        // Phase 6: Notify listener of movement
        if (movementListener != null && oldLocation != null && oldLocation != dest) {
            movementListener.onPieceMoved(this, oldLocation, dest);
        }
    }

    public void setLocation(Area location) {
        this.currentLocation = location;
    }

    public Area currentLocation() {
        return this.currentLocation;
    }

    public String toString() {
        return id();
    }

    /* renamed from: id */
    public String id() {
        return this.id;
    }

    public void setID(String newID) {
        this.id = newID;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof GamePiece)) {
            return false;
        }
        GamePiece other = (GamePiece) obj;
        // Compare pieces by ID - two pieces are equal if they have the same ID
        return this.id != null && this.id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        // Use ID for hash code
        return this.id != null ? this.id.hashCode() : 0;
    }
}
