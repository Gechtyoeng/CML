import util.base.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import database.UserDao;
import javaCode.*;
public class App {

    public static void main(String[] args) {
        // clinic management main system
          Scanner scanner = new Scanner(System.in);
          UserDao userDAO = new UserDao(); // Create an instance of UserDAO
          System.out.println("<=====Welcome to our clinis=====>");
          String role = "";
          //get the user role
          while (true) {
                System.out.println("Please choose your position below");
                System.out.println("1. Admin");
                System.out.println("2. Doctor");
                System.out.println("3. Receptionist");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int chioce = scanner.nextInt();
                scanner.nextLine();// Consume the newline character

                if(chioce == 1){
                    role = "Admin";
                    break;
                }
                else if(chioce == 2)
                {
                    role = "Doctor";
                    break;
                }else if (chioce == 3)
                {
                    role = "Receptionist";
                    break;
                }else if(chioce == 4)
                {
                    System.out.println("Exit the system...");
                    break;
                }else{
                    System.out.println("Invalid option! please choose again...");
                }
            
            //let user login or sign up
            System.out.println("1. Login");
            System.out.println("2. Sign up");
            System.out.print("Please enter your option:");
            int option = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter username:");
            String username = scanner.nextLine();
            System.out.print("Enter password:");
            String password = scanner.nextLine();

            if(option == 1){
                boolean isLogin = userDAO.login(username,password,role);
                if(isLogin){
                    System.out.println("Login Successful");
                    break;
                }else{
                    System.out.println("Check your input again...");
                    continue;
                }
            }
            else if(option == 2){
                
                boolean isSignup = userDAO.signup(username, password);
                if(isSignup){
                    System.out.println("Sign up Successful");
                    break;
                }else{
                    System.out.println("Check your input again...");
                    continue;
                }
                
            }
        }
         
     scanner.close(); 
    }
      
}
