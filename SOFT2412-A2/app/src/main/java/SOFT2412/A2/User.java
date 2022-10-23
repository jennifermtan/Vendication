package SOFT2412.A2;
import java.util.*;
import java.io.*;
// import org.json.simple.*;
// import org.json.simple.parser.*;

public abstract class User {
    // Stores the current user and the type
    public static String currentUser = "";
    // HashMap to store the user's name, username, password and card number (optional)
    private static Map<String, String> userLogins = new HashMap<String, String>();
    // List to store all the users
    private static List<User> users = new ArrayList<User>();
    // Object attributes
    protected String name;
    protected String username;
    protected String password;
    protected Card card;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Untested and unused
    public static void loadUsers() {
        String[] userInfo;
        try {
            Scanner usersFile = new Scanner(new File("./src/main/resources/users.txt"));
            while(usersFile.hasNextLine()) {
                String line = usersFile.nextLine();
                userInfo = line.split(", ");
                if(!userLogins.containsKey(userInfo[1]))
                    userLogins.put(userInfo[1], userInfo[2]);
                else
                    System.out.println("That username already exists.");
            }
            usersFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("loadUsers: File not found exception.");
        }
    }


    // Allow admin users to edit the change and update cash.txt
    public void editChange(String cashAmount, int quantity) {
        VendingMachine.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 1);
    }

    // (!) add card to individual user
    public void addCard(Card card) {
        this.card = card;
    }

    public String getName(){return name;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
}
