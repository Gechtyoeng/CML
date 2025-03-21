package javaCode;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes.Name;

public class Medicine {
    private int medicineId;
    private String medicineName;
    private String dosage;
    private double price;
    private int stock;
    private LocalDate expiryDate;
    private String description;

    // Constructor
    public Medicine(int medicineId, String medicineName, String dosage, double price, int stock, LocalDate expiryDate, String description) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.description = description;
    }

    private static List<Medicine> medList = new ArrayList<>();

    // getter
    public int getMedicineID(){
        return medicineId;
    }
    public String getMedicineName(){
        return medicineName;
    }
    public String getDosage(){
        return dosage;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }
    public String getDescription() {
        return description;
    }

    //setter
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    public void setName(String medicineName) {
        this.medicineName = medicineName;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //method

    //Add medicine to DB
    public void addMedicine(){
        String sql = "INSERT INTO Medicine (medicine_name, dosage, price, stock, expiry_date, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, this.medicineName);
            stmt.setString(2, this.dosage);
            stmt.setDouble(3, this.price);
            stmt.setInt(4, this.stock);
            stmt.setDate(5, java.sql.Date.valueOf(this.expiryDate));
            stmt.setString(6, this.description);

            int rs = stmt.executeUpdate();
            System.out.println(rs + " medicine added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void removeMedicineByID(int medicineId){
        String sql = "DELETE FROM Medicine WHERE medicine_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, medicineId);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " medicine deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update medicine
    public void updateMedicine(){
        String sql = "UPDATE Medicine SET price = ?, stock = ?, expiry_date = ?, description = ? WHERE medicine_id = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setDouble(1, this.price);
            stmt.setInt(2, this.stock);
            stmt.setDate(3, java.sql.Date.valueOf(this.expiryDate));
            stmt.setString(4, this.description);
            stmt.setInt(5, this.medicineId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //display all medicine
    public void displayMedicine(){
        String sql = "SELECT * FROM Medicine";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("medicine_id") + ", Name: " + rs.getString("medicine_name") + ", Dosage: " + rs.getString("dosage") + ", Price: " + rs.getDouble("price") +", Stock: " + rs.getInt("stock") + ", Expiry date: " + rs.getDate("expiryDate") + ", Description: " + rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Check stock level
    public void medStockLevel(){
        int medLowStock = 5;
        String sql = "SELECT medicine_name, stock FROM Medicine";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                int stock = rs.getInt("stock");
                String medicine_name = rs.getString("medicine_name");
                if(stock <= medLowStock){
                    System.out.println(medicine_name + ": Low stock (only " + stock + " left)");
                }else{
                    System.out.println(medicine_name + ": Sufficient stock (" + stock + " left)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // search medicine by id
    public static void searchMedByID(int medicineId){
        String sql = "SELECT * FROM Medicine WHERE medicine_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("Medicine Found: " + rs.getString("medicine_name") + ", Dosage: " + rs.getString("dosage") + ", Price: " + rs.getDouble("price") +", Stock: " + rs.getInt("stock") + ", Expiry date: " + rs.getDate("expiryDate") + ", Description: " + rs.getString("description"));
            }else{
                System.out.println("Medicine not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //checking the expiration date of the medicine
    public void expiryCheck(LocalDate expiryDate){
        LocalDate currTime = LocalDate.now();
        String sql = "SELECT medicineName, expiryDate FROM Medicine";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            

            while (rs.next()) {
                LocalDate expiry = rs.getDate("expiryDate").toLocalDate();
                if(expiry.isBefore(currTime)){
                    System.out.println("The medicine " + rs.getString("medicineName") + " has expired!");
                }else if(expiry.isEqual(currTime)){
                    System.out.println("The medicine " + rs.getString("medicineName") + " expires today!");
                }else{
                    System.out.println("The medicine " + rs.getString("medicineName") + " is still valid.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //checking quantity in stock by ID
    public void quantityCheck(int medicineId){
        String sql = "SELECT medicineName, stock FROM Medicine WHERE medicineId = ?";
    
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
                stmt.setInt(1, medicineId);
                ResultSet rs = stmt.executeQuery();
    
                if(rs.next()){
                    System.out.println("Stock left for ID " + medicineId + " (" + rs.getString("medicineId") + "): " + rs.getInt(stock));
    
                }else {
                    System.out.println("Medicine with ID " + medicineId + " not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
    }

}
