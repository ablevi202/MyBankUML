package bank;

import java.time.Instant;

public class Transaction {

    // TODO IMPLEMENT TxStatus, it is currently unhandled since risk verificiation code is unwritten

    private String transactionID; // Unique ID for all transactions
    private static int nextTransactionID = 0;
    private String TxStatus; // Status of the transaction, either PENDING_REVIEW, APPROVED, CANCELLED, or COMPLETED
    private float ammount; // The amount of money being transferred
    private Instant createdAt; // The instant that the transaction was created
    private String fromAccount; // The ID of the account that the money is coming from
    private String toAccount; // The ID of the account that the money is going to


    // Constructors
    public Transaction(float amount, String fromAccount, String toAccount){
        transactionID = String.valueOf(nextTransactionID);
        nextTransactionID++;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        createdAt = Instant.now(); // Sets the instant to the moment the transaction was created

    }

    // Accessors
    public String getTransactionID(){
        return transactionID;
    }

    public String getFromAccount(){
        return fromAccount;
    }

    public String getToAccount(){
        return toAccount;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

    public String getTxStatus(){
        return TxStatus;
    }

    public float getAmount(){
        return amount;
    }


    // Mutator
    public void setTxStatus(string TxStatus){
        this.TxStatus = TxStatus;
    }



    public void pay() {
        System.out.println("Payment transaction is done.");
    }

    public void receipt() {
        System.out.println("Transaction receipt.");
    }
}
