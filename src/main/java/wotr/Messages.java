package wotr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/* renamed from: Messages */
public class Messages {
    private static ResourceBundle RESOURCE_BUNDLE;
    private static ResourceBundle RESOURCE_BUNDLE2;

    public static String getString(String key) {
        if (RESOURCE_BUNDLE == null) {
            try {
                RESOURCE_BUNDLE = new PropertyResourceBundle(new FileInputStream(new File(getLanguageLocation("messages.properties"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (RESOURCE_BUNDLE2 == null) {
            try {
                if (Game.varianttype.startsWith("expansion2")) {
                    String currVariantType = Game.varianttype;
                    Game.varianttype = "base2";
                    RESOURCE_BUNDLE2 = new PropertyResourceBundle(new FileInputStream(new File(getLanguageLocation("messages.properties"))));
                    Game.varianttype = currVariantType;
                } else {
                    RESOURCE_BUNDLE2 = new PropertyResourceBundle(new FileInputStream(new File("messages.properties")));
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e12) {
                e12.printStackTrace();
            }
        }
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e3) {
            try {
                return RESOURCE_BUNDLE2.getString(key);
            } catch (MissingResourceException e4) {
                return String.valueOf('!') + key + '!';
            }
        }
    }

    public static String getKeyString(String key) {
        return "<~" + key + "~>";
    }

    public static String removeKeyString(String key) {
        String res = key;
        if (res == null) {
            String str = res;
            return res;
        }
        while (res.indexOf("~>") != -1) {
            res = String.valueOf(res.substring(0, res.indexOf("<~"))) + getString(res.substring(res.indexOf("<~") + 2, res.indexOf("~>"))) + res.substring(res.indexOf("~>") + 2);
        }
        String str2 = res;
        return res;
    }

    public static String getLanguageLocation(String file) {
        String newLocation = null;
        String originalFile = file;
        String varianttype = Game.varianttype;
        newLocation = file;
        if (varianttype.contains("[")) {
            varianttype = varianttype.substring(0, varianttype.indexOf("["));
        }
        if (varianttype.startsWith("base2")) {
            newLocation = "2ndedition/" + file.substring(0, file.lastIndexOf(".")) + "b2" + file.substring(file.lastIndexOf("."));
        }
        if (varianttype.equals("base")) {
            newLocation = "base/" + file.substring(0, file.lastIndexOf(".")) + "b" + file.substring(file.lastIndexOf("."));
        }
        if (varianttype.equals("expansion2")) {
            newLocation = "expansion2/" + file.substring(0, file.lastIndexOf(".")) + "-E" + file.substring(file.lastIndexOf("."));
        }
        String file2try = file;
        if (Game.prefs != null && !Game.prefs.language.equals("")) {
            newLocation = String.valueOf(Game.prefs.language) + "/" + newLocation;
            file2try = String.valueOf(Game.prefs.language) + "/" + file;
        }
        boolean exists = new File(newLocation).exists();
        if (!exists && varianttype.equals("expansion2")) {
            file2try = originalFile;
            newLocation = "2ndedition/" + file2try.substring(0, file2try.lastIndexOf(".")) + "b2" + file2try.substring(file2try.lastIndexOf("."));
            if (Game.prefs != null && !Game.prefs.language.equals("")) {
                newLocation = String.valueOf(Game.prefs.language) + "/" + newLocation;
                file2try = String.valueOf(Game.prefs.language) + "/" + file2try;
            }
            exists = new File(newLocation).exists();
        }
        if (exists) {
            return newLocation;
        }
        if (new File(file2try).exists()) {
            return file2try;
        }
        return originalFile;
    }
}
