package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javaCode.Medicine;

public class MedicineDao {
    // Add medicine to DB
    public static int addMedicine(Medicine medicine) {
        String sql = "INSERT INTO Medicines (medicine_name, dosage, price, description, expiry_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, medicine.getMedicineName());
            stmt.setString(2, medicine.getDosage());
            stmt.setDouble(3, medicine.getPrice());
            stmt.setString(4, medicine.getDescription());
            stmt.setDate(5, java.sql.Date.valueOf(medicine.getExpiryDate()));

            int affectedRows = stmt.executeUpdate();
           if (affectedRows > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                medicine.setMedicineId(generatedId);
                return generatedId;
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Remove medicine by ID
    public static void removeMedicineById(int medicineId) {
        String sql = "DELETE FROM Medicines WHERE medicine_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println(rowsDeleted + " medicine deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update medicine by ID
    public static void updateMedicine(int medicineId, double price, LocalDate expiryDate, String description) {
        String sql = "UPDATE Medicines SET price = ?, expiry_date = ?, description = ? WHERE medicine_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, price);
            stmt.setDate(2, java.sql.Date.valueOf(expiryDate));
            stmt.setString(3, description);
            stmt.setInt(4, medicineId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated + " medicine updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all medicines
    public static List<Medicine> getAllMedicines() {
        List<Medicine> medicineList = new ArrayList<>();
        String sql = "SELECT * FROM Medicines";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("dosage"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getDate("expiry_date").toLocalDate()
                );
                medicineList.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicineList;
    }
    //get expired medicine
    public static List<Medicine> getExpiredMedicines() {
        List<Medicine> expiredMedicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE expiry_date < CURRENT_DATE";
    
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Medicine medicine = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("dosage"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getDate("expiry_date").toLocalDate()
                );
                expiredMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return expiredMedicines;
    }
    

    // Search medicine by ID
    public static Medicine searchMedicineById(int medicineId) {
        String sql = "SELECT * FROM Medicines WHERE medicine_id = ?";
        Medicine medicine = null;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("dosage"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getDate("expiry_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicine;
    }

    // Check stock level
    public void checkStockLevel(int threshold) {
        String sql = "SELECT medicine_name, stock FROM Medicines";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int stock = rs.getInt("stock");
                String medicineName = rs.getString("medicine_name");
                if (stock <= threshold) {
                    System.out.println(medicineName + ": Low stock (only " + stock + " left)");
                } else {
                    System.out.println(medicineName + ": Sufficient stock (" + stock + " left)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check expiry status
    public static void checkExpiry() {
        LocalDate today = LocalDate.now();
        String sql = "SELECT medicine_name, expiry_date FROM Medicines";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDate expiryDate = rs.getDate("expiry_date").toLocalDate();
                String medicineName = rs.getString("medicine_name");

                if (expiryDate.isBefore(today)) {
                    System.out.println(medicineName + " has expired!");
                } else if (expiryDate.isEqual(today)) {
                    System.out.println(medicineName + " expires today!");
                } else {
                    System.out.println(medicineName + " is still valid.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check stock by medicine ID
    public void checkStockById(int medicineId) {
        String sql = "SELECT medicine_name, stock FROM Medicines WHERE medicine_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Stock for " + rs.getString("medicine_name") + ": " + rs.getInt("stock") + " left.");
            } else {
                System.out.println("Medicine with ID " + medicineId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
