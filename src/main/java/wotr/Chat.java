package wotr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
//import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Chat
  extends JPanel
  implements ActionListener
{
  boolean jumpToBottom = false;
  boolean empty = true;
  

  JScrollPane jsp;
  
  JScrollBar jsb;
  
  JTextPane textPane = new JTextPane(new DefaultStyledDocument());
  SimpleAttributeSet attrSet = new SimpleAttributeSet();
  
  JTextField jtf;
  Game game;
  public boolean silent = false;
  private String lastMessage = null; // Track last message for deduplication
  private long lastMessageTime = 0; // Track when last message was written
  
  JTextField status;
  Timer silenceTimer;
  String myColour;
  String myRed;
  String myGreen;
  String myBlue;
  String opponentColour;
  String opponentRed;
  String opponentGreen;
  String opponentBlue;
  String myactionColour;
  String myactionRed;
  String myactionGreen;
  String myactionBlue;
  String opponentactionColour;
  String opponentactionRed;
  String opponentactionGreen;
  String opponentactionBlue;
  
  public Chat(int w, int h, Game g)
  {
    setPreferredSize(new Dimension(w, h));
    
    setLayout(new BoxLayout(this, 1));
    
    game = g;
    StyleConstants.setFontFamily(attrSet, Game.prefs.font);
    StyleConstants.setFontSize(attrSet, Game.prefs.fontsize);
    
    textPane.setEditable(false);
    
    StyleConstants.setForeground(attrSet, Color.black);
    myColour = Game.prefs.myChatColour;
    myRed = myColour.substring(0, myColour.indexOf(","));
    myGreen = myColour.substring(myColour.indexOf(",") + 1, myColour.lastIndexOf(","));
    myBlue = myColour.substring(myColour.lastIndexOf(",") + 1);
    opponentColour = Game.prefs.opponentChatColour;
    opponentRed = opponentColour.substring(0, opponentColour.indexOf(","));
    opponentGreen = opponentColour.substring(opponentColour.indexOf(",") + 1, opponentColour.lastIndexOf(","));
    opponentBlue = opponentColour.substring(opponentColour.lastIndexOf(",") + 1);
    
    myactionColour = Game.prefs.myActionColour;
    myactionRed = myactionColour.substring(0, myactionColour.indexOf(","));
    myactionGreen = myactionColour.substring(myactionColour.indexOf(",") + 1, myactionColour.lastIndexOf(","));
    myactionBlue = myactionColour.substring(myactionColour.lastIndexOf(",") + 1);
    opponentactionColour = Game.prefs.opponentActionColour;
    opponentactionRed = opponentactionColour.substring(0, opponentactionColour.indexOf(","));
    opponentactionGreen = opponentactionColour.substring(opponentactionColour.indexOf(",") + 1, opponentactionColour.lastIndexOf(","));
    opponentactionBlue = opponentactionColour.substring(opponentactionColour.lastIndexOf(",") + 1);

    jsp = new JScrollPane(textPane, 22, 31);

    textPane.addMouseWheelListener(new MouseWheelListener()
    {
      public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
          game.interpreter.decreaseReplaySpeed();
        } else {
          game.interpreter.increaseReplaySpeed();
          
        }
      }
    });
    textPane.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent e)
      {
        e.getModifiers();
        if ((e.getModifiers() & 0x8) == 8)
          game.interpreter.toggleReplayClock();
        if ((e.getModifiers() & 0x4) == 4) {
          game.interpreter.replayStep();
        }
      }
      
      public void mouseEntered(MouseEvent e) {}

      public void mouseExited(MouseEvent e) {}

      public void mousePressed(MouseEvent e) {}

      public void mouseReleased(MouseEvent e) {}
    });
    jsb = new JScrollBar();
    
    jsb.addAdjustmentListener(new AdjustmentListener()
    {
      public void adjustmentValueChanged(AdjustmentEvent e)
      {
        if (jumpToBottom) {
          jumpToBottom = false;
          int bottom = jsb.getMaximum() - jsb.getVisibleAmount();
          jsb.setValue(bottom);
        }
        
      }
    });
    jsp.setVerticalScrollBar(jsb);
    
    add(jsp);
    jsp.setPreferredSize(new Dimension(w - 4, h - 44));
    
    status = new JTextField();
    status.setEditable(false);
    status.setFont(new Font("SansSerif", 0, 10));
    status.setPreferredSize(new Dimension(w - 4, 15));
    status.setMaximumSize(new Dimension(10000000, 15));
    add(status);
    status.setBackground(getBackground());
    status.setBorder(null);
    status.setHorizontalAlignment(0);
    
    jtf = new JTextField();
    add(jtf);
    jtf.setPreferredSize(new Dimension(w - 4, 20));
    jtf.setMaximumSize(new Dimension(10000000, 20));
    jtf.addActionListener(this);
    
    InputMap map = jtf.getInputMap();
    while (map != null) {
      map.remove(KeyStroke.getKeyStroke("control H"));
      map.remove(KeyStroke.getKeyStroke("DELETE"));
      map.remove(KeyStroke.getKeyStroke("control RIGHT"));
      map.remove(KeyStroke.getKeyStroke("control LEFT"));
      map = map.getParent();
    }

    map = textPane.getInputMap();
    while (map != null) {
      map.remove(KeyStroke.getKeyStroke("control H"));
      map.remove(KeyStroke.getKeyStroke("DELETE"));
      map.remove(KeyStroke.getKeyStroke("control RIGHT"));
      map.remove(KeyStroke.getKeyStroke("control LEFT"));
      map = map.getParent();
    }
    
    map = status.getInputMap();
    while (map != null) {
      map.remove(KeyStroke.getKeyStroke("control H"));
      map.remove(KeyStroke.getKeyStroke("DELETE"));
      map.remove(KeyStroke.getKeyStroke("control RIGHT"));
      map.remove(KeyStroke.getKeyStroke("control LEFT"));
      map = map.getParent();
    }
    
    jtf.setBorder(new SoftBevelBorder(1));
    
    silenceTimer = new Timer(2000, new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (game.talker.connected) {
          synchronized (game.talker.queue) {
            game.talker.out.println("silence");
          }
        }
      }
    });
    silenceTimer.setRepeats(false);
    jtf.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent e) {
        if ((e.getKeyChar() != '\n') && (e.getKeyChar() != '\033') && (e.getKeyChar() != '') && (!e.isControlDown())) {
          if ((!silenceTimer.isRunning()) && (game.talker.connected)) {
            synchronized (game.talker.queue) {
              game.talker.out.println("type");
            }
          }
          silenceTimer.restart();
        }
      }
    });
  }
  
  private String realStatusString = "";
  
  public synchronized void setStatus(String statusString) {
    String newStatus = statusString == null ? "" : statusString;
    try
    {
      if (realStatusString.equals(status.getText())) {
        status.setText(newStatus);
      }
      realStatusString = newStatus;
    } catch (Exception e) {
      System.out.println("Freeze?" + e.getMessage());
      
      e.printStackTrace();
    }
  }

  public void write(String s)
  {
    // Deduplicate: skip if this exact message was written within 500ms (network echo)
    long now = System.currentTimeMillis();
    if (s.equals(lastMessage) && (now - lastMessageTime) < 500) {
      // Likely a network echo, skip it
      return;
    }
    lastMessage = s;
    lastMessageTime = now;
    
    int myNickLength = Game.prefs.nick.length() + 2;
    int opponentNickLength = game.opponent.length() + 2;
    
    if ((s.length() >= myNickLength) && (s.substring(0, myNickLength).equals("<" + Game.prefs.nick + ">")) && (Game.prefs.nick.length() > 0))
    {
      StyleConstants.setForeground(attrSet, new Color(Integer.parseInt(myRed), Integer.parseInt(myGreen), Integer.parseInt(myBlue)));
    }
    else if ((s.length() >= opponentNickLength) && (s.substring(0, opponentNickLength).equals("<" + game.opponent + ">")) && (game.opponent.length() > 0)) {
      StyleConstants.setForeground(attrSet, new Color(Integer.parseInt(opponentRed), Integer.parseInt(opponentGreen), Integer.parseInt(opponentBlue)));
    }
    else if ((s.length() >= myNickLength) && (s.substring(0, myNickLength).equals("$" + Game.prefs.nick + " ")) && (Game.prefs.nick.length() > 0)) {
      StyleConstants.setForeground(attrSet, new Color(Integer.parseInt(myactionRed), Integer.parseInt(myactionGreen), Integer.parseInt(myactionBlue)));
    }
    else if ((s.length() >= opponentNickLength) && (s.substring(0, opponentNickLength).equals("$" + game.opponent + " ")) && (game.opponent.length() > 0)) {
      StyleConstants.setForeground(attrSet, new Color(Integer.parseInt(opponentactionRed), Integer.parseInt(opponentactionGreen), Integer.parseInt(opponentactionBlue)));
    }
    else {
      StyleConstants.setForeground(attrSet, new Color(0, 0, 0));
    }
    if (game.historyactionpointer < game.historypointer) {
      if (game.historyactionpointer < game.historyactions.size()) {
        game.historyactions.set(game.historyactionpointer, s);
        while (game.historyactionpointer < game.historyactions.size() - 1) {
          game.historyactions.remove(game.historyactionpointer + 1);
        }
      }
      else {
        game.historyactions.add(s);
      }
      game.historyactionpointer += 1;
    }
    if (silent)
      return;
    jumpToBottom = true;
    
    if (empty)
    {
      try {
        textPane.getDocument().insertString(textPane.getDocument().getLength(), s, attrSet);
      }
      catch (BadLocationException e1) {
        e1.printStackTrace();
      }
      empty = false;
    }
    else
    {
      try {
        textPane.getDocument().insertString(textPane.getDocument().getLength(), "\n" + s, attrSet);
      }
      catch (BadLocationException e1) {
        e1.printStackTrace();
      }
      textPane.invalidate();
      jsb.invalidate();
    }
    
    textPane.invalidate();
    jsb.invalidate();
  }
  
  public void writenohistory(String s)
  {
    if (silent)
      return;
    jumpToBottom = true;
    
    if (empty)
    {
      try {
        textPane.getDocument().insertString(textPane.getDocument().getLength(), s, attrSet);
      }
      catch (BadLocationException e1) {
        e1.printStackTrace();
      }
      empty = false;
    }
    else {
      try {
        textPane.getDocument().insertString(textPane.getDocument().getLength(), "\n" + s, attrSet);
      }
      catch (BadLocationException e1) {
        e1.printStackTrace();
      }
    }
    
    textPane.invalidate();
    jsb.invalidate();
  }
  
  public void actionPerformed(ActionEvent e)
  {
    jtf.setText(null);
    game.talker.enqueue("<" + Game.prefs.nick + "> " + e.getActionCommand());
    
    if (silenceTimer.isRunning()) {
      silenceTimer.stop();
      if (game.talker.connected) {
        synchronized (game.talker.queue) {
          game.talker.out.println("silence");
        }
      }
    }
  }
}
