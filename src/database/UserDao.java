package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public boolean login(String username, String password, String role) {
        try (Connection conn = Database.connect()) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet result = stmt.executeQuery();

            return result.next(); // Returns true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
     // **Signup method**
     public boolean signup(String username, String password) {
        try (Connection conn = Database.connect()) {
            // Check if the username already exists
            String checkUser = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkUser);
            checkStmt.setString(1, username);
            ResultSet checkResult = checkStmt.executeQuery();
            if (checkResult.next()) {
                return false; // Username already exists
            }

            // If username doesn't exist, insert a new user
            String insertUser = "INSERT INTO Users (username, password) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertUser);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            int rowsInserted = insertStmt.executeUpdate();
            
            return rowsInserted > 0; // Return true if signup successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
