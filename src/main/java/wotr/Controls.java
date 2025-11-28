package wotr;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;
import wotr.ui.FsmPhasePanel;

/* renamed from: Controls */
public class Controls extends JPanel {
    static final String FPC_GRAPHIC = Messages.getLanguageLocation("images/FPCasualties.jpg");
    static final String FPR_GRAPHIC = Messages.getLanguageLocation("images/FPReinforcements.jpg");
    static final String HUNT_POOL_GRAPHIC = Messages.getLanguageLocation("images/huntpool.png");
    static final String HUNT_REMOVED_GRAPHIC = Messages.getLanguageLocation("images/huntremoved.png");
    static final String HUNT_USED_GRAPHIC = Messages.getLanguageLocation("images/huntused.png");
    static final String SAC_GRAPHIC = Messages.getLanguageLocation("images/SACasualties.jpg");
    static final String SAR_GRAPHIC = Messages.getLanguageLocation("images/SAReinforcements.jpg");
    JLabel FPCasualties;
    AreaPanel FPD;
    AreaPanel FPH;
    AreaPanel FPH2;
    JLabel FPReinforcementTokens;
    JLabel FPReinforcements;
    JLabel FPTactics;
    JLabel SACasualties;
    AreaPanel SAD;
    AreaPanel SAH;
    AreaPanel SAH2;
    JLabel SAReinforcementTokens;
    JLabel SAReinforcements;
    JLabel SATactics;
    CtrlButtonHandler cbh;
    Chat chat;
    ConnectionDialogHandler connectionDialogHandler;
    boolean echoer;
    public FsmPhasePanel fsmPhasePanel;

    /* renamed from: fc */
    final JFileChooser f3fc = new JFileChooser("logs/");
    Buffet fpd;
    Buffet fph;
    JScrollPane fpsp;
    JScrollPane fpsp2;
    Game game;

    /* renamed from: hl */
    HighlightLabel f4hl;
    JScrollPane hlsp;
    JLabel huntPool;
    JLabel huntRemoved;
    JLabel huntUsed;
    JTabbedPane jtp = new JTabbedPane();
    Buffet sad;
    Buffet sah;
    JScrollPane sasp;
    JScrollPane sasp2;
    int startarea;

