package wotr;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/* renamed from: Interpreter */
public class Interpreter implements ActionListener {
    private static final int DEFAULT_REPLAY_SPEED = 2000;
    boolean Encrypted;
    private boolean _safeMode = false;
    boolean alwaysReport = false;
    public boolean balrogBrighterTimer;
    int balrogFlashes;
    boolean cardBrighterTimer;
    int cardFlashes;
    public boolean contactTimer;
    int dice1flashpos = -1;
    int dice2flashpos = -1;
    int dice3flashpos = -1;
    int dice4flashpos = -1;
    int dice5flashpos = -1;
    int diceFlashes;
    public boolean diceTimer;
    Timer flashTimer;
    boolean fromFile;
    Game game;
    /* access modifiers changed from: private */
    public boolean goblinpassnorthTimer;
    /* access modifiers changed from: private */
    public boolean goblinpasswestTimer;
    private FileWriter log;
    private String logHash = null;
    private String logName;
    private StringBuilder logText;
    int northpassFlashes;
    boolean onlyForOpponent;
    int preHuntFlashes;
    public boolean preHuntTimer;
    private BufferedReader replayBuffer;
    Timer timer;
    int westpassFlashes;
    public boolean witchkingBrighterTimer;
    public boolean wkFlash;
    int wkFlashes;

