package wotr;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/* renamed from: RemoteSecureDiceRoller */
public class RemoteSecureDiceRoller {
    private String _R2 = null;
    private Game _game;
    private int _maxValue;
    private int _numberOfDice;
    private String _remoteCommand = null;
    private String _remoteHash = null;
    private boolean _remoteRollInProgress = false;
    JDialog _rollDialog = null;

    public RemoteSecureDiceRoller(Game game) {
        this._game = game;
    }

    public boolean rollDiceRequest(int numberOfDice, int diceSides, String command, String hash) {
        if (!this._remoteRollInProgress) {
            this._remoteRollInProgress = true;
            this._numberOfDice = numberOfDice;
            this._maxValue = diceSides;
            this._R2 = DiceRoller.roll(this._numberOfDice, this._maxValue).trim();
            this._remoteHash = hash;
            this._remoteCommand = command.trim();
            if (!this._game.interpreter.fromFile) {
                this._rollDialog = new JDialog(this._game.win, "Opponent requested a safe dice roll", true);
                JProgressBar dpb = new JProgressBar(0, 500);
                this._rollDialog.add("Center", dpb);
                this._rollDialog.setDefaultCloseOperation(0);
                this._rollDialog.setSize(300, 75);
                this._rollDialog.setLocationRelativeTo(this._game.win);
                dpb.setIndeterminate(true);
                new Thread(new Runnable() {
                    public void run() {
                        RemoteSecureDiceRoller.this._rollDialog.setVisible(true);
                    }
                }).start();
            }
            this._game.talker.enqueue("$" + Game.prefs.nick + " secureDiceReturn " + this._R2);
            return true;
        }
        this._remoteRollInProgress = false;
        return false;
    }

    public SecureRollResult rollDiceConfirm(String salt, String opponentRoll) {
        if (!this._remoteRollInProgress) {
            this._remoteRollInProgress = false;
            return null;
        }
        this._remoteRollInProgress = false;
        if (this._rollDialog != null) {
            this._rollDialog.setVisible(false);
            this._rollDialog.dispose();
        }
        if (!Game.Hash(String.valueOf(salt) + opponentRoll).equals(this._remoteHash)) {
            JOptionPane.showMessageDialog(this._game.win, "Opponent's dice roll is invalid! Please contact the client developrers");
            return null;
        }
        int[] summedRoll = SecureDiceRoller.SumRolls(opponentRoll, this._R2, this._maxValue);
        if (summedRoll != null) {
            return new SecureRollResult(this._numberOfDice, this._maxValue, summedRoll, this._remoteCommand);
        }
        JOptionPane.showMessageDialog(this._game.win, "Error in the dice roll returned by the opponent!");
        return null;
    }
}
