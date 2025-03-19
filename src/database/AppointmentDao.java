package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getDoctor().getId());
            stmt.setObject(3, appointment.getAppointmentDate()); // LocalDateTime
            stmt.setLong(4, appointment.getDuration().toMinutes());
            stmt.setString(5, appointment.getStatus());
            stmt.executeUpdate();

            System.out.println("Appointment saved to database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //fetch all appointment and store in to appointment from database
    public static List<Appointment> fetchAllAppointment() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * from appointments";
        
        try(Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int patientId = rs.getInt("patient_id");
                    int doctorId = rs.getInt("doctor_id");
                    LocalDateTime appointmentTime = rs.getTimestamp("appointment_time").toLocalDateTime();
                    Duration duration = Duration.ofMinutes(rs.getLong("duration"));
                    String status = rs.getString("status");

                    //get doctor and patient
                    Doctor doctor = null;
                    Patient patient = null;

                    Person patientUser = UserDao.fetchUser(patientId, null, null, null);
                    Person doctorUser = UserDao.fetchUser(doctorId, null, null, null);

                    if(patientUser instanceof Patient){
                        patient = (Patient)patientUser;
                    }
                    if(doctorUser instanceof Doctor){
                        doctor = (Doctor)doctorUser;
                    }
                    if(doctor != null && patient != null){
                        Appointment appointment = new Appointment(patient, doctor, appointmentTime, duration, status);
                        appointments.add(appointment);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                return appointments;
                }
}

