package wotr;

import java.util.Random;

/* renamed from: SecureDeck */
public class SecureDeck {
    private String[] _cardsHashes;
    private int[] _cardsMapping;
    private String[] _cardsSalts;

    /* access modifiers changed from: package-private */
    public String GetCardHash(int i) {
        if (i < 0 || i >= this._cardsHashes.length) {
            return null;
        }
        return this._cardsHashes[i];
    }

    /* access modifiers changed from: package-private */
    public String GetCardSalt(int i) {
        if (i < 0 || i >= this._cardsSalts.length) {
            return null;
        }
        return this._cardsSalts[i];
    }

    /* access modifiers changed from: package-private */
    public String[] GetCardsHashes() {
        return (String[]) this._cardsHashes.clone();
    }

    /* access modifiers changed from: package-private */
    public String[] GetCardsSalts() {
        return (String[]) this._cardsSalts.clone();
    }

    SecureDeck(int Ncards, String password) {
        this._cardsMapping = new int[Ncards];
        for (int i = 0; i < Ncards; i++) {
            this._cardsMapping[i] = i;
        }
        shuffleDeck();
        this._cardsSalts = new String[Ncards];
        this._cardsHashes = new String[Ncards];
        for (int i2 = 0; i2 < Ncards; i2++) {
            boolean collision = true;
            while (collision) {
                collision = false;
                this._cardsSalts[i2] = Hasher.GenerateRandomSalt();
                String h1 = Hasher.Hash(String.valueOf(password) + this._cardsSalts[i2]);
                this._cardsHashes[i2] = Hasher.Hash(String.valueOf(h1) + this._cardsMapping[i2]);
                if (i2 == 0) {
                    System.out.println(String.valueOf(password) + " " + h1 + " " + this._cardsMapping[0] + " " + this._cardsHashes[0]);
                }
                for (int j = 0; j < i2; j++) {
                    collision = this._cardsHashes[i2].equals(this._cardsHashes[j]) ? true : collision;
                }
            }
        }
    }

    public SecureDeck(String[] cardsHashes, String[] cardsSalts) {
        int N = cardsHashes.length;
        this._cardsMapping = new int[N];
        for (int i = 0; i < N; i++) {
            this._cardsMapping[i] = -1;
        }
        this._cardsHashes = cardsHashes;
        this._cardsSalts = cardsSalts;
    }

    /* access modifiers changed from: package-private */
    public void shuffleDeck() {
        Random rnd = new Random();
        for (int i = this._cardsMapping.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = this._cardsMapping[index];
            this._cardsMapping[index] = this._cardsMapping[i];
            this._cardsMapping[i] = a;
        }
    }

    public boolean VerifyCard(int index, String innerHash, int value) {
        if (this._cardsMapping[index] >= 0) {
            if (this._cardsMapping[index] == value) {
                return true;
            }
            return false;
        } else if (!Hasher.Hash(String.valueOf(innerHash) + value).equals(this._cardsHashes[index])) {
            return false;
        } else {
            this._cardsMapping[index] = value;
            return true;
        }
    }

    public int GetCard(int index) {
        if (index < 0 || index >= this._cardsMapping.length) {
            return -1;
        }
        return this._cardsMapping[index];
    }

    public int Ncards() {
        return this._cardsMapping.length;
    }

    public boolean MapCardsFromPassword(String password) {
        int N = this._cardsHashes.length;
        for (int i = 0; i < N; i++) {
            String s1 = Hasher.Hash(String.valueOf(password) + this._cardsSalts[i]);
            boolean mapped = false;
            if (this._cardsMapping[i] >= 0) {
                mapped = true;
                if (!Hasher.Hash(String.valueOf(s1) + this._cardsMapping[i]).equals(this._cardsHashes[i])) {
                    return false;
                }
            } else {
                int j = 0;
                while (true) {
                    if (j >= N) {
                        break;
                    } else if (Hasher.Hash(String.valueOf(s1) + j).equals(this._cardsHashes[i])) {
                        mapped = true;
                        this._cardsMapping[i] = j;
                        break;
                    } else {
                        j++;
                    }
                }
            }
            if (!mapped) {
                return false;
            }
        }
        return true;
    }

    public void HideCards() {
        for (int i = 0; i < this._cardsMapping.length; i++) {
            this._cardsMapping[i] = -1;
        }
    }
}
