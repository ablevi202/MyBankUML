package bank;

public class RiskVerification {
    // Requirement: Transactions > $10,000 must be reviewed
    private static final double RISK_LIMIT = 10000.00;

    public boolean isHighRisk(double amount) {
        return amount > RISK_LIMIT;
    }

    public String getTransactionStatus(double amount) {
        return isHighRisk(amount) ? "PENDING_REVIEW" : "COMPLETED";
    }
}