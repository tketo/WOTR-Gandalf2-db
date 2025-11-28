package wotr;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

/* renamed from: Area */
public class Area {
    private final boolean _hiddenFromOpponent;
    private boolean _viewFronts;
    GamePieceComparator comp;
    private String name;
    private int[] neighbours;
    protected AreaArt picture;
    protected AreaArt pictureChit;
    protected ArrayList<GamePiece> pieces;
    boolean selectme;
    public boolean tall;
    private String terrain;
    boolean updateChit;
    boolean updatePic;

    public Area(String name2) {
        this(name2, false, true, false);
    }

    public Area(String name2, boolean nationCompare, boolean viewFronts, boolean hiddenFromOpponent) {
        this.pieces = new ArrayList<>();
        this.name = name2;
        this.tall = false;
        this.picture = null;
        this.pictureChit = null;
        this.updatePic = true;
        this.updateChit = true;
        this.selectme = true;
        this._viewFronts = viewFronts;
        this._hiddenFromOpponent = hiddenFromOpponent;
        if (nationCompare) {
            this.comp = new NationComparator();
        } else {
            this.comp = new LevelComparator();
        }
    }

    public Area(String name2, int[] neighbours2) {
        this(name2, false, true, false);
        this.neighbours = neighbours2;
    }

    public void setTerrain(String terrain2) {
        this.terrain = terrain2;
    }

    public int[] getNeighbours() {
        return this.neighbours;
    }

    public String getTerrain() {
        return this.terrain;
    }

    /* access modifiers changed from: package-private */
    public void addPiece(GamePiece p) {
        this.updatePic = true;
        this.updateChit = true;
        int i = 0;
        while (i < this.pieces.size() && this.comp.compare(p, this.pieces.get(i)) < 0) {
            i++;
        }
        this.pieces.add(i, p);
    }

    /* access modifiers changed from: package-private */
    public void removePiece(GamePiece p) {
        this.updatePic = true;
        this.updateChit = true;
        if (!this.pieces.remove(p)) {
            System.out.println("ERROR removing piece " + p.id() + " (" + p.type() + ") from " + this.name + 
                             " - piece not found in area (has " + this.pieces.size() + " pieces)");
        }
    }

    /* access modifiers changed from: package-private */
    public GamePiece randomPiece() {
        return get(DiceRoller.roll(numPieces()) - 1);
    }

    /* access modifiers changed from: package-private */
    public GamePiece GetRandomPiece() {
        return get(DiceRoller.roll(numPieces()) - 1);
    }

    /* access modifiers changed from: package-private */
    public GamePiece randomNationPiece(int searchNation) {
        int availablepieces = 0;
        for (int i = 0; i < this.pieces.size(); i++) {
            if (this.pieces.get(i).nation() == searchNation) {
                availablepieces++;
            }
        }
        int randompiece = DiceRoller.roll(availablepieces) - 1;
        int i2 = 0;
        while (i2 < this.pieces.size()) {
            if (this.pieces.get(i2).nation() == searchNation) {
                randompiece--;
            }
            if (randompiece == -1) {
                break;
            }
            i2++;
        }
        return get(i2);
    }

    /* access modifiers changed from: package-private */
    public boolean containsPiece(GamePiece p) {
        return this.pieces.contains(p);
    }

    public String name() {
        return this.name;
    }

    public boolean GetViewFronts() {
        return this._viewFronts;
    }

    public void SetViewFronts(boolean viewFronts) {
        this._viewFronts = viewFronts;
    }

    public boolean IsHiddenFromOpponent() {
        return this._hiddenFromOpponent;
    }

    public String toString() {
        return name();
    }

    /* access modifiers changed from: package-private */
    public GamePiece get(int i) {
        if (i < 0 || i >= numPieces()) {
            return null;
        }
        return this.pieces.get(i);
    }

    /* access modifiers changed from: package-private */
    public int numPieces() {
        if (this.pieces == null) {
            return 0;
        }
        return this.pieces.size();
    }

    /* access modifiers changed from: package-private */
    public void clearAllPieces() {
        this.updatePic = true;
        this.updateChit = true;
        this.pieces.clear();
    }

    /* access modifiers changed from: package-private */
    public ArrayList<GamePiece> pieces() {
        return this.pieces;
    }

