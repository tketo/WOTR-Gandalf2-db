package wotr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.SwingWorker;

public class Talker implements Runnable
{
  boolean connected;
  boolean server;
  LinkedList<String> queue;
  Socket socket;
  ServerSocket serverSocket;
  BufferedReader in;
  PrintStream out;
  Thread thd;
  Game game;
  int port;
  InetAddress address;
  boolean usingConnection;
  SwingWorker<String, String> talkThread;
  
  public Talker(Game g)
  {
    connected = false;
    server = false;
    queue = new LinkedList();
    socket = null;
    serverSocket = null;
    in = null;
    out = null;
    game = g;
    

    port = 0;
    address = null;
  }
  


  public static String myAddress()
  {
    try
    {
      InetAddress localaddr = InetAddress.getLocalHost();
      return localaddr.toString();
    }
    catch (UnknownHostException e)
    {
      return Messages.getString("Talker.0") + e.toString();
    }
  }
  

  public void enqueue(String s)
  {
    enqueue(s, null);
  }
  
  public void enqueue(String s, ObserverHost source) {
    if (connected) {
      synchronized (queue) {
        queue.addLast(s);
      }
    }
    String hash = ShortHash(game.interpreter.GetLogText() + s.trim());
    game.interpreter.SetLastHash(hash);
    game.interpreter.execute(s);
    
    if (!game.interpreter.contactTimer) {
      game.interpreter.contactTimer = false;
    }
    game.sendToObservers(s, source);
    game.sendAsObserver(s);
  }
  
  public void enqueuedirect(String s, ObserverHost source) {
    if (connected) {
      synchronized (queue) {
        queue.addLast(s);
      }
    }
    String hash = ShortHash(game.interpreter.GetLogText() + s.trim());
    game.interpreter.SetLastHash(hash);
    game.interpreter.execute(s);
    
    game.sendToObservers(s, source);
    game.sendAsObserver(s);
  }
  
  public void waitforemptyqueue()
  {
    do
    {
      try
      {
        Thread.sleep(40L);
      }
      catch (InterruptedException localInterruptedException) {}
    } while ((!queue.isEmpty()) && (!game.inaction));
  }
  
