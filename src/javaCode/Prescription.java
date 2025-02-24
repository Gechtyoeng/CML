package javaCode;

import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private int prescriptionID;
    private int doctorID;
    private int patientID;
    private List<Medicine> medicines;
    private double consultationCharge;
    private double medicationCharge;
    private String dateIssued;

    // Constructor
    public Prescription(int prescriptionID, int doctorID, int patientID, List<Medicine> medicines, String dateIssued) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.medicines = new ArrayList<>(medicines);
        this.dateIssued = dateIssued;
        this.consultationCharge = 0.0;
        this.medicationCharge = 0.0;
    }

    // Getters and Setters
    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public double getConsultationCharge() {
        return consultationCharge;
    }

    public void setConsultationCharge(double consultationCharge) {
        this.consultationCharge = consultationCharge;
    }

    public double getMedicationCharge() {
        return medicationCharge;
    }

    public void setMedicationCharge(double medicationCharge) {
        this.medicationCharge = medicationCharge;
    }

    // Method to display prescription details
    public void viewPrescriptionDetails() {
        System.out.println("Prescription ID: " + prescriptionID);
        // System.out.println("Doctor: " + doctorID.getName());
        // System.out.println("Patient: " + patientID.getName());
        System.out.println("Date Issued: " + dateIssued);
        System.out.println("Medicines Prescribed:");
        // for (Medicine med : medicines) {
        //     System.out.println(" - " + med.getMedicineName() + " (Qty: " + med.getQuantity() + ")");
        // }
        System.out.println("Consultation Charge: $" + consultationCharge);
        System.out.println("Medication Charge: $" + medicationCharge);
        System.out.println("Total Charge: $" + (consultationCharge + medicationCharge));
    }

    // Method to delete a prescription
    public void deletePrescription() {
        System.out.println("Prescription " + prescriptionID + " deleted.");
        this.medicines.clear();
        this.consultationCharge = 0;
        this.medicationCharge = 0;
    }

    // Method to create a prescription
    public static Prescription createPrescription(int doctorID, int patientID, List<Medicine> medicines) {
        int newPrescriptionID = (int) (Math.random() * 10000); // unique ID
        String currentDate = java.time.LocalDate.now().toString(); // Auto-generate date
        return new Prescription(newPrescriptionID, doctorID, patientID, medicines, currentDate);
    }

    // Method to update prescription details
    public void updatePrescription(List<Medicine> newMedicines) {
    }

    // Method to add a consultation charge
    public void addConsultationCharge(int doctorID, double amount) {
    }

    // Method to add medication charge
    public void addMedicationCharge(int medicineID, int quantity) {
    }
}

