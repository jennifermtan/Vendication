package CC_04_Wed_16_Frank_Group._Assignment2;
import java.util.*;

public class VendingMachine {
    private Map<String, Integer> cash;
    private List<Customer> customers;
    private List<Card> cards;
    private Map<Food, Integer> food;

    public VendingMachine() {
        this.cash = new HashMap<String, Integer>();
        this.customers = new ArrayList<>();
        this.cards = new ArrayList<Card>();
        this.food = new HashMap<>();
    }

    public void setInitialItems() {

        //Initial items in the vending machine
        //Drinks
        Food mw = new Food("Mineral Water", "mw", "Drinks", 2.99);
        Food spr = new Food("Sprite", "spr", "Drinks", 3.00);
        Food cc = new Food("Coca Cola", "cc", "Drinks", 3.00);
        Food pep = new Food ("Pepsi", "pep", "Drinks", 3.00);
        Food jui = new Food("Juice", "jui", "Drinks", 3.99);
    
        //Chocolates
        Food mars = new Food("Mars", "mars", "Chocolates", 2.50);
        Food mm = new Food("M&M", "mm", "Chocolates", 2.50);
        Food bou = new Food("Bounty", "bou", "Chocolates", 2.50);
        Food sni = new Food ("Snickers", "sni", "Chocolates", 2.50);
    
        //Chips
        Food smi = new Food("Smiths", "smi", "Chips", 5.50);
        Food pri = new Food("Pringles", "pri", "Chips", 5.50);
        Food ket = new Food("Kettle", "ket", "Chips", 5.50);
        Food thi = new Food("Thins", "thi", "Chips", 5.50);
    
        //Candies
        Food men = new Food("Mentos", "men", "Candies", 2.50);
        Food sp = new Food("Sour Patch", "sp", "Candies", 2.50);
        Food ski = new Food("Skittles", "ski", "Candies", 2.50);
        
        // Quantity is 7 at first run
        addItem(mw, 7);
        addItem(spr, 7);
        addItem(cc, 7);
        addItem(pep, 7);
        addItem(jui, 7);
        addItem(mars, 7);
        addItem(mm, 7);
        addItem(bou, 7);
        addItem(sni, 7);
        addItem(smi, 7);
        addItem(pri, 7);
        addItem(ket, 7);
        addItem(thi, 7);
        addItem(men, 7);
        addItem(sp, 7);
        addItem(ski, 7); 
    }

    // When adding an item make sure that there can only be max 15 items for each food!!
    public void addItem(Food item, int quantity) {
        if (! food.containsKey(item)) {
            food.put(item, quantity);
        } else {
            food.put(item, food.get(item) + quantity);
        }
    }

    public Map<String, Integer> getCash() {
        return cash;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Map<Food, Integer> getFood() {
        return food;
    }
    
}
