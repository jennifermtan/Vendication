
package SOFT2412.A2;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class App {

    // Method takes user input as a single line
    public void takeInput(String command, List<String> args) {
        Scanner scan = new Scanner(System.in);
        
    }


    public static void main(String[] args) throws IOException{

        Scanner scan = new Scanner(System.in);
        UserInterface ui = new UserInterface();
        ui.displaySnacks(scan, ui.vm.getInventory());
        User.loadUsers();
        ui.buy();
    }
}
