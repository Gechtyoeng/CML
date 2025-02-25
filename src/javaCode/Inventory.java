package javaCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory {

    private int inventoryId;     // Corresponds to inventory_id
    private String itemName;     // Corresponds to item_name
    private int quantity;        // Corresponds to quantity
    private double price;        // Corresponds to price
    private LocalDate expiryDate;   // Corresponds to expiry_date(yyyy-mm-dd)

     // Constructor
    public Inventory(int inventoryId, String itemName, int quantity, double price, LocalDate expiryDate) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    private static List<Inventory> inventoryList = new ArrayList<>();

     // Getters and Setters
     public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    //method

    //Add new items
    public void addItems(Inventory item){
        inventoryList.add(item);
        System.out.println("Item " + item.getItemName() + " added sucessfully!");

    }

    //Update items by ID(Modify quantity, price, or expiry date)
    public void updateItems(int inventoryId, int newQuantity, double newPrice, LocalDate newExpiryDate){
        for(Inventory item : inventoryList){
            if(item.getInventoryId() == inventoryId){
                item.setQuantity(newQuantity);
                item.setPrice(newPrice);
                item.setExpiryDate(newExpiryDate);
                System.out.println("Item updated sucessfully!");
                return;
            }
        }
        System.out.println("Item with ID " + inventoryId + " not found.");
    }

    //Remove items by ID
    public void removeItems(int inventoryId){
        Iterator <Inventory> iterator = inventoryList.iterator();

        while (iterator.hasNext()) {
            Inventory item = iterator.next();
            if(item.inventoryId == inventoryId){
                iterator.remove();
                System.out.println("Item " + item.getItemName() + " removed sucessfully!");
                return;
            }
        }
        System.out.println("Item with ID " + inventoryId + " not found.");
    }

    //Search for an item by name or ID
    public void searchItemsByID(int inventoryId){
        for(Inventory item : inventoryList){
            if(item.getInventoryId() == inventoryId){
                System.out.println("Item found: " + item.getItemName());
                return;
            }
        }
        System.out.println("Item with ID " + inventoryId + " not found.");
    }

    public void searchItemsByName(String itemName){
        for(Inventory item :inventoryList){
            if(item.getItemName().equalsIgnoreCase(itemName)){
                System.out.println("Item found: " + item.getItemName());
                return;
            }
        }
        System.out.println("Item with ID " + inventoryId + " not found.");
    }

    //Display all inventory items
    public void displayInventory(){
        if (inventoryList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (Inventory item : inventoryList) {
            System.out.println("ID: " + item.getInventoryId() + ", Name: " + item.getItemName() +
                    ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice() +
                    ", Expiry Date: " + item.getExpiryDate());
        }
    }

    //checking the expiration date of the items
    public void expiryCheck(LocalDate expiryDate){
        LocalDate currTime = LocalDate.now();
        System.out.println("Expiry Status of all items:");

        for(Inventory item : inventoryList){
        
            if(currTime.isBefore(item.getExpiryDate())){
                System.out.println("The item " + item.getItemName() + " is not expired yet!");
            }else if(currTime.isEqual(item.getExpiryDate())){
                System.out.println("The item " + item.getItemName() + " is expired today!");
            }else{
                System.out.println("The item " + item.getItemName() + " have already expired!");
            }
        }
    }

    //checking quantity in stock by ID
    public void quantityCheck(){
         System.out.println("Stock left for " + inventoryId + " (" + itemName + "): " + quantity);
    }

    public void quantityCheckByID(int inventoryId){
        for(Inventory item : inventoryList){
            if(item.getInventoryId() == inventoryId){
                item.quantityCheck();
                return;
            }
        }
        System.out.println("Item with ID " + inventoryId + " not found.");
    }

    public void quantityCheckByName(String itemName){
        for(Inventory item : inventoryList){
            if(item.getItemName() == itemName){
                item.quantityCheck();
                return;
            }
        }
        System.out.println("Item with Name " + itemName + " not found.");
    }

    //checking stock level
    public void stockLevel(){
        int lowStock = 5;

        for(Inventory item : inventoryList){
            if(item.getQuantity() <= lowStock){
                System.out.println(item.getItemName() + ": Low stock (only " + item.getQuantity() + " left)");
            }else{
                System.out.println(item.getItemName() + ": Sufficient Stock (" + item.getQuantity() + " left)");
            }
        }
    }
}
