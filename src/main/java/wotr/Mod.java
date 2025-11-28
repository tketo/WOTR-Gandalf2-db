package wotr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/* renamed from: Mod */
public class Mod {
    List<String> commands = new LinkedList();
    String errorString;
    Game game;
    List<Integer> locations = new LinkedList();
    int realNumBits;

    public Mod(File f, Game g) {
        this.game = g;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                String line = br.readLine();
                if (line.length() > 0) {
                    this.commands.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
        }
        this.realNumBits = this.game.numBits;
    }

    public void init() {
        this.errorString = "";
        LinkedList<String> commands2 = new LinkedList<>();
        while (!this.commands.isEmpty()) {
            String c = this.commands.get(0);
            if (c.charAt(0) == '<' || c.charAt(0) == '$') {
                commands2.add("~" + this.commands.remove(0));
            } else if (c.charAt(0) == '~') {
                this.game.interpreter.execute(c.substring(1));
                commands2.add(this.commands.remove(0));
            } else if (c.charAt(0) == '#') {
                this.commands.remove(0);
            } else {
                parseCommand(this.commands.get(0));
                this.commands.remove(0);
            }
        }
        this.commands = commands2;
        checkForErrors();
    }

    public void parseCommand(String com) {
        try {
            Scanner s = new Scanner(com);
            String comType = s.next();
            if (comType.equals("card")) {
                parseCard(s, com);
            } else if (comType.equals("chit")) {
                parseChit(s, com);
            } else {
                addCommandError(com);
            }
        } catch (NoSuchElementException e) {
            addCommandError(com);
        }
    }

    public void parseCard(Scanner s, String com) {
        GamePiece gp = null;
        String comType = String.valueOf(s.next()) + s.next();
        String name = s.next();
        String imgname = s.next();
        if ("freemuster".equals(comType)) {
            gp = new FreeStrategyCard(Game.areas[122], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(122));
        } else if ("freearmy".equals(comType)) {
            gp = new FreeStrategyCard(Game.areas[122], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(122));
        } else if ("freecharacter".equals(comType)) {
            gp = new FreeCharacterCard(Game.areas[121], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(Game.FP_CHARACTER_DECK));
        } else if ("shadowmuster".equals(comType)) {
            gp = new ShadowStrategyCard(Game.areas[124], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(Game.SA_STRATEGY_DECK));
        } else if ("shadowarmy".equals(comType)) {
            gp = new ShadowStrategyCard(Game.areas[124], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(Game.SA_STRATEGY_DECK));
        } else if ("shadowcharacter".equals(comType)) {
            gp = new ShadowCharacterCard(Game.areas[123], "custom/images/smallcards/" + imgname, "custom/images/cards/" + imgname, removeUnderscores(name));
            this.locations.add(Integer.valueOf(123));
        } else {
            addCommandError(com);
        }
        if (gp != null) {
            addToBits(gp);
        }
    }

    private static String removeUnderscores(String s) {
        return s.replace('_', ' ').replace('>', '\n');
    }

    private void addCommandError(String com) {
        this.errorString = String.valueOf(this.errorString) + Messages.getString("Mod.0") + com + "'\n";
    }

    public void parseChit(Scanner s, String com) {
        String side = s.next();
        if (side.equals("flippable")) {
            parseFlippableChit(s, com);
        } else if (side.equals("action")) {
            parseActionChit(s, com);
        } else {
            GamePiece gp = null;
            String name = s.next();
            String imgname = s.next();
            if (side.equals("free")) {
                gp = new Chit(Game.areas[174], removeUnderscores(name), "custom/images/" + imgname, 1);
                this.locations.add(Integer.valueOf(Game.FP_REINFORCEMENTS));
            } else if (side.equals("shadow")) {
                gp = new Chit(Game.areas[176], removeUnderscores(name), "custom/images/" + imgname);
                this.locations.add(Integer.valueOf(Game.SA_REINFORCEMENTS));
            } else {
                addCommandError(com);
            }
            if (gp != null) {
                addToBits(gp);
            }
        }
    }

    public void parseActionChit(Scanner s, String com) {
        GamePiece gp = null;
        String side = s.next();
        String name = s.next();
        String imgname = s.next();
        if (side.equals("free")) {
            gp = new ActionChit(Game.areas[174], removeUnderscores(name), "custom/images/" + imgname, 1);
            this.locations.add(Integer.valueOf(Game.FP_REINFORCEMENTS));
        } else if (side.equals("shadow")) {
            gp = new ActionChit(Game.areas[176], removeUnderscores(name), "custom/images/" + imgname);
            this.locations.add(Integer.valueOf(Game.SA_REINFORCEMENTS));
        } else {
            addCommandError(com);
        }
        if (gp != null) {
            addToBits(gp);
        }
    }

    public void parseFlippableChit(Scanner s, String com) {
        GamePiece gp = null;
        String side = s.next();
        String name = s.next();
        String imgname1 = s.next();
        String imgname2 = s.next();
        if (side.equals("free")) {
            gp = new TwoChit(Game.areas[174], removeUnderscores(name), "custom/images/" + imgname1, "custom/images/" + imgname2);
            this.locations.add(Integer.valueOf(Game.FP_REINFORCEMENTS));
        } else if (side.equals("shadow")) {
            gp = new TwoChit(Game.areas[176], removeUnderscores(name), "custom/images/" + imgname1, "custom/images/" + imgname2);
            this.locations.add(Integer.valueOf(Game.SA_REINFORCEMENTS));
        } else {
            addCommandError(com);
        }
        if (gp != null) {
            addToBits(gp);
        }
    }

    public void addToBits(GamePiece gp) {
        int id = this.realNumBits;
        this.realNumBits = id + 1;
        if (id >= this.game.bits.length) {
            GamePiece[] gp2 = new GamePiece[(id + 50)];
            for (int i = 0; i < this.game.bits.length; i++) {
                gp2[i] = this.game.bits[i];
            }
            this.game.bits = gp2;
        }
        this.game.bits[id] = gp;
    }

    private void checkForErrors() {
        if (!this.errorString.equals("") && JOptionPane.showConfirmDialog(Game.win, String.valueOf(Messages.getString("Mod.41")) + this.errorString + Messages.getString("Mod.42"), Messages.getString("Mod.43"), 0, 2) == 1) {
            System.exit(1);
        }
    }

    public void reset() {
        this.errorString = "";
        int index = this.game.numBits;
        for (Integer intValue : this.locations) {
            this.game.bits[index].moveTo(Game.areas[intValue.intValue()]);
            index++;
        }
        init();
    }
}
