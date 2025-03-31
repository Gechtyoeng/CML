package javaCode;

import java.sql.*;
import java.time.LocalDate;
import database.Database;

public class Medicine {
    private int medicineId;
    private String medicineName;
    private String dosage;
    private double price;
    private String description;
    private LocalDate expiry_date;

    // Constructor
    public Medicine(int medicineId, String medicineName, String dosage, double price, String description,LocalDate expiry_date) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.dosage = dosage;
         this.price = price;
        this.description = description;
        this.expiry_date = expiry_date;
    }
    public Medicine( String medicineName, String dosage, double price, String description,LocalDate expiry_date) {
        this.medicineName = medicineName;
        this.dosage = dosage;
         this.price = price;
        this.description = description;
        this.expiry_date = expiry_date;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Medicine Details:\n" +
            "----------------------------\n" +
            "ID            : %d\n" +
            "Name          : %s\n" +
            "Dosage        : %s\n" +
            "Price         : $%.2f\n" +
            "Description   : %s\n" +
            "Expiry Date   : %s\n" +
            "----------------------------",
            medicineId, medicineName, dosage, price, description, expiry_date
        );
    }
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
    public String getDescription() {
        return description;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public LocalDate getExpiryDate(){
        return this.expiry_date;
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
    public void setExpiryDate(LocalDate  expiry_date){
        this.expiry_date = expiry_date;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //method

    //Add medicine to DB
    public void addMedicine(){
        String sql = "INSERT INTO Medicines (medicine_name, dosage, price, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){//auto increment id
            
            stmt.setString(1, this.medicineName);
            stmt.setString(2, this.dosage);
            stmt.setDouble(3, price);
            stmt.setString(4, this.description);
            int rs = stmt.executeUpdate();
            System.out.println(rs + " medicine added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //remove medicine by id
    public void removeMedicineByID(int medicineId){
        String sql = "DELETE FROM Medicines WHERE medicine_id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, medicineId);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " medicine deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update medicine base on id -> update medicine should update to inventory
    public void updateMedicine(int medicineId , double price, int stock, LocalDate expiryDate, String description){
        String sql = "UPDATE Medicines SET price = ?, stock = ?, expiry_date = ?, description = ? WHERE medicine_id = ?";
        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setDouble(1, price);
            stmt.setInt(2, stock);
            stmt.setDate(3, java.sql.Date.valueOf(expiryDate));
            stmt.setString(4, description);
            stmt.setInt(5, medicineId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //display all medicine
    public void displayMedicine(){
        String sql = "SELECT * FROM Medicines";

        try (Connection conn = Database.connect();
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

        try (Connection conn = Database.connect();
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

        try (Connection conn = Database.connect();
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

        try (Connection conn = Database.connect();
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
    
        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
                stmt.setInt(1, medicineId);
                ResultSet rs = stmt.executeQuery();
    
                if(rs.next()){
                    System.out.println("Stock left for ID " + medicineId + " (" + rs.getString("medicineId") + "): " );
    
                }else {
                    System.out.println("Medicine with ID " + medicineId + " not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
    }

}
