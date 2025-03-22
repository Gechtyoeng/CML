package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

}
