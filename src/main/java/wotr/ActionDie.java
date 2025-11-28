package wotr;

/* renamed from: ActionDie */
public abstract class ActionDie extends GamePiece implements Recoverable {
    protected int _minimumResult;
    protected int _state;

    public abstract int GetStats(int i);

    public abstract void SetStats(int i, int i2);

    public abstract char getChar(int i);

    public abstract int mapRoll(int i);

    public ActionDie(Area startLoc, int minimumResult, int state) {
        super(startLoc);
        this._minimumResult = minimumResult;
        this._state = state;
    }

    public int Roll() {
        return mapRoll(DiceRoller.roll(6));
    }

    public int GetState() {
        return this._state;
    }

    public void SetState(int s) {
        this._state = s;
    }

    public void SetStateWithStats(int s) {
        this._state = s;
        SetStats(s - this._minimumResult, GetStats(s - this._minimumResult) + 1);
    }
}
