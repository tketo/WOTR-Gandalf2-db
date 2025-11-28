package wotr.rules;

/**
 * ValidationResult - Result of a rules validation check
 * 
 * Immutable result object that indicates whether an action is valid
 * and provides a reason if it's not.
 */
public class ValidationResult {
    private final boolean valid;
    private final String reason;
    
    private ValidationResult(boolean valid, String reason) {
        this.valid = valid;
        this.reason = reason;
    }
    
    /**
     * Create a successful validation result
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }
    
    /**
     * Create a failed validation result with reason
     */
    public static ValidationResult failure(String reason) {
        return new ValidationResult(false, reason);
    }
    
    /**
     * Create an error result (database/system error)
     */
    public static ValidationResult error(String errorMessage) {
        return new ValidationResult(false, "ERROR: " + errorMessage);
    }
    
    /**
     * Is the action valid?
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Get the reason for failure (null if valid)
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Get a user-friendly message
     */
    public String getMessage() {
        if (valid) {
            return "Action is valid";
        } else {
            return reason != null ? reason : "Action is not valid";
        }
    }
    
    @Override
    public String toString() {
        return valid ? "VALID" : "INVALID: " + reason;
    }
}
