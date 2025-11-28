package wotr;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: CtrlAreaHandler */
/* compiled from: Controls */
class CtrlAreaHandler extends MouseAdapter {
    Controls controls;
    Game game;
    Point pressPoint;

    CtrlAreaHandler(Controls c) {
        this.controls = c;
        this.game = c.game;
    }

    public void mouseClicked(MouseEvent e) {
        int pos;
        int pos2;
        boolean reveal = false;
        Component origin = e.getComponent();
        Area loc = null;
        if (origin == this.controls.FPReinforcements) {
            loc = Game.areas[174];
        } else if (origin == this.controls.FPCasualties) {
            loc = Game.areas[175];
        } else if (origin == this.controls.FPReinforcementTokens) {
            loc = Game.areas[185];
        } else if (origin == this.controls.FPTactics) {
            loc = Game.areas[186];
        } else if (origin == this.controls.SAReinforcements) {
            loc = Game.areas[176];
        } else if (origin == this.controls.SACasualties) {
            loc = Game.areas[177];
        } else if (origin == this.controls.SAReinforcementTokens) {
            loc = Game.areas[187];
        } else if (origin == this.controls.SATactics) {
            loc = Game.areas[188];
        } else if (origin == this.controls.FPD) {
            if (e.getClickCount() != 2 || e.isControlDown() || (e.getModifiers() & 4) == 4 || Game.areas[178].numPieces() <= (pos2 = (e.getX() / (Game.prefs.horizontalSpacing + 32)) + (((origin.getWidth() / 32) + Game.prefs.horizontalSpacing) * (e.getY() / (Game.prefs.verticalSpacing + 32))))) {
                loc = Game.areas[178];
            } else {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.271") + Game.areas[178].get(pos2) + Messages.getKeyString("Controls.272") + Game.areas[178].name() + Messages.getKeyString("Controls.273") + Game.areas[152].name() + " ");
                return;
            }
        } else if (origin == this.controls.SAD) {
            if (e.getClickCount() != 2 || e.isControlDown() || (e.getModifiers() & 4) == 4 || Game.areas[179].numPieces() <= (pos = (e.getX() / (Game.prefs.horizontalSpacing + 32)) + (((origin.getWidth() / 32) + Game.prefs.horizontalSpacing) * (e.getY() / (Game.prefs.verticalSpacing + 32))))) {
                loc = Game.areas[179];
            } else {
                this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.276") + Game.areas[179].get(pos) + Messages.getKeyString("Controls.277") + Game.areas[179].name() + Messages.getKeyString("Controls.278") + Game.areas[152].name() + " ");
                return;
            }
        } else if (origin == this.controls.huntPool) {
            loc = Game.areas[182];
        } else if (origin == this.controls.huntUsed) {
            loc = Game.areas[183];
        } else if (origin == this.controls.huntRemoved) {
            loc = Game.areas[184];
        }
        if (loc == null) {
            return;
        }
        if ((e.isControlDown() || (e.getModifiers() & 4) == 4) && this.game.selection.numPieces() > 0) {
            if (this.game.highlight.IsHiddenFromOpponent() && !loc.IsHiddenFromOpponent()) {
                reveal = true;
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("Controls.281") + this.game.selection.contents(reveal, this.game) + Messages.getKeyString("Controls.282") + this.game.highlight.name() + Messages.getKeyString("Controls.283") + loc.name() + " ");
            return;
        }
        this.game.highlightArea(loc, false);
    }

    public void mousePressed(MouseEvent e) {
        this.pressPoint = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        if (this.pressPoint != null) {
            Point releasePoint = e.getPoint();
            if (!this.pressPoint.equals(releasePoint) && releasePoint.distanceSq(this.pressPoint) < 200.0d) {
                e.translatePoint((-e.getX()) + this.pressPoint.x, (-e.getY()) + this.pressPoint.y);
                mouseClicked(e);
            }
        }
    }
}
