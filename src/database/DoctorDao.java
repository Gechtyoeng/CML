package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javaCode.Doctor;

public class DoctorDao {
     // Fetch doctor by ID
     public static Doctor getDoctorById(int doctorId) {
        String query = "SELECT * FROM doctor_view WHERE doctor_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("specialization")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Doctor does not exist!");
        }
        return null;
    }
    //fetch all doctor
    public static List<Doctor> fetchAllDoctor(){
        String query = "SELECT * FROM doctor_view ";
        List<Doctor> doctors = new ArrayList<>();
        
        try (Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    doctors.add( new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("specialization")
                    )
                    );
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    //add doctor
    public static boolean addDoctor(Doctor doctor) {
        String query = "INSERT INTO doctors (username, password, first_name, last_name, email, phone_number, specialization) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctor.getUsername());
            stmt.setString(2, doctor.getPassword());
            stmt.setString(3, doctor.getFirstName());
            stmt.setString(4, doctor.getLastName());
            stmt.setString(5, doctor.getEmail());
            stmt.setString(6, doctor.getPhone());
            stmt.setString(7, doctor.getSpecialization());
    
            return stmt.executeUpdate() > 0; // Returns true if insertion is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //delete doctor
    public static boolean deleteDoctor(int doctorId) {
        String query = "DELETE FROM users WHERE id = ?";
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            return stmt.executeUpdate() > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateDoctor(Doctor doctor) {
        String query = "UPDATE users SET username=?, password=?, first_name=?, last_name=?, email=?, phone_number=? WHERE id=?";
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctor.getUsername());
            stmt.setString(2, doctor.getPassword());
            stmt.setString(3, doctor.getFirstName());
            stmt.setString(4, doctor.getLastName());
            stmt.setString(5, doctor.getEmail());
            stmt.setString(6, doctor.getPhone());
            stmt.setInt(7, doctor.getId());
    
            return stmt.executeUpdate() > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
