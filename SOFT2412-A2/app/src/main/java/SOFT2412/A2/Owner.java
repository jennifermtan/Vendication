package SOFT2412.A2;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Owner extends User{
    public Owner(String name, String username, String password) {
        super(name, username, password);
    }

    public void editItemDetail(String detailType, String oldDetail, String newDetail) {
        switch (detailType) {
            case "name":
                UserInterface.vm.updateLine("./src/main/resources/inventory.txt", oldDetail, newDetail, 0);
                for (Food food : UserInterface.vm.getInventory().keySet()) {
                    if (food.getName().equals(oldDetail)) {
                        food.setName(newDetail);
                    }
                }
                break;

            case "code":
                UserInterface.vm.updateLine("./src/main/resources/inventory.txt", oldDetail, newDetail, 2);
                for (Food food : UserInterface.vm.getInventory().keySet()) {
                    if (food.getItemCode().equals(oldDetail)) {
                        food.setItemCode(newDetail);
                    }
                }
                break;

            case "category":
                UserInterface.vm.updateLine("./src/main/resources/inventory.txt", oldDetail, newDetail, 1);
                for (Food food : UserInterface.vm.getInventory().keySet()) {
                    if (food.getItemCode().equals(oldDetail)) {
                        food.setCategory(newDetail);
                    }
                }
                break;

            // case "price":
            //     for (Food food : UserInterface.vm.getInventory().keySet()) {
            //         if (food.getItemCode().equals(oldDetail)) {
            //             food.setCost(newDetail);
            //         }
            //     }
            //     UserInterface.vm.updateLine("./src/main/resources/inventory.txt", oldDetail, Double.toString(newDetail), 3);
            //     break;
            //
            // case "quantity":
            //     if (newQuantity > 15) {
            //         System.out.println("Error: Maximum quantity is 15.");
            //     } else if (newQuantity < 0) {
            //         System.out.println("Error: Please enter a valid quantity.");
            //     } else {
            //         System.out.println("Help");
            //         UserInterface.vm.updateLine("./src/main/resources/inventory.txt", oldDetail, Integer.toString(newDetail), 4);
            //         UserInterface.vm.loadInventory();
            //     }
            //     break;
        }
    }

    //Edit item name
    // public void editItemName(String name, String newName) {
    //     UserInterface.vm.updateLine("./src/main/resources/inventory.txt", name, newName, 0);
    //     for (Food food : UserInterface.vm.getInventory().keySet()) {
    //         if (food.getName().equals(name)) {
    //             food.setName(newName);
    //         }
    //     }
    // }

    //Edit item code
    // public void editItemCode(String code, String newCode) {
    //     UserInterface.vm.updateLine("./src/main/resources/inventory.txt", code, newCode, 2);
    //     for (Food food : UserInterface.vm.getInventory().keySet()) {
    //         if (food.getItemCode().equals(code)) {
    //             food.setItemCode(newCode);
    //         }
    //     }
    // }

    //Edit item category
    // public void editItemCategory(String itemCode, String newCategory) {
    //     UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, newCategory, 1);
    //     for (Food food : UserInterface.vm.getInventory().keySet()) {
    //         if (food.getItemCode().equals(itemCode)) {
    //             food.setCategory(newCategory);
    //         }
    //     }
    // }

    //Edit item price
    // public void editItemPrice(String itemCode, double newPrice) {
    //     for (Food food : UserInterface.vm.getInventory().keySet()) {
    //         if (food.getItemCode().equals(itemCode)) {
    //             food.setCost(newPrice);
    //         }
    //     }
    //     UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, Double.toString(newPrice), 3);
    // }

    //Edit item quantity
    // public void editItemQuantity(String itemCode, int newQuantity) {
    //     if (newQuantity > 15) {
    //         System.out.println("Error: Maximum quantity is 15.");
    //     } else if (newQuantity < 0) {
    //         System.out.println("Error: Please enter a valid quantity.");
    //     } else {
    //         System.out.println("Help");
    //         UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, Integer.toString(newQuantity), 4);
    //         UserInterface.vm.loadInventory();
    //     }
    // }

    // Power of owner
    public static String getCancelledSummary(){
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

    // Owner can also add a user for them
    public static void addUser(String type, String name, String username, String password){
        if (type.equals("seller") || type.equals("cashier")){
            User.signup(type, name, username, password);}
        else if (type.equals("customer")){
            System.out.println("I think we can let the customers sign themselves up");
        }
        else if (type.equals("owner")){
            System.out.println("I don't think you want to assign another owner who can usurp you...");
        }
    }
    // Let the owner remove any user they like
    public static void removeUser(String username){
        // Don't allow the owner to remove themselves
        if (username.equals("generic")){
            throw new IllegalStateException();
        }

        List<User> allUsers = User.getUsers();
        User toRemove = User.getUserByName(username);
        for (User u: allUsers){
            if (u.getUsername().equals(username)){
                toRemove = u;}
        }

        // Check if this user even exists
        if (toRemove == null){throw new NoSuchElementException();}

        // Remove from list of users
        allUsers.remove(toRemove);

        File currUsers = new File("./src/main/resources/users.txt");
        File newUsers = new File("./src/main/resources/tempUsers.txt");
        //Remove this user from users.txt by making a new file with all of the users except the user we want to delete
        try{
            FileWriter fw = new FileWriter("./src/main/resources/tempUsers.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("./src/main/resources/users.txt");
            BufferedReader br = new BufferedReader(fr);

            String currentLine;
            while ((currentLine = br.readLine()) != null){
                // Only write this line from users.txt into tempUsers.txt if it's not the user we want to delete
                String[] info = currentLine.split(", ");
                if (!info[2].equals(username)){
                    pw.println(currentLine);
                }
            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            currUsers.delete();
            File dump = new File("./src/main/resources/users.txt");
            newUsers.renameTo(dump);
        }
        catch(IOException fe){System.out.println(fe);}
    }

    // Edit the change and update cash.txt
    public static void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 1);
        UserInterface.vm.loadCash();
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
