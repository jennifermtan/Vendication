package CC_04_Wed_16_Frank_Group._Assignment2;
import java.util.*;

public class UserInterface{

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