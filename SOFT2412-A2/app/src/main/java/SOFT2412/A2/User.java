package SOFT2412.A2;
import java.util.*;
import java.io.*;

public abstract class User {
    private static Map<String, String> userLogins = new HashMap<String, String>();
    private static List<User> users = new ArrayList<User>();
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
}
