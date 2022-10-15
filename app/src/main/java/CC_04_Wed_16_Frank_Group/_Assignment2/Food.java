package CC_04_Wed_16_Frank_Group._Assignment2;

public class Food{
    private String name;
    private String code;
    private String category;
    private double price;

    public Food(String name, String code, String category, double price) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

}