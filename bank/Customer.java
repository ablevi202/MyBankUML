package bank;

import java.util.Date;
import java.util.ArrayList;

public class Customer extends User {

    private String name; // Customer's full name
    private String customerID; // Unique customer ID for each customer
    private static int nextCustomerID = 0; // Stores the next customer ID to be used, so each one is unique.
    private Date dateOfBirth; // Customer's date of birth
    private String placeOfBirth; // Customer's place of birth
    private ArrayList<Account> accounts; // List of all the customer's accounts.

    // Constructors

    // Constructs customer without any accounts
        public Customer(String name, String username, Date dateOfBirth, String placeOfBirth) {
        super(username, "Customer");
        this.name = name;
        this.customerID = String.valueOf(nextCustomerID);
        nextCustomerID++;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
    }

        // Constructs customer with one account
        public Customer(String name, String username, Date dateOfBirth, String placeOfBirth, Account account) {
        super(username, "Customer");
        this.name = name;
        this.customerID = String.valueOf(nextCustomerID);
        nextCustomerID++;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.accounts.add(account);
    }

        // Constructs customer with multiple accounts
        public Customer(String name, String username, Date dateOfBirth, String placeOfBirth, ArrayList<Account> accounts) {
        super(username, "Customer");
        this.name = name;
        this.customerID = String.valueOf(nextCustomerID);
        nextCustomerID++;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        for(int i = 0; i < accounts.size(); i++){ // Adds all accounts to this object's account list
            this.accounts.add(accounts.get(i));
        }
    }

    // Accessors
    public String getName(){
        return name;
    }

    public String getCustomerID(){
        return customerID;
    }
    
    public Date getDateOfBirth(){
        return dateOfBirth;
    }

    public String getPlaceOfBirth(){
        return placeOfBirth;
    }

    // Mutators
    public void setUsername(string username){
        super.setUsername(username);
    }

    public void setName(String name){
        this.name = name;
    }


    // Display customers info
    public void printCustomerInfo() {
        System.out.println("Customer's info: " );
        System.out.println("name: "+ name);
        System.out.println("place of birth: "+ placeOfBirth);       
        System.out.println("date of birth: "+ df.format(dateOfBirth));
    }
}

