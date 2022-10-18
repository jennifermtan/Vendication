package SOFT2412.A2;

import java.util.*;
import java.lang.NumberFormatException;
public class UserInterface {
    private Scanner scan = new Scanner(System.in);
    VendingMachine vm = new VendingMachine();


    public void buy(List<String> input){
        System.out.println("\nAren't you salivating at the mouth-watering image that this list of product options has conjured?");
        System.out.println("If you're paying with CARD today, just input your request in the form: \npaymentType quantity itemCode");
        System.out.println("\nFor example, a purchase of 4 sprites with card would be: card 4 se\n");
        System.out.println("If you're paying with CASH today, just input your request in the form: \npaymentType quantity itemCode $dollar*quantity centsc*quantity (and so on for the number of coins and notes you're inputting)");
        System.out.println("\nFor example, a purchase of 4 sprites with cash would be: cash 4 se 50c*3 $5*3\n");

        if (!validateInput(input)) {
            System.out.println("We apologise. Please check that was the correct format. Type 'exit' to quit the program.");
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
                System.out.println("Enjoy! If you'd like to buy anything else, please use the previous format. Otherwise, press 'E' to exit.");
                
            }
            // If the customer has not given enough money
            catch(ArithmeticException ae){
                System.out.print("Apologies, but that is not enough for your purchase. Please check your input.");
                double toPay = vm.calculateToPay(input.get(2), Integer.parseInt(input.get(1)));
                System.out.print(" You are to pay $" + String.format("%.2f",toPay) + ".");

                System.out.println("\nReinput your payment type, item code, quantity, and cash input in that order to continue payment. Otherwise input 'E' to cancel your transaction.");
                return;
            }
            // If the machine can't give the right change
            catch(IllegalStateException is){
                System.out.println("Sincere apologies. We do not have enough change to pay you back your change at this time. Please either reinput your payment or press 'E' to cancel your transaction");
                return;
            }
        }

        if (input.get(0).equals("card")) {
            String[] details;
            System.out.println("Please input your card details in the form:\nName Number\n\nFor example: Max 40420");
            // check details against saved cards, prompts user again if fails
            while (true) {
                String cardInput = scan.nextLine();
                details = cardInput.split(" ");
                if (Card.checkCardDetails(details[0], details[1])) {
                    break;
                }
                System.out.println("We were unable to match your card, please try again.");
            }
            Food itemPurchased = vm.searchByItemCode(input.get(2));
            int itemQuantity = Integer.parseInt(input.get(1));
            vm.updateItem(input.get(2), itemQuantity); // removing items from inventory (assume enough stock)
            System.out.printf("Thank you! Here are your items.\n User received %s %s(s)!\n", input.get(1), itemPurchased.getName());
            // if (user is logged in), option to save credit card details (!)
        }
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

            }
            catch(NumberFormatException F) { return false; }
            catch(NoSuchFieldException nf) { return false; }
            catch(ArrayIndexOutOfBoundsException a) { return false; }
        }
        return true;
        // System.out.println("We apologise. Please check that was the correct format. Type 'exit' to quit the program.");
        
    }
}
