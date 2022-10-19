
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
                // System.out.println(arguments);
                ui.buy(arguments);
                break;
            case "sell":
                break;
            case "signup":
                break;
            case "login":
                break;
            case "help":
                ui.help(arguments);
                break;
            case "exit":
                System.out.println("\nThank you for using our vending machine! Have a good day :)");
                System.out.println("--------------------------------END OF PROGRAM--------------------------------");
                System.exit(0);
            default:
                System.out.printf("Command \"%s\" not found, please type \"help\" to view a list of commands and their usage.\n", command);
                break;
        }
    }


    public static void main(String[] args) throws IOException{
        System.out.println("--------------------------------START OF PROGRAM--------------------------------");
        UserInterface ui = new UserInterface();
        ui.displaySnacks(scan, ui.vm.getInventory());

        // ui.buy();
        String command;
        ArrayList<String> arguments;
        while (true) {
            System.out.println("\n--------------------------------NEXT INPUT--------------------------------");
            System.out.printf("%s> ", User.currentUser);
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
