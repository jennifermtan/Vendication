package SOFT2412.A2;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;

public class Owner extends User{
    public Owner(String name, String username, String password) {
        super(name, username, password);
    }

    // Let the owner remove any user they like
    public void removeUser(String username){
        List<User> allUsers = User.getUsers();
        User toRemove = null;
        for (User u: allUsers){
            if (u.getUsername().equals(username)){
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
    public void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 0);
    }
}
