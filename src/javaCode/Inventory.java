package javaCode;

import java.time.LocalDate;
public class Inventory {

    private int inventoryId;     // Corresponds to inventory_id
    private int itemID;     // Corresponds to itemid
    private int stockQuantity;
    private String supplier;
    private LocalDate lastRestocked;   // Corresponds to expiry_date(yyyy-mm-dd)

     // Constructor
     public Inventory(int inventoryId, int itemID, int stockQuantity, String supplier, LocalDate lastRestocked) {
        this.inventoryId = inventoryId;
        this.itemID = itemID;
        this.stockQuantity = stockQuantity;
        this.supplier = supplier;
        this.lastRestocked = lastRestocked;
    }

    //private static List<Inventory> inventoryList = new ArrayList<>();

     // Getters and Setters
     public int getInventoryId() { return inventoryId; }
     public int getMedicineId() { return itemID; }
     public int getStockQuantity() { return stockQuantity; }
     public String getSupplier() { return supplier; }
     public LocalDate getLastRestocked() { return lastRestocked; }
 
     public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
     public void setLastRestocked(LocalDate lastRestocked) { this.lastRestocked = lastRestocked; }
 
     @Override
     public String toString() {
         return "Inventory ID: " + inventoryId + ", Item ID: " + itemID + 
                ", Stock: " + stockQuantity + ", Supplier: " + supplier +
                ", Last Restocked: " + lastRestocked;
     }
 }

