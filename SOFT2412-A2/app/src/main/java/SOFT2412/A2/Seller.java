package SOFT2412.A2;

public class Seller extends User{

    public Seller(String name, String username, String password) {
        super(name, username, password);
    }

    //Edit item name
    public void editItemName(String name, String newName) {
        UserInterface.vm.updateLine("./src/main/resources/inventory.txt", name, newName, 0);
        for (Food food : UserInterface.vm.getInventory().keySet()) {
            if (food.getName().equals(name)) {
                food.setName(newName);
            }
        }
    }

    //Edit item code
    public void editItemCode(String code, String newCode) {
        UserInterface.vm.updateLine("./src/main/resources/inventory.txt", code, newCode, 2);
        for (Food food : UserInterface.vm.getInventory().keySet()) {
            if (food.getItemCode().equals(code)) {
                food.setItemCode(newCode);
            }
        }
    }

    //Edit item category
    public void editItemCategory(String itemCode, String newCategory) {
        UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, newCategory, 1);
        for (Food food : UserInterface.vm.getInventory().keySet()) {
            if (food.getItemCode().equals(itemCode)) {
                food.setCategory(newCategory);
            }
        }
    }

    //Edit item price
    public void editItemPrice(String itemCode, double newPrice) {
        for (Food food : UserInterface.vm.getInventory().keySet()) {
            if (food.getItemCode().equals(itemCode)) {
                food.setCost(newPrice);
            }
        }

        UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, Double.toString(newPrice), 3);
    }

    //Edit item quantity
    public void editItemQuantity(String itemCode, int newQuantity) {
        if (newQuantity > 15) {
            System.out.println("Error: Maximum quantity is 15.");
        } else {
            UserInterface.vm.updateLine("./src/main/resources/inventory.txt", itemCode, Integer.toString(newQuantity), 4);
            Food food = UserInterface.vm.searchByItemCode(itemCode);
            UserInterface.vm.addItem(food, 15 - UserInterface.vm.getInventory().get(food));
        }
    }
}
