package SOFT2412.A2;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
public class VendingMachine {

    private List<Customer> customers = new ArrayList<Customer>();
    private List<Card> cards = new ArrayList<Card>();

    // A hashmap of the form: foodType: quantity in the vending machine
    private HashMap<Food, Integer> inventory = new HashMap<Food, Integer>();

    // A hashmap that records all the cash in the form cashType: quantity
    private HashMap<String, Integer> cash = new LinkedHashMap<String, Integer>();

    // A JSONArray to store card details for reading and writing to JSON file
    private JSONArray cardArray;

    // private JSONParser = new JSONParser();
    private JSONArray cardArray;

    public VendingMachine(){
        // Load in the cash and the inventory from "inventory.txt" and "cash.txt" files using methods in case we need to reload them
        // load in the cash numbers from "cash.txt"
        loadCash();
        // Load in the card details from "creditCards.json"
        loadCard();
        // load in the inventory from "inventory.txt"
        loadInventory();
    }

    public void loadCash(){
        try{
            File cashFile = new File("./src/main/resources/cash.txt");
            Scanner scan1 = new Scanner(cashFile);
            while (scan1.hasNextLine()){
                String[] line = scan1.nextLine().split(", ");
                cash.put(line[0], Integer.valueOf(line[1]));
            }
        }
        catch(FileNotFoundException fe){}
    }

