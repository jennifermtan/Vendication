
package SOFT2412.A2;
import java.util.*;
public class App {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        UserInterface ui = new UserInterface();
        ui.displaySnacks(scan, ui.vm.getInventory());
        User.loadUsers();
    }
}
