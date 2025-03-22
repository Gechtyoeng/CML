package javaCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.Database;

public class Prescription {
    private int prescriptionID;
    private int doctorID;
    private int patientID;
    private List<Medicine> medicines;   //Waiting for Nita class medicines
    private double consultationCharge;
    private double medicationCharge;
    private double diagnosisCharge;
    private double nursingCharge;
    private double facilityCharge;
    private double totalCharge;
    private String dateIssued;

    // Database connection details
    // private static final String URL = "jdbc:mysql://localhost:3306/clinic_db";  //Change latter after merge
    // private static final String USER = "root";
    // private static final String PASSWORD = "";

    // Fixed charges for hospital services
    private static final double CONSULTATION_FEE = 20.0;  // Doctor fee
    private static final double MEDICINE_FEE = 5.0;       // Per medicine - change later
    private static final double DIAGNOSIS_FEE = 15.0;     // Duagnosis fee
    private static final double NURSING_FEE = 10.0;       // Nursing care fee
    private static final double FACILITY_FEE = 30.0;      // Clinic facility use

    // Constructor
    public Prescription(int prescriptionID, int doctorID, int patientID, List<Medicine> medicines, String dateIssued) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.medicines = new ArrayList<>(medicines);
        this.dateIssued = dateIssued;

        // Fixed charges
        this.consultationCharge = CONSULTATION_FEE;
        this.diagnosisCharge = DIAGNOSIS_FEE;
        this.nursingCharge = NURSING_FEE;
        this.facilityCharge = FACILITY_FEE;

        // Initial medication charge (calculated later)
        this.medicationCharge = 0.0;

        // Calculate total charge
        calculateTotalCharge();
    }

    // Method to calculate total charge
    private void calculateTotalCharge() {
        this.totalCharge = (consultationCharge + medicationCharge + diagnosisCharge + nursingCharge + facilityCharge);
    }

    // Save prescription to database
    public void savePrescription() {
        String sql = "INSERT INTO prescriptions (doctor_id, patient_id, consultationCharge, medicationCharge, diagnosisCharge, nursingCharge, facilityCharge, totalCharge, dateIssued) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, doctorID);
            pstmt.setInt(2, patientID);
            pstmt.setDouble(3, consultationCharge);
            pstmt.setDouble(4, medicationCharge);
            pstmt.setDouble(5, diagnosisCharge);
            pstmt.setDouble(6, nursingCharge);
            pstmt.setDouble(7, facilityCharge);
            pstmt.setDouble(9, totalCharge);
            pstmt.setString(10, dateIssued);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.prescriptionID = generatedKeys.getInt(1);
                }
            }

            System.out.println("Prescription saved successfully with complete hospital costs.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add medicine with cost
    public void addMedicineCharge(int medicineID, int quantity) {
        double pricePerUnit = getMedicinePrice(medicineID);

        double totalMedicineCost = (pricePerUnit * quantity);
        this.medicationCharge += totalMedicineCost;

        calculateTotalCharge();
        updatePrescription();

        System.out.println("Medicine added. New total charge: $" + totalCharge);
    }

    // Fetch medicine price from database
    private double getMedicinePrice(int medicineID) {
        String sql = "SELECT price FROM medicines WHERE medicineID = ?";
        double pricePerUnit = 0;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, medicineID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pricePerUnit = rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pricePerUnit;
    }

    // Update prescription in database
    public void updatePrescription() {
        String sql = "UPDATE prescriptions SET consultationCharge = ?, medicationCharge = ?, diagnosisCharge = ?, nursingCharge = ?, facilityCharge = ?, totalCharge = ? WHERE prescriptionID = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, consultationCharge);
            pstmt.setDouble(2, medicationCharge);
            pstmt.setDouble(3, diagnosisCharge);
            pstmt.setDouble(4, nursingCharge);
            pstmt.setDouble(5, facilityCharge);
            pstmt.setDouble(6, totalCharge);
            pstmt.setInt(7, prescriptionID);
            pstmt.executeUpdate();

            System.out.println("Prescription updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Prescription Details
    public void viewPrescriptionDetails() {
        System.out.println("Prescription ID: " + prescriptionID);
        System.out.println("Doctor ID: " + doctorID);
        System.out.println("Patient ID: " + patientID);
        System.out.println("Date Issued: " + dateIssued);
        System.out.println("Doctor Consultation Fee: $" + consultationCharge);
        System.out.println("Medication Charge: $" + medicationCharge);
        System.out.println("Diagnosis Charge: $" + diagnosisCharge);
        System.out.println("Nursing and Service Charge: $" + nursingCharge);
        System.out.println("Clinic Facility Charge: $" + facilityCharge);
        System.out.println("Total Hospital Bill: $" + totalCharge);
    }
}
