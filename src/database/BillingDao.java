package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
