package SOFT2412.A2;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class VendingMachine {

    private List<Customer> customers = new ArrayList<Customer>();
    private List<Card> cards = new ArrayList<Card>();

    // A hashmap of the form: foodType: quantity in the vending machine
    private static Map<Food, Integer> inventory = new HashMap<>() {{
        // We need to add all the food items, which include 7 of each on startup
        put(new Food("Mineral Water", "Drinks", "mw", 1.5), 7);
        put(new Food("Sprite", "Drinks", "se", 2.5), 7);
        put(new Food("Coca Cola", "Drinks", "cc", 2.5), 7);
        put(new Food("Pepsi", "Drinks", "pi", 2.5), 7);
        put(new Food("Juice", "Drinks", "js", 2.5), 7);
        put(new Food("Mars", "Chocolates", "ms", 1.5), 7);
        put(new Food("M&M", "Chocolates", "mm", 1.7), 7);
        put(new Food("Bounty", "Chocolates", "by", 1.3), 7);
        put(new Food("Snickers", "Chocolates", "sn", 1.8), 7);
        put(new Food("Smiths", "Chips", "sm", 2.3), 7);
        put(new Food("Pringles", "Chips", "ps", 2.1), 7);
        put(new Food("Kettle", "Chips", "ke", 2.3), 7);
        put(new Food("Thins", "Chips", "ts", 2.3), 7);
        put(new Food("Mentos", "Candies", "mn", 1.3), 7);
        put(new Food("Sour Patch", "Candies", "sp", 1.95), 7);
        put(new Food("Skittles", "Candies", "sk", 1.8), 7);
    }};

    // A hashmap that records all the cash in the form cashType: quantity
    private static HashMap<String, Integer> cash = new HashMap<String, Integer>(){{
        // The vending machine starts out with 5 of each cash item
        put("$100", 5);
        put("$50", 5);
        put("$20", 5);
        put("$10", 5);
        put("$5", 5);
        put("$2", 5);
        put("$1", 5);
        put("50c", 5);
        put("20c", 5);
        put("10c", 5);
        put("5c", 5);

    }};

    // I'm following Frank's structure that user input will be of the form:
    // buyer cash 3 mw 50c*3 $5*3
    // i.e. userType paymentType quantity itemCode givenMoney
    // GivenMoney can have a variable length so it's simply all the inputs after the itemCode
    public static String payByCash(int quantity, String itemCode, String givenMoney){
        Food toBuy = searchByItemCode(itemCode);
        double toPay = toBuy.getCost() * quantity;
        String[] givenCash = givenMoney.split(" ");
        double paid = 0;

        // Go through every cash item and calculate how much money has been given
        for (String typeOfCash: givenCash){
            try {
                String[] thisCash = typeOfCash.split("\\*");

                if (!cash.keySet().contains(thisCash[0])) {
                    return "incorrect format";
                }
                // Check if the input type given is a dollar (starts with $)
                if (Character.toString(thisCash[0].charAt(0)).equals("$")) {
                    paid += (Double.parseDouble(thisCash[0].substring(1)) * Double.parseDouble(thisCash[1]));
                }
                // Otherwise it's cents
                else {
                    paid += ((Double.parseDouble(thisCash[0].substring(0, thisCash[0].length() - 1)) / 100) * Double.parseDouble(thisCash[1]));
                }
            }
            catch(NumberFormatException f){ return "incorrect format";}

        }

        // Check that they've given enough
        if (toPay > paid && Math.abs(toPay - paid) >= 0.00001){
            return "not enough paid";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal change = new BigDecimal(paid - toPay);

        // Calculate change breakdown
        Map<String, Integer> changeCash = new LinkedHashMap<String, Integer>();
        List<String> cashTypes = new ArrayList<>(){{
           add("$100");
           add("$50");
           add("$20");
           add("$10");
           add("$5");
           add("$2");
           add("$1");
           add("50c");
           add("20c");
           add("10c");
           add("5c");
        }};
        BigDecimal changeNum = change;
        // This is a disgusting line but basically it's giving change to the customer while there is still change to be given (change > 0)
        while (change.subtract(new BigDecimal(0.0001)).compareTo(new BigDecimal(0)) == 1){

            // Check every cash value to try to add a coin or note to the change
            for (String cashType: cashTypes){
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

                    if (!changeCash.containsKey(cashType)){
                        changeCash.put(cashType, 1);
                    }
                    else{
                        changeCash.put(cashType, changeCash.get(cashType) + 1);
                    }

                }
            }

        }
        String changeBreakdown = "";
        for (Map.Entry<String, Integer> payment: changeCash.entrySet()){
            changeBreakdown += (" (" + payment.getKey() + "*" + payment.getValue() + ")");
        }
        return "Transaction successful!\n" + "Paid: $" + df.format(paid) + "\nDue: $" + df.format(toPay) + "\nChange: $" + df.format(changeNum)
                + "\n\nChange Breakdown: \n" + changeBreakdown;

    }

    public static Food searchByItemCode(String itemCode){
        for (Food f: inventory.keySet()){
            if (f.getItemCode().equals(itemCode)){
                return f;
            }
        }
        return null;
    }

    public static BigDecimal round(BigDecimal value, BigDecimal increment,
                                   RoundingMode roundingMode) {
        if (increment.signum() == 0) {
            // 0 increment does not make much sense, but prevent division by 0
            return value;
        } else {
            BigDecimal divided = value.divide(increment, 0, roundingMode);
            BigDecimal result = divided.multiply(increment);
            return result;
        }
    }

}