    /* access modifiers changed from: package-private */
    public String contents() {
        return contents(false, (Game) null);
    }

    /* access modifiers changed from: package-private */
    public String contents(boolean revealCards, Game game) {
        String ret = "";
        for (int i = 0; i < this.pieces.size(); i++) {
            GamePiece p = this.pieces.get(i);
            if (((p instanceof Card) || (p instanceof GenericCard)) && revealCards) {
                ret = String.valueOf(ret) + game.GetCardRevealText(p) + " ";
            } else {
                ret = String.valueOf(ret) + p + " ";
            }
        }
        return ret;
    }

    /* access modifiers changed from: package-private */
    public String contentnames() {
        String ret = "";
        for (int i = 0; i < this.pieces.size(); i++) {
            ret = String.valueOf(ret) + this.pieces.get(i).type() + " ";
        }
        return ret;
    }

    /* access modifiers changed from: package-private */
    public String contentRolls() {
        String ret = "";
        for (int i = 0; i < this.pieces.size(); i++) {
            if (get(i) instanceof ActionDie) {
                ret = String.valueOf(ret) + new Integer(((ActionDie) get(i)).Roll()) + " ";
            }
        }
        return ret;
    }

    /* access modifiers changed from: package-private */
    public String contontentSetDiceResult(int[] rolled) {
        String ret = "";
        int N = 0;
        for (int i = 0; i < this.pieces.size(); i++) {
            if (get(i) instanceof ActionDie) {
                N++;
            }
        }
        if (N != rolled.length) {
            System.out.println("An Attempt to roll a wrong number of dice!");
            String str = ret;
            return null;
        }
        for (int i2 = 0; i2 < this.pieces.size(); i2++) {
            if (get(i2) instanceof ActionDie) {
                ret = String.valueOf(ret) + rolled[i2] + " ";
            }
        }
        String str2 = ret;
        return ret;
    }

    /* access modifiers changed from: package-private */
    public void addAllPieces(Area a) {
        if (a != null) {
            this.pieces.addAll(a.pieces());
        }
        this.updatePic = true;
        this.updateChit = true;
    }

