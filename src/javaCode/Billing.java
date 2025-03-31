package javaCode;

import java.sql.*;
import java.time.LocalDate;

import database.Database;
import util.base.BillingStatus;

public class Billing {
    private int billingId;
    private int patientId;
    private double totalAmount;//total amount from perscription total charge
    private double paidAmount;
    private double balance;
    private LocalDate billingDate;
    private BillingStatus status;
    private String paymentMethod;
    private int prescriptionId;

    // Constructor
    public Billing(int billingId, int patientId,int prescriptionId, double totalAmount, double paidAmount, BillingStatus status, String paymentMethod) {
        this.billingId = billingId;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balance = totalAmount - paidAmount;
        this.billingDate = LocalDate.now();
        this.status = status;
        this.paymentMethod = paymentMethod;
    }
    public Billing(int billingId, Prescription prescription, double paidAmount, BillingStatus status, String paymentMethod) {
        this.billingId = billingId;
        this.patientId = prescription.getPatientID();
        this.prescriptionId = prescription.getPrescriptionID();
        this.totalAmount = prescription.getTotalCharge();  // Fetch total charge from Prescription
        this.paidAmount = paidAmount;
        this.balance = totalAmount - paidAmount;
        this.billingDate = LocalDate.now();
        this.status = status;
        this.paymentMethod = paymentMethod;
    }
    // Getters and Setters
    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
        this.balance = totalAmount - paidAmount;
        this.status = (balance == 0) ? BillingStatus.PAID : BillingStatus.UNPAID;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public BillingStatus getStatus() {
        return status;
    }

    public void setStatus(BillingStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public int getPrescriptionID(){
        return this.prescriptionId;
    }
    

    // Calculate total cost for the patient (retrieves from database)
    public double calculateTotal(Connection conn, int patientId) throws SQLException {
        String query = "SELECT SUM(total_cost) FROM services WHERE patient_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.totalAmount = rs.getDouble(1);
            }
        }
        return this.totalAmount;
    }

    // Process payment
    public boolean addPayment(Connection conn, int patientID, double amount, String paymentMethod) throws SQLException {
        String updateQuery = "UPDATE billing SET paid_amount = paid_amount + ?, payment_method = ?, status = ? WHERE patient_id = ?";
        
        double newPaidAmount = this.paidAmount + amount;
        this.paidAmount = newPaidAmount;
        this.balance = this.totalAmount - newPaidAmount;
        this.status = (this.balance == 0) ? BillingStatus.PAID : BillingStatus.UNPAID;
        this.paymentMethod = paymentMethod;

        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, this.status.toString());
            pstmt.setInt(4, patientID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Retrieve billing details from the database
    public void getBillingDetails(Connection conn) throws SQLException {
        String query = "SELECT * FROM billing WHERE billing_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, this.billingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.patientId = rs.getInt("patient_id");
                this.totalAmount = rs.getDouble("total_amount");
                this.paidAmount = rs.getDouble("paid_amount");
                this.balance = this.totalAmount - this.paidAmount;
                this.paymentMethod = rs.getString("payment_method");
                this.status = BillingStatus.valueOf(rs.getString("status"));
            }
        }
    }

    // Check pending payment for a patient
    public double checkPendingPayment(Connection conn, int patientID) throws SQLException {
        String query = "SELECT (total_amount - paid_amount) FROM billing WHERE patient_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    // Generate an invoice
    public String generateInvoice() {
        return "=============== Invoice ===============\n" +
                "Billing ID: " + billingId + "\n" +
                "Patient ID: " + patientId + "\n" +
                "Total Amount: $" + totalAmount + "\n" +
                "Paid Amount: $" + paidAmount + "\n" +
                "Outstanding Balance: $" + balance + "\n" +
                "Payment Status: " + status + "\n" +
                "Payment Method: " + paymentMethod + "\n" +
                "=======================================";
    }
   
}
