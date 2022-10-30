package SOFT2412.A2;

import java.time.LocalDateTime;
import java.util.*;
import java.lang.NumberFormatException;
public class UserInterface {
    private Scanner scan = new Scanner(System.in);
    public static VendingMachine vm = new VendingMachine();
    // Current User (null if guest user)
    public static User currentUser = null;
    // HashMap of all valid commands and their brief description
    public static final Map<String, String> allCommandBriefs = new HashMap<String, String>() {{
        put("buy", "Allows any user to buy a product from the vending machine.");
        put("sell", "Allows a vending machine owner to sell a product.");
        put("signup", "Allows any user to create an account for the machine.");
        put("login", "Allows any user to login to their account in the machine.");
        put("logout", "Allows any user to log out of their account.");
        put("help", "Gives information on how to use the application.");
        put("menu", "Shows you everything you can buy in the vending machine.");
        put("exit", "Exits the application.");
    }};
    // HashMap of all valid commands and their usage
    public static final Map<String, String> allCommandUsage = new HashMap<String, String>() {{
        put("buy", "\nAllows any user to buy a product from the vending machine.\n" +
        "\nUsage: buy <payment method> <amount> <product code> [currency]\n" +
        "<payment method> -> card or cash\n<amount>         -> amount of the product\n" +
        "<product code>   -> code of the desired product\n[currency]       -> Currency denomination of given payment (Optional argument given only when paying by cash)\n" +
        "\nExample of usage: buy cash 4 se $5*2 50c*5\n");
        put("sell", "\nAllows a vending machine owner to sell a product.");
        put("signup", "\nAllows any user to create an account for the machine.\n" + 
        "\nUsage: signup <type> <name> <username> <password>\n" +
        "<type>     -> type of user (cashier, customer, owner, seller)\n" + 
        "<name>     -> name of the user\n" +
        "<username> -> username of the user, has to be unique\n" +
        "<password> -> password of the user\n" + 
        "\nExample of usage: signup customer John myusername mypassword\n");
        put("login", "\nAllows any user to login to their account in the machine.\n" +
        "\nUsage: login <username> <password>\n" +
        "<username> -> username registered upon signup\n" +
        "<password> -> password registered upon signup\n" +
        "\nExample of usage: login myusername mypassword\n");
        put("logout", "\nAllows any user to log out of their account.\n" +
        "\nUsage: logout\n");
        put("help", "\nIf you got this far, you already know how to use the help command!\n" +
        "\nUsage: help <command>\n" + "\nExample of usage: help buy\n");
        put("exit", "\nExits the application.\n" +
        "\nUsage: exit\n");
    }};