    /* access modifiers changed from: package-private */
    AreaArt getAreaPic() {
        int i;
        int j = 0;
        int j2;
        this.updatePic = false;
        int regs = 0;
        int elites = 0;
        int leaders = 0;
        int ents = 0;
        int freefactions = 0;
        int shadowfactions = 0;
        int dunlendings = 0;
        int corsairs = 0;
        GamePiece unitrep1 = null;
        GamePiece unitrep2 = null;
        GamePiece leaderrep = null;
        GamePiece specrep = null;
        GamePiece cardrep = null;
        GamePiece tower = null;
        GamePiece trebuchet = null;
        GamePiece ship = null;
        GamePiece fsprep = null;
        boolean doubleTower = false;
        boolean doubleTrebuchet = false;
        boolean freePresent = false;
        boolean shadowPresent = false;
        int num = 0;
        for (int i2 = 0; i2 < this.pieces.size(); i2++) {
            GamePiece g = get(i2);
            if (g != null) {
                if (g.type().equals(Messages.getString("Area.0")) || g.type().equals(Messages.getString("Area.1")) || g.type().equals(Messages.getString("Area.2")) || g.type().equals(Messages.getString("Area.3")) || (g instanceof UnitFreeControlLarge) || (g instanceof UnitShadowControlLarge) || (((g instanceof UnitEagle) && Game.boardtype == "b5a") || g.type().equals(Messages.getString("Area.22")) || (g instanceof UnitBat))) {
                    g = null;
                } else if (g instanceof Regular) {
                    regs++;
                } else if (g instanceof Elite) {
                    elites++;
                } else if (g instanceof Leader) {
                    leaders++;
                    if (leaderrep == null) {
                        leaderrep = g;
                        g = null;
                    } else if (g.level() > leaderrep.level()) {
                        GamePiece temp = leaderrep;
                        leaderrep = g;
                        g = temp;
                    }
                }
                if (Game.isWOME.booleanValue() && ((g instanceof UnitDeadmen) || (g instanceof UnitEagle) || (g instanceof UnitEnt))) {
                    freefactions++;
                }
                if (Game.isWOME.booleanValue() && ((g instanceof UnitSpider) || (g instanceof UnitCorsair) || (g instanceof UnitDunlending))) {
                    shadowfactions++;
                }
                if (Game.isWOME.booleanValue() && (g instanceof UnitCorsair)) {
                    corsairs++;
                }
                if (Game.isWOME.booleanValue() && (g instanceof UnitDunlending)) {
                    dunlendings++;
                }
                if (g instanceof UnitFellowship) {
                    fsprep = g;
                } else if (g instanceof Card) {
                    cardrep = g;
                } else if ((g instanceof Special) && (specrep == null || (g.level() > specrep.level() && (leaderrep == null || !leaderrep.type().equals(g.type()))))) {
                    specrep = g;
                } else if (((g instanceof Regular) || (g instanceof Elite)) && (unitrep1 == null || g.level() > unitrep1.level())) {
                    unitrep2 = unitrep1;
                    unitrep1 = g;
                } else if (((g instanceof Regular) || (g instanceof Elite)) && ((unitrep2 == null || g.level() > unitrep2.level()) && !unitrep1.type().equals(g.type()))) {
                    unitrep2 = g;
                } else if (g instanceof UnitTower) {
                    doubleTower = tower != null;
                    tower = g;
                } else if (g instanceof UnitTrebuchet) {
                    doubleTrebuchet = trebuchet != null;
                    trebuchet = g;
                }
                if ((g instanceof UnitEnt) && !Game.isWOME.booleanValue()) {
                    ents++;
                }
                if (((g instanceof Regular) || (g instanceof Elite)) && g.nation() < 0) {
                    shadowPresent = true;
                }
                if (((g instanceof Regular) || (g instanceof Elite)) && g.nation() > 0) {
                    freePresent = true;
                }
            }
        }
        if (unitrep1 != null) {
            num = 0 + 1;
        }
        if (unitrep2 != null) {
            num++;
        }
        if (leaderrep != null) {
            num++;
        }
        int totnum = num;
        if (cardrep != null) {
            totnum++;
        }
        if (fsprep != null) {
            totnum++;
        }
        if (specrep != null) {
            totnum++;
        }
        if (tower != null) {
            totnum++;
        }
        if (trebuchet != null) {
            totnum++;
        }
        if (ship != null) {
            totnum++;
        }
        Image[] pics = new Image[totnum];
        if (cardrep != null) {
            i = 0 + 1;
            pics[0] = cardrep.getPic();
        } else {
            i = 0;
        }
        if (unitrep2 != null) {
            pics[i] = unitrep2.getPic();
            i++;
        }
        if (unitrep1 != null) {
            pics[i] = unitrep1.getPic();
            i++;
        }
        if (leaderrep != null) {
            pics[i] = leaderrep.getPic();
            i++;
        }
        if (specrep != null) {
            pics[i] = specrep.getPic();
            i++;
        }
        if (tower != null) {
            if (doubleTower) {
                pics[i] = ((UnitTower) tower).getPic2();
                i++;
            } else {
                pics[i] = tower.getPic();
                i++;
            }
        }
        if (trebuchet != null) {
            if (doubleTrebuchet) {
                pics[i] = ((UnitTrebuchet) trebuchet).getPic2();
                i++;
            } else {
                pics[i] = trebuchet.getPic();
                i++;
            }
        }
        if (ship != null) {
            pics[i] = ship.getPic();
            i++;
        }
        if (fsprep != null) {
            int i3 = i + 1;
            pics[i] = fsprep.getPic();
        }
        Point[] offsets = new Point[totnum];
        if (!this.tall) {
            if (totnum >= 1 && num == 0) {
                offsets[num] = new Point(0, 0);
            } else if (num == 1) {
                offsets[0] = new Point(0, 0);
                if (totnum > num) {
                    offsets[num] = new Point(14, 20);
                }
            } else if (num == 2) {
                offsets[0] = new Point(-15, 0);
                offsets[1] = new Point(15, 0);
                if (totnum > num) {
                    offsets[num] = new Point(5, 20);
                }
            } else if (num == 3) {
                offsets[0] = new Point(-28, 0);
                offsets[1] = new Point(0, 0);
                offsets[2] = new Point(28, 0);
                if (totnum > num) {
                    offsets[num] = new Point(14, 20);
                }
            }
            if (fsprep != null) {
                if (num == 0) {
                    offsets[totnum - 1] = new Point(0, 0);
                } else {
                    offsets[totnum - 1] = new Point(-10, 10);
                }
            }
            int j3 = totnum - 1;
            if (ship == null || !Game.boardtype.equals("wotr")) {
                j = j3;
            } else {
                j = j3 - 1;
                offsets[j3] = new Point(-50, -5);
            }
            if (trebuchet != null) {
                if (Game.boardtype.equals("wotr")) {
                    offsets[j] = new Point(40, 0);
                    j--;
                } else {
                    offsets[j] = new Point(-20, 20);
                    j--;
                }
            }
            if (tower != null) {
                if (!Game.boardtype.equals("wotr")) {
                    int j4 = j - 1;
                    offsets[j] = new Point(-20, 20);
                } else if (this.name.equals(Messages.getString("Game.19")) || this.name.equals(Messages.getString("Game.20")) || this.name.equals(Messages.getString("Game.21")) || this.name.equals(Messages.getString("Game.22")) || this.name.equals(Messages.getString("Game.23")) || this.name.equals(Messages.getString("Game.24")) || this.name.equals(Messages.getString("Game.25")) || this.name.equals(Messages.getString("Game.26"))) {
                    int i4 = j - 1;
                    offsets[j] = new Point(25, -25);
                } else {
                    int i5 = j - 1;
                    offsets[j] = new Point(70, 0);
                }
            }
        } else {
            if (totnum >= 1 && num == 0) {
                offsets[num] = new Point(0, 0);
            } else if (num == 1) {
                if (specrep == null) {
                    offsets[0] = new Point(0, 0);
                } else {
                    offsets[0] = new Point(6, -10);
                    offsets[1] = new Point(-2, 14);
                }
            } else if (num == 2) {
                offsets[0] = new Point(-7, -15);
                offsets[1] = new Point(7, 15);
                if (totnum != num) {
                    offsets[2] = new Point(-10, 20);
                }
            } else if (num == 3) {
                offsets[0] = new Point(-8, -26);
                offsets[1] = new Point(0, 0);
                offsets[2] = new Point(8, 26);
                if (totnum != num) {
                    offsets[3] = new Point(-15, 20);
                }
            }
            if (fsprep != null) {
                offsets[totnum - 1] = new Point(-10, 10);
            }
            int j5 = totnum - 1;
            if (ship == null || !Game.boardtype.equals("wotr")) {
                j2 = j5;
            } else {
                j2 = j5 - 1;
                offsets[j5] = new Point(-50, -5);
            }
            if (trebuchet != null) {
                if (Game.boardtype.equals("wotr")) {
                    offsets[j2] = new Point(40, 0);
                    j2--;
                } else {
                    offsets[j2] = new Point(-20, 20);
                    j2--;
                }
            }
            if (tower != null) {
                if (!Game.boardtype.equals("wotr")) {
                    int j6 = j - 1;
                    offsets[j] = new Point(-20, 20);
                } else if (this.name.equals(Messages.getString("Game.19")) || this.name.equals(Messages.getString("Game.20")) || this.name.equals(Messages.getString("Game.21")) || this.name.equals(Messages.getString("Game.22")) || this.name.equals(Messages.getString("Game.23")) || this.name.equals(Messages.getString("Game.24")) || this.name.equals(Messages.getString("Game.25")) || this.name.equals(Messages.getString("Game.26"))) {
                    int i6 = j - 1;
                    offsets[j] = new Point(25, -25);
                } else {
                    int i7 = j - 1;
                    offsets[j] = new Point(70, 0);
                }
            }
        }
        if (cardrep != null) {
            offsets[offsets.length - 1] = new Point(0, 0);
        }
        String s = "";
        String s2 = "";
        String s3 = "";
        if (!Game.boardtype.equals("wotr") || Game.prefs.showLeadership) {
            leaders = calculateleadership(freePresent, shadowPresent);
        }
        if (unitrep1 != null) {
            s = " " + regs + "/" + elites + "/" + leaders + " ";
            s2 = "[" + regs + "/" + elites + "/" + leaders + "]";
            if (ents > 0) {
                s = String.valueOf(s) + "+" + ents + "E";
                s2 = String.valueOf(s2) + "+" + ents + "E";
            }
        } else if (leaderrep != null) {
            if (ents == 0) {
                s = "  {" + leaders + "}";
                s2 = "{" + leaders + "}";
            } else {
                s = "{" + leaders + "}+" + ents + "E";
                s2 = "{" + leaders + "}+" + ents + "E";
            }
        } else if (ents > 0) {
            s = "   " + ents + "E";
            s2 = String.valueOf(ents) + "E";
        }
        if (Game.isWOME.booleanValue() && (freefactions > 0 || shadowfactions > 0)) {
            s3 = "  " + freefactions + "-" + shadowfactions + " ";
        }
        this.picture = new AreaArt(pics, offsets, s, s2, s3, regs + elites > 10, corsairs > 5 || dunlendings > 3);
        return this.picture;
    }

