package SOFT2412.A2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Cashier extends User{
    public Cashier(String name, String username, String password) {
        super(name, username, password);
    }

    // Edit the change and update cash.txt
    public void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 1);
    }
}
