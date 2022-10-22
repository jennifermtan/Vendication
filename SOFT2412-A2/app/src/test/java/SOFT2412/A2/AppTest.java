/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package SOFT2412.A2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class AppTest {
    VendingMachine vm = new VendingMachine();

    @Test
    public void testAddItem() {
        return;
    }

    @Test
    public void testPayByCard() {
        int quantity = 4;
        String itemCode = "se";
        vm.payByCard(quantity, itemCode);
    }

    @Test
    public void testCardInput() {

    }

    // Need to find a way to update the txt file to its original inventory after testing
    // @Test
    // public void testUpdateItem() {
    //     vm.updateItem("cc", 1);
    //     assertEquals(vm.getInventory().get(vm.searchByItemCode("cc")), 6);
    // }

    // This also tests updateCash(), updateItem(), updateTransactions(), and calculateChange() because the method is called in payByCash()
    @Test public void testPayByCash() {
        vm.defaultCashAndInventory();
        vm.payByCash(1, "cc", "$5*1 $2*1");
        System.out.println();
        assertEquals(6, vm.getCash().get("$5"));
        assertEquals( 4, vm.getCash().get("$2"));
        assertEquals(4, vm.getCash().get("50c"));
        vm.updateTransactions("cc", -1);
        vm.defaultCashAndInventory();
    }

    @Test void testUser(){
        UserInterface ui = new UserInterface();
        Map<String, String> holder = ui.allCommandBriefs;
        holder = ui.allCommandUsage;

        User md = new Customer("Md", "Emmder", "password124");
        // Check that we've saved this card for the user and for the overall card JSON array
        Card coolCard = new Card("Md", "123456");
        md.addCard(coolCard);
        assertEquals(md.getCard(), coolCard);
        // (!) this was commented out, now have separate methods to add card to cards list, and attach card to specific user
        // assertTrue(Card.getCards().contains(coolCard));
        assertTrue(Card.checkCardDetails("Md", "123456"));
    }

    @Test public void testVendingMachineValidation(){

        // Test that we don't let user buy something if they haven't paid enough for it
        vm.defaultCashAndInventory();
        try{
            vm.payByCash(1, "mm", "");
            vm.payByCash(1, "mm", "$1*1");
            fail("allows us to buy if they haven't paid");
        }
        catch(ArithmeticException ae){
            // Check that we haven't taken their money
            assertEquals(vm.getCash().get("$1"), 5);
            // Check that we have not given them the item
            assertEquals(vm.getInventory().get(vm.searchByItemCode("mm")), 7);
        }

        vm.defaultCashAndInventory();
        // Test that we don't let user buy something if it's over our stock
        try{
            vm.payByCash(8, "cc", "$100*2");
            fail("Let user buy amount over the stock that we have.");
        }
        catch(NoSuchElementException ne){
            // Check that we didn't take their money
            assertEquals(vm.getCash().get("$100"), 5);
            // Check that we didn't lose stock
            assertEquals(7, vm.getInventory().get(vm.searchByItemCode("cc")));
        }
        vm.defaultCashAndInventory();

        // Test that we don't let user buy something if we don't have enough change by first removing all our change
        vm.updateLine("./src/main/resources/cash.txt", "5c", "0", 1);
        vm.updateLine("./src/main/resources/cash.txt", "10c", "0", 1);
        vm.updateLine("./src/main/resources/cash.txt", "20c", "0", 1);
        vm.updateLine("./src/main/resources/cash.txt", "50c", "0", 1);
        vm.updateLine("./src/main/resources/cash.txt", "$1", "0", 1);

        UserInterface ui = new UserInterface();
        List<String> input = new ArrayList<String>(Arrays.asList("cash", "3", "sp", "$50*1"));
        ui.buy(input);
        // Check that we didn't take their money
        assertEquals(vm.getCash().get("$50"), 5);
        // Check that we didn't lose stock
        assertEquals(vm.getInventory().get(vm.searchByItemCode("sp")), 7);

        vm.defaultCashAndInventory();
    }



}
