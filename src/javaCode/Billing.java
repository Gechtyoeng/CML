package javaCode;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import util.base.BillingStatus;

public class Billing {
    private static final Logger LOGGER = Logger.getLogger(Billing.class.getName());

    private int billingId;
    private int patientId;
    private double totalAmount;
    private double paidAmount;
    private double balance;
    private Date billingDate;
    private BillingStatus status;
    private String paymentMethod;
    private List<Prescription> prescriptions;

    // Constructor
    public Billing(int billingId, int patientId, double totalAmount, double paidAmount, BillingStatus status, String paymentMethod, List<Prescription> prescriptions) {
        this.billingId = billingId;
        this.patientId = patientId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balance = totalAmount - paidAmount;
        this.billingDate = new Date();
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.prescriptions = prescriptions;
    }

    // Getters and Setters
    public int getBillingId() { return billingId; }
    public int getPatientId() { return patientId; }
    public double getTotalAmount() { return totalAmount; }
    public double getPaidAmount() { return paidAmount; }
    public double getBalance() { return balance; }
    public Date getBillingDate() { return billingDate; }
    public BillingStatus getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<Prescription> getPrescriptions() { return prescriptions; }

    public void setPaidAmount(double amount) {
        this.paidAmount = amount;
        this.balance = this.totalAmount - amount;
        this.status = (balance == 0) ? BillingStatus.PAID : BillingStatus.UNPAID;
    }

    public void setPaymentMethod(String method) { this.paymentMethod = method; }

    //Method to Process Payments
    public boolean processPayment(Connection conn, double amount, String method) {
        try {
            conn.setAutoCommit(false); // Start transaction
    
            String query = "UPDATE billing SET paid_amount = ?, balance = ?, payment_method = ?, status = ? WHERE billing_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
    
            double newPaidAmount = this.paidAmount + amount;
            double newBalance = this.totalAmount - newPaidAmount;
            BillingStatus newStatus = (newBalance == 0) ? BillingStatus.PAID : BillingStatus.UNPAID;
    
            pstmt.setDouble(1, newPaidAmount);
            pstmt.setDouble(2, newBalance);
            pstmt.setString(3, method);
            pstmt.setString(4, newStatus.toString());
            pstmt.setInt(5, this.billingId);
    
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                this.paidAmount = newPaidAmount;
                this.balance = newBalance;
                this.status = newStatus;
                this.paymentMethod = method;
                LOGGER.info("Payment processed successfully.");
                conn.commit(); //saves pending changes (like inserts, updates, or deletes) made within a transaction to the database
                return true;
            } 
            return false;
            
        } catch (SQLException e) {
            LOGGER.severe("Error processing payment: " + e.getMessage());
            try {
                conn.rollback(); //sends a ROLLBACK statement to the MySQL server, undoing all data changes from the current transaction
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Error rolling back transaction: " + rollbackEx.getMessage());
            }
        }
        return false;
    }

    //Retrieve Billing Details from Database
    public static Billing getBillingById(Connection conn, int billingId) {
        try {
            String query = "SELECT * FROM billing WHERE billing_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, billingId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                List<Prescription> prescriptions = getPrescriptionsForBilling(conn, billingId);
                return new Billing(
                    rs.getInt("billing_id"),
                    rs.getInt("patient_id"),
                    rs.getDouble("total_amount"),
                    rs.getDouble("paid_amount"),
                    BillingStatus.valueOf(rs.getString("status")),
                    rs.getString("payment_method"),
                    prescriptions
                );
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving billing details: " + e.getMessage());
        }
        return null;
    }

    //Generate Invoice
    public String generateInvoice() {
        return "=========== Invoice ===========\n" +
                "Billing ID: " + billingId + "\n" +
                "Patient ID: " + patientId + "\n" +
                "Total Amount: $" + totalAmount + "\n" +
                "Paid Amount: $" + paidAmount + "\n" +
                "Balance: $" + balance + "\n" +
                "Payment Status: " + status + "\n" +
                "Payment Method: " + paymentMethod + "\n" +
                "================================";
    }

    //Insert a New Bill into Database
    public boolean createBillingRecord(Connection conn) {
        String query = "INSERT INTO billing (patient_id, total_amount, paid_amount, balance, billing_date, status, payment_method) VALUES (?, ?, ?, ?, NOW(), ?, ?)";
        
        try {
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, this.patientId);
                pstmt.setDouble(2, this.totalAmount);
                pstmt.setDouble(3, this.paidAmount);
                pstmt.setDouble(4, this.balance);
                pstmt.setString(5, this.status.toString());
                pstmt.setString(6, this.paymentMethod);
    
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        this.billingId = generatedKeys.getInt(1);
    
                        // Insert prescriptions
                        if (this.prescriptions != null) {
                            for (Prescription prescription : this.prescriptions) {
                                insertPrescription(conn, this.billingId, prescription);
                            }
                        }
                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.severe("Error inserting billing record: " + e.getMessage());
            }
        } catch (SQLException rollbackEx) {
            LOGGER.severe("Error rolling back transaction: " + rollbackEx.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException e) {
                LOGGER.warning("Could not restore auto-commit mode.");
            }
        }
        return false;
    }
    
    
    private void insertPrescription(Connection conn, int billingId, Prescription prescription) {
        String query = "INSERT INTO prescriptions (billing_id, medicine_name, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, billingId);
            pstmt.setString(2, prescription.getMedicineName());
            pstmt.setInt(3, prescription.getQuantity());
            pstmt.setDouble(4, prescription.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting prescription: " + e.getMessage());
        }
    }
    
    public static List<Prescription> getPrescriptionsForBilling(Connection conn, int billingId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM prescriptions WHERE billing_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, billingId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int prescriptionID = rs.getInt("prescription_id");
                int doctorID = rs.getInt("doctor_id");
                int patientID = rs.getInt("patient_id");
                String dateIssued = rs.getString("dateIssued");
    
                // Retrieve medicines for this prescription
                List<Medicine> medicines = getMedicinesForPrescription(conn, prescriptionID);

                prescriptions.add(new Prescription(prescriptionID, doctorID, patientID, medicines, dateIssued));
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving prescriptions: " + e.getMessage());
        }
        return prescriptions;
    }

    private static List<Medicine> getMedicinesForPrescription(Connection conn, int prescriptionID) {
        List<Medicine> medicines = new ArrayList<>();
        String query = "SELECT m.medicineID, m.name, pm.quantity, m.price " +
                       "FROM PrescriptionMedicine pm " +
                       "JOIN medicines m ON pm.medicineID = m.medicineID " +
                       "WHERE pm.prescriptionID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, prescriptionID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {     // this line is error, related to medicine, check back when merge together
                Medicine medicine = new Medicine(
                    rs.getInt("medicineID"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                medicines.add(medicine);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving medicines for prescription: " + e.getMessage());
        }
        return medicines;
    }
}