    private int calculateleadership(boolean freeRequired, boolean shadowRequired) {
        int bestleadership = 0;
        boolean ExtendedLeaders = true;
        boolean isDeadArmy = false;
        
        if (Game.isWOME.booleanValue()) {
            for (int i = 0; i < this.pieces.size(); i++) {
                if (get(i) instanceof UnitDeadmen) {
                    isDeadArmy = true;
                }
            }
        }
        for (int i2 = 0; i2 < this.pieces.size(); i2++) {
            GamePiece unit = get(i2);
            int totalleadership = 0;
            if (((unit.nation() < 0 && shadowRequired) || ((unit.nation() > 0 && freeRequired) || (!shadowRequired && !freeRequired))) && (unit instanceof ExtendedLeader)) {
                if (!Game.isWOME.booleanValue() || !(unit instanceof UnitStrider) || !isDeadArmy) {
                    for (int j = 0; j < this.pieces.size(); j++) {
                        if (j == i2) {
                            totalleadership += ((ExtendedLeader) unit).Leadership();
                        } else {
                            GamePiece otherunit = get(j);
                            if ((otherunit.nation() < 0 && shadowRequired) || ((otherunit.nation() > 0 && freeRequired) || (!shadowRequired && !freeRequired))) {
                                if (otherunit instanceof ExtendedLeader) {
                                    ExtendedLeader unitleadership = (ExtendedLeader) otherunit;
                                    if (ExtendedLeaders) {
                                        totalleadership += unitleadership.AdditionalLeadership();
                                    }
                                } else if ((otherunit instanceof UnitIsengardElite) && Game.boardtype.equals("wotr") && Game.SarumanInIsengard) {
                                    totalleadership++;
                                } else if ((otherunit instanceof UnitElvenElite) && ElrondPresent()) {
                                    totalleadership++;
                                } else if (otherunit instanceof Leader) {
                                    totalleadership++;
                                }
                            }
                        }
                    }
                }
                if (totalleadership > bestleadership) {
                    bestleadership = totalleadership;
                }
            }
        }
        if (bestleadership > 0) {
            int i3 = bestleadership;
            return bestleadership;
        }
        for (int i4 = 0; i4 < this.pieces.size(); i4++) {
            GamePiece unit2 = get(i4);
            if ((unit2 instanceof UnitIsengardElite) && Game.boardtype.equals("wotr") && Game.SarumanInIsengard && shadowRequired) {
                bestleadership++;
            } else if ((unit2 instanceof UnitElvenElite) && this.name.equals(Messages.getKeyString("Game.1911")) && ElrondPresent()) {
                bestleadership++;
            } else if ((unit2 instanceof Leader) && ((!Game.isWOME.booleanValue() || !(unit2 instanceof UnitStrider) || !isDeadArmy) && ((unit2.nation() < 0 && shadowRequired) || (unit2.nation() > 0 && freeRequired)))) {
                bestleadership++;
            }
        }
        int i5 = bestleadership;
        return bestleadership;
    }

