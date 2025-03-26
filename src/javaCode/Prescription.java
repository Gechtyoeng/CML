package javaCode;

import java.time.LocalDate;
import java.util.*;
import database.InventoryDao;

public class Prescription {
    private int prescriptionID;
    private int doctorID;
    private int patientID;
    private List<PerscriptionMedicine> medicines = new ArrayList<>();   //Waiting for Nita class medicines
    private double consultationCharge;
    private double medicationCharge;
    private double diagnosisCharge;
    private double nursingCharge;
    private double facilityCharge;
    private double totalCharge;
    LocalDate dateIssued;

    //still have bug cannot insert the fee into database but can calculate total

    // Fixed charges for hospital services
    private static final double CONSULTATION_FEE = 20.0;  // Doctor fee
    //private static final double MEDICINE_FEE = 5.0;       // Per medicine - change later
    private static final double DIAGNOSIS_FEE = 15.0;     // Duagnosis fee
    private static final double NURSING_FEE = 10.0;       // Nursing care fee
    private static final double FACILITY_FEE = 30.0;      // Clinic facility use

    // Constructor
    public Prescription(int prescriptionID, int doctorID, int patientID,LocalDate dateIssued, double totalCharge) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.dateIssued = dateIssued;
        // Fixed charges
        this.consultationCharge = CONSULTATION_FEE;
        this.diagnosisCharge = DIAGNOSIS_FEE;
        this.nursingCharge = NURSING_FEE;
        this.facilityCharge = FACILITY_FEE;
        
        this.totalCharge = totalCharge;
    }
    //for new prescription
    public Prescription( int doctorID, int patientID) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.dateIssued = LocalDate.now();
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
    // Getters and setters
    public int getPrescriptionID() {
        return prescriptionID;
    }
    public List<PerscriptionMedicine> getMedicine(){
        return this.medicines;
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

    public double getDiagnosisCharge() {
        return diagnosisCharge;
    }

    public void setDiagnosisCharge(double diagnosisCharge) {
        this.diagnosisCharge = diagnosisCharge;
    }

    public double getNursingCharge() {
        return nursingCharge;
    }

    public void setNursingCharge(double nursingCharge) {
        this.nursingCharge = nursingCharge;
    }

    public double getFacilityCharge() {
        return facilityCharge;
    }

    public void setFacilityCharge(double facilityCharge) {
        this.facilityCharge = facilityCharge;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

    public List<PerscriptionMedicine> getPerscriptionMedicines() {
        return this.medicines;
    }

    // Method to calculate total charge
    private void calculateTotalCharge() {
        this.totalCharge = (consultationCharge + medicationCharge + diagnosisCharge + nursingCharge + facilityCharge);
    }

    // Add medicine to prescription
    public  void addMedicine(Medicine medicine, int quantity) {
        if (medicine == null || quantity <= 0) {
            System.out.println("Invalid medicine or quantity.");
            return;
        }
        // prescribedMedicines.add(new PerscriptionMedicine(medicine, quantity));
        // medicationCharge += medicine.getPrice() * quantity;
        // calculateTotalCharge();
        if (InventoryDao.reduceStock(medicine.getMedicineID(), quantity)) {
            this.medicines.add(new PerscriptionMedicine(medicine, quantity));
            double price = medicine.getPrice();
            medicationCharge += price * quantity;
            calculateTotalCharge();
            System.out.println("Medicine added to prescription and stock updated.");
        } else {
            System.out.println("Failed to add medicine. Not enough stock.");
        }
    }

    @Override
    public String toString() {
    return "Prescription ID: " + prescriptionID + "\n" +
           "Doctor ID: " + doctorID + "\n" +
           "Patient ID: " + patientID + "\n" +
           "Date Issued: " + dateIssued + "\n" +
           "Doctor Consultation Fee: $" + consultationCharge + "\n" +
           "Medication Charge: $" + medicationCharge + "\n" +
           "Diagnosis Charge: $" + diagnosisCharge + "\n" +
           "Nursing and Service Charge: $" + nursingCharge + "\n" +
           "Clinic Facility Charge: $" + facilityCharge + "\n" +
           "Total Hospital Bill: $" + totalCharge + "\n";
}
    
}
