import java.util.UUID;

public class Customer {
    private final String ID = UUID.randomUUID().toString();
    private final String name;
    private final String username;
    private String password;
    private int currency;

    private Customer(String name, String username, String password, int currency){
        this.name = name;
        this.username = username;
        this.password = password;
        this.currency = currency;
    }

    public String getName(){ return name;}
    public String getID(){ return ID;}
    public int getCurrency(){ return currency;}
    public void setCurrency(int value){ currency = value;}
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
    // Static methods

    public static void createUser(String name, String username, String password, int amount){
        Customer customer = new Customer(name, username, password, amount );
        Database.customerList.put(username, customer);
    }


}
