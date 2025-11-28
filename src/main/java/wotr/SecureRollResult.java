package wotr;

/* renamed from: SecureRollResult */
public class SecureRollResult {
    private String _command;
    private int _maxValue;
    private int _nDice;
    private int[] _roll;

    public SecureRollResult(int nDice, int maxValue, int[] roll, String command) {
        this._command = command;
        this._nDice = nDice;
        this._maxValue = maxValue;
        this._roll = roll;
    }

    public String GetCommand() {
        return this._command;
    }

    public int[] GetRoll() {
        return this._roll;
    }

    public int GetNdice() {
        return this._nDice;
    }

    public int GetMaxValue() {
        return this._maxValue;
    }
}
