package SOFT2412.A2;
import java.util.*;

public class Card extends Payment{
    private name;
    private number;
    public static List<Card> cards = new ArrayList<Card>();
    public Card(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }
}
