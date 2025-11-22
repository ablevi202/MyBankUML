package bank;

public class Branch {
    private final String address;
    private final Bank bank;

    public Branch(String address, Bank bank) {
        this.address = address;
        this.bank = bank;
        // REMOVED: bank.addBranch(this);  
    }

    public void printBranchInfo() {
        System.out.println("Branch " + address + " From Bank " + bank.getName());
    }

    public String getAddress() {
        return address;
    }

    public Bank getBank() {
        return bank;
    }
}