import util.base.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.*;
import database.*;
import javaCode.*;
public class App {

    public static void main(String[] args) {
        // clinic management main system
        Scanner scanner = new Scanner(System.in);
        UserDao userDAO = new UserDao(); // Create an instance of UserDAo

        Person user = null;
        System.out.println("\n========================================");
        System.out.println("|  Welcome to Clinic Management System   |");
        System.out.println("==========================================");
        
                while (true) {
                    System.out.println("\n1. Login");
                    System.out.println("2. Close");
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

                        if (user == null) {
                            System.out.println("Invalid username or password. Please try again.");
                            continue;
                        }
                        loadDashboard(user, userDAO, scanner);
                        return;
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
                adminMenu(userDAO, scanner);
                scanner.close();
                break;
            case "Doctor":
                Doctor doctor = (Doctor)user;
                doctorMenu(scanner,doctor);
                scanner.close();
                break;
            case "Patient":
                System.out.println("To be continue...");
                break;
            case "Receptionist1":
                receptionistMenu(userDAO, scanner);
                scanner.close();
                break;
            case "Receptionist2":
                receptionistMenu2(userDAO, scanner);
                scanner.close();
                break;
            default:
                System.out.println("No specific user role!");
                scanner.close();
                break;
        }
    }

    // =============Admin Menu
    public static void adminMenu(UserDao userDAO, Scanner scanner) {
    
        while (true) {
            clearConsole();//clear the console
            System.out.println("\n=================================");
            System.out.println("      Welcome to Admin Menu      ");
            System.out.println("=================================");
            System.out.println("1. Doctor management");
            System.out.println("2. Patient management");
            System.out.println("3. Generate total income");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    DoctorMangement(userDAO, scanner);
                    break;
                case 2:
                    PatientMangement(userDAO, scanner);
                    break;
                case 3:
                    generateReport(userDAO, scanner);//not complete yet
                    break;
                case 4:
                    return;
                default:
                    System.out.println("invalid option!");
                    continue;
            }
        }
    }

    // ============Receptionist Menu --1
    public static void receptionistMenu(UserDao userDAO, Scanner scanner) {
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("   Welcome to Receptionist Menu  ");
            System.out.println("=================================");
            System.out.println("1. Patient management");
            System.out.println("2. Appointment management");
            System.out.println("3. Make Billing");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    PatientMangement(userDAO, scanner);
                    break;
                case 2:
                    AppointmentManagement(userDAO, scanner);
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
    // Receptionist Menu --2
    public static void receptionistMenu2(UserDao userDAO, Scanner scanner) {
        while (true) {
            clearConsole();
            System.out.println("\n=================================");
            System.out.println("  Welcome to Receptionist Menu   ");
            System.out.println("=================================");
            System.out.println("1. Inventory Management");
            System.out.println("2. Medicines Management");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            clearConsole();
            switch (choice) {
                case 1:
                    InventoryManagement(userDAO, scanner);
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
            System.out.println("5. Exit");
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
                       
                        PrescriptionDao.savePrescriptionMedicines(id, thisPrescription.getPerscriptionMedicines());
                        PrescriptionDao.updatePrescription(thisPrescription);
                        System.out.println("Perscription has been created...");
                        System.out.println("\nPress Enter to return to menu...");
                        scanner.nextLine(); // Wait for user input before clearing the screen
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
                    return;
                default:
                    System.out.println("Invalid option please choose again...");
                    continue;
            }
        }
    }
    //Doctor managment
    public static void DoctorMangement(UserDao userDAO, Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("============Doctor Management==========");
            System.out.println("1. View All Doctor:");
            System.out.println("2. Search Doctor");
            System.out.println("3. Register Doctor");
            System.out.println("4. Update Doctor");//base on id
            System.out.println("5. Delete Doctor");
            System.out.println("6. Exit:");
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
                    registerDoctor(userDAO, scanner);
                    System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 4:
                    System.out.println("==========Update Doctor==========");
                    updateDoctors(userDAO, scanner);
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
    public static void PatientMangement(UserDao userDAO, Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("============Patient Management==========");
            System.out.println("1. View All Patient:");
            System.out.println("2. Search Patient:");
            System.out.println("3. Register Patient:");
            System.out.println("4. Delete Patient:");
            System.out.println("5. Exit:");
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
                    registerPatient(userDAO, scanner);
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
    public static void ReceptionistManagement(UserDao userDAO, Scanner scanner){

    }
    //appointment management
    public static void AppointmentManagement(UserDao userDAO, Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("============Appointment Management==========");
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
    public static void InventoryManagement(UserDao userDAO, Scanner scanner){
        while (true) {
            clearConsole();
            System.out.println("============Inventory Management==========");
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
                        System.out.println("Enter Item ID:");
                        itemId = scanner.nextInt();
                        scanner.nextLine();

                        InventoryDao.removeItem(itemId);
                        System.out.println("\nPress Enter to return to menu...");
                    scanner.nextLine(); // Wait for user input before clearing the screen
                    break;
                case 5:
                    System.out.println("==========Check Item Quantity=========");
                        System.out.println("Enter Item ID:");
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
            System.out.println("============Medicine Management==========");
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
                    System.out.println("Enter Medicine ID:");
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
    public static void generateReport(UserDao userDAO, Scanner scanner){
    }
    //make billing
    public static void makeBilling(Scanner scanner){
        clearConsole();
        System.out.print("Enter Patient ID:");
        int patient_id = scanner.nextInt();
        scanner.nextLine();

        Prescription patiePrescription = PrescriptionDao.getLatestPrescriptionByPatientId(patient_id);
        if(patiePrescription != null){
            System.out.print("Choose Payment Method:");
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
                patBilling.generateInvoice();
            }else{
                System.out.println("fail to make payment...");
            }
        }
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine(); // Wait for user input before clearing the screen
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

    //updateDoctor
    public static void updateDoctors(UserDao userDAO, Scanner scanner){
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
