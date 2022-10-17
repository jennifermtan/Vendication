package SOFT2412.A2;

import java.util.*;
public class UserInterface {
    private Scanner scan = new Scanner(System.in);
    VendingMachine vm = new VendingMachine();
    public void buy(){
        System.out.println("Aren't you salivating at the mouth-watering image that this list of product options has conjured?");
        System.out.println("If you're paying with card today, just input your request in the form: \npaymentType quantity itemCode");
        System.out.println("\n For example, a purchase of 4 sprites with card would be: card 4 se\n");
        System.out.println("If you're paying with cash today, just input your request in the form: \npaymentType quantity itemCode $dollar*quantity centsc*quantity (and so on for the number of coins and notes you're inputting)");
        System.out.println("\n For example, a purchase of 4 sprites with cash would be: cash 4 se 50c*3 $5*3\n");

        String line = "";
        String[] input;
        while (true){
            line= scan.nextLine();
            input = line.split(" ");
            // Stop asking for info if their info is correct
            if (input.length > 2 && (input[0].equals("card") || input[0].equals("cash"))){
                try{
                    // Check that the second input is a quantity
                    Integer.valueOf(input[1]);

                    // Check that the third input is a viable item code
                    if (vm.searchByItemCode(input[2]) == null){
                        throw new NoSuchFieldException();
                    }
                    // checking if there is enough stock for the transaction (necessary?)
                    // if (!vm.checkStock(vm.searchByItemCode(input[2])), Integer.parseInt(input[1])) {
                    //     System.out.println("We're sorry, that item is in low stock, please try again.")
                    //     throw new NotEnoughStockException();
                    // }
                    // STILL NEED TO CHECK IF THE CASH IS GIVEN CORRECTLY,
                    // although this is already checked in the VendingMachine 'payByCash' method
                    break;
                }
                catch(NumberFormatException F){}
                catch(NoSuchFieldException nf){}
                // catch (NotEnoughStockException nS) {}
            }
            System.out.println("We apologise. Please check that was the correct format");
        }

        if (input[0].equals("cash")){
            String cashInput = "";
            for (int i = 3; i < input.length ; i++){
                cashInput += (input[i] + " ");
            }
            cashInput = cashInput.substring(0, cashInput.length() - 1);


            System.out.println(vm.payByCash(Integer.valueOf(input[1]), input[2], cashInput));
        }

        if (input[0].equals("card")) {
            String[] details;
            System.out.println("Please input your card details in the form:\nName Number\n\n For example: Max 40420");
            // check details against saved cards, prompts user again if fails
            while (true) {
                String cardInput = scan.nextLine();
                details = cardInput.split(" ");
                if (vm.checkCardDetails(details[0], details[1])) {
                    break;
                }
                System.out.println("We were unable to match your card, please try again.");
            }
            Food itemPurchased = vm.searchByItemCode(input[2]);
            int itemQuantity = Integer.parseInt(input[1]);
            vm.removeItem(itemPurchased, itemQuantity); // removing items from inventory (assume enough stock)
            System.out.printf("Thank you! Here are your items.\n User received %s %s(s)!\n", input[1], itemPurchased.getName());
            // if (user is logged in), option to save credit card details (!)
        }
    }

    public static void displaySnacks(Scanner scan, Map<Food, Integer> food) {
        String drinks = "";

        System.out.println("Snacks available:");
        System.out.print("Drinks: ");
        for (Food key : food.keySet()) {
            if (food.get(key) != 0) {
                // Drinks
                if (key.getCategory().equals("Drinks")) {
                    drinks += key.getName() + ", ";
                    System.out.printf(drinks);
                }
            }
        }
    }
}