  public void listenAsServer()
  {
    try {
      serverSocket = new ServerSocket(port);
      socket = serverSocket.accept();
      connectionSettings();
      out = new PrintStream(socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      connected = true;
      startTalkThread();
      
      game.interpreter.execute(Messages.getString("Talker.31"));
      
      enqueue("<host> " + Game.prefs.nick + Messages.getKeyString("Talker.17") + Game.versionno + " [" + Game.boardtype + " " + Game.varianttype + "]" + Messages.getKeyString("Talker.18") + getViewingHandsString());
      

      game.controls.cbh.TryToSetFPpassword(game.GetFPpassword());
      game.controls.cbh.TryToSetSPpassword(game.GetSPpassword());
    }
    catch (BindException e) {
      game.interpreter.execute(Messages.getString("Talker.19"));
      e.printStackTrace();
    } catch (IOException e) {
      game.interpreter.execute(Messages.getString("Talker.20"));
      e.printStackTrace();
    }
  }
  
  private String getCommand()
  {
    String cmd;
    try {
      cmd = in.readLine();
      cmd = DeHashCommand(cmd);
    } catch (Exception e) {
      e.printStackTrace();
      return null; }
    if (cmd == null) {
      return null;
    }
    
    if (cmd.equals("Disconnecting")) {
      game.interpreter.execute(Messages.getString("Talker.22"));
      disconnect(true);
    } else if ("type".equals(cmd)) {
      game.setOpponentActive(true);
    } else if ("silence".equals(cmd)) {
      game.setOpponentActive(false);
    }
    else {
      return cmd;
    }
    return null;
  }
  
  public void disconnect(boolean silent) {
    if (!silent) {
      if (connected) {
        out.println("Disconnecting");
        out.flush();
        game.interpreter.execute(Messages.getString("Talker.26"));
      } else {
        game.interpreter.execute(Messages.getString("Talker.27"));
      }
    }
    connected = false;
    
    abortTalkThread();
    queue.clear();
    try {
      if (in != null)
        in.close();
      if (out != null)
        out.close();
      if (socket != null)
        socket.close();
      if (serverSocket != null)
        serverSocket.close();
    } catch (IOException e) {
      game.interpreter.execute(Messages.getString("Talker.28"));
      e.printStackTrace();
    }
    game.opponent = "";
    game.opponentHandViewing = "";
    game.redoTitle();
    game.setOpponentActive(false);
  }
  
  public void connectTo() {
    try {
      socket = new Socket(address, port);
      connectionSettings();
      out = new PrintStream(socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      connected = true;
      startTalkThread();
      game.interpreter.execute(Messages.getString("Talker.31"));
      

      enqueue("<client> " + Game.prefs.nick + Messages.getKeyString("Talker.33") + Game.versionno + " [" + Game.boardtype + " " + Game.varianttype + "]" + Messages.getKeyString("Talker.34") + getViewingHandsString());
      
      game.controls.cbh.TryToSetFPpassword(game.GetFPpassword());
      game.controls.cbh.TryToSetSPpassword(game.GetSPpassword());
    }
    catch (IOException e) {
      game.interpreter.execute(Messages.getString("Talker.35"));
      e.printStackTrace();
    }
  }
  
  private String getViewingHandsString() {
    if (game.hasSPpassword()) {
      if (game.hasFPpassword()) {
        return Messages.getKeyString("Talker.36");
      }
      
      return Messages.getKeyString("Talker.37");
    }
    
    if (game.hasFPpassword()) {
      return Messages.getKeyString("Talker.38");
    }
    
    return Messages.getKeyString("Talker.39");
  }
  
  public void connectionSettings() {
    if (socket == null) return;
    try {
      socket.setTrafficClass(16);
      socket.setTcpNoDelay(true);
    } catch (java.net.SocketException e) {
      System.out.println(Messages.getString("Talker.40") + e);
      System.out.flush();
    }
  }
  
  public void run()
  {
    if (server) {
      listenAsServer();
    }
    else {
      connectTo();
    }
  }
  
  public void giveUpHosting() {
    try {
      serverSocket.close();
    } catch (Throwable localThrowable) {}
    try {
      socket.close();
    } catch (Throwable localThrowable1) {}
    connected = false;
  }
 
  public void abortTalkThread()
  {
    if (talkThread != null) {
      talkThread.cancel(true);
    }
    talkThread = null;
  }
  
  public void startTalkThread() {
    talkThread = new SwingWorker()
    {
      public String doInBackground()
      {
        talkLoop();
        talkThread = null;
        return null;
      }
    };
    talkThread.execute();
  }

  public void talkLoop()
  {
    while (connected) {
      try {
        if (in.ready())
        {
          String cmd = getCommand();
          System.out.println("Received message: " + cmd);
          if ((cmd != null) && 
            (cmd.equals("Incoming"))) {
            synchronized (queue) {
              out.println("Accept");
              out.flush();
            }
            readLoop();
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      } catch (Exception e) {
        System.out.println("NEW ERROR");
        e.printStackTrace();
      }
      
      if (!queue.isEmpty())
      {
        sendLoop();
      }
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  public void sendLoop() {
    System.out.println("Entering send loop");
    try
    {
      synchronized (queue) {
        out.println("Incoming");
        out.flush();
      }
      String cmd;
      do
      {
        do
        {
          while (!in.ready()) {}
          


          cmd = getCommand();
        } while (cmd == null);
        

        if (cmd.equals("Incoming")) {
          if (server) {
            break;
          }
          
          readLoop();
          return;
        }
      } while (!cmd.equals("Accept"));
      



      synchronized (queue) {
        while (!queue.isEmpty()) {
          cmd = (String)queue.removeFirst();
          System.out.println("Queue - " + game.interpreter.GetLogText().length() + cmd);
          String hash = ShortHash(game.interpreter.GetLogText() + cmd.trim());
          
          String hashedcmd;
          if (!game.interpreter.onlyForOpponent) {
            game.interpreter.SetLastHash(hash);
            hashedcmd = cmd + " " + hash;
          } else {
            hashedcmd = cmd; }
          out.println(hashedcmd);
          game.interpreter.execute(cmd);
        }
        
        System.out.println("Exiting send loop");
        out.println("Done");
        out.flush();
      }
      return;
    } catch (IOException e) { e.printStackTrace();
    }
  }
  
  public String ShortHash(String text) {
    String password = null;
    if (game.hasFPpassword()) password = game.GetFPpassword(); else {
      password = game.GetSPpassword();
    }
    if (password == null) { return "";
    }
    return Hasher.Hash(text + password).substring(0, 6);
  }
  
  public String DeHashCommand(String command)
  {
    int index = command.lastIndexOf(' ');
    
    if (index >= 0) {
      game.interpreter.SetLastHash(command.substring(index + 1));
      return command.substring(0, index);
    }
    
    game.interpreter.SetLastHash(null);
    return command;
  }
  
  public void readLoop() {
    System.out.println("Entering read loop");
    

    String cmd;
    
    while (!"Done".equals(cmd = getCommand())) { 
      if (cmd == null) {
        System.out.println("Read loop nulls");
      }
      else {
        String unallteredcmd = cmd;
        cmd = Messages.removeKeyString(cmd);
        

        if (Messages.removeKeyString(Messages.getString("Controls.825")).equals(cmd)) {
          game.interpreter.sendLog();
        }
        else
        {
          try {
            if ((cmd.startsWith(Messages.getString("Talker.49") + game.opponent)) && (cmd.indexOf(Messages.getString("Talker.50")) > 0) && (!game.interpreter.fromFile)) {
              String tmp = cmd.substring(cmd.indexOf(Messages.getString("Talker.51")) + Messages.getString("Talker.52").length());
              game.opponent = tmp.substring(0, tmp.lastIndexOf('.'));
              game.redoTitle();
            }
            else if ((cmd.startsWith("<game> " + game.opponent + Messages.getString("Talker.54"))) && (cmd.contains(Messages.getString("Talker.55")))) {
              game.setViewing(false, cmd.contains(Messages.getString("Talker.56")), !cmd.contains(Messages.getString("Talker.57")));
            }
            else if (cmd.contains(Messages.getString("Talker.58"))) {
              game.chosen1 = null;
              game.chosen2 = null;
              game.chosen1player = null;
              game.chosen2player = null;
            }
            else if ((cmd.startsWith("<client>")) || (cmd.startsWith("<host>"))) {
              game.opponent = cmd.substring(cmd.indexOf(">") + 2, cmd.indexOf(Messages.getString("Talker.17")));
              if (cmd.contains(Messages.getString("Talker.63"))) {
                game.setViewing(false, true, true);
                game.setViewing(false, false, true);
              } else if (cmd.contains(Messages.getString("Talker.64"))) {
                game.setViewing(false, true, true);
              } else if (cmd.contains(Messages.getString("Talker.65"))) {
                game.setViewing(false, false, true);
              } else {
                game.redoTitle();
              }
            }
          } catch (Throwable e) {
            e.printStackTrace();
          }
          game.interpreter.execute(unallteredcmd);
          game.sendToObservers(unallteredcmd, null);
        }
      }
    }
  }
}
