package SOFT2412.A2;

import java.util.*;
import java.lang.NumberFormatException;
public class UserInterface {
    private Scanner scan = new Scanner(System.in);
    public VendingMachine vm = new VendingMachine();
    // Current User (null if guest user)
    public static User currentUser = null;
    // HashMap of all valid commands and their brief description
    public static final Map<String, String> allCommandBriefs = new HashMap<String, String>() {{
        put("buy", "Allows any user to buy a product from the vending machine.");
        put("sell", "Allows a vending machine owner to sell a product.");
        put("signup", "Allows any user to create an account for the machine.");
        put("login", "Allows any user to login to their account in the machine.");
        put("help", "Gives information on how to use the application.");
        put("exit", "Exits the application.");
    }};
    // HashMap of all valid commands and their usage
    public static final Map<String, String> allCommandUsage = new HashMap<String, String>() {{
        put("buy", "Allows any user to buy a product from the vending machine.\n" + 
        "Usage: buy <payment method> <amount> <product code> [currency]\n" + 
        "<payment method> -> card or cash\n<amount>         -> amount of the product\n" + 
        "<product code>   -> code of the desired product\n[currency]       -> Currency denomination of given payment (Optional argument given only when paying by cash)\n" +
        "\nExample of usage: buy cash 4 se $5*2 50c*5\n");
        put("sell", "Allows a vending machine owner to sell a product.");
        put("signup", "Allows any user to create an account for the machine.");
        put("login", "Allows any user to login to their account in the machine.");
        put("help", "Gives information on how to use the application.");
        put("exit", "Exits the application.");
    }};


    public void buy(List<String> input){

        if (!validateInput(input)) {
            System.out.println("\nWe apologise. Please check that was the correct format. Type 'help' for help or 'exit' to quit the program.");
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
                System.out.println(vm.payByCash(Integer.valueOf(input.get(1)), input.get(2), cashInput));
            }
            // If the customer has not given enough money
            catch(ArithmeticException ae){
                System.out.print("\nApologies, but that is not enough for your purchase. Please check your input.");
                double toPay = vm.calculateToPay(input.get(2), Integer.parseInt(input.get(1)));
                System.out.print(" You are to pay $" + String.format("%.2f",toPay) + ".");

                System.out.println("\nReinput your payment type, item code, quantity, and cash input in that order to continue payment. Otherwise input 'exit' to cancel your transaction.");
                return;
            }
            // If the machine can't give the right change
            catch(IllegalStateException is){
                System.out.println("\nSincere apologies. We do not have enough change to pay you back your change at this time. Please either reinput your payment or press 'exit' to cancel your transaction.");
                return;
            }
            // If the machine doesn't have enough stock for the purchase
            catch(NoSuchElementException ne){
                System.out.println("\nSincere apologies. We do not have enough stock to accommodate that purchase. Please either reinput your quantity or press 'exit' to quit the program.");
                return;
            }
        }

        if (input.get(0).equals("card")) {
            // Check that we have enough stock for the purchase
            if (!vm.checkStock(vm.searchByItemCode(input.get(2)), Integer.parseInt(input.get(1)))){
                System.out.println("\nSincere apologies. We do not have enough stock to accommodate that purchase. Please either reinput your quantity or press 'exit' to quit the program.");
                return;
            }
            String[] details;
            System.out.println("\nPlease input your card details in the form:\nName Number\n\nFor example: Max 40420");

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
                break;

            }
            Food itemPurchased = vm.searchByItemCode(input.get(2));
            int itemQuantity = Integer.parseInt(input.get(1));
            vm.updateItem(input.get(2), itemQuantity); // removing items from inventory (assume enough stock)
            System.out.printf("\nThank you! Here are your items.\n User received %s %s(s)!\n", input.get(1), itemPurchased.getName());
            // if (user is logged in), option to save credit card details (!)
        }
        System.out.println("\nEnjoy! If you'd like to buy anything else, please use the previous format (you can enter 'help buy' or 'help' for a refresher). Otherwise, press 'exit' to exit.");
    }

    public static void displaySnacks(Scanner scan, Map<Food, Integer> inventory) {
        List<Food> drinks = new ArrayList<>();
        List<Food> chocolates = new ArrayList<>();
        List<Food> chips = new ArrayList<>();
        List<Food> candies = new ArrayList<>();

        System.out.println("Snacks available:");
        for (Food key : inventory.keySet()) {
            if (inventory.get(key) != 0) {
                // Drinks
                if (key.getCategory().equals("Drinks")) {
                    drinks.add(key);
                } else if (key.getCategory().equals("Chocolates")) {
                    chocolates.add(key);
                } else if (key.getCategory().equals("Chips")) {
                    chips.add(key);
                } else if (key.getCategory().equals("Candies")) {
                    candies.add(key);
                }
            }
        }

        System.out.print("Drinks: ");
        for (int i = 0; i < drinks.size(); i++) {
            if (i != drinks.size() - 1) {
                System.out.printf("%s ($%.2f) (%s), ", drinks.get(i).getName(), drinks.get(i).getCost(), drinks.get(i).getItemCode());
            } else {
                System.out.printf("%s ($%.2f) (%s)", drinks.get(i).getName(), drinks.get(i).getCost(), drinks.get(i).getItemCode());
            }
        }
        System.out.println();

        System.out.print("Chocolates: ");
        for (int i = 0; i < chocolates.size(); i++) {
            if (i != chocolates.size() - 1) {
                System.out.printf("%s ($%.2f) (%s), ", chocolates.get(i).getName(), chocolates.get(i).getCost(), chocolates.get(i).getItemCode());
            } else {
                System.out.printf("%s ($%.2f) (%s)", chocolates.get(i).getName(), chocolates.get(i).getCost(), chocolates.get(i).getItemCode());
            }
        }
        System.out.println();

        System.out.print("Chips: ");
        for (int i = 0; i < chips.size(); i++) {
            if (i != chips.size() - 1) {
                System.out.printf("%s ($%.2f) (%s), ", chips.get(i).getName(), chips.get(i).getCost(), chips.get(i).getItemCode());
            } else {
                System.out.printf("%s ($%.2f) (%s)", chips.get(i).getName(), chips.get(i).getCost(), chips.get(i).getItemCode());
            }
        }
        System.out.println();

        System.out.print("Candies: ");
        for (int i = 0; i < candies.size(); i++) {
            if (i != candies.size() - 1) {
                System.out.printf("%s ($%.2f) (%s), ", candies.get(i).getName(), candies.get(i).getCost(), candies.get(i).getItemCode());
            } else {
                System.out.printf("%s ($%.2f) (%s)", candies.get(i).getName(), candies.get(i).getCost(), candies.get(i).getItemCode());
            }
        }
        System.out.println();
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
