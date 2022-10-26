package SOFT2412.A2;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        User tempUser = null;
        try {
            Scanner usersFile = new Scanner(new File("./src/main/resources/users.txt"));
            while(usersFile.hasNextLine()) {
                String line = usersFile.nextLine();
                userInfo = line.split(", ");
                // if(!userLogins.containsKey(userInfo[1]))
                //     userLogins.put(userInfo[1], userInfo[2]);
                // else
                //     System.out.println("That username already exists.");
                userLogins.put(userInfo[2], userInfo[3]);
                // Checking which type of user it is
                if(userInfo[0].equals("cashier"))
                    tempUser = new Cashier(userInfo[1], userInfo[2], userInfo[3]);
                else if(userInfo[0].equals("customer"))
                    tempUser = new Customer(userInfo[1], userInfo[2], userInfo[3]);
                else if(userInfo[0].equals("owner"))
                    new Owner(userInfo[0], userInfo[1], userInfo[2]);
                else if(userInfo[0].equals("seller"))
                    new Seller(userInfo[0], userInfo[1], userInfo[2]);

                if (!userInfo[3].equals(""))
                    tempUser.card = Card.getCard(userInfo[4]);
                users.add(tempUser);
            }
            usersFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("loadUsers: File not found exception.");
        }
    }

    public static void login(String username, String password) {
        for(User u: users) {
            if (u.username.equals(username)) {
                if(u.password.equals(password)) {
                    UserInterface.currentUser = u;
                    System.out.println("Login Successful!");
                    return;
                }
                else {
                    System.out.println("Login Failed! Wrong password.");
                }
            }
        }
        System.out.println("Login Failed! Username does not exist.");
    }

    public static void signup(String type, String name, String username, String password) {
        // Error checking
        if(!type.equals("cashier") && !type.equals("customer") && !type.equals("owner") && !type.equals("seller")) {
            System.out.println("Incorrect Format. For more help on the signup command, type \"help signup\".");
            return;
        }
        // Checking if username already exists in the system
        for(User u: users) {
            if (u.username.equals(username)) {
                System.out.println("Sorry, that username already exists. Please try again.");
                return;
            }
        }
        // Signing up
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter("./src/main/resources/users.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            
            pw.printf("%s, %s, %s, %s, \n", type, name, username, password);
            pw.flush();
        } catch (IOException e) { System.out.println("signup: Error while writing to file."); }
        finally {
            try {
                pw.close();
                bw.close();
                fw.close();
            } catch (IOException io) { System.out.println("signup: Error while closing writers."); }

        }
        User tempUser = null;
        if(type.equals("cashier"))
            tempUser = new Cashier(name, username, password);
        else if(type.equals("customer"))
            tempUser = new Customer(name, username, password);
        else if(type.equals("owner"))
            tempUser = new Owner(name, username, password);
        else if(type.equals("seller"))
            tempUser = new Seller(name, username, password);
        users.add(tempUser);
        
        System.out.println("Account created successfully!");
        UserInterface.currentUser = tempUser;
    }

    public static void logout() {
        UserInterface.currentUser = null;
        System.out.println("Logged out successfully!");
    }
    
    // Method for finding a user by their name
    public static User getUserByName(String name){
        for (User u: users){
            if (u.getName().equals(name)){
                return u;
            }
        }
        return null;
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
    public Card getCard(){return card;}
}
