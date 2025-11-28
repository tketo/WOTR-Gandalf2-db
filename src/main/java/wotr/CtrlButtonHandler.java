package wotr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/* renamed from: CtrlButtonHandler */
/* compiled from: Controls */
class CtrlButtonHandler implements ActionListener {
    Controls controls;
    Game game;

    CtrlButtonHandler(Controls c) {
        this.controls = c;
        this.game = c.game;
    }

    private static boolean isElvenRing(Chit c) {
        return Messages.getString("Controls.285").equals(c.type()) || Messages.getString("Controls.286").equals(c.type()) || Messages.getString("Controls.287").equals(c.type()) || Messages.getString("Controls.288").equals(c.type());
    }

    private static boolean isElvenRing(TwoChit c) {
        return Messages.getString("Controls.285").equals(c.type()) || Messages.getString("Controls.286").equals(c.type()) || Messages.getString("Controls.287").equals(c.type()) || Messages.getString("Controls.288").equals(c.type());
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("[CB] Menu action: " + e.getActionCommand());
        if (e.getActionCommand().startsWith("schange")) {
            actionPerformed_sChange(e);
        } else if (e.getActionCommand().startsWith("fchange")) {
            actionPerformed_fChange(e);
        } else if (e.getActionCommand().equals("kill")) {
            actionPerformed_kill();
        } else if (e.getActionCommand().equals("dkill")) {
            actionPerformed_dkill();
        } else if (e.getActionCommand().equals("flip")) {
            actionPerformed_flip();
        } else if (e.getActionCommand().equals("server")) {
            actionPerformed_server();
        } else if (e.getActionCommand().equals("hostobs")) {
            actionPerformed_hostobs();
        } else if (e.getActionCommand().equals("connectobs")) {
            actionPerformed_connectobs();
        } else if (e.getActionCommand().equals("client")) {
            actionPerformed_client();
        } else if (e.getActionCommand().equals("roll")) {
            actionPerformed_rollActionDice();
        } else if (e.getActionCommand().equals("ring")) {
            actionPerformed_ring();
        } else if (e.getActionCommand().equals("ringVilya")) {
            actionPerformed_ringVilya();
        } else if (e.getActionCommand().equals("ringNenya")) {
            actionPerformed_ringNenya();
        } else if (e.getActionCommand().equals("ringNarya")) {
            actionPerformed_ringNarya();
        } else if (e.getActionCommand().equals("eye")) {
            actionPerformed_eye();
        } else if (e.getActionCommand().equals("stats")) {
            actionPerformed_stats();
        } else if (e.getActionCommand().equals("cards")) {
            actionPerformed_cards();
        } else if (e.getActionCommand().equals("challengestats")) {
            actionPerformed_challengestats();
        } else if (e.getActionCommand().equals("load")) {
            actionPerformed_load();
        } else if (e.getActionCommand().equals("loadchallenge")) {
            actionPerformed_loadChallengeFile();
        } else if (e.getActionCommand().equals("quit")) {
            actionPerformed_quit();
        } else if (e.getActionCommand().startsWith("newBase2")) {
            if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.405"), Messages.getString("Controls.406"), 0, 2) == 0) {
                Game.prefs.lastGame = e.getActionCommand().replace("newBase2","base2");
                Game.prefs.save(Game.PREF_FILE);
                Game.main((String[]) null);
            }
        } else if (e.getActionCommand().startsWith("newLOME")) {
            if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.405"), Messages.getString("Controls.406"), 0, 2) == 0) {
                Game.prefs.lastGame = e.getActionCommand();
                Game.prefs.save(Game.PREF_FILE);
                Game.main((String[]) null);
            }
        } else if (e.getActionCommand().startsWith("newWOME")) {
            if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.405"), Messages.getString("Controls.406"), 0, 2) == 0) {
                Game.prefs.lastGame = e.getActionCommand();
                Game.prefs.save(Game.PREF_FILE);
                Game.main((String[]) null);
            }
        } else if (e.getActionCommand().equals("changenick")) {
            actionPerformed_changeNick();
        } else if (e.getActionCommand().equals("getaddress")) {
            this.game.showAddress();
        } else if (e.getActionCommand().equals("disconnect")) {
            actionPerformed_disconnect();
        } else if (e.getActionCommand().equals("disconnectobs")) {
            actionPerformed_disconnectObs();
        } else if (e.getActionCommand().equals("fsm_connect")) {
            actionPerformed_fsmConnect();
        } else if (e.getActionCommand().equals("fsm_disconnect")) {
            actionPerformed_fsmDisconnect();
        } else if (e.getActionCommand().equals("FSM_ROLL_FP_DICE")) {
            actionPerformed_fsmRollFpDice();
        } else if (e.getActionCommand().equals("FSM_ROLL_SP_DICE")) {
            actionPerformed_fsmRollSpDice();
        } else if (e.getActionCommand().equals("wwsat")) {
            actionPerformed_wwsat();
        } else if (e.getActionCommand().equals("viewfp")) {
            actionPerformed_viewfp();
        } else if (e.getActionCommand().equals("viewsa")) {
            actionPerformed_viewsa();
        } else if (e.getActionCommand().equals("fdrawfaction")) {
            actionPerformed_fdrawfaction();
        } else if (e.getActionCommand().equals("fdrawfaction2")) {
            actionPerformed_fdrawfaction2();
        } else if (e.getActionCommand().equals("fdrawc")) {
            actionPerformed_fdrawc();
        } else if (e.getActionCommand().equals("fdrawstory")) {
            actionPerformed_fdrawstory();
        } else if (e.getActionCommand().equals("fdraws")) {
            actionPerformed_fdraws();
        } else if (e.getActionCommand().equals("sdrawstory")) {
            actionPerformed_sdrawstory();
        } else if (e.getActionCommand().equals("fdrawfate")) {
            actionPerformed_fdrawfate();
        } else if (e.getActionCommand().equals("fdrawgeneric")) {
            actionPerformed_fdrawgeneric();
        } else if (e.getActionCommand().equals("sdrawfaction")) {
            actionPerformed_sdrawfaction();
        } else if (e.getActionCommand().equals("sdrawfaction2")) {
            actionPerformed_sdrawfaction2();
        } else if (e.getActionCommand().equals("sdrawc")) {
            actionPerformed_sdrawc();
        } else if (e.getActionCommand().equals("sdraws")) {
            actionPerformed_sdraws();
        } else if (e.getActionCommand().equals("sdrawgeneric")) {
            actionPerformed_sdrawgeneric();
        } else if (e.getActionCommand().equals("sdrawspecial")) {
            actionPerformed_sdrawspecial();
        } else if (e.getActionCommand().equals("structuredamage")) {
            actionPerformed_structuredamage();
        } else if (e.getActionCommand().equals("structurerepair")) {
            actionPerformed_structurerepair();
        } else if (e.getActionCommand().equals("initialcounters")) {
            actionPerformed_initialcounters();
        } else if (e.getActionCommand().equals("hunt")) {
            actionPerformed_hunt();
        } else if (e.getActionCommand().equals("fate")) {
            actionPerformed_fate();
        } else if (e.getActionCommand().equals("randomcomp")) {
            actionPerformed_randomcomp();
        } else if (e.getActionCommand().equals("endturn")) {
            actionPerformed_endturn();
        } else if (e.getActionCommand().equals("clearcards")) {
            actionPerformed_clearcards();
        } else if (e.getActionCommand().startsWith("roll") && e.getActionCommand().length() == 5) {
            int nDice = Integer.parseInt(e.getActionCommand().substring(4));
            if (this.game.talker.connected) {
                actionPerformed_SecureRoll(nDice, 6, "roll D6 dice");
            } else {
                this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.1") + DiceRoller.rollD6(nDice));
            }
        } else if (e.getActionCommand().startsWith("mroll") && e.getActionCommand().length() == 6) {
            int nDice2 = Integer.parseInt(e.getActionCommand().substring(5));
            if (this.game.talker.connected) {
                actionPerformed_SecureRoll(nDice2, 6, "roll D6 dice (maneuver)");
            } else {
                this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.111") + DiceRoller.rollD6(nDice2));
            }
        } else if (e.getActionCommand().equals("frecover")) {
            actionPerformed_frecover();
        } else if (e.getActionCommand().equals("frecovertactics")) {
            actionPerformed_frecoverstatistics();
        } else if (e.getActionCommand().equals("srecovertactics")) {
            actionPerformed_srecoverstatistics();
        } else if (e.getActionCommand().equals("srecover")) {
            actionPerformed_srecover();
        } else if (e.getActionCommand().equals("fdiscardstory")) {
            actionPerformed_fdiscardstory();
        } else if (e.getActionCommand().equals("stransfergeneric")) {
            actionPerformed_stransfergeneric();
        } else if (e.getActionCommand().equals("movefsp")) {
            actionPerformed_movefsp();
        } else if (e.getActionCommand().equals("movefatemarker")) {
            actionPerformed_movefatemarker();
        } else if (e.getActionCommand().equals("reformentwood")) {
            actionPerformed_reformentwood();
        } else if (e.getActionCommand().equals("undo")) {
            actionPerformed_undo();
        } else if (e.getActionCommand().equals("redo")) {
            actionPerformed_redo();
        } else if (e.getActionCommand().equals("synchronize")) {
            actionPerformed_synchronize();
        } else if (e.getActionCommand().equals("save")) {
            actionPerformed_save();
        } else if (e.getActionCommand().equals("about")) {
            SplashWindow.disposeSplash();
            SplashWindow.splash(Toolkit.getDefaultToolkit().getImage(Messages.getLanguageLocation("images/splash.png")));
        } else if (e.getActionCommand().equals("allowobs")) {
            actionPerformed_allowobs();
        } else if (e.getActionCommand().equals("choose")) {
            actionPerformed_choose();
        } else if (e.getActionCommand().equals("unchoose")) {
            actionPerformed_unchoose();
        } else if (e.getActionCommand().equals("playchosen")) {
            actionPerformed_playchosen();
        } else if (e.getActionCommand().equals("loadreplay")) {
            actionPerformed_loadreplay();
        } else if (e.getActionCommand().equals("playreplay")) {
            this.game.interpreter.toggleReplayClock();
        } else if (e.getActionCommand().equals("fasterreplay")) {
            this.game.interpreter.increaseReplaySpeed();
        } else if (e.getActionCommand().equals("slowerreplay")) {
            this.game.interpreter.decreaseReplaySpeed();
        } else if (e.getActionCommand().equals("stepreplay")) {
            this.game.interpreter.replayStep();
        } else if (e.getActionCommand().equals("continuereplay")) {
            this.game.interpreter.replayContinue();
        } else if (e.getActionCommand().equals("insertbreak")) {
            this.game.interpreter.execute("<replay>" + JOptionPane.showInputDialog(Messages.getString("Controls.189")));
        } else if (e.getActionCommand().equals("setzoom")) {
            actionPerformed_setzoom();
        } else if (e.getActionCommand().equals("readme")) {
            actionPerformed_readme();
        } else if (e.getActionCommand().equals("openguild")) {
            actionPerformed_openguild();
        } else if (e.getActionCommand().equals("soundon")) {
            this.game.soundon = true;
        } else if (e.getActionCommand().equals("soundoff")) {
            this.game.soundon = false;
        } else if (e.getActionCommand().startsWith("translateoff")) {
            this.game.translate = "";
        } else if (e.getActionCommand().startsWith("translateon")) {
            this.game.translate = Game.prefs.translation.substring(Game.prefs.translation.indexOf("[") + 1, Game.prefs.translation.indexOf("]"));
        } else if (e.getActionCommand().startsWith("setBalrog")) {
            Game.versionno = Messages.getString("Controls.190");
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.191"));
        } else if (e.getActionCommand().startsWith("connectsoundoff")) {
            this.game.connectsound = false;
            if (!this.game.interpreter.contactTimer) {
                this.game.interpreter.contactTimer = false;
            }
        } else if (e.getActionCommand().startsWith("connectsoundon")) {
            this.game.connectsound = true;
        }
        this.game.boardlabel.repaint();
    }

    /* access modifiers changed from: package-private */
    public void actionPerformed_SecureRoll(int nDice, int maxValue, String command) {
        if (nDice >= 3 && !command.equals("flip token")) {
            this.game.playSound("soundpack/dice.wav");
        }
        if (maxValue == 0) {
            maxValue = 1;
        }
        this.game.diceRoller.rollDice(nDice, maxValue, command);
    }

    private void actionPerformed_sChange(ActionEvent e) {
        if (this.game.selection.numPieces() != 1 || !(this.game.selection.get(0) instanceof ShadowActionDie)) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.943"));
        } else {
            this.game.talker.enqueue("$" + Game.prefs.nick + " changes " + this.game.selection.contents() + " to " + e.getActionCommand().substring(e.getActionCommand().length() - 1) + " ");
        }
    }

    private void actionPerformed_fChange(ActionEvent e) {
        if (this.game.selection.numPieces() != 1 || !(this.game.selection.get(0) instanceof FreeActionDie)) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.943"));
        } else {
            this.game.talker.enqueue("$" + Game.prefs.nick + " changes " + this.game.selection.contents() + " to " + e.getActionCommand().substring(e.getActionCommand().length() - 1) + " ");
        }
    }

    private static void actionPerformed_openguild() {
        URI uri;
        try {
            uri = new URI("https://boardgamegeek.com/guild/1733");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            uri = null;
        }
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void actionPerformed_kill() {
        if (this.game.selection.numPieces() != 0 || this.game.controls.chat == null || !this.game.controls.chat.jtf.isFocusOwner()) {
            boolean DarknessTokenPresent = false;
            for (int c = 0; c < this.game.selection.numPieces(); c++) {
                if ((this.game.selection.get(c) instanceof Chit) && this.game.selection.get(c).type().equals(Messages.getString("Controls.69"))) {
                    DarknessTokenPresent = true;
                }
            }
            if (DarknessTokenPresent && Game.areas[4].numPieces() == this.game.selection.numPieces()) {
                if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.293"), Messages.getString("Controls.294"), 0, 2) == 0) {
                    for (int i = 0; i < Game.areas.length; i++) {
                        for (int j = 0; j < Game.areas[i].numPieces(); j++) {
                            GamePiece gp = Game.areas[i].get(j);
                            if ((gp instanceof ShadowDarknessCard) || (gp instanceof UnitNazgul)) {
                                this.game.selection.addPiece(gp);
                            }
                        }
                    }
                } else {
                    return;
                }
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.296") + this.game.selection.contents() + Messages.getKeyString("Controls.297") + this.game.highlight.name() + " ");
            return;
        }
        String s = this.game.controls.chat.jtf.getText();
        if (!s.equals("")) {
            int pos = this.game.controls.chat.jtf.getCaretPosition();
            this.game.controls.chat.jtf.setText(String.valueOf(s.substring(0, pos)) + this.game.controls.chat.jtf.getText().substring(pos + 1));
            this.game.controls.chat.jtf.setCaretPosition(pos);
        }
    }

    private void actionPerformed_dkill() {
        Area loc = this.game.highlight;
        ArrayList<GamePiece> pieces = this.game.selection.pieces();
        int[] nations = new int[15];
        int totalElite = 0;
        int totalRegular = 0;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) instanceof Elite) {
                int nation = pieces.get(i).nation() + 5;
                nations[nation] = nations[nation] + 1;
                totalElite++;
            }
            if (pieces.get(i) instanceof Regular) {
                int nation2 = pieces.get(i).nation() + 5;
                nations[nation2] = nations[nation2] + 1;
                totalRegular++;
            }
        }
        if (pieces.size() == totalElite || pieces.size() == totalRegular) {
            if (totalElite > 0) {
                downgradeElites(loc, nations);
            }
            if (totalRegular > 0) {
                upgradeRegulars(loc, nations);
                return;
            }
            return;
        }
        JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.70"));
    }

    private void actionPerformed_flip() {
        new ArrayList();
        Area temp = new Area("temp");
        Area tempHolding = new Area("Holding");
        Area tempSelectArea = new Area("temp2");
        if (this.game.hasFPpassword() || this.game.hasSPpassword()) {
            for (int i = 0; i <= this.game.selection.numPieces() - 1; i++) {
                if ((this.game.selection.get(i) instanceof UnitRecruitmentToken) && ((TwoChit) this.game.selection.get(i)).currentState()) {
                    temp.clearAllPieces();
                    for (int p = 0; p <= this.game.bits.length - 1; p++) {
                        GamePiece gp = this.game.bits[p];
                        if ((gp instanceof UnitRecruitmentToken) && gp.nation() == this.game.selection.get(i).nation() && !gp.currentLocation.equals(Game.areas[175]) && !gp.currentLocation.equals(Game.areas[177]) && ((TwoChit) gp).currentState() && !tempHolding.containsPiece(gp)) {
                            temp.addPiece(gp);
                        }
                    }
                    if (this.game.selection.numPieces() > 1) {
                        JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.154"));
                        return;
                    } else if (temp.numPieces() <= 0) {
                        continue;
                    } else if (this.game.talker.connected) {
                        actionPerformed_SecureRoll(1, temp.numPieces(), "flip token " + this.game.selection.get(0).id());
                        return;
                    } else {
                        tempHolding.addPiece((TwoChit) temp.randomPiece());
                    }
                }
            }
            tempSelectArea.addAllPieces(this.game.selection);
            temp.clearAllPieces();
            for (int p2 = 0; p2 <= tempHolding.numPieces() - 1; p2++) {
                if (this.game.selection.containsPiece(tempHolding.get(p2))) {
                    temp.addPiece(tempHolding.get(p2));
                }
            }
            for (int p3 = 0; p3 <= tempHolding.numPieces() - 1; p3++) {
                if (!temp.containsPiece(tempHolding.get(p3))) {
                    for (int i2 = 0; i2 <= tempSelectArea.numPieces() - 1; i2++) {
                        if (!tempHolding.containsPiece(tempSelectArea.get(i2))) {
                            Area fliplocation = tempHolding.get(p3).currentLocation();
                            GamePiece sparegp = tempSelectArea.get(i2);
                            this.game.talker.enqueue("$base silent ");
                            this.game.talker.enqueue("<auto><~Scribe.141~>" + tempHolding.get(p3) + " <~Scribe.142~>" + tempHolding.get(p3).currentLocation() + "<~Scribe.143~>" + sparegp.currentLocation() + "<~Scribe.144~>");
                            this.game.talker.enqueue("<auto><~Scribe.141~>" + sparegp + " <~Scribe.142~>" + sparegp.currentLocation() + "<~Scribe.143~>" + fliplocation + "<~Scribe.144~>");
                            this.game.talker.enqueue("$base noisy ");
                            temp.addPiece(tempHolding.get(p3));
                        }
                    }
                }
            }
            if (temp.numPieces() > 0) {
                this.game.selection.clearAllPieces();
                for (int i3 = 0; i3 <= tempHolding.numPieces() - 1; i3++) {
                    this.game.selection.addPiece(tempHolding.get(i3));
                }
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.323") + this.game.selection.contents());
            return;
        }
        JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.544"));
    }

    private void actionPerformed_server() {
        JDialog jd = new JDialog(Game.win, Messages.getString("Controls.325"));
        JTextField tf = new JTextField(new Integer(Game.prefs.port).toString());
        tf.setSize(65, 25);
        tf.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf);
        JButton jb = new JButton(Messages.getString("Controls.326"));
        jb.setActionCommand("wait");
        jb.setSize(200, 35);
        jb.addActionListener(ConnectionDialogHandler.getInstance(this.game, tf, (JTextField) null, jd));
        jd.getContentPane().add(jb);
        jd.setSize(305, 70);
        jd.setLocation((Game.win.getWidth() - 65) / 2, (Game.win.getHeight() - 25) / 2);
        jd.setLayout(new FlowLayout());
        jd.setVisible(true);
    }

    private void actionPerformed_hostobs() {
        JDialog jd = new JDialog(Game.win, Messages.getString("Controls.329"));
        JTextField tf = new JTextField("5555");
        tf.setSize(65, 25);
        tf.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf);
        JButton jb = new JButton(Messages.getString("Controls.331"));
        jb.setActionCommand("wait");
        jb.setSize(200, 35);
        jb.addActionListener(ObserverDialogHandler.getInstance(this.game, tf, (JTextField) null, jd));
        jd.getContentPane().add(jb);
        jd.setSize(305, 70);
        jd.setLocation(555, 555);
        jd.setLayout(new FlowLayout());
        jd.setVisible(true);
    }

    private void actionPerformed_connectobs() {
        JDialog jd = new JDialog(Game.win, Messages.getString("Controls.334"));
        JTextField tf = new JTextField("5555");
        tf.setSize(65, 25);
        tf.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf);
        JTextField tf2 = new JTextField(Game.prefs.addr);
        tf2.setSize(130, 25);
        tf2.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf2);
        JButton jb = new JButton(Messages.getString("Controls.336"));
        jb.setActionCommand("connect");
        jb.setSize(200, 35);
        jb.addActionListener(ObserverDialogHandler.getInstance(this.game, tf, tf2, jd));
        jd.getContentPane().add(jb);
        jd.setSize(420, 70);
        jd.setLocation(555, 555);
        jd.setLayout(new FlowLayout());
        jd.setVisible(true);
    }

    private void actionPerformed_client() {
        JDialog jd = new JDialog(Game.win, Messages.getString("Controls.339"));
        JTextField tf = new JTextField(new Integer(Game.prefs.port).toString());
        tf.setSize(65, 25);
        tf.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf);
        JTextField tf2 = new JTextField(Game.prefs.addr);
        tf2.setSize(130, 25);
        tf2.setBorder(new LineBorder(Color.black, 1));
        jd.getContentPane().add(tf2);
        JButton jb = new JButton(Messages.getString("Controls.340"));
        jb.setActionCommand("connect");
        jb.setSize(200, 35);
        jb.addActionListener(ConnectionDialogHandler.getInstance(this.game, tf, tf2, jd));
        jd.getContentPane().add(jb);
        jd.setSize(420, 70);
        jd.setLocation(555, 555);
        jd.setLayout(new FlowLayout());
        jd.setVisible(true);
    }

    private void actionPerformed_rollActionDice() {
        Area temp = new Area("temp");
        ArrayList<GamePiece> list = this.game.selection.pieces();
        for (int i = list.size() - 1; i >= 0; i--) {
            GamePiece g = list.get(i);
            if (g instanceof ActionDie) {
                temp.addPiece(g);
            }
        }
        if (temp.numPieces() != 0) {
            if (this.game.talker.connected) {
                actionPerformed_SecureRoll(temp.numPieces(), 6, "roll ACTION dice " + temp.contents());
                return;
            }
            String roll = temp.contentRolls();
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.345") + temp.contents() + Messages.getKeyString("Controls.346") + roll);
            if (Game.boardtype.equals("wotr")) {
                String eyeResult = new Integer(15).toString();
                if (this.game.highlight == Game.areas[179] && (temp.numPieces() != 1 || roll.contains(eyeResult))) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.350") + temp.contents());
                }
            }
        }
    }

    private void actionPerformed_ring() {
        boolean freeRing;
        Chit ring;
        Chit ring2 = null;
        Area origin = null;
        Area destination;
        Object die = null;
        if (Game.areas[152].numPieces() > 0) {
            die = Game.areas[152].get(0);
        }
        if (die != null && (die instanceof ShadowActionDie)) {
            freeRing = false;
            Area temp = new Area("ring temp");
            temp.addAllPieces(Game.areas[152]);
            temp.addAllPieces(Game.areas[154]);
            temp.addAllPieces(Game.areas[153]);
            int i = 0;
            while (true) {
                if (i < temp.numPieces()) {
                    if ((temp.get(i) instanceof HuntTile) && ((HuntTile) temp.get(i)).type().equals("Eye")) {
                        freeRing = true;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
        } else if (die != null && (die instanceof FreeActionDie)) {
            freeRing = true;
        } else if (!this.game.hasSPpassword() || this.game.hasFPpassword()) {
            freeRing = true;
        } else {
            freeRing = false;
        }
        if (freeRing) {
            Area[] areas = Game.areas;
            origin = areas[127];
            if (origin.numPieces() <= 0 || !(origin.get(0) instanceof Chit) || !isElvenRing((Chit) origin.get(0))) {
                origin = areas[126];
                if (origin.numPieces() <= 0 || !(origin.get(0) instanceof Chit) || !isElvenRing((Chit) origin.get(0))) {
                    origin = areas[125];
                    if (origin.numPieces() > 0 && (origin.get(0) instanceof Chit) && isElvenRing((Chit) origin.get(0))) {
                        ring2 = (Chit) origin.get(0);
                    } else {
                        return;
                    }
                } else {
                    ring2 = (Chit) origin.get(0);
                }
            } else {
                ring2 = (Chit) origin.get(0);
            }
            destination = areas[128];
            if (destination.numPieces() != 0) {
                destination = areas[129];
                if (destination.numPieces() != 0) {
                    destination = areas[130];
                }
            }
        } else {
            Area[] areas2 = Game.areas;
            Area origin2 = areas2[130];
            if (origin2.numPieces() <= 0 || !(origin2.get(0) instanceof Chit) || !isElvenRing((Chit) origin2.get(0))) {
                origin2 = areas2[129];
                if (origin2.numPieces() <= 0 || !(origin2.get(0) instanceof Chit) || !isElvenRing((Chit) origin2.get(0))) {
                    origin2 = areas2[128];
                    if (origin2.numPieces() > 0 && (origin2.get(0) instanceof Chit) && isElvenRing((Chit) origin2.get(0))) {
                        ring = (Chit) origin2.get(0);
                    } else {
                        return;
                    }
                } else {
                    ring = (Chit) origin2.get(0);
                }
            } else {
                ring = (Chit) origin2.get(0);
            }
            destination = areas2[177];
        }
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.366") + ring2 + Messages.getKeyString("Controls.367") + origin + Messages.getKeyString("Controls.368") + destination + " ");
    }

    private void actionPerformed_ringVilya() {
        boolean freeRing;
        Area origin;
        TwoChit ring;
        Area destination;
        Object die = null;
        if (Game.areas[152].numPieces() > 0) {
            die = Game.areas[152].get(0);
        }
        if (die != null && (die instanceof ShadowActionDie)) {
            freeRing = false;
        } else if (die != null && (die instanceof FreeActionDie)) {
            freeRing = true;
        } else if (!this.game.hasSPpassword() || this.game.hasFPpassword()) {
            freeRing = true;
        } else {
            freeRing = false;
        }
        if (freeRing) {
            Area[] areas = Game.areas;
            origin = areas[125];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas[128];
            } else {
                return;
            }
        } else {
            Area[] areas2 = Game.areas;
            origin = areas2[128];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas2[177];
            } else {
                return;
            }
        }
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.366") + ring + Messages.getKeyString("Controls.367") + origin + Messages.getKeyString("Controls.368") + destination + " ");
        if (freeRing) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.323") + ring);
        }
    }

    private void actionPerformed_ringNenya() {
        boolean freeRing;
        Area origin;
        TwoChit ring;
        Area destination;
        Object die = null;
        if (Game.areas[152].numPieces() > 0) {
            die = Game.areas[152].get(0);
        }
        if (die != null && (die instanceof ShadowActionDie)) {
            freeRing = false;
        } else if (die != null && (die instanceof FreeActionDie)) {
            freeRing = true;
        } else if (!this.game.hasSPpassword() || this.game.hasFPpassword()) {
            freeRing = true;
        } else {
            freeRing = false;
        }
        if (freeRing) {
            Area[] areas = Game.areas;
            origin = areas[126];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas[129];
            } else {
                return;
            }
        } else {
            Area[] areas2 = Game.areas;
            origin = areas2[129];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas2[177];
            } else {
                return;
            }
        }
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.366") + ring + Messages.getKeyString("Controls.367") + origin + Messages.getKeyString("Controls.368") + destination + " ");
        if (freeRing) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.323") + ring);
        }
    }

    private void actionPerformed_ringNarya() {
        boolean freeRing;
        Area origin;
        TwoChit ring;
        Area destination;
        Object die = null;
        if (Game.areas[152].numPieces() > 0) {
            die = Game.areas[152].get(0);
        }
        if (die != null && (die instanceof ShadowActionDie)) {
            freeRing = false;
        } else if (die != null && (die instanceof FreeActionDie)) {
            freeRing = true;
        } else if (!this.game.hasSPpassword() || this.game.hasFPpassword()) {
            freeRing = true;
        } else {
            freeRing = false;
        }
        if (freeRing) {
            Area[] areas = Game.areas;
            origin = areas[127];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas[130];
            } else {
                return;
            }
        } else {
            Area[] areas2 = Game.areas;
            origin = areas2[130];
            if (origin.numPieces() > 0 && (origin.get(0) instanceof TwoChit) && isElvenRing((TwoChit) origin.get(0))) {
                ring = (TwoChit) origin.get(0);
                destination = areas2[177];
            } else {
                return;
            }
        }
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.366") + ring + Messages.getKeyString("Controls.367") + origin + Messages.getKeyString("Controls.368") + destination + " ");
        if (freeRing) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.323") + ring);
        }
    }

    private void actionPerformed_eye() {
        ArrayList<GamePiece> list = Game.areas[179].pieces();
        for (int i = 0; i < list.size(); i++) {
            GamePiece g = list.get(i);
            if (g instanceof ShadowActionDie) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.372") + g + " ");
                return;
            }
        }
    }

    private void actionPerformed_stats() {
        Font statFont = new Font("Monospaced", 0, 12);
        JDialog statsDialog = new JDialog(Game.win, "Statistics");
        float totaldice = 0.0f;
        DecimalFormat diceformat = new DecimalFormat("##0.00");
        DecimalFormat avgdiceformat = new DecimalFormat("+###;-###");
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        jta.setFont(statFont);
        jta.setBackground(new Color(140, 140, 255));
        for (int i : FreeActionDie.stats) {
            totaldice += (float) i;
        }
        float averagedice = totaldice / 6.0f;
        jta.append(String.valueOf(Messages.getString("Controls.4")) + ((int) totaldice) + Messages.getString("Controls.41"));
        jta.append(String.valueOf(diceformat.format((double) averagedice)) + Messages.getString("Controls.42"));
        for (int i2 = 0; i2 < FreeActionDie.stats.length; i2++) {
            if (i2 == 1) {
                jta.append("  " + FreeActionDie.GetChar(i2) + ": " + FreeActionDie.stats[i2] + " (" + avgdiceformat.format((double) (((float) FreeActionDie.stats[i2]) - (2.0f * averagedice))) + ")");
            } else {
                jta.append("  " + FreeActionDie.GetChar(i2) + ": " + FreeActionDie.stats[i2] + " (" + avgdiceformat.format((double) (((float) FreeActionDie.stats[i2]) - averagedice)) + ")");
            }
            if (i2 != 5) {
                jta.append("\n");
            }
        }
        if (Game.varianttype.startsWith("expansion2")) {
            jta.append(String.valueOf(Messages.getString("Controls.929")) + VilyaDice.reportResults + "\n");
            jta.append(String.valueOf(Messages.getString("Controls.925")) + NenyaDice.reportResults + "\n");
            jta.append(String.valueOf(Messages.getString("Controls.926")) + NaryaDice.reportResults + "\n");
        }
        statsDialog.getContentPane().add(jta);
        JTextArea jta2 = new JTextArea();
        jta2.setEditable(false);
        jta2.setFont(statFont);
        jta2.setBackground(new Color(255, 140, 140));
        float totaldice2 = 0.0f;
        for (int i3 : ShadowActionDie.stats) {
            totaldice2 += (float) i3;
        }
        float averagedice2 = totaldice2 / 6.0f;
        jta2.append(String.valueOf(Messages.getString("Controls.125")) + ((int) totaldice2) + Messages.getString("Controls.127"));
        jta2.append(String.valueOf(diceformat.format((double) averagedice2)) + Messages.getString("Controls.129"));
        for (int i4 = 0; i4 < ShadowActionDie.stats.length; i4++) {
            jta2.append("  " + ShadowActionDie.GetChar(i4 + 10) + ": " + ShadowActionDie.stats[i4] + " (" + avgdiceformat.format((double) (((float) ShadowActionDie.stats[i4]) - averagedice2)) + ")");
            jta2.append("\n");
        }
        if (Game.varianttype.startsWith("expansion2")) {
            jta2.append(String.valueOf(Messages.getString("Controls.927")) + BalrogDice.reportResults + "\n");
            jta2.append(String.valueOf(Messages.getString("Controls.928")) + GothmogDice.reportResults + "\n");
        }
        if (Game.boardtype.equals("wotr")) {
            jta2.append(String.valueOf(Messages.getString("Controls.133")) + (ShadowActionDie.allocations - ShadowActionDie.stats[5]));
        }
        statsDialog.getContentPane().add(jta2);
        JTextArea jta3 = new JTextArea();
        jta3.setEditable(false);
        jta3.setFont(statFont);
        jta3.setBackground(new Color(140, 255, 140));
        float totaldice3 = 0.0f;
        for (int i5 = 0; i5 < Game.D6statsN(true); i5++) {
            totaldice3 += (float) Game.D6stats(true, i5);
        }
        float averagedice3 = totaldice3 / 6.0f;
        jta3.append(String.valueOf(Messages.getString("Controls.134")) + ((int) totaldice3) + Messages.getString("Controls.135"));
        jta3.append(String.valueOf(diceformat.format((double) averagedice3)) + Messages.getString("Controls.136"));
        for (int i6 = 0; i6 < Game.D6statsN(true); i6++) {
            jta3.append("  " + (i6 + 1) + ": " + Game.D6stats(true, i6) + " (" + avgdiceformat.format((double) (((float) Game.D6stats(true, i6)) - averagedice3)) + ")");
            if (i6 != 5) {
                jta3.append("\n");
            }
        }
        statsDialog.getContentPane().add(jta3);
        JTextArea jta4 = new JTextArea();
        jta4.setEditable(false);
        jta4.setFont(statFont);
        jta4.setBackground(new Color(200, 200, 200));
        float totaldice4 = 0.0f;
        for (int i7 = 0; i7 < Game.D6statsN(false); i7++) {
            totaldice4 += (float) Game.D6stats(false, i7);
        }
        float averagedice4 = totaldice4 / 6.0f;
        jta4.append(String.valueOf(Messages.getString("Controls.139")) + ((int) totaldice4) + Messages.getString("Controls.140"));
        jta4.append(String.valueOf(diceformat.format((double) averagedice4)) + Messages.getString("Controls.142"));
        for (int i8 = 0; i8 < Game.D6statsN(false); i8++) {
            jta4.append("  " + (i8 + 1) + ": " + Game.D6stats(false, i8) + " (" + avgdiceformat.format((double) (((float) Game.D6stats(false, i8)) - averagedice4)) + ")");
            if (i8 != 5) {
                jta4.append("\n");
            }
        }
        statsDialog.getContentPane().add(jta4);
        statsDialog.setSize(650, 350);
        statsDialog.setLocation((Game.win.getX() + (Game.win.getWidth() / 2)) - (statsDialog.getWidth() / 2), (Game.win.getY() + (Game.win.getHeight() / 2)) - (statsDialog.getHeight() / 2));
        statsDialog.setLayout(new FlowLayout());
        statsDialog.setVisible(true);
    }

    private void actionPerformed_cards() {
        Font font = new Font("Monospaced", 0, 12);
        JDialog statsDialog = new JDialog(Game.win, "Statistics");
        JTextArea jta = new JTextArea();
        JScrollPane jtp = new JScrollPane(jta);
        jta.setEditable(false);
        jta.setFont(font);
        jta.setBackground(new Color(140, 140, 255));
        jta.append(String.valueOf(Messages.getString("Controls.213")) + "\n");
        int freeChar = 0;
        int lines = 0;
        for (int i = 0; i < Game.areas[175].numPieces(); i++) {
            GamePiece p = Game.areas[175].get(i);
            if (p instanceof FreeCharacterCard) {
                if (((Card) p).played || this.game.hasFPpassword()) {
                    if (lines == 0) {
                        jta.append(String.valueOf(Messages.getString("Controls.217")) + "\n");
                    }
                    if (!((Card) p).played) {
                        jta.append(String.valueOf(((Card) p).name()) + Messages.getString("Controls.152") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p).name()) + "\n");
                    }
                    lines++;
                } else {
                    freeChar++;
                }
            }
        }
        if (freeChar > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.225")) + freeChar + "\n");
        }
        int freeStrat = 0;
        int currlines = lines;
        for (int i2 = 0; i2 < Game.areas[175].numPieces(); i2++) {
            GamePiece p2 = Game.areas[175].get(i2);
            if (p2 instanceof FreeStrategyCard) {
                if (((Card) p2).played || this.game.hasFPpassword()) {
                    if (lines == currlines) {
                        jta.append("\n" + Messages.getString("Controls.228") + "\n");
                    }
                    if (!((Card) p2).played) {
                        jta.append(String.valueOf(((Card) p2).name()) + Messages.getString("Controls.230") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p2).name()) + "\n");
                    }
                    lines++;
                } else {
                    freeStrat++;
                }
            }
        }
        if (freeStrat > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.233")) + freeStrat + "\n");
        }
        int currlines2 = lines;
        for (int i3 = 0; i3 < Game.areas[175].numPieces(); i3++) {
            GamePiece p3 = Game.areas[175].get(i3);
            if ((p3 instanceof FateCard) && (((Card) p3).played || this.game.hasFPpassword())) {
                if (lines == currlines2) {
                    jta.append(String.valueOf(Messages.getString("Controls.235")) + "\n");
                }
                if (!((Card) p3).played) {
                    jta.append(String.valueOf(((Card) p3).name()) + Messages.getString("Controls.239") + "\n");
                } else {
                    jta.append(String.valueOf(((Card) p3).name()) + "\n");
                }
                lines++;
            }
        }
        int freeStory = 0;
        int currlines3 = lines;
        for (int i4 = 0; i4 < Game.areas[175].numPieces(); i4++) {
            GamePiece p4 = Game.areas[175].get(i4);
            if (p4 instanceof FreeStoryCard) {
                if (((Card) p4).played || this.game.hasFPpassword()) {
                    if (lines == currlines3) {
                        jta.append(String.valueOf(Messages.getString("Controls.245")) + "\n");
                    }
                    if (!((Card) p4).played) {
                        jta.append(String.valueOf(((Card) p4).name()) + Messages.getString("Controls.249") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p4).name()) + "\n");
                    }
                    lines++;
                } else {
                    freeStory++;
                }
            }
        }
        if (freeStory > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.255")) + freeStory + "\n");
        }
        int freeGeneric = 0;
        int currlines4 = lines;
        for (int i5 = 0; i5 < Game.areas[175].numPieces(); i5++) {
            GamePiece p5 = Game.areas[175].get(i5);
            if (p5 instanceof GenericCard) {
                if (((GenericCard) p5).played || this.game.hasFPpassword()) {
                    if (lines == currlines4) {
                        jta.append(String.valueOf(Messages.getString("Controls.259")) + "\n");
                    }
                    if (!((GenericCard) p5).played) {
                        jta.append(String.valueOf(((GenericCard) p5).name()) + Messages.getString("Controls.263") + "\n");
                    } else {
                        jta.append(String.valueOf(((GenericCard) p5).name()) + "\n");
                    }
                    lines++;
                } else {
                    freeGeneric++;
                }
            }
        }
        if (freeGeneric > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.266")) + freeGeneric + "\n");
        }
        jta.append("\n" + Messages.getString("Controls.269") + "\n");
        int shadowChar = 0;
        int currlines5 = lines;
        for (int i6 = 0; i6 < Game.areas[177].numPieces(); i6++) {
            GamePiece p6 = Game.areas[177].get(i6);
            if (p6 instanceof ShadowCharacterCard) {
                if (((Card) p6).played || this.game.hasSPpassword()) {
                    if (lines == currlines5) {
                        jta.append(String.valueOf(Messages.getString("Controls.274")) + "\n");
                    }
                    if (!((Card) p6).played) {
                        jta.append(String.valueOf(((Card) p6).name()) + Messages.getString("Controls.279") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p6).name()) + "\n");
                    }
                    lines++;
                } else {
                    shadowChar++;
                }
            }
        }
        if (shadowChar > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.289")) + shadowChar + "\n");
        }
        int shadowStrat = 0;
        int currlines6 = lines;
        for (int i7 = 0; i7 < Game.areas[177].numPieces(); i7++) {
            GamePiece p7 = Game.areas[177].get(i7);
            if (p7 instanceof ShadowStrategyCard) {
                if (((Card) p7).played || this.game.hasSPpassword()) {
                    if (lines == currlines6) {
                        jta.append("\n" + Messages.getString("Controls.298") + "\n");
                    }
                    if (!((Card) p7).played) {
                        jta.append(String.valueOf(((Card) p7).name()) + Messages.getString("Controls.302") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p7).name()) + "\n");
                    }
                    lines++;
                } else {
                    shadowStrat++;
                }
            }
        }
        if (shadowStrat > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.311")) + shadowStrat + "\n");
        }
        int shadowStory = 0;
        int currlines7 = lines;
        for (int i8 = 0; i8 < Game.areas[177].numPieces(); i8++) {
            GamePiece p8 = Game.areas[177].get(i8);
            if (p8 instanceof ShadowStoryCard) {
                if (((Card) p8).played || this.game.hasSPpassword()) {
                    if (lines == currlines7) {
                        jta.append(String.valueOf(Messages.getString("Controls.315")) + "\n");
                    }
                    if (!((Card) p8).played) {
                        jta.append(String.valueOf(((Card) p8).name()) + Messages.getString("Controls.317") + "\n");
                    } else {
                        jta.append(String.valueOf(((Card) p8).name()) + "\n");
                    }
                    lines++;
                } else {
                    shadowStory++;
                }
            }
        }
        if (shadowStory > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.320")) + shadowStory + "\n");
        }
        int shadowGeneric = 0;
        int currlines8 = lines;
        for (int i9 = 0; i9 < Game.areas[177].numPieces(); i9++) {
            GamePiece p9 = Game.areas[177].get(i9);
            if (p9 instanceof GenericCard) {
                if (((GenericCard) p9).played || this.game.hasSPpassword()) {
                    if (lines == currlines8) {
                        jta.append(String.valueOf(Messages.getString("Controls.327")) + "\n");
                    }
                    if (!((GenericCard) p9).played) {
                        jta.append(String.valueOf(((GenericCard) p9).name()) + Messages.getString("Controls.330") + "\n");
                    } else {
                        jta.append(String.valueOf(((GenericCard) p9).name()) + "\n");
                    }
                    lines++;
                } else {
                    shadowGeneric++;
                }
            }
        }
        if (shadowGeneric > 0) {
            jta.append(String.valueOf(Messages.getString("Controls.335")) + shadowGeneric + "\n");
        }
        statsDialog.getContentPane().add(jtp);
        if ((lines * 20) + 220 < Game.win.getHeight()) {
            statsDialog.setSize(650, (lines * 20) + 220);
        } else {
            statsDialog.setSize(650, Game.win.getHeight());
        }
        statsDialog.setVisible(true);
    }

    private void actionPerformed_challengestats() {
        Font statFont = new Font("Monospaced", 0, 12);
        JDialog statsDialog = new JDialog(Game.win, "Challenge Statistics");
        JTextArea jta = new JTextArea();
        JScrollPane jtp = new JScrollPane(jta);
        jta.setEditable(false);
        jta.setFont(statFont);
        jta.setBackground(new Color(140, 140, 255));
        jta.append(String.valueOf(Messages.getString("Controls.162")) + "\n");
        for (int i = 0; i < this.game.challengeShadowAction.length(); i += 3) {
            if (i == this.game.challengeShadowActionPos) {
                jta.append("--");
            }
            jta.append(Character.toString(ShadowActionDie.GetChar(Integer.parseInt(this.game.challengeShadowAction.substring(i + 1, i + 3)))));
        }
        jta.append("\n");
        jta.append(String.valueOf(Messages.getString("Controls.168")) + "\n");
        for (int i2 = 0; i2 < this.game.challengeShadowCombat.length(); i2 += 2) {
            if (i2 == this.game.challengeShadowCombatPos) {
                jta.append("--");
            }
            jta.append(this.game.challengeShadowCombat.substring(i2 + 1, i2 + 2));
        }
        jta.append("\n");
        jta.append(String.valueOf(Messages.getString("Controls.175")) + "\n");
        for (int i3 = 0; i3 < this.game.challengeFreeAction.length(); i3 += 2) {
            if (i3 == this.game.challengeFreeActionPos) {
                jta.append("--");
            }
            jta.append(Character.toString(FreeActionDie.GetChar(Integer.parseInt(this.game.challengeFreeAction.substring(i3 + 1, i3 + 2)))));
        }
        jta.append("\n");
        jta.append(String.valueOf(Messages.getString("Controls.185")) + "\n");
        for (int i4 = 0; i4 < this.game.challengeFreeCombat.length(); i4 += 2) {
            if (i4 == this.game.challengeFreeCombatPos) {
                jta.append("--");
            }
            jta.append(this.game.challengeFreeCombat.substring(i4 + 1, i4 + 2));
        }
        jta.append("\n");
        statsDialog.getContentPane().add(jtp);
        if (300 < Game.win.getHeight()) {
            statsDialog.setSize(650, 300);
        } else {
            statsDialog.setSize(650, Game.win.getHeight());
        }
        statsDialog.setVisible(true);
    }

    private void actionPerformed_load() {
        while (this.controls.f3fc.showOpenDialog(Game.win) == 0) {
            File f = this.controls.f3fc.getSelectedFile();
            if (("logs/" + f.getName()).equals(this.game.interpreter.GetLogName())) {
                try {
                    JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.395"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                if (Game.boardtype.equals("wotr")) {
                    this.game.resetBoard();
                }
                this.game.gameloading = true;
                this.game.interpreter.setLogFile(Game.generateLogName());
                if (this.game.GetFPpassword() != null) {
                    this.game.HideFPhand();
                    this.game.ClearFPDecks();
                }
                if (this.game.GetSPpassword() != null) {
                    this.game.HideSPhand();
                    this.game.ClearSPDecks();
                }
                this.game.interpreter.loadFile(f);
                this.game.redoTitle();
                this.game.gameloading = false;
                this.game.talker.enqueuedirect("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.397"), (ObserverHost) null);
                return;
            }
        }
    }

    private void actionPerformed_loadChallengeFile() {
        while (this.controls.f3fc.showOpenDialog(Game.win) == 0) {
            File f = this.controls.f3fc.getSelectedFile();
            if (("logs/" + f.getName()).equals(this.game.interpreter.GetLogName())) {
                try {
                    JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.395"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                this.game.talker.enqueue("<auto> silent ");
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    while (br.ready()) {
                        String newLine = Messages.removeKeyString(br.readLine());
                        if (newLine.startsWith("$") && (newLine.contains(Messages.getString("Controls.479")) || newLine.contains(Messages.getString("Controls.464")))) {
                            this.game.interpreter.execute(newLine);
                        }
                        if (newLine.startsWith("<game>") && newLine.contains("rolls")) {
                            if (newLine.startsWith("<game> " + this.game.shadowname)) {
                                Game game2 = this.game;
                                game2.challengeShadowCombat = String.valueOf(game2.challengeShadowCombat) + newLine.substring(newLine.indexOf("rolls") + 5);
                            } else {
                                Game game3 = this.game;
                                game3.challengeFreeCombat = String.valueOf(game3.challengeFreeCombat) + newLine.substring(newLine.indexOf("rolls") + 5);
                            }
                        }
                        if (newLine.startsWith("$") && newLine.contains("rolls") && newLine.contains("as")) {
                            if (newLine.startsWith("$" + this.game.shadowname)) {
                                Game game4 = this.game;
                                game4.challengeShadowAction = String.valueOf(game4.challengeShadowAction) + newLine.substring(newLine.indexOf("as") + 2);
                            } else {
                                Game game5 = this.game;
                                game5.challengeFreeAction = String.valueOf(game5.challengeFreeAction) + newLine.substring(newLine.indexOf("as") + 2);
                            }
                        }
                    }
                    this.game.talker.enqueue("<auto> noisy ");
                    this.game.talker.enqueue("$" + Game.prefs.nick + " challengeswith" + this.game.challengeShadowCombat + ":" + this.game.challengeShadowAction + ":" + this.game.challengeFreeCombat + ":" + this.game.challengeFreeAction + ": ");
                    this.game.challengeShadowCombatPos = 0;
                    this.game.challengeFreeCombatPos = 0;
                    this.game.challengeShadowActionPos = 0;
                    this.game.challengeFreeActionPos = 0;
                    br.close();
                    return;
                } catch (FileNotFoundException e) {
                    System.out.println(String.valueOf(Messages.getString("Interpreter.0")) + e);
                    return;
                } catch (IOException e2) {
                    System.out.println(String.valueOf(Messages.getString("Interpreter.1")) + e2);
                    return;
                }
            }
        }
    }

    private void actionPerformed_quit() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.402"), Messages.getString("Controls.403"), 0, 2) == 0) {
            this.game.exit();
        }
    }

    private void actionPerformed_changeNick() {
        String old = Game.prefs.nick;
        String newnick = JOptionPane.showInputDialog(Messages.getString("Controls.435"));
        if (newnick.equals("game") || newnick.equals("<game>") || newnick.equals(Messages.getString("Controls.165")) || newnick.equals("<replay>") || newnick.equals(Messages.getString("Controls.167")) || newnick.equals("<FP>") || newnick.equals(Messages.getString("Controls.169")) || newnick.equals("<SA>") || newnick.equals("auto") || newnick.equals("<auto>") || (this.game.talker.connected && newnick.equals(this.game.opponent))) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.173"));
            return;
        }
        Game.prefs.setNick(newnick);
        this.game.talker.enqueuedirect("<game> " + old + Messages.getKeyString("Controls.437") + Game.prefs.nick + ".", (ObserverHost) null);
        this.game.redoTitle();
    }

    private void actionPerformed_disconnect() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.442"), Messages.getString("Controls.443"), 0, 2) == 0) {
            this.game.talker.disconnect(false);
        }
    }

    private void actionPerformed_disconnectObs() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.445"), Messages.getString("Controls.446"), 0, 2) == 0) {
            this.game.disconnectAllObservers();
        }
    }

    private void actionPerformed_fsmConnect() {
        // Prompt for FSM server host and port
        String host = JOptionPane.showInputDialog(Game.win, 
            "Enter FSM Server Host:", "localhost");
        if (host == null || host.trim().isEmpty()) {
            return; // User cancelled
        }
        
        String portStr = JOptionPane.showInputDialog(Game.win, 
            "Enter FSM Server Port:", "8080");
        if (portStr == null || portStr.trim().isEmpty()) {
            return; // User cancelled
        }
        
        try {
            int port = Integer.parseInt(portStr);
            boolean success = this.game.fsmConnection.connect(host.trim(), port);
            
            if (!success) {
                JOptionPane.showMessageDialog(Game.win, 
                    "Failed to connect to FSM server at " + host + ":" + port,
                    "Connection Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(Game.win, 
                "Invalid port number: " + portStr,
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionPerformed_fsmDisconnect() {
        if (this.game.fsmConnection.isConnected()) {
            if (JOptionPane.showConfirmDialog(Game.win, 
                "Disconnect from FSM server?", 
                "Confirm Disconnect", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                this.game.fsmConnection.disconnect();
            }
        } else {
            JOptionPane.showMessageDialog(Game.win, 
                "Not currently connected to FSM server",
                "Not Connected", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * FSM command: Roll all Free Peoples action dice
     * Auto-selects all dice in FP pool (area 178) and rolls them
     */
    private void actionPerformed_fsmRollFpDice() {
        rollDiceFromPool(178, "FP");
    }

    /**
     * FSM command: Roll all Shadow action dice
     * Auto-selects all dice in Shadow pool (area 179) and rolls them
     */
    private void actionPerformed_fsmRollSpDice() {
        rollDiceFromPool(179, "Shadow");
    }

    /**
     * Roll all dice from specified pool
     */
    private void rollDiceFromPool(int poolAreaIndex, String factionName) {
        Area dicePool = Game.areas[poolAreaIndex];
        Area temp = new Area("temp");
        
        // Select all action dice from the pool
        for (int i = 0; i < dicePool.numPieces(); i++) {
            GamePiece piece = dicePool.get(i);
            if (piece instanceof ActionDie) {
                temp.addPiece(piece);
            }
        }
        
        if (temp.numPieces() == 0) {
            System.out.println("[FSM] No " + factionName + " dice to roll");
            return;
        }
        
        System.out.println("[FSM] Rolling " + temp.numPieces() + " " + factionName + " dice");
        
        // Roll the dice
        if (this.game.talker.connected) {
            actionPerformed_SecureRoll(temp.numPieces(), 6, "roll " + factionName + " ACTION dice " + temp.contents());
        } else {
            String roll = temp.contentRolls();
            this.game.talker.enqueue("$" + Game.prefs.nick + " rolls " + temp.contents() + " = " + roll);
            
            // Handle special dice results for WOTR
            if (Game.boardtype.equals("wotr")) {
                String eyeResult = "15";
                if (this.game.highlight == Game.areas[179] && (temp.numPieces() != 1 || roll.contains(eyeResult))) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + " allocates eye dice " + temp.contents());
                }
            }
        }
    }

    private void actionPerformed_wwsat() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.448"), Messages.getString("Controls.449"), 0, 2) == 0) {
            Area chars = new Area("wwsat");
            for (int i = 0; i < Game.areas[180].numPieces(); i++) {
                GamePiece p = Game.areas[180].get(i);
                if (p instanceof FreeCharacterCard) {
                    chars.addPiece(p);
                }
            }
            if (chars.numPieces() != 0) {
                if (this.game.talker.connected) {
                    actionPerformed_SecureRoll(1, chars.numPieces(), "draw WWSAT");
                    return;
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.452") + chars.GetRandomPiece() + Messages.getKeyString("Controls.453") + Game.areas[180] + Messages.getKeyString("Controls.454") + Game.areas[175] + " ");
                this.game.refreshBoard();
            }
        }
    }

    public void TryToSetFPpassword(String password) {
        String message;
        if (password != null && (this.game.FPsecurityEmpty() || this.game.verifyFPPassword(password))) {
            Game.prefs.lastPasswordUsed = password;
            Game.prefs.save(Game.PREF_FILE);
            this.game.SetFPpassword(password);
        }
        if ((password == null || this.game.FPsecurityEmpty() || this.game.verifyFPPassword(password)) && !this.game.FPsecurityEmpty()) {
            String passwordHash = this.game.GetFPpasswordHash();
            String passwordSalt = this.game.GetFPpasswordSalt();
            SecureDeck characterCards = this.game.GetFPcharacterDeck();
            String[] CcardsHashes = characterCards.GetCardsHashes();
            String[] CcardsSalts = characterCards.GetCardsSalts();
            int NcharacterCards = CcardsHashes.length;
            SecureDeck strategyCards = this.game.GetFPstrategyDeck();
            String[] ScardsHashes = strategyCards.GetCardsHashes();
            String[] ScardsSalts = strategyCards.GetCardsSalts();
            int NstrategyCards = ScardsHashes.length;
            SecureDeck factionCards = this.game.GetFPfactionDeck();
            String[] FcardsHashes = factionCards.GetCardsHashes();
            String[] FcardsSalts = factionCards.GetCardsSalts();
            int NFactionCards = FcardsHashes.length;
            SecureDeck genericCards = this.game.GetGenericDeck();
            String[] GcardsHashes = genericCards.GetCardsHashes();
            String[] GcardsSalts = genericCards.GetCardsSalts();
            int NgenericCards = GcardsHashes.length;
            String message2 = "$" + Game.prefs.nick;
            if (password != null) {
                message = String.valueOf(message2) + Messages.getKeyString("Controls.464");
            } else {
                message = String.valueOf(message2) + " sendTokens FREE_PEOPLE.";
            }
            String message3 = String.valueOf(message) + passwordHash + " " + passwordSalt + " " + NcharacterCards + " ";
            for (int i = 0; i < NcharacterCards; i++) {
                message3 = String.valueOf(message3) + CcardsHashes[i] + " " + CcardsSalts[i] + " ";
            }
            String message4 = String.valueOf(message3) + NstrategyCards + " ";
            for (int i2 = 0; i2 < NstrategyCards; i2++) {
                message4 = String.valueOf(message4) + ScardsHashes[i2] + " " + ScardsSalts[i2] + " ";
            }
            String message5 = String.valueOf(message4) + NFactionCards + " ";
            for (int i3 = 0; i3 < NFactionCards; i3++) {
                message5 = String.valueOf(message5) + FcardsHashes[i3] + " " + FcardsSalts[i3] + " ";
            }
            String message6 = String.valueOf(message5) + NgenericCards + " ";
            for (int i4 = 0; i4 < NgenericCards; i4++) {
                message6 = String.valueOf(message6) + GcardsHashes[i4] + " " + GcardsSalts[i4] + " ";
            }
            this.game.talker.enqueue(message6);
            if (this.game.hasFPpassword()) {
                this.game.setViewing(true, true, true);
                this.game.MapFPcardsFromPassword();
                this.game.RevealFPcards();
            }
        }
    }

    public void TryToSetSPpassword(String password) {
        String message;
        if (password != null && (this.game.SPsecurityEmpty() || this.game.verifySPPassword(password))) {
            Game.prefs.lastPasswordUsed = password;
            Game.prefs.save(Game.PREF_FILE);
            this.game.SetSPpassword(password);
        }
        if ((password == null || this.game.SPsecurityEmpty() || this.game.verifySPPassword(password)) && !this.game.SPsecurityEmpty()) {
            String passwordHash = this.game.GetSPpasswordHash();
            String passwordSalt = this.game.GetSPpasswordSalt();
            SecureDeck characterCards = this.game.GetSPcharacterDeck();
            SecureDeck strategyCards = this.game.GetSPstrategyDeck();
            String[] CcardsHashes = characterCards.GetCardsHashes();
            String[] CcardsSalts = characterCards.GetCardsSalts();
            String[] ScardsHashes = strategyCards.GetCardsHashes();
            String[] ScardsSalts = strategyCards.GetCardsSalts();
            int NcharacterCards = CcardsHashes.length;
            int NstrategyCards = ScardsHashes.length;
            SecureDeck factionCards = this.game.GetSPfactionDeck();
            String[] FcardsHashes = factionCards.GetCardsHashes();
            String[] FcardsSalts = factionCards.GetCardsSalts();
            int NFactionCards = FcardsHashes.length;
            SecureDeck genericCards = this.game.GetGenericDeck();
            String[] GcardsHashes = genericCards.GetCardsHashes();
            String[] GcardsSalts = genericCards.GetCardsSalts();
            int NgenericCards = GcardsHashes.length;
            String message2 = "$" + Game.prefs.nick;
            if (password != null) {
                message = String.valueOf(message2) + Messages.getKeyString("Controls.479");
            } else {
                message = String.valueOf(message2) + " sendTokens SHADOW.";
            }
            String message3 = String.valueOf(message) + passwordHash + " " + passwordSalt + " " + NcharacterCards + " ";
            for (int i = 0; i < NcharacterCards; i++) {
                message3 = String.valueOf(message3) + CcardsHashes[i] + " " + CcardsSalts[i] + " ";
            }
            String message4 = String.valueOf(message3) + NstrategyCards + " ";
            for (int i2 = 0; i2 < NstrategyCards; i2++) {
                message4 = String.valueOf(message4) + ScardsHashes[i2] + " " + ScardsSalts[i2] + " ";
            }
            String message5 = String.valueOf(message4) + NFactionCards + " ";
            for (int i3 = 0; i3 < NFactionCards; i3++) {
                message5 = String.valueOf(message5) + FcardsHashes[i3] + " " + FcardsSalts[i3] + " ";
            }
            String message6 = String.valueOf(message5) + NgenericCards + " ";
            for (int i4 = 0; i4 < NgenericCards; i4++) {
                message6 = String.valueOf(message6) + GcardsHashes[i4] + " " + GcardsSalts[i4] + " ";
            }
            this.game.talker.enqueue(message6);
            if (this.game.hasSPpassword()) {
                this.game.setViewing(true, false, true);
                this.game.MapSPcardsFromPassword();
                this.game.RevealSPcards();
            }
        }
    }

    private void actionPerformed_viewfp() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.457"), Messages.getString("Controls.458"), 0, 2) == 0) {
            if (!this.game.hasFPpassword()) {
                String unecryptedPW = "";
                if (!Game.prefs.noPassword) {
                    this.game.playSound("soundpack/secret.sp1");
                    unecryptedPW = JOptionPane.showInputDialog(Messages.getString("Controls.461"), Game.prefs.lastPasswordUsed);
                }
                TryToSetFPpassword(unecryptedPW);
                if (!this.game.hasFPpassword() && !this.game.verifyFPPassword(unecryptedPW)) {
                    JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.466"));
                }
            } else {
                String neg = Messages.getKeyString("Controls.467");
                this.game.setViewing(true, true, false);
                this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.469") + neg + Messages.getKeyString("Controls.470"));
                this.game.HideFPhand();
            }
            System.out.println("Viewing FP");
            System.out.println("Variant type: " + Game.varianttype);
            System.out.println("FSP chosen: " + this.game.FSPchosen);
            if (Game.varianttype.startsWith("expansion2") && !this.game.FSPchosen) {
                this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
                this.game.talker.enqueuedirect("<auto> wait ", (ObserverHost) null);
                this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
                new ChooseFSP().display(this.game);
            }
        }
        this.game.updateStatus();
        this.game.refreshBoard();
    }

    private void actionPerformed_viewsa() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.472"), Messages.getString("Controls.473"), 0, 2) == 0) {
            if (!this.game.hasSPpassword()) {
                String unecryptedPW = "";
                if (!Game.prefs.noPassword) {
                    this.game.playSound("soundpack/secret.sp1");
                    unecryptedPW = JOptionPane.showInputDialog(Messages.getString("Controls.476"), Game.prefs.lastPasswordUsed);
                }
                TryToSetSPpassword(unecryptedPW);
                if (!this.game.hasSPpassword() && !this.game.verifySPPassword(unecryptedPW)) {
                    JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.481"));
                }
            } else {
                String neg = Messages.getKeyString("Controls.482");
                this.game.setViewing(true, false, false);
                this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.484") + neg + Messages.getKeyString("Controls.485"));
                this.game.HideSPhand();
            }
            this.game.updateStatus();
            this.game.refreshBoard();
        }
    }

    public void actionPerformed_fdrawfaction() {
        if (!this.game.talker.connected) {
            GamePiece g = Game.areas[194].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.491") + g + Messages.getKeyString("Controls.492") + Game.areas[194].name() + Messages.getKeyString("Controls.493") + Game.areas[180].name() + " ");
            }
        } else if (Game.areas[194].numPieces() > 0 || Game.areas[197].numPieces() > 0 || Game.areas[196].numPieces() > 0) {
            actionPerformed_SecureRoll(1, Game.areas[194].numPieces(), "draw FPFACTION card");
        }
    }

    public void actionPerformed_fdrawfaction2() {
        if (!this.game.talker.connected) {
            GamePiece g = Game.areas[194].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.491") + g + Messages.getKeyString("Controls.492") + Game.areas[194].name() + Messages.getKeyString("Controls.493") + Game.areas[202].name() + " ");
            }
        } else if (Game.areas[194].numPieces() > 0 || Game.areas[197].numPieces() > 0 || Game.areas[196].numPieces() > 0) {
            actionPerformed_SecureRoll(1, Game.areas[194].numPieces(), "draw FPFACTION2 card");
        }
    }

    public void actionPerformed_fdrawc() {
        if (!this.game.talker.connected || Game.areas[121].numPieces() <= 0) {
            GamePiece g = Game.areas[121].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.491") + g + Messages.getKeyString("Controls.492") + Game.areas[121].name() + Messages.getKeyString("Controls.493") + Game.areas[180].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[121].numPieces(), "draw FPCHAR card");
    }

    private void actionPerformed_fdrawstory() {
        if (!this.game.talker.connected || Game.areas[82].numPieces() <= 0) {
            GamePiece g = Game.areas[82].randomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.500") + g + Messages.getKeyString("Controls.501") + Game.areas[82].name() + Messages.getKeyString("Controls.502") + Game.areas[180].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[82].numPieces(), "draw FPSTORY card");
    }

    public void actionPerformed_fdraws() {
        if (!this.game.talker.connected || Game.areas[122].numPieces() <= 0) {
            GamePiece g = Game.areas[122].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.509") + g + Messages.getKeyString("Controls.510") + Game.areas[122].name() + Messages.getKeyString("Controls.511") + Game.areas[180].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[122].numPieces(), "draw FPSTRA card");
    }

    private void actionPerformed_sdrawstory() {
        if (!this.game.talker.connected || Game.areas[82].numPieces() <= 0) {
            GamePiece g = Game.areas[81].randomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.518") + g + Messages.getKeyString("Controls.519") + Game.areas[81].name() + Messages.getKeyString("Controls.520") + Game.areas[181].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[81].numPieces(), "draw SASTORY card");
    }

    private void actionPerformed_fdrawfate() {
        if (!this.game.talker.connected || Game.areas[0].numPieces() <= 0) {
            GamePiece g = Game.areas[0].randomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.518") + g + Messages.getKeyString("Controls.519") + Game.areas[0].name() + Messages.getKeyString("Controls.520") + Game.areas[154].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[0].numPieces(), "draw FATE card");
    }

    private void actionPerformed_fdrawgeneric() {
        GamePiece g;
        if (((this.game.hasFPpassword() && this.game.hasSPpassword()) || ((this.game.hasFPpassword() && this.game.opponentHandViewing.contains("(" + Messages.getString("Game.1") + ")")) || (this.game.hasSPpassword() && this.game.opponentHandViewing.contains("(" + Messages.getString("Game.3") + ")")))) && (g = Game.areas[80].randomPiece()) != null) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.528") + g + Messages.getKeyString("Controls.529") + Game.areas[80].name() + Messages.getKeyString("Controls.530") + Game.areas[180].name() + " ");
            this.game.talker.waitforemptyqueue();
            if (Game.areas[80].numPieces() == 0) {
                this.game.playSound("soundpack/deal.sp1");
                Area a = new Area("temp");
                ArrayList<GamePiece> temp = Game.areas[175].pieces();
                for (int i = 0; i <= temp.size() - 1; i++) {
                    GamePiece gp = temp.get(i);
                    if (gp instanceof GenericCard) {
                        a.addPiece(gp);
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.534") + a.contents() + Messages.getKeyString("Controls.535") + Game.areas[175].name() + Messages.getKeyString("Controls.536") + Game.areas[80].name() + " ");
                a.clearAllPieces();
                ArrayList<GamePiece> temp2 = Game.areas[177].pieces();
                for (int i2 = 0; i2 <= temp2.size() - 1; i2++) {
                    GamePiece gp2 = temp2.get(i2);
                    if (gp2 instanceof GenericCard) {
                        a.addPiece(gp2);
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.539") + a.contents() + Messages.getKeyString("Controls.540") + Game.areas[177].name() + Messages.getKeyString("Controls.541") + Game.areas[80].name() + " ");
            }
        }
    }

    public void actionPerformed_sdrawfaction() {
        if (!this.game.talker.connected || (Game.areas[198].numPieces() <= 0 && Game.areas[201].numPieces() <= 0 && Game.areas[200].numPieces() <= 0)) {
            GamePiece g = Game.areas[198].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.491") + g + Messages.getKeyString("Controls.492") + Game.areas[198].name() + Messages.getKeyString("Controls.493") + Game.areas[181].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[198].numPieces(), "draw SAFACTION card");
    }

    public void actionPerformed_sdrawfaction2() {
        if (!this.game.talker.connected || (Game.areas[198].numPieces() <= 0 && Game.areas[201].numPieces() <= 0 && Game.areas[200].numPieces() <= 0)) {
            GamePiece g = Game.areas[198].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.491") + g + Messages.getKeyString("Controls.492") + Game.areas[198].name() + Messages.getKeyString("Controls.493") + Game.areas[203].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[198].numPieces(), "draw SAFACTION2 card");
    }

    public void actionPerformed_sdrawc() {
        if (!this.game.talker.connected || Game.areas[123].numPieces() <= 0) {
            GamePiece g = Game.areas[123].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.0") + g + Messages.getKeyString("Controls.549") + Game.areas[123].name() + Messages.getKeyString("Controls.550") + Game.areas[181].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[123].numPieces(), "draw SACHAR card");
    }

    public void actionPerformed_sdraws() {
        if (!this.game.talker.connected || Game.areas[124].numPieces() <= 0) {
            GamePiece g = Game.areas[124].GetRandomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.557") + g + Messages.getKeyString("Controls.558") + Game.areas[124].name() + Messages.getKeyString("Controls.559") + Game.areas[181].name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[124].numPieces(), "draw SASTRA card");
    }

    private void actionPerformed_sdrawgeneric() {
        if ((this.game.hasFPpassword() && this.game.hasSPpassword()) || ((this.game.hasFPpassword() && this.game.opponentHandViewing.contains("(" + Messages.getString("Game.1") + ")")) || (this.game.hasSPpassword() && this.game.opponentHandViewing.contains("(" + Messages.getString("Game.3") + ")")))) {
            GamePiece g = Game.areas[80].randomPiece();
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.567") + g + Messages.getKeyString("Controls.568") + Game.areas[80].name() + Messages.getKeyString("Controls.569") + Game.areas[181].name() + " ");
            }
            this.game.talker.waitforemptyqueue();
            if (Game.areas[80].numPieces() == 0) {
                this.game.playSound("soundpack/deal.sp1");
                Area a = new Area("temp");
                ArrayList<GamePiece> temp = Game.areas[175].pieces();
                for (int i = 0; i <= temp.size() - 1; i++) {
                    GamePiece gp = temp.get(i);
                    if (gp instanceof GenericCard) {
                        a.addPiece(gp);
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.573") + a.contents() + Messages.getKeyString("Controls.574") + Game.areas[175].name() + Messages.getKeyString("Controls.575") + Game.areas[80].name() + " ");
                a.clearAllPieces();
                ArrayList<GamePiece> temp2 = Game.areas[177].pieces();
                for (int i2 = 0; i2 <= temp2.size() - 1; i2++) {
                    GamePiece gp2 = temp2.get(i2);
                    if (gp2 instanceof GenericCard) {
                        a.addPiece(gp2);
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.578") + a.contents() + Messages.getKeyString("Controls.579") + Game.areas[177].name() + Messages.getKeyString("Controls.580") + Game.areas[80].name() + " ");
            }
        }
    }

    private void actionPerformed_sdrawspecial() {
        GamePiece g = Game.areas[0].randomPiece();
        if (g != null) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.587") + g + Messages.getKeyString("Controls.588") + Game.areas[0].name() + Messages.getKeyString("Controls.589") + Game.areas[181].name() + " ");
        }
    }

    private void actionPerformed_structuredamage() {
        Area structure = this.game.highlight;
        if ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.80")) || this.game.OrthancHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.82")) || this.game.HornburgHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.84")) || this.game.EdorasHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.86")) || this.game.NCityWallHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.88")) || this.game.GreatGateHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.90")) || this.game.SouthRammasHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.92")) || this.game.CairAndrosHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.545")) || this.game.FrontGateHP <= 0) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.580")) || this.game.RavenHillHP <= 0) && (!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.569")) || this.game.EasternSpurHP <= 0)))))))))) {
            this.game.controls.chat.write(Messages.getString("Controls.95"));
        } else {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.93") + structure.name());
        }
    }

    private void actionPerformed_structurerepair() {
        Area structure = this.game.highlight;
        if ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.97")) || this.game.OrthancHP >= 8) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.99")) || this.game.HornburgHP >= 6) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.100")) || this.game.EdorasHP >= 3) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.102")) || this.game.NCityWallHP >= 10) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.103")) || this.game.GreatGateHP >= 6) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.104")) || this.game.SouthRammasHP >= 10) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Controls.105")) || this.game.CairAndrosHP >= 3) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.545")) || this.game.FrontGateHP >= 6) && ((!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.580")) || this.game.RavenHillHP >= 3) && (!Messages.removeKeyString(structure.name()).equals(Messages.getString("Game.569")) || this.game.EasternSpurHP >= 3)))))))))) {
            this.game.controls.chat.write(Messages.getString("Controls.110"));
        } else if (JOptionPane.showConfirmDialog(Game.win, String.valueOf(Messages.getString("Controls.106")) + Messages.removeKeyString(structure.name()), Messages.getString("Controls.108"), 0, 2) == 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.109") + structure.name());
        }
    }

    private void actionPerformed_initialcounters() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.620"), Messages.getString("Controls.621"), 0, 2) == 0) {
            this.game.interpreter.exInitialCounters();
        }
    }

    private void actionPerformed_hunt() {
        if (!this.game.talker.connected || Game.areas[182].numPieces() <= 0) {
            GamePiece g = Game.areas[182].GetRandomPiece();
            Area dest = Game.areas[154];
            if (dest.numPieces() > 0) {
                dest = Game.areas[153];
            }
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.624") + g + Messages.getKeyString("Controls.625") + Game.areas[182].name() + Messages.getKeyString("Controls.626") + dest.name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[182].numPieces(), "draw HUNT tile");
    }

    private void actionPerformed_fate() {
        if (!this.game.talker.connected || Game.areas[182].numPieces() <= 0) {
            GamePiece g = Game.areas[182].randomPiece();
            Area dest = Game.areas[154];
            if (dest.numPieces() > 0) {
                dest = Game.areas[153];
            }
            if (g != null) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.630") + g + Messages.getKeyString("Controls.631") + Game.areas[182].name() + Messages.getKeyString("Controls.632") + dest.name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, Game.areas[182].numPieces(), "draw HUNT tile");
    }

    private void actionPerformed_randomcomp() {
        Area temp = new Area("temp");
        temp.addAllPieces(Game.areas[115]);
        temp.addAllPieces(Game.areas[116]);
        if (!this.game.talker.connected || temp.numPieces() <= 0) {
            GamePiece g = temp.GetRandomPiece();
            if (g != null) {
                Area dest = Game.areas[153];
                if (dest.numPieces() > 0) {
                    dest = Game.areas[154];
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.637") + g + Messages.getKeyString("Controls.638") + g.currentLocation().name() + Messages.getKeyString("Controls.639") + dest.name() + " ");
                return;
            }
            return;
        }
        actionPerformed_SecureRoll(1, temp.numPieces(), "draw companion");
    }

    private void actionPerformed_endturn() {
        this.game.stopBrightObjects();
        Area a = new Area("temp");
        a.addAllPieces(Game.areas[152]);
        a.addAllPieces(Game.areas[153]);
        a.addAllPieces(Game.areas[154]);
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.644") + a.contents() + Messages.getKeyString("Controls.645") + Game.areas[152].name() + " ");
        if (this.game.chosen1 != null) {
            this.game.talker.enqueue("$" + this.game.chosen1player + Messages.getKeyString("Controls.179"));
        }
        if (this.game.chosen2 != null) {
            this.game.talker.enqueue("$" + this.game.chosen2player + Messages.getKeyString("Controls.182"));
        }
    }

    private void actionPerformed_clearcards() {
        Area a = new Area("temp");
        a.addAllPieces(Game.areas[153]);
        a.clearAllPieces();
        a.addAllPieces(Game.areas[154]);
        this.game.talker.waitforemptyqueue();
        a.clearAllPieces();
        a.addAllPieces(Game.areas[153]);
        a.addAllPieces(Game.areas[154]);
        if (a.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.662") + a.contents() + Messages.getKeyString("Controls.663") + Game.areas[152].name() + " ");
        }
        this.game.talker.waitforemptyqueue();
    }

    private void actionPerformed_frecover() {
        Area temparea = new Area("temp");
        ArrayList<GamePiece> temp = Game.areas[114].pieces();
        for (int i = temp.size() - 1; i >= 0; i--) {
            GamePiece gp = temp.get(i);
            if ((gp instanceof Recoverable) && gp.nation() > 0) {
                temparea.addPiece(gp);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.726") + temparea.contents() + Messages.getKeyString("Controls.727") + Game.areas[114].name() + Messages.getKeyString("Controls.728") + Game.areas[178].name() + " ");
        }
        temparea.clearAllPieces();
        ArrayList<GamePiece> temp2 = Game.areas[175].pieces();
        for (int i2 = temp2.size() - 1; i2 >= 0; i2--) {
            GamePiece gp2 = temp2.get(i2);
            if ((gp2 instanceof Recoverable) && gp2.nation() > 0) {
                temparea.addPiece(gp2);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.731") + temparea.contents() + Messages.getKeyString("Controls.732") + Game.areas[175].name() + Messages.getKeyString("Controls.733") + Game.areas[178].name() + " ");
        }
        temparea.clearAllPieces();
        ArrayList<GamePiece> temp3 = Game.areas[152].pieces();
        for (int i3 = temp3.size() - 1; i3 >= 0; i3--) {
            GamePiece gp3 = temp3.get(i3);
            if ((gp3 instanceof Recoverable) && gp3.nation() > 0) {
                temparea.addPiece(gp3);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.736") + temparea.contents() + Messages.getKeyString("Controls.737") + Game.areas[152].name() + Messages.getKeyString("Controls.738") + Game.areas[178].name() + " ");
        }
        for (int i4 = 0; i4 <= Game.areas.length - 1; i4++) {
            if (!(i4 == 174 || i4 == 175)) {
                int totalpieces = Game.areas[i4].numPieces();
                for (int p = 0; p <= totalpieces - 1; p++) {
                    GamePiece gp5 = Game.areas[i4].get(p);
                    if ((gp5 instanceof UnitLeadershipToken) && gp5.nation() > 0) {
                        temparea.addPiece(gp5);
                    }
                }
                if (temparea.numPieces() > 0) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.784") + temparea.contents() + Messages.getKeyString("Controls.785") + Game.areas[i4].name() + Messages.getKeyString("Controls.786") + Game.areas[174].name() + " ");
                    temparea.clearAllPieces();
                }
            }
        }
    }

    private void actionPerformed_frecoverstatistics() {
        Area a = new Area("temp");
        a.clearAllPieces();
        a.addAllPieces(Game.areas[83]);
        if (a.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.743") + a.contents() + Messages.getKeyString("Controls.744") + Game.areas[83].name() + Messages.getKeyString("Controls.745") + Game.areas[186].name() + " ");
        }
    }

    private void actionPerformed_srecoverstatistics() {
        Area a = new Area("temp");
        a.clearAllPieces();
        a.addAllPieces(Game.areas[84]);
        if (a.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.750") + a.contents() + Messages.getKeyString("Controls.751") + Game.areas[84].name() + Messages.getKeyString("Controls.752") + Game.areas[188].name() + " ");
        }
    }

    private void actionPerformed_srecover() {
        Area temparea = new Area("temp");
        ArrayList<GamePiece> temp = Game.areas[114].pieces();
        for (int i = temp.size() - 1; i >= 0; i--) {
            GamePiece gp = temp.get(i);
            if ((gp instanceof Recoverable) && gp.nation() <= 0) {
                temparea.addPiece(gp);
            }
        }
        if (!temparea.contents().equals("")) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.757") + temparea.contents() + Messages.getKeyString("Controls.758") + Game.areas[114].name() + Messages.getKeyString("Controls.759") + Game.areas[179].name() + " ");
        }
        temparea.clearAllPieces();
        ArrayList<GamePiece> temp2 = Game.areas[177].pieces();
        for (int i2 = temp2.size() - 1; i2 >= 0; i2--) {
            GamePiece gp2 = temp2.get(i2);
            if ((gp2 instanceof Recoverable) && gp2.nation() <= 0) {
                temparea.addPiece(gp2);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.762") + temparea.contents() + Messages.getKeyString("Controls.763") + Game.areas[177].name() + Messages.getKeyString("Controls.764") + Game.areas[179].name() + " ");
        }
        temparea.clearAllPieces();
        ArrayList<GamePiece> temp3 = Game.areas[152].pieces();
        for (int i3 = temp3.size() - 1; i3 >= 0; i3--) {
            GamePiece gp3 = temp3.get(i3);
            if ((gp3 instanceof Recoverable) && gp3.nation() > 0) {
                temparea.addPiece(gp3);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.767") + temparea.contents() + Messages.getKeyString("Controls.768") + Game.areas[152].name() + Messages.getKeyString("Controls.769") + Game.areas[178].name() + " ");
        }
        temparea.clearAllPieces();
        for (int i4 = 0; i4 <= Game.areas.length - 1; i4++) {
            if (!(i4 == 176 || i4 == 177)) {
                int totalpieces = Game.areas[i4].numPieces();
                for (int p = 0; p <= totalpieces - 1; p++) {
                    GamePiece gp4 = Game.areas[i4].get(p);
                    if ((gp4 instanceof UnitLeadershipToken) && gp4.nation() < 0) {
                        temparea.addPiece(gp4);
                    }
                }
                if (temparea.numPieces() > 0) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.784") + temparea.contents() + Messages.getKeyString("Controls.785") + Game.areas[i4].name() + Messages.getKeyString("Controls.786") + Game.areas[176].name() + " ");
                    temparea.clearAllPieces();
                }
            }
        }
        temparea.clearAllPieces();
        for (GamePiece gp5 : this.game.bits) {
            if ((gp5 instanceof UnitBat) && gp5.currentLocation() != Game.areas[176]) {
                temparea.addPiece(gp5);
            }
        }
        if (temparea.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.736") + temparea.contents() + Messages.getKeyString("Controls.737") + Game.areas[152].name() + Messages.getKeyString("Controls.738") + Game.areas[176].name() + " ");
        }
        temparea.clearAllPieces();
    }

    private void actionPerformed_fdiscardstory() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.147"), Messages.getString("Controls.148"), 0, 2) == 0) {
            Area chars = new Area("temp");
            for (int i = 0; i < Game.areas[180].numPieces(); i++) {
                GamePiece p = Game.areas[180].get(i);
                if (p instanceof FreeStoryCard) {
                    chars.addPiece(p);
                }
            }
            if (chars.numPieces() != 0) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.452") + chars.randomPiece() + Messages.getKeyString("Controls.453") + Game.areas[180] + Messages.getKeyString("Controls.454") + Game.areas[175] + " ");
                this.game.refreshBoard();
            }
        }
    }

    private void actionPerformed_stransfergeneric() {
        if (JOptionPane.showConfirmDialog(Game.win, Messages.getString("Controls.149"), Messages.getString("Controls.151"), 0, 2) == 0) {
            Area chars = new Area("temp");
            for (int i = 0; i < Game.areas[181].numPieces(); i++) {
                GamePiece p = Game.areas[181].get(i);
                if (p instanceof GenericCard) {
                    chars.addPiece(p);
                }
            }
            if (chars.numPieces() != 0) {
                GamePiece p2 = chars.randomPiece();
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + p2 + Messages.getKeyString("Controls.453") + Game.areas[181] + Messages.getKeyString("Controls.454") + Game.areas[180] + " ");
                this.game.talker.waitforemptyqueue();
                chars.removePiece(p2);
                GamePiece p3 = chars.randomPiece();
                if (p3 != null) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + p3 + Messages.getKeyString("Controls.453") + Game.areas[181] + Messages.getKeyString("Controls.454") + Game.areas[180] + " ");
                }
                this.game.refreshBoard();
            }
        }
    }

    private void actionPerformed_movefsp() {
        Area x = null;
        int fsp = 0;
        while (true) {
            if (fsp >= this.game.bits.length) {
                break;
            } else if (this.game.bits[fsp] instanceof UnitFellowship) {
                x = this.game.bits[fsp].currentLocation();
                break;
            } else {
                fsp++;
            }
        }
        if (x.name().equals(Messages.getKeyString("Game.524"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[fsp] + Messages.getKeyString("Controls.791") + x.name() + Messages.getKeyString("Controls.792") + Messages.getKeyString("Game.525") + " ");
        } else if (x.name().equals(Messages.getKeyString("Game.525"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[fsp] + Messages.getKeyString("Controls.791") + x.name() + Messages.getKeyString("Controls.792") + Messages.getKeyString("Game.528") + " ");
        } else if (x.name().equals(Messages.getKeyString("Game.528"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[fsp] + Messages.getKeyString("Controls.791") + x.name() + Messages.getKeyString("Controls.792") + Messages.getKeyString("Game.529") + " ");
        } else if (x.name().equals(Messages.getKeyString("Game.529"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[fsp] + Messages.getKeyString("Controls.791") + x.name() + Messages.getKeyString("Controls.792") + Messages.getKeyString("Game.532") + " ");
        } else if (x.name().equals(Messages.getKeyString("Game.532"))) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[fsp] + Messages.getKeyString("Controls.791") + x.name() + Messages.getKeyString("Controls.792") + Messages.getKeyString("Game.533") + " ");
        } else {
            Area x2 = this.game.bits[248].currentLocation();
            int i = 0;
            while (true) {
                if (i >= Game.areas.length) {
                    break;
                } else if (Game.areas[i] == x2) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.790") + this.game.bits[248] + Messages.getKeyString("Controls.791") + x2.name() + Messages.getKeyString("Controls.792") + Game.areas[i + 1].name() + " ");
                    break;
                } else {
                    i++;
                }
            }
        }
        ArrayList<GamePiece> temp = Game.areas[152].pieces();
        for (int i2 = temp.size() - 1; i2 >= 0; i2--) {
            GamePiece gp = temp.get(i2);
            if ((gp instanceof FreeActionDie) || (gp instanceof NaryaDice) || (gp instanceof NenyaDice) || (gp instanceof VilyaDice)) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.795") + gp + Messages.getKeyString("Controls.796") + Game.areas[152].name() + Messages.getKeyString("Controls.797") + Game.areas[114].name() + " ");
                return;
            }
        }
    }

    private void actionPerformed_movefatemarker() {
        Area x = null;
        int fateID = 0;
        int i = 0;
        while (true) {
            if (i >= this.game.bits.length) {
                break;
            } else if (this.game.bits[i].type().equals(Messages.getString("Controls.116"))) {
                x = this.game.bits[i].currentLocation();
                fateID = i;
                break;
            } else {
                i++;
            }
        }
        for (int i2 = 0; i2 < Game.areas.length; i2++) {
            if (Game.areas[i2] == x) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.802") + this.game.bits[fateID] + Messages.getKeyString("Controls.803") + x.name() + Messages.getKeyString("Controls.804") + Game.areas[i2 + 1].name() + " ");
                return;
            }
        }
    }

    private void actionPerformed_reformentwood() {
        if (JOptionPane.showConfirmDialog(Game.win, String.valueOf(Messages.getString("Controls.117")) + Messages.removeKeyString(this.game.highlight.name()), Messages.getString("Controls.119"), 0, 2) == 0) {
            Area temparea = new Area("temp");
            for (int i = 0; i < Game.areas.length - 1; i++) {
                temparea.clearAllPieces();
                ArrayList<GamePiece> temp = Game.areas[i].pieces();
                if (!(i == 174 || Game.areas[i].name() == this.game.highlight.name())) {
                    for (int j = temp.size() - 1; j >= 0; j--) {
                        GamePiece gp = temp.get(j);
                        if ((gp instanceof UnitEnt) || (gp instanceof UnitTreebeard)) {
                            temparea.addPiece(gp);
                        }
                    }
                }
                if (temparea.numPieces() > 0) {
                    this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.811") + temparea.contents() + Messages.getKeyString("Controls.812") + Game.areas[i].name() + Messages.getKeyString("Controls.813") + this.game.highlight.name() + " ");
                }
            }
        }
    }

    private void actionPerformed_undo() {
        if (this.game.historyactionpointer > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.819"));
        } else {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.820"));
        }
    }

    private void actionPerformed_redo() {
        if (this.game.historyactionpointer < this.game.historyactions.size()) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.16"));
        } else {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.25"));
        }
    }

    private void actionPerformed_synchronize() {
        this.game.synchronizing = true;
        if (Game.boardtype.equals("wotr")) {
            this.game.resetBoard();
        }
        this.game.interpreter.setLogFile(Game.generateLogName());
        this.game.talker.enqueuedirect(Messages.getKeyString("Controls.825"), (ObserverHost) null);
        this.game.synchronizing = false;
    }

    private void actionPerformed_save() {
        if (this.controls.f3fc.showSaveDialog(Game.win) == 0) {
            File f = this.controls.f3fc.getSelectedFile();
            if (!f.getName().contains(".")) {
                try {
                    File f2 = new File(String.valueOf(f.getAbsolutePath()) + ".log");
                    if (!f2.exists()) {
                        f = f2;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.game.interpreter.save(f);
        }
    }

    private void actionPerformed_allowobs() {
        this.game.allowObserverCommands = !this.game.allowObserverCommands;
        if (this.game.allowObserverCommands) {
            this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.833"));
        } else {
            this.game.talker.enqueue("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.835"));
        }
    }

    private void actionPerformed_choose() {
        if (((this.game.selection.get(0) instanceof Card) || (this.game.selection.get(0) instanceof GenericCard)) && this.game.selection.get(0).nation() < 0 && !this.game.hasSPpassword()) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.837"));
        } else if (((this.game.selection.get(0) instanceof Card) || (this.game.selection.get(0) instanceof GenericCard)) && this.game.selection.get(0).nation() > 0 && !this.game.hasFPpassword()) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.838"));
        } else {
            if (this.game.selection.numPieces() > 0 && ((this.game.selection.get(0) instanceof Card) || (this.game.selection.get(0) instanceof GenericCard))) {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.840") + this.game.selection.get(0) + " ");
            }
        }
    }

    private void actionPerformed_unchoose() {
        if (Game.prefs.nick.equals(this.game.chosen1player) || Game.prefs.nick.equals(this.game.chosen2player)) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getString("Controls.186"));
        } else {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.183"));
        }
    }

    public void actionPerformed_playchosen() {
        if (this.game.chosen1 != null) {
            if (this.game.chosen1.nation() > 0) {
                if (this.game.hasFPpassword()) {
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Controls.861") + this.game.GetCardRevealText(this.game.chosen1) + Messages.getKeyString("Controls.862") + this.game.chosen1.currentLocation().name() + Messages.getKeyString("Controls.863") + Game.areas[154].name() + " ");
                } else {
                    this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
                    this.game.talker.enqueuedirect("<auto> playchosen ", (ObserverHost) null);
                    this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
                }
            } else if (this.game.hasSPpassword()) {
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Controls.866") + this.game.GetCardRevealText(this.game.chosen1) + Messages.getKeyString("Controls.867") + this.game.chosen1.currentLocation().name() + Messages.getKeyString("Controls.868") + Game.areas[153].name() + " ");
            } else {
                this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
                this.game.talker.enqueuedirect("<auto> playchosen ", (ObserverHost) null);
                this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
            }
        }
        if (this.game.chosen2 != null) {
            if (this.game.chosen2.nation() > 0) {
                if (this.game.hasFPpassword()) {
                    this.game.talker.enqueue("<auto>" + Messages.getKeyString("Controls.3") + this.game.GetCardRevealText(this.game.chosen2) + Messages.getKeyString("Controls.884") + this.game.chosen2.currentLocation().name() + Messages.getKeyString("Controls.885") + Game.areas[154].name() + " ");
                } else {
                    this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
                    this.game.talker.enqueuedirect("<auto> playchosen ", (ObserverHost) null);
                    this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
                }
            } else if (this.game.hasSPpassword()) {
                this.game.talker.enqueue("<auto>" + Messages.getKeyString("Controls.888") + this.game.GetCardRevealText(this.game.chosen2) + Messages.getKeyString("Controls.889") + this.game.chosen2.currentLocation().name() + Messages.getKeyString("Controls.890") + Game.areas[153].name() + " ");
            } else {
                this.game.talker.enqueuedirect("<mute_on> " + Game.prefs.nick, (ObserverHost) null);
                this.game.talker.enqueuedirect("<auto> playchosen ", (ObserverHost) null);
                this.game.talker.enqueuedirect("<mute_off> " + Game.prefs.nick, (ObserverHost) null);
            }
        }
        this.game.chosen1 = null;
        this.game.chosen1player = null;
        this.game.chosen2 = null;
        this.game.chosen2player = null;
    }

    private void actionPerformed_loadreplay() {
        if (this.controls.f3fc.showOpenDialog(Game.win) == 0) {
            File f = this.controls.f3fc.getSelectedFile();
            if (Game.boardtype.equals("wotr")) {
                this.game.resetBoard();
            }
            this.game.interpreter.setLogFile(Game.generateLogName());
            if (this.game.GetFPpassword() != null) {
                this.game.HideFPhand();
                this.game.ClearFPDecks();
            }
            if (this.game.GetSPpassword() != null) {
                this.game.HideSPhand();
                this.game.ClearSPDecks();
            }
            this.game.interpreter.loadReplay(f);
            this.game.talker.enqueuedirect("<game> " + Game.prefs.nick + Messages.getKeyString("Controls.894"), (ObserverHost) null);
        }
    }

    private void actionPerformed_setzoom() {
        int newzoom;
        try {
            newzoom = Integer.parseInt(JOptionPane.showInputDialog((Component) null, Messages.getString("Controls.904"), Integer.valueOf(Game.prefs.zoom)));
        } catch (Throwable th) {
            newzoom = 100;
        }
        Game.prefs.zoom = newzoom;
        this.game.zoomBoard(newzoom);
        this.game.refreshBoard();
    }

    private static void actionPerformed_readme() {
        try {
            Desktop.getDesktop().open(new File("readme.pdf"));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void downgradeElites(Area loc, int[] nations) {
        String fr;
        Area regs = new Area("temp");
        int re = 0;
        int ca = 0;
        for (int i = 0; i < nations.length; i++) {
            if (nations[i] > 0) {
                if (i > 3) {
                    Area a = Game.areas[175];
                    for (int j = 0; j < a.numPieces(); j++) {
                        GamePiece p = a.get(j);
                        if ((p instanceof Regular) && p.nation() == i - 5) {
                            regs.addPiece(p);
                            nations[i] = nations[i] - 1;
                            ca++;
                            if (nations[i] <= 0) {
                                break;
                            }
                        }
                    }
                }
                if (nations[i] > 0) {
                    if (i <= 3) {
                        Area a2 = Game.areas[177];
                        for (int j2 = 0; j2 < a2.numPieces(); j2++) {
                            GamePiece p2 = a2.get(j2);
                            if ((p2 instanceof Regular) && p2.nation() == i - 5) {
                                regs.addPiece(p2);
                                nations[i] = nations[i] - 1;
                                ca++;
                                if (nations[i] <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (nations[i] > 0) {
                        if (i > 3) {
                            Area a3 = Game.areas[174];
                            for (int j3 = 0; j3 < a3.numPieces(); j3++) {
                                GamePiece p3 = a3.get(j3);
                                if ((p3 instanceof Regular) && p3.nation() == i - 5) {
                                    regs.addPiece(p3);
                                    nations[i] = nations[i] - 1;
                                    re++;
                                    if (nations[i] <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (nations[i] > 0 && i <= 3) {
                            Area a4 = Game.areas[176];
                            for (int j4 = 0; j4 < a4.numPieces(); j4++) {
                                GamePiece p4 = a4.get(j4);
                                if ((p4 instanceof Regular) && p4.nation() == i - 5) {
                                    regs.addPiece(p4);
                                    nations[i] = nations[i] - 1;
                                    re++;
                                    if (nations[i] <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (ca == 0) {
            if (re == 0) {
                this.game.controls.chat.write(Messages.getString("Controls.172"));
            } else if (loc.get(0).nation() >= 0) {
                this.game.controls.chat.write(Messages.getString("Controls.177"));
            }
            fr = Messages.getKeyString("Controls.307");
        } else if (re == 0) {
            fr = Messages.getKeyString("Controls.308");
        } else {
            this.game.controls.chat.write(Messages.getString("Controls.178"));
            fr = String.valueOf(Messages.getKeyString("Controls.309")) + re + Messages.getString("Controls.310") + ca + ")";
        }
        this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.304") + this.game.selection.contents() + Messages.getKeyString("Controls.305") + loc.name() + " ", (ObserverHost) null);
        if (regs.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.313") + regs.contents() + fr + Messages.getKeyString("Controls.314") + loc.name() + " ", (ObserverHost) null);
        }

    }

    private void upgradeRegulars(Area loc, int[] nations) {
        Area regs = new Area("temp");
        for (int i = 0; i < nations.length; i++) {
            if (nations[i] > 0) {
                if (i > 3) {
                    Area a = Game.areas[174];
                    for (int j = 0; j < a.numPieces(); j++) {
                        GamePiece p = a.get(j);
                        if ((p instanceof Elite) && p.nation() == i - 5) {
                            regs.addPiece(p);
                            nations[i] = nations[i] - 1;
                            if (nations[i] <= 0) {
                                break;
                            }
                        }
                    }
                }
                if (nations[i] > 0 && i <= 3) {
                    Area a2 = Game.areas[176];
                    for (int j2 = 0; j2 < a2.numPieces(); j2++) {
                        GamePiece p2 = a2.get(j2);
                        if ((p2 instanceof Elite) && p2.nation() == i - 5) {
                            regs.addPiece(p2);
                            nations[i] = nations[i] - 1;
                            if (nations[i] <= 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (regs.numPieces() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.304") + this.game.selection.contents() + Messages.getKeyString("Controls.305") + loc.name() + " ");
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.313") + regs.contents() + Messages.getKeyString("Controls.309") + Messages.getKeyString("Controls.314") + loc.name() + " ");
            return;
        }
        JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.121"));
    }
}
