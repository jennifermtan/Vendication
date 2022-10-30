package SOFT2412.A2;
import java.util.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public abstract class User {

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

    public static void loadUsers() {
        String[] userInfo;
        User tempUser = null;
        try {
            Scanner usersFile = new Scanner(new File("./src/main/resources/users.txt"));
            usersFile.nextLine();
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
                    tempUser = new Owner(userInfo[1], userInfo[2], userInfo[3]);
                else if(userInfo[0].equals("seller"))
                    tempUser = new Seller(userInfo[1], userInfo[2], userInfo[3]);

                if (userInfo.length == 5) {
                    tempUser.card = Card.getCard(userInfo[4]);
                }
                users.add(tempUser);
            }
            usersFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("loadUsers: File not found exception.");
        }
    }

    public static void login(String username, String password) {
        loadUsers();
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
        loadUsers();
        // Error checking
        if(!type.equals("cashier") && !type.equals("customer") && !type.equals("owner") && !type.equals("seller")) {
            System.out.println("Incorrect Format. For more help on the signup command, type \"help signup\".");
            return;
        }
        // Restricting signup
        if(type.equals("owner")) {
            if(UserInterface.currentUser instanceof Owner)
                System.out.println("Sorry, there can only be one owner account.");
            else
                System.out.println("Sorry, you do not have permission to perform this action.");
            return;
        }
        else if(type.equals("seller") && !(UserInterface.currentUser instanceof Seller) && !(UserInterface.currentUser instanceof Owner)) {
            System.out.println("Sorry, you do not have permission to perform this action.");
            return;
        }
        else if(type.equals("cashier") && !(UserInterface.currentUser instanceof Cashier) && !(UserInterface.currentUser instanceof Owner)) {
            System.out.println("Sorry, you do not have permission to perform this action.");
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

    // (!) add card to individual user
    public void addCard(Card card) {
        this.card = card;
    }

    // Power of cashier and owner
    public String getTransactionSummary(){
        String allTransactions="";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        allTransactions += "--------------------------------------------------------------------\n";
        allTransactions += "|  Snack Name  |  Paid  | Change | Payment Method |       Time      |\n";
        allTransactions += "--------------------------------------------------------------------\n";
        for (List<Transaction> tList: Transaction.userTransactions.values()){
            for (Transaction t: tList) {

                allTransactions += ("|" + UserInterface.foodDetailString(14, t.getItemSold().getName()) +  UserInterface.foodDetailString(8, "$" + String.valueOf(t.getPaid())) + UserInterface.foodDetailString(8, "$" + String.valueOf(t.getChange()))
                        + UserInterface.foodDetailString(16, t.getPaymentMethod()) + UserInterface.foodDetailString(16, t.getTimeSold().format(formatter)) + "\n");
            }
        }

        for (Transaction t: Transaction.anonTransactions){
            allTransactions += ("|" + UserInterface.foodDetailString(14, t.getItemSold().getName()) +  UserInterface.foodDetailString(8, "$" + String.valueOf(t.getPaid())) + UserInterface.foodDetailString(8, "$" + String.valueOf(t.getChange()))
                    + UserInterface.foodDetailString(16, t.getPaymentMethod()) + UserInterface.foodDetailString(16, t.getTimeSold().format(formatter)) + "\n");
        }
        allTransactions += "--------------------------------------------------------------------";
        return allTransactions;
    }

    // Power of owner
    public String getCancelledSummary(){
        String cancelTransactions="";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        cancelTransactions += "------------------------------------------------------------------------------\n";
        cancelTransactions += "|    User    |      Time      |                    Reason                    |\n";
        cancelTransactions += "------------------------------------------------------------------------------\n";

        for (Transaction t: Transaction.cancelTransactions){
            cancelTransactions += ("|" + UserInterface.foodDetailString(12, t.getUserName()) +  UserInterface.foodDetailString(16, t.getTimeSold().format(formatter)) +
                    UserInterface.foodDetailString(46,  t.getState())  + "\n");
        }
        cancelTransactions += "------------------------------------------------------------------------------\n";
        return cancelTransactions;
    }

    public String getName(){return name;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public Card getCard(){return card;}
    public static List<User> getUsers(){return users;}
}
