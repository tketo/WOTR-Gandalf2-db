package wotr;

/* renamed from: DiceRoller */
public class DiceRoller {
    public static String rollD6(int nDice) {
        return roll(nDice, 6);
    }

    public static String roll(int nDice, int max) {
        String ret = "";
        for (int i = 0; i < nDice; i++) {
            ret = String.valueOf(ret) + " " + roll(max);
        }
        return ret;
    }

    public static int roll(int max) {
        if (max <= 0) {
            return 0;
        }
        return ((int) (Math.random() * ((double) max))) + 1;
    }
}
