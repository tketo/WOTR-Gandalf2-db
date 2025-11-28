package wotr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import javax.swing.SwingWorker;

/* renamed from: ObserverHost */
public class ObserverHost implements ObserverNode {
    boolean connected = false;
    Game game;

    /* renamed from: in */
    BufferedReader f12in = null;
    PrintStream out = null;
    int port;
    LinkedList<String> queue = new LinkedList<>();
    ServerSocket serverSocket = null;
    Socket socket = null;
    SwingWorker<String, String> talkThread;
    Thread thd;

    public ObserverHost(Game g) {
        this.game = g;
        this.port = 0;
    }

    public void setPort(int port2) {
        this.port = port2;
    }

    public void setAddress(InetAddress address) {
    }

    public void enqueue(String s) {
        synchronized (this.queue) {
            this.queue.addLast(s);
        }
    }

    public void listenAsServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.socket = this.serverSocket.accept();
            connectionSettings();
            this.out = new PrintStream(this.socket.getOutputStream());
            this.f12in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.connected = true;
            startTalkThread();
            this.game.interpreter.execute(Messages.getString("ObserverHost.0"));
            enqueue("<host> " + this.game.windowTitle.substring(this.game.windowTitle.indexOf(45) + 1));
        } catch (BindException e) {
            this.game.interpreter.execute(Messages.getString("ObserverHost.2"));
            e.printStackTrace();
        } catch (IOException e2) {
            this.game.interpreter.execute(Messages.getString("ObserverHost.3"));
            e2.printStackTrace();
        }
    }

    private String getCommand() {
        try {
            String cmd = this.f12in.readLine();
            if (cmd == null) {
                return null;
            }
            if (!cmd.equals("Disconnecting")) {
                return cmd;
            }
            this.game.interpreter.execute(Messages.getString("ObserverHost.5"));
            disconnect(true);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect(boolean silent) {
        if (!silent) {
            if (this.connected) {
                this.out.println("Disconnecting");
                this.out.flush();
                this.game.interpreter.execute(Messages.getString("ObserverHost.7"));
            } else {
                this.game.interpreter.execute(Messages.getString("ObserverHost.8"));
            }
        }
        this.connected = false;
        abortTalkThread();
        this.queue.clear();
        try {
            if (this.f12in != null) {
                this.f12in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            this.game.interpreter.execute(Messages.getString("ObserverHost.9"));
            e.printStackTrace();
        }
        this.game.removeObserver(this);
    }

    public void connectionSettings() {
        if (this.socket != null) {
            try {
                this.socket.setTrafficClass(16);
                this.socket.setTcpNoDelay(true);
            } catch (SocketException e) {
                System.out.println(String.valueOf(Messages.getString("ObserverHost.10")) + e);
                System.out.flush();
            }
        }
    }

    public void run() {
        listenAsServer();
    }

    public void giveUpHosting() {
        try {
            this.serverSocket.close();
            this.socket.close();
        } catch (Throwable th) {
        }
        this.connected = false;
        this.game.removeObserver(this);
    }

    public void abortTalkThread() {
        if (this.talkThread != null) {
            this.talkThread.cancel(true);
        }
        this.talkThread = null;
    }

    public void startTalkThread() {
        this.talkThread = new SwingWorker<String, String>() {
            public String doInBackground() {
                ObserverHost.this.talkLoop();
                ObserverHost.this.talkThread = null;
                return null;
            }
        };
        this.talkThread.execute();
    }

    public void talkLoop() {
        String cmd;
        while (this.connected) {
            try {
                if (this.f12in.ready() && (cmd = getCommand()) != null && cmd.equals("Incoming")) {
                    this.out.println("Accept");
                    this.out.flush();
                    readLoop();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!this.queue.isEmpty()) {
                sendLoop();
            }
            try {
                Thread.sleep(143);
            } catch (InterruptedException e2) {
            }
        }
    }

    public void sendLoop() {
        String cmd;
        try {
            synchronized (this.queue) {
                this.out.println("Incoming");
                this.out.flush();
            }
            while (true) {
                if (this.f12in.ready() && (cmd = getCommand()) != null) {
                    if (cmd.equals("Incoming")) {
                        break;
                    } else if (cmd.equals("Accept")) {
                        break;
                    }
                }
            }
            synchronized (this.queue) {
                while (!this.queue.isEmpty()) {
                    this.out.println(this.queue.removeFirst());
                }
                this.out.println("Done");
                this.out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLoop() {
        while (true) {
            String cmd = getCommand();
            if (!"Done".equals(cmd)) {
                if (cmd != null) {
                    if (Messages.getString("ObserverHost.18").equals(Messages.removeKeyString(cmd))) {
                        this.game.interpreter.sendLog(this);
                    } else if (this.game.allowObserverCommands || cmd.startsWith("<")) {
                        this.game.talker.enqueue(cmd, this);
                    }
                }
            } else {
                return;
            }
        }
    }
}
