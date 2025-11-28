package wotr;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/* renamed from: CardBuffet */
public class CardBuffet extends Buffet implements MouseListener {
    public int CAPACITY = 21;
    public int CARDS_PER_ROW = 2;
    public int HEIGHT;
    public int WIDTH;
    public int X_EDGE = 0;
    public int X_OFFSET;
    public int Y_EDGE = -1;
    public int Y_OFFSET = 38;
    Point pressPoint;
    Area selCard;
    JComponent visual;

    public CardBuffet(String name, JComponent v, Game g, int pieceWidth) {
        super(name, (Point[]) null, true);
        ImageIcon smallBack;
        this.visual = v;
        this.visual.addMouseListener(this);
        this.game = g;
        this.selCard = new Area(name, false, false, true);
        this.WIDTH = Game.prefs.cardsWidth;
        this.HEIGHT = Game.prefs.cardsHeight;
        this.X_OFFSET = pieceWidth;
        smallBack = new ImageIcon(Messages.getLanguageLocation("images/FPAtextcard.gif"));
        this.X_OFFSET = smallBack.getIconWidth() + Game.prefs.horizontalSpacing;
        this.Y_OFFSET = smallBack.getIconHeight() + Game.prefs.verticalSpacing;
        this.CARDS_PER_ROW = this.WIDTH / this.X_OFFSET;
        this.points = getPtArray();
    }

    public void SetViewFronts(boolean viewFronts) {
        super.SetViewFronts(viewFronts);
        this.selCard.SetViewFronts(viewFronts);
    }

    public Point[] getPtArray() {
        Point[] p = new Point[this.CAPACITY];
        for (int i = 0; i < this.CAPACITY; i++) {
            p[i] = new Point(this.X_EDGE + (this.X_OFFSET * (i % this.CARDS_PER_ROW)), this.Y_EDGE + (this.Y_OFFSET * (i / this.CARDS_PER_ROW)));
        }
        return p;
    }

    public void mouseClicked(MouseEvent e) {
        boolean reveal = false;
        if ((e.isControlDown() || (e.getModifiers() & 4) == 4) && this.game.selection.numPieces() > 0) {
            if (this.game.highlight.IsHiddenFromOpponent() && !IsHiddenFromOpponent()) {
                reveal = true;
            }
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("CardBuffet.3") + this.game.selection.contents(reveal, this.game) + Messages.getKeyString("CardBuffet.4") + this.game.highlight.name() + Messages.getKeyString("CardBuffet.5") + name() + " ");
            return;
        }
        int x = e.getX();
        int y = e.getY();
        GamePiece card = get((this.CARDS_PER_ROW * ((y - this.Y_EDGE) / this.Y_OFFSET)) + ((x - this.X_EDGE) / this.X_OFFSET));
        if (card == null) {
            highlightMe();
        } else if (e.getClickCount() != 2) {
            this.selCard.clearAllPieces();
            this.selCard.addPiece(card);
            this.game.highlightArea(this.selCard, false);
        } else if ((card instanceof GenericCard) && card.nation() > 0 && !this.game.hasFPpassword()) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.291"));
        } else if ((card instanceof GenericCard) && card.nation() <= 0 && !this.game.hasSPpassword()) {
            JOptionPane.showMessageDialog(Game.win, Messages.getString("Controls.291"));
        } else if (card.nation() > 0) {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("CardBuffet.9") + this.game.GetCardRevealText(card) + Messages.getKeyString("CardBuffet.10") + name() + Messages.getKeyString("CardBuffet.11"));
        } else {
            this.game.talker.enqueue("$" + Game.prefs.nick + Messages.getKeyString("CardBuffet.14") + this.game.GetCardRevealText(card) + Messages.getKeyString("CardBuffet.15") + name() + Messages.getKeyString("CardBuffet.16"));
        }
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

    public void highlightMe() {
        if (numPieces() == 0) {
            this.game.highlightArea(this, false);
            return;
        }
        this.selCard.clearAllPieces();
        this.selCard.addPiece(get(0));
        this.game.highlightArea(this.selCard, false);
    }

    public void highlightMeWithSpecifiedCard(GamePiece selectedcard) {
        if (numPieces() == 0) {
            this.game.highlightArea(this, false);
            return;
        }
        this.selCard.clearAllPieces();
        this.selCard.addPiece(selectedcard);
        this.game.highlightArea(this.selCard, false);
    }

    /* access modifiers changed from: package-private */
    public AreaArt getAreaPic() {
        int n;
        if (!this.updatePic) {
            return this.picture;
        }
        this.updatePic = false;
        if (this.points.length < this.pieces.size()) {
            n = this.points.length;
        } else {
            n = this.pieces.size();
        }
        Image[] pics = new Image[n];
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            GamePiece g = (GamePiece) this.pieces.get(i);
            if (!GetViewFronts() && (g instanceof Card)) {
                pics[i] = ((Card) g).getNeutralPic();
            } else if (GetViewFronts() || !(g instanceof GenericCard)) {
                pics[i] = g.getPic();
            } else {
                pics[i] = ((GenericCard) g).getNeutralPic();
            }
            pts[i] = this.points[i];
        }
        this.picture = new AreaArt(pics, pts, "", "", "", false, false);
        return this.picture;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
