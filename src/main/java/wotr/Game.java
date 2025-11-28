package wotr;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
//import sun.awt.AppContext;

/* renamed from: Game */
public class Game {
    // ========================================================================
    // AREA CONSTANTS - Verified from BoardInit.java and messages.properties
    // ========================================================================
    
    // === MAP REGIONS (0-30) ===
    public static final int MINHIRIATH = 0;
    public static final int SOUTH_ERED_LUIN = 1;
    public static final int CARDOLAN = 2;
    public static final int HARLINDON = 3;
    public static final int GREY_HAVENS = 4;
    public static final int FORLINDON = 5;
    public static final int TOWER_HILLS = 6;
    public static final int THE_SHIRE = 7;
    public static final int ERED_LUIN = 8;
    public static final int OLD_FOREST = 9;
    public static final int NORTH_ERED_LUIN = 10;
    public static final int ENEDWAITH = 11;
    public static final int ARNOR = 12;
    public static final int BUCKLAND = 13;
    public static final int ANGMAR = 14;
    public static final int NORTH_DOWNS = 15;
    public static final int THARBAD = 16;
    public static final int ETTENMOORS = 17;
    public static final int BREE = 18;
    public static final int MOUNT_GRAM = 19;
    public static final int WEATHER_HILLS = 20;
    public static final int DRUWAITH_IAUR = 21;
    public static final int SOUTH_DOWNS = 22;
    public static final int ANDRAST = 23;
    public static final int TROLLSHAWS = 24;
    public static final int ANFALAS = 25;
    public static final int NORTH_DUNLAND = 26;
    public static final int RIVENDELL = 27;
    public static final int HOLLIN = 28;
    public static final int ERECH = 29;
    public static final int SOUTH_DUNLAND = 30;

    // === MAP REGIONS (31-60) ===
    public static final int FORDS_OF_BRUINEN = 31;
    public static final int GAP_OF_ROHAN = 32;
    public static final int HIGH_PASS = 33;
    public static final int GLADDEN_FIELDS = 34;
    public static final int MORIA = 35;
    public static final int DIMRILL_DALE = 36;
    public static final int LORIEN = 37;
    public static final int PARTH_CELEBRANT = 38;
    public static final int FANGORN = 39;
    public static final int ORTHANC = 40;
    public static final int FORDS_OF_ISEN = 41;
    public static final int DOL_AMROTH = 42;
    public static final int EDORAS = 43;
    public static final int MOUNT_GUNDABAD = 44;
    public static final int HELMS_DEEP = 45;
    public static final int LAMEDON = 46;
    public static final int WESTEMNET = 47;
    public static final int PELARGIR = 48;
    public static final int UMBAR = 49;
    public static final int FOLDE = 50;
    public static final int EAGLES_EYRIE = 51;
    public static final int LOSSARNACH = 52;
    public static final int GOBLINS_GATE = 53;
    public static final int EASTEMNET = 54;
    public static final int WEST_HARONDOR = 55;
    public static final int EVENDIM = 56;
    public static final int OLD_FORD = 57;
    public static final int CARROCK = 58;
    public static final int DRUADAN_FOREST = 59;
    public static final int RHOSGOBEL = 60;

    // === MAP REGIONS (61-90) ===
    public static final int NEAR_HARAD = 61;
    public static final int NORTHERN_MIRKWOOD = 62;
    public static final int DOL_GULDUR_REGION = 63;
    public static final int NORTH_ANDUIN_VALE = 64;
    public static final int WESTERN_MIRKWOOD = 65;
    public static final int WESTERN_BROWN_LANDS = 66;
    public static final int EAST_HARONDOR = 67;
    public static final int SOUTH_ANDUIN_VALE = 68;
    public static final int WOODLAND_REALM = 69;
    public static final int WESTERN_RHOVANION = 70;
    public static final int DEAD_MARSHES = 71;
    public static final int EASTERN_BROWN_LANDS = 72;
    public static final int OSGILIATH = 73;
    public static final int MINAS_TIRITH = 74;
    public static final int SOUTH_ITHILIEN = 75;
    public static final int OLD_FOREST_ROAD = 76;
    public static final int NARROWS_OF_FOREST = 77;
    public static final int DOL_GULDUR = 78;
    public static final int EASTERN_MIRKWOOD = 79;
    public static final int WITHERED_HEATH = 80;
    public static final int SOUTHERN_MIRKWOOD = 81;
    public static final int NORTH_ITHILIEN = 82;
    public static final int NORTHERN_RHOVANION = 83;
    public static final int EASTERN_EMYN_MUIL = 84;
    public static final int FAR_HARAD = 85;
    public static final int MINAS_MORGUL = 86;
    public static final int DALE = 87;
    public static final int NURN = 88;
    public static final int NOMAN_LANDS = 89;
    public static final int IRON_HILLS = 90;

    // === MAP REGIONS (91-104) ===
    public static final int MORANNON = 91;
    public static final int SOUTHERN_RHOVANION = 92;
    public static final int DAGORLAD = 93;
    public static final int GORGOROTH = 94;
    public static final int KHAND = 95;
    public static final int BARAD_DUR = 96;
    public static final int VALE_OF_CARNEN = 97;
    public static final int ASH_MOUNTAINS = 98;
    public static final int VALE_OF_CELDUIN = 99;
    public static final int SOUTHERN_DORWINION = 100;
    public static final int EAST_RHUN = 101;
    public static final int NORTHERN_DORWINION = 102;
    public static final int SOUTH_RHUN = 103;
    public static final int NORTH_RHUN = 104;

    // === STRONGHOLDS (105-113) ===
    public static final int EREBOR_STRONGHOLD = 105;
    public static final int GREY_HAVENS_STRONGHOLD = 106;
    public static final int RIVENDELL_STRONGHOLD = 107;
    public static final int WOODLAND_REALM_STRONGHOLD = 108;
    public static final int LORIEN_STRONGHOLD = 109;
    public static final int HELMS_DEEP_STRONGHOLD = 110;
    public static final int MINAS_TIRITH_STRONGHOLD = 111;
    public static final int DOL_AMROTH_STRONGHOLD = 112;
    public static final int SHADOW_STRONGHOLD = 113;

    // === HUNT & FELLOWSHIP (114-116) ===
    public static final int HUNT = 114;
    public static final int FELLOWSHIP = 115;
    public static final int GUIDE = 116;

    // === POLITICAL TRACK (117-120) ===
    public static final int POLITICAL_TRACK_3 = 117;
    public static final int POLITICAL_TRACK_2 = 118;
    public static final int POLITICAL_TRACK_1 = 119;
    public static final int POLITICAL_TRACK_0 = 120;

    // === CHARACTER & STRATEGY DECKS (121-124) ===
    public static final int FP_CHARACTER_DECK = 121;
    public static final int FP_STRATEGY_DECK = 122;
    public static final int SA_CHARACTER_DECK = 123;
    public static final int SA_STRATEGY_DECK = 124;

    // === RING AREAS (125-130) ===
    public static final int ELVEN_RING_1 = 125;
    public static final int ELVEN_RING_2 = 126;
    public static final int ELVEN_RING_3 = 127;
    public static final int DARK_RING_1 = 128;
    public static final int DARK_RING_2 = 129;
    public static final int DARK_RING_3 = 130;

    // === FELLOWSHIP TRACK (131-143) ===
    public static final int FELLOWSHIP_TRACK_0 = 131;
    public static final int FELLOWSHIP_TRACK_1 = 132;
    public static final int FELLOWSHIP_TRACK_2 = 133;
    public static final int FELLOWSHIP_TRACK_3 = 134;
    public static final int FELLOWSHIP_TRACK_4 = 135;
    public static final int FELLOWSHIP_TRACK_5 = 136;
    public static final int FELLOWSHIP_TRACK_6 = 137;
    public static final int FELLOWSHIP_TRACK_7 = 138;
    public static final int FELLOWSHIP_TRACK_8 = 139;
    public static final int FELLOWSHIP_TRACK_9 = 140;
    public static final int FELLOWSHIP_TRACK_10 = 141;
    public static final int FELLOWSHIP_TRACK_11 = 142;
    public static final int FELLOWSHIP_TRACK_12 = 143;

    // === SPECIAL AREAS (144-145) ===
    public static final int SEA = 144;
    public static final int SHADOW_STRONGHOLD_2 = 145;

    // === ON-TABLE CARD SLOTS (146-151) ===
    public static final int BOARD_SLOT_1 = 146;
    public static final int BOARD_SLOT_2 = 147;
    public static final int BOARD_SLOT_3 = 148;
    public static final int BOARD_SLOT_4 = 149;
    public static final int BOARD_SLOT_5 = 150;
    public static final int BOARD_SLOT_6 = 151;

    // === CURRENT ACTION AREAS (152-154) ===
    public static final int CURRENT_ACTION = 152;
    public static final int CURRENT_SA_CARD = 153;
    public static final int CURRENT_FP_CARD = 154;

    // === STRONGHOLD REFERENCES (155-162) ===
    public static final int MINAS_TIRITH_STR = 155;
    public static final int MORIA_STR = 156;
    public static final int DOL_GULDUR_STR = 157;
    public static final int ORTHANC_STR = 158;
    public static final int MORANNON_STR = 159;
    public static final int BARAD_DUR_STR = 160;
    public static final int MINAS_MORGUL_STR = 161;
    public static final int UMBAR_STR = 162;

    // === VICTORY POINT TRACK (163-173) ===
    public static final int VP_0 = 163;
    public static final int VP_1 = 164;
    public static final int VP_2 = 165;
    public static final int VP_3 = 166;
    public static final int VP_4 = 167;
    public static final int VP_5 = 168;
    public static final int VP_6 = 169;
    public static final int VP_7 = 170;
    public static final int VP_8 = 171;
    public static final int VP_9 = 172;
    public static final int VP_10 = 173;

    // === UNIT POOLS & CASUALTY AREAS (174-177) ===
    public static final int FP_REINFORCEMENTS = 174;  // FP Reserve Pool
    public static final int FP_CASUALTIES = 175;      // Eliminated FP units
    public static final int SA_REINFORCEMENTS = 176;  // Shadow Reserve Pool
    public static final int SA_CASUALTIES = 177;      // SAC - Eliminated Shadow units

    // === DICE AREAS (178-179) ===
    public static final int FP_DICE = 178;
    public static final int SA_DICE = 179;

    // === PLAYER HANDS (180-181) ===
    public static final int FP_HAND = 180;
    public static final int SA_HAND = 181;

    // === HUNT TILE AREAS (182-184) ===
    public static final int HUNT_POOL = 182;
    public static final int HUNT_USED = 183;
    public static final int HUNT_REMOVED = 184;

    // === SPARE/STORAGE (185) ===
    public static final int SPARE = 185;  // Also Shadow Character Hand

    // === NOMAN-LANDS (186-187) ===
    public static final int NORTHERN_NOMAN_LANDS = 186;
    public static final int SOUTHERN_NOMAN_LANDS = 187;

