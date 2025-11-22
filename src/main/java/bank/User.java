package bank;

public abstract class User{

    private String ID;
    private static int nextID = 0; // Used in class creation to make sure each ID is unique
    private String username;
    private String role; 

    // Constructor
    public User(String username, String role){
        this.ID = String.valueOf(nextID);
        nextID++;
        this.username = username;
        this.role = role;
    }

    // Accessors
    public String getID(){
        return ID;
    }

    public String getUsername(){
        return username;
    }

    public String getRole(){
        return role;
    }


    // Mutator
    public void setUsername(String username){
        this.username = username;
    }


}