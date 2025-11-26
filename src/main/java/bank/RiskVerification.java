package bank;

public class RiskVerification {
    // Defined limit from requirements
    private static final double RISK_LIMIT = 10000.00;

    public boolean isHighRisk(double amount) {
        return amount > RISK_LIMIT;
    }
    
    public String determineStatus(double amount) {
        if (isHighRisk(amount)) {
            System.out.println("Risk Alert: Transaction of $" + amount + " exceeds limit.");
            return "PENDING_REVIEW";
        }
        return "COMPLETED";
    }
}