    // === CORRUPTION TRACK (188-193) ===
    public static final int CORRUPTION_STEP_0 = 188;
    public static final int CORRUPTION_STEP_1 = 189;
    public static final int CORRUPTION_STEP_2 = 190;
    public static final int CORRUPTION_STEP_3 = 191;
    public static final int CORRUPTION_STEP_4 = 192;
    public static final int CORRUPTION_STEP_5 = 193;

    // === WARRIORS OF MIDDLE-EARTH EXPANSION (194-211) ===
    public static final int FP_FACTION_DECK = 194;
    public static final int FP_FACTION_DISCARD = 195;
    public static final int FP_FACTION_BOTTOM = 196;
    public static final int FP_FACTION_TOP = 197;
    public static final int SA_FACTION_DECK = 198;
    public static final int SA_FACTION_DISCARD = 199;
    public static final int SA_FACTION_BOTTOM = 200;
    public static final int SA_FACTION_TOP = 201;
    public static final int FP_HAND_2 = 202;
    public static final int SA_HAND_2 = 203;
    public static final int BOARD_SLOT_7 = 204;
    public static final int BOARD_SLOT_8 = 205;
    public static final int BOARD_SLOT_9 = 206;
    public static final int BOARD_SLOT_10 = 207;
    public static final int BOARD_SLOT_11 = 208;
    public static final int BOARD_SLOT_12 = 209;
    public static final int BOARD_SLOT_13 = 210;
    public static final int BOARD_SLOT_14 = 211;

    // ========================================================================
    // END OF AREA CONSTANTS
    // ========================================================================

    public static final int WOME_PIECE_COUNT = 111; // 2 faction dice + 48 units + 61 cards
    

    public int[] getOpponentD6stats() {
        return _opponentD6stats;
    }
    public void setOpponentD6stats(int index, int value) {
        if (_opponentD6stats == null) {
            throw new IllegalStateException("Array is not initialized.");
        }
        if (index < 0 || index >= _opponentD6stats.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for array of length " + _opponentD6stats.length);
        }
        _opponentD6stats[index] = value;  // Set the value at the index
    }
    private static int[] _opponentD6stats = new int[6];
    public int[] getPlayerD6stats() {
        return _playerD6stats;
    }
    public void setPlayerD6stats(int index, int value) {
        if (_playerD6stats == null) {
            throw new IllegalStateException("Array is not initialized.");
        }
        if (index < 0 || index >= _playerD6stats.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for array of length " + _playerD6stats.length);
        }
        _playerD6stats[index] = value;  // Set the value at the index
    }
    private static int[] _playerD6stats = new int[6];
    static Area[] areas;
    static Boolean isWOME = false;
    static boolean SarumanInIsengard = false;
    static final File PREF_FILE = new File("wotr_preferences");
    static Preferences prefs = Preferences.loadPrefs(PREF_FILE);
    static String boardtype = "wotr";
    static String varianttype = "expansion";
    static String versionno = "WOTR-Gandalf";
    
    boolean piecesLoadedFromDatabase = false;
    int CairAndrosHP = 3;
    JLabel CairAndrosStructure;
    int EasternSpurHP = 3;
    JLabel EasternSpurStructure;
    int EdorasHP = 3;
    JLabel EdorasStructure;
    boolean FSPchosen;
    JLabel FSPreminder;
    int FrontGateHP = 6;
    JLabel FrontGateStructure;
    boolean Gandalfplayed = false;
    int GothmogNo;
    int GreatGateHP = 6;
    int WitckKingNo;
    JLabel GreatGateStructure;
    boolean GrimasRemoved = false;
    int HornburgHP = 6;
    JLabel HornburgStructure;
    int NCityWallHP = 10;
    JLabel NCityWallStructure;
    int OrthancHP = 8;
    JLabel OrthancStructure;
    int RavenHillHP = 3;
    JLabel RavenHillStructure;
    int SarumanNo;
    JLabel ShadowWK;
    int SouthRammasHP = 10;
    JLabel SouthRammasStructure;
    int TSupCard;
    boolean Theodenplayed = false;
    boolean TreeBeardplayed = false;
    private ArrayList<Card> _FPCcards;
    private ArrayList<FreeFactionCard> _FPFcards;
    private ArrayList<FreeStrategyCard> _FPScards;
    private HandSecurity _FPsecurity = new HandSecurity();
    private ArrayList<Card> _SPCcards;
    private ArrayList<ShadowFactionCard> _SPFcards;
    private ArrayList<Card> _SPScards;
    private HandSecurity _SPsecurity = new HandSecurity();
    private ArrayList<Card> _abstractFPCcards;
    private ArrayList<FreeFactionCard> _abstractFPFcards;
    private ArrayList<FreeStrategyCard> _abstractFPScards;
    private ArrayList<GenericCard> _abstractGenericCards;
    private ArrayList<Card> _abstractSPCcards;
    private ArrayList<ShadowFactionCard> _abstractSPFcards;
    private ArrayList<Card> _abstractSPScards;
    public boolean allowObserverCommands = false;
    Point[] areaChits;
    Point[] areaControlPoints;
    Point[] areaCoords;
    int[] areaIDs;
    GamePiece[] bits;
    GamePiece[] dbPieces = new GamePiece[1000];
    int dbPieceCount = 0;

    /* renamed from: bl */
    BoardLabel boardlabel;
    String board = Messages.getLanguageLocation("images/board.jpg");
    String challengeFreeAction = "";
    int challengeFreeActionPos = -1;
    String challengeFreeCombat = "";
    int challengeFreeCombatPos = -1;
    String challengeShadowAction = "";
    int challengeShadowActionPos = -1;
    String challengeShadowCombat = "";
    int challengeShadowCombatPos = -1;
    GamePiece chosen1;
    String chosen1player;
    GamePiece chosen2;
    String chosen2player;
    public boolean circle;
    public int circleX;
    public int circleY;
    Clip clip = null;
    boolean connectsound = false;
    Controls controls;
    JDialog dialog = new JDialog(win);
    public SecureDiceRoller diceRoller = new SecureDiceRoller(this);
    boolean fHaveRecovered;
    boolean fHaveRolled;
    boolean gameloading = false;
    String handViewing;
    Area highlight;
    ArrayList<GamePiece[]> history = new ArrayList<>();
    int historyactionpointer;
    ArrayList<String> historyactions = new ArrayList<>();
    int historypointer;
    boolean inaction;
    Interpreter interpreter;
    private BoardInit boardInit;
    JScrollPane jsp;
    int maxboardheight = 688;
    int maxboardwidth = 996;
    Mod mod;
    boolean newgame = true;
    int numBits;
    public ObserverClient observerClient = new ObserverClient(this);
    public List<ObserverHost> observers = new LinkedList<ObserverHost>();
    public boolean observersExist = false;
    String opponent = "";
    private int opponentActivityAnimationStage = 0;
    private Timer opponentActivityAnimationTimer;
    String opponentHandViewing;
    String overlay = Messages.getLanguageLocation("images/map_mask.png");
    public RemoteSecureDiceRoller remoteDiceRoller = new RemoteSecureDiceRoller(this);
    boolean sHaveRecovered;
    boolean sHaveRolled;
    Scribe scribe;
    Area selection;
    String shadowname;
    public Image[] siegeOverlays;
    boolean soundon = true;
    boolean synchronizing;
    Talker talker;
    String translate = "";
    int turn;
    boolean waiting;
    static JFrame win;
    String windowTitle;
    
    // Database integration (Phase 6)
    private wotr.services.GameStateService gameStateService;
    private wotr.turn.TurnManager turnManager;
    
    // FSM server integration (turn/phase arbitration - superseded Phase 5 UI components)
    public FsmConnection fsmConnection;
    private wotr.mapping.GameBoardQuery boardQuery;
    private String currentGameId;  // Current game session ID

    // Getters
    public ArrayList<Card> getFPCcards() {
        return _FPCcards;
    }

    public void setFPCcards(ArrayList<Card> cards) {
        this._FPCcards = cards;
    }

    public ArrayList<Card>  getAbstractFPCcards() {
        return _abstractFPCcards;
    }

    public void setAbstractFPCcards(ArrayList<Card> cards) {
        this._abstractFPCcards = cards;
    }

    public ArrayList<FreeStrategyCard> getFPScards() {
        return _FPScards;
    }

    public void setFPScards(ArrayList<FreeStrategyCard> cards) {
        this._FPScards = cards;
    }

    public ArrayList<FreeStrategyCard>  getAbstractFPScards() {
        return _abstractFPScards;
    }

    public void setAbstractFPScards(ArrayList<FreeStrategyCard>  cards) {
        this._abstractFPScards = cards;
    }

    public ArrayList<FreeFactionCard> getFPFcards() {
        return _FPFcards;
    }

    public void setFPFcards(ArrayList<FreeFactionCard> cards) {
        this._FPFcards = cards;
    }

    public ArrayList<FreeFactionCard> getAbstractFPFcards() {
        return _abstractFPFcards;
    }

    public void setAbstractFPFcards(ArrayList<FreeFactionCard> cards) {
        this._abstractFPFcards = cards;
    }

    public ArrayList<Card> getSPCcards() {
        return _SPCcards;
    }

    public void setSPCcards(ArrayList<Card> cards) {
        this._SPCcards = cards;
    }

    public ArrayList<Card> getAbstractSPCcards() {
        return _abstractSPCcards;
    }

    public void setAbstractSPCcards(ArrayList<Card> cards) {
        this._abstractSPCcards = cards;
    }

    public ArrayList<Card> getSPScards() {
        return _SPScards;
    }

    public void setSPScards(ArrayList<Card> cards) {
        this._SPScards = cards;
    }

    public ArrayList<Card> getAbstractSPScards() {
        return _abstractSPScards;
    }

    public void setAbstractSPScards(ArrayList<Card> cards) {
        this._abstractSPScards = cards;
    }

    public ArrayList<ShadowFactionCard> getSPFcards() {
        return _SPFcards;
    }

    public void setSPFcards(ArrayList<ShadowFactionCard> cards) {
        this._SPFcards = cards;
    }

    public ArrayList<ShadowFactionCard> getAbstractSPFcards() {
        return _abstractSPFcards;
    }

    public void setAbstractSPFcards(ArrayList<ShadowFactionCard> cards) {
        this._abstractSPFcards = cards;
    }

    public void setTurn(int n) {
        for (int p = 0; p <= this.bits.length - 1; p++) {
            GamePiece gp = this.bits[p];
            if (gp instanceof UnitMouth) {
                ((UnitMouth) gp).defaultSide();
            }
        }
        this.fHaveRolled = false;
        this.sHaveRolled = false;
        this.turn = n;
        boardInit.redoTitle();
        if (!this.synchronizing && !this.interpreter.fromFile) {
            this.controls.chat.write(String.valueOf(Messages.getString("Game.7")) + n);
            this.interpreter.record(String.valueOf(Messages.getString("Game.7")) + n);
        }
    }

    /* renamed from: Game$winCloser */
    private class winCloser extends WindowAdapter {
        public winCloser() {
        }

        public void windowClosing(WindowEvent e) {
            if (JOptionPane.showConfirmDialog((JFrame) e.getSource(), Messages.getString("Game.8"), Messages.getString("Game.9"), 0, 2) == 0) {
                Game.prefs.winx = Game.win.getWidth();
                Game.prefs.winy = Game.win.getHeight();
                Game.prefs.save(Game.PREF_FILE);
                Game.this.exit();
            }
        }
    }

    // /**
    //  * Create a headless game instance for dumping purposes only
    //  * Initializes only areas and pieces, skipping database/GUI/FSM
    //  */
    public static Game createHeadlessGame(String variant) {
        Game game = new Game(true); // Use private constructor
        Game.varianttype = variant;
        Game.isWOME = variant.contains("[W");
        
        // Initialize arrays needed for areaInit and bitInit
        if (Game.isWOME) {
            areas = new Area[212];
            game.areaIDs = new int[212];
            game.areaCoords = new Point[212];
            game.areaChits = new Point[212];
            game.areaControlPoints = new Point[212];
        } else {
            areas = new Area[194];
            game.areaIDs = new int[194];
            game.areaCoords = new Point[194];
            game.areaChits = new Point[194];
            game.areaControlPoints = new Point[194];
        }
        
        // Calculate piece counts
        int treeBeard = variant.endsWith("T]") ? 1 : 0;
        int factionCount = variant.contains("W") ? WOME_PIECE_COUNT : 0;
        
        // Allocate bits array
        if (variant.startsWith("expansion2")) {
            game.bits = new GamePiece[(treeBeard + 453 + factionCount)];
        } else {
            game.bits = new GamePiece[(treeBeard + 428 + factionCount)];
        }
        
        // Initialize areas and pieces
        game.boardInit = new BoardInit(game);
        game.boardInit.areaInit();
        game.boardInit.hybridPieceInit(); // This will run bitInit() since no database
        
        return game;
    }
    
    /**
     * Private constructor for headless mode
     */
    private Game(boolean headless) {
        this.siegeOverlays = new Image[16];
        // Note: prefs is already initialized as a static field
    }
    
    public Game() {
        //AppContext.getAppContext().put(SwingWorker.class, Executors.newCachedThreadPool());
        
        // Initialize database services first
        initializeDatabaseServices();
        
        // Initialize static areas array based on variant (must be done before areaInit)
        boolean hasWOME = prefs.lastGame.contains("[W");
        isWOME = hasWOME;  // Set static flag early to match array allocation
        if (hasWOME) {
            areas = new Area[212];
            this.areaIDs = new int[212];
            this.areaCoords = new Point[212];
            this.areaChits = new Point[212];
            this.areaControlPoints = new Point[212];
        } else {
            areas = new Area[194];
            this.areaIDs = new int[194];
            this.areaCoords = new Point[194];
            this.areaChits = new Point[194];
            this.areaControlPoints = new Point[194];
        }
        
        // Initialize board and areas
        this.siegeOverlays = new Image[16];
        this.boardInit = new BoardInit(this);
        this.boardInit.areaInit();
        
        // Note: prefs is already initialized as a static field (line 512)
        if (prefs.lastGame.startsWith("base2")) {
            boardInit.newBase2(prefs.lastGame);
        }
        if (prefs.lastGame.startsWith("newLOME")) {
            boardInit.newLOME(prefs.lastGame);
        }
        if (prefs.lastGame.startsWith("newWOME")) {
            boardInit.newWOME(prefs.lastGame);
            boardInit.refreshBoard();
        }
        if (prefs.iponstart) {
            showAddress();
        }
        String source = prefs.disableDatabase ? "_bitinit" : "_db";
        boardInit.exportBitsArrayToJson("data\\bits_array_" + varianttype + source + ".json");  // Export to file
        boardInit.AnonymizeDecks();

        // Initialize FSM connection (not connected yet, user must choose to connect)
        fsmConnection = new FsmConnection(this);
    }
    
    /**
     * Initialize database services for game state management
     * Phase 6: Database Integration
     */
    private void initializeDatabaseServices() {
        try {
            System.out.println("[Phase 6] Initializing database services...");
            
            // Create game state service
            gameStateService = new wotr.services.GameStateService();
            
            // Create new game session
            currentGameId = gameStateService.createNewGame("free_peoples");
            System.out.println("[Phase 6] Game session initialized: " + currentGameId);
            
            // Create turn manager with database
            turnManager = new wotr.turn.TurnManager(gameStateService);
            System.out.println("[Phase 6] Turn/phase system initialized");
            
            // Initialize board query service for database-driven board state
            boardQuery = wotr.mapping.GameBoardQuery.getInstance();
            boardQuery.setGameState(gameStateService);
            System.out.println("[Phase 6] Board query service initialized (105 regions mapped)");
            
            // Register movement listener to intercept all piece movements (Phase 4)
            GamePiece.setMovementListener(new GamePiece.MovementListener() {
                @Override
                public void onPieceMoved(GamePiece piece, Area from, Area to) {
                    // Sync to database
                    syncUnitMoveToDatabase(piece, from, to);
                    
                    // Notify FSM server if connected
                    if (fsmConnection != null && fsmConnection.isConnected()) {
                        // Simple generic event - FSM can query DB for details if needed
                        fsmConnection.sendEvent("PIECE_MOVED");
                    }
                }
            });
            System.out.println("[Phase 6] Movement listener registered - all moves will sync to database");
            
        } catch (Exception e) {
            System.err.println("[Phase 6] Warning: Database initialization failed: " + e.getMessage());
            e.printStackTrace();
            // Game continues without database (graceful degradation)
            gameStateService = null;
            turnManager = null;
            boardQuery = null;
        }
    }
    
    /**
     * Sync units from database to bits[] array (Hybrid Approach - Phase 2)
     * Loads all units from database and creates corresponding GamePiece objects
     */
    private void syncUnitsFromDatabase() {
        if (boardQuery == null || gameStateService == null) {
            System.out.println("[Phase 6] Skipping database sync - services not available");
            return;
        }
        
        try {
            System.out.println("[Phase 6] Syncing units from database...");
            int unitsCreated = 0;
            
            // Query all map regions (areas 0-104)
            for (int areaIndex = 0; areaIndex <= 104; areaIndex++) {
                if (!boardQuery.isMapRegion(areaIndex)) continue;
                
                // Get units in this region from database
                java.util.List<wotr.models.ArmyUnit> units = boardQuery.getUnitsInArea(areaIndex);
                
                for (wotr.models.ArmyUnit unit : units) {
                    // Create 'count' number of unit pieces
                    for (int i = 0; i < unit.getCount(); i++) {
                        GamePiece piece = createUnitFromDatabase(unit, areas[areaIndex]);
                        if (piece != null) {
                            unitsCreated++;
                        }
                    }
                }
            }
            
            System.out.println("[Phase 6] Database sync complete: " + unitsCreated + " units loaded");
            
        } catch (Exception e) {
            System.err.println("[Phase 6] Error syncing from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Factory method to create GamePiece from database ArmyUnit
     */
    private GamePiece createUnitFromDatabase(wotr.models.ArmyUnit unit, Area location) {
        int nationId = unit.getNationId();
        String unitType = unit.getUnitType();
        
        try {
            // Create appropriate unit class based on nation and type
            switch (nationId) {
                case 1: // Dwarves
                    if ("regular".equals(unitType)) {
                        return new UnitDwarvenRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitDwarvenElite(location);
                    } else if ("leader".equals(unitType)) {
                        return new UnitDwarvenLeader(location);
                    }
                    break;
                    
                case 2: // Elves
                    if ("regular".equals(unitType)) {
                        return new UnitElvenRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitElvenElite(location);
                    } else if ("leader".equals(unitType)) {
                        return new UnitElvenLeader(location);
                    }
                    break;
                    
                case 3: // Gondor
                    if ("regular".equals(unitType)) {
                        return new UnitGondorRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitGondorElite(location);
                    } else if ("leader".equals(unitType)) {
                        return new UnitGondorLeader(location);
                    }
                    break;
                    
                case 4: // The North
                    if ("regular".equals(unitType)) {
                        return new UnitNorthRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitNorthElite(location);
                    } else if ("leader".equals(unitType)) {
                        return new UnitNorthLeader(location);
                    }
                    break;
                    
                case 5: // Rohan
                    if ("regular".equals(unitType)) {
                        return new UnitRohanRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitRohanElite(location);
                    } else if ("leader".equals(unitType)) {
                        return new UnitRohanLeader(location);
                    }
                    break;
                    
                case 6: // Isengard
                    if ("regular".equals(unitType)) {
                        return new UnitIsengardRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitIsengardElite(location);
                    }
                    break;
                    
                case 7: // Sauron
                    if ("regular".equals(unitType)) {
                        return new UnitSauronRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitSauronElite(location);
                    }
                    break;
                    
                case 8: // Southrons & Easterlings
                    if ("regular".equals(unitType)) {
                        return new UnitSouthronRegular(location);
                    } else if ("elite".equals(unitType)) {
                        return new UnitSouthronElite(location);
                    }
                    break;
            }
            
            System.err.println("[Phase 6] Warning: Unknown unit type - nation:" + nationId + " type:" + unitType);
            return null;
            
        } catch (Exception e) {
            System.err.println("[Phase 6] Error creating unit: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Sync piece movement TO database (Unified Approach)
     * ALL pieces use the same logic - one piece = one row in game_pieces table
     */
    private void syncUnitMoveToDatabase(GamePiece piece, Area fromArea, Area toArea) {
        if (boardQuery == null || gameStateService == null) {
            return; // Database not available
        }
        
        try {
            // Get piece ID (index in bits[] array)
            int pieceId = getPieceIdFromArray(piece);
            if (pieceId < 0) {
                return; // Piece not found in bits[] array
            }
            
            // Get destination area index
            int toIndex = getAreaIndex(toArea);
            if (toIndex < 0) {
                return; // Invalid area
            }
            
            // Simple sync: Update ONE row in database
            // UPDATE game_pieces SET area_id = toIndex WHERE piece_id = pieceId
            syncPieceLocation(pieceId, toIndex);
            
            // Log for debugging
            String pieceInfo = getPieceTypeDescription(piece);
            System.out.println("[Sync] Piece " + pieceId + " (" + pieceInfo + ") moved to area " + toIndex);
            
        } catch (Exception e) {
            System.err.println("[Sync] Error updating piece location: " + e.getMessage());
        }
    }
    
    /**
     * Find piece ID (index in bits[] array)
     */
    private int getPieceIdFromArray(GamePiece piece) {
        if (piece == null || bits == null) return -1;
        
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == piece) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Initialize game_pieces table from bits[] array
     * Call this after game board is set up
     */
    public void initializeGamePiecesDatabase() {
        if (currentGameId == null || bits == null) {
            System.out.println("[DB] Skipping piece initialization - no active game");
            return;
        }
        
        System.out.println("[DB] Initializing game_pieces table from bits[] array...");
        int piecesInitialized = 0;
        int piecesSkipped = 0;
        
        for (int pieceId = 0; pieceId < bits.length; pieceId++) {
            GamePiece piece = bits[pieceId];
            
            // Skip null pieces
            if (piece == null) {
                continue;
            }
            
            // Get piece's current area
            Area currentArea = piece.currentLocation();
            if (currentArea == null) {
                piecesSkipped++;
                continue;
            }
            
            // Find area index
            int areaId = getAreaIndex(currentArea);
            if (areaId < 0) {
                piecesSkipped++;
                continue;
            }
            
            // Insert piece into database
            try {
                insertPieceLocation(pieceId, areaId);
                piecesInitialized++;
                
                //System.out.println(pieceId + ":" + areaId + ":" + piece.type());
                
            } catch (Exception e) {
                System.err.println("[DB] Error initializing piece " + pieceId + ": " + e.getMessage());
                piecesSkipped++;
            }
        }
        
        System.out.println("[DB] Piece initialization complete:");
        System.out.println("  - Pieces initialized: " + piecesInitialized);
        System.out.println("  - Pieces skipped: " + piecesSkipped);
        System.out.println("  - Total pieces in bits[]: " + bits.length);
    }
    
    // ============================================
    // DATABASE QUERY METHODS
    // ============================================
    
    /**
     * Query all pieces in a specific area
     * @param areaId The area to query
     * @return List of piece IDs in that area
     */
    public java.util.List<Integer> getPiecesInArea(int areaId) {
        java.util.List<Integer> pieceIds = new java.util.ArrayList<>();
        
        if (currentGameId == null) {
            return pieceIds; // Return empty list if no active game
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            String sql = "SELECT piece_id FROM game_pieces WHERE game_id = ? AND area_id = ? ORDER BY piece_type, piece_id";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            stmt.setInt(2, areaId);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pieceIds.add(rs.getInt("piece_id"));
            }
            
            rs.close();
            stmt.close();
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error querying pieces in area " + areaId + ": " + e.getMessage());
        }
        
        return pieceIds;
    }
    
    /**
     * Query all pieces in a specific area by type
     * @param areaId The area to query
     * @param pieceType The piece type to filter by (e.g. "regular", "elite", "fellowship")
     * @return List of piece IDs matching the criteria
     */
    public java.util.List<Integer> getPiecesInAreaByType(int areaId, String pieceType) {
        java.util.List<Integer> pieceIds = new java.util.ArrayList<>();
        
        if (currentGameId == null) {
            return pieceIds;
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            String sql = "SELECT piece_id FROM game_pieces WHERE game_id = ? AND area_id = ? AND piece_type = ? ORDER BY piece_id";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            stmt.setInt(2, areaId);
            stmt.setString(3, pieceType);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pieceIds.add(rs.getInt("piece_id"));
            }
            
            rs.close();
            stmt.close();
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error querying pieces: " + e.getMessage());
        }
        
        return pieceIds;
    }
    
    /**
     * Query the location of a specific piece
     * @param pieceId The piece ID to query
     * @return The area ID where the piece is located, or -1 if not found
     */
    public int getPieceLocation(int pieceId) {
        if (currentGameId == null) {
            return -1; // No active game
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            String sql = "SELECT area_id FROM game_pieces WHERE game_id = ? AND piece_id = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            stmt.setInt(2, pieceId);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            int areaId = -1;
            if (rs.next()) {
                areaId = rs.getInt("area_id");
            }
            
            rs.close();
            stmt.close();
            
            return areaId;
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error querying piece location for piece " + pieceId + ": " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Count all pieces in a specific area
     * @param areaId The area to query
     * @return Number of pieces in that area
     */
    public int countPiecesInArea(int areaId) {
        return countPiecesInArea(areaId, null);
    }
    
    /**
     * Count pieces in a specific area with optional filter
     * @param areaId The area to query
     * @param pieceType The piece type to filter by, or null for all pieces
     * @return Number of pieces matching the criteria
     */
    public int countPiecesInArea(int areaId, String pieceType) {
        if (currentGameId == null) {
            return 0;
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            String sql;
            java.sql.PreparedStatement stmt;
            
            if (pieceType == null) {
                // Count all pieces in area
                sql = "SELECT COUNT(*) as count FROM game_pieces WHERE game_id = ? AND area_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, currentGameId);
                stmt.setInt(2, areaId);
            } else {
                // Count pieces of specific type in area
                sql = "SELECT COUNT(*) as count FROM game_pieces WHERE game_id = ? AND area_id = ? AND piece_type = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, currentGameId);
                stmt.setInt(2, areaId);
                stmt.setString(3, pieceType);
            }
            
            java.sql.ResultSet rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("count");
            }
            
            rs.close();
            stmt.close();
            
            return count;
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error counting pieces in area " + areaId + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Count military units (regular, elite, leader) in a specific area
     * @param areaId The area to query
     * @return Number of military units in that area
     */
    public int countMilitaryUnitsInArea(int areaId) {
        if (currentGameId == null) {
            return 0;
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            String sql = "SELECT COUNT(*) as count FROM game_pieces " +
                        "WHERE game_id = ? AND area_id = ? AND piece_type IN ('regular', 'elite', 'leader')";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            stmt.setInt(2, areaId);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("count");
            }
            
            rs.close();
            stmt.close();
            
            return count;
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error counting military units in area " + areaId + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Update piece location in database (unified approach)
     * Uses UPSERT to avoid race conditions during rapid updates
     */
    private void syncPieceLocation(int pieceId, int areaId) {
        if (currentGameId == null || pieceId >= bits.length || bits[pieceId] == null) {
            return; // No active game session or invalid piece
        }
        
        try {
            GamePiece piece = bits[pieceId];
            
            // Determine piece type
            String pieceType = getPieceTypeForDatabase(piece);
            
            // Determine nation (if applicable)
            Integer nationId = null;
            int gameNationId = piece.nation();
            if (gameNationId > 0) {
                nationId = mapGameNationToDb(gameNationId);
            }
            
            // Get database connection
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            // Use INSERT OR REPLACE (UPSERT) to avoid race conditions
            // This will either insert a new record or update the existing one
            String sql = "INSERT OR REPLACE INTO game_pieces " +
                        "(piece_id, game_id, piece_type, area_id, nation_id, state_data) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pieceId);
            stmt.setString(2, currentGameId);
            stmt.setString(3, pieceType);
            stmt.setInt(4, areaId);
            
            if (nationId != null) {
                stmt.setInt(5, nationId);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setString(6, "{}"); // Empty JSON for now
            
            stmt.executeUpdate();
            stmt.close();
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error syncing piece location: " + e.getMessage());
        }
    }
    
    /**
     * Insert new piece into database (first time initialization)
     * Delegates to syncPieceLocation to use UPSERT logic
     */
    private void insertPieceLocation(int pieceId, int areaId) {
        // Use the same UPSERT logic as syncPieceLocation to avoid duplicate key errors
        syncPieceLocation(pieceId, areaId);
    }
    
    /**
     * Map piece class to database piece_type string
     */
    private String getPieceTypeForDatabase(GamePiece piece) {
        if (piece instanceof Regular) return "regular";
        if (piece instanceof Elite) return "elite";
        if (piece instanceof Leader) return "leader";
        if (piece instanceof UnitFellowship) return "fellowship";
        
        if (piece instanceof TwoChit) {
            TwoChit tc = (TwoChit) piece;
            if ("FSP".equals(tc.type())) return "fellowship_counter";
            return "nation_chit";
        }
        
        if (piece instanceof Chit) {
            Chit c = (Chit) piece;
            if (c.type() != null && c.type().contains("Corruption")) return "corruption_counter";
            return "marker";
        }
        
        // Default for other types
        return "other";
    }
    
    /**
     * Map game nation IDs to database nation IDs
     */
    private Integer mapGameNationToDb(int gameNationId) {
        switch (gameNationId) {
            case 1: return 1;  // Dwarves
            case 9: return 2;  // Elves
            case 8: return 3;  // Gondor
            case 10: return 4; // The North
            case 11: return 5; // Rohan
            case 7: return 6;  // Isengard
            case 6: return 7;  // Sauron
            case 12: return 8; // Southrons & Easterlings
            default: return null;
        }
    }
    
    /**
     * Get human-readable piece description for logging
     */
    private String getPieceTypeDescription(GamePiece piece) {
        if (piece instanceof Regular) return "Regular";
        if (piece instanceof Elite) return "Elite";
        if (piece instanceof Leader) return "Leader";
        if (piece instanceof UnitFellowship) return "Ring Bearers";
        if (piece instanceof TwoChit) {
            TwoChit tc = (TwoChit) piece;
            return "TwoChit(" + tc.type() + ")";
        }
        if (piece instanceof Chit) {
            Chit c = (Chit) piece;
            return "Chit(" + c.type() + ")";
        }
        return piece.getClass().getSimpleName();
    }
    
    /**
     * Get area index from Area object
     */
    private int getAreaIndex(Area area) {
        if (area == null || areas == null) return -1;
        
        for (int i = 0; i < areas.length; i++) {
            if (areas[i] == area) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Phase 1: Refresh game state from database
     * 
     * Reads current piece positions from game_pieces table and updates
     * in-memory piece objects to match. This makes Game.java a "view" of
     * the database state rather than the source of truth.
     * 
     * Usage:
     * - Call after loading a saved game
     * - Call after network sync in multiplayer
     * - Call to verify state matches database
     * 
     * Note: Currently only updates piece locations. Future versions will
     * also sync piece states (flipped, revealed, etc.)
     */
    public void refreshFromDatabase() {
        if (currentGameId == null) {
            System.out.println("[DB] Cannot refresh - no active game session");
            return;
        }
        
        if (bits == null || areas == null) {
            System.out.println("[DB] Cannot refresh - game not initialized");
            return;
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            // Query all pieces for current game
            String sql = "SELECT piece_id, area_id, piece_type, state_data " +
                        "FROM game_pieces WHERE game_id = ? ORDER BY piece_id";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            int piecesRefreshed = 0;
            int piecesMoved = 0;
            
            while (rs.next()) {
                int pieceId = rs.getInt("piece_id");
                int areaId = rs.getInt("area_id");
                String stateData = rs.getString("state_data");
                
                // Validate piece exists
                if (pieceId >= bits.length || bits[pieceId] == null) {
                    System.err.println("[DB] Warning: Piece " + pieceId + " in database but not in bits[]");
                    continue;
                }
                
                // Validate area exists
                if (areaId < 0 || areaId >= areas.length || areas[areaId] == null) {
                    System.err.println("[DB] Warning: Invalid area " + areaId + " for piece " + pieceId);
                    continue;
                }
                
                GamePiece piece = bits[pieceId];
                Area targetArea = areas[areaId];
                
                // Check if piece needs to be moved
                if (piece.currentLocation() != targetArea) {
                    // Remove from old area
                    if (piece.currentLocation() != null) {
                        piece.currentLocation().removePiece(piece);
                    }
                    
                    // Add to new area and update piece location
                    piece.setLocation(targetArea);
                    targetArea.addPiece(piece);
                    piecesMoved++;
                }
                
                // TODO: Parse and apply state_data JSON (flipped, revealed, etc.)
                // For now, just update location
                
                piecesRefreshed++;
            }
            
            rs.close();
            stmt.close();
            
            System.out.println("[DB] Refreshed " + piecesRefreshed + " pieces from database (" + piecesMoved + " moved)");
            
            // Trigger UI repaint if needed
            if (piecesMoved > 0 && boardlabel != null) {
                boardlabel.repaint();
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error refreshing from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verify game state matches database
     * Useful for debugging and testing
     * 
     * @return true if all pieces match database positions
     */
    public boolean verifyDatabaseSync() {
        if (currentGameId == null || bits == null || areas == null) {
            return false;
        }
        
        try {
            java.sql.Connection conn = wotr.database.DatabaseManager.getInstance().getConnection();
            
            String sql = "SELECT piece_id, area_id FROM game_pieces WHERE game_id = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentGameId);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            int mismatches = 0;
            int checked = 0;
            
            while (rs.next()) {
                int pieceId = rs.getInt("piece_id");
                int dbAreaId = rs.getInt("area_id");
                
                if (pieceId >= bits.length || bits[pieceId] == null) {
                    continue;
                }
                
                GamePiece piece = bits[pieceId];
                int memoryAreaId = getAreaIndex(piece.currentLocation());
                
                if (memoryAreaId != dbAreaId) {
                    System.err.println("[DB] Mismatch: Piece " + pieceId + 
                                     " in memory area " + memoryAreaId + 
                                     " but database area " + dbAreaId);
                    mismatches++;
                }
                checked++;
            }
            
            rs.close();
            stmt.close();
            
            if (mismatches == 0) {
                System.out.println("[DB] Verification passed: " + checked + " pieces match database");
                return true;
            } else {
                System.err.println("[DB] Verification failed: " + mismatches + " mismatches out of " + checked + " pieces");
                return false;
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("[DB] Error verifying database sync: " + e.getMessage());
            return false;
        }
    }
    
    public void showAddress() {
        String i;
        try {
            this.controls.chat.write("<address> INTERNET: " + new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e12) {
            e12.printStackTrace();
        }
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c ipconfig");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String Search = "Hamachi";
            String Search2 = "Hamachi";
            while (true) {
                i = bufferedReader.readLine();
                if (i == null) {
                    break;
                } else if (i.indexOf(Search) > 0 || i.indexOf(Search2) > 0) {
                    if (Search.equals("IPv4") || Search2.equals("IP Address")) {
                        this.controls.chat.write("<address> HAMACHI: " + i.substring(i.indexOf(":") + 1));
                    } else {
                        Search2 = "IP Address";
                        Search = "IPv4";
                    }
                }
            }
            if ( i != null) {
                this.controls.chat.write("<address> HAMACHI: " + i.substring(i.indexOf(":") + 1));
            }
            pro.waitFor();
        } catch (IOException e) {
        } catch (InterruptedException e2) {
        }
        this.controls.chat.write("<address> NETWORK: " + Talker.myAddress());
    }

    public void gameInit() {
        int TreeBeard = 0;
        prefs = Preferences.loadPrefs(PREF_FILE);
        isWOME = false;
        if (varianttype.contains("W")) {
            isWOME = true;
        }
        this.opponent = "";
        this.handViewing = "";
        this.opponentHandViewing = "";
        this.maxboardwidth = 996;
        this.maxboardheight = 688;
        SarumanInIsengard = false;
        this.opponentActivityAnimationTimer = new Timer(350, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game.this.controls.chat.setStatus(Game.this.getOpponentBusyString());
            }
        });
        this.opponentActivityAnimationTimer.setInitialDelay(1);
        if (isWOME.booleanValue()) {
            areas = new Area[212];
            this.areaIDs = new int[212];
            this.areaCoords = new Point[212];
            this.areaChits = new Point[212];
            this.areaControlPoints = new Point[212];
        } else {
            areas = new Area[194];
            this.areaIDs = new int[194];
            this.areaCoords = new Point[194];
            this.areaChits = new Point[194];
            this.areaControlPoints = new Point[194];
        }
        this.boardInit = new BoardInit(this);
        this.boardInit.areaInit();
        this.selection = new Area(Messages.getString("Game.14"));
        this.highlight = areas[MINHIRIATH];
        if (win != null) {
            win.dispose();
        }
        win = new JFrame(Messages.getString("Game.15"));
        win.setLocation(-10000, 0);
        win.setSize(prefs.winx, prefs.winy);
        ((JPanel)win.getContentPane()).setLayout(new BoxLayout(((JPanel)win.getContentPane()), 0));
        this.boardlabel = new BoardLabel(new ImageIcon(), this);
        this.controls = new Controls(this, new Dimension(211, 665), this.areaIDs.length);
        this.jsp = new JScrollPane(this.boardlabel);
        this.jsp.setHorizontalScrollBarPolicy(32);
        this.jsp.setVerticalScrollBarPolicy(22);
        ((JPanel)win.getContentPane()).add(this.jsp);
        ((JPanel)win.getContentPane()).add(this.controls);
        this.jsp.setMaximumSize(new Dimension(((int) (((double) (this.maxboardwidth * prefs.zoom)) / 100.0d)) + 16, ((int) (((double) (this.maxboardheight * prefs.zoom)) / 100.0d)) + 16));
        win.setDefaultCloseOperation(0);
        this.jsp.getVerticalScrollBar().setUnitIncrement(15);
        this.jsp.getHorizontalScrollBar().setUnitIncrement(15);
        if (prefs.noBorder) {
            win.setDefaultCloseOperation(3);
            win.setExtendedState(6);
            win.setUndecorated(true);
    }
    if (this.talker == null) {
        this.talker = new Talker(this);
    }
    if (this.interpreter == null) {
        this.interpreter = new Interpreter(this, generateLogName());
    }
    win.addWindowListener(new winCloser());
    if (varianttype.endsWith("T]")) {
        TreeBeard = 1;
    }
    int Factioncount = 0;
    if (varianttype.contains("W")) {
        Factioncount = WOME_PIECE_COUNT;
    }
    if (varianttype.startsWith("expansion2")) {
        // LOME includes 25 pieces (24 expansion + 1 Treebeard always in array)
        // If Treebeard not used, he goes to Spare area post-load
        this.bits = new GamePiece[(TreeBeard + 453 + Factioncount)];
    } else {
        this.bits = new GamePiece[(TreeBeard + 428 + Factioncount)];
    }
    this.boardInit.hybridPieceInit(); // Phase 2: Try database first, fall back to bitInit()
    if ( !piecesLoadedFromDatabase ) {
        this.boardInit.bitHuntForTheRing();
    }
    this.mod = new Mod(new File("custom_pieces.txt"), this);
    this.mod.init();
    Action focusChat = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            Game.this.controls.chat.jtf.requestFocus();
        }
    };
    ((JPanel)win.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke("ENTER"), "chat");
    ((JPanel)win.getContentPane()).getActionMap().put("chat", focusChat);
    Action scrollRight = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JScrollBar b = Game.this.jsp.getHorizontalScrollBar();
            b.setValue(b.getValue() + b.getBlockIncrement(-1));
        }
    };
    ((JPanel)win.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke("control RIGHT"), "scrollRight");
    ((JPanel)win.getContentPane()).getActionMap().put("scrollRight", scrollRight);
    Action scrollLeft = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JScrollBar b = Game.this.jsp.getHorizontalScrollBar();
            b.setValue(b.getValue() - b.getBlockIncrement(1));
        }
    };
    ((JPanel)win.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke("control LEFT"), "scrollLeft");
    ((JPanel)win.getContentPane()).getActionMap().put("scrollLeft", scrollLeft);
    Action scrollUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JScrollBar b = Game.this.jsp.getVerticalScrollBar();
            b.setValue(b.getValue() - b.getBlockIncrement(1));
        }
    };
    ((JPanel)win.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke("control UP"), "scrollUp");
    ((JPanel)win.getContentPane()).getActionMap().put("scrollUp", scrollUp);
    Action scrollDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JScrollBar b = Game.this.jsp.getVerticalScrollBar();
            b.setValue(b.getValue() + b.getBlockIncrement(1));
        }
    };
    ((JPanel)win.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke("control DOWN"), "scrollDown");
    ((JPanel)win.getContentPane()).getActionMap().put("scrollDown", scrollDown);
    highlightArea(areas[MINHIRIATH], false);
    boardInit.refreshBoard();
    win.setLocation(0, 0);
    win.setVisible(true);
    win.toFront();
    this.scribe = new Scribe(this.overlay, win, this);
    zoomBoard(prefs.zoom);
    this.boardlabel.addMouseWheelListener(this.scribe);
    this.interpreter.record("<game> " + prefs.nick + Messages.getString("Controls.408") + "[" + versionno + " " + boardtype + " " + varianttype + "]");
    this.controls.chat.write("<game> " + prefs.nick + Messages.getString("Controls.408") + "[" + versionno + " " + boardtype + " " + varianttype + "]");
}

    public void playSound(String Soundfile) {
            if (this.soundon && !this.gameloading) {
            File soundFile = new File(Soundfile);
            AudioInputStream sound = null;
            try {
                sound = AudioSystem.getAudioInputStream(AudioSystem.getAudioFileFormat(soundFile).getFormat(), AudioSystem.getAudioInputStream(soundFile));
                DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
                if (this.clip == null || !this.clip.isActive()) {
                    this.clip = (Clip)AudioSystem.getLine(info);
                    this.clip.open(sound);
                    this.clip.start();
                    this.clip.addLineListener(new LineListener() {
                        public void update(LineEvent event) {
                            if (event.getType() == LineEvent.Type.STOP) {
                                event.getLine().close();
                            }
                        }
                    });
                }
            } catch (Exception e) {
            }
            if (sound != null) {
                try {
                    sound.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Game();
    }

    /* access modifiers changed from: package-private */
    public void zoomBoard(int zoom) {
        if (this.jsp != null) {
            this.jsp.setMaximumSize(new Dimension(((int) (((double) (this.maxboardwidth * zoom)) / 100.0d)) + 16, ((int) (((double) (this.maxboardheight * zoom)) / 100.0d)) + 16));
        }
        this.boardlabel.removeMouseListener(this.scribe);
        if (zoom == 100) {
            this.boardlabel.setImage(new ImageIcon(this.board).getImage());
        } else {
            String s = null;
            if (boardtype.equals("wotr")) {
                s = Messages.getLanguageLocation("images/board" + zoom + ".jpg");
            }
            if (this.board.contains("test")) {
                s = Messages.getLanguageLocation("images/boardtest" + zoom + ".jpg");
            }
            if (isWOME.booleanValue()) {
                s = Messages.getLanguageLocation("images/board" + zoom + "W.jpg");
            }
            if (new File(s).exists()) {
                this.boardlabel.setImage(new ImageIcon(s).getImage());
            } else {
                Image img = new ImageIcon(this.board).getImage();
                this.boardlabel.setImage(img.getScaledInstance((int) (((double) (img.getWidth(win) * zoom)) / 100.0d), (int) (((double) (img.getHeight(win) * zoom)) / 100.0d), 4));
            }
        }
        Container c = this.boardlabel.getParent();
        if (c != null) {
            c.remove(this.boardlabel);
            c.add(this.boardlabel);
        }
        ((JPanel)win.getContentPane()).revalidate();
        this.boardlabel.removeMouseListener(this.scribe);
        this.boardlabel.addMouseListener(this.scribe);
    }
    
    public void setViewing(boolean localPlayer, boolean FPtrueSAfalse, boolean viewing) {
        boolean sa;
        boolean fp;
        String string;
        if (!this.synchronizing && !this.gameloading) {
            if (FPtrueSAfalse) {
                fp = viewing;
                sa = localPlayer ? this.handViewing.contains(Messages.getString("Game.1")) : this.opponentHandViewing.contains(Messages.getString("Game.2"));
            } else {
                sa = viewing;
                fp = localPlayer ? this.handViewing.contains(Messages.getString("Game.3")) : this.opponentHandViewing.contains(Messages.getString("Game.4"));
            }
            if (localPlayer) {
                if (fp) {
                    if (sa) {
                        string = Messages.getString("Game.5");
                    } else {
                        string = Messages.getString("Game.6");
                    }
                    this.handViewing = string;
                } else {
                    this.handViewing = sa ? Messages.getString("Game.10") : "";
                }
            } else if (fp) {
                this.opponentHandViewing = sa ? Messages.getString("Game.1442") : Messages.getString("Game.1443");
            } else {
                this.opponentHandViewing = sa ? Messages.getString("Game.1444") : "";
            }
            redoTitle();
        }
    }

    public void redoTitle() {
        String players;
        String type = "";
        if (this.talker.connected) {
            players = String.valueOf(prefs.nick) + this.handViewing + Messages.getString("Game.1446") + this.opponent + this.opponentHandViewing;
        } else {
            players = String.valueOf(prefs.nick) + this.handViewing + Messages.getString("Game.1447") + prefs.nick;
        }
        if (boardtype.equals("wotr") && varianttype.equals("base")) {
            type = Messages.getString("Game.12");
        }
        if (varianttype.startsWith("base2")) {
            type = "[Base rules 2nd edition]";
        }
        if (varianttype.startsWith("expansion2")) {
            type = "[Expansion rules 2nd edition]";
        }
        windowTitle = String.valueOf(Messages.getString("Game.1449")) + type + " " + players + Messages.getString("Game.1450") + this.turn;
        win.setTitle(windowTitle);
        win.repaint();
    }

    public void setOpponentActive(boolean active) {
        if (!active) {
            this.opponentActivityAnimationTimer.stop();
            this.controls.chat.setStatus((String) null);
        } else if (!this.opponentActivityAnimationTimer.isRunning()) {
            this.opponentActivityAnimationStage = 0;
            this.opponentActivityAnimationTimer.restart();
        }
    }
    
    /* access modifiers changed from: private */
    public String getOpponentBusyString() {
        if (this.opponentActivityAnimationStage == 0) {
            this.opponentActivityAnimationStage++;
            return "•";
        } else if (this.opponentActivityAnimationStage == 1) {
            this.opponentActivityAnimationStage++;
            return "••";
        } else if (this.opponentActivityAnimationStage == 2) {
            this.opponentActivityAnimationStage++;
            return "•••";
        } else {
            this.opponentActivityAnimationStage = 0;
            return "••••";
        }
    }

    public synchronized void refreshBoard() {
        boardInit.refreshBoard();
    }

    

    public void exit() {
        if (this.talker.connected) {
            this.talker.disconnect(false);
        }
        disconnectAllObservers();
        prefs.save(PREF_FILE);
        System.exit(0);
    }

    public static String generateLogName() {
        String ret;
        String ret2;
        String ret3;
        String ret4;
        Calendar now = Calendar.getInstance();
        String ret5 = "logs/auto/" + boardtype + now.get(1) + "_";
        String temp = "0" + (now.get(2) + 1) + "_";
        if (temp.length() > 2) {
            ret = String.valueOf(ret5) + temp.substring(1);
        } else {
            ret = String.valueOf(ret5) + temp;
        }
        String temp2 = "0" + now.get(5) + "_";
        if (temp2.length() > 2) {
            ret2 = String.valueOf(ret) + temp2.substring(1);
        } else {
            ret2 = String.valueOf(ret) + temp2;
        }
        String temp3 = "0" + now.get(11) + "_";
        if (temp3.length() > 2) {
            ret3 = String.valueOf(ret2) + temp3.substring(1);
        } else {
            ret3 = String.valueOf(ret2) + temp3;
        }
        String temp4 = "0" + now.get(12);
        if (temp4.length() > 2) {
            ret4 = String.valueOf(ret3) + temp4.substring(1);
        } else {
            ret4 = String.valueOf(ret3) + temp4;
        }
        try {
            while (new File(String.valueOf(ret4) + ".log").exists()) {
                ret4 = String.valueOf(ret4) + "-1";
            }
        } catch (Exception e) {
        }
        return String.valueOf(ret4) + ".log";
    }
    
    public void resetBoard() {
        boardInit.resetBoard();
        // DON'T reset piecesLoadedFromDatabase - it tracks whether security was initialized
        // Replays work fine even with the flag true, and we need it to skip verification
    }
    
    public void moveGroup(Area group, Area dest) {
        int size = group.numPieces();
        for (int i = 0; i < size; i++) {
            group.get(i).moveTo(dest);
            if (group.get(i) instanceof GenericCard) {
                if (dest.name().equals(areas[FP_HAND].name())) {
                    ((GenericCard) group.get(i)).setnation(5);
                }
                if (dest.name().equals(areas[SA_HAND].name())) {
                    ((GenericCard) group.get(i)).setnation(-5);
                }
            }
            if (group.get(i) instanceof Card) {
                Card c = (Card) group.get(i);
                if (dest.name().equals(areas[CURRENT_SA_CARD].name()) || dest.name().equals(areas[CURRENT_FP_CARD].name()) || dest.name().equals(areas[BOARD_SLOT_1].name()) || dest.name().equals(areas[BOARD_SLOT_2].name()) || dest.name().equals(areas[BOARD_SLOT_3].name()) || dest.name().equals(areas[BOARD_SLOT_4].name()) || dest.name().equals(areas[BOARD_SLOT_5].name()) || dest.name().equals(areas[BOARD_SLOT_6].name())) {
                    c.played = true;
                }
                if (!(group.get(i) instanceof ShadowManeuverCard) && !(group.get(i) instanceof FreeManeuverCard) && !(group.get(i) instanceof FateCard)) {
                    if (!dest.GetViewFronts()) {
                        c.DeleteHiddenData();
                    } else {
                        String password = GetCardPassword(c);
                        SecureDeck deck = GetCardDeck(c);
                        if (!(password == null || deck == null)) {
                            int index = c.GetIndex();
                            RevealCard(c, deck.GetCard(index), Hasher.Hash(String.valueOf(password) + deck.GetCardSalt(index)));
                        }
                    }
                }
            }
        }
        if (isWOME.booleanValue() && (dest.name().equals(areas[SA_FACTION_DECK].name()) || dest.name().equals(areas[FP_FACTION_DECK].name()))) {
            playSound("soundpack/deal.sp1");
        }
        boardInit.refreshBoard();
    }

    /* access modifiers changed from: package-private */
    public void killGroup(Area group) {
        int size = group.numPieces();
        Area loc = null;
        Area temp = new Area("temp");
        if (size > 0) {
             loc = group.get(0).currentLocation();
        }
        for (int i = 0; i < size; i++) {
            GamePiece p = group.get(i);
            if (boardtype.equals("wotr")) {
                if ((p instanceof UnitSmeagol) && areas[SPARE].numPieces() > 0) {
                    int i2 = 0;
                    while (i2 <= areas[SPARE].numPieces() - 1 && (!(areas[SPARE].get(i2) instanceof ShadowCharacterCard) || !((ShadowCharacterCard) areas[SPARE].get(i2)).name().equals(Messages.getString("Game.18")))) {
                        i2++;
                    }
                    if (i2 <= areas[SPARE].numPieces() - 1) {
                        areas[SPARE].get(i2).moveTo(areas[CURRENT_SA_CARD]);
                    }
                    for (int i1 = 0; i1 <= areas.length - 1; i1++) {
                        int totalpieces = areas[i1].numPieces();
                        for (int p1 = 0; p1 <= totalpieces; p1++) {
                            GamePiece gp = areas[i1].get(p1);
                            if ((gp instanceof HuntTile) && gp.type().equals(Messages.getString("Game.1832"))) {
                                temp.addPiece(gp);
                            }
                        }
                        if (temp.numPieces() > 0) {
                            moveGroup(temp, areas[HUNT_USED]);
                        }
                    }
                }
                if ((p instanceof ShadowActionDie) && ((ShadowActionDie) p).GetState() == 13 && ((areas[BOARD_SLOT_1].numPieces() > 0 && (areas[BOARD_SLOT_1].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_1].pieces().get(0)).name().equals(Messages.getString("Game.1655"))) || ((areas[BOARD_SLOT_2].numPieces() > 0 && (areas[BOARD_SLOT_2].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_2].pieces().get(0)).name().equals(Messages.getString("Game.1655"))) || ((areas[BOARD_SLOT_3].numPieces() > 0 && (areas[BOARD_SLOT_3].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_3].pieces().get(0)).name().equals(Messages.getString("Game.1655"))) || ((areas[BOARD_SLOT_4].numPieces() > 0 && (areas[BOARD_SLOT_4].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_4].pieces().get(0)).name().equals(Messages.getString("Game.1655"))) || ((areas[BOARD_SLOT_5].numPieces() > 0 && (areas[BOARD_SLOT_5].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_5].pieces().get(0)).name().equals(Messages.getString("Game.1655"))) || (areas[BOARD_SLOT_6].numPieces() > 0 && (areas[BOARD_SLOT_6].pieces().get(0) instanceof Card) && ((Card) areas[BOARD_SLOT_6].pieces().get(0)).name().equals(Messages.getString("Game.1655"))))))))) {
                    p.moveTo(areas[HUNT]);
                } else if ((p instanceof UnitFreeControlMarker) || (p instanceof UnitTrebuchet) || (p instanceof UnitFreeControlLarge)) {
                    p.moveTo(areas[FP_REINFORCEMENTS]);
                } else if (p instanceof FreeFactionCard) {
                    p.moveTo(areas[FP_FACTION_DISCARD]);
                } else if (p instanceof FreeBattleCard) {
                    p.moveTo(areas[FP_HAND]);
                } else if (p instanceof ShadowBattleCard) {
                    p.moveTo(areas[SA_HAND]);
                } else if (p instanceof ShadowFactionCard) {
                    p.moveTo(areas[SA_FACTION_DISCARD]);
                } else if (p.nation() > 0) {
                    p.moveTo(areas[FP_CASUALTIES]);
                } else if (p instanceof HuntTile) {
                    group.get(i).moveTo(areas[HUNT_USED]);
                } else if ((p instanceof UnitCorsair) || (p instanceof UnitDunlending) || (p instanceof UnitSpider)) {
                    group.get(i).moveTo(areas[SA_REINFORCEMENTS]);
                } else if (!(p instanceof Special) || (p instanceof UnitNazgul) || (p instanceof UnitShadowControlMarker) || (p instanceof UnitShadowControlLarge)) {
                    group.get(i).moveTo(areas[SA_REINFORCEMENTS]);
                } else {
                    group.get(i).moveTo(areas[SA_CASUALTIES]);
                }
            }
            
        }
        
        boardInit.refreshBoard();
    }

    /* access modifiers changed from: package-private */
    public void highlightArea(Area a, boolean keepSelection) {
        this.highlight = a;
        if (keepSelection) {
            for (int i = this.selection.pieces().size() - 1; i >= 0; i--) {
                if (!this.selection.get(i).currentLocation().equals(a)) {
                    this.selection.removePiece(this.selection.get(i));
                }
            }
        } else {
            this.selection.clearAllPieces();
            if (a.selectme && a.numPieces() > 0) {
                this.selection.addAllPieces(a);
                for (int i2 = this.selection.pieces().size() - 1; i2 >= 0; i2--) {
                    if ((this.selection.get(i2) instanceof UnitFreeControlMarker) || (this.selection.get(i2) instanceof UnitFreeControlLarge) || (this.selection.get(i2) instanceof UnitShadowControlMarker) || (this.selection.get(i2) instanceof UnitShadowControlLarge) || ((this.selection.get(i2) instanceof UnitRecruitmentToken) || (((this.selection.get(i2) instanceof Chit) && this.selection.get(i2).type().equals(Messages.getString("Game.1267"))) || (this.selection.get(i2) instanceof UnitEagle)))) {
                        this.selection.removePiece(this.selection.get(i2));
                    }
                }
                if (this.selection.numPieces() == 0) {
                    this.selection.addAllPieces(a);
                }
            }
        }
        this.selection.SetViewFronts(a.GetViewFronts());
        this.controls.updateHighlight();
        updateStatus();
    }

    /* access modifiers changed from: package-private */
    public void setSelection(Area a) {
        this.selection = a;
        updateStatus();
    }

    public void updateStatus() {
        String statusString = Interpreter.getUnitGroupString(this.selection, this.selection.GetViewFronts());
        if (this.selection.numPieces() == 1) {
            this.controls.chat.setStatus(statusString);
        } else if (statusString.startsWith(Messages.getString("Game.1484")) || statusString.startsWith(Messages.getString("Game.1485"))) {
            this.controls.chat.setStatus(statusString.substring(6));
        } else {
            this.controls.chat.setStatus("");
        }
    }
    public void addObserver(ObserverHost observerHost) {
        this.observersExist = true;
        this.observers.add(observerHost);
    }

    public void removeObserver(ObserverHost observerHost) {
        this.observers.remove(observerHost);
        if (this.observers.size() == 0) {
            this.observersExist = false;
        }
    }

    public void sendToObservers(String cmd, ObserverHost source) {
        if (this.observersExist && cmd != null && !Messages.getString("Game.2047").equals(cmd)) {
            for (ObserverHost host : this.observers) {
                if (!host.equals(source)) {
                    host.enqueue(cmd);
                }
            }
        }
    }

    public void sendAsObserver(String cmd) {
        if (this.observerClient.connected && cmd != null) {
            this.observerClient.enqueue(cmd);
        }
    }

    public void disconnectAllObservers() {
        for (int i = this.observers.size() - 1; i >= 0; i--) {
            if (this.observers.get(i).connected) {
                this.observers.get(i).disconnect(false);
            }
        }
        if (this.observerClient.connected) {
            this.observerClient.disconnect(false);
        }
    }

    public void stopBrightObjects() {
        this.interpreter.cardBrighterTimer = false;
        this.interpreter.balrogBrighterTimer = false;
        this.interpreter.witchkingBrighterTimer = false;
        this.interpreter.preHuntTimer = false;
        this.interpreter.diceTimer = false;
        for (int i = 0; i <= this.bits.length - 1; i++) {
            GamePiece gp = this.bits[i];
            if (gp instanceof Brightness) {
                ((Brightness) gp).makeDull();
            }
            if (gp instanceof NaryaDice) {
                ((NaryaDice) gp).setVisible();
            }
            if (gp instanceof NenyaDice) {
                ((NenyaDice) gp).setVisible();
            }
            if (gp instanceof VilyaDice) {
                ((VilyaDice) gp).setVisible();
            }
            if (gp instanceof BalrogDice) {
                ((BalrogDice) gp).setVisible();
            }
            if (gp instanceof GothmogDice) {
                ((GothmogDice) gp).setVisible();
            }
        }
    }

    
    

    public static void RecordDice(String inputRoll, boolean isFPplayer) {
        String roll = inputRoll;
        if (isFPplayer) {
            while (roll.length() > 1) {
                int result = Integer.parseInt(roll.substring(0, 1));
                roll = roll.substring(2);
                int[] iArr = _playerD6stats;
                int i = result - 1;
                iArr[i] = iArr[i] + 1;
            }
            return;
        }
        while (roll.length() > 1) {
            int result2 = Integer.parseInt(roll.substring(0, 1));
            roll = roll.substring(2);
            int[] iArr2 = _opponentD6stats;
            int i2 = result2 - 1;
            iArr2[i2] = iArr2[i2] + 1;
        }
    }

    public static int D6statsN(boolean forFPplayer) {
        if (forFPplayer) {
            return _playerD6stats.length;
        }
        return _opponentD6stats.length;
    }

    public static int D6stats(boolean forFPplayer, int i) {
        if (forFPplayer && i >= 0 && i < _playerD6stats.length) {
            return _playerD6stats[i];
        }
        if (forFPplayer || i < 0 || i >= _opponentD6stats.length) {
            return -1;
        }
        return _opponentD6stats[i];
    }

    public static String SortDice(String roll) {
        String resultRoll = "";
        if (!prefs.dicesorted) {
            return " " + roll;
        }
        String parseresults = roll;
        int[] resulttypes = new int[6];
        while (parseresults.length() > 1) {
            int result = Integer.parseInt(parseresults.substring(0, 1));
            parseresults = parseresults.substring(2);
            int i = result - 1;
            resulttypes[i] = resulttypes[i] + 1;
        }
        for (int i2 = 0; i2 < resulttypes.length; i2++) {
            if (resulttypes[i2] > 0) {
                for (int j = 0; j < resulttypes[i2]; j++) {
                    resultRoll = String.valueOf(resultRoll) + " " + (i2 + 1);
                }
            }
        }
        return resultRoll;
    }

    public boolean hasFPpassword() {
        return this._FPsecurity.GetPassword() != null;
    }

    public boolean FPsecurityEmpty() {
        return this._FPsecurity.isEmpty();
    }

    public boolean verifyFPPassword(String password) {
        return this._FPsecurity.VerifyPassword(password);
    }

    public boolean hasSPpassword() {
        return this._SPsecurity.GetPassword() != null;
    }

    public boolean SPsecurityEmpty() {
        return this._SPsecurity.isEmpty();
    }

    public boolean verifySPPassword(String password) {
        return this._SPsecurity.VerifyPassword(password);
    }

    public static String Hash(String text) {
        String hash = Hasher.Hash(text);
        return hash;
    }

    public boolean SetFPpassword(String password) {
        if (this._FPsecurity.GetPassword() != null) {
            return false;
        }
        int genericCardsSize = (this._abstractGenericCards != null) ? this._abstractGenericCards.size() : 0;
        boolean result = this._FPsecurity.SetPassword(password, this._abstractFPCcards.size(), this._abstractFPScards.size(), genericCardsSize, this._abstractFPFcards.size());
        if (!result) {
            return result;
        }
        this._SPsecurity.SetGenericDeck(this._FPsecurity.GetGenericDeck().GetCardsHashes(), this._FPsecurity.GetGenericDeck().GetCardsSalts());
        return result;
    }

    public boolean SetSPpassword(String password) {
        if (this._SPsecurity.GetPassword() != null) {
            return false;
        }
        int genericCardsSize = (this._abstractGenericCards != null) ? this._abstractGenericCards.size() : 0;
        return this._SPsecurity.SetPassword(password, this._abstractSPCcards.size(), this._abstractSPScards.size(), genericCardsSize, this._abstractSPFcards.size());
    }

public void HideFPhand() {
    this._FPsecurity.ClearPassword();
    areas[FP_HAND].SetViewFronts(false);
    areas[FP_HAND_2].SetViewFronts(false);
    for (Area area : areas) {
        boolean hiding = false;
        if (area.IsHiddenFromOpponent()) {
            Iterator<GamePiece> it = area.pieces().iterator();
            while (it.hasNext()) {
                GamePiece p = it.next();
                if ((p instanceof FreeStrategyCard) || (p instanceof FreeCharacterCard) || (p instanceof FreeManeuverCard) || (p instanceof FreeStoryCard) || (p instanceof FreeFactionCard)) {
                    ((Card) p).DeleteHiddenData();
                    hiding = true;
                }
                if (p instanceof GenericCard) {
                    hiding = true;
                }
            }
            if (hiding) {
                area.updateChit = true;
                area.updatePic = true;
            }
        }
    }
    boardInit.refreshBoard();
    }

    public void ClearFPDecks() {
        this._FPsecurity.ClearDecks();
    }

    public void ClearSPDecks() {
        this._SPsecurity.ClearDecks();
    }
    
    public void HideSPhand() {
    this._SPsecurity.ClearPassword();
    areas[SA_HAND].SetViewFronts(false);
    areas[SA_HAND_2].SetViewFronts(false);
    for (Area area : areas) {
        if (area.IsHiddenFromOpponent()) {
            boolean hiding = false;
            Iterator<GamePiece> it = area.pieces().iterator();
            while (it.hasNext()) {
                GamePiece p = it.next();
                if ((p instanceof ShadowStrategyCard) || (p instanceof ShadowCharacterCard) || (p instanceof ShadowManeuverCard) || (p instanceof ShadowStoryCard) || (p instanceof ShadowFactionCard)) {
                    ((Card) p).DeleteHiddenData();
                    hiding = true;
                }
            }
            if (hiding) {
                area.updateChit = true;
                area.updatePic = true;
            }
        }
    }
    boardInit.refreshBoard();
}

public String GetFPpassword() {
        return this._FPsecurity.GetPassword();
    }

    public String GetSPpassword() {
        return this._SPsecurity.GetPassword();
    }

    public String GetFPpasswordHash() {
        return this._FPsecurity.GetPasswordHash();
    }

    public String GetSPpasswordHash() {
        return this._SPsecurity.GetPasswordHash();
    }

    public String GetFPpasswordSalt() {
        return this._FPsecurity.GetPasswordSalt();
    }

    public String GetSPpasswordSalt() {
        return this._SPsecurity.GetPasswordSalt();
    }

    public SecureDeck GetFPcharacterDeck() {
        return this._FPsecurity.GetCharacterDeck();
    }

    public SecureDeck GetSPcharacterDeck() {
        return this._SPsecurity.GetCharacterDeck();
    }

    public SecureDeck GetFPstrategyDeck() {
        return this._FPsecurity.GetStrategyDeck();
    }

    public SecureDeck GetSPstrategyDeck() {
        return this._SPsecurity.GetStrategyDeck();
    }

    public SecureDeck GetFPfactionDeck() {
        return this._FPsecurity.GetFactionDeck();
    }

    public SecureDeck GetSPfactionDeck() {
        return this._SPsecurity.GetFactionDeck();
    }

    public SecureDeck GetGenericDeck() {
        return this._SPsecurity.GetGenericDeck();
    }

    public boolean SetFPSecurity(String passwordHash, String passwordSalt, String[] CcardsHashes, String[] CcardsSalts, String[] ScardsHashes, String[] ScardsSalts, String[] FcardsHashes, String[] FcardsSalts, String[] GcardsHashes, String[] GcardsSalts) {
        this._SPsecurity.SetGenericDeck(GcardsHashes, GcardsSalts);
        return this._FPsecurity.SetHashAndSalt(passwordHash, passwordSalt, CcardsHashes, CcardsSalts, ScardsHashes, ScardsSalts, FcardsHashes, FcardsSalts, GcardsHashes, GcardsSalts);
    }

    public boolean SetSPSecurity(String passwordHash, String passwordSalt, String[] CcardsHashes, String[] CcardsSalts, String[] ScardsHashes, String[] ScardsSalts, String[] FcardsHashes, String[] FcardsSalts, String[] GcardsHashes, String[] GcardsSalts) {
        this._FPsecurity.SetGenericDeck(GcardsHashes, GcardsSalts);
        return this._SPsecurity.SetHashAndSalt(passwordHash, passwordSalt, CcardsHashes, CcardsSalts, ScardsHashes, ScardsSalts, FcardsHashes, FcardsSalts, GcardsHashes, GcardsSalts);
    }

    public static String GenerateRandomSalt() {
        return Hasher.GenerateRandomSalt();
    }

    public void RevealFPcharacterCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._FPsecurity.GetCharacterDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._FPCcards.get(index).CopyCardHiddenDataFrom(this._abstractFPCcards.get(realIndex));
        }
    }

    public void RevealFPstrategyCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._FPsecurity.GetStrategyDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._FPScards.get(index).CopyCardHiddenDataFrom(this._abstractFPScards.get(realIndex));
        }
    }

    public void RevealFPfactionCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._FPsecurity.GetFactionDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._FPFcards.get(index).CopyCardHiddenDataFrom(this._abstractFPFcards.get(realIndex));
        }
    }

    public void RevealSPcharacterCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._SPsecurity.GetCharacterDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._SPCcards.get(index).CopyCardHiddenDataFrom(this._abstractSPCcards.get(realIndex));
        }
    }

    public void RevealSPstrategyCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._SPsecurity.GetStrategyDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._SPScards.get(index).CopyCardHiddenDataFrom(this._abstractSPScards.get(realIndex));
        }
    }

    public void RevealSPfactionCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._SPsecurity.GetFactionDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        } else {
            this._SPFcards.get(index).CopyCardHiddenDataFrom(this._abstractSPFcards.get(realIndex));
        }
    }

    public void RevealGenericCard(int index, int realIndex, String innerHash) {
        if (!piecesLoadedFromDatabase && !this._SPsecurity.GetGenericDeck().VerifyCard(index, innerHash, realIndex)) {
            JOptionPane.showMessageDialog(win, "Error in the card signature shown by the opponent!");
        }
    }

    public void RevealCard(Card card, int index, String hash) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (((card instanceof FreeCharacterCard) || (card instanceof FreeStoryCard)) && (i6 = this._FPCcards.indexOf(card)) != -1) {
            RevealFPcharacterCard(i6, index, hash);
        }
        if ((card instanceof FreeStrategyCard) && (i5 = this._FPScards.indexOf(card)) != -1) {
            RevealFPstrategyCard(i5, index, hash);
        }
        if ((card instanceof FreeFactionCard) && (i4 = this._FPFcards.indexOf(card)) != -1) {
            RevealFPfactionCard(i4, index, hash);
        }
        if (((card instanceof ShadowCharacterCard) || (card instanceof ShadowStoryCard)) && (i3 = this._SPCcards.indexOf(card)) != -1) {
            RevealSPcharacterCard(i3, index, hash);
        }
        if (((card instanceof ShadowStrategyCard) || (card instanceof ShadowDarknessCard) || (card instanceof ShadowGrimaCard)) && (i2 = this._SPScards.indexOf(card)) != -1) {
            RevealSPstrategyCard(i2, index, hash);
        }
        if ((card instanceof ShadowFactionCard) && (i = this._SPFcards.indexOf(card)) != -1) {
            RevealSPfactionCard(i, index, hash);
        }
    }

    public void MapFPcardsFromPassword() {
        if (!this._FPsecurity.MapCardsFromPassword()) {
            JOptionPane.showMessageDialog(win, "Error in mapping the FP cards, security is compromised!");
        }
    }

    public void MapSPcardsFromPassword() {
        if (!this._SPsecurity.MapCardsFromPassword()) {
            JOptionPane.showMessageDialog(win, "Error in mapping the SA cards, security is compromised!");
        }
    }
    
    public void RevealFPcards() {
    SecureDeck characterCards = this._FPsecurity.GetCharacterDeck();
    SecureDeck strategyCards = this._FPsecurity.GetStrategyDeck();
    String password = this._FPsecurity.GetPassword();
    int i = 0;
    while (i < characterCards.Ncards()) {
        this.RevealFPcharacterCard(i, characterCards.GetCard(i), Hasher.Hash(String.valueOf(password) + characterCards.GetCardSalt(i)));
        ++i;
    }
    i = 0;
    while (i < strategyCards.Ncards()) {
        this.RevealFPstrategyCard(i, strategyCards.GetCard(i), Hasher.Hash(String.valueOf(password) + strategyCards.GetCardSalt(i)));
        ++i;
    }
    areas[FP_HAND].SetViewFronts(true);
    Area[] arrarea = areas;
    int n = arrarea.length;
    int n2 = 0;
    while (n2 < n) {
        Area area = arrarea[n2];
        boolean revealing = false;
        if (area.GetViewFronts() && area.IsHiddenFromOpponent()) {
            for (GamePiece p : area.pieces()) {
                if (!(p instanceof FreeStrategyCard) && !(p instanceof FreeCharacterCard) && !(p instanceof FreeManeuverCard) && !(p instanceof FreeStoryCard)) continue;
                revealing = true;
                break;
            }
            if (revealing) {
                area.updateChit = true;
                area.updatePic = true;
            }
        }
        ++n2;
    }
    boardInit.refreshBoard();    
}

    public void RevealSPcards() {
        SecureDeck characterCards = this._SPsecurity.GetCharacterDeck();
        SecureDeck strategyCards = this._SPsecurity.GetStrategyDeck();
        String password = this._SPsecurity.GetPassword();
        int i = 0;
        while (i < characterCards.Ncards()) {
            this.RevealSPcharacterCard(i, characterCards.GetCard(i), Hasher.Hash(String.valueOf(password) + characterCards.GetCardSalt(i)));
            ++i;
        }
        i = 0;
        while (i < strategyCards.Ncards()) {
            this.RevealSPstrategyCard(i, strategyCards.GetCard(i), Hasher.Hash(String.valueOf(password) + strategyCards.GetCardSalt(i)));
            ++i;
        }
        areas[SA_HAND].SetViewFronts(true);
        Area[] arrarea = areas;
        int n = arrarea.length;
        int n2 = 0;
        while (n2 < n) {
            Area area = arrarea[n2];
            boolean revealing = false;
                if (area.GetViewFronts() && area.IsHiddenFromOpponent()) {
                    for (GamePiece p : area.pieces()) { 
                        if (!(p instanceof ShadowStrategyCard) && !(p instanceof ShadowCharacterCard) && !(p instanceof ShadowManeuverCard) && !(p instanceof ShadowStoryCard)) continue;
                        revealing = true;
                        break;
                    }
                    if (revealing) {
                        area.updateChit = true;
                        area.updatePic = true;
                    
                    }
            }
            ++n2;
        }
        boardInit.refreshBoard();
    }

    private String GetCardPassword(Card card) {
        String password = null;
        if ((card instanceof FreeCharacterCard) || (card instanceof FreeStoryCard) || (card instanceof FreeStrategyCard) || (card instanceof FreeFactionCard)) {
            password = GetFPpassword();
        }
        if ((card instanceof ShadowCharacterCard) || (card instanceof ShadowStoryCard) || (card instanceof ShadowStrategyCard) || (card instanceof ShadowDarknessCard) || (card instanceof ShadowGrimaCard) || (card instanceof ShadowFactionCard)) {
            return GetSPpassword();
        }
        return password;
    }

    private SecureDeck GetCardDeck(Card card) {
        SecureDeck deck = null;
        if ((card instanceof FreeCharacterCard) || (card instanceof FreeStoryCard)) {
            deck = GetFPcharacterDeck();
        }
        if (card instanceof FreeStrategyCard) {
            deck = GetFPstrategyDeck();
        }
        if (card instanceof FreeFactionCard) {
            deck = GetFPfactionDeck();
        }
        if ((card instanceof ShadowCharacterCard) || (card instanceof ShadowStoryCard)) {
            deck = GetSPcharacterDeck();
        }
        if ((card instanceof ShadowStrategyCard) || (card instanceof ShadowDarknessCard) || (card instanceof ShadowGrimaCard)) {
            deck = GetSPstrategyDeck();
        }
        if (card instanceof ShadowFactionCard) {
            return GetSPfactionDeck();
        }
        return deck;
    }

    /* access modifiers changed from: package-private */
    public String GetCardRevealText(GamePiece piece) {
        if (piece instanceof GenericCard) {
            return piece.toString();
        }
        if (!(piece instanceof Card)) {
            return piece.toString();
        }
        Card c = (Card) piece;
        int index = c.GetIndex();
        String password = GetCardPassword(c);
        SecureDeck deck = GetCardDeck(c);
        if (password == null) {
            return c.toString();
        }
        return "reveal " + c + " " + Hasher.Hash(String.valueOf(password) + deck.GetCardSalt(index)) + " " + deck.GetCard(index);
    }
}