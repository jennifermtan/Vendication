package SOFT2412.A2;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Owner extends User{
    public Owner(String name, String username, String password) {
        super(name, username, password);
    }

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

    // Edit the change and update cash.txt
    public void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 0);
    }

    public String getUsernames() {
        String users = "";
        users += "-----------------------------------\n";
        users += "|      Username      |    Role    |\n";
        users += "-----------------------------------\n";
        try{
            File file = new File("./src/main/resources/users.txt");
            Scanner scan = new Scanner(file);
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts =  line.split(", ");
                String username = parts[2];
                String role = parts[0];
                users += ("|" + UserInterface.foodDetailString(20, username)) +  (UserInterface.foodDetailString(12, role) + "\n");
            }
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
        users +="-----------------------------------\n";
        return users;
    }
}
