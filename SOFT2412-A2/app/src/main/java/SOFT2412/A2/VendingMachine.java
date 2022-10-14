package SOFT2412.A2;
import java.util.*;

public class VendingMachine {

    private List<Customer> customers = new ArrayList<Customer>();
    private List<Card> cards = new ArrayList<Card>();

    // A hashmap of the form: foodType: quantity in the vending machine
    private static Map<Food, Integer> inventory = new HashMap<>() {{
        // We need to add all the food items, which include 7 of each on startup
        inventory.put(new Food("Mineral Water", "Drinks", "mw", 1.5), 7);
        inventory.put(new Food("Sprite", "Drinks", "se", 2.5), 7);
        inventory.put(new Food("Coca Cola", "Drinks", "cc", 2.5), 7);
        inventory.put(new Food("Pepsi", "Drinks", "pi", 2.5), 7);
        inventory.put(new Food("Juice", "Drinks", "js", 2.5), 7);
        inventory.put(new Food("Mars", "Chocolates", "ms", 1.5), 7);
        inventory.put(new Food("M&M", "Chocolates", "mm", 1.7), 7);
        inventory.put(new Food("Bounty", "Chocolates", "by", 1.3), 7);
        inventory.put(new Food("Snickers", "Chocolates", "sn", 1.8), 7);
        inventory.put(new Food("Smiths", "Chips", "sm", 2.3), 7);
        inventory.put(new Food("Pringles", "Chips", "ps", 2.1), 7);
        inventory.put(new Food("Kettle", "Chips", "ke", 2.3), 7);
        inventory.put(new Food("Thins", "Chips", "ts", 2.3), 7);
        inventory.put(new Food("Mentos", "Candies", "mn", 1.3), 7);
        inventory.put(new Food("Sour Patch", "Candies", "sp", 1.95), 7);
        inventory.put(new Food("Skittles", "Candies", "sk", 1.8), 7);
    }};

    // A hashmap that records all the cash in the form cashType: quantity
    private static HashMap<String, Integer> cash = new HashMap<String, Integer>(){{
        // The vending machine starts out with 5 of each cash item
        cash.put("$100", 5);
        cash.put("$50", 5);
        cash.put("$20", 5);
        cash.put("$10", 5);
        cash.put("$5", 5);
        cash.put("$2", 5);
        cash.put("$1", 5);
        cash.put("50c", 5);
        cash.put("20c", 5);
        cash.put("10c", 5);
        cash.put("5c", 5);

    }};

    // I'm following Frank's structure that user input will be of the form:
    // buyer cash 3 mw 50c*3 5*3
    // i.e. userType paymentType quantity itemCode givenMoney
    // GivenMoney can have a variable length so it's simply all the inputs after the itemCode
    public void payByCash(int quantity, String itemCode, String givenMoney){


    }

    public Food searchByItemCode(String itemCode){
        for (Food f: inventory.keySet()){
            if (f.getItemCode().equals(itemCode)){
                return f;
            }
        }
        return null;
    }

}
