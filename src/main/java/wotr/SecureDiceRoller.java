package wotr;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/* renamed from: SecureDiceRoller */
public class SecureDiceRoller {
    private String _R1 = null;
    private String _command = null;
    private Game _game;
    private int _maxValue;
    private int _nDice;
    JDialog _rollDialog = null;
    private boolean _rollInProgress = false;
    private String _salt = null;

    public SecureDiceRoller(Game game) {
        this._game = game;
    }

    public String rollDice(int nDice, int maxValue, String command) {
        if (this._rollInProgress) {
            return null;
        }
        this._rollInProgress = true;
        this._maxValue = maxValue;
        this._nDice = nDice;
        this._command = command;
        this._R1 = DiceRoller.roll(this._nDice, this._maxValue).trim();
        this._salt = Game.GenerateRandomSalt();
        String hash = Game.Hash(String.valueOf(this._salt) + this._R1);
        this._rollDialog = new JDialog(this._game.win, Messages.getString("SecureDiceRoller.0"), true);
        JProgressBar dpb = new JProgressBar(0, 500);
        this._rollDialog.add("Center", dpb);
        this._rollDialog.setDefaultCloseOperation(0);
        this._rollDialog.setSize(300, 75);
        this._rollDialog.setLocationRelativeTo(this._game.win);
        dpb.setIndeterminate(true);
        new Thread(new Runnable() {
            public void run() {
                SecureDiceRoller.this._rollDialog.setVisible(true);
            }
        }).start();
        this._game.talker.enqueue("$" + Game.prefs.nick + " secureDiceRoll " + Integer.toString(nDice) + " " + Integer.toString(maxValue) + " " + hash + " " + command);
        return null;
    }

    public SecureRollResult rollDiceReturn(String opponentRoll) {
        if (!this._rollInProgress) {
            this._rollInProgress = false;
            return null;
        }
        this._game.talker.enqueue("$" + Game.prefs.nick + " secureDiceConfirm " + this._salt + " " + this._R1);
        this._rollDialog.setVisible(false);
        this._rollDialog.dispose();
        this._rollInProgress = false;
        int[] summedRoll = SumRolls(this._R1, opponentRoll, this._maxValue);
        if (summedRoll != null) {
            return new SecureRollResult(this._nDice, this._maxValue, summedRoll, this._command);
        }
        JOptionPane.showMessageDialog(this._game.win, "Error in the dice roll returned by the opponent!");
        return null;
    }

    public static int[] SumRolls(String R1, String R2, int maxValue) {
        String[] r1Rolls = R1.split(" ");
        String[] r2Rolls = R2.split(" ");
        if (r1Rolls.length != r2Rolls.length) {
            return null;
        }
        int[] res = new int[r1Rolls.length];
        for (int i = 0; i < r1Rolls.length; i++) {
            int roll = (Integer.parseInt(r1Rolls[i]) + Integer.parseInt(r2Rolls[i])) % maxValue;
            if (roll == 0) {
                roll = maxValue;
            }
            res[i] = roll;
        }
        return res;
    }
}
