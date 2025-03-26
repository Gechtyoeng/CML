package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javaCode.Inventory;

public class InventoryDao {
    public static boolean addStock(int itemId, int quantity, String supplier, LocalDate lastRestocked) {
        String checkSql = "SELECT stock_quantity FROM inventorys WHERE item_id = ?";
        String updateSql = "UPDATE inventorys SET stock_quantity = stock_quantity + ?, last_restocked = ? WHERE item_id = ?";
        String insertSql = "INSERT INTO inventorys (item_id, stock_quantity, supplier, last_restocked) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = Database.connect()) {
            // Check if the medicine already exists in inventory
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, itemId);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    // Medicine exists, update stock
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, quantity);
                        updateStmt.setDate(2, Date.valueOf(lastRestocked));
                        updateStmt.setInt(3, itemId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Medicine does not exist, insert new record
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, itemId);
                        insertStmt.setInt(2, quantity);
                        insertStmt.setString(3, supplier);
                        insertStmt.setDate(4, Date.valueOf(lastRestocked));
                        insertStmt.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
//update stock base on item id
    public static void updateStock(int itemID, int newQuantity) {
        String sql = "UPDATE inventorys SET stock_quantity = ? WHERE item_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, itemID);

            pstmt.executeUpdate();
            System.out.println("Stock updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//remove item base on iventory id
    public static void removeItem(int item_id){
        String sql = "DELETE FROM inventorys WHERE item_id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, item_id);
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
//return all inventory 
    public static List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventorys";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                inventoryList.add(new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("item_id"),
                    rs.getInt("stock_quantity"),
                    rs.getString("supplier"),
                    rs.getDate("last_restocked").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
//search item base on item id
public static Inventory getInventoryByMedicineId(int medicineId) {
    String sql = "SELECT * FROM inventorys WHERE item_id = ?";
    Inventory inventory = null;

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, medicineId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            inventory = new Inventory(
                rs.getInt("inventory_id"),
                rs.getInt("item_id"),
                rs.getInt("stock_quantity"),
                rs.getString("supplier"),
                rs.getDate("last_restocked").toLocalDate()
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return inventory;
}
//check stock quantity base on item id
public static int getStockQuantity(int medicineId) {
    String sql = "SELECT stock_quantity FROM inventorys WHERE item_id = ?";
    int stockQuantity = 0;

    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, medicineId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            stockQuantity = rs.getInt("stock_quantity");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return stockQuantity;
}
public static boolean reduceStock(int medicineId, int quantity) {
    String checkSql = "SELECT stock_quantity FROM inventorys WHERE item_id = ?";
    String updateSql = "UPDATE inventorys SET stock_quantity = stock_quantity - ? WHERE item_id = ?";

    try (Connection conn = Database.connect()) {
        // Check if enough stock is available
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, medicineId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("stock_quantity");
                if (currentStock >= quantity) {
                    // Reduce stock
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, quantity);
                        updateStmt.setInt(2, medicineId);
                        updateStmt.executeUpdate();
                        return true;
                    }
                } else {
                    System.out.println("Not enough stock available for medicine ID: " + medicineId);
                    return false;
                }
            } else {
                System.out.println("Medicine not found in inventory.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


}
