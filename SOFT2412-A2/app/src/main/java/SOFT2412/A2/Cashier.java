package SOFT2412.A2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Cashier extends User{
    public Cashier(String name, String username, String password) {
        super(name, username, password);
    }

    // public String getTransactionSummary(){
    //     String allTransactions="";
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    //     allTransactions += "--------------------------------------------------------------------\n";
    //     allTransactions += "|  Snack Name  |  Paid  | Change | Payment Method |       Time      \n";
    //     allTransactions += "--------------------------------------------------------------------\n";
    //     for (List<Transaction> tList: Transaction.userTransactions.values()){
    //         for (Transaction t: tList) {

    //             allTransactions += ("|" + UserInterface.foodDetailString(14, t.getItemSold().getName()) +  UserInterface.foodDetailString(8, "$" + String.valueOf(t.getPaid())) + UserInterface.foodDetailString(8, "$" + String.valueOf(t.getChange()))
    //                     + UserInterface.foodDetailString(16, t.getPaymentMethod()) + UserInterface.foodDetailString(16, t.getTimeSold().format(formatter)) + "\n");
    //         }
    //     }

    //     for (Transaction t: Transaction.anonTransactions){
    //         allTransactions += ("|" + UserInterface.foodDetailString(14, t.getItemSold().getName()) +  UserInterface.foodDetailString(8, "$" + String.valueOf(t.getPaid())) + UserInterface.foodDetailString(8, "$" + String.valueOf(t.getChange()))
    //                 + UserInterface.foodDetailString(16, t.getPaymentMethod()) + UserInterface.foodDetailString(16, t.getTimeSold().format(formatter)) + "\n");
    //     }
    //     allTransactions += "--------------------------------------------------------------------";
    //     return allTransactions;
    // }

    // Edit the change and update cash.txt
    public void editChange(String cashAmount, int quantity) {
        UserInterface.vm.updateLine("./src/main/resources/cash.txt", cashAmount, Integer.toString(quantity), 1);
    }
}