    public Interpreter(Game g, String logName2) {
        this.game = g;
        setLogFile(logName2);
        this.fromFile = false;
        this.onlyForOpponent = false;
        this.timer = new Timer(DEFAULT_REPLAY_SPEED, this);
        this.timer.setInitialDelay(0);
        this.preHuntFlashes = 0;
        this.balrogFlashes = 0;
        this.flashTimer = new Timer(350, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Interpreter.this.preHuntTimer) {
                    Interpreter.this.flash_preHunt();
                }
                if (Interpreter.this.balrogBrighterTimer) {
                    Interpreter.this.flash_Balrog();
                }
                if (Interpreter.this.witchkingBrighterTimer) {
                    Interpreter.this.flash_WK();
                }
                if (Interpreter.this.cardBrighterTimer) {
                    Interpreter.this.flash_Cards();
                }
                if ((Interpreter.this.dice1flashpos > -1 || Interpreter.this.dice2flashpos > -1 || Interpreter.this.dice3flashpos > -1 || Interpreter.this.dice4flashpos > -1 || Interpreter.this.dice5flashpos > -1) && Interpreter.this.diceTimer) {
                    Interpreter.this.flash_Dice();
                }
                if (Interpreter.this.goblinpassnorthTimer) {
                    Interpreter.this.flash_NorthPass();
                }
                if (Interpreter.this.goblinpasswestTimer) {
                    Interpreter.this.flash_WestPass();
                }
                if (Interpreter.this.contactTimer) {
                    Interpreter.this.game.playSound("soundpack/connect.wav");
                }
            }
        });
        this.flashTimer.start();
    }

    public void loadFile(File f) {
        this.fromFile = true;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                execute(this.game.talker.DeHashCommand(br.readLine()));
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.0")) + e);
        } catch (IOException e2) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.1")) + e2);
        } finally {
            this.fromFile = false;
        }
    }

    public String GetLogName() {
        return this.logName;
    }

    public String GetLogText() {
        return this.logText.toString();
    }

    public void SetLastHash(String hash) {
        this.logHash = hash;
    }

    /* access modifiers changed from: package-private */
    public void VerifyTheLogConsistency() {
        String password;
        if (this.game.hasFPpassword()) {
            password = this.game.GetFPpassword();
        } else {
            password = this.game.GetSPpassword();
        }
        if (password != null) {
            BufferedReader reader = new BufferedReader(new StringReader(this.logText.toString()));
            int Nwarnings = 0;
            try {
                StringBuilder oldText = new StringBuilder();
                while (true) {
                    String newLine = reader.readLine();
                    if (newLine == null) {
                        break;
                    }
                    if (newLine.startsWith("$" + Game.prefs.nick) && !newLine.startsWith("$" + Game.prefs.nick + " sendTokens")) {
                        String cmd = this.game.talker.DeHashCommand(newLine);
                        String hash = this.logHash;
                        if (!hash.equals("") && !this.game.talker.ShortHash(oldText + cmd).equals(hash)) {
                            Nwarnings++;
                        }
                    }
                    oldText.append(String.valueOf(newLine) + "\n");
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Nwarnings > 0) {
                JOptionPane.showMessageDialog(Game.win, "WARNING: There are " + Nwarnings + " consistency problems with the log file. There is a risk that the logfile was manipulated outside of the game.");
            }
        }
    }

    public void loadReplay(File f) {
        try {
            this.replayBuffer = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.2")) + e);
        }
    }

    public void loadFileForOpponent(File f) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                this.game.talker.enqueuedirect(br.readLine(), (ObserverHost) null);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.4")) + e);
        } catch (IOException e2) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.5")) + e2);
        }
    }

    public static void loadFileForObserver(File f, ObserverNode observer) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                observer.enqueue(br.readLine());
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.6")) + e);
        } catch (IOException e2) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.7")) + e2);
        }
    }

    public void sendLog() {
        this.game.talker.enqueuedirect(Messages.getKeyString("Interpreter.8"), (ObserverHost) null);
        this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
        loadFileForOpponent(new File(this.logName));
        this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
        this.game.talker.enqueuedirect(Messages.getString("Interpreter.17"), (ObserverHost) null);
        this.game.synchronizing = false;
    }

    public void sendLog(ObserverNode observer) {
        observer.enqueue(Messages.getKeyString("Interpreter.18"));
        observer.enqueue("<mute_on> " + Game.prefs.nick);
        loadFileForObserver(new File(this.logName), observer);
        observer.enqueue("<mute_off> " + Game.prefs.nick);
        observer.enqueue(Messages.getKeyString("Interpreter.21"));
    }

    public void setLogFile(String s) {
        try {
            this.log = new FileWriter(s, true);
            this.logName = s;
            this.logText = new StringBuilder();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    private static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len <= 0) {
                in.close();
                out.close();
                return;
            }
            out.write(buf, 0, len);
        }
    }

    public void save(File target) {
        try {
            copy(new File(this.logName), target);
        } catch (IOException e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.22")) + e);
        }
    }

    public void executeSafely(String cmd) {
        this._safeMode = true;
        this.logHash = null;
        execute(cmd);
        this._safeMode = false;
    }

    public void execute(String inputCmd) {
        String cmd = inputCmd.trim();
        String unalteredCmd = cmd;
        if (Game.win.getExtendedState() == 1) {
            this.game.playSound("soundpack/connect.wav");
        }
        String cmd2 = Messages.removeKeyString(cmd);
        System.out.println("Command - " + cmd2);
        if ((cmd2.startsWith("<client>") || cmd2.startsWith("<host>")) && cmd2.indexOf(Messages.getString("Interpreter.35")) > 0) {
            if (!Game.prefs.nick.equals(cmd2.substring(cmd2.indexOf(">") + 2, cmd2.indexOf(Messages.getString("Interpreter.37"))))) {
                this.game.opponent = cmd2.substring(cmd2.indexOf(">") + 2, cmd2.indexOf(Messages.getString("Interpreter.39")));
            } else {
                if (cmd2.startsWith("<client>") && this.game.talker.server) {
                    this.game.opponent = String.valueOf(Game.prefs.nick) + "_B";
                }
                if (cmd2.startsWith("<host>") && !this.game.talker.server) {
                    Game.prefs.setNick(String.valueOf(Game.prefs.nick) + "_B");
                }
            }
            exContact(cmd2);
            return;
        }
        if (cmd2.charAt(cmd2.length() - 1) != ' ') {
            cmd2 = String.valueOf(cmd2) + " ";
        }
        if (cmd2.length() >= 10) {
            if ("<mute_on>".equals(cmd2.substring(0, 9))) {
                if (cmd2.startsWith("<mute_on> " + Game.prefs.nick)) {
                    this.onlyForOpponent = true;
                    return;
                } else {
                    this.fromFile = true;
                    return;
                }
            } else if ("<mute_off>".equals(cmd2.substring(0, 10))) {
                if (cmd2.startsWith("<mute_off> " + Game.prefs.nick)) {
                    this.onlyForOpponent = false;
                    return;
                } else {
                    this.fromFile = false;
                    return;
                }
            }
        }
        if (this.onlyForOpponent) {
            String cmd3 = unalteredCmd;
            return;
        }
        record(unalteredCmd);
        if (cmd2.length() > 0 && (cmd2.charAt(0) == '$' || cmd2.startsWith("<auto>"))) {
            if (this.fromFile) {
                cmd2 = "%" + cmd2.substring(1, cmd2.length());
            }
            int nextspace = cmd2.indexOf(" ");
            if (nextspace != -1) {
                String nick = cmd2.substring(0, nextspace);
                String cmd4 = cmd2.substring(nextspace + 1, cmd2.length());
                int nextspace2 = cmd4.indexOf(" ");
                if (nextspace2 != -1) {
                    String token = cmd4.substring(0, nextspace2);
                    if (token.equals("nowait")) {
                        this.game.waiting = false;
                        this.game.dialog.setVisible(false);
                    }
                    if (this.game.waiting) {
                        return;
                    }
                    if (token.equals("playchosen")) {
                        this.game.controls.cbh.actionPerformed_playchosen();
                        return;
                    }
                    if (token.equals("changes")) {
                        exChange(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.48"))) {
                        createHistory();
                        exMove(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.49"))) {
                        if (this._safeMode || !this.game.talker.connected || this.fromFile) {
                            exRoll(cmd4.substring(nextspace2 + 1), nick);
                        } else {
                            JOptionPane.showMessageDialog(Game.win, "This command is no longer supported, use a secure version of the client please!");
                        }
                    }
                    if (token.equals(Messages.getString("Interpreter.50"))) {
                        exDraw(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.51"))) {
                        createHistory();
                        exKill(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.52"))) {
                        createHistory();
                        exFlip(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.53"))) {
                        exChoose(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.43"))) {
                        exUnchoose(nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.54"))) {
                        createHistory();
                        exAllocate(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.11"))) {
                        exDamage(cmd4.substring(nextspace2 + 1), nick, -1);
                    }
                    if (token.equals(Messages.getString("Interpreter.13"))) {
                        exDamage(cmd4.substring(nextspace2 + 1), nick, 1);
                    }
                    if (token.equals("silent")) {
                        this.game.controls.chat.silent = true;
                    }
                    if (token.equals("noisy")) {
                        this.game.controls.chat.silent = false;
                    }
                    if (token.equals(Messages.getString("Interpreter.14"))) {
                        exInitialCounters();
                    }
                    if (token.equals(Messages.getString("Interpreter.60"))) {
                        exPassword(cmd4, nick);
                    }
                    if (token.equals("sendTokens")) {
                        exTokens(cmd4, nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.61"))) {
                        exUndo(cmd4, nick);
                    }
                    if (token.equals(Messages.getString("Interpreter.20"))) {
                        exRedo(cmd4, nick);
                    }
                    if (token.equals("names")) {
                        exNames(cmd4.substring(nextspace2 + 1));
                    }
                    if (token.equals("wait") && this.game.talker.connected) {
                        this.game.waiting = true;
                        this.game.dialog.setUndecorated(true);
                        JLabel label = new JLabel(Messages.getString("Interpreter.476"));
                        label.setFont(new Font("Tolkien", 1, 40));
                        this.game.dialog.add(label);
                        this.game.dialog.pack();
                        this.game.dialog.setLocation((Game.win.getWidth() - label.getWidth()) / 2, (Game.win.getHeight() - label.getHeight()) / 2);
                        this.game.dialog.setVisible(true);
                        this.game.dialog.setAlwaysOnTop(true);
                    }
                    if (token.equals("challengeswith")) {
                        exChallenge(cmd4.substring(nextspace2 + 1), nick);
                    }
                    if (token.equals("secureDiceRoll")) {
                        exSecureDiceRollRequest(cmd4, nick);
                    }
                    if (token.equals("secureDiceReturn")) {
                        exSecureDiceRollReturn(cmd4, nick);
                    }
                    if (token.equals("secureDiceConfirm")) {
                        exSecureDiceRollConfirm(cmd4, nick);
                    }
                    if (token.startsWith(Messages.getString("Interpreter.38"))) {
                        exAddDwarvenRings(cmd4);
                    }
   }
            }
        } else if (cmd2.length() > 0 && cmd2.charAt(0) == '<') {
            specialCommand(cmd2);
        } else {
            // Handle FSM macro commands (keyboard shortcuts)
            handleMacroCommand(cmd2.trim());
        }
        this.game.refreshBoard();
        String cmd5 = unalteredCmd;
    }

    /**
     * Handle FSM macro commands (keyboard shortcuts)
     * Maps keyboard shortcuts to button action commands
     */
    private void handleMacroCommand(String cmd) {
        if (cmd == null || cmd.isEmpty()) {
            return;
        }
        
        // Map FSM keyboard shortcuts to button actions
        ActionEvent actionEvent = null;
        
        switch (cmd) {
            // Phase 1: Card drawing shortcuts
            case "F7":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fdrawc");
                break;
            case "F8":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fdraws");
                break;
            case "F11":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "sdrawc");
                break;
            case "F12":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "sdraws");
                break;
            case "Ctrl+9":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "frecover");
                break;
            case "Ctrl+0":
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "srecover");
                break;
            // Add more macro mappings as needed
            default:
                // Unknown macro, ignore silently
                return;
        }
        
        // Execute the action through the button handler
        if (actionEvent != null && this.game.controls.cbh != null) {
            this.game.controls.cbh.actionPerformed(actionEvent);
        }
    }

    private void exSecureDiceRollRequest(String inputCmd, String nick) {
        if (!nick.equals("$" + Game.prefs.nick)) {
            String cmd = inputCmd.substring(inputCmd.indexOf(" ") + 1);
            int nextspace = cmd.indexOf(" ");
            int numberOfDice = Integer.parseInt(cmd.substring(0, nextspace));
            String cmd2 = cmd.substring(nextspace + 1);
            int nextspace2 = cmd2.indexOf(" ");
            int diceSides = Integer.parseInt(cmd2.substring(0, nextspace2));
            String cmd3 = cmd2.substring(nextspace2 + 1);
            int nextspace3 = cmd3.indexOf(" ");
            String hash = cmd3.substring(0, nextspace3);
            if (!this.game.remoteDiceRoller.rollDiceRequest(numberOfDice, diceSides, cmd3.substring(nextspace3 + 1), hash)) {
                this.game.controls.chat.write("Security problem, the opponent requesting multiple rolls!");
            }
        }
    }

    private void exSecureDiceRollReturn(String inputCmd, String nick) {
        if (!nick.equals("$" + Game.prefs.nick)) {
            SecureRollResult rollResult = this.game.diceRoller.rollDiceReturn(inputCmd.substring(inputCmd.indexOf(" ") + 1).trim());
            System.out.println("executing roll $" + Game.prefs.nick);
            Execute("$" + Game.prefs.nick, rollResult);
        }
    }

    private void exSecureDiceRollConfirm(String inputCmd, String nick) {
        if (!nick.equals("$" + Game.prefs.nick)) {
            String cmd = inputCmd.substring(inputCmd.indexOf(" ") + 1);
            int nextspace = cmd.indexOf(" ");
            Execute(nick, this.game.remoteDiceRoller.rollDiceConfirm(cmd.substring(0, nextspace), cmd.substring(nextspace + 1).trim()));
        }
    }

    private void Execute(String nick, SecureRollResult rollResult) {
        if (rollResult != null) {
            String command = rollResult.GetCommand();
            if (command.equals("roll D6 dice")) {
                LocalSafeExecution_D6roll(nick, rollResult);
            }
            if (command.equals("roll D6 dice (maneuver)")) {
                LocalSafeExecution_D6rollM(nick, rollResult);
            }
            if (command.equals("draw companion")) {
                LocalSafeExecution_DrawCompanion(nick, rollResult);
            }
            if (command.equals("draw FPSTRA card")) {
                LocalSafeExecution_DrawFPSTRAcard(nick, rollResult);
            }
            if (command.equals("draw FPCHAR card")) {
                LocalSafeExecution_DrawFPCHARcard(nick, rollResult);
            }
            if (command.equals("draw FPSTORY card")) {
                LocalSafeExecution_DrawFPSTORYcard(nick, rollResult);
            }
            if (command.equals("draw FPFACTION card")) {
                LocalSafeExecution_DrawFPFACTIONcard(nick, rollResult);
            }
            if (command.equals("draw FPFACTION2 card")) {
                LocalSafeExecution_DrawFPFACTION2card(nick, rollResult);
            }
            if (command.equals("draw SASTRA card")) {
                LocalSafeExecution_DrawSASTRAcard(nick, rollResult);
            }
            if (command.equals("draw SACHAR card")) {
                LocalSafeExecution_DrawSACHARcard(nick, rollResult);
            }
            if (command.equals("draw SASTORY card")) {
                LocalSafeExecution_DrawSASTORYcard(nick, rollResult);
            }
            if (command.equals("draw SAFACTION card")) {
                LocalSafeExecution_DrawSAFACTIONcard(nick, rollResult);
            }
            if (command.equals("draw SAFACTION2 card")) {
                LocalSafeExecution_DrawSAFACTION2card(nick, rollResult);
            }
            if (command.equals("draw FATE card")) {
                LocalSafeExecution_DrawFATEcard(nick, rollResult);
            }
            if (command.equals("draw HUNT tile")) {
                LocalSafeExecution_DrawHUNTtile(nick, rollResult);
            }
            if (command.equals("draw WWSAT")) {
                LocalSafeExecution_DrawWWSAT(nick, rollResult);
            }
            if (command.startsWith("roll ACTION dice")) {
                LocalSafeExecution_ActionDiceRoll(nick, rollResult);
            }
            if (command.equals("draw Grima card")) {
                LocalSafeExecution_DrawGrimaCard(rollResult);
            }
            if (command.startsWith("flip token ")) {
                LocalSafeExecution_DrawRecruitTile(nick, rollResult, command.substring(11));
            }
        } else if (!this.fromFile || this.game.synchronizing) {
            record("<game> Problem with integrity of a received opponent's dice roll!");
            this.game.controls.chat.write("<game> Problem with integrity of a received opponent's dice roll!");
        }
    }

    private void LocalSafeExecution_DrawSAFACTIONcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[201].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[201].get(0) + Messages.getKeyString("Controls.510") + Game.areas[201].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
        } else if (Game.areas[198].numPieces() > 0) {
            if (Game.areas[198].numPieces() != rollResult.GetMaxValue()) {
                JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[198].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
                return;
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[198].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[198].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
        } else if (Game.areas[200].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[200].get(0) + Messages.getKeyString("Controls.510") + Game.areas[200].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
        }
    }

    private void LocalSafeExecution_DrawSAFACTION2card(String nick, SecureRollResult rollResult) {
        if (Game.areas[201].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[201].get(0) + Messages.getKeyString("Controls.510") + Game.areas[201].name() + Messages.getKeyString("Controls.511") + Game.areas[203].name() + " ");
        } else if (Game.areas[198].numPieces() > 0) {
            if (Game.areas[198].numPieces() != rollResult.GetMaxValue()) {
                JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[198].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
                return;
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[198].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[198].name() + Messages.getKeyString("Controls.511") + Game.areas[203].name() + " ");
        } else if (Game.areas[200].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[200].get(0) + Messages.getKeyString("Controls.510") + Game.areas[200].name() + Messages.getKeyString("Controls.511") + Game.areas[203].name() + " ");
        }
    }

    private void LocalSafeExecution_DrawFPFACTIONcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[197].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[197].get(0) + Messages.getKeyString("Controls.510") + Game.areas[197].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
        } else if (Game.areas[194].numPieces() > 0) {
            if (Game.areas[194].numPieces() != rollResult.GetMaxValue()) {
                JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[194].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
                return;
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[194].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[194].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
        } else if (Game.areas[196].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[196].get(0) + Messages.getKeyString("Controls.510") + Game.areas[196].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
        }
    }

    private void LocalSafeExecution_DrawFPFACTION2card(String nick, SecureRollResult rollResult) {
        if (Game.areas[197].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[197].get(0) + Messages.getKeyString("Controls.510") + Game.areas[197].name() + Messages.getKeyString("Controls.511") + Game.areas[202].name() + " ");
        } else if (Game.areas[194].numPieces() > 0) {
            if (Game.areas[194].numPieces() != rollResult.GetMaxValue()) {
                JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[194].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
                return;
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[194].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[194].name() + Messages.getKeyString("Controls.511") + Game.areas[202].name() + " ");
        } else if (Game.areas[196].numPieces() > 0) {
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[196].get(0) + Messages.getKeyString("Controls.510") + Game.areas[196].name() + Messages.getKeyString("Controls.511") + Game.areas[202].name() + " ");
        }
    }

    private void LocalSafeExecution_D6roll(String nick, SecureRollResult rollResult) {
        int[] roll = rollResult.GetRoll();
        String rollText = " ";
        for (int i = 0; i < rollResult.GetNdice(); i++) {
            rollText = String.valueOf(rollText) + roll[i] + " ";
        }
        executeSafely("<game> " + nick.substring(1) + Messages.getString("Controls.1") + rollText);
    }

    private void LocalSafeExecution_D6rollM(String nick, SecureRollResult rollResult) {
        int[] roll = rollResult.GetRoll();
        String rollText = " ";
        for (int i = 0; i < rollResult.GetNdice(); i++) {
            rollText = String.valueOf(rollText) + roll[i] + " ";
        }
        executeSafely("<game> " + nick.substring(1) + Messages.getString("Controls.111") + rollText);
    }

    private void LocalSafeExecution_DrawCompanion(String nick, SecureRollResult rollResult) {
        Area temp = new Area("temp");
        temp.addAllPieces(Game.areas[115]);
        temp.addAllPieces(Game.areas[116]);
        if (temp.numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw companions from a wrong set of companions! Please ensure you both have the same set of companions in the fellowship box and try to draw again.");
            return;
        }
        GamePiece g = temp.get(rollResult.GetRoll()[0] - 1);
        if (g != null) {
            Area dest = Game.areas[153];
            if (dest.numPieces() > 0) {
                dest = Game.areas[154];
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.637") + g + Messages.getKeyString("Controls.638") + g.currentLocation().name() + Messages.getKeyString("Controls.639") + dest.name() + " ");
        }
    }

    private void LocalSafeExecution_DrawFPSTRAcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[122].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[122].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[122].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[122].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
    }

    private void LocalSafeExecution_DrawFATEcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[0].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[0].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[0].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[0].name() + Messages.getKeyString("Controls.511") + Game.areas[154].name() + " ");
    }

    private void LocalSafeExecution_DrawFPCHARcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[121].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[121].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[121].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[121].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
    }

    private void LocalSafeExecution_DrawFPSTORYcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[82].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[82].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[82].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[82].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
    }

    private void LocalSafeExecution_DrawSASTRAcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[124].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[124].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[124].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[124].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
    }

    private void LocalSafeExecution_DrawSACHARcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[123].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[123].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[123].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[123].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
    }

    private void LocalSafeExecution_DrawSASTORYcard(String nick, SecureRollResult rollResult) {
        if (Game.areas[81].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a card from " + Game.areas[81].name() + ", but you have a different number of cards in the deck. Ensure your decks have the same cards and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.509") + Game.areas[81].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.510") + Game.areas[81].name() + Messages.getKeyString("Controls.511") + Game.areas[181].name() + " ");
    }

    private void LocalSafeExecution_DrawHUNTtile(String nick, SecureRollResult rollResult) {
        if (Game.areas[182].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a hunt tile, but you have a different number of tiles in the hunt pool. Ensure your hunt pools have the same tiles and try to draw again.");
            return;
        }
        GamePiece g = Game.areas[182].get(rollResult.GetRoll()[0] - 1);
        Area dest = Game.areas[154];
        if (dest.numPieces() > 0) {
            dest = Game.areas[153];
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.624") + g + Messages.getKeyString("Controls.625") + Game.areas[182].name() + Messages.getKeyString("Controls.626") + dest.name() + " ");
    }

    private void LocalSafeExecution_DrawRecruitTile(String nick, SecureRollResult rollResult, String gamepieceid) {
        Area temp = new Area("temp");
        Area tempHolding = new Area("Holding");
        Area tempSelectArea = new Area("temp2");
        temp.clearAllPieces();
        for (int p = 0; p <= this.game.bits.length - 1; p++) {
            GamePiece gp = this.game.bits[p];
            if ((gp instanceof UnitRecruitmentToken) && gp.nation() == this.game.bits[Integer.parseInt(gamepieceid)].nation() && !gp.currentLocation.equals(Game.areas[175]) && !gp.currentLocation.equals(Game.areas[177]) && ((TwoChit) gp).currentState() && !tempHolding.containsPiece(gp)) {
                temp.addPiece(gp);
            }
        }
        GamePiece swappiece = temp.get(rollResult.GetRoll()[0] - 1);
        GamePiece originalgpchosen = this.game.bits[Integer.parseInt(gamepieceid)];
        tempSelectArea.addPiece(originalgpchosen);
        temp.clearAllPieces();
        for (int p2 = 0; p2 <= tempHolding.numPieces() - 1; p2++) {
            if (this.game.selection.containsPiece(tempHolding.get(p2))) {
                temp.addPiece(tempHolding.get(p2));
            }
        }
        if (!originalgpchosen.id.equals(swappiece.id)) {
            Area fliplocation = originalgpchosen.currentLocation();
            GamePiece sparegp = swappiece;
            executeSafely("$base silent ");
            executeSafely("<auto><~Scribe.141~>" + originalgpchosen + " <~Scribe.142~>" + originalgpchosen.currentLocation() + "<~Scribe.143~>" + sparegp.currentLocation() + "<~Scribe.144~>");
            executeSafely("<auto><~Scribe.141~>" + sparegp + " <~Scribe.142~>" + sparegp.currentLocation() + "<~Scribe.143~>" + fliplocation + "<~Scribe.144~>");
            executeSafely("$base noisy ");
        }
        if (temp.numPieces() > 0) {
            this.game.selection.clearAllPieces();
            for (int i = 0; i <= tempHolding.numPieces() - 1; i++) {
                this.game.selection.addPiece(tempHolding.get(i));
            }
        }
        executeSafely("$" + Game.prefs.nick + Messages.getKeyString("Controls.323") + swappiece);
    }

    private void LocalSafeExecution_DrawWWSAT(String nick, SecureRollResult rollResult) {
        Area chars = new Area("wwsat");
        for (int i = 0; i < Game.areas[180].numPieces(); i++) {
            GamePiece p = Game.areas[180].get(i);
            if (p instanceof FreeCharacterCard) {
                chars.addPiece(p);
            }
        }
        if (chars.numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw for WWSAT, but you have a different number of character cards on FP player's hand. Ensure you both have the same number of cards on the FP player's hand and try to draw again.");
            return;
        }
        executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.452") + chars.get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Controls.453") + Game.areas[180] + Messages.getKeyString("Controls.454") + Game.areas[175] + " ");
    }

    private void LocalSafeExecution_ActionDiceRoll(String nick, SecureRollResult rollResult) {
        String[] dice = rollResult.GetCommand().split(" ");
        Area temp = new Area("temp");
        int i = dice.length - 1;
        while (i >= 3) {
            try {
                GamePiece p = this.game.bits[Integer.parseInt(dice[i])];
                if (!(p instanceof ActionDie)) {
                    JOptionPane.showMessageDialog(Game.win, "Error, the opponent did not roll an action die!");
                    return;
                }
                temp.addPiece(p);
                unChoose(p);
                i--;
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(Game.win, "Error: Message format problem!");
                return;
            }
        }
        if (rollResult.GetMaxValue() != 6) {
            JOptionPane.showMessageDialog(Game.win, "Error, the opponent did not use 6-sided dice!");
        } else if (dice.length - 3 != rollResult.GetNdice()) {
            JOptionPane.showMessageDialog(Game.win, "Error. The opponent rolled inconsistent number of action dice.");
        } else {
            int N = dice.length - 3;
            int[] roll = rollResult.GetRoll();
            String rollText = "";
            for (int i2 = 0; i2 < N; i2++) {
                rollText = String.valueOf(rollText) + ((ActionDie) temp.get(i2)).mapRoll(roll[i2]) + " ";
            }
            executeSafely(String.valueOf(nick) + Messages.getKeyString("Controls.345") + temp.contents() + Messages.getKeyString("Controls.346") + rollText);
            if (Game.boardtype.equals("wotr")) {
                String eyeResult = new Integer(15).toString();
                if (temp.pieces().get(0).currentLocation == Game.areas[179] && (temp.numPieces() != 1 || rollText.contains(eyeResult))) {
                    execute(String.valueOf(nick) + Messages.getKeyString("Controls.350") + temp.contents());
                }
            }
        }
    }

    private void LocalSafeExecution_DrawGrimaCard(SecureRollResult rollResult) {
        if (Game.areas[0].numPieces() != rollResult.GetMaxValue()) {
            JOptionPane.showMessageDialog(Game.win, "Warning. The opponent attempted to draw a Grima card but the number of cards in your decks differ! Please ensure you both have the same set of Grima cards in the deck and try to draw again.");
            return;
        }
        executeSafely("<auto>" + Messages.getKeyString("Scribe.20") + Game.areas[0].get(rollResult.GetRoll()[0] - 1) + Messages.getKeyString("Scribe.21") + Game.areas[0].name() + Messages.getKeyString("Scribe.22") + Game.areas[181].name() + " ");
    }

    private void exChallenge(String inputCmd, String nick) {
        String cmd = " " + inputCmd;
        this.game.challengeShadowCombat = cmd.substring(0, cmd.indexOf(":"));
        int temppointer = cmd.indexOf(":");
        this.game.challengeShadowAction = cmd.substring(temppointer + 1, cmd.indexOf(":", temppointer + 1));
        int temppointer2 = cmd.indexOf(":", temppointer + 1);
        this.game.challengeFreeCombat = cmd.substring(temppointer2 + 1, cmd.indexOf(":", temppointer2 + 1));
        int temppointer3 = cmd.indexOf(":", temppointer2 + 1);
        this.game.challengeFreeAction = cmd.substring(temppointer3 + 1, cmd.indexOf(":", temppointer3 + 1));
        this.game.challengeShadowCombatPos = 0;
        this.game.challengeFreeCombatPos = 0;
        this.game.challengeShadowActionPos = 0;
        this.game.challengeFreeActionPos = 0;
        this.game.controls.chat.writenohistory(String.valueOf(nick) + " loads a challenge file");
    }

    private void exChange(String inputCmd, String nick) {
        GamePiece unit = null;
        String cmd = inputCmd;
        int nextspace = cmd.indexOf(" ");
        if (nextspace != -1) {
            unit = this.game.bits[Integer.parseInt(cmd.substring(0, nextspace))];
            cmd = cmd.substring(nextspace + 1);
        }
        String cmd2 = cmd.substring(cmd.indexOf("to ") + 3).trim();
        String originalValue = unit.type();
        if (unit instanceof ShadowActionDie) {
            ((ShadowActionDie)unit).setChar(cmd2);
        }
        if (unit instanceof FreeActionDie) {
            ((FreeActionDie)unit).setChar(cmd2);
        }
        this.game.controls.chat.write(String.valueOf(nick) + Messages.getString("Interpreter.478") + originalValue + Messages.getString("Interpreter.479") + cmd2);
        this.game.refreshBoard();
    }

    private void exNames(String inputCmd) {
        GamePiece unit = null;
        String name = null;
        this.game.FSPchosen = true;
        String cmd = inputCmd;
        int nextspace = cmd.indexOf(" ");
        if (nextspace != -1) {
            String token = cmd.substring(0, nextspace);
            unit = this.game.bits[Integer.parseInt(token)];
            cmd = String.valueOf(cmd.substring(nextspace + 1)) + " ";
        }
        if ((nextspace = cmd.indexOf(" ")) != -1) {
            name = cmd.substring(0, nextspace);
        }
        if (name.equals("Boromir1")) {
            ((UnitBoromir)unit).setalternativetype(Messages.getString("Interpreter.33"));
        }
        if (name.equals("Boromir2")) {
            ((UnitBoromir)unit).setalternativetype(Messages.getString("Interpreter.36"));
        }
        if (name.equals("Gandalf1")) {
            ((UnitGandalf)unit).setalternativetype(Messages.getString("Interpreter.40"));
        }
        if (name.equals("Gandalf2")) {
            ((UnitGandalf)unit).setalternativetype(Messages.getString("Interpreter.42"));
        }
        if (name.equals("Strider1")) {
            ((UnitStrider)unit).setalternativetype(Messages.getString("Interpreter.45"));
        }
        if (name.equals("Strider2")) {
            ((UnitStrider)unit).setalternativetype(Messages.getString("Interpreter.67"));
        }
        if (name.equals("Legolas1")) {
            ((UnitLegolas)unit).setalternativetype(Messages.getString("Interpreter.80"));
        }
        if (name.equals("Legolas2")) {
            ((UnitLegolas)unit).setalternativetype(Messages.getString("Interpreter.84"));
        }
        if (name.equals("Merry1")) {
            ((UnitMerry)unit).setalternativetype(Messages.getString("Interpreter.86"));
        }
        if (name.equals("Merry2")) {
            ((UnitMerry)unit).setalternativetype(Messages.getString("Interpreter.89"));
        }
        if (name.equals("Gimli1")) {
            ((UnitGimli)unit).setalternativetype(Messages.getString("Interpreter.102"));
        }
        if (name.equals("Gimli2")) {
            ((UnitGimli)unit).setalternativetype(Messages.getString("Interpreter.121"));
        }
        if (name.equals("Pippin1")) {
            ((UnitPippin)unit).setalternativetype(Messages.getString("Interpreter.124"));
        }
        if (name.equals("Pippin2")) {
            ((UnitPippin)unit).setalternativetype(Messages.getString("Interpreter.130"));
        }
    }

    private void createHistory() {
        GamePiece[] historyentry = copybits(this.game.bits);
        if (this.game.historypointer < this.game.history.size()) {
            this.game.history.set(this.game.historypointer, historyentry);
            while (this.game.historypointer < this.game.history.size() - 1) {
                this.game.history.remove(this.game.historypointer + 1);
            }
        } else {
            this.game.history.add(historyentry);
        }
        this.game.historypointer++;
    }

    public void specialCommand(String chatCommand) {
        int turnNumber;
        String results;
        String chat;
        boolean isFPplayer = false;
        String chat2 = chatCommand;
        try {
            if (chatCommand.startsWith("<game> ") && chatCommand.contains(Messages.getString("Interpreter.64"))) {
                if (!this.game.talker.connected || this._safeMode || this.fromFile) {
                    String player = chatCommand.substring(7);
                    if (!player.substring(0, player.indexOf(" ")).equals(this.game.shadowname)) {
                        isFPplayer = true;
                    }
                    if (chatCommand.contains(Messages.getString("Interpreter.16"))) {
                        results = chatCommand.substring(chatCommand.lastIndexOf(Messages.getString("Interpreter.19")) + Messages.getString("Interpreter.19").length());
                        chat = chatCommand.substring(0, chatCommand.indexOf(results));
                    } else if (this.game.challengeShadowCombatPos > -1 && !isFPplayer) {
                        String results2 = chatCommand.substring(chatCommand.lastIndexOf(Messages.getString("Interpreter.69")) + Messages.getString("Interpreter.69").length());
                        chat = chatCommand.substring(0, chatCommand.indexOf(results2));
                        if (this.game.challengeShadowCombat.length() - this.game.challengeShadowCombatPos >= results2.length()) {
                            results = this.game.challengeShadowCombat.substring(this.game.challengeShadowCombatPos + 1, this.game.challengeShadowCombatPos + results2.length() + 1);
                            this.game.challengeShadowCombatPos += results.length();
                        } else {
                            results = (String.valueOf(this.game.challengeShadowCombat.substring(this.game.challengeShadowCombatPos + 1)) + " " + results2).substring(0, results2.length());
                            this.game.challengeShadowCombatPos = -1;
                        }
                    } else if (this.game.challengeFreeCombatPos <= -1 || !isFPplayer) {
                        results = chatCommand.substring(chatCommand.lastIndexOf(Messages.getString("Interpreter.69")) + Messages.getString("Interpreter.69").length());
                        chat = chatCommand.substring(0, chatCommand.indexOf(results));
                    } else {
                        String results3 = chatCommand.substring(chatCommand.lastIndexOf(Messages.getString("Interpreter.69")) + Messages.getString("Interpreter.69").length());
                        chat = chatCommand.substring(0, chatCommand.indexOf(results3));
                        if (this.game.challengeFreeCombat.length() - this.game.challengeFreeCombatPos >= results3.length()) {
                            results = this.game.challengeFreeCombat.substring(this.game.challengeFreeCombatPos + 1, this.game.challengeFreeCombatPos + results3.length() + 1);
                            this.game.challengeFreeCombatPos += results.length();
                        } else {
                            results = (String.valueOf(this.game.challengeFreeCombat.substring(this.game.challengeFreeCombatPos + 1)) + " " + results3).substring(0, results3.length());
                            this.game.challengeFreeCombatPos = -1;
                        }
                    }
                    chat2 = String.valueOf(chat) + Game.SortDice(results);
                    Game.RecordDice(results, isFPplayer);
                } else {
                    JOptionPane.showMessageDialog(Game.win, "This command is no longer supported, use a secure version of the client please!");
                    return;
                }
            }
            if (chatCommand.startsWith("<game> ") && chatCommand.indexOf(Messages.getString("Talker.50")) > 0) {
                String tmp = chatCommand.substring(chatCommand.indexOf(Messages.getString("Talker.51")) + Messages.getString("Talker.52").length());
                if (chatCommand.substring(7, chatCommand.indexOf(Messages.getString("Talker.50"))).equals(this.game.shadowname)) {
                    this.game.shadowname = tmp.substring(0, tmp.lastIndexOf(46));
                }
            }
            if (chatCommand.trim().startsWith(Messages.getString("Interpreter.17"))) {
                this.game.synchronizing = false;
                this.game.redoTitle();
            }
            if (chatCommand.trim().startsWith("<replay> " + Messages.getString("Interpreter.17"))) {
                this.game.setTurn(this.game.turn);
                this.game.redoTitle();
                return;
            }
            if (this.fromFile) {
                this.game.controls.chat.write("%" + chat2);
            } else {
                this.game.controls.chat.write(chat2);
            }
            String command = chatCommand.substring(chatCommand.indexOf(">") + 2);
            if (command.startsWith(this.fromFile ? Messages.getString("Interpreter.71") : Messages.getString("Interpreter.72"))) {
                if (this.fromFile) {
                    turnNumber = Integer.parseInt(command.substring(command.indexOf(Messages.getString("Interpreter.71")) + Messages.getString("Interpreter.71").length()).trim());
                } else {
                    turnNumber = Integer.parseInt(command.substring(command.indexOf(Messages.getString("Interpreter.72")) + Messages.getString("Interpreter.72").length()).trim());
                }
                this.game.setTurn(turnNumber);
            }
        } catch (Throwable e) {
            System.out.println(String.valueOf(Messages.getString("Interpreter.73")) + e);
        }
    }

    public void exMove(String inputCmd, String name) {
        int i = 0;
        boolean abort = false;
        Area area = new Area("temp");
        String cmd = inputCmd;
        while (!abort) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            String token = cmd.substring(0, nextspace);
            if (!token.equals(Messages.getString("Interpreter.76"))) {
                try {
                    if (token.equals("reveal")) {
                        String cmd2 = cmd.substring(nextspace + 1);
                        int nextspace2 = cmd2.indexOf(" ");
                        GamePiece p = this.game.bits[Integer.parseInt(cmd2.substring(0, nextspace2))];
                        String cmd3 = cmd2.substring(nextspace2 + 1);
                        int nextspace3 = cmd3.indexOf(" ");
                        String hash = cmd3.substring(0, nextspace3);
                        cmd = cmd3.substring(nextspace3 + 1);
                        nextspace = cmd.indexOf(" ");
                        this.game.RevealCard((Card) p, Integer.parseInt(cmd.substring(0, nextspace)), hash);
                        area.addPiece(p);
                        unChoose(p);
                    } else {
                        GamePiece p2 = this.game.bits[Integer.parseInt(token)];
                        area.addPiece(p2);
                        unChoose(p2);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                abort = true;
            }
            cmd = cmd.substring(nextspace + 1);
        }
        int nextspace4 = cmd.indexOf(Messages.getString("Interpreter.77"));
        String fromName = cmd.substring(0, nextspace4);
        String cmd4 = cmd.substring(Messages.getString("Interpreter.77").length() + nextspace4 + 1);
        int nextspace5 = cmd4.lastIndexOf(" ");
        if (nextspace5 == -1) {
            nextspace5 = cmd4.length();
        }
        String toName = cmd4.substring(0, nextspace5);
        if (toName.equals(Messages.getString("Game.1997"))) {
            for (int t = 0; t < area.numPieces(); t++) {
                if (area.pieces().get(t) instanceof ShadowActionDie) {
                    ShadowActionDie.allocations++;
                }
            }
        }
        if (Game.prefs.flashOptional > -1 && area.contentnames().indexOf(Messages.getString("Game.1852")) > -1) {
            this.preHuntTimer = true;
        }
        if (Game.prefs.flashMandatory > -1 && area.contentnames().indexOf(Messages.getString("UnitBalrog.0")) > -1 && fromName.equals(Messages.getString("Game.2040")) && toName.equals(Messages.getString("Game.1917")) && !this.balrogBrighterTimer) {
            this.balrogBrighterTimer = true;
        }
        if (Game.prefs.flashMandatory > -1 && area.numPieces() == 1 && (area.get(0) instanceof UnitWitchKing) && fromName.equals(Messages.getString("Game.2040")) && !this.witchkingBrighterTimer) {
            this.witchkingBrighterTimer = true;
        }
        if ((Game.varianttype.startsWith("base") || Game.varianttype.startsWith("base2") || Game.varianttype.startsWith("expansion2")) && fromName.equals(Messages.getString("Game.1997")) && toName.equals(Messages.getString("Game.2042"))) {
            boolean correctdice = false;
            int t7 = 0;
            while (true) {
                if (t7 < area.numPieces()) {
                    if (!(area.get(t7) instanceof FreeActionDie)) {
                        if (!(area.get(t7) instanceof NenyaDice) || ((NenyaDice) area.get(t7)).charState() == 'E') {
                            if (!(area.get(t7) instanceof NaryaDice) || ((NaryaDice) area.get(t7)).charState() == 'E') {
                                if ((area.get(t7) instanceof VilyaDice) && ((VilyaDice) area.get(t7)).charState() != 'E') {
                                    correctdice = true;
                                    break;
                                }
                                t7++;
                            } else {
                                correctdice = true;
                                break;
                            }
                        } else {
                            correctdice = true;
                            break;
                        }
                    } else {
                        correctdice = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (correctdice) {
                this.game.FSPreminder = new JLabel();
                this.game.FSPreminder.setBounds((int) ((((double) (Game.prefs.zoom * 66)) / 100.0d) - 10.0d), (int) ((((double) (Game.prefs.zoom * 575)) / 100.0d) - 10.0d), 100, 100);
                this.game.FSPreminder.setIcon(new ImageIcon(Messages.getLanguageLocation("images/units/Fellowship-transparent.png")));
                this.game.boardlabel.add(this.game.FSPreminder);
                this.game.FSPreminder.repaint();
            }
        }
        Area movingTo = null;
        Area movingFrom = null;
        int i2 = 0;
        boolean found = false;
        while (i2 < Game.areas.length && !fromName.equals(Messages.removeKeyString(Game.areas[i2].name()))) {
            i2++;
            found = true;
        }
        if (i2 < Game.areas.length && found) {
            movingFrom = Game.areas[i2];
        }
        int i3 = 0;
        found = false;
        while (i3 < Game.areas.length && !toName.equals(Messages.removeKeyString(Game.areas[i3].name()))) {
            i3++;
        }
        if (i3 < Game.areas.length) {
            movingTo = Game.areas[i3];
        }
        if (movingFrom == null && fromName.length() >= 8) {
            if (fromName.substring(fromName.length() - 8).equals(Messages.getString("Talker.58"))) {
                String prefix = fromName.substring(0, fromName.length() - 9);
                i2 = 0;
                found = false;
                while (i2 < Game.areas.length && !prefix.equals(Messages.removeKeyString(Game.areas[i2].name()))) {
                    i2++;
                    found = true;
                }
                if (i2 < Game.areas.length) {
                    movingFrom = Game.areas[i2];
                }
                if ( !found ) {
                    System.out.println("Moving from " + fromName + " to " + toName + " not found!");
                }
            }
        }
        if (movingTo != null) {
            this.game.moveGroup(area, movingTo);
            if (name.equals("$" + Game.prefs.nick) || this.fromFile) {
                if (movingFrom instanceof CardBuffet) {
                    //System.out.println(i2 + ":" + movingFrom );
                    if ( i2 < Game.areas.length ) {
                        ((CardBuffet) Game.areas[i2]).highlightMe();
                    } else {
                        System.out.println("Moving from " + fromName + " to " + toName + " out of bounds!");
                    }
                } else {
                    this.game.highlightArea(movingTo, true);
                }
            }
        } else {
            System.out.println("ERROR movingTO unknown location! Name: " + toName);
        }
        String unitsmoved = getUnitGroupString(area, movingFrom == null || movingFrom.GetViewFronts() || movingTo.GetViewFronts());
        if (area.contentnames().indexOf(Messages.getString("Interpreter.83")) > -1 && !this.game.Gandalfplayed) {
            this.game.playSound("soundpack/late.wav");
            this.game.Gandalfplayed = true;
        }
        if (area.contentnames().indexOf(Messages.getString("Interpreter.55")) > -1 && !this.game.TreeBeardplayed) {
            this.game.playSound("soundpack/q1.sp1");
            this.game.TreeBeardplayed = true;
        }
        // Write move to chat
        // Note: duplicates can occur in network play, but deduplication happens at chat level
        this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.105") + unitsmoved + Messages.getString("Interpreter.106") + fromName + Messages.getString("Interpreter.107") + toName + ".");
        if (area.contentnames().indexOf(Messages.getString("Interpreter.109")) > -1 && Game.boardtype.equals("wotr")) {
            if (toName.equals(Messages.getString("Interpreter.66")) || toName.equals(Messages.getString("Game.22"))) {
                Game.SarumanInIsengard = true;
            } else {
                Game.SarumanInIsengard = false;
            }
            this.game.refreshBoard();
        }
        if ((!(!this.game.fHaveRecovered) || !toName.equals(Messages.getString("Interpreter.112"))) || (!fromName.equals(Messages.getString("Interpreter.113")) && !fromName.equals(Messages.getString("Game.1997")))) {
            if (((!this.game.sHaveRecovered) && toName.equals(Messages.getString("Interpreter.114"))) && (fromName.equals(Messages.getString("Interpreter.115")) || fromName.equals(Messages.getString("Game.1997")))) {
                this.game.sHaveRecovered = true;
                if (this.game.ShadowWK != null) {
                    this.game.boardlabel.remove(this.game.ShadowWK);
                    this.game.ShadowWK = null;
                }
                if (this.game.fHaveRecovered && this.game.sHaveRecovered) {
                    this.game.setTurn(this.game.turn + 1);
                }
            }
        } else {
            this.game.fHaveRecovered = true;
            if (this.game.fHaveRecovered && this.game.sHaveRecovered) {
                this.game.setTurn(this.game.turn + 1);
            }
        }
        if (this.game.fHaveRecovered && Game.varianttype.contains("L")) {
            Area temp = Game.areas[178];
            for (int t8 = 0; t8 < temp.numPieces(); t8++) {
                if ((temp.get(t8) instanceof NaryaDice) && (this.game.FSPreminder == null || !GandalfGuiding())) {
                    this.dice1flashpos = t8;
                    this.diceTimer = true;
                }
                if ((temp.get(t8) instanceof VilyaDice) && (!ElrondInRivendell() || ((((VilyaDice) temp.get(t8)).charState() == 'E' || ((VilyaDice) temp.get(t8)).charState() == 'A') && GTWinPlay()))) {
                    this.dice2flashpos = t8;
                    this.diceTimer = true;
                }
                if ((temp.get(t8) instanceof NenyaDice) && (!GaladrielInLorien() || ((((NenyaDice) temp.get(t8)).charState() == 'E' || ((NenyaDice) temp.get(t8)).charState() == 'C') && GTWinPlay()))) {
                    this.dice3flashpos = t8;
                    this.diceTimer = true;
                }
            }
        }
        if (this.game.sHaveRecovered && Game.varianttype.contains("L")) {
            Area temp2 = Game.areas[179];
            for (int t9 = 0; t9 < temp2.numPieces(); t9++) {
                if (temp2.get(t9) instanceof BalrogDice) {
                    if (!BalrogInPlay()) {
                        this.dice4flashpos = t9;
                    }
                    if (WKInPlay() && (((BalrogDice) temp2.get(t9)).GetState() == 4 || ((BalrogDice) temp2.get(t9)).GetState() == 0)) {
                        this.dice4flashpos = t9;
                    }
                }
                if ((temp2.get(t9) instanceof GothmogDice) && (!GothmogInPlay() || ((((GothmogDice) temp2.get(t9)).GetState() == 3 || ((GothmogDice) temp2.get(t9)).GetState() == 4) && WKInPlay()))) {
                    this.dice5flashpos = t9;
                }
            }
        }
    }

    private boolean GothmogInPlay() {
        int t = 0;
        while (t < this.game.bits.length && !(this.game.bits[t] instanceof UnitGothmog)) {
            t++;
        }
        if (this.game.bits[t].currentLocation() == Game.areas[177] || this.game.bits[t].currentLocation() == Game.areas[176]) {
            return false;
        }
        return true;
    }

    private boolean WKInPlay() {
        int t = 0;
        while (t < this.game.bits.length && !(this.game.bits[t] instanceof UnitCotR)) {
            t++;
        }
        if (this.game.bits[t].currentLocation() == Game.areas[177] || this.game.bits[t].currentLocation() == Game.areas[176]) {
            int t2 = 0;
            while (t2 < this.game.bits.length && !(this.game.bits[t2] instanceof UnitWitchKing)) {
                t2++;
            }
            if (this.game.bits[t2].currentLocation() == Game.areas[177] || this.game.bits[t2].currentLocation() == Game.areas[176]) {
                return false;
            }
        }
        return true;
    }

    private boolean BalrogInPlay() {
        int t = 0;
        while (t < this.game.bits.length && !(this.game.bits[t] instanceof UnitBalrog)) {
            t++;
        }
        if (this.game.bits[t].currentLocation() == Game.areas[177] || this.game.bits[t].currentLocation() == Game.areas[176]) {
            return false;
        }
        return true;
    }

    private boolean GTWinPlay() {
        int t = 0;
        while (t < this.game.bits.length && !(this.game.bits[t] instanceof UnitGandalf)) {
            t++;
        }
        if (((UnitGandalf) this.game.bits[t]).currentState() || ((UnitGandalf) this.game.bits[t]).currentLocation() == Game.areas[175]) {
            return false;
        }
        return true;
    }

    private static boolean GaladrielInLorien() {
        Area temp = Game.areas[37];
        for (int t = 0; t < temp.numPieces(); t++) {
            if (temp.get(t) instanceof UnitGaladriel) {
                return true;
            }
        }
        return false;
    }

    private static boolean ElrondInRivendell() {
        Area temp = Game.areas[27];
        for (int t = 0; t < temp.numPieces(); t++) {
            if (temp.get(t) instanceof UnitElrond) {
                return true;
            }
        }
        return false;
    }

    private static boolean GandalfGuiding() {
        Area temp = Game.areas[116];
        for (int t = 0; t < temp.numPieces(); t++) {
            if (temp.get(t) instanceof UnitGandalf) {
                return true;
            }
        }
        return false;
    }

    public static String getUnitGroupString(Area list, boolean declareCards) {
        if (Game.prefs.shortGroupDesc) {
            return getUnitGroupStringShort(list, declareCards);
        }
        String lastPiece = null;
        String currentPiece = null;
        int noPieces = 0;
        if (list.numPieces() == 0) {
            return Messages.getString("Interpreter.116");
        }
        if (list.numPieces() != 1) {
            String unitsString = Messages.getString("Interpreter.120");
            Iterator<GamePiece> it = list.pieces().iterator();
            while (it.hasNext()) {
                GamePiece x = it.next();
                if ((x instanceof Card) && declareCards) {
                    currentPiece = ((Card) x).name();
                } else if (!(x instanceof GenericCard) || !declareCards) {
                    currentPiece = x.type();
                } else {
                    currentPiece = ((GenericCard) x).name();
                }
                if (currentPiece.equals(lastPiece) || noPieces == 0) {
                    noPieces++;
                } else {
                    if (noPieces > 1) {
                        unitsString = String.valueOf(unitsString) + "[" + noPieces + "]";
                    }
                    unitsString = String.valueOf(unitsString) + lastPiece + ", ";
                    noPieces = 1;
                }
                lastPiece = currentPiece;
            }
            if (noPieces > 1) {
                unitsString = String.valueOf(unitsString) + "[" + noPieces + "]";
            }
            String unitsString2 = String.valueOf(unitsString) + currentPiece + ", ";
            return String.valueOf(unitsString2.substring(0, unitsString2.length() - 2)) + ")";
        } else if ((list.get(0) instanceof Card) && declareCards) {
            return ((Card) list.get(0)).name();
        } else {
            if (!(list.get(0) instanceof GenericCard) || !declareCards) {
                return list.get(0).type();
            }
            return ((GenericCard) list.get(0)).name();
        }
    }

    public static String getUnitGroupStringShort(Area list, boolean declareCards) {
        String unitsString;
        if (list.numPieces() == 0) {
            return Messages.getString("Interpreter.116");
        }
        if (list.numPieces() == 1) {
            if ((list.get(0) instanceof Card) && declareCards) {
                return ((Card) list.get(0)).name();
            }
            if (!(list.get(0) instanceof GenericCard) || !declareCards) {
                return list.get(0).type();
            }
            return ((GenericCard) list.get(0)).name();
        } else if (list.getAreaPic().chatText.length() > 0) {
            return String.valueOf(Messages.getString("Interpreter.117")) + list.getAreaPic().chatText;
        } else {
            if (list.numPieces() > 4) {
                return String.valueOf(Messages.getString("Interpreter.118")) + list.numPieces() + ")";
            }
            String unitsString2 = Messages.getString("Interpreter.120");
            Iterator<GamePiece> it = list.pieces().iterator();
            while (it.hasNext()) {
                GamePiece x = it.next();
                if (!(x instanceof Card) || !declareCards) {
                    unitsString = String.valueOf(unitsString2) + x.type();
                } else {
                    unitsString = String.valueOf(unitsString2) + ((Card) x).name();
                }
                unitsString2 = String.valueOf(unitsString) + ", ";
            }
            return String.valueOf(unitsString2.substring(0, unitsString2.length() - 2)) + ")";
        }
    }

    public void exAllocate(String inputCmd, String name) {
        Area list = new Area("temp");
        String cmd = inputCmd;
        while (true) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            GamePiece g = this.game.bits[Integer.parseInt(cmd.substring(0, nextspace))];
            if (g instanceof ShadowActionDie) {
                list.addPiece(g);
            }
            cmd = cmd.substring(nextspace + 1);
        }
        boolean single = true;
        String chatStr = null;
        if (Game.boardtype.equals("wotr")) {
            if (list.numPieces() == 1) {
                ((ShadowActionDie) list.get(0)).SetState(15);
            } else {
                single = false;
                ArrayList<GamePiece> al = list.pieces();
                int i = 0;
                while (i < al.size()) {
                    if (((ActionDie) al.get(i)).GetState() != 15) {
                        list.removePiece(al.get(i));
                        i--;
                    }
                    i++;
                }
            }
            if (list.numPieces() > 0) {
                ShadowActionDie.allocations += list.numPieces();
                this.game.moveGroup(list, Game.areas[114]);
                // Only remove from selection if piece is actually there (not during replay)
                for (int i2 = 0; i2 < list.numPieces(); i2++) {
                    if (this.game.selection.pieces().contains(list.get(i2))) {
                        this.game.selection.removePiece(list.get(i2));
                    }
                }
            }
            if (single) {
                chatStr = String.valueOf(name) + Messages.getString("Interpreter.126");
                if (this.game.FSPreminder != null) {
                    this.game.boardlabel.remove(this.game.FSPreminder);
                    this.game.FSPreminder = null;
                }
            } else {
                chatStr = list.numPieces() == 1 ? String.valueOf(name) + Messages.getString("Interpreter.127") : String.valueOf(name) + Messages.getString("Interpreter.128") + list.numPieces() + Messages.getString("Interpreter.129");
            }
        }
        this.game.controls.chat.write(chatStr);
    }

    public void exDraw(String inputCmd, String name) {
        String unitsmoved;
        boolean abort = false;
        Area list = new Area("temp");
        String cmd = inputCmd;
        boolean GenericsOnly = true;
        while (!abort) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            String token = cmd.substring(0, nextspace);
            if (!token.equals(Messages.getString("Interpreter.138"))) {
                GamePiece p = this.game.bits[Integer.parseInt(token)];
                list.addPiece(p);
                unChoose(p);
                if (!(p instanceof GenericCard)) {
                    GenericsOnly = false;
                }
            } else {
                abort = true;
            }
            cmd = cmd.substring(nextspace + 1);
        }
        if (this._safeMode || !this.game.talker.connected || this.fromFile || GenericsOnly) {
            createHistory();
            String cmd2 = cmd.substring(Messages.getString("Interpreter.139").length() + cmd.indexOf(Messages.getString("Interpreter.139")) + 1);
            int nextspace2 = cmd2.lastIndexOf(" ");
            if (nextspace2 == -1) {
                nextspace2 = cmd2.length();
            }
            String toName = cmd2.substring(0, nextspace2);
            int i = 0;
            while (i < Game.areas.length && !toName.equals(Messages.removeKeyString(Game.areas[i].name()))) {
                i++;
            }
            if (i < Game.areas.length) {
                this.game.moveGroup(list, Game.areas[i]);
                
                // Refresh the area panel to show the new card
                if (Game.areas[i] instanceof CardBuffet) {
                    CardBuffet buffet = (CardBuffet) Game.areas[i];
                    if (buffet.visual != null) {
                        buffet.visual.revalidate();
                        buffet.visual.repaint();
                    }
                }
                
                if (name.equals("$" + Game.prefs.nick) || this.fromFile) {
                    if (Game.areas[i] instanceof CardBuffet) {
                        ((CardBuffet) Game.areas[i]).highlightMeWithSpecifiedCard(list.get(0));
                    } else {
                        this.game.highlightArea(Game.areas[i], false);
                    }
                }
            }
            if (list.numPieces() > 1) {
                unitsmoved = String.valueOf(Messages.getString("Interpreter.142")) + list.getAreaPic().text;
            } else if (list.numPieces() == 1) {
                unitsmoved = list.get(0).type();
            } else {
                unitsmoved = Messages.getString("Interpreter.143");
            }
            if (Game.prefs.flashOptional > -1 && list.numPieces() == 1 && (list.get(0) instanceof HuntTile) && !this.cardBrighterTimer) {
                this.cardBrighterTimer = true;
            }
            String chatStr = String.valueOf(name) + Messages.getString("Interpreter.144") + unitsmoved + ".";
            if (toName.equals(Messages.getString("Interpreter.146")) && unitsmoved.equals(Messages.getString("Interpreter.147"))) {
                chatStr = String.valueOf(name) + Messages.getString("Interpreter.148");
            }
            if (toName.equals(Messages.getString("Interpreter.146")) && unitsmoved.equals("Free Story Card")) {
                chatStr = String.valueOf(name) + Messages.getString("Interpreter.87");
            }
            if (unitsmoved.equals(Messages.getString("Interpreter.68"))) {
                if (toName.equals(Game.areas[180].name())) {
                    ((GenericCard) list.get(0)).setnation(5);
                }
                if (toName.equals(Game.areas[181].name())) {
                    ((GenericCard) list.get(0)).setnation(-5);
                }
            }
            this.game.controls.chat.write(chatStr);
            return;
        }
        JOptionPane.showMessageDialog(Game.win, "This command is no longer supported, use a secure version of the client please!");
    }

    public void exKill(String inputCmd, String name) {
        String unitsmoved;
        String chatStr;
        boolean abort = false;
        Area list = new Area("temp");
        String cmd = inputCmd;
        this.game.stopBrightObjects();
        while (!abort) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            String token = cmd.substring(0, nextspace);
            if (!token.equals(Messages.getString("Interpreter.152"))) {
                GamePiece p = this.game.bits[Integer.parseInt(token)];
                list.addPiece(p);
                unChoose(p);
            } else {
                abort = true;
            }
            cmd = cmd.substring(nextspace + 1);
        }
        int nextspace2 = cmd.lastIndexOf(Messages.getString("Interpreter.153"));
        if (nextspace2 == -1) {
            nextspace2 = cmd.length();
        }
        String inName = cmd.substring(0, nextspace2);
        Area areaIn = null;
        int i = 0;
        while (i < Game.areas.length && !inName.equals(Messages.removeKeyString(Game.areas[i].name()))) {
            i++;
        }
        if (i < Game.areas.length) {
            areaIn = Game.areas[i];
        }
        if (list.contentnames().indexOf(Messages.getString("Interpreter.154")) > -1 && Game.boardtype.equals("wotr")) {
            Game.SarumanInIsengard = false;
            this.game.refreshBoard();
        }
        this.game.killGroup(list);
        if (list.numPieces() >= 1) {
            unitsmoved = getUnitGroupString(list, areaIn.GetViewFronts());
        } else {
            unitsmoved = Messages.getString("Interpreter.156");
        }
        if (inName.equals("Spiel")) {
            chatStr = String.valueOf(name) + Messages.getString("Interpreter.158") + unitsmoved + Messages.getString("Interpreter.159") + inName + ".";
        } else {
            chatStr = String.valueOf(name) + Messages.getString("Interpreter.158") + unitsmoved + Messages.getString("Interpreter.188") + inName + ".";
        }
        this.game.controls.chat.write(chatStr);
    }

    public void exRoll(String inputCmd, String name) {
        boolean abort = false;
        ArrayList<GamePiece> list = new ArrayList<>();
        this.game.fHaveRecovered = false;
        this.game.sHaveRecovered = false;
        String cmd = inputCmd;
        this.game.playSound("soundpack/dice.wav");
        while (!abort) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            String token = cmd.substring(0, nextspace);
            if (!token.equals(Messages.getString("Interpreter.163"))) {
                list.add(this.game.bits[Integer.parseInt(token)]);
            } else {
                abort = true;
            }
            cmd = cmd.substring(nextspace + 1);
        }
        ArrayList<Integer> l = new ArrayList<>();
        boolean abort2 = false;
        while (!abort2) {
            int nextspace2 = cmd.indexOf(" ");
            if (nextspace2 == -1) {
                break;
            }
            String token2 = cmd.substring(0, nextspace2);
            if (!token2.equals("")) {
                l.add(new Integer(Integer.parseInt(token2)));
            } else {
                abort2 = true;
            }
            cmd = cmd.substring(nextspace2 + 1);
        }
        Iterator<Integer> it = l.iterator();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ActionDie) {
                ((ActionDie) list.get(i)).SetStateWithStats(it.next().intValue());
                list.get(i).currentLocation().updatePic = true;
            }
        }
        String letters = "";
        for (int i2 = 0; i2 < l.size(); i2++) {
            letters = String.valueOf(letters) + ((ActionDie) list.get(i2)).getChar(l.get(i2).intValue()) + " ";
        }
        this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.169") + letters + ".");
        try {
            if (!list.isEmpty()) {
                if (list.get(0).currentLocation == Game.areas[179]) {
                    this.game.sHaveRolled = true;
                }
                if (list.get(0).currentLocation == Game.areas[178]) {
                    this.game.fHaveRolled = true;
                }
            }
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

    public void exFlip(String inputCmd, String name) {
        String flippedBits;
        String location = null;
        ArrayList<GamePiece> list = new ArrayList<>();
        Area area = new Area("temp");
        Area dVar2 = null;
        String cmd = inputCmd;
        while (true) {
            int nextspace = cmd.indexOf(" ");
            if (nextspace == -1) {
                break;
            }
            list.add(this.game.bits[Integer.parseInt(cmd.substring(0, nextspace))]);
            cmd = cmd.substring(nextspace + 1);
        }
        String flippedBits2 = "";
        if ( list.size() == 0 ) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Interpreter.196"));
            return;
        }
        int i = 0;
        while (i < list.size()) {
            if (location == null) {
                location = list.get(i).currentLocation().name();
            }
            if (Game.varianttype.startsWith("expansion2") && (list.get(i) instanceof UnitWitchKing)) {
                execute("$base silent ");
                Area swaplocation = list.get(i).currentLocation();
                int i1 = 0;
                while (i1 < this.game.bits.length && !(this.game.bits[i1] instanceof UnitWitchKing)) {
                    i1++;
                }
                execute("<auto><~Controls.281~>" + i1 + " <~Controls.282~>" + swaplocation + "<~Controls.283~><~Game.2040~>");
                int i12 = 0;
                while (i12 < this.game.bits.length && !(this.game.bits[i12] instanceof UnitCotR)) {
                    i12++;
                }
                execute("<auto><~Scribe.141~>" + i12 + " <~Scribe.142~><~Game.2040~><~Scribe.143~>" + swaplocation + "<~Scribe.144~>");
                execute("$base noisy ");
                this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.477"));
                return;
            } else if (!Game.varianttype.startsWith("expansion2") || !(list.get(i) instanceof UnitCotR)) {
                if (list.get(i) instanceof Flippable) {
                    ((Flippable) list.get(i)).flip();
                    list.get(i).currentLocation().updatePic = true;
                    list.get(i).currentLocation().updateChit = true;
                    flippedBits2 = String.valueOf(flippedBits2) + ", " + list.get(i).type();
                    if (!(!(list.get(i) instanceof TwoChit) || ((TwoChit) list.get(i)).revealedtype() == "" || ((TwoChit) list.get(i)).revealedtype() == null)) {
                        flippedBits2 = String.valueOf(flippedBits2) + " (" + ((TwoChit) list.get(i)).revealedtype() + ")";
                    }
                    if (list.get(i).type().equals(Messages.getString("Interpreter.70"))) {
                        ((TwoChit) list.get(i)).setType(Messages.getString("Interpreter.74"));
                        ((TwoChit) list.get(i)).setNation(1);
                    }
                    if (list.get(i).type().equals(Messages.getString("Interpreter.75"))) {
                        ((TwoChit) list.get(i)).setType(Messages.getString("Interpreter.78"));
                        ((TwoChit) list.get(i)).setNation(-12);
                    }
                    if (Game.boardtype.equals("wotr") && Game.varianttype.startsWith("expansion") && (list.get(i) instanceof UnitSmeagol)) {
                        this.game.playSound("soundpack/gollum.sp1");
                        for (int i11 = 0; i11 <= Game.areas.length - 1; i11++) {
                            try {
                                int totalpieces = Game.areas[i11].numPieces();
                                for (int p = 0; p <= totalpieces; p++) {
                                    GamePiece gp = Game.areas[i11].get(p);
                                    if ((gp instanceof HuntTile) && gp.type().equals(Messages.getString("Game.1832"))) {
                                        area.addPiece(gp);
                                    }
                                }
                                if (area.numPieces() > 0) {
                                    this.game.moveGroup(area, Game.areas[183]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (Game.boardtype.equals("wotr") && (list.get(i) instanceof UnitFellowship)) {
                        int p2 = 1;
                        while (true) {
                            if (p2 <= this.game.bits.length - 1) {
                                GamePiece gp2 = this.game.bits[p2];
                                if ((gp2 instanceof TwoChit) && gp2.type().equals(Messages.getString("Game.1852"))) {
                                    ((Flippable) gp2).flip();
                                    break;
                                }
                                p2++;
                            } else {
                                break;
                            }
                        }
                        dVar2 = resetFSP();
                    }
                    if (Game.boardtype.equals("wotr") && (list.get(i) instanceof TwoChit) && ((TwoChit) list.get(i)).type().equals(Messages.getString("Game.1852"))) {
                        int p3 = 1;
                        while (true) {
                            if (p3 > this.game.bits.length - 1) {
                                break;
                            }
                            GamePiece gp3 = this.game.bits[p3];
                            if (gp3 instanceof UnitFellowship) {
                                ((Flippable) gp3).flip();
                                break;
                            }
                            p3++;
                        }
                        resetFSP();
                    }
                }
                i++;
            } else {
                execute("$base silent ");
                Area swaplocation2 = list.get(i).currentLocation();
                int i13 = 0;
                while (i13 < this.game.bits.length && !(this.game.bits[i13] instanceof UnitCotR)) {
                    i13++;
                }
                execute("<auto><~Controls.281~>" + i13 + " <~Controls.282~>" + swaplocation2 + "<~Controls.283~><~Game.2040~>");
                int i14 = 0;
                while (i14 < this.game.bits.length && !(this.game.bits[i14] instanceof UnitWitchKing)) {
                    i14++;
                }
                execute("<auto><~Scribe.141~>" + i14 + " <~Scribe.142~><~Game.2040~><~Scribe.143~>" + swaplocation2 + "<~Scribe.144~>");
                execute("$base noisy ");
                this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.477"));
                return;
            }
        }
        if (flippedBits2.equals("")) {
            flippedBits = Messages.getString("Interpreter.186");
        } else {
            flippedBits = flippedBits2.substring(1);
        }
        //this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.187") + flippedBits + Messages.getString("Interpreter.188") + Messages.removeKeyString(location) + ".");
        String str6 = String.valueOf(name) + Messages.getString("Interpreter.187") + flippedBits + Messages.getString("Interpreter.188") + Messages.removeKeyString(location) + ".";
        this.game.controls.chat.write(dVar2 != null ? String.valueOf(str6) + Messages.getString("Interpreter.197") + Messages.removeKeyString(dVar2.name()) + "." : str6);
    }

    private Area resetFSP() {
        int p = 1;
        while (p <= this.game.bits.length - 1) {
            GamePiece gp = this.game.bits[p];
            if (!(gp instanceof TwoChit) || !((TwoChit) gp).type().equals(Messages.getString("Game.1852"))) {
                p++;
            } else if (!gp.currentLocation.equals(Messages.getString("Game.2014"))) {
                Area dVar = gp.currentLocation;
                gp.moveTo(Game.areas[131]);
                return dVar;
            } else {
                return null;
            }
        }
        return null;
    }

    public void exChoose(String cmd, String inputName) {
        String chatStr = "";
        String name = inputName;
        if (Game.boardtype.equals("wotr")) {
            chatStr = String.valueOf(name) + Messages.getString("Interpreter.192");
        }
        if (Game.isWOME.booleanValue() || Game.varianttype.startsWith("base2") || Game.varianttype.startsWith("expansion2")) {
            chatStr = String.valueOf(name) + Messages.getString("Interpreter.192") + "[" + this.game.bits[Integer.parseInt(cmd.substring(0, cmd.indexOf(" ")))].type() + "]";
        }
        this.game.controls.chat.write(chatStr);
        String name2 = name.substring(1);
        if (this.game.chosen1player == null || this.game.chosen1player.equals(name2)) {
            this.game.chosen1player = name2;
            this.game.chosen1 = this.game.bits[Integer.parseInt(cmd.substring(0, cmd.indexOf(" ")))];
        } else {
            this.game.chosen2player = name2;
            this.game.chosen2 = this.game.bits[Integer.parseInt(cmd.substring(0, cmd.indexOf(" ")))];
        }
        if (!this.fromFile && this.game.chosen1 != null && this.game.chosen2 != null) {
            this.game.controls.cbh.actionPerformed(new ActionEvent(this, DEFAULT_REPLAY_SPEED, "playchosen"));
        }
    }

    public void exUnchoose(String inputName) {
        String chatStr = String.valueOf(inputName) + Messages.getString("Interpreter.25");
        String name = inputName.substring(1);
        if (this.game.chosen1player != null && this.game.chosen1player.equals(name)) {
            if (this.game.chosen1 instanceof Card) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.23");
            }
            if (this.game.chosen1 instanceof GenericCard) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.23");
            }
            if (this.game.chosen1 instanceof HuntTile) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.108");
            }
            this.game.chosen1player = null;
            this.game.chosen1 = null;
        } else if (this.game.chosen1player != null) {
            if (this.game.chosen2 instanceof Card) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.110");
            }
            if (this.game.chosen2 instanceof GenericCard) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.110");
            }
            if (this.game.chosen2 instanceof HuntTile) {
                chatStr = String.valueOf(chatStr) + Messages.getString("Interpreter.111");
            }
            this.game.chosen2player = null;
            this.game.chosen2 = null;
        }
        if (this.game.chosen1player != null || this.game.chosen2player != null) {
            this.game.controls.chat.write(chatStr);
        }
    }

    private void SetFPSecurity(String securityTextInput) {
        String securityText = securityTextInput;
        String[] FcardsHashes;
        String[] FcardsSalts;

        int space = securityText.indexOf(32);
        String passwordHash = securityText.substring(0, space);
        securityText = securityText.substring(space + 1);

        space = securityText.indexOf(32);
        String passwordSalt = securityText.substring(0, space);
        securityText = securityText.substring(space + 1);

        space = securityText.indexOf(32);
        int NcharacterCards = Integer.parseInt(securityText.substring(0, space));
        securityText = securityText.substring(space + 1);
        String[] CcardsHashes = new String[NcharacterCards];
        String[] CcardsSalts = new String[NcharacterCards];
        for (int i = 0; i < NcharacterCards; i++) {
            space = securityText.indexOf(32);
            CcardsHashes[i] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);

            space = securityText.indexOf(32);
            CcardsSalts[i] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        space = securityText.indexOf(32);
        int NstrategyCards = Integer.parseInt(securityText.substring(0, space));
        securityText = String.valueOf(securityText.substring(space + 1)) + " ";
        String[] ScardsHashes = new String[NstrategyCards];
        String[] ScardsSalts = new String[NstrategyCards];
        for (int i2 = 0; i2 < NstrategyCards; i2++) {
            space = securityText.indexOf(32);
            ScardsHashes[i2] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
            
            space = securityText.indexOf(32);
            ScardsSalts[i2] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        if (Game.isWOME.booleanValue()) {
            space = securityText.indexOf(32);
            int NfactionCards = Integer.parseInt(securityText.substring(0, space));
            securityText = securityText.substring(space + 1);
            FcardsHashes = new String[NfactionCards];
            FcardsSalts = new String[NfactionCards];
            for (int i3 = 0; i3 < NfactionCards; i3++) {
                space = securityText.indexOf(32);
                FcardsHashes[i3] = securityText.substring(0, space);
                securityText = securityText.substring(space + 1);
                space = securityText.indexOf(32);
                FcardsSalts[i3] = securityText.substring(0, space);
                securityText = securityText.substring(space + 1);
            }
        } else {
            FcardsHashes = new String[0];
            FcardsSalts = new String[0];
        }
        space = securityText.indexOf(32);
        int NgenericCards = Integer.parseInt(securityText.substring(0, space));
        securityText = String.valueOf(securityText.substring(space + 1)) + " ";
        String[] genericHashes = new String[NgenericCards];
        String[] genericSalts = new String[NgenericCards];
        for (int i4 = 0; i4 < NgenericCards; i4++) {
            space = securityText.indexOf(32);
            genericHashes[i4] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
            space = securityText.indexOf(32);
            genericSalts[i4] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        if (!this.game.SetFPSecurity(passwordHash, passwordSalt, CcardsHashes, CcardsSalts, ScardsHashes, ScardsSalts, FcardsHashes, FcardsSalts, genericHashes, genericSalts)) {
            JOptionPane.showMessageDialog(Game.win, "Error, the opponent is attempting to use incorrect FP security tokens!");
            if (this.game.talker.server) {
                this.game.talker.disconnect(false);
            }
        }
    }

    private void SetSPSecurity(String securityTextInput) {
        String securityText = securityTextInput;
        String[] FcardsHashes;
        String[] FcardsSalts;
        
        int space = securityText.indexOf(32);
        String passwordHash = securityText.substring(0, space);
        securityText = securityText.substring(space + 1);

        space = securityText.indexOf(32);
        String passwordSalt = securityText.substring(0, space);
        securityText = securityText.substring(space + 1);

        space = securityText.indexOf(32);
        int NcharacterCards = Integer.parseInt(securityText.substring(0, space));
        securityText = String.valueOf(securityText.substring(space + 1)) + " ";
        String[] CcardsHashes = new String[NcharacterCards];
        String[] CcardsSalts = new String[NcharacterCards];
        for (int i = 0; i < NcharacterCards; i++) {
            space = securityText.indexOf(32);
            CcardsHashes[i] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);

            space = securityText.indexOf(32);
            CcardsSalts[i] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        space = securityText.indexOf(32);
        int NstrategyCards = Integer.parseInt(securityText.substring(0, space));
        securityText = securityText.substring(space + 1);

        String[] ScardsHashes = new String[NstrategyCards];
        String[] ScardsSalts = new String[NstrategyCards];
        for (int i2 = 0; i2 < NstrategyCards; i2++) {
            space = securityText.indexOf(32);
            ScardsHashes[i2] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);

            space = securityText.indexOf(32);
            ScardsSalts[i2] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        if (Game.isWOME.booleanValue()) {
            space = securityText.indexOf(32);
            int NfactionCards = Integer.parseInt(securityText.substring(0, space));
            securityText = securityText.substring(space + 1);
            FcardsHashes = new String[NfactionCards];
            FcardsSalts = new String[NfactionCards];
            for (int i3 = 0; i3 < NfactionCards; i3++) {
                space = securityText.indexOf(32);
                FcardsHashes[i3] = securityText.substring(0, space);
                securityText = securityText.substring(space + 1);

                space = securityText.indexOf(32);
                FcardsSalts[i3] = securityText.substring(0, space);
                securityText = securityText.substring(space + 1);
            }
        } else {
            FcardsHashes = new String[0];
            FcardsSalts = new String[0];
        }
        space = securityText.indexOf(32);
        int NgenericCards = Integer.parseInt(securityText.substring(0, space));
        securityText = String.valueOf(securityText.substring(space + 1)) + " ";
        String[] genericHashes = new String[NgenericCards];
        String[] genericSalts = new String[NgenericCards];
        for (int i4 = 0; i4 < NgenericCards; i4++) {
            space = securityText.indexOf(32);
            genericHashes[i4] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);

            space = securityText.indexOf(32);
            genericSalts[i4] = securityText.substring(0, space);
            securityText = securityText.substring(space + 1);
        }
        if (!this.game.SetSPSecurity(passwordHash, passwordSalt, CcardsHashes, CcardsSalts, ScardsHashes, ScardsSalts, FcardsHashes, FcardsSalts, genericHashes, genericSalts)) {
            if (this.fromFile) {
                if (this.game.GetFPpassword() != null) {
                    this.game.HideFPhand();
                    this.game.ClearFPDecks();
                }
                if (this.game.GetSPpassword() != null) {
                    this.game.HideSPhand();
                    this.game.ClearSPDecks();
                }
            } else {
                JOptionPane.showMessageDialog(Game.win, "Error, the opponent is attempting to use incorrect SP security tokens!");
            }
            if (this.game.talker.server) {
                this.game.talker.disconnect(false);
            }
        }
    }

    public void exPassword(String cmd, String name) {
        if (cmd.indexOf(Messages.getString("Interpreter.199")) != -1 && (name.equals("$" + this.game.opponent) || name.startsWith("%"))) {
            SetFPSecurity(cmd.substring(cmd.indexOf(Messages.getString("Interpreter.201")) + Messages.getString("Interpreter.201").length(), cmd.length()).trim());
            if (name.equals("$" + this.game.opponent)) {
                this.game.setViewing(false, true, true);
            }
        }
        if (cmd.indexOf(Messages.getString("Interpreter.203")) != -1) {
            this.game.shadowname = name.substring(1);
            if (name.equals("$" + this.game.opponent) || name.startsWith("%")) {
                SetSPSecurity(cmd.substring(cmd.indexOf(Messages.getString("Interpreter.480")) + Messages.getString("Interpreter.480").length(), cmd.length()).trim());
                if (name.equals("$" + this.game.opponent)) {
                    this.game.setViewing(false, false, true);
                }
            }
        }
        this.game.controls.chat.write(String.valueOf(name) + " " + cmd.substring(0, cmd.indexOf(".") + 1));
    }

    public void exTokens(String cmd, String name) {
        if (cmd.indexOf("FREE_PEOPLE") != -1 && (name.equals("$" + this.game.opponent) || name.startsWith("%"))) {
            SetFPSecurity(cmd.substring(cmd.indexOf("FREE_PEOPLE.") + "FREE_PEOPLE.".length(), cmd.length()).trim());
        }
        if (cmd.indexOf("SHADOW.") != -1) {
            this.game.shadowname = name.substring(1);
            if (name.equals("$" + this.game.opponent) || name.startsWith("%")) {
                SetSPSecurity(cmd.substring(cmd.indexOf("SHADOW.") + "SHADOW.".length(), cmd.length()).trim());
            }
        }
    }

    public void exContact(String cmd) {
        if (this.game.connectsound && !this.contactTimer) {
            this.contactTimer = true;
        }
        String name = cmd.substring(cmd.indexOf(">") + 2, cmd.indexOf(","));
        if (cmd.substring(cmd.indexOf(","), cmd.length()).indexOf(Messages.getString("Interpreter.214")) != -1) {
            if (name.equals(this.game.opponent)) {
                this.game.setViewing(false, true, true);
            } else {
                this.game.setViewing(true, true, true);
            }
        }
        if (cmd.substring(cmd.indexOf(","), cmd.length()).indexOf(Messages.getString("Interpreter.215")) != -1) {
            if (name.equals(this.game.opponent)) {
                this.game.setViewing(false, false, true);
            } else {
                this.game.setViewing(true, false, true);
            }
        }
        this.game.redoTitle();
        if (!cmd.substring(cmd.indexOf(","), cmd.lastIndexOf(",")).equals(String.valueOf(Messages.getString("Talker.17")) + Game.versionno + " [" + Game.boardtype + Messages.getString("Interpreter.26") + Game.varianttype + "]")) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Interpreter.29"));
        }
        this.game.controls.chat.write(cmd.substring(0, cmd.indexOf(".") + 1));
    }

    public void exDamage(String cmd, String name, int damage) {
        double scale = ((double) Game.prefs.zoom) / 100.0d;
        Font f = new Font("Monospaced", 1, 14);
        if (cmd.contains(Messages.getString("Interpreter.92"))) {
            if (this.game.OrthancStructure != null) {
                this.game.boardlabel.remove(this.game.OrthancStructure);
            }
            this.game.OrthancStructure = new JLabel();
            this.game.OrthancHP += damage;
            this.game.OrthancStructure.setBounds((int) ((135.0d * scale) - 10.0d), (int) ((83.0d * scale) - 10.0d), 10, 10);
            this.game.OrthancStructure.setFont(f);
            this.game.OrthancStructure.setForeground(Color.black);
            this.game.OrthancStructure.setText(String.valueOf(this.game.OrthancHP));
            this.game.boardlabel.add(this.game.OrthancStructure);
            this.game.OrthancStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.93"))) {
            if (this.game.HornburgStructure != null) {
                this.game.boardlabel.remove(this.game.HornburgStructure);
            }
            this.game.HornburgStructure = new JLabel();
            this.game.HornburgHP += damage;
            this.game.HornburgStructure.setBounds((int) ((373.0d * scale) - 10.0d), (int) ((521.0d * scale) - 10.0d), 10, 10);
            this.game.HornburgStructure.setFont(f);
            this.game.HornburgStructure.setForeground(Color.black);
            this.game.HornburgStructure.setText(String.valueOf(this.game.HornburgHP));
            this.game.boardlabel.add(this.game.HornburgStructure);
            this.game.HornburgStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.94"))) {
            if (this.game.EdorasStructure != null) {
                this.game.boardlabel.remove(this.game.EdorasStructure);
            }
            this.game.EdorasStructure = new JLabel();
            this.game.EdorasHP += damage;
            this.game.EdorasStructure.setBounds((int) ((712.0d * scale) - 10.0d), (int) ((517.0d * scale) - 10.0d), 10, 10);
            this.game.EdorasStructure.setFont(f);
            this.game.EdorasStructure.setForeground(Color.black);
            this.game.EdorasStructure.setText(String.valueOf(this.game.EdorasHP));
            this.game.boardlabel.add(this.game.EdorasStructure);
            this.game.EdorasStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.95"))) {
            if (this.game.NCityWallStructure != null) {
                this.game.boardlabel.remove(this.game.NCityWallStructure);
            }
            this.game.NCityWallStructure = new JLabel();
            this.game.NCityWallHP += damage;
            if (this.game.NCityWallHP == 10) {
                this.game.NCityWallStructure.setBounds((int) ((269.0d * scale) - 20.0d), (int) ((369.0d * scale) - 20.0d), 20, 20);
            } else {
                this.game.NCityWallStructure.setBounds((int) ((264.0d * scale) - 10.0d), (int) ((365.0d * scale) - 10.0d), 10, 10);
            }
            this.game.NCityWallStructure.setFont(f);
            this.game.NCityWallStructure.setForeground(Color.black);
            this.game.NCityWallStructure.setText(String.valueOf(this.game.NCityWallHP));
            this.game.boardlabel.add(this.game.NCityWallStructure);
            this.game.NCityWallStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.97"))) {
            if (this.game.GreatGateStructure != null) {
                this.game.boardlabel.remove(this.game.GreatGateStructure);
            }
            this.game.GreatGateStructure = new JLabel();
            this.game.GreatGateHP += damage;
            this.game.GreatGateStructure.setBounds((int) ((297.0d * scale) - 10.0d), (int) ((425.0d * scale) - 10.0d), 10, 10);
            this.game.GreatGateStructure.setFont(f);
            this.game.GreatGateStructure.setForeground(Color.black);
            this.game.GreatGateStructure.setText(String.valueOf(this.game.GreatGateHP));
            this.game.boardlabel.add(this.game.GreatGateStructure);
            this.game.GreatGateStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.98"))) {
            if (this.game.SouthRammasStructure != null) {
                this.game.boardlabel.remove(this.game.SouthRammasStructure);
            }
            this.game.SouthRammasStructure = new JLabel();
            this.game.SouthRammasHP += damage;
            if (this.game.SouthRammasHP == 10) {
                this.game.SouthRammasStructure.setBounds((int) ((248.0d * scale) - 20.0d), (int) ((490.0d * scale) - 20.0d), 20, 20);
            } else {
                this.game.SouthRammasStructure.setBounds((int) ((243.0d * scale) - 10.0d), (int) ((485.0d * scale) - 10.0d), 10, 10);
            }
            this.game.SouthRammasStructure.setFont(f);
            this.game.SouthRammasStructure.setForeground(Color.black);
            this.game.SouthRammasStructure.setText(String.valueOf(this.game.SouthRammasHP));
            this.game.boardlabel.add(this.game.SouthRammasStructure);
            this.game.SouthRammasStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.99"))) {
            if (this.game.CairAndrosStructure != null) {
                this.game.boardlabel.remove(this.game.CairAndrosStructure);
            }
            this.game.CairAndrosStructure = new JLabel();
            this.game.CairAndrosHP += damage;
            this.game.CairAndrosStructure.setBounds((int) ((501.0d * scale) - 10.0d), (int) ((42.0d * scale) - 10.0d), 10, 10);
            this.game.CairAndrosStructure.setFont(f);
            this.game.CairAndrosStructure.setForeground(Color.black);
            this.game.CairAndrosStructure.setText(String.valueOf(this.game.CairAndrosHP));
            this.game.boardlabel.add(this.game.CairAndrosStructure);
            this.game.CairAndrosStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.225"))) {
            if (this.game.FrontGateStructure != null) {
                this.game.boardlabel.remove(this.game.FrontGateStructure);
            }
            this.game.FrontGateStructure = new JLabel();
            this.game.FrontGateHP += damage;
            this.game.FrontGateStructure.setBounds((int) ((140.0d * scale) - 10.0d), (int) ((115.0d * scale) - 10.0d), 10, 10);
            this.game.FrontGateStructure.setFont(f);
            this.game.FrontGateStructure.setForeground(Color.black);
            this.game.FrontGateStructure.setText(String.valueOf(this.game.FrontGateHP));
            this.game.boardlabel.add(this.game.FrontGateStructure);
            this.game.FrontGateStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.226"))) {
            if (this.game.RavenHillStructure != null) {
                this.game.boardlabel.remove(this.game.RavenHillStructure);
            }
            this.game.RavenHillStructure = new JLabel();
            this.game.RavenHillHP += damage;
            this.game.RavenHillStructure.setBounds((int) ((126.0d * scale) - 10.0d), (int) ((615.0d * scale) - 10.0d), 10, 10);
            this.game.RavenHillStructure.setFont(f);
            this.game.RavenHillStructure.setForeground(Color.black);
            this.game.RavenHillStructure.setText(String.valueOf(this.game.RavenHillHP));
            this.game.boardlabel.add(this.game.RavenHillStructure);
            this.game.RavenHillStructure.repaint();
        }
        if (cmd.contains(Messages.getString("Interpreter.227"))) {
            if (this.game.EasternSpurStructure != null) {
                this.game.boardlabel.remove(this.game.EasternSpurStructure);
            }
            this.game.EasternSpurStructure = new JLabel();
            this.game.EasternSpurHP += damage;
            this.game.EasternSpurStructure.setBounds((int) ((768.0d * scale) - 10.0d), (int) ((191.0d * scale) - 10.0d), 10, 10);
            this.game.EasternSpurStructure.setFont(f);
            this.game.EasternSpurStructure.setForeground(Color.black);
            this.game.EasternSpurStructure.setText(String.valueOf(this.game.EasternSpurHP));
            this.game.boardlabel.add(this.game.EasternSpurStructure);
            this.game.EasternSpurStructure.repaint();
        }
        if (damage < 0) {
            this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.100") + cmd);
        } else if (damage > 0) {
            this.game.controls.chat.write(String.valueOf(name) + Messages.getString("Interpreter.101") + cmd);
        }
    }

    public void exInitialCounters() {
        this.game.controls.chat.silent = true;
        this.game.talker.enqueue("$" + Game.prefs.nick + " silent ");
        this.game.talker.enqueue("$" + Game.prefs.nick + " noisy ");
        this.game.controls.chat.write(Messages.getString("Interpreter.123"));
        this.game.controls.chat.silent = false;
    }

    private void exUndo(String cmd, String nick) {
        do {
            int temppointer = this.game.historypointer;
            if (this.game.historypointer == this.game.history.size()) {
                ++this.game.historypointer;
                this.createHistory();
            }
            this.game.historypointer = temppointer;
            if (this.game.history.size() <= 0) continue;
            GamePiece[] historybits = this.game.history.get(this.game.historypointer - 1);
            int i = 0;
            while (i < this.game.numBits) {
                if (this.game.bits[i].currentLocation.name() != historybits[i].currentLocation.name()) {
                    this.game.bits[i].moveTo(historybits[i].currentLocation);
                }
                if (this.game.bits[i] instanceof Flippable && ((Flippable)((Object)this.game.bits[i])).currentState() != ((Flippable)((Object)historybits[i])).currentState()) {
                    ((Flippable)((Object)this.game.bits[i])).flip();
                }
                ++i;
            }
            String chatStr = String.valueOf(nick) + " " + cmd + ": " + this.game.historyactions.get(this.game.historyactionpointer - 1);
            --this.game.historypointer;
            --this.game.historyactionpointer;
            Game.SarumanInIsengard = Game.boardtype.equals("wotr") && (Game.areas[40].containsPiece(this.game.bits[this.game.SarumanNo]) || Game.areas[158].containsPiece(this.game.bits[this.game.SarumanNo]));
            this.game.refreshBoard();
            this.game.controls.chat.writenohistory(chatStr);
        } while (this.game.historyactionpointer > 0 && this.game.historyactions.get(this.game.historyactionpointer - 1).startsWith("<auto>"));
    }

    private void exRedo(String cmd, String nick) {
        do {
            if (this.game.history.size() <= this.game.historypointer + 1) continue;
            GamePiece[] historybits = this.game.history.get(this.game.historypointer + 1);
            int i = 0;
            while (i < this.game.numBits) {
                if (this.game.bits[i].currentLocation.name() != historybits[i].currentLocation.name()) {
                    this.game.bits[i].moveTo(historybits[i].currentLocation);
                }
                ++i;
            }
            String chatStr = String.valueOf(nick) + " " + cmd + ": " + this.game.historyactions.get(this.game.historyactionpointer);
            ++this.game.historypointer;
            ++this.game.historyactionpointer;
            if (this.game.historyactions.get(this.game.historyactions.size() - 1).indexOf(Messages.getString("Interpreter.52")) > -1) {
                this.game.controls.chat.silent = true;
                int i2 = 0;
                while (i2 < this.game.numBits) {
                    if (this.game.bits[i2] instanceof Flippable && this.game.historyactions.get(this.game.historyactions.size() - 1).indexOf(this.game.bits[i2].type()) > -1) {
                        this.exFlip(String.valueOf(this.game.bits[i2].id()) + " ", "<replay>");
                    }
                    ++i2;
                }
                this.game.controls.chat.silent = false;
            }
            Game.SarumanInIsengard = Game.boardtype.equals("wotr") && Game.areas[40].containsPiece(this.game.bits[this.game.SarumanNo]) || Game.areas[158].containsPiece(this.game.bits[this.game.SarumanNo]);
            this.game.refreshBoard();
            this.game.controls.chat.writenohistory(chatStr);
        } while (this.game.historyactionpointer > 0 && this.game.historyactions.get(this.game.historyactionpointer - 1).startsWith("<auto>"));
    }

    private GamePiece[] copybits(GamePiece[] sourcebits) {
        GamePiece[] destbits = new GamePiece[sourcebits.length];
        for (int i = 0; i < this.game.numBits; i++) {
            if (sourcebits[i] == null) {
                destbits[i] = null;
                continue;
            }
            destbits[i] = new UnitGenericPiece(sourcebits[i].currentLocation);
            if (sourcebits[i] instanceof Flippable) {
                ((UnitGenericPiece) destbits[i]).setState(((Flippable) sourcebits[i]).currentState());
            }
        }
        return destbits;
    }

    public void record(String s) {
        try {
            this.game.newgame = false;
            String s_removed = Messages.removeKeyString(s);
            if (!s_removed.startsWith(Messages.getString("Interpreter.17")) && !s_removed.startsWith(Messages.getString("Interpreter.18")) && !s_removed.startsWith(Messages.getString("Controls.825")) && !s_removed.startsWith("$" + Game.prefs.nick + " secureDice") && !s_removed.startsWith("$" + this.game.opponent + " secureDice")) {
                String sLine = String.valueOf(s) + " " + this.logHash + "\n";
                this.logHash = null;
                this.log.write(sLine);
                this.logText.append(sLine);
                this.log.flush();
            }
        } catch (IOException e) {
            System.out.println(Messages.getString("Interpreter.470"));
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.game.inaction = true;
        replayStep();
        this.game.inaction = false;
    }

    /* access modifiers changed from: package-private */
    public void replayStep() {
        // Removed piecesLoadedFromDatabase check - replay works fine with DB-loaded pieces
        // resetBoard() re-initializes pieces before replay, and card verification is skipped via the flag
        
        try {
            if (this.replayBuffer != null && this.replayBuffer.ready()) {
                this.fromFile = true;
                execute(this.game.talker.DeHashCommand(this.replayBuffer.readLine()));
                this.fromFile = false;
            } else if (this.replayBuffer != null) {
                this.replayBuffer.close();
                this.replayBuffer = null;
                this.game.talker.enqueue(Messages.getKeyString("Interpreter.471"));
                this.timer.stop();
            }
        } catch (IOException e) {
            System.err.println(String.valueOf(Messages.getString("Interpreter.472")) + e);
        }
    }

    /* access modifiers changed from: package-private */
    void replayContinue() {
        // Removed piecesLoadedFromDatabase check - replay works fine with DB-loaded pieces
        if (this.replayBuffer == null) {
            return;
        }
        try {
            while (this.replayBuffer.ready()) {
                this.fromFile = true;
                String s = this.replayBuffer.readLine();
                if (s == null) continue;
                s = this.game.talker.DeHashCommand(s);
                this.execute(s);
                this.fromFile = false;
                if (!(s = Messages.removeKeyString(s)).startsWith("<replay>")) continue;
                return;
            }
            this.replayBuffer.close();
            this.replayBuffer = null;
            this.game.talker.enqueue(Messages.getKeyString("Interpreter.474"));
            this.timer.stop();
            return;
        }
        catch (IOException e) {
            System.err.println(String.valueOf(Messages.getString("Interpreter.475")) + e);
            return;
        }
    }


    /* access modifiers changed from: package-private */
    public void toggleReplayClock() {
        if (this.replayBuffer == null) {
            return;
        }
        if (this.timer.isRunning()) {
            this.timer.stop();
        } else {
            this.timer.start();
        }
    }

    /* access modifiers changed from: package-private */
    public void decreaseReplaySpeed() {
        if (this.replayBuffer != null) {
            this.timer.setDelay((int) (((double) this.timer.getDelay()) * 1.2d));
        }
    }

    /* access modifiers changed from: package-private */
    public void increaseReplaySpeed() {
        if (this.replayBuffer != null) {
            this.timer.setDelay((int) (((double) this.timer.getDelay()) / 1.2d));
        }
    }

    private void unChoose(GamePiece p) {
        if (p != null && p.equals(this.game.chosen1)) {
            this.game.chosen1 = null;
        }
        if (p != null && p.equals(this.game.chosen2)) {
            this.game.chosen2 = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void flash_preHunt() {
        for (int i = 146; i < 152; i++) {
            if (Game.areas[i].numPieces() > 0 && (Game.areas[i].pieces().get(0) instanceof FreeCharacterCard) && ((FreeCharacterCard) Game.areas[i].pieces().get(0)).name().equals(Messages.getString("Game.1643"))) {
                ((Brightness) Game.areas[i].pieces().get(0)).toggleBrightness();
            }
            if (Game.areas[i].numPieces() > 0 && (Game.areas[i].pieces().get(0) instanceof ShadowCharacterCard) && ((ShadowCharacterCard) Game.areas[i].pieces().get(0)).name().equals(Messages.getString("Game.1670"))) {
                ((Brightness) Game.areas[i].pieces().get(0)).toggleBrightness();
            }
        }
        this.preHuntFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashOptional > 0 && this.preHuntFlashes > (Game.prefs.flashOptional * 2) - 1) {
            this.preHuntTimer = false;
            this.preHuntFlashes = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void flash_Balrog() {
        for (int t = 0; t < Game.areas[117].numPieces(); t++) {
            if (Game.areas[117].pieces().get(t).type().equals(Messages.getString("Game.1863")) || Game.areas[117].pieces().get(t).type().equals(Messages.getString("Game.1865"))) {
                ((Brightness) Game.areas[117].pieces().get(t)).toggleBrightness();
            }
        }
        for (int t2 = 0; t2 < Game.areas[118].numPieces(); t2++) {
            if (Game.areas[118].pieces().get(t2).type().equals(Messages.getString("Game.1863")) || Game.areas[118].pieces().get(t2).type().equals(Messages.getString("Game.1865"))) {
                ((Brightness) Game.areas[118].pieces().get(t2)).toggleBrightness();
            }
        }
        for (int t3 = 0; t3 < Game.areas[119].numPieces(); t3++) {
            if (Game.areas[119].pieces().get(t3).type().equals(Messages.getString("Game.1863")) || Game.areas[119].pieces().get(t3).type().equals(Messages.getString("Game.1865"))) {
                ((Brightness) Game.areas[119].pieces().get(t3)).toggleBrightness();
            }
        }
        this.balrogFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashMandatory > 0 && this.balrogFlashes > (Game.prefs.flashMandatory * 2) - 1) {
            this.balrogBrighterTimer = false;
            this.balrogFlashes = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void flash_WK() {
        for (int t = 0; t < Game.areas[117].numPieces(); t++) {
            if (!Game.areas[117].pieces().get(t).type().equals(Messages.getString("Game.1863")) && (Game.areas[117].pieces().get(t) instanceof TwoChit) && ((TwoChit) Game.areas[117].pieces().get(t)).getPic() != ((TwoChit) Game.areas[117].pieces().get(t)).getReversePic()) {
                ((Brightness) Game.areas[117].pieces().get(t)).toggleBrightness();
            }
        }
        for (int t2 = 0; t2 < Game.areas[118].numPieces(); t2++) {
            if (!Game.areas[118].pieces().get(t2).type().equals(Messages.getString("Game.1863")) && (Game.areas[118].pieces().get(t2) instanceof TwoChit) && ((TwoChit) Game.areas[118].pieces().get(t2)).getPic() != ((TwoChit) Game.areas[118].pieces().get(t2)).getReversePic()) {
                ((Brightness) Game.areas[118].pieces().get(t2)).toggleBrightness();
            }
        }
        for (int t3 = 0; t3 < Game.areas[119].numPieces(); t3++) {
            if (!Game.areas[119].pieces().get(t3).type().equals(Messages.getString("Game.1863")) && (Game.areas[119].pieces().get(t3) instanceof TwoChit) && ((TwoChit) Game.areas[119].pieces().get(t3)).getPic() != ((TwoChit) Game.areas[119].pieces().get(t3)).getReversePic()) {
                ((Brightness) Game.areas[119].pieces().get(t3)).toggleBrightness();
            }
        }
        this.wkFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashMandatory > 0 && this.wkFlashes > (Game.prefs.flashMandatory * 2) - 1) {
            this.witchkingBrighterTimer = false;
            this.wkFlashes = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void flash_Cards() {
        for (int i = Game.BOARD_SLOT_1; i <= Game.BOARD_SLOT_6; i++) {
            if (Game.areas[i].numPieces() > 0 && (Game.areas[i].pieces().get(0) instanceof Brightness) && ((!(Game.areas[i].pieces().get(0) instanceof FreeCharacterCard) || !((FreeCharacterCard) Game.areas[i].pieces().get(0)).name().equals(Messages.getString("Game.1643"))) && (!(Game.areas[i].pieces().get(0) instanceof ShadowCharacterCard) || !((ShadowCharacterCard) Game.areas[i].pieces().get(0)).name().equals(Messages.getString("Game.18")) || (Game.areas[116].pieces().get(0) instanceof UnitSmeagol)))) {
                ((Brightness) Game.areas[i].pieces().get(0)).toggleBrightness();
            }
        }
        this.cardFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashOptional > 0 && this.cardFlashes > (Game.prefs.flashOptional * 2) - 1) {
            this.cardBrighterTimer = false;
            this.cardFlashes = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void flash_Dice() {
        if (this.dice1flashpos > -1) {
            ((NaryaDice) Game.areas[178].get(this.dice1flashpos)).toggleAppearance();
        }
        if (this.dice2flashpos > -1) {
            ((VilyaDice) Game.areas[178].get(this.dice2flashpos)).toggleAppearance();
        }
        if (this.dice3flashpos > -1) {
            ((NenyaDice) Game.areas[178].get(this.dice3flashpos)).toggleAppearance();
        }
        if (this.dice4flashpos > -1) {
            ((BalrogDice) Game.areas[179].get(this.dice4flashpos)).toggleAppearance();
        }
        if (this.dice5flashpos > -1) {
            ((GothmogDice) Game.areas[179].get(this.dice5flashpos)).toggleAppearance();
        }
        this.diceFlashes++;
        System.out.println(this.diceFlashes);
        game.refreshBoard();
        if (Game.prefs.flashOptional > 0 && this.diceFlashes > (Game.prefs.flashOptional * 2) - 1) {
            this.diceTimer = false;
            this.diceFlashes = 0;
            if (this.dice1flashpos > -1) {
                ((NaryaDice) Game.areas[178].get(this.dice1flashpos)).setVisible();
            }
            if (this.dice2flashpos > -1) {
                ((VilyaDice) Game.areas[178].get(this.dice2flashpos)).setVisible();
            }
            if (this.dice3flashpos > -1) {
                ((NenyaDice) Game.areas[178].get(this.dice3flashpos)).setVisible();
            }
            if (this.dice4flashpos > -1) {
                ((BalrogDice) Game.areas[179].get(this.dice4flashpos)).setVisible();
            }
            if (this.dice5flashpos > -1) {
                ((GothmogDice) Game.areas[179].get(this.dice5flashpos)).setVisible();
            }
            this.dice1flashpos = -1;
            this.dice2flashpos = -1;
            this.dice3flashpos = -1;
            this.dice4flashpos = -1;
            this.dice5flashpos = -1;
        }
    }

    /* access modifiers changed from: private */
    public void flash_NorthPass() {
        ((Chit) Game.areas[60].pieces().get(0)).toggleAppearance();
        this.northpassFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashOptional > 0 && this.northpassFlashes > (Game.prefs.flashOptional * 2) - 1) {
            this.goblinpassnorthTimer = false;
            this.northpassFlashes = 0;
        }
    }

    /* access modifiers changed from: private */
    public void flash_WestPass() {
        ((Chit) Game.areas[59].pieces().get(0)).toggleAppearance();
        this.westpassFlashes++;
        this.game.refreshBoard();
        if (Game.prefs.flashOptional > 0 && this.westpassFlashes > (Game.prefs.flashOptional * 2) - 1) {
            this.goblinpasswestTimer = false;
            this.westpassFlashes = 0;
        }
    }
    
    /**
     * Adds Dwarven Ring pieces to the game selection.
     * @param cmd The command string (e.g., "adds 4 Dwarven Ring(s) ")
     */
    private void exAddDwarvenRings(String cmd) {
        // Parse the number of rings from the command
        // cmd format: "adds 4 Dwarven Ring(s) " - skip "adds " then parse number
        int numRings = 1;
        try {
            // Skip past "adds " to get to the number
            int firstSpace = cmd.indexOf(" ");
            if (firstSpace > 0) {
                String afterAdds = cmd.substring(firstSpace + 1); // "4 Dwarven Ring(s) "
                int secondSpace = afterAdds.indexOf(" ");
                if (secondSpace > 0) {
                    numRings = Integer.parseInt(afterAdds.substring(0, secondSpace));
                } else {
                    numRings = Integer.parseInt(afterAdds.trim());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing dwarven ring count: " + cmd);
            return;
        }

        // Add the dwarven rings to the game selection using Chit class
        String dwarvenRingType = Messages.getString("DwarvenRing.0");
        for (int i = 0; i < numRings; i++) {
            String imagePath = Messages.getLanguageLocation("images/DwarvenRing" + String.format("%01d", i + 1) + ".png");
            Chit dwarvenRing = new Chit(Game.areas[114], dwarvenRingType, imagePath);
            this.game.selection.addPiece(dwarvenRing);
        }
    }
}
