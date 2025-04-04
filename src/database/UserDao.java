package database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javaCode.*;
import util.base.Person;
import org.mindrot.jbcrypt.BCrypt;

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
    //delete user
    public static boolean deleteUserById(int userId) {
        String query = "DELETE FROM Users WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0; // Returns true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //return the user when they login successful
    public static Person login(String username, String password){
        Person user = fetchUser(null,username, password,null);

        if(user != null){
            System.out.println("Welcome "+user.getRole()+" "+user.getFirstName()+" "+user.getLastName());
            return user;
        }else{

        System.out.println("Invalid username or password!");
        return null;
        }
    }

    //this method use to insert user to users table in dadabase and it will return ID for each user 
    //then we use this id to insert in to specific table for each user (if fail to insert it return 0)
     public static int registerUser(String username, String password, String firstName, String lastName,
                                    String email, String phoneNumber,String role) {
        
        String query = "INSERT INTO users (username, password, first_name, last_name, email, phone_number, role) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            // Set values in the prepared statement
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, phoneNumber);
            stmt.setString(7, role);

            // Execute the insert
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {

                // Retrieve the generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1); // Get the auto-incremented ID
                    return generatedId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Signup for Doctors
    public static boolean signupDoctor(String username, String password, String firstName, String lastName,
                                    String email, String phoneNumber, String specialization) {

        int doctorId = registerUser(username, password, firstName, lastName, email, phoneNumber,"Doctor");
        
        //insert into doctor table
        try (Connection conn = Database.connect()){

            String mysql = "INSERT INTO doctors (doctor_id ,specialization) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(mysql);
            stmt.setInt(1, doctorId);
            stmt.setString(2, specialization);
            stmt.executeUpdate();
           
            return true;
        } catch (Exception e) {
           e.printStackTrace();
        }
       return false;
    }

    //Signup for Receptionists
    public static boolean signupReceptionist(String username, String password, String firstName, String lastName,
                                           String email, String phoneNumber) {
        int re_id = registerUser(username, password, firstName, lastName, email, phoneNumber,"Receptionist");

        try (Connection conn = Database.connect()){
            String mysql = "INSERT INTO receptionists (receptionist_id) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(mysql);

            stmt.setInt(1, re_id);
            stmt.executeUpdate();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Signup for Patients
    public static boolean signupPatient(String username, String password, String firstName, String lastName,
                                String email, String phoneNumber,String gender, String dob, String address) {
    
        //get patient id
        int patientId = registerUser(username, password, firstName, lastName, email, phoneNumber, "Patient");

        try(Connection conn = Database.connect()){
            String mysql = 
            "INSERT INTO patients (patient_id, dob, address, gender) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(mysql);
            stmt.setInt(1, patientId);
            stmt.setString(2, dob);
            stmt.setString(3, address);
            stmt.setString(4, gender);
            stmt.executeUpdate();
            return true;
          
        } catch (Exception e) {
             e.printStackTrace();
        }
            return false;
    }

    //check the password match with the hash or not
    public static boolean authenticateUser(String username, String enteredPassword) {
        String query = "SELECT password FROM users WHERE username = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");  // Hashed password from DB
                
                // Compare the entered password with the hashed password
                return BCrypt.checkpw(enteredPassword, storedHashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false; // User not found or error occurred
    }


    //this funtion use to fetch user base on their role by using id or username & password or name
    public static Person fetchUser(Integer id, String username, String password, String name) {
        Person user = null;
        StringBuilder query = new StringBuilder("SELECT * FROM user_details WHERE 1=1");
    
        if (id != null) query.append(" AND id = ?");
        if (username != null) query.append(" AND username = ?");
        if (password != null) query.append(" AND password = ?");
        if (name != null) query.append(" AND (first_name LIKE ? OR last_name LIKE ?)");
    
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
    
            int index = 1;
            if (id != null) stmt.setInt(index++, id);
            if (username != null) stmt.setString(index++, username);
            if (password != null) stmt.setString(index++, password); 
            if (name != null) {
                stmt.setString(index++, "%" + name + "%");
                stmt.setString(index++, "%" + name + "%");
            }
    
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                int userId = rs.getInt("id");
                String userUsername = rs.getString("username");
                String Password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");
    
                switch (role) {
                    case "Doctor":
                        user = new Doctor(userId, userUsername, Password, firstName, lastName, email, phoneNumber, rs.getString("specialization"));
                        break;
                    case "Patient":
                        user = new Patient(userId, userUsername, Password, firstName, lastName, email, phoneNumber, rs.getString("gender"), rs.getString("dob"), rs.getString("address"));
                        break;
                    case "Receptionist":
                        user = new Receptionist(userId, userUsername, Password, firstName, lastName, email, phoneNumber);
                        user.setrole(role);
                        break;
                    case "Pharmacist":
                        user = new Receptionist(userId, userUsername, Password, firstName, lastName, email, phoneNumber);
                        user.setrole(role);
                        break;
                    case "Admin":
                        user = new Admin(userId, userUsername,Password, firstName, lastName, email, phoneNumber);
                        break;
                    default:
                        user = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }
    
        return user;
    }
    //method to check if the username already exist in our database
    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
    
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Username already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Username is available
    }

    // Method to check if the email already exists in the database
    public static boolean isEmailTaken(String email) {
        // Query to check if the email exists
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Email already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Email is available
    }
    
    public static boolean isValidPassword(String password) {
        // Check password length and if it contains at least one special character
        return password.length() >= 8 && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
    //method for allow user to chage their password after they login
    public static boolean changePassword(String username, Scanner scanner) {
        String querySelect = "SELECT password FROM users WHERE username = ?";
        String queryUpdate = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement selectStmt = conn.prepareStatement(querySelect)) {
            
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // Verify Old Password
                System.out.print("Enter your current password: ");
                String oldPassword = scanner.nextLine();

                if (!BCrypt.checkpw(oldPassword, storedHashedPassword)) {
                    System.out.println("Incorrect password. Try again.");
                    return false;
                }

                //Enter New Password & Validate
                String newPassword;
                while (true) {
                    System.out.print("Enter new password (min 8 chars, 1 special char): ");
                    newPassword = scanner.nextLine();

                    if (isValidPassword(newPassword)) {
                        break;
                    } else {
                        System.out.println("Password must be at least 8 characters long and contain at least 1 special character.");
                    }
                }

                // Hash New Password & Update
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

                try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                    updateStmt.setString(1, hashedNewPassword);
                    updateStmt.setString(2, username);
                    updateStmt.executeUpdate();
                    System.out.println("Password changed successfully!");
                    return true;
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //this function use to hash all password in database
    public static void updatePasswords() {
        String selectQuery = "SELECT id, password FROM users";
        String updateQuery = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             ResultSet rs = selectStmt.executeQuery()) {
            
            while (rs.next()) {
                int userId = rs.getInt("id");
                String password = rs.getString("password");

                // Only hash the password if it is not already hashed
                if (!isHashed(password)) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setInt(2, userId);
                        updateStmt.executeUpdate();
                        System.out.println("Updated password for user ID: " + userId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if the password is already hashed
    public static boolean isHashed(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}

