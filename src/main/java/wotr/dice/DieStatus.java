package wotr.dice;

/**
 * DieStatus - Current status of an action die
 */
public enum DieStatus {
    /**
     * Die is available to be rolled/used
     */
    AVAILABLE,
    
    /**
     * Die has been rolled and is awaiting use
     */
    ROLLED,
    
    /**
     * Die has been used this turn
     */
    USED,
    
    /**
     * Die is removed from play (special circumstances)
     */
    REMOVED
}
