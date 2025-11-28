package wotr;

import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/* renamed from: Splasher */
public class Splasher {
    public static void main(String[] args) {
        try {
            Preferences.loadPrefs(new File("wotr_preferences"));
            SplashWindow.splash(Toolkit.getDefaultToolkit().getImage(Messages.getLanguageLocation("images/splash.png")));
            SplashWindow.invokeMain("wotr.Game", args);  // Fixed: use fully qualified class name
            SplashWindow.disposeSplash();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(new JFrame(Messages.getString("Splasher.2")), e + Messages.getString("Splasher.3") + System.getProperty("java.version") + Messages.getString("Splasher.5"), Messages.getString("Splasher.0"), 2);
            System.exit(1);
        }
    }
}
