package SOFT2412.A2;
import java.util.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import processing.data.JSONObject;
import processing.data.JSONArray;

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
        this.type = type;
    }

    // Save a card for the user
    public void saveCard(Card card) {
        this.card = card;
        // JSONParser jsonParser = new JSONParser();
        JSONObject jsonArray = loadJSONArray("./src/main/resources/creditCards.json");
        System.out.println(jsonArray);

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
