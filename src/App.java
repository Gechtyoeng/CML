import util.base.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.*;
import database.*;
import javaCode.*;
import org.mindrot.jbcrypt.BCrypt;

public class App {

    public static void main(String[] args) {
        // clinic management main system
        //UserDao.updatePasswords();
        Scanner scanner = new Scanner(System.in);
        UserDao userDAO = new UserDao(); // Create an instance of UserDAo
        Person user = null;
                while (true) {
                    clearConsole();
                    System.out.println("\n========================================");
                    System.out.println("|  Welcome to Clinic Management System   |");
                    System.out.println("==========================================");
                    System.out.println("\n1. Login");
                    System.out.println("2. Close");
                    System.out.print("Choose an option: ");
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (option) {
                        case 1:
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine();

                            if(UserDao.authenticateUser(username, password)){
                            //     //fetching the user base on username and password
                            
                                user = UserDao.fetchUser(null,username, null,null);
                            }

                            if (user == null) {
                                System.out.println("Invalid username or password. Please try again.");
                                continue;
                            }
                            loadDashboard(user, userDAO, scanner);
                            break;
                        case 2:
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid option...");
                    } 
                }
    }
        
    

    //function for clear the console 
    public static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("Error clearing console.");
        }
    }
        
    //  Load Role-Based Dashboard
    public static void loadDashboard(Person user, UserDao userDAO, Scanner scanner) {
        switch (user.getRole()) {
            case "Admin":
                Admin admin = (Admin)user;
                adminMenu(scanner, admin);
               // scanner.close();
                break;
            case "Doctor":
                Doctor doctor = (Doctor)user;
                doctorMenu(scanner,doctor);
               // scanner.close();
                break;
            case "Patient":
                System.out.println("To be continue...");
                break;
            case "Receptionist":
                Receptionist receptionist = (Receptionist)user;
                receptionistMenu(scanner,receptionist);
               // scanner.close();
                break;
            case "Pharmacist":
                Receptionist pharmacist = (Receptionist)user;
                PharmacistMenu(scanner, pharmacist);
               // scanner.close();
                break;
            default:
                System.out.println("No specific user role!");
              //  scanner.close();
                break;
        }
    }

    // =============Admin Menu
    public static void adminMenu(Scanner scanner, Admin admin) {
    
        while (true) {
            clearConsole();//clear the console
            System.out.println("\n=================================");
            System.out.println("      Welcome to Admin Menu      ");
            System.out.println("=================================");
            System.out.println("1. Doctor management");
            System.out.println("2. Patient management");
            System.out.println("3. view financial report");
            System.out.println("4. Setting");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    DoctorMangement(scanner);
                    break;
                case 2:
                    PatientMangement(scanner);
                    break;
                case 3:
                    FinancialReport( scanner);
                    break;
                case 4:
                    AdminSettig(scanner, admin);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("invalid option!");
                    continue;
            }
        }
    }

    // ============Receptionist Menu --1
    public static void receptionistMenu(Scanner scanner, Receptionist receptionist) {
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("   Welcome to Receptionist Menu  ");
            System.out.println("=================================");
            System.out.println("1. Patient management");
            System.out.println("2. Appointment management");
            System.out.println("3. Make Billing");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    PatientMangement(scanner);
                    break;
                case 2:
                    AppointmentManagement( scanner);
                    break;
                case 3:
                    makeBilling(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }
    // ============Receptionist Menu --2
    public static void PharmacistMenu(Scanner scanner, Receptionist pharmacist) {
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("  Welcome to Pharmacist Menu   ");
            System.out.println("=================================");
            System.out.println("1. Inventory Management");
            System.out.println("2. Medicines Management");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    InventoryManagement(scanner);
                    break;
                case 2:
                    MedicineManagement(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }
    //doctor menu
    public static void doctorMenu(Scanner scanner,Doctor doctor){
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("      Welcome to Doctor Menu     ");
            System.out.println("=================================");
            System.out.println("1. View appointment");//view only belong to doctor appionment
            System.out.println("2. Write perscription");
            System.out.println("3. Book an Appointment");
            System.out.println("4. Update appointment");
            System.out.println("5. Setting");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    //view appointment()
                    System.out.println("==========="+doctor.getFirstName()+" "+doctor.getLastName()+" Appointment========");
                    doctor.viewAppointments();
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    //writeperscription
                    System.out.println("==========Write prescription==========");
                    System.out.print("Enter patient name:");
                    String patientname = scanner.nextLine();
                    Patient selectedPatient = (Patient)UserDao.fetchUser(null, null, null, patientname);
                    
                    if(selectedPatient != null){
                        Prescription thisPrescription = new Prescription(doctor.getId(), selectedPatient.getId());
                        int id = PrescriptionDao.savePrescription(thisPrescription);//get id from auto generate 
                       // System.out.println(id);
                        System.out.println("Enter Medicine to add & 0 to finish:");//add medicine and enter 0 to finish
                        Medicine medicine = null;
                        while (true) {
                            System.out.print("Enter medicine ID:");
                            int med_id = scanner.nextInt();
                            scanner.nextLine();

                            if(med_id == 0){break;}

                            medicine = MedicineDao.searchMedicineById(med_id);
                            System.out.print("Enter quantity:");
                            int quantity = scanner.nextInt();
                            scanner.nextLine();

                            if(medicine != null){
                                thisPrescription.addMedicine(medicine, quantity);
                            }
                           
                        }
                        thisPrescription.calculateTotalCharge();
                        PrescriptionDao.savePrescriptionMedicines(id, thisPrescription.getPerscriptionMedicines());
                        PrescriptionDao.updatePrescription(thisPrescription,id);
                        System.out.println("Press Enter to return to menu...");
                        scanner.nextLine(); 
                    }  
                    break;
                case 3:
                    //bookAppointment()
                    System.out.println("==========Book Appointments==========");
                    System.out.print("Enter patient name:");
                    String ap_patientname = scanner.nextLine();
                    Patient ap_selectedPatient = (Patient)UserDao.fetchUser(null, null, null, ap_patientname);
                    if(ap_selectedPatient != null){
                        System.out.print("Enter Appointment date (dd-mm-yyyy):");
                        String date = scanner.nextLine();
                        LocalDate selecteddate = BookingSystem.isvalidDate(date);

                        System.out.println("Doctors working time < 9:00-11:00 > and < 14:00-17:00 >");
                        System.out.print("Enter Appointment time (hh:mm):");
                        String time = scanner.nextLine();
                        LocalTime selectedtime = BookingSystem.isValidTime(time);

                        System.out.print("Enter duration:");
                        int durationMinutes = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        Duration duration = Duration.ofMinutes(durationMinutes);
                    
                        if(selecteddate != null && selectedtime != null){
                            LocalDateTime apptime = selecteddate.atTime(selectedtime);
                            BookingSystem.bookAppointment(doctor, ap_selectedPatient, apptime, duration);
                        }
                        System.out.println("appointment book successful");

                        System.out.println("Press Enter to return to menu...");
                        scanner.nextLine(); 
                    }else{
                        System.out.println("Booking failed. Invalid doctor or patient.");
                    }
                    break;
                case 4:
                    //update appointment()
                    System.out.println("==========Update Appointments==========");
                    List<Appointment> doctorAppointment = doctor.getAppointments();
                    for(int i = 0 ;i< doctorAppointment.size();i++){
                        System.out.println("--"+i+1+"--");
                        System.out.println(doctorAppointment.get(i));
                    }
                    System.out.print("Choose one appointment:");
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    if(option > 0 && option <= doctorAppointment.size()){
                        System.out.println("1. mark as completed");
                        System.out.println("2. mark as Rescheduled");
                        System.out.println("3. mark as canceled");
                        System.out.print("choose an option:");
                        int op = scanner.nextInt();
                        scanner.nextLine();
                        switch (op){
                            case 1:
                                doctorAppointment.get(option-1).markAsCompleted();
                                break;
                            case 2:
                                doctorAppointment.get(option-1).markAsRescheduled();
                                break;
                            case 3:
                                doctorAppointment.get(option-1).markAsCancelled();
                                break;
                            default:
                                System.out.println("Invalid option...");
                                continue;
                        }
                        AppointmentDao.updateAppointmentStatus(doctorAppointment.get(option-1).getAppointmentId(), doctorAppointment.get(option-1).getStatus());
                        System.out.println("Update successful");
                    }
                    break;
                case 5:
                    DoctorSetting(scanner, doctor);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }
    //Doctor managment
    public static void DoctorMangement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("=======================================");
            System.out.println("============Doctor Management==========");
            System.out.println("=======================================");
            System.out.println("1. View All Doctor");
            System.out.println("2. Search Doctor");
            System.out.println("3. Register Doctor");
            System.out.println("4. Update Doctor");//base on id
            System.out.println("5. Delete Doctor");
            System.out.println("6. Exit");
            System.out.print("Choose an option:");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            clearConsole();
            switch (choice) {
                case 1:
                    System.out.println("==========All Doctor==========");
                    List<Doctor> doctors = DoctorDao.fetchAllDoctor();
                    for (Doctor doctor : doctors) {
                        System.out.println(doctor);//display all doctor
                    }
                    System.out.println("\nPress Enter to return to menu...");
                     scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("==========Search Doctor==========");
                    System.out.print("Enter Doctor id:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    //fetchdoctor
                    if(DoctorDao.getDoctorById(id)!=null){
                        System.out.println(DoctorDao.getDoctorById(id));
                    }else{
                        System.out.println("Doctor not found!");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    System.out.println("==========Register Doctor==========");
                    registerDoctor( scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Update Doctor==========");
                    updateDoctors( scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    System.out.println("==========Delete Doctor==========");
                    System.out.print("Enter Doctor id:");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();
                    if(UserDao.deleteUserById(idToDelete)){
                        System.out.println("Delete succesfull");
                    }else{
                        System.out.println("fail to delete");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 6:
                    return;
                default:
                System.out.println("Invalid option!");
                    break;
            }

        }
    }
    //patient management 
    public static void PatientMangement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("========================================");
            System.out.println("============Patient Management==========");
            System.out.println("========================================");
            System.out.println("1. View All Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Register Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Exit");
            System.out.print("Choose an option:");

            int option = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (option) {
                case 1:
                    System.out.println("==========All Patients==========");
                    List<Patient> patients = PatientDao.getAllPatients();
                    for (Patient patient : patients) {
                        System.out.println(patient);
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("==========Search Patient==========");
                    System.out.print("Enter Patient id:");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    if(PatientDao.getPatientById(id)!=null){
                        System.out.println(PatientDao.getPatientById(id));
                    }else{
                        System.out.println("Patients not found!");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    System.out.println("==========Register new Patients==========");
                    registerPatient( scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Delete Patients==========");
                    System.out.print("Enter Patient id:");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();

                    if(UserDao.deleteUserById(idToDelete)){
                        System.out.println("Delete succesfull");
                    }else{
                        System.out.println("fail to delete");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                System.out.println("Exit...");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }
    //reciptionist manament not exist yet
    public static void ReceptionistManagement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("========================================");
            System.out.println("=========Receptionist Management========");
            System.out.println("========================================");
            System.out.println("1. View All Receptionist");
            System.out.println("2. Search Receptionist");
            System.out.println("3. Register Receptionist");
            System.out.println("4. Delete Receptionist");
            System.out.println("5. Exit");
            System.out.print("Choose an option:");
            int opt = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (opt) {
                case 1:
                    //view all receptionist

                    break;
                case 2:
                    //search receptionist

                    break;
                case 3:
                    System.out.println("==========Register Receptionist==========");
                    registerReceptionist( scanner);
                    break;
                case 4:
                    //delete recepionsit
                    System.out.println("==========Delete Receptionist==========");
                    System.out.print("Enter Receptionsit id:");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();

                    if(UserDao.deleteUserById(idToDelete)){
                        System.out.println("Delete succesfull");
                    }else{
                        System.out.println("fail to delete");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    return;
                default:
                System.out.println("Invalid option...");
                    
            }
        }
    }
    //appointment management
    public static void AppointmentManagement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("============================================");
            System.out.println("============Appointment Management==========");
            System.out.println("============================================");
            System.out.println("1. View All appointment:");
            System.out.println("2. Edit Appointment status:");
            System.out.println("3. Book an appointment:");
            System.out.println("4. Search Appointment:");
            System.out.println("5. Exit:");
            System.out.print("Choose an option:");

            int option = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (option) {
                case 1:
                    System.out.println("==========All Appointments==========");
                    List<Appointment> appointments = AppointmentDao.getAllAppointments();
                     if (appointments.isEmpty()) {
                    System.out.println("No appointments found.");
                    } else {
                        for (Appointment appointment : appointments) {
                            System.out.println(appointment);
                        }
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("==========Update Appointment's status==========");
                    System.out.print("Enter appointment id:");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Appointment selectedAppointment = AppointmentDao.getAppointmentById(id);
                    if(selectedAppointment != null){
                        System.out.println("1. mark as completed");
                        System.out.println("2. mark as Rescheduled");
                        System.out.println("3. mark as canceled");
                        System.out.print("choose an option:");
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice) {
                            case 1:
                                selectedAppointment.markAsCompleted();
                                break;
                            case 2:
                                selectedAppointment.markAsRescheduled();
                                break;
                            case 3:
                                selectedAppointment.markAsCancelled();
                                break;
                            default:
                                System.out.println("Invalid option...");
                                continue;
                        }
                        AppointmentDao.updateAppointmentStatus(selectedAppointment.getAppointmentId(), selectedAppointment.getStatus());
                        System.out.println("Update successful");
                    }else{
                        System.out.println("Appointment not found.");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    System.out.println("==========Book Appointments==========");
                    System.out.print("Enter patient name:");
                    String patientname = scanner.nextLine();
                    Patient selectedPatient = (Patient)UserDao.fetchUser(null, null, null, patientname);

                    System.out.print("Enter doctor name: ");
                    String doctorname = scanner.nextLine();
                    Doctor selectedDoctor = (Doctor)UserDao.fetchUser(null, null, null, doctorname);

                    if(selectedDoctor != null && selectedPatient!= null){
                        System.out.print("Enter Appointment date (dd-mm-yyyy) :");
                        String date = scanner.nextLine();
                        LocalDate selecteddate = BookingSystem.isvalidDate(date);

                        System.out.print("Enter Appointment time (hh:mm)");
                        String time = scanner.nextLine();
                        LocalTime selectedtime = BookingSystem.isValidTime(time);

                        System.out.print("Enter duration:");
                        int durationMinutes = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        Duration duration = Duration.ofMinutes(durationMinutes);
                    
                        if(selecteddate != null && selectedtime != null){
                            LocalDateTime apptime = selecteddate.atTime(selectedtime);
                            BookingSystem.bookAppointment(selectedDoctor, selectedPatient, apptime, duration);
                        }
                    }else{
                        System.out.println("Booking failed. Invalid doctor or patient.");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Search Appointments==========");
                    System.out.println("1. search by patient id");
                    System.out.println("2. search by appointment id");
                    System.out.print("Enter an option:");
                    int searchOpt = scanner.nextInt();
                    scanner.nextLine();

                    List<Appointment> selecAppointment = new ArrayList<>();

                        if(searchOpt == 1){
                            System.out.println("Enter patient id:");
                            int p_id = scanner.nextInt();
                            selecAppointment = AppointmentDao.getAppointmentsByPatientId(p_id);

                        }else if(searchOpt == 2){
                            System.out.println("Enter Appointemnt id:");
                            int a_id = scanner.nextInt();
                            Appointment appointment = AppointmentDao.getAppointmentById(a_id);
                            if(appointment != null){
                                selecAppointment.add(appointment);
                            }

                        }else{
                            System.out.println("Invalid option...");
                            continue;
                        }
                        if (selecAppointment.isEmpty()) {
                            System.out.println("No appointments found.");
                        } else {
                            for (Appointment appointment : selecAppointment) {
                                System.out.println(appointment);
                            }
                        }
                        System.out.println("\nPress Enter to return to menu...");
                        scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option...");
                    break;
            }
        }
    }
    //inventory management
    public static void InventoryManagement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("==========================================");
            System.out.println("============Inventory Management==========");
            System.out.println("==========================================");
            System.out.println("1. View All Inventory");
            System.out.println("2. Update stock");
            System.out.println("3. search for Item");
            System.out.println("4. Remove Item");
            System.out.println("5. check Item quantity");
            System.out.println("6. Exit:");
            System.out.print("Choose an option:");

            int option = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (option) {
                case 1:
                    System.out.println("==========Total Item=========");
                    List<Inventory> inventories = InventoryDao.getAllInventory();

                    if(inventories.isEmpty()){
                       System.out.println("No existing inventory...");
                    }else{
                        for (Inventory inventory : inventories) {
                            System.out.println(inventory);
                        }
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("==========Update Stock=========");
                    System.out.print("Enter Item ID:");
                    int itemId = scanner.nextInt();
                    scanner.nextLine();
                    LocalDate date = LocalDate.now();
                    System.out.print("Enter supplier name:");
                    String supp = scanner.nextLine();
                    System.out.print("Enter quantity:");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    if(InventoryDao.addStock(itemId, quantity, supp, date)){
                        System.out.println("Add to stock successful...");
                    }else{
                        System.out.println("Fail to add pleae try again...");
                    };
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    System.out.println("==========Search Item=========");
                    System.out.print("Enter Item ID:");
                    itemId = scanner.nextInt();
                    scanner.nextLine();

                    Inventory item = InventoryDao.getInventoryByMedicineId(itemId);
                    if(item != null){
                        System.out.println(item);
                    }else{
                        System.out.println("Item not found...");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Remove Item=========");
                        System.out.print("Enter Item ID:");
                        itemId = scanner.nextInt();
                        scanner.nextLine();

                        InventoryDao.removeItem(itemId);
                        System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    System.out.println("==========Check Item Quantity=========");
                        System.out.print("Enter Item ID:");
                        itemId = scanner.nextInt();
                        scanner.nextLine();

                        int thisQuantity = InventoryDao.getStockQuantity(itemId);
                        System.out.println("There is "+ thisQuantity + " left");
                        System.out.println("\nPress Enter to return to menu...");
                        scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option...");
                    continue;
            }
        }
    }
    //medicine management
    public static void MedicineManagement( Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("=========================================");
            System.out.println("============Medicine Management==========");
            System.out.println("=========================================");
            System.out.println("1. View All Medicine");
            System.out.println("2. search medicine");
            System.out.println("3. Add medicine");
            System.out.println("4. Remove medicine");
            System.out.println("5. check medicine quantity");
            System.out.println("6. Exit:");
            System.out.print("Choose an option:");
            int option = scanner.nextInt();
            scanner.nextLine();
            
            clearConsole();
            switch (option) {
                case 1:
                    System.out.println("==========Total Medicines=========");
                    List<Medicine> medicines = MedicineDao.getAllMedicines();
                    if(medicines.isEmpty()){
                        System.out.println("No mendicines...");
                    }else{
                        for (Medicine medicine : medicines) {
                            System.out.println(medicine);//don't forget tostring method
                        }
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("==========Search Medicines=========");   
                    System.out.print("Enter medicine ID:");
                    int medId = scanner.nextInt();
                    scanner.nextLine();

                    Medicine thismMedicine = MedicineDao.searchMedicineById(medId);
                    if(thismMedicine != null){
                        System.out.println(thismMedicine);
                    }else{
                        System.out.println("Medicines not found...");
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    System.out.println("==========Add Medicines=========");
                    Medicine newMedicine = null;
                    System.out.print("Enter medicine name:"); 
                    String name = scanner.nextLine();
                    System.out.print("Enter medicine dosage:");
                    String dosage = scanner.nextLine();
                    System.out.print("Enter medicine price:");
                    Double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter medicine description:");
                    String desc = scanner.nextLine();
                    System.out.print("Enter medicine expiry date:");
                    String dateString = scanner.nextLine();
                    LocalDate date = LocalDate.parse(dateString);

                    newMedicine = new Medicine(name, dosage,price, desc, date);
                    int thisId = MedicineDao.addMedicine(newMedicine);
                    if(thisId != -1){
                        System.out.print("Enter quantity:");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter suplier:");
                        String supp = scanner.nextLine();
                        LocalDate now = LocalDate.now();
                        if(InventoryDao.addStock(thisId, quantity, supp, now)){
                            System.out.println("Add successful...");
                        }else{
                            System.out.println("fail to add...");
                        };
                    }
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Remove Medicines=========");
                    System.out.print("Enter Medicine ID:");
                    int removeID = scanner.nextInt();
                    scanner.nextLine();
                    MedicineDao.removeMedicineById(removeID);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    System.out.println("==========Check Medicines Quantity=========");
                    System.out.print("Enter Medicine ID:");
                        medId = scanner.nextInt();
                        scanner.nextLine();

                        int thisQuantity = InventoryDao.getStockQuantity(medId);
                        System.out.println("There is "+ thisQuantity + " left");
                        System.out.println("\nPress Enter to return to menu...");
                        scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option...");
                    continue;
            }
        }
    }
    //generate report
    public static void FinancialReport(Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("       Financial Reports         ");
            System.out.println("=================================");
            System.out.println("1. View Total Income");
            System.out.println("2. View Income by Date ");
            System.out.println("3. View Today Income");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int opt = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            switch (opt) {
                case 1:
                    BillingDao.totalIncome();
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:

                    BillingDao.viewIncomeByDateRange(scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;

                case 3:
                    BillingDao.getTodayIncome();
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                return;

                default:
                System.out.println("In valid option...");
            }
        }
    }
    //make billing
    public static void makeBilling(Scanner scanner){
        clearConsole();
        System.out.println("=====================================");
        System.out.println("==============Billing================");
        System.out.println("=====================================");
        System.out.print("Enter Patient ID:");
        int patient_id = scanner.nextInt();
        scanner.nextLine();

        Prescription patiePrescription = PrescriptionDao.getLatestPrescriptionByPatientId(patient_id);
        if(patiePrescription != null){
            System.out.println("Choose Payment Method:");
            System.out.println("1. cash");
            System.out.println("2. QR");
            System.out.print("Enter your option:");
            int opt = scanner.nextInt();
            scanner.nextLine();

            String method = "cash";
            if(opt == 2 ){
                method = "QR";
            }

            System.out.println("Patient with ID "+patient_id+" Total charge : $"+patiePrescription.getTotalCharge());
            System.out.print("Enter patient Paid amount:");
            double paid_amount = scanner.nextDouble();
            scanner.nextLine();

            Billing patBilling = new Billing(patient_id, patiePrescription, paid_amount, BillingStatus.PAID, method);
            int newID = BillingDao.saveBilling(patBilling);
            if(newID != -1){
                String thisInvoice = patBilling.generateInvoice();
                System.out.println(thisInvoice);
            }else{
                System.out.println("fail to make payment...");
            }
        }
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine(); // Wait for user input before clearing the screen
    }
    //  Register Doctor (Only Admin can do this)
    public static void registerDoctor( Scanner scanner) {
      //  System.out.print("Enter doctor username: ");
        String username = InputUsername(scanner);
       // System.out.print("Enter doctor password: ");
        String password = InputPassword(scanner);
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
       // System.out.print("Enter email: ");
        String email = InputEmail(scanner);
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();

        if (UserDao.signupDoctor(username, password, firstName, lastName, email, phone, specialization)) {
            System.out.println("Doctor registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    //setting
    public static void DoctorSetting(Scanner scanner, Doctor doctor){
       
        while (true) {
            clearConsole();
            System.out.println("======================");
            System.out.println("=======Setting========");
            System.out.println("======================");
            System.out.println("1. View my profile");
            System.out.println("2. Change password");
            System.out.println("3. Exit");
            System.out.print("Choose one option:");
            int opt = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (opt) {
                case 1:
                    System.out.println("===========profile===========");
                    System.out.println(doctor);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("=========change password========");
                    System.out.print("Enter username:");
                    String username = scanner.nextLine();
                    UserDao.changePassword(username, scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    //Admin setting
    public static void AdminSettig(Scanner scanner, Admin admin){
        
        while (true) {
            clearConsole();
            System.out.println("======================");
            System.out.println("=======Setting========");
            System.out.println("======================");
            System.out.println("1. View my profile");
            System.out.println("2. Change password");
            System.out.println("3. Exit");
            System.out.print("Choose one option:");
            int opt = scanner.nextInt();
            scanner.nextLine();

            clearConsole();
            switch (opt) {
                case 1:
                    System.out.println("===========profile===========");
                    System.out.println(admin);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 2:
                    System.out.println("=========change password========");
                    System.out.print("Enter username:");
                    String username = scanner.nextLine();
                    UserDao.changePassword(username, scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    //updateDoctor
    public static void updateDoctors( Scanner scanner){
        System.out.print("Enter Doctor ID to update:");
        int docID = scanner.nextInt();
        scanner.nextLine();

        Doctor doctorToUpdate = DoctorDao.getDoctorById(docID);
        while(doctorToUpdate != null){
            System.out.println("1. Update firstname");
            System.out.println("2. Update email");
            System.out.println("3. Update phone number");
            System.out.println("4. Exit");
            System.out.print("Enter your option:");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.print("Enter new firstname");
                    String newFirstname = scanner.nextLine();
                    System.out.print("Enter new lastname:");
                    String newLastname = scanner.nextLine();
                    doctorToUpdate.setFirstName(newFirstname);
                    doctorToUpdate.setLastName(newLastname);
                    System.out.println("update successful...");
                    break;
                case 2:
                    System.out.print("Enter new email:");
                    String newEmial = scanner.nextLine();
                    doctorToUpdate.setEmail(newEmial);
                    System.out.println("update successful...");
                    break;
                case 3:
                    System.out.print("Enter new phone number:");
                    String newphone = scanner.nextLine();
                    doctorToUpdate.setPhone(newphone);
                    System.out.println("update successful...");
                    break;
                case 4:
                    DoctorDao.updateDoctor(doctorToUpdate);
                    System.out.println("Exit...");
                    return;
                default:
                    break;
            }
            
        }
        
    }

    //  Register Receptionist (Only Admin can do this)
    public static void registerReceptionist( Scanner scanner) {
       // System.out.print("Enter Receptionist username: ");
        String username = InputUsername(scanner);
       // System.out.print("Enter Receptionist password: ");
        String password = InputPassword(scanner);
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
      //  System.out.print("Enter email: ");
        String email = InputEmail(scanner);
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        if (UserDao.signupReceptionist(username, password, firstName, lastName, email, phone)) {
            System.out.println("Receptionist registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    // Register Patient (receptionist and admin)
    public static void registerPatient( Scanner scanner) {
      //  System.out.print("Enter patient username: ");
        String username = InputUsername(scanner);

       // System.out.print("Enter patient password: ");
        String password = InputPassword(scanner);
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        //System.out.print("Enter email: ");
        String email = InputEmail(scanner);
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        if (UserDao.signupPatient(username, password, firstName, lastName, email, phone, gender, dob, address)) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }
  
     //input username 
     public static String InputUsername(Scanner scanner){
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();

            try {
                // Check if the username is already taken
                if (UserDao.isUsernameTaken(username)) {
                    System.out.println("Username already exists. Please choose a different one.");
                    continue; // If username is taken, prompt user again
                } else {
                    return username;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occurred during registration. Please try again.");
            }
        }
    }

    //input password
    public static String InputPassword(Scanner scanner){
        String password;
        while (true) {
            System.out.print("Enter password (min 8 characters, 1 special character): ");
            password = scanner.nextLine();

            if (UserDao.isValidPassword(password)) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
                System.out.println("Password is valid!");
                return hashedPassword;  // Return the hashed password
                
            } else {
                System.out.println(" Password must be at least 8 characters long and contain at least 1 special character.");
            }
        }
    }
    //input email 
    public static String InputEmail(Scanner scanner){
        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();

            try {
                // Check if the email is already taken
                if (UserDao.isEmailTaken(email)) {
                    System.out.println("Email already exists. Please choose a different email.");
                    continue; // If email is taken, prompt user again
                } else {
                    System.out.println("Email is available!");
                    return email; // Exit loop when a unique email is entered
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" An error occurred during registration. Please try again.");
            }
        }
    }

}
