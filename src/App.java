import util.base.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import database.*;
import javaCode.*;
public class App {

    public static void main(String[] args) {
        // clinic management main system
        Scanner scanner = new Scanner(System.in);
        UserDao userDAO = new UserDao(); // Create an instance of UserDAO
        // Fetch all appointments from the database
        List<Appointment> appointments = AppointmentDao.fetchAllAppointment();
        Person user = null;
        System.out.println("<===== Welcome to Clinic Management System =====>");
        
                while (true) {
                    System.out.println("\n1. Login");
                    System.out.println("2. Exit");
                    System.out.print("Choose an option: ");
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
        
                    if (option == 1) {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
        
                        //fetching the user base on username and password
                        user = UserDao.fetchUser(null,username, password,null);
        
                        loadDashboard(user, userDAO, scanner);
                    } 
                    else if (option == 2) {
                        System.out.println("Exiting the system...");
                        break;
                    } 
                    else {
                        System.out.println("Invalid choice! Please try again.");
                        continue;
                    }
                }
                scanner.close();
    }
        
            //  Load Role-Based Dashboard
    public static void loadDashboard(Person user, UserDao userDAO, Scanner scanner) {
        switch (user.getRole()) {
            case "Admin":
                adminMenu(userDAO, scanner);
                break;
            case "Doctor":
                doctorMenu(userDAO, scanner);
                break;
            case "Patient":
                System.out.println("To be continue...");
                break;
            case "Receptionist":
                receptionistMenu(userDAO, scanner);
                break;
            default:
                System.out.println("No specific user role!");
                break;
        }
    }

    //  Admin Menu
    public static void adminMenu(UserDao userDAO, Scanner scanner) {
        while (true) {
            System.out.println("\n=====Welcome to Admin menu=====");
            System.out.println("1. Register Doctor");
            System.out.println("2. Register Receptionist");
            System.out.println("3. Register Patient");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerDoctor(userDAO, scanner);
                    break;
                case 2:
                    registerReceptionist(userDAO, scanner);
                    break;
                case 3:
                    registerPatient(userDAO, scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("invalid option!");
                    continue;
            }
        }
    }

    //  Receptionist Menu 
    public static void receptionistMenu(UserDao userDAO, Scanner scanner) {
        while (true) {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. Register Patient");
            System.out.println("2. Book an Appointment");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerPatient(userDAO, scanner);
                    break;
                case 2:
                    //bookAppointment();
                case 3:
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }
    //doctor menu
    public static void doctorMenu(UserDao userDAO, Scanner scanner){
        while (true) {
            System.out.println("\nDoctor Menu:");
            System.out.println("1. View appointment");
            System.out.println("2. Book an appointment");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerPatient(userDAO, scanner);
                    break;
                case 2:
                    //bookAppointment();
                case 3:
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }


    //  Register Doctor (Only Admin can do this)
    public static void registerDoctor(UserDao userDAO, Scanner scanner) {
        System.out.print("Enter doctor username: ");
        String username = scanner.nextLine();
        System.out.print("Enter doctor password: ");
        String password = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();

        if (userDAO.signupDoctor(username, password, firstName, lastName, email, phone, specialization)) {
            System.out.println("Doctor registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    //  Register Receptionist (Only Admin can do this)
    public static void registerReceptionist(UserDao userDAO, Scanner scanner) {
        System.out.print("Enter Receptionist username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Receptionist password: ");
        String password = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        if (userDAO.signupReceptionist(username, password, firstName, lastName, email, phone)) {
            System.out.println("Receptionist registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    // Register Patient (receptionist and admin)
    public static void registerPatient(UserDao userDAO, Scanner scanner) {
        System.out.print("Enter patient username: ");
        String username = scanner.nextLine();
        System.out.print("Enter patient password: ");
        String password = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        if (userDAO.signupPatient(username, password, firstName, lastName, email, phone, gender, dob, address)) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }
  
      
}
