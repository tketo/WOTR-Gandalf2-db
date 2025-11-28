package wotr;

/* renamed from: NationComparator */
/* compiled from: Area */
class NationComparator implements GamePieceComparator {
    LevelComparator sub = new LevelComparator();

    NationComparator() {
    }

    public int compare(GamePiece x, GamePiece y) {
        if (x.nation() < y.nation()) {
            return -1;
        }
        if (x.nation() > y.nation()) {
            return 1;
        }
        return this.sub.compare(x, y);
    }
}
