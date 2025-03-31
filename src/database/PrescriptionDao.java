package database;

import java.sql.*;
import java.util.List;
import javaCode.PerscriptionMedicine;
import javaCode.Prescription;

public class PrescriptionDao {
 
    // Method to fetch a prescription by its ID
    public Prescription getPrescriptionById(int prescriptionID) {
        String sql = "SELECT * FROM prescriptions WHERE prescription_id = ?";
        Prescription prescription = null;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, prescriptionID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                    prescription = new Prescription(
                    rs.getInt("prescription_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("patient_id"),
                    rs.getDate("date_issued").toLocalDate(),
                    rs.getDouble("total_charge")
            );
                prescription.setConsultationCharge(rs.getDouble("consultationCharge"));
                prescription.setMedicationCharge(rs.getDouble("medicationCharge"));
                prescription.setDiagnosisCharge(rs.getDouble("diagnosisCharge"));
                prescription.setNursingCharge(rs.getDouble("nursingCharge"));
                prescription.setFacilityCharge(rs.getDouble("facilityCharge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prescription;
    }

    // Method to update prescription in the database
    public static void updatePrescription(Prescription prescription,int id) {
        String sql = "UPDATE prescriptions SET medicationCharge = ?, total_Charge = ? WHERE prescription_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, prescription.getMedicationCharge());
            pstmt.setDouble(2, prescription.getTotalCharge());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

            System.out.println("Prescription updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //save perscription medicine
    public static void savePrescriptionMedicines(int prescriptionID, List<PerscriptionMedicine> medicines) {
    String sql = "INSERT INTO prescription_medicine (prescription_id, medicine_id, quantity) VALUES (?, ?, ?)";
    try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (PerscriptionMedicine pm : medicines) {
            pstmt.setInt(1, prescriptionID);
            pstmt.setInt(2, pm.getMedicine().getMedicineID());
            pstmt.setInt(3, pm.getQuantity());
            pstmt.addBatch(); // Add to batch for efficiency
        }

        pstmt.executeBatch(); // Execute all at once
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public static int savePrescription(Prescription prescription) {
        String sql = "INSERT INTO prescriptions (doctor_id, patient_id, consultationCharge, diagnosisCharge, nursingCharge, facilityCharge, date_issued) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setInt(1, prescription.getDoctorID());
            pstmt.setInt(2, prescription.getPatientID());
            pstmt.setDouble(3, prescription.getConsultationCharge());
            pstmt.setDouble(4, prescription.getDiagnosisCharge());
            pstmt.setDouble(5, prescription.getNursingCharge());
            pstmt.setDouble(6, prescription.getFacilityCharge());
            java.sql.Date sqlDate = java.sql.Date.valueOf(prescription.getDateIssued());
            pstmt.setDate(7, sqlDate); 

    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int prescriptionID = generatedKeys.getInt(1);
                    return prescriptionID;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }
    public static Prescription getLatestPrescriptionByPatientId(int patientId) {
        String sql = "SELECT * FROM prescriptions WHERE patient_id = ? ORDER BY date_issued DESC LIMIT 1";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                Prescription prescription = new Prescription(
                        rs.getInt("prescription_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("patient_id"),
                        rs.getDate("date_issued").toLocalDate(),
                        rs.getDouble("total_charge")
                );
                return prescription;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // If no prescription found
    }
}
