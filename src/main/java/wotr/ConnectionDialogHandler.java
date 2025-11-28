package wotr;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/* renamed from: ConnectionDialogHandler */
/* compiled from: Controls */
class ConnectionDialogHandler implements ActionListener {
    static ConnectionDialogHandler instance;
    JTextField address;
    JDialog cancelDialog;
    JDialog dialog;
    Game game;
    SwingWorker<String, String> networkThread;
    JTextField port;

    ConnectionDialogHandler(Game g, JTextField port2, JTextField address2, JDialog dialog2) {
        this.game = g;
        this.port = port2;
        this.address = address2;
        this.dialog = dialog2;
    }

    public static ConnectionDialogHandler getInstance(Game g, JTextField port2, JTextField address2, JDialog dialog2) {
        if (instance == null) {
            instance = new ConnectionDialogHandler(g, port2, address2, dialog2);
        } else {
            instance.game = g;
            instance.port = port2;
            instance.address = address2;
            instance.dialog = dialog2;
        }
        return instance;
    }

    public void stopNetworkThread() {
        if (this.networkThread != null) {
            this.networkThread.cancel(true);
            this.game.talker.giveUpHosting();
        }
        this.networkThread = null;
    }

    public void startNetworkThread() {
        this.networkThread = new SwingWorker<String, String>() {
            public String doInBackground() {
                ConnectionDialogHandler.this.game.talker.run();
                ConnectionDialogHandler.this.removeCancelDialog();
                ConnectionDialogHandler.this.networkThread = null;
                return null;
            }
        };
        this.networkThread.execute();
    }

    public void removeCancelDialog() {
        if (this.cancelDialog != null) {
            this.cancelDialog.dispose();
            this.cancelDialog = null;
        }
    }

    public void showCancelDialog(String title) {
        this.cancelDialog = new JDialog(Game.win, title);
        JButton jb = new JButton(Messages.getString("Controls.914"));
        jb.setActionCommand("cancel");
        jb.setSize(200, 35);
        jb.addActionListener(this);
        this.cancelDialog.getContentPane().add(jb);
        this.cancelDialog.setSize(305, 70);
        this.cancelDialog.setLocation((Game.win.getX() + (Game.win.getWidth() / 2)) - (this.cancelDialog.getWidth() / 2), (Game.win.getY() + (Game.win.getHeight() / 2)) - (this.cancelDialog.getHeight() / 2));
        this.cancelDialog.setLayout(new FlowLayout());
        this.cancelDialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("wait")) {
            this.game.talker.server = true;
            this.game.talker.port = Integer.parseInt(this.port.getText());
            Game.prefs.port = this.game.talker.port;
            this.dialog.dispose();
            stopNetworkThread();
            startNetworkThread();
            showCancelDialog(Messages.getString("Controls.917"));
        } else if (e.getActionCommand().equals("connect")) {
            try {
                this.game.talker.server = false;
                this.game.talker.address = InetAddress.getByName(this.address.getText());
                this.game.talker.port = Integer.parseInt(this.port.getText());
                Game.prefs.port = this.game.talker.port;
                Game.prefs.addr = this.game.talker.address.toString().substring(1);
                this.dialog.dispose();
                stopNetworkThread();
                startNetworkThread();
                showCancelDialog(Messages.getString("Controls.919"));
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            }
        } else if (e.getActionCommand().equals("cancel")) {
            removeCancelDialog();
            stopNetworkThread();
        }
    }
}
