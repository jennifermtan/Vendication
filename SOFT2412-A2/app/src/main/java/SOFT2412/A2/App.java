
package SOFT2412.A2;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.*;

public class App {
    // Scanner to use for the whole application
    private static Scanner scan = new Scanner(System.in);
    private static UserInterface ui = new UserInterface();

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

    public static void main(String[] args) {start();}


    // Let's use this method as the entry point of the app since we need to be able to restart here if the user times out
    public static void start(){
        System.out.println("--------------------------------START OF PROGRAM--------------------------------");
        String command;
        ArrayList<String> arguments;
        ui.displaySnacks(scan, ui.vm.getInventory());

        System.out.println("\nIf you're paying with CARD today, just input your request in the form: \nbuy paymentType quantity itemCode");
        System.out.println("\nFor example, a purchase of 4 sprites with card would be: buy card 4 se\n");
        System.out.println("If you're paying with CASH today, just input your request in the form: \nbuy paymentType quantity itemCode $dollar*quantity centsc*quantity (and so on for the number of coins and notes you're inputting)");
        System.out.println("\nFor example, a purchase of 4 sprites with cash would be: buy cash 4 se 50c*3 $5*3\n");
        while (true) {
            String input = timeOut();
            // If they were timed out, repeat the loop
            if (input.equals("never initialised")) {
                ui.displaySnacks(scan, ui.vm.getInventory());
                continue;
            }
            String[] temp = input.split(" ");
            List<String> temp2 = Arrays.asList(temp);
            arguments = new ArrayList<String>(temp2);
            command = arguments.get(0);
            arguments.remove(0);
            takeInput(ui, command, arguments);
        }
    }

    public static String timeOut(){
        System.out.println("\n--------------------------------NEXT INPUT--------------------------------");
        System.out.printf("%s> ", User.currentUser);
        Callable<String> k = () -> new Scanner(System.in).nextLine();
        Long start= System.currentTimeMillis();
        String choice="never initialised";
        ExecutorService l = Executors.newFixedThreadPool(1);  ;
        Future<String> g;

        g= l.submit(k);
        // Times out after 120 seconds
        while(System.currentTimeMillis()-start<120*1000 && !g.isDone()){}
        if(g.isDone()){
            try {
                choice=g.get();
            } catch (InterruptedException | ExecutionException e) {
                //e.printStackTrace();
            }
        }
        g.cancel(true);
        return choice;
    }

}

