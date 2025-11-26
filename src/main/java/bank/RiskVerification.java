package bank;

/**
 * Encapsulates business rules for transaction risk assessment.
 * <p>
 * This class determines if a transaction requires manual review based on
 * predefined thresholds (e.g., amounts exceeding $10,000). It is used by
 * the {@link UIManager} to set the initial status of a transaction.
 * </p>
 */
public class RiskVerification {

    /**
     * The monetary threshold above which a transaction is considered high risk.
     * Current limit: $10,000.00.
     */
    private static final double RISK_LIMIT = 10000.00;

    /**
     * Checks if a given transaction amount exceeds the risk limit.
     *
     * @param amount The transaction amount to evaluate.
     * @return {@code true} if the amount is greater than the risk limit; {@code false} otherwise.
     */
    public boolean isHighRisk(double amount) {
        return amount > RISK_LIMIT;
    }

    /**
     * Determines the initial status of a transaction based on its risk level.
     *
     * @param amount The transaction amount.
     * @return "PENDING_REVIEW" if high risk, otherwise "COMPLETED".
     */
    public String getTransactionStatus(double amount) {
        return isHighRisk(amount) ? "PENDING_REVIEW" : "COMPLETED";
    }
}