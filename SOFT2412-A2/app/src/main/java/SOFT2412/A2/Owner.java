package SOFT2412.A2;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

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

    // Let the owner remove any user they like
    public void removeUser(String username){
        List<User> allUsers = User.getUsers();
        User toRemove = null;
        for (User u: allUsers){
            if (u.getUsername().equals(name)){
                if (u.getClass().equals("cashier") || u.getClass().equals("seller")){
                    toRemove = u;
                }
                // Don't allow the owner to remove a customer user
                else{throw new IllegalStateException();}
            }
        }

        // Check if this user even exists
        if (toRemove == null){throw new NoSuchElementException();}

        // Remove from list of users
        allUsers.remove(toRemove);

        //Remove from users.txt

    }

    // Edit the change and update cash.txt
    public void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 0);
    }
}
