package javaCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
//this booking system can be used by admin and doctor
//admin can book a patient with any doctor while doctors can only book for themselves

import database.AppointmentDao;

public class BookingSystem {
    //booking for doctor role
    public void doctorBook(Doctor doctor){
        List<Patient> doctorPatients = doctor.getAppointments().stream()
                .map(Appointment::getPatient)
                .distinct() 
                .toList(); // Convert to List

    if (doctorPatients.isEmpty()) {
        System.out.println("You have no patients with existing appointments.");
        return;
    }
        bookAppointment(doctorPatients, null, doctor);
    }
    //booking for admin role
    public void adminBook(Admin admin){
        bookAppointment(admin.getPatients(), admin.getDoctors(), null);
    }
    //processing the booking
    private void bookAppointment(List<Patient> patients, List<Doctor> doctors, Doctor isDoctor){
        Scanner scanner = new Scanner(System.in);
        //get patient by input patient id
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        Patient selectedPatient = null;
        for (Patient patient : patients) {
            if(patient.getId() == patientId) {
                selectedPatient = patient;
                break;
            }
        }
        if(selectedPatient == null) {
            System.out.println("Patient not found");
            return;
        }
        //get doctor by list down the doctor and let patient select the doctor if admin
        Doctor selectedDoctor = null;
        // if doctors book for themselves let the selected doctor = this doctor
        if(isDoctor == null){
            System.out.println("Select a doctor:");
        for (Doctor doctor : doctors) {
            doctor.displayDoctorDetails();
        }

        System.out.print("Enter the doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        for (Doctor doctor : doctors) {
            if(doctor.getId() == doctorId) {
                selectedDoctor = doctor;
                break;
            }
        }
        if(selectedDoctor == null) {
            System.out.println("Doctor not found!");
            return;
        } 
        }else{
            selectedDoctor = isDoctor;
        }
        
        //get appointment date
        System.out.print("Enter appointment date (dd-MM-yyyy): ");
        String dateInput = scanner.next();
        LocalDate appointmentDate = isvalidDate(dateInput);
        if(appointmentDate == null) {
            return;
        }
        //get appointment time
        System.out.print("Enter appointment time (HH:mm): ");
        String timeInput = scanner.next(); 
        //get the duration
        System.out.print("Please enter your expected duration minutes:");
        int minutes = scanner.nextInt();
        Duration duration = Duration.ofMinutes(minutes);

        LocalTime appointmentTime = isValidTime(timeInput);
        //booking if all the input is valid
        if(selectedDoctor != null && appointmentDate != null && appointmentTime != null){
            if(isDoctorAvailiable(selectedDoctor, appointmentDate, appointmentTime, duration)){
                Appointment appointment = new Appointment(selectedPatient, selectedDoctor, appointmentDate.atTime(appointmentTime),duration, "Scheduled");
                //save appointment to database
                AppointmentDao.saveAppointment(appointment);
                //add appointment to doctor and patient list
                selectedDoctor.addAppointment(appointment);
                selectedPatient.addAppointment(appointment);

                System.out.println("Appointment booked successfully");
            }
        }
        scanner.close();
    }
//valid the date input format
    public LocalDate isvalidDate(String dateInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate appointmentDate = LocalDate.parse(dateInput, formatter);

             // Check if the date is in the past
            if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Invalid date! You entered the past date!");
                return null;
            }
            
             return appointmentDate;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Please use dd-MM-yyyy.");
            return null;
        }
    }

    public LocalTime isValidTime(String timeInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime appointmentTime = LocalTime.parse(timeInput, formatter);

            //check if the time is during working hours
            if (!((appointmentTime.isAfter(LocalTime.of(8, 59)) && appointmentTime.isBefore(LocalTime.of(11, 0))) ||
            (appointmentTime.isAfter(LocalTime.of(13, 59)) && appointmentTime.isBefore(LocalTime.of(17, 0))))) {
            System.out.println("Invalid time! Doctors are only available between 9:00-11:00 and 14:00-17:00.");
            return null;
        }
            return appointmentTime;

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format! Please use HH:mm.");
            return null;
        }
    }
    
    public boolean isDoctorAvailiable(Doctor doctor, LocalDate appointmentDate, LocalTime startTime, Duration duration){
        List<Appointment> appointments = doctor.getAppointments();
        LocalTime endTime = startTime.plus(duration);
        for (Appointment appointment : appointments) {
            LocalDateTime existingStart = appointment.getAppointmentDate();
            LocalDateTime existingEnd = appointment.getAppointmentDate().plus(appointment.getDuration());

            LocalDateTime currentStart = appointmentDate.atTime(startTime);
            LocalDateTime currentEnd = appointmentDate.atTime(endTime);
            if (currentStart.isBefore(existingEnd) && currentEnd.isAfter(existingStart)) {
                return false;
            }
        }
        return true;
    }
   
}
