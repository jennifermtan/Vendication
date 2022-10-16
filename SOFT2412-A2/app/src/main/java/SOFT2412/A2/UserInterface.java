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
                    // STILL NEED TO CHECK IF THE CASH IS GIVEN CORRECTLY,
                    // although this is already checked in the VendingMachine 'payByCash' method
                    break;
                }
                catch(NumberFormatException F){}
                catch(NoSuchFieldException nf){}
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
}
