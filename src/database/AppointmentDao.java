package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxCursor.Fetch;

import javaCode.Appointment;
import javaCode.Doctor;
import javaCode.Patient;
import util.base.Person;

public class AppointmentDao {

    //save appointment in to database
    public static void saveAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, duration, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctoId());
            stmt.setObject(3, appointment.getAppointmentDate()); // LocalDateTime
            stmt.setLong(4, appointment.getDuration().toMinutes());
            stmt.setString(5, appointment.getStatus());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get appointment base on id
    public static Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getObject("appointment_time", LocalDateTime.class),
                    Duration.ofMinutes(rs.getLong("duration")),
                    rs.getString("status")
                );
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//return null if appointment not found
    }

    //fetch all appointment from database
    public static List<Appointment> getAllAppointments() {
        String sql = "SELECT * FROM appointments";
        List<Appointment> appointments = new ArrayList<>();
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getObject("appointment_time", LocalDateTime.class),
                    Duration.ofMinutes(rs.getLong("duration")),
                    rs.getString("status")
                ));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    //Fetch appointments for a doctor base on id
    public static List<Appointment> getDoctorAppointments(int Id) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE doctor_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, Id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                    int patientId = rs.getInt("patient_id");
                    int doctorId = rs.getInt("doctor_id");
                    LocalDateTime appointmentTime = rs.getTimestamp("appointment_time").toLocalDateTime();
                    Duration duration = Duration.ofMinutes(rs.getLong("duration"));
                    String status = rs.getString("status");
                    appointments.add(new Appointment(appointmentId,patientId,doctorId,appointmentTime,duration,status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    //get appointment base on patients id

    public static List<Appointment> getAppointmentsByPatientId(int patientId) {
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        List<Appointment> appointments = new ArrayList<>();
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getObject("appointment_time", LocalDateTime.class),
                    Duration.ofMinutes(rs.getLong("duration")),
                    rs.getString("status")
                ));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    //update appointment status
    public static void updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete an appointment
    public static void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    

}