    /* access modifiers changed from: package-private */
    public AreaArt getChitPic() {
        if (!this.updateChit) {
            return this.pictureChit;
        }
        this.updateChit = false;
        int regs = 0;
        int elites = 0;
        for (int i = this.pieces.size() - 1; i > -1; i--) {
            GamePiece g = get(i);
            String pieceType = (g != null && g.type() != null) ? g.type() : "";
            if (g != null && (pieceType.equals(Messages.getString("Area.4")) || pieceType.equals(Messages.getString("Area.5")) || pieceType.equals(Messages.getString("Area.6")) || pieceType.equals(Messages.getString("Area.7")) || (g instanceof UnitFreeControlLarge) || (g instanceof UnitShadowControlLarge) || (((g instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType.equals(Messages.getString("Area.23")) || (g instanceof UnitBat)))) {
                if (pieceType.equals(Messages.getString("Area.8")) || pieceType.equals(Messages.getString("Area.9")) || (g instanceof UnitFreeControlLarge) || (g instanceof UnitShadowControlLarge) || (((g instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType.equals("Wrath") || (g instanceof UnitBat))) {
                    regs++;
                } else if (pieceType.equals(Messages.getString("Area.10")) || pieceType.equals(Messages.getString("Area.11"))) {
                    elites++;
                }
            }
        }
        Image[] pics = new Image[(regs + elites)];
        Point[] offsets = new Point[(regs + elites)];
        int recruitcount = 0;
        int damagecount = 0;
        for (int i2 = this.pieces.size() - 1; i2 > -1; i2--) {
            GamePiece g2 = get(i2);
            String pieceType2 = (g2 != null && g2.type() != null) ? g2.type() : "";
            if (g2 != null && ((pieceType2.equals(Messages.getString("Area.12")) || pieceType2.equals(Messages.getString("Area.13")) || pieceType2.equals(Messages.getString("Area.14")) || pieceType2.equals(Messages.getString("Area.15")) || (g2 instanceof UnitFreeControlLarge) || (g2 instanceof UnitShadowControlLarge) || (((g2 instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType2.equals(Messages.getString("Area.26")) || (g2 instanceof UnitBat))) && ((pieceType2.equals(Messages.getString("Area.16")) || pieceType2.equals(Messages.getString("Area.17")) || (g2 instanceof UnitFreeControlLarge) || (g2 instanceof UnitShadowControlLarge) || (((g2 instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType2.equals("Wrath") || (g2 instanceof UnitBat))) && (g2 instanceof TwoChit) && ((TwoChit) g2).currentState()))) {
                pics[recruitcount] = g2.getPic();
                offsets[recruitcount] = new Point(recruitcount * 6, 0);
                recruitcount++;
            }
        }
        for (int i3 = this.pieces.size() - 1; i3 > -1; i3--) {
            GamePiece g3 = get(i3);
            String pieceType3 = (g3 != null && g3.type() != null) ? g3.type() : "";
            if (g3 != null && (pieceType3.equals(Messages.getString("Area.12")) || pieceType3.equals(Messages.getString("Area.13")) || pieceType3.equals(Messages.getString("Area.14")) || pieceType3.equals(Messages.getString("Area.15")) || (g3 instanceof UnitFreeControlLarge) || (g3 instanceof UnitShadowControlLarge) || (((g3 instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType3.equals(Messages.getString("Area.29")) || (g3 instanceof UnitBat)))) {
                if (pieceType3.equals(Messages.getString("Area.16")) || pieceType3.equals(Messages.getString("Area.17")) || (g3 instanceof UnitFreeControlLarge) || (g3 instanceof UnitShadowControlLarge) || (((g3 instanceof UnitEagle) && Game.boardtype == "b5a") || pieceType3.equals("Wrath") || (g3 instanceof UnitBat))) {
                    if (((g3 instanceof TwoChit) && !((TwoChit) g3).currentState()) || !(g3 instanceof TwoChit)) {
                        pics[recruitcount] = g3.getPic();
                        offsets[recruitcount] = new Point(recruitcount * 6, 0);
                        recruitcount++;
                    }
                } else if (pieceType3.equals(Messages.getString("Area.18")) || pieceType3.equals(Messages.getString("Area.19"))) {
                    pics[damagecount + regs] = g3.getPic();
                    offsets[damagecount + regs] = new Point(damagecount * 6, 8);
                    damagecount++;
                }
            }
        }
        this.pictureChit = new AreaArt(pics, offsets, "", "", "", false, false);
        return this.pictureChit;
    }

    private boolean ElrondPresent() {
        boolean present = false;
        for (int i = 0; i < this.pieces.size(); i++) {
            if (get(i) instanceof UnitElrond) {
                present = true;
            }
        }
        return present;
    }
}
