package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javaCode.Billing;
import util.base.BillingStatus;

public class BillingDao {
    public static int saveBilling(Billing billing) {
        String sql = "INSERT INTO billings (patient_id, prescription_id, total_amount, paid_amount, balance, billing_date, status, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, billing.getPatientId());
            pstmt.setInt(2, billing.getPrescriptionID());
            pstmt.setDouble(3, billing.getTotalAmount());
            pstmt.setDouble(4, billing.getPaidAmount());
            pstmt.setDouble(5, billing.getBalance());
            pstmt.setString(6, billing.getBillingDate().toString());
            pstmt.setString(7, billing.getStatus().toString());
            pstmt.setString(8, billing.getPaymentMethod());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the new Billing ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // Retrieve billing by ID
    public static Billing getBillingById(int billingId) {
        String sql = "SELECT * FROM billings WHERE billing_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Billing(
                        rs.getInt("billing_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("prescription_id"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("paid_amount"),
                        BillingStatus.valueOf(rs.getString("status")),
                        rs.getString("payment_method")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update Payment Status
    public static boolean updatePaymentStatus(int billingId, double newPaidAmount, BillingStatus newStatus) {
        String sql = "UPDATE billings SET paid_amount = ?, balance = total_amount - ?, status = ? WHERE billing_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPaidAmount);
            pstmt.setDouble(2, newPaidAmount);
            pstmt.setString(3, newStatus.toString());
            pstmt.setInt(4, billingId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
//get total income from all data
    public static void totalIncome() {
        String sql = "SELECT SUM(total_amount) as total_income FROM billings";
        try (Connection conn = Database.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery())
        {
            System.out.println("=======================");
            System.out.println("=====Total-Income======");
            System.out.println("=======================");

            if(rs.next()){
                double totalIncome = rs.getDouble("total_income");
                System.out.printf("Total Income: $%.2f%n", totalIncome);
            }else{
                System.out.println("No total income record.");
            }
            System.out.println("=======================");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
//view inocme by date rang that promt user to input
    public static void viewIncomeByDateRange(Scanner scanner) {
        String startDate, endDate;

        System.out.print("Enter start date (YYYY-MM-DD): ");
        startDate = scanner.nextLine();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        endDate = scanner.nextLine();

        String sql = "SELECT SUM(total_amount) AS income FROM billings WHERE billing_date BETWEEN ? AND ?";

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double income = rs.getDouble("income");
                System.out.println("\n=================================");
                System.out.println("      Income Report by Date      ");
                System.out.println("=================================");
                System.out.printf("Date %s to %s: ", startDate, endDate);
                System.out.println();
                System.out.printf("Total Income: $%.2f%n",income);
                System.out.println("=================================");
            } else {
                System.out.println("No data found for the given date range.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //get today income
    public static void getTodayIncome(){
        String sql = "SELECT SUM(total_amount) as income FROM billings where DATE(billing_date) = CURDATE()";

        try (Connection conn = Database.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) { 

            System.out.println("=======================");
            System.out.println("=====Today-Income======");
            System.out.println("=======================");

            if(rs.next()){
                double totalIncome = rs.getDouble("income");
                System.out.printf("Today Income: $%.2f%n", totalIncome);
            }else{
                System.out.println("No total income record.");
            }
            System.out.println("=======================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
