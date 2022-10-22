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
    public static void takeInput(String command, List<String> arguments) {
        // Some command examples
        switch (command) {
            case "buy":
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
                System.out.println("\nYou have been sufficiently Vendicated! Have a good day :)");
                System.out.println("--------------------------------END OF PROGRAM--------------------------------");
                System.exit(0);
            default:
                System.out.printf("Command \"%s\" not found, please type \"help\" to view a list of commands and their usage.\n", command);
                break;
        }
    }

    public static void main(String[] args) {
        System.out.println("--------------------------------START OF PROGRAM--------------------------------");
        String command;
        ArrayList<String> arguments;
        menu();

        while (true) {
            String input = null;
            try{
                input = readLine();
            }
            catch(InterruptedException ie){}

            // If they were timed out and input was never initialised, repeat the loop
            if (input == null) {
                menu();
                continue;

            }
            String[] temp = input.split(" ");
            List<String> temp2 = Arrays.asList(temp);
            arguments = new ArrayList<String>(temp2);
            command = arguments.get(0);
            arguments.remove(0);
            takeInput(command, arguments);
        }
    }

    public static String readLine() throws InterruptedException {
        System.out.println("---------------------------------- NEXT INPUT -----------------------------------");
        ExecutorService ex = Executors.newSingleThreadExecutor();
        String input = null;
        System.out.printf("%s> ", User.currentUser);

        Future<String> result = ex.submit(new ConsoleInputReadTask());
        try {
            // Timeout after 120 seconds (2 mins)
            input = result.get(120, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.getCause().printStackTrace();
        } catch (TimeoutException e) {
            result.cancel(true);
        }
        finally {
            ex.shutdownNow();
        }
        return input;
    }

    // This is the 'entry point' to the program
    public static void menu(){
        ui.displaySnacks(scan, ui.vm.getInventory());
        System.out.println("\nTo be Vendicated, please read our help guidelines:");
        ui.help(new ArrayList<String>());
    }

}




