package wotr;

/* renamed from: HandSecurity */
public class HandSecurity extends Hasher {
    private static String _genericPassword = "secret";
    private SecureDeck _characterCards = null;
    private SecureDeck _factionCards = null;
    private SecureDeck _genericCards = null;
    private String _password;
    private String _passwordHash;
    private String _passwordSalt;
    private SecureDeck _strategyCards = null;

    public boolean SetPassword(String password, int numberCcards, int numberScards, int numberGcards, int numberFcards) {
        if (this._passwordHash == null) {
            this._password = password;
            this._passwordSalt = GenerateRandomSalt();
            this._passwordHash = Hash(String.valueOf(this._passwordSalt) + this._password);
            this._characterCards = new SecureDeck(numberCcards, password);
            this._strategyCards = new SecureDeck(numberScards, password);
            this._genericCards = new SecureDeck(numberGcards, _genericPassword);
            this._factionCards = new SecureDeck(numberFcards, password);
            return true;
        } else if (!VerifyPassword(password)) {
            return false;
        } else {
            this._password = password;
            return true;
        }
    }

    public String GetPassword() {
        return this._password;
    }

    public void ClearPassword() {
        this._password = null;
        this._passwordHash = null;
        this._passwordSalt = null;
        this._characterCards.HideCards();
        this._strategyCards.HideCards();
        this._factionCards.HideCards();
    }

    public void ClearDecks() {
        this._characterCards = null;
        this._strategyCards = null;
        this._genericCards = null;
        this._factionCards = null;
    }

    public String GetPasswordSalt() {
        return this._passwordSalt;
    }

    public String GetPasswordHash() {
        return this._passwordHash;
    }

    public SecureDeck GetCharacterDeck() {
        return this._characterCards;
    }

    public SecureDeck GetStrategyDeck() {
        return this._strategyCards;
    }

    public SecureDeck GetGenericDeck() {
        return this._genericCards;
    }

    public SecureDeck GetFactionDeck() {
        return this._factionCards;
    }

    public void SetGenericDeck(String[] hashes, String[] salts) {
        this._genericCards = new SecureDeck(hashes, salts);
        this._genericCards.MapCardsFromPassword(_genericPassword);
    }

    public boolean SetHashAndSalt(String passwordHash, String passwordSalt, String[] CcardsHashes, String[] CcardsSalts, String[] ScardsHashes, String[] ScardsSalts, String[] FcardsHashes, String[] FcardsSalts, String[] GcardsHashes, String[] GcardsSalts) {
        if ((this._passwordHash != null && !this._passwordHash.equals(passwordHash)) || (this._passwordSalt != null && !this._passwordSalt.equals(passwordSalt))) {
            return false;
        }
        this._passwordHash = passwordHash;
        this._passwordSalt = passwordSalt;
        if (this._characterCards == null) {
            this._characterCards = new SecureDeck(CcardsHashes, CcardsSalts);
        }
        if (this._strategyCards == null) {
            this._strategyCards = new SecureDeck(ScardsHashes, ScardsSalts);
        }
        if (this._factionCards == null) {
            this._factionCards = new SecureDeck(FcardsHashes, FcardsSalts);
        }
        this._genericCards = new SecureDeck(GcardsHashes, GcardsSalts);
        return true;
    }

    public boolean isEmpty() {
        return this._passwordHash == null;
    }

    public boolean VerifyPassword(String password) {
        if (password == null) {
            return false;
        }
        if (this._passwordSalt == null || this._passwordHash == null) {
            return true;
        }
        return Hash(String.valueOf(this._passwordSalt) + password).equals(this._passwordHash);
    }

    public boolean MapCardsFromPassword() {
        if (this._password != null && this._characterCards.MapCardsFromPassword(this._password) && this._strategyCards.MapCardsFromPassword(this._password) && this._factionCards.MapCardsFromPassword(this._password)) {
            return true;
        }
        return false;
    }
}
