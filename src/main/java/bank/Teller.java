package bank;

public class Teller extends User{

    private String employeeID; // Unique teller ID 
    private static int nextEmployeeID = 0;
    private String name; // Teller's full name

    // Constructor
    public Teller(String username, String name){
        super(username, "Teller");
        this.name = name;
        this.employeeID = String.valueOf(nextEmployeeID);
        nextEmployeeID++;
    }

    // Accessors
    public String getName(){
        return name;
    }

    public String getEmployeeID(){
        return employeeID;
    }

    // Mutator
    public void setName(String name){
        this.name = name;
    }
}