    public void buy(List<String> input){
        if (!validateInput(input)) {
            // Record the cancelled transaction
            Transaction t = new Transaction(User.currentUser, LocalDateTime.now(), "Cancelled due to incorrect user input");
            Transaction.writeTransaction(t);
            System.out.println("\nWe apologise. Please check that was the correct format. Type 'help buy' for help or 'exit' to quit the program.");
            return;
        }

        // Call user into a loop of buying items until they exit
        if (input.get(0).equals("cash")){
            String cashInput = "";
            // Find the string that includes the cash input
            for (int i = 3; i < input.size() ; i++){
                cashInput += (input.get(i) + " ");
            }
            if (cashInput.length() > 0) {
                cashInput = cashInput.substring(0, cashInput.length() - 1);
            }

            // Try to process their transaction
            try{
                String successful = vm.payByCash(Integer.valueOf(input.get(1)), input.get(2), cashInput);
                System.out.println(successful);

                String[] result = successful.split("\n");
                String paid = result[1].split(": \\$")[1];
                String change = result[3].split(": \\$")[1];
                // Record the successful transaction:
                Transaction t = new Transaction(User.currentUser, vm.searchByItemCode(input.get(2)), LocalDateTime.now(), Double.parseDouble(paid), Double.parseDouble(change), "cash", "Successful");
                Transaction.writeTransaction(t);

            }
            // If the customer has not given enough money
            catch(ArithmeticException ae){
                System.out.print("\nApologies, but that is not enough for your purchase. Please check your input.");
                double toPay = vm.calculateToPay(input.get(2), Integer.parseInt(input.get(1)));
                System.out.print(" You are to pay $" + String.format("%.2f",toPay) + ".");

                // Record the cancelled transaction
                Transaction t = new Transaction(User.currentUser, LocalDateTime.now(), "Cancelled due to insufficient payment");
                Transaction.writeTransaction(t);

                System.out.println("\nReinput your payment type, item code, quantity, and cash input in that order to continue payment. Otherwise input 'exit' to cancel your transaction.");
                return;
            }
            // If the machine can't give the right change
            catch(IllegalStateException is){
                System.out.println("\nSincere apologies. We do not have enough change to pay you back your change at this time. Please either reinput your payment or press 'exit' to cancel your transaction.");
                // Record the cancelled transaction
                Transaction t = new Transaction(User.currentUser, LocalDateTime.now(), "Cancelled due to insufficient change in vending machine");
                Transaction.writeTransaction(t);
                return;
            }
            // If the machine doesn't have enough stock for the purchase
            catch(NoSuchElementException ne){
                System.out.println("\nSincere apologies. We do not have enough stock to accommodate that purchase. Please either reinput your quantity or press 'exit' to quit the program.");
                // Record the cancelled transaction
                Transaction t = new Transaction(User.currentUser, LocalDateTime.now(), "Cancelled due to insufficient stock");
                Transaction.writeTransaction(t);
                return;
            }
        }

        if (input.get(0).equals("card")) {
            // Check that we have enough stock for the purchase
            if (!vm.checkStock(vm.searchByItemCode(input.get(2)), Integer.parseInt(input.get(1)))){
                // Record the cancelled transaction
                Transaction t = new Transaction(User.currentUser, LocalDateTime.now(), "Cancelled due to insufficient stock");
                System.out.println("\nSincere apologies. We do not have enough stock to accommodate that purchase. Please either reinput your quantity or press 'exit' to quit the program.");
                return;
            }
            String[] details;
            System.out.println("\nPlease input your card details in the form:\nName Number\n\nFor example: Max 40420");
            String name;
            String number;
            // check details against saved cards, prompts user again if fails
            while (true) {
                String cardInput = null;
                try{
                    cardInput = App.readLine();
                }
                catch(InterruptedException ie){}
                // Restart the app if this doesn't get an answer in 2 mins
                if (cardInput == null){
                    App.menu();
                    return;
                }
                details = cardInput.split(" ");
                // If they haven't given a viable input then keep asking
                if (details.length != 2 || !Card.checkCardDetails(details[0], details[1])){
                    System.out.println("\nWe were unable to match your card, please try again.");
                    continue;
                }
                name = details[0];
                number = details[1];
                break;
            }

            System.out.println(vm.payByCard(Integer.parseInt(input.get(1)), input.get(2)));

            // Record the successful transaction:
            Transaction t = new Transaction(User.currentUser, vm.searchByItemCode(input.get(2)), LocalDateTime.now(), vm.calculateToPay(input.get(2), Integer.parseInt(input.get(1))), 0.0, "Card", "Successful");
            Transaction.writeTransaction(t);

            // if (user is logged in), option to save credit card details (!)
            System.out.println("Would you like to save your card details to your account? Input 'yes' or 'no' to continue.");
            while (true) {
                String saveCard = null;
                try {
                    saveCard = App.readLine();
                }
                catch(InterruptedException ie) { }
                if (saveCard == null) {
                    App.menu();
                    return;
                }
                if (saveCard.equals("yes")) {
                    vm.saveCardDetails(new Card(name, number)); // (!) include User object to save to specific one
                    System.out.println("Card details were successfully saved to your account!");
                    break;
                }
                else if (saveCard.equals("no")) {
                    System.out.println("Card details were not saved to your account.");
                    break;
                }
                else {
                    System.out.println("\nWe were unable to process your request, please try again.");
                }
            }
        }

        System.out.println("\nEnjoy! If you'd like to buy anything else, please use the previous format (you can enter 'help buy' or 'help' for a refresher). Otherwise, press 'exit' to exit.");
    }

