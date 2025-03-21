package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javaCode.Patient;

public class PatientDao {
     // Fetch patient by ID
    public static Patient getPatientById(int patientId){
        String query = "SELECT * FROM patient_view WHERE patient_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("gender"),
                    rs.getString("dob"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Doctor does not exist!");
        }
        return null;
    }
    }

