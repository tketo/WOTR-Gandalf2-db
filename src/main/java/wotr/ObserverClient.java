package wotr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import javax.swing.SwingWorker;

/* renamed from: ObserverClient */
public class ObserverClient implements ObserverNode {
    InetAddress address;
    boolean connected = false;
    Game game;

    /* renamed from: in */
    BufferedReader f11in = null;
    PrintStream out = null;
    int port;
    LinkedList<String> queue = new LinkedList<>();
    Socket socket = null;
    SwingWorker<String, String> talkThread;
    Thread thd;

    public ObserverClient(Game g) {
        this.game = g;
        this.port = 0;
        this.address = null;
    }

    public void setPort(int port2) {
        this.port = port2;
    }

    public void setAddress(InetAddress address2) {
        this.address = address2;
    }

    public void enqueue(String s) {
        synchronized (this.queue) {
            this.queue.addLast(s);
        }
    }

    public void connectTo() {
        try {
            this.socket = new Socket(this.address, this.port);
            connectionSettings();
            this.out = new PrintStream(this.socket.getOutputStream());
            this.f11in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.connected = true;
            startTalkThread();
            this.game.interpreter.execute(Messages.getString("ObserverClient.0"));
            enqueue(String.valueOf(Messages.getString("ObserverClient.1")) + Game.prefs.nick);
        } catch (IOException e) {
            this.game.interpreter.execute(Messages.getString("ObserverClient.2"));
            e.printStackTrace();
        }
    }

    private String getCommand() {
        try {
            String cmd = this.f11in.readLine();
            if (cmd == null) {
                return null;
            }
            if (!cmd.equals("Disconnecting")) {
                return cmd;
            }
            this.game.interpreter.execute(Messages.getString("ObserverClient.4"));
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
                this.game.interpreter.execute(Messages.getString("ObserverClient.6"));
            } else {
                this.game.interpreter.execute(Messages.getString("ObserverClient.7"));
            }
        }
        this.connected = false;
        abortTalkThread();
        this.queue.clear();
        try {
            if (this.f11in != null) {
                this.f11in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            this.game.interpreter.execute(Messages.getString("ObserverClient.8"));
            e.printStackTrace();
        }
    }

    public void connectionSettings() {
        if (this.socket != null) {
            try {
                this.socket.setTrafficClass(16);
                this.socket.setTcpNoDelay(true);
            } catch (SocketException e) {
                System.out.println(String.valueOf(Messages.getString("ObserverClient.9")) + e);
                System.out.flush();
            }
        }
    }

    public void run() {
        connectTo();
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
                ObserverClient.this.talkLoop();
                ObserverClient.this.talkThread = null;
                return null;
            }
        };
        this.talkThread.execute();
    }

    public void talkLoop() {
        String cmd;
        while (this.connected) {
            try {
                if (this.f11in.ready() && (cmd = getCommand()) != null && cmd.equals("Incoming")) {
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

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003a, code lost:
        r4 = r6.queue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x003c, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0043, code lost:
        if (r6.queue.isEmpty() == false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0045, code lost:
        r6.out.println("Done");
        r6.out.flush();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0051, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r6.out.println(r6.queue.removeFirst());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendLoop() {
        /*
            r6 = this;
            java.util.LinkedList<java.lang.String> r4 = r6.queue     // Catch:{ IOException -> 0x002d }
            monitor-enter(r4)     // Catch:{ IOException -> 0x002d }
            java.io.PrintStream r3 = r6.out     // Catch:{ all -> 0x002a }
            java.lang.String r5 = "Incoming"
            r3.println(r5)     // Catch:{ all -> 0x002a }
            java.io.PrintStream r3 = r6.out     // Catch:{ all -> 0x002a }
            r3.flush()     // Catch:{ all -> 0x002a }
            monitor-exit(r4)     // Catch:{ all -> 0x002a }
        L_0x0010:
            java.io.BufferedReader r3 = r6.f11in     // Catch:{ IOException -> 0x002d }
            boolean r3 = r3.ready()     // Catch:{ IOException -> 0x002d }
            if (r3 == 0) goto L_0x0010
            java.lang.String r1 = r6.getCommand()     // Catch:{ IOException -> 0x002d }
            if (r1 == 0) goto L_0x0010
            java.lang.String r3 = "Incoming"
            boolean r3 = r1.equals(r3)     // Catch:{ IOException -> 0x002d }
            if (r3 == 0) goto L_0x0032
            r6.readLoop()     // Catch:{ IOException -> 0x002d }
        L_0x0029:
            return
        L_0x002a:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x002a }
            throw r3     // Catch:{ IOException -> 0x002d }
        L_0x002d:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0029
        L_0x0032:
            java.lang.String r3 = "Accept"
            boolean r3 = r1.equals(r3)     // Catch:{ IOException -> 0x002d }
            if (r3 == 0) goto L_0x0010
            java.util.LinkedList<java.lang.String> r4 = r6.queue     // Catch:{ IOException -> 0x002d }
            monitor-enter(r4)     // Catch:{ IOException -> 0x002d }
        L_0x003d:
            java.util.LinkedList<java.lang.String> r3 = r6.queue     // Catch:{ all -> 0x0053 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0053 }
            if (r3 == 0) goto L_0x0056
            java.io.PrintStream r3 = r6.out     // Catch:{ all -> 0x0053 }
            java.lang.String r5 = "Done"
            r3.println(r5)     // Catch:{ all -> 0x0053 }
            java.io.PrintStream r3 = r6.out     // Catch:{ all -> 0x0053 }
            r3.flush()     // Catch:{ all -> 0x0053 }
            monitor-exit(r4)     // Catch:{ all -> 0x0053 }
            goto L_0x0029
        L_0x0053:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0053 }
            throw r3     // Catch:{ IOException -> 0x002d }
        L_0x0056:
            java.util.LinkedList<java.lang.String> r3 = r6.queue     // Catch:{ all -> 0x0053 }
            java.lang.Object r3 = r3.removeFirst()     // Catch:{ all -> 0x0053 }
            r0 = r3
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0053 }
            r1 = r0
            java.io.PrintStream r3 = r6.out     // Catch:{ all -> 0x0053 }
            r3.println(r1)     // Catch:{ all -> 0x0053 }
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: wotr.ObserverClient.sendLoop():void");
    }

    public void readLoop() {
        while (true) {
            String cmd = getCommand();
            if (!"Done".equals(cmd)) {
                if (cmd != null && !cmd.startsWith("<hostkeys>")) {
                    if (Messages.getString("ObserverClient.17").equals(Messages.removeKeyString(cmd))) {
                        this.game.interpreter.sendLog(this);
                    } else {
                        this.game.interpreter.execute(cmd);
                    }
                }
            } else {
                return;
            }
        }
    }

    public void giveUpHosting() {
        try {
            this.socket.close();
        } catch (Throwable th) {
        }
        this.connected = false;
    }
}