    public void loadCard() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("./src/main/resources/creditCards.json"));
            cardArray = (JSONArray) object;
            for (Object o : cardArray) {
                JSONObject entry = (JSONObject) o;
                String name = (String) entry.get("name");
                String number = (String) entry.get("number");
                Card card = new Card(name, number);
                cards.add(card);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadInventory(){
        try{
            File invenFile = new File("./src/main/resources/inventory.txt");
            Scanner scan2 = new Scanner(invenFile);
            while (scan2.hasNextLine()){
                String[] line = scan2.nextLine().split(", ");
                inventory.put(new Food(line[0], line[1], line[2], Double.parseDouble(line[3])), Integer.valueOf(line[4]));
            }
        }
        catch(FileNotFoundException fe){}
    }

    // I'm following Frank's structure that user input will be of the form:
    // buyer cash 3 mw 50c*3 $5*3
    // i.e. userType paymentType quantity itemCode givenMoney
    // GivenMoney can have a variable length so it's simply all the inputs after the itemCode
    public String payByCash(int quantity, String itemCode, String givenMoney){

        double toPay = calculateToPay(itemCode, quantity);
        String[] givenCash = givenMoney.split(" ");
        double paid = calculateGivenCash(givenCash);

        // Check that they've given enough
        if (toPay > paid && Math.abs(toPay - paid) >= 0.00001){
            throw new ArithmeticException();
        }

        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal change = new BigDecimal(paid - toPay);

        // Calculate change
        Map<String, Integer> changeCash = calculateChange(change);

        // Process final result summary
        String changeBreakdown = "";
        for (Map.Entry<String, Integer> payment: changeCash.entrySet()){
            changeBreakdown += (" (" + payment.getKey() + "*" + payment.getValue() + ")");
        }

        String resultString = "Transaction successful!\n" + "Paid: $" + df.format(paid) + "\nDue: $" + df.format(toPay) + "\nChange: $" + df.format(change);
        if (changeBreakdown != ""){
            resultString += ("\n\nChange Breakdown: \n" + changeBreakdown);
        }
        return resultString;


    }

    public Map<String, Integer> calculateChange(BigDecimal change){
        Map<String, Integer> changeCash = new LinkedHashMap<String, Integer>();
        int prevChange = -1;
        int currChange = 0;
        // This is a disgusting line but basically it's giving change to the customer while there is still change to be given (change > 0)
        while (change.subtract(new BigDecimal(0.0001)).compareTo(new BigDecimal(0)) == 1){
            // If we haven't added any new change this round, then we can't figure out a way to give them change so return
            if (prevChange == currChange){
                // if there was no possible change then we should return all the cash to the hashmap by reading in from cash.txt
                loadCash();
                throw new IllegalStateException();
            }
            prevChange = currChange;
            // Check every cash value to try to add a coin or note to the change
            for (String cashType: cash.keySet()){
                BigDecimal value;

                // Check how much this cash type is worth according to whether it's a dollar or cent
                if (Character.toString(cashType.charAt(0)).equals("$")){
                    value = new BigDecimal(cashType.substring(1));
                }
                else{
                    value = BigDecimal.valueOf(Double.parseDouble(cashType.substring(0, cashType.length() - 1)) / 100);
                }

                // round to the nearest 5 cents
                change = round(change, new BigDecimal("0.05"), RoundingMode.HALF_UP);
                // Try to add it to the list of change as many times as you can
                while (change.compareTo(value) >= 0 && cash.get(cashType) > 0){
                    change = change.subtract(value);
                    currChange++;
                    if (!changeCash.containsKey(cashType)){
                        changeCash.put(cashType, 1);
                    }
                    else{
                        changeCash.put(cashType, changeCash.get(cashType) + 1);
                    }
                    cash.put(cashType, cash.get(cashType) - 1);

                }
            }
        }

        return changeCash;
    }

    public double calculateToPay(String itemCode, int quantity){
        Food toBuy = searchByItemCode(itemCode);
        return toBuy.getCost() * quantity;
    }

    public double calculateGivenCash(String[] givenCash){
        double paid = 0;
        // Go through every cash item and calculate how much money has been given
        for (String typeOfCash: givenCash){
            String[] thisCash = typeOfCash.split("\\*");
            if (thisCash[0].equals("")){
                throw new ArithmeticException();
            }

            // Add the given cash into our list of cash
            cash.put(thisCash[0], cash.get(thisCash[0]) + Integer.parseInt(thisCash[1]));
            // Check if the input type given is a dollar (starts with $)
            if (Character.toString(thisCash[0].charAt(0)).equals("$")) {
                paid += (Double.parseDouble(thisCash[0].substring(1)) * Double.parseDouble(thisCash[1]));
            }
            // Otherwise it's cents
            else {
                paid += ((Double.parseDouble(thisCash[0].substring(0, thisCash[0].length() - 1)) / 100) * Double.parseDouble(thisCash[1]));
            }
        }
        return paid;
    }

    public Food searchByItemCode(String itemCode){
        for (Food f: inventory.keySet()){
            if (f.getItemCode().equals(itemCode)){
                return f;
            }
        }
        return null;
    }

    // This method rounds a BigDecimal to the given number of d.p
    public static BigDecimal round(BigDecimal value, BigDecimal increment, RoundingMode roundingMode) {
        if (increment.signum() == 0) {
            // 0 increment does not make much sense, but prevent division by 0
            return value;
        } else {
            BigDecimal divided = value.divide(increment, 0, roundingMode);
            BigDecimal result = divided.multiply(increment);
            return result;
        }
    }

    public boolean checkStock(Food item, int quantity) {
        if (inventory.get(item) < quantity) {
            return false;
        }
        return true;
    }

    public boolean checkCardDetails(String name, String number) {
        for (Card c : cards) {
            if ((name.equals(c.getName())) && (number.equals(c.getNumber()))) {
                return true;
            }
        }
        return false;
    }

    // Adds a card to saved card list and the json file
    @SuppressWarnings("unchecked")
    public void addCard(Card card) {
        System.out.println(cardArray);
        cards.add(card);
        JSONObject newCard = new JSONObject();
        newCard.put("name", card.getName());
        newCard.put("number", card.getNumber());
        cardArray.add(newCard);
        try (FileWriter file = new FileWriter("./src/main/resources/creditCards.json")) {
            file.write(cardArray.toJSONString());
            file.flush();
            file.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addItem(Food item, int quantity) {
        if (! inventory.containsKey(item) && quantity <= 15) {
            inventory.put(item, quantity);
        } else if (inventory.get(item) + quantity <= 15) {
            inventory.put(item, inventory.get(item) + quantity);
        } else {
            System.out.printf("Maximum quantity is 15: only %s items added", 15 - inventory.get(item));
            System.out.println();
        }
    }

    // Need to update inventory.txt as well? (!)
    public void removeItem(Food item, int quantity) {
        inventory.put(item, inventory.get(item) - quantity);
    }

    public HashMap<String, Integer> getCash() {
        return cash;
    }

    public List<Card> getCards() {
        return cards;
    }

    public HashMap<Food, Integer> getInventory() {
        return inventory;
    }


}
