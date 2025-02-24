package javaCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import util.base.BillingStatus;

public class Billing {
    private int billingId;          // Corresponds to billing_id
    private int patientId;          // Corresponds to patient_id
    private double totalAmount;     // Corresponds to total amount
    private double paidAmount;      // Corresponds to paid amount
    private double balance;
    private Date billingDate;       
    private BillingStatus status;   // BillingStatus enum (Paid/Unpaid)
    private String paymentMethod;   // "Cash", "Card", "Insurance"
    private List<Prescription> prescriptions; // List of prescriptions

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

    // Methods
    //Calculate total: calculate total cost base on the service that patient used
    public double calculateTotal(int patientId){
        return this.totalAmount - this.paidAmount;
    }

    //Process payment: cash, card, insurance, etc.
    public boolean addPayment(int patientID, double amount, String paymentMethod){
        //Logic
        return true;
    }

    //Patient history and detail (connect to database)
    public void getBillingDetails(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Billing WHERE billingID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Billing ID: " + rs.getInt("billingID"));
                System.out.println("Patient ID: " + rs.getInt("patientID"));
                System.out.println("Total Amount: $" + rs.getDouble("totalAmount"));
                System.out.println("Outstanding Balance: $" + rs.getDouble("outstandingBalance"));
                System.out.println("Payment Status: " + rs.getString("paymentStatus"));
            }
        }
    }

    //Check patient pending payment
    public double checkPendingPayment(int patientID){
        return totalAmount - paidAmount;
    }

    //Generate invoice
    public String generateInvoice() {
        return "===============Invoice=============== \nBilling ID: " + billingId + "\nPatient ID: " + patientId +
                "\nTotal Amount: $" + totalAmount + "\nOutstanding Balance: $" + balance +
                "\nPayment Status: " + status + "\n======================================";
    }

    // Generate a bill (insert into database)
    public void generateBill(Connection conn) throws SQLException {
    }
}
