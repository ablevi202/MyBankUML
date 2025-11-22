package bank;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    protected Customer customer; // The customer that owns the account
    protected List<Transaction> transactions; // List of all transactions involving the account
    private final String accountID; // Unique account ID
    private static int nextAccountID = 0; // Stores the next accountID so all account IDs are unique
    private float balance; // The balance of the account
    private String accountType; // The type of account, either chequing or saving account


    // Constructors
    // Constructor with only a customer and the type of account
    public Account(Customer customer, String accountType) {
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.accountType = accountType;
        this.accountID = String.valueOf(nextAccountID);
        nextAccountID++;
    }
    // Constructor with customer, account type, and also balance
        public Account(Customer customer, String accountType, float balance) {
        this.customer = customer;
        this.transactions = new ArrayList<>();
        this.balance = balance;
        this.accountType = accountType;
        this.accountID = String.valueOf(nextAccountID);
        nextAccountID++;
    }

    // Accessors
    public Customer getCustomer(){
        return customer;
    }

    public String getAccountID(){
        return accountID;
    }

    public float getBalance(){
        return balance;
    }

    public String getAccountType(){
        return accountType;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    // Mutators
    public void setAccountType(String accountType){
        this.accountType = accountType;
    }

    public void setBalance(float balance){
        this.balance = balance;
    }

    public void deposit(float deposit){ 
        this.balance += deposit;
    }

    public void withdraw(float withdraw){
        this.balance -= withdraw;
    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public abstract void pay();
    public abstract void receipt();
}

