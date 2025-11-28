package wotr;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/* renamed from: Hasher */
public class Hasher {
    private static MessageDigest _md;
    private static Random _rnd = new Random();

    static {
        try {
            _md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String GenerateRandomSalt() {
        byte[] salt = new byte[16];
        synchronized (_rnd) {
            _rnd.nextBytes(salt);
        }
        return byteArrayToHexString(salt);
    }

    public static String Hash(String text) {
        byte[] hash;
        try {
            synchronized (_md) {
                hash = _md.digest(text.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            hash = null;
        }
        return byteArrayToHexString(hash);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte b2 : b) {
            int v = b2 & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
}
