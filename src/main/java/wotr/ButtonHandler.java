package wotr;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ButtonHandler */
/* compiled from: ChooseFSP */
class ButtonHandler implements ActionListener {
    ChooseFSP controls;

    /* renamed from: f */
    Frame f2f;
    Game game;

    ButtonHandler(Frame f, Game game2, ChooseFSP controls2) {
        this.f2f = f;
        this.game = game2;
        this.controls = controls2;
    }

    public void actionPerformed(ActionEvent e) {
        GamePiece unit = null;
        this.game.talker.enqueue("<auto> nowait");
        if (ChooseFSP.Gandalf1.isSelected()) {
            for (int i = 0; i < this.game.bits.length; i++) {
                unit = this.game.bits[i];
                if (unit instanceof UnitGandalf) {
                    break;
                }
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Gandalf1");
        }
        if (ChooseFSP.Gandalf2.isSelected()) {
            for (int i2 = 0; i2 < this.game.bits.length; i2++) {
                unit = this.game.bits[i2];
                if (unit instanceof UnitGandalf) {
                    break;
                }
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Gandalf2");
        }
        if (ChooseFSP.CouncilChosen) {
            if (ChooseFSP.Boromir.isSelected()) {
                for (int i3 = 0; i3 < this.game.bits.length; i3++) {
                    unit = this.game.bits[i3];
                    if (unit instanceof UnitBoromir) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Boromir1");
            }
            if (ChooseFSP.Boromir2.isSelected()) {
                for (int i4 = 0; i4 < this.game.bits.length; i4++) {
                    unit = this.game.bits[i4];
                    if (unit instanceof UnitBoromir) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Boromir2");
            }
            if (ChooseFSP.Strider.isSelected()) {
                for (int i5 = 0; i5 < this.game.bits.length; i5++) {
                    unit = this.game.bits[i5];
                    if (unit instanceof UnitStrider) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Strider1");
            }
            if (ChooseFSP.Strider2.isSelected()) {
                for (int i6 = 0; i6 < this.game.bits.length; i6++) {
                    unit = this.game.bits[i6];
                    if (unit instanceof UnitStrider) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Strider2");
            }
            if (ChooseFSP.Legolas.isSelected()) {
                for (int i7 = 0; i7 < this.game.bits.length; i7++) {
                    unit = this.game.bits[i7];
                    if (unit instanceof UnitLegolas) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Legolas1");
            }
            if (ChooseFSP.Legolas2.isSelected()) {
                for (int i8 = 0; i8 < this.game.bits.length; i8++) {
                    unit = this.game.bits[i8];
                    if (unit instanceof UnitLegolas) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Legolas2");
            }
            if (ChooseFSP.Merry.isSelected()) {
                for (int i9 = 0; i9 < this.game.bits.length; i9++) {
                    unit = this.game.bits[i9];
                    if (unit instanceof UnitMerry) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Merry1");
            }
            if (ChooseFSP.Merry2.isSelected()) {
                for (int i10 = 0; i10 < this.game.bits.length; i10++) {
                    unit = this.game.bits[i10];
                    if (unit instanceof UnitMerry) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Merry2");
            }
            if (ChooseFSP.Gimli.isSelected()) {
                for (int i11 = 0; i11 < this.game.bits.length; i11++) {
                    unit = this.game.bits[i11];
                    if (unit instanceof UnitGimli) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Gimli1");
            }
            if (ChooseFSP.Gimli2.isSelected()) {
                for (int i12 = 0; i12 < this.game.bits.length; i12++) {
                    unit = this.game.bits[i12];
                    if (unit instanceof UnitGimli) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Gimli2");
            }
            if (ChooseFSP.Pippin.isSelected()) {
                for (int i13 = 0; i13 < this.game.bits.length; i13++) {
                    unit = this.game.bits[i13];
                    if (unit instanceof UnitPippin) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Pippin1");
            }
            if (ChooseFSP.Pippin2.isSelected()) {
                for (int i14 = 0; i14 < this.game.bits.length; i14++) {
                    unit = this.game.bits[i14];
                    if (unit instanceof UnitPippin) {
                        break;
                    }
                }
                this.game.talker.enqueue("$" + Game.prefs.nick + " names " + unit + " Pippin2");
            }
        }
        if (!ChooseFSP.CouncilChosen) {
            this.game.talker.enqueue("<auto> silent ");
            this.game.talker.enqueue("<auto><~Scribe.141~>446 <~Scribe.142~><~Game.2040~><~Scribe.143~>Spare<~Scribe.144~>");
            this.game.talker.enqueue("<auto><~Scribe.141~>447 <~Scribe.142~><~Game.2040~><~Scribe.143~>Spare<~Scribe.144~>");
            this.game.talker.enqueue("<auto> noisy ");
        }
        this.f2f.dispose();
    }
}