    public static void displaySnacks(Scanner scan, Map<Food, Integer> inventory) {
        System.out.println("----------------------------------------------------------");
        System.out.println("|  Snack Name  | Category | Item Code | Quantity |  Price |");
        System.out.println("----------------------------------------------------------");
        for (Map.Entry<Food, Integer> food : inventory.entrySet()) {
            // Don't display the item if we don't have any left of it
            if (food.getValue() == 0){continue;}
            Food item = food.getKey();

            System.out.println("|" + foodDetailString(14, item.getName()) + foodDetailString(10, item.getCategory()) + foodDetailString(11, item.getItemCode())
                    + foodDetailString(10, String.valueOf(food.getValue()))  + foodDetailString(8, "$" + item.getCost()));
        }
        System.out.println("----------------------------------------------------------");

    }

    public static String foodDetailString(int maxLength, String toDisplay){
        // Calculate how many spaces should be around the current output
        int n = maxLength - toDisplay.length();
        String repeated = "";
        if (n > 0){
            repeated = new String(new char[n/2]).replace("\0", " ");
            if (n % 2 == 0) {
                return repeated + toDisplay + repeated + "|";
            }
            String odd = new String(new char[n/2 + 1]).replace("\0", " ");
            return repeated + toDisplay + odd + "|";
        }
        return toDisplay + "|";
    }

    public boolean validateInput(List<String> input){

        // Stop asking for info if their info is correct
        if (input.size() <= 2 || (!input.get(0).equals("card") && !input.get(0).equals("cash"))){
            return false;
        }
        else {
            try{
                // Check that the second input is a quantity
                Integer.valueOf(input.get(1));

                // Check that the third input is a viable item code
                if (vm.searchByItemCode(input.get(2)) == null){
                    throw new NoSuchFieldException();
                }

                // Check that their given cash was in the correct format
                if (input.get(0).equals("cash")){
                    // check that each cash item was of the form $cash*int
                    for (int i = 3; i < input.size(); i++){
                        String[] cashGiven = input.get(i).split("\\*");
                        if (!vm.getCash().containsKey(cashGiven[0])) {
                            throw new NumberFormatException();
                        }
                        int numGiven = Integer.parseInt(cashGiven[1]);
                    }

                }

            }catch(NumberFormatException F) { return false; }
            catch(NoSuchFieldException nf) { return false; }
            catch(ArrayIndexOutOfBoundsException a) { return false; }

        }
        return true;
    }

    // Displays by default, before user chooses to log in
    public void anonymousPage() {
        if (Transaction.anonTransactions.size() < 5){return;}
        System.out.println("\nThese were the last 5 items bought by anonymous users:");
        List<Transaction> transactions = Transaction.anonTransactions;
        int index = 1;
        for (int initial = transactions.size() - 1; initial >= transactions.size() - 5; initial -= 1) {
            System.out.println(index + ") " + transactions.get(initial).getItemSold().getName());
            index++;
        }
    }

    // (!) Displays after user logs in
    // public void loggedInPage(User user) {
    //     System.out.println("\nThese were the last 5 items bought by you:");
    //     Map<User, List<Transaction>> users = Transaction.userTransactions;
    //     List<Transaction> transactions = users.get(user);
    //     int index = 1;
    //     for (int initial = transactions.size() - 1; initial >= transactions.size() - 5; initial -= 1) {
    //         System.out.println(index + ") " + transactions.get(initial).getItemSold().getName());
    //         index++;
    //     }
    // }

    // Help command
    public void help(List<String> arguments) {
        if(arguments.size() == 0) {
            System.out.println("Below is a list of all valid commands in the application. For more information on usage, type \"help <command>\".\n");
            for(String command : allCommandBriefs.keySet())
                System.out.printf("%6s:          %s%n", command, allCommandBriefs.get(command));
        }
        else {
            for(int i = 0; i < arguments.size(); i++) {
                for(String command : allCommandUsage.keySet()) {
                    if(command.equals(arguments.get(i)))
                        System.out.println(allCommandUsage.get(command));
                }
            }
        }

    }

}
