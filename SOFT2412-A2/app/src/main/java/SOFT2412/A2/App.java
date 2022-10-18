
package SOFT2412.A2;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class App {
    // Scanner to use for the whole application
    static Scanner scan = new Scanner(System.in);

    // Method takes user input as a single line
    public static void takeInput(UserInterface ui, String command, List<String> arguments) {
        // Some command examples
        switch (command) {
            case "buy":
                // System.out.println("buy successful");
                ui.buy(arguments);
                break;
            case "sell":
                break;
            case "signup":
                break;
            case "login":
                break;
            case "exit":
                System.out.println("Thank you for using our vending machine! Have a good day :)");
                System.exit(0);
        }
    }


    public static void main(String[] args) throws IOException{

        UserInterface ui = new UserInterface();
        ui.displaySnacks(scan, ui.vm.getInventory());

        // ui.buy();

        String command;
        ArrayList<String> arguments;
        while (true) {
            String input = scan.nextLine();
            String[] temp = input.split(" ");
            List<String> temp2 = Arrays.asList(temp);
            arguments = new ArrayList<String>(temp2);
            command = arguments.get(0);
            arguments.remove(0);
            takeInput(ui, command, arguments);
        }  
    }
}
