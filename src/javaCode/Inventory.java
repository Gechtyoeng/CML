package javaCode;

import java.sql.*;
import java.time.LocalDate;
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
    public void addItems(){
        String sql = "INSERT INTO inventory (item_name, quantity, price, expiry_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, this.itemName);
            stmt.setInt(2, this.quantity);
            stmt.setDouble(3, this.price);
            stmt.setDate(4, Date.valueOf(this.expiryDate));

            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("Item added successfully!");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Update items
    public static void updateItems(int newInventory_id, int newQuantity, double newPrice, LocalDate newExpiryDate){
        String sql = "UPDATE inventory SET quantity = ?, price = ?, expiry_date = ? WHERE inventory_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, newQuantity);
            stmt.setDouble(2, newPrice);
            stmt.setDate(3, Date.valueOf(newExpiryDate));
            stmt.setInt(4, newInventory_id);

            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("Item updated successfully!");
            }else{
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Remove items 
    public static void removeItem(int inventory_id){
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, inventory_id);
            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("Item removed successfully!");
            }else{
                System.out.println("Item not found.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Search for an item by ID
    public static void searchItemsByID(int inventory_id) {
        String sql = "SELECT * FROM inventory WHERE inventory_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, inventory_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("Item Found: " + rs.getString("item_name") + ", Quantity: " + rs.getInt("quantity") + ", Price: " + rs.getDouble("price") + ", Expiry Date: " + rs.getDate("expiry_date"));
            }else{
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Display all inventory items
    public void displayInventory(){
        String sql = "SELECT * FROM inventory";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("inventory_id") + ", Name: " + rs.getString("item_name") + rs.getInt("quantity") + ", Price: " + rs.getDouble("price") + ", Expiry Date: " + rs.getDate("expiry_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //checking the expiration date of the items
    public void expiryCheck(LocalDate expiryDate){
        LocalDate currTime = LocalDate.now();
        String sql = "SELECT item_name, expiry_date FROM inventory";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            

            while (rs.next()) {
                LocalDate expiry = rs.getDate("expiry_date").toLocalDate();
                if(expiry.isBefore(currTime)){
                    System.out.println("The item " + rs.getString("item_name") + " has expired!");
                }else if(expiry.isEqual(currTime)){
                    System.out.println("The item " + rs.getString("item_name") + " expires today!");
                }else{
                    System.out.println("The item " + rs.getString("item_name") + " is still valid.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //checking quantity in stock by ID
    public void quantityCheck(int inventory_id){
    String sql = "SELECT item_name, quantity FROM inventory WHERE inventory_id = ?";

    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
        
            stmt.setInt(1, inventory_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("Stock left for ID " + inventory_id + " (" + rs.getString("item_name") + "): " + rs.getInt(quantity));

            }else {
                System.out.println("Item with ID " + inventory_id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }
    //checking stock level
    public void stockLevel(){
        int lowStock = 5;
        String sql = "SELECT item_name, quantity FROM inventory";

        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){
            
        while (rs.next()) {
            int quantity = rs.getInt("quantity");
            String itemName = rs.getString("item_name");
            if (quantity <= lowStock) {
                System.out.println(itemName + ": Low stock (only " + quantity + " left)");
            }else{
                System.out.println(itemName + ": Sufficient stock (" + quantity + " left)");
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
