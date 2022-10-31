package SOFT2412.A2;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class App {
    // Scanner to use for the whole application
    private static Scanner scan = new Scanner(System.in);
    private static UserInterface ui = new UserInterface();
    private static String userIdentifier = "";

    // Method takes user input as a single line
    public static void takeInput(String command, List<String> arguments) {
        // Some command examples
        switch (command) {
            case "buy":
                ui.buy(arguments);
                break;

            case "menu":
                menu();
                break;

            case "summary":
                if(arguments.size() == 1) {
                    if (arguments.get(0).equals("cancelled")) {
                        if(UserInterface.currentUser instanceof Owner)
                            System.out.println(Owner.getCancelledSummary());
                        else
                            System.out.println("Sorry, you do not have permission to perform this action.");
                    }
                    else if (arguments.get(0).equals("successful"))
                        if((UserInterface.currentUser instanceof Customer) || (UserInterface.currentUser == null))
                            System.out.println("Sorry, you do not have permission to perform this action.");
                        else
                            System.out.println(Cashier.getTransactionSummary());
                }
                else
                    System.out.println("Incorrect Format. For more help on the summary command, type \"help summary\".");
                break;

            case "addEmployee":
                if (UserInterface.currentUser instanceof Owner){
                    if(arguments.size() == 4)
                        Owner.addUser(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3));
                    else{System.out.println("Incorrect Format. For more help on the addEmployee command, type \"help addEmployee\".");}
                }
                else{
                    System.out.println("Sorry, you do not have permission to perform this action.");
                }
                break;

            case "removeEmployee":
                if (UserInterface.currentUser instanceof Owner){
                    try{
                        if (arguments.size() == 1){
                            Owner.removeUser(arguments.get(0));
                            System.out.println("Successfully removed!");
                        }
                        else{
                            System.out.println("Incorrect Format. For more help on the removeEmployee command, type \"help removeEmployee\".");
                        }
                    }
                    catch(NoSuchElementException ne){
                        System.out.println("Apologies, we could not find a user with that username.");
                    }
                    catch(IllegalStateException ie){
                        System.out.println("You just tried to remove yourself :o");
                    }
                }
                else{System.out.println("Sorry, you do not have permission to perform this action.");}
                break;

            case "signup":
                if(arguments.size() == 4)
                    User.signup(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3));
                else
                    System.out.println("Incorrect Format. For more help on the signup command, type \"help signup\".");
                break;

            case "login":
                if(arguments.size() == 2) {
                    User.login(arguments.get(0), arguments.get(1));
                    // if (UserInterface.currentUser != null) {
                    //     ui.loggedInPage();
                    // }
                }
                else
                    System.out.println("Incorrect Format. For more help on the login command, type \"help login\".");
                break;

            case "logout":
                User.logout();
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
        System.out.println("        _______  _        ______  _________ _______  _______ __________________ _________       \n" +
                "|\\     /|(  ____ \\( (    /|(  __  \\ \\__   __/(  ____ \\(  ___  )\\__   __/\\__   __/(  ___  )( (    /|\n" +
                "| )   ( || (    \\/|  \\  ( || (  \\  )   ) (   | (    \\/| (   ) |   ) (      ) (   | (   ) ||  \\  ( |\n" +
                "| |   | || (__    |   \\ | || |   ) |   | |   | |      | (___) |   | |      | |   | |   | ||   \\ | |\n" +
                "( (   ) )|  __)   | (\\ \\) || |   | |   | |   | |      |  ___  |   | |      | |   | |   | || (\\ \\) |\n" +
                " \\ \\_/ / | (      | | \\   || |   ) |   | |   | |      | (   ) |   | |      | |   | |   | || | \\   |\n" +
                "  \\   /  | (____/\\| )  \\  || (__/  )___) (___| (____/\\| )   ( |   | |   ___) (___| (___) || )  \\  |\n" +
                "   \\_/   (_______/|/    )_)(______/ \\_______/(_______/|/     \\|   )_(   \\_______/(_______)|/    )_)\n" +
                "                                                                                                   ");
        String command;
        ArrayList<String> arguments;
        menu();
        ui.anonymousPage();
        ui.help(new ArrayList<String>());

        while (true) {
            String input = null;
            try{
                input = readLine();
            }
            catch(InterruptedException ie){}

            // If they were timed out and input was never initialised, repeat the loop
            if (input == null) {
                System.out.println("Apologies, you have been timed out after 2 minutes of inactivity.");
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

        if (UserInterface.currentUser instanceof Cashier)
            userIdentifier = "Cashier";
        else if (UserInterface.currentUser instanceof Customer)
            userIdentifier = "Customer";
        else if (UserInterface.currentUser instanceof Owner)
            userIdentifier = "Owner";
        else if (UserInterface.currentUser instanceof Seller)
            userIdentifier = "Seller";
        else
            userIdentifier = "";
        if(!UserInterface.currentUser.getName().equals(""))
            System.out.printf("%s> ", userIdentifier + " " + UserInterface.currentUser.getName() + " ");
        else
            System.out.printf("Customer > ");

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
        if (UserInterface.currentUser == null) {
            ui.anonymousPage();
        }
        // else if (UserInterface.currentUser != null) {
        //     ui.loggedInPage();
        // }
    }

}
