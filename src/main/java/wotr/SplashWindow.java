package wotr;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/* renamed from: SplashWindow */
public class SplashWindow extends Window {
    private static SplashWindow instance;
    private Image image;
    boolean paintCalled = false;

    private SplashWindow(Frame parent, Image image2) {
        super(parent);
        this.image = image2;
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image2, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException e) {
        }
        if (mt.isErrorID(0)) {
            setSize(0, 0);
            System.err.println(Messages.getString("SplashWindow.0"));
            synchronized (this) {
                this.paintCalled = true;
                notifyAll();
            }
            return;
        }
        int imgWidth = image2.getWidth(this);
        int imgHeight = image2.getHeight(this);
        setSize(imgWidth, imgHeight);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - imgWidth) / 2, (screenDim.height - imgHeight) / 2);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                synchronized (SplashWindow.this) {
                    SplashWindow.this.paintCalled = true;
                    SplashWindow.this.notifyAll();
                }
                SplashWindow.this.dispose();
            }
        });
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        g.drawImage(this.image, 0, 0, this);
        if (!this.paintCalled) {
            this.paintCalled = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public static void splash(Image image2) {
        if (instance == null && image2 != null) {
            instance = new SplashWindow(new Frame(), image2);
            instance.setVisible(true);
            instance.paintAll(instance.getGraphics());
            if (!EventQueue.isDispatchThread() && Runtime.getRuntime().availableProcessors() == 1) {
                synchronized (instance) {
                    while (!instance.paintCalled) {
                        try {
                            instance.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    public static void splash(URL imageURL) {
        if (imageURL != null) {
            splash(Toolkit.getDefaultToolkit().createImage(imageURL));
        }
    }

    public static void disposeSplash() {
        if (instance != null) {
            instance.getOwner().dispose();
            instance = null;
        }
    }

    public static void invokeMain(String className, String[] args) {
        try {
            Class.forName(className).getMethod("main", new Class[]{String[].class}).invoke((Object) null, new Object[]{args});
        } catch (Exception e) {
            InternalError error = new InternalError(Messages.getString("SplashWindow.2"));
            error.initCause(e);
            throw error;
        }
    }
}
