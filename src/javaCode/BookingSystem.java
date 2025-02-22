package javaCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BookingSystem {
    private Admin admin;
    BookingSystem(Admin admin){
        this.admin = admin;
    }
    public void bookAppointment(){
        Scanner scanner = new Scanner(System.in);
        //get patient by input patient id
        List<Patient> patients = admin.getPatients();
        System.out.println("Enter patient ID: ");
        int patientId = scanner.nextInt();
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
        //get doctor by list down the doctor and let patient select the doctor
        List<Doctor> doctors = admin.getDoctors();
        System.out.println("Select a doctor to book an appointment");
        for (Doctor doctor : doctors) {
            doctor.displayDoctorDetails();
        }
        System.out.println("Enter the doctor ID: ");
        int doctorId = scanner.nextInt();
        Doctor selectedDoctor = null;
        for (Doctor doctor : doctors) {
            if(doctor.getId() == doctorId) {
                selectedDoctor = doctor;
                break;
            }
        }
        if(selectedDoctor == null) {
            System.out.println("Doctor not found");
            return;
        } 
        //get appointment date
        System.out.println("Enter appointment date (yyyy-MM-dd): ");
        String dateInput = scanner.next();
        LocalDate appointmentDate = isvaliDate(dateInput);
        if(appointmentDate == null) {
            return;
        }
        //get appointment time
        System.out.println("Enter appointment time (HH:mm): ");
        String timeInput = scanner.next(); 
        LocalTime appointmentTime = isValiiTime(timeInput);
        //booking if all the input is valid
        if(selectedDoctor != null && appointmentDate != null && appointmentTime != null){
            if(isDoctorAvailiable(selectedDoctor, appointmentDate, appointmentTime)){
                Appointment appointment = new Appointment(selectedPatient, selectedDoctor, appointmentDate.atTime(appointmentTime), "Scheduled");
                selectedDoctor.addAppointment(appointment);
                selectedPatient.addAppointment(appointment);
                System.out.println("Appointment booked successfully");
            }
        }
    }

    public LocalDate isvaliDate(String dateInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate appointmentDate = LocalDate.parse(dateInput, formatter);

             // Check if the date is in the past
             if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Invalid date! You cannot book an appointment in the past.");
                return null;
            }
            //check is the date is given is valid for the given year and month
            if(appointmentDate.getDayOfMonth() > appointmentDate.lengthOfMonth() || 
               appointmentDate.getMonthValue() > 12 || appointmentDate.getMonthValue() < 1){
                System.out.println("Invalid date! The date you entered the wrong date.");
                return null;
            }
             return appointmentDate;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Please use yyyy-MM-dd.");
            return null;
        }
    }

    public LocalTime isValiiTime(String timeInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime appointmentTime = LocalTime.parse(timeInput, formatter);

            //check if the time is during working hours
            if(appointmentTime.isBefore(LocalTime.of(9, 0)) || appointmentTime.isAfter(LocalTime.of(11, 0)) ||
               appointmentTime.isBefore(LocalTime.of(14, 0)) || appointmentTime.isAfter(LocalTime.of(17, 0))){
                System.out.println("Invalid time! Doctors are only available between 9:00-11:00 and 14:00-17:00.");
                return null;
            }
            return appointmentTime;

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format! Please use HH:mm.");
            return null;
        }
    }
    
    public boolean isDoctorAvailiable(Doctor doctor, LocalDate appointmentDate, LocalTime timeInput){
        List<Appointment> appointments = doctor.getAppointments();
        for (Appointment appointment : appointments) {
            if(appointment.getAppointmentDate().toLocalDate().equals(appointmentDate) && 
               appointment.getAppointmentDate().toLocalTime().equals(timeInput)){
                System.out.println("Doctor is not available at this time.");
                return false;
            }
        }
        return true;
    }
}
