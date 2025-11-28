package wotr;

/* renamed from: LevelComparator */
/* compiled from: Area */
class LevelComparator implements GamePieceComparator {
    LevelComparator() {
    }

    public int compare(GamePiece x, GamePiece y) {
        if (x.level() < y.level()) {
            return -1;
        }
        if (x.level() > y.level()) {
            return 1;
        }
        return 0;
    }
}