    public Controls(Game g, Dimension d, int startregion) {
        this.game = g;
        setPreferredSize(d);
        setMinimumSize(d);
        this.startarea = startregion;
        setLayout(new BoxLayout(this, 1));
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JPanel jp4 = new JPanel();
        JPanel jp5 = new JPanel();
        for (InputMap map = this.jtp.getInputMap(); map != null; map = map.getParent()) {
            map.remove(KeyStroke.getKeyStroke("control UP"));
            map.remove(KeyStroke.getKeyStroke("control DOWN"));
        }
        for (InputMap map2 = this.jtp.getInputMap(1); map2 != null; map2 = map2.getParent()) {
            map2.remove(KeyStroke.getKeyStroke("control UP"));
            map2.remove(KeyStroke.getKeyStroke("control DOWN"));
        }
        if (Game.boardtype.equals("wotr")) {
            this.FPReinforcements = new JLabel(new ImageIcon(FPR_GRAPHIC));
            this.FPCasualties = new JLabel(new ImageIcon(FPC_GRAPHIC));
            this.SAReinforcements = new JLabel(new ImageIcon(SAR_GRAPHIC));
            this.SACasualties = new JLabel(new ImageIcon(SAC_GRAPHIC));
        }
        jp1.add(this.FPReinforcements);
        jp1.add(this.FPCasualties);
        jp2.add(this.SAReinforcements);
        jp2.add(this.SACasualties);
        this.jtp.addTab(Messages.getString("Controls.27"), jp1);
        if (Game.isWOME.booleanValue()) {
            this.jtp.addTab("F+", jp4);
        }
        this.jtp.addTab(Messages.getString("Controls.28"), jp2);
        if (Game.isWOME.booleanValue()) {
            this.jtp.addTab("S+", jp5);
        }
        if (Game.boardtype.equals("wotr") && !Game.varianttype.equals("introductory")) {
            this.jtp.addTab(Messages.getString("Controls.30"), jp3);
        }
        this.jtp.setMaximumSize(new Dimension(Game.prefs.cardsWidth, Game.prefs.cardsHeight + 54));
        if (System.getProperty("os.name").indexOf("Mac") != -1) {
            this.jtp.setMaximumSize(new Dimension(Game.prefs.cardsWidth + 20, Game.prefs.cardsHeight + 54));
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        add(this.jtp);
        this.jtp.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (Game.isWOME.booleanValue() && (e.getModifiers() & 4) == 4) {
                    if (Controls.this.jtp.getSelectedIndex() == 0) {
                        ((CardBuffet) Game.areas[180]).mouseClicked(e);
                    }
                    if (Controls.this.jtp.getSelectedIndex() == 1) {
                        ((CardBuffet) Game.areas[202]).mouseClicked(e);
                    }
                    if (Controls.this.jtp.getSelectedIndex() == 2) {
                        ((CardBuffet) Game.areas[181]).mouseClicked(e);
                    }
                    if (Controls.this.jtp.getSelectedIndex() == 3) {
                        ((CardBuffet) Game.areas[203]).mouseClicked(e);
                    }
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        CtrlAreaHandler cah = new CtrlAreaHandler(this);
        this.FPReinforcements.addMouseListener(cah);
        this.FPCasualties.addMouseListener(cah);
        this.SAReinforcements.addMouseListener(cah);
        this.SACasualties.addMouseListener(cah);
        if (Game.boardtype.equals("wotr") && !Game.varianttype.equals("introductory")) {
            this.huntPool = new JLabel(new ImageIcon(HUNT_POOL_GRAPHIC));
            this.huntUsed = new JLabel(new ImageIcon(HUNT_USED_GRAPHIC));
            this.huntRemoved = new JLabel(new ImageIcon(HUNT_REMOVED_GRAPHIC));
            jp3.add(this.huntPool);
            jp3.add(this.huntUsed);
            jp3.add(this.huntRemoved);
            this.huntPool.addMouseListener(cah);
            this.huntUsed.addMouseListener(cah);
            this.huntRemoved.addMouseListener(cah);
        }
        this.FPH = new AreaPanel((Area) null, 118);
        this.FPH.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (9 / (Game.prefs.cardsWidth / 114)) * 44));
        if (Game.isWOME.booleanValue()) {
            this.FPH.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (21 / (Game.prefs.cardsWidth / 114)) * 44));
        }
        this.FPH.setBorder(new LineBorder(Color.gray, 1));
        Game.areas[180] = new CardBuffet(Messages.getKeyString("Controls.45"), this.FPH, this.game, 118);        
        this.FPH.area = Game.areas[180];
        if (Game.isWOME.booleanValue()) {
            this.FPH2 = new AreaPanel((Area) null, 118);
            this.FPH2.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (21 / (Game.prefs.cardsWidth / 114)) * 44));
            this.FPH2.setBorder(new LineBorder(Color.gray, 1));
            Game.areas[202] = new CardBuffet("F+", this.FPH2, this.game, 118);
            this.FPH2.area = Game.areas[202];
            this.fpsp2 = new JScrollPane(this.FPH2, 20, 31);
            this.fpsp2.setPreferredSize(new Dimension(Game.prefs.cardsWidth, Game.prefs.cardsHeight + 51));
            jp4.add(this.fpsp2);
            this.fpsp2.getVerticalScrollBar().setUnitIncrement(20);
            jp4.setPreferredSize(new Dimension(Game.prefs.cardsWidth + 2, Game.prefs.cardsHeight + 51));
        }
        this.SAH = new AreaPanel((Area) null, 118);
        this.SAH.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (9 / (Game.prefs.cardsWidth / 114)) * 44));
        if (Game.isWOME.booleanValue()) {
            this.SAH.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (21 / (Game.prefs.cardsWidth / 114)) * 44));
        }
        this.SAH.setBorder(new LineBorder(Color.gray, 1));
        Game.areas[181] = new CardBuffet(Messages.getKeyString("Controls.46"), this.SAH, this.game, 118);
        this.SAH.area = Game.areas[181];
        if (Game.isWOME.booleanValue()) {
            this.SAH2 = new AreaPanel((Area) null, 118);
            this.SAH2.setPreferredSize(new Dimension(Game.prefs.cardsWidth, (21 / (Game.prefs.cardsWidth / 114)) * 44));
            this.SAH2.setBorder(new LineBorder(Color.gray, 1));
            Game.areas[203] = new CardBuffet("SA2", this.SAH2, this.game, 118);
            this.SAH2.area = Game.areas[203];
            this.sasp2 = new JScrollPane(this.SAH2, 20, 31);
            this.sasp2.setPreferredSize(new Dimension(Game.prefs.cardsWidth, Game.prefs.cardsHeight + 51));
            jp5.add(this.sasp2);
            this.sasp2.getVerticalScrollBar().setUnitIncrement(20);
            jp5.setPreferredSize(new Dimension(Game.prefs.cardsWidth + 2, Game.prefs.cardsHeight + 51));
        }
        this.fpsp = new JScrollPane(this.FPH, 20, 31);
        this.fpsp.setPreferredSize(new Dimension(Game.prefs.cardsWidth, Game.prefs.cardsHeight));
        jp1.add(this.fpsp);
        this.fpsp.getVerticalScrollBar().setUnitIncrement(20);
        this.sasp = new JScrollPane(this.SAH, 20, 31);
        jp2.add(this.sasp);
        this.sasp.setPreferredSize(new Dimension(Game.prefs.cardsWidth, Game.prefs.cardsHeight));
        this.sasp.getVerticalScrollBar().setUnitIncrement(20);
        jp1.setPreferredSize(new Dimension(Game.prefs.cardsWidth + 2, Game.prefs.cardsHeight + 51));
        jp2.setPreferredSize(new Dimension(Game.prefs.cardsWidth + 2, Game.prefs.cardsHeight + 51));
        jp3.setPreferredSize(new Dimension(Game.prefs.cardsWidth + 2, Game.prefs.cardsHeight + 51));
        this.cbh = new CtrlButtonHandler(this);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(Messages.getString("Controls.47"));
        JMenu jMenu = new JMenu(Messages.getString("Controls.48"));
        JMenu gameMenu = new JMenu(Messages.getString("Controls.49"));
        JMenu fMenu = new JMenu(Messages.getString("Controls.50"));
        JMenu jMenu2 = new JMenu(Messages.getString("Controls.51"));
        JMenu dMenu = Game.boardtype.equals("wotr") ? new JMenu(Messages.getString("Controls.53")) : null;
        JMenu jMenu3 = new JMenu(Messages.getString("Controls.60"));
        JMenu jMenu4 = new JMenu(Messages.getString("Controls.61"));
        JMenu viewMenu = new JMenu(Messages.getString("Controls.62"));
        JMenu helpMenu = new JMenu(Messages.getString("Controls.63"));
        menuBar.add(fileMenu);
        menuBar.add(jMenu);
        menuBar.add(gameMenu);
        menuBar.add(fMenu);
        menuBar.add(jMenu2);
        menuBar.add(dMenu);
        menuBar.add(jMenu3);
        menuBar.add(jMenu4);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        JMenu fileSubMenu = new JMenu(Messages.getString("Controls.66"));
        JMenuItem item = new JMenu(Messages.getString("Controls.10"));
        item.setActionCommand("newBase");
        item.addActionListener(this.cbh);
        fileSubMenu.add(item);
        JMenuItem subitem = new JMenuItem(Messages.getString("Controls.923"));
        subitem.setActionCommand("newBase2");
        subitem.setAccelerator(KeyStroke.getKeyStroke(78, 2));
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.920"));
        subitem.setActionCommand("newBase2[T]");
        subitem.addActionListener(this.cbh);
        item.add(subitem);

        item = new JMenu(Messages.getString("Controls.924"));
        fileSubMenu.add(item);
        subitem = new JMenuItem(Messages.getString("Controls.921"));
        subitem.setActionCommand("newLOME");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.922"));
        subitem.setActionCommand("newLOME[T]");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        
        item = new JMenu(Messages.getString("Controls.153"));
        fileSubMenu.add(item);
        subitem = new JMenuItem(Messages.getString("Controls.156"));
        subitem.setActionCommand("newWOME");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.158"));
        subitem.setActionCommand("newWOME[L]");
        subitem.addActionListener(this.cbh);
        item.add(subitem);

        fileMenu.add(fileSubMenu);

        item = new JMenuItem(Messages.getString("Controls.73"));
        item.setAccelerator(KeyStroke.getKeyStroke(76, 2));
        fileMenu.add(item);
        item.setActionCommand("load");
        item.addActionListener(this.cbh);

        item = new JMenuItem(Messages.getString("Controls.75"));
        item.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        fileMenu.add(item);
        item.setActionCommand("save");
        item.addActionListener(this.cbh);

        fileMenu.addSeparator();

        item = new JMenuItem(Messages.getString("Controls.77"));
        item.setAccelerator(KeyStroke.getKeyStroke(81, 2));
        fileMenu.add(item);
        item.setActionCommand("quit");
        item.addActionListener(this.cbh);

        item = new JMenuItem(Messages.getString("Controls.79"));
        jMenu.add(item);
        item.setActionCommand("getaddress");
        item.addActionListener(this.cbh);

        item = new JMenuItem(Messages.getString("Controls.81"));
        jMenu.add(item);
        item.setActionCommand("server");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.83"));
        jMenu.add(item);
        item.setActionCommand("client");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.85"));
        jMenu.add(item);
        item.setActionCommand("disconnect");
        item.addActionListener(this.cbh);
        jMenu.addSeparator();
        
        // FSM server connection menu items
        item = new JMenuItem("Connect to FSM Server...");
        jMenu.add(item);
        item.setActionCommand("fsm_connect");
        item.addActionListener(this.cbh);
        item = new JMenuItem("Disconnect from FSM Server");
        jMenu.add(item);
        item.setActionCommand("fsm_disconnect");
        item.addActionListener(this.cbh);
        jMenu.addSeparator();
        
        item = new JMenuItem(Messages.getString("Controls.87"));
        jMenu.add(item);
        item.setActionCommand("changenick");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.89"));
        jMenu.add(item);
        item.setActionCommand("synchronize");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.91"));
        item.setAccelerator(KeyStroke.getKeyStroke(127, 2));
        item.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        gameMenu.add(item);
        item.setActionCommand("kill");
        item.addActionListener(this.cbh);
        if (Game.boardtype.equals("wotr")) {
            item = new JMenuItem(Messages.getString("Controls.94"));
            item.setAccelerator(KeyStroke.getKeyStroke(155, 2));
            item.setAccelerator(KeyStroke.getKeyStroke(155, 0));
            gameMenu.add(item);
            item.setActionCommand("dkill");
            item.addActionListener(this.cbh);
        }
        gameMenu.addSeparator();
        
        if (Game.prefs.language.equals("Deutsch")) {
            item = new JMenuItem(Messages.getString("Controls.96"));
            item.setAccelerator(KeyStroke.getKeyStroke(87, 2));
        } else {
            item = new JMenuItem(Messages.getString("Controls.96"));
            item.setAccelerator(KeyStroke.getKeyStroke(82, 2));
        }
        gameMenu.add(item);
        item.setActionCommand("roll");
        item.addActionListener(this.cbh);
        if (!Game.varianttype.equals("introductory")) {
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.98"));
                item.setAccelerator(KeyStroke.getKeyStroke(85, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.98"));
                item.setAccelerator(KeyStroke.getKeyStroke(70, 2));
            }
            gameMenu.add(item);
            item.setActionCommand("flip");
            item.addActionListener(this.cbh);
        }
        if (Game.varianttype.contains("L")) {
            item = new JMenu(Messages.getString("Controls.157"));
            gameMenu.add(item);
            subitem = new JMenuItem(Messages.getString("Controls.944"));
            subitem.setActionCommand("ringVilya");
            subitem.addActionListener(this.cbh);
            item.add(subitem);
            subitem = new JMenuItem(Messages.getString("Controls.945"));
            subitem.setActionCommand("ringNenya");
            subitem.addActionListener(this.cbh);
            item.add(subitem);
            subitem = new JMenuItem(Messages.getString("Controls.946"));
            subitem.setActionCommand("ringNarya");
            subitem.addActionListener(this.cbh);
            item.add(subitem);
        } else if (Game.boardtype.equals("wotr")) {
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.157"));
                item.setAccelerator(KeyStroke.getKeyStroke(82, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.157"));
                item.setAccelerator(KeyStroke.getKeyStroke(85, 2));
            }
            gameMenu.add(item);
            item.setActionCommand("ring");
            item.addActionListener(this.cbh);
        }
        if (!Game.varianttype.equals("introductory")) {
            gameMenu.addSeparator();
            item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.101")) : item;
            item.setAccelerator(KeyStroke.getKeyStroke(112, 0));
            gameMenu.add(item);
            item.setActionCommand("choose");
            item.addActionListener(this.cbh);
            item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.107")) : item;
            item.setAccelerator(KeyStroke.getKeyStroke(113, 0));
            gameMenu.add(item);
            item.setActionCommand("playchosen");
            item.addActionListener(this.cbh);
            item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.78")) : item;
            item.setAccelerator(KeyStroke.getKeyStroke(115, 0));
            gameMenu.add(item);
            item.setActionCommand("unchoose");
            item.addActionListener(this.cbh);
        }
        gameMenu.addSeparator();
        if (!Game.varianttype.equals("introductory")) {
            item = new JMenuItem(Messages.getString("Controls.118"));
            item.setAccelerator(KeyStroke.getKeyStroke(27, 1));
            gameMenu.add(item);
            item.setActionCommand("clearcards");
            item.addActionListener(this.cbh);
        }
        item = new JMenuItem(Messages.getString("Controls.120"));
        item.setAccelerator(KeyStroke.getKeyStroke(27, 0));
        gameMenu.add(item);
        item.setActionCommand("endturn");
        item.addActionListener(this.cbh);
        gameMenu.addSeparator();
        item = new JMenuItem(Messages.getString("Controls.126"));
        item.setAccelerator(KeyStroke.getKeyStroke(90, 2));
        gameMenu.add(item);
        item.setActionCommand("undo");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.6"));
        item.setAccelerator(KeyStroke.getKeyStroke(89, 2));
        gameMenu.add(item);
        item.setActionCommand("redo");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.128"));
        fMenu.add(item);
        item.setActionCommand("viewfp");
        item.addActionListener(this.cbh);
        fMenu.addSeparator();
        if (Game.isWOME.booleanValue()) {
            item = new JMenuItem(Messages.getString("Controls.163"));
            item.setAccelerator(KeyStroke.getKeyStroke(116, 0));
            fMenu.add(item);
            item.setActionCommand("fdrawfaction");
            item.addActionListener(this.cbh);
            item = new JMenuItem(Messages.getString("Controls.164"));
            item.setAccelerator(KeyStroke.getKeyStroke(116, 2));
            fMenu.add(item);
            item.setActionCommand("fdrawfaction2");
            item.addActionListener(this.cbh);
        }
        item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.131")) : item;
        item.setAccelerator(KeyStroke.getKeyStroke(118, 0));
        fMenu.add(item);
        if (Game.boardtype.equals("wotr")) {
            item.setActionCommand("fdrawc");
        }
        item.addActionListener(this.cbh);
        item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.141")) : item;
        item.setAccelerator(KeyStroke.getKeyStroke(119, 0));
        fMenu.add(item);
        if (Game.boardtype.equals("wotr")) {
            item.setActionCommand("fdraws");
        }
        item.addActionListener(this.cbh);
        item = new JMenu(Messages.getString("Controls.930"));
        fMenu.add(item);
        item.addActionListener(this.cbh);
        subitem = new JMenuItem(Messages.getString("Controls.931"));
        subitem.setActionCommand("fchangeC");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.932"));
        subitem.setActionCommand("fchangeP");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.933"));
        subitem.setActionCommand("fchangeH");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.934"));
        subitem.setActionCommand("fchangeM");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.935"));
        subitem.setActionCommand("fchangeW");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        item = new JMenuItem(Messages.getString("Controls.150"));
        item.setAccelerator(KeyStroke.getKeyStroke(57, 2));
        fMenu.add(item);
        item.setActionCommand("frecover");
        item.addActionListener(this.cbh);
        fMenu.addSeparator();
        if (!Game.varianttype.equals("introductory")) {
            if (Game.boardtype.equals("wotr")) {
                if (Game.prefs.language.equals("Deutsch")) {
                    item = new JMenuItem(Messages.getString("Controls.160"));
                } else {
                    item = new JMenuItem(Messages.getString("Controls.160"));
                }
            }
            if (Game.prefs.language.equals("Deutsch")) {
                item.setAccelerator(KeyStroke.getKeyStroke(66, 2));
            } else {
                item.setAccelerator(KeyStroke.getKeyStroke(77, 2));
            }
            fMenu.add(item);
            if (Game.boardtype.equals("wotr")) {
                item.setActionCommand("movefsp");
            }
            item.addActionListener(this.cbh);
        }
        if (Game.boardtype.equals("wotr")) {
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.174"));
                item.setAccelerator(KeyStroke.getKeyStroke(71, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.174"));
                item.setAccelerator(KeyStroke.getKeyStroke(68, 2));
            }
            fMenu.add(item);
            item.setActionCommand("randomcomp");
            item.addActionListener(this.cbh);
        }
        item = new JMenuItem(Messages.getString("Controls.181"));
        jMenu2.add(item);
        item.setActionCommand("viewsa");
        item.addActionListener(this.cbh);
        jMenu2.addSeparator();
        if (Game.isWOME.booleanValue()) {
            item = new JMenuItem(Messages.getString("Controls.166"));
            item.setAccelerator(KeyStroke.getKeyStroke(120, 0));
            jMenu2.add(item);
            item.setActionCommand("sdrawfaction");
            item.addActionListener(this.cbh);
            item = new JMenuItem(Messages.getString("Controls.170"));
            item.setAccelerator(KeyStroke.getKeyStroke(120, 2));
            jMenu2.add(item);
            item.setActionCommand("sdrawfaction2");
            item.addActionListener(this.cbh);
        }
        item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.184")) : item;
        item.setAccelerator(KeyStroke.getKeyStroke(122, 0));
        jMenu2.add(item);
        if (Game.boardtype.equals("wotr")) {
            item.setActionCommand("sdrawc");
        }
        item.addActionListener(this.cbh);
        item = Game.boardtype.equals("wotr") ? new JMenuItem(Messages.getString("Controls.194")) : item;
        item.setAccelerator(KeyStroke.getKeyStroke(123, 0));
        jMenu2.add(item);
        if (Game.boardtype.equals("wotr")) {
            item.setActionCommand("sdraws");
        }
        item.addActionListener(this.cbh);
        item = new JMenu(Messages.getString("Controls.936"));
        jMenu2.add(item);
        item.addActionListener(this.cbh);
        subitem = new JMenuItem(Messages.getString("Controls.937"));
        subitem.setActionCommand("schangeC");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.938"));
        subitem.setActionCommand("schangeP");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        JMenuItem jMenuItem17 = new JMenuItem(Messages.getString("Controls.939"));
        jMenuItem17.setActionCommand("schangeH");
        jMenuItem17.addActionListener(this.cbh);
        item.add(jMenuItem17);
        subitem = new JMenuItem(Messages.getString("Controls.940"));
        subitem.setActionCommand("schangeA");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.941"));
        subitem.setActionCommand("schangeM");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        subitem = new JMenuItem(Messages.getString("Controls.942"));
        subitem.setActionCommand("schangeE");
        subitem.addActionListener(this.cbh);
        item.add(subitem);
        item = new JMenuItem(Messages.getString("Controls.203"));
        item.setAccelerator(KeyStroke.getKeyStroke(48, 2));
        jMenu2.add(item);
        item.setActionCommand("srecover");
        item.addActionListener(this.cbh);
        if (Game.boardtype.equals("wotr")) {
            jMenu2.addSeparator();
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.210"));
                item.setAccelerator(KeyStroke.getKeyStroke(74, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.210"));
                item.setAccelerator(KeyStroke.getKeyStroke(72, 2));
            }
            jMenu2.add(item);
            item.setActionCommand("hunt");
            item.addActionListener(this.cbh);
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.212"));
                item.setAccelerator(KeyStroke.getKeyStroke(65, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.212"));
                item.setAccelerator(KeyStroke.getKeyStroke(69, 2));
            }
            jMenu2.add(item);
            item.setActionCommand("eye");
            item.addActionListener(this.cbh);
            if (Game.prefs.language.equals("Deutsch")) {
                item = new JMenuItem(Messages.getString("Controls.176"));
                item.setAccelerator(KeyStroke.getKeyStroke(75, 2));
            } else {
                item = new JMenuItem(Messages.getString("Controls.176"));
                item.setAccelerator(KeyStroke.getKeyStroke(87, 2));
            }
            jMenu2.add(item);
            item.setActionCommand("wwsat");
            item.addActionListener(this.cbh);
        }
        item = new JMenuItem(Messages.getString("Controls.214"));
        item.setAccelerator(KeyStroke.getKeyStroke(49, 2));
        dMenu.add(item);
        item.setActionCommand("roll1");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.216"));
        item.setAccelerator(KeyStroke.getKeyStroke(50, 2));
        dMenu.add(item);
        item.setActionCommand("roll2");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.218"));
        item.setAccelerator(KeyStroke.getKeyStroke(51, 2));
        dMenu.add(item);
        item.setActionCommand("roll3");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.220"));
        item.setAccelerator(KeyStroke.getKeyStroke(52, 2));
        dMenu.add(item);
        item.setActionCommand("roll4");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.222"));
        item.setAccelerator(KeyStroke.getKeyStroke(53, 2));
        dMenu.add(item);
        item.setActionCommand("roll5");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.236"));
        jMenu3.add(item);
        item.setActionCommand("loadreplay");
        item.addActionListener(this.cbh);
        jMenu3.addSeparator();
        item = new JMenuItem(Messages.getString("Controls.238"));
        item.setAccelerator(KeyStroke.getKeyStroke(32, 2));
        jMenu3.add(item);
        item.setActionCommand("playreplay");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.240"));
        item.setAccelerator(KeyStroke.getKeyStroke(61, 3));
        jMenu3.add(item);
        item.setActionCommand("fasterreplay");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.242"));
        item.setAccelerator(KeyStroke.getKeyStroke(45, 2));
        jMenu3.add(item);
        item.setActionCommand("slowerreplay");
        item.addActionListener(this.cbh);
        if (Game.prefs.language.equals("Deutsch")) {
            item = new JMenuItem(Messages.getString("Controls.244"));
            item.setAccelerator(KeyStroke.getKeyStroke(72, 2));
        } else {
            item = new JMenuItem(Messages.getString("Controls.244"));
            item.setAccelerator(KeyStroke.getKeyStroke(66, 2));
        }
        jMenu3.add(item);
        item.setActionCommand("continuereplay");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.124"));
        jMenu3.add(item);
        item.setActionCommand("insertbreak");
        item.addActionListener(this.cbh);
        jMenu3.addSeparator();
        item = new JMenuItem(Messages.getString("Controls.246"));
        item.setAccelerator(KeyStroke.getKeyStroke(32, 0));
        jMenu3.add(item);
        item.setActionCommand("stepreplay");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.248"));
        jMenu4.add(item);
        item.setActionCommand("hostobs");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.250"));
        jMenu4.add(item);
        item.setActionCommand("connectobs");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.252"));
        jMenu4.add(item);
        item.setActionCommand("disconnectobs");
        item.addActionListener(this.cbh);
        jMenu4.addSeparator();
        item = new JMenuItem(Messages.getString("Controls.254"));
        jMenu4.add(item);
        item.setActionCommand("allowobs");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.256"));
        viewMenu.add(item);
        item.setActionCommand("setzoom");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.258"));
        viewMenu.add(item);
        item.setActionCommand("stats");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.209"));
        viewMenu.add(item);
        item.setActionCommand("cards");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.260"));
        helpMenu.add(item);
        item.setActionCommand("about");
        item.addActionListener(this.cbh);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            item = new JMenuItem(Messages.getString("Controls.262"));
            helpMenu.add(item);
            item.setActionCommand("readme");
            item.addActionListener(this.cbh);
        }
        JMenuItem jMenuItem84 = new JMenuItem(Messages.getString("Controls.159"));
        helpMenu.add(jMenuItem84);
        jMenuItem84.setActionCommand("openguild");
        jMenuItem84.addActionListener(this.cbh);

        item = new JMenuItem(Messages.getString("Controls.64"));
        helpMenu.add(item);
        item.setActionCommand("soundon");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.65"));
        helpMenu.add(item);
        item.setActionCommand("soundoff");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.130"));
        helpMenu.add(item);
        item.setActionCommand("connectsoundon");
        item.addActionListener(this.cbh);
        item = new JMenuItem(Messages.getString("Controls.143"));
        helpMenu.add(item);
        item.setActionCommand("connectsoundoff");
        item.addActionListener(this.cbh);
        helpMenu.addSeparator();
        item = new JMenuItem(Messages.getString("Controls.123"));
        helpMenu.add(item);
        item.setActionCommand("setBalrog");
        item.addActionListener(this.cbh);
        Game.win.setJMenuBar(menuBar);
        this.FPD = new AreaPanel(Game.areas[178], 36);
        this.FPD.setPreferredSize(new Dimension(Game.prefs.diceFPWidth, Game.prefs.diceFPHeight));
        this.FPD.setMaximumSize(new Dimension(Game.prefs.diceFPWidth, Game.prefs.diceFPHeight));
        this.FPD.setBorder(new LineBorder(Color.black, 1));
        add(this.FPD);
        this.FPD.addMouseListener(cah);
        this.SAD = new AreaPanel(Game.areas[179], 36);
        this.SAD.setPreferredSize(new Dimension(Game.prefs.diceSAWidth, Game.prefs.diceSAHeight));
        this.SAD.setMaximumSize(new Dimension(Game.prefs.diceSAWidth, Game.prefs.diceSAHeight));
        this.SAD.setBorder(new LineBorder(Color.black, 1));
        add(this.SAD);
        this.SAD.addMouseListener(cah);
        this.f4hl = new HighlightLabel(this.game);
        this.hlsp = new JScrollPane(this.f4hl, 22, 30);
        add(this.hlsp);
        this.hlsp.setPreferredSize(new Dimension(Game.prefs.highlightWidth, Game.prefs.highlightHeight));
        this.hlsp.setMaximumSize(new Dimension(Game.prefs.highlightWidth, Game.prefs.highlightHeight));
        this.hlsp.getVerticalScrollBar().setUnitIncrement(20);
        this.f4hl.addMouseListener(this.f4hl);
        this.hlsp.setBorder(new LineBorder(Color.black, 1));
        if (Game.boardtype.equals("wotr")) {
            this.chat = new Chat(Game.prefs.chatWidth, Game.prefs.chatHeight, this.game);
        }
        add(this.chat);
        
        // FSM Phase Panel
        this.fsmPhasePanel = new FsmPhasePanel(this.game);
        add(this.fsmPhasePanel);
    }

    /* access modifiers changed from: package-private */
    public void updateHighlight() {
        this.f4hl.setPreferredSize(this.f4hl.happyDimension(this.game.highlight.pieces().size()));
        ((JViewport)this.f4hl.getParent()).setViewSize(this.f4hl.happyDimension(this.game.highlight.pieces().size()));
        this.f4hl.getParent().validate();
        this.f4hl.validate();
        repaint();
    }
}
