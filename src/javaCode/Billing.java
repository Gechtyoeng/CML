package javaCode;

import java.sql.*;
import java.util.Date;
import java.util.List;
import util.base.BillingStatus;

public class Billing {
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

    public Date getBillingDate() {
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

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
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

    // Insert a new bill into the database
    public void generateBill(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO billing (patient_id, total_amount, paid_amount, balance, billing_date, status, payment_method) VALUES (?, ?, ?, ?, NOW(), ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, this.patientId);
            pstmt.setDouble(2, this.totalAmount);
            pstmt.setDouble(3, this.paidAmount);
            pstmt.setDouble(4, this.balance);
            pstmt.setString(5, this.status.toString());
            pstmt.setString(6, this.paymentMethod);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.billingId = generatedKeys.getInt(1);
                }
            }
        }
    }
}
