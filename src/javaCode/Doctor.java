package javaCode;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import util.base.Person;

public class Doctor extends Person{
    private String specialization;
    private List<Appointment> appointments; 
    //private Map<Integer, String> patientList = new HashMap<>(); // Map of Patient ID -> Patient Name

    public Doctor(int id,String username, String password, String firstName, String lastName,
         String email, String phone, String specialization) {

        super(id,username, password, firstName, lastName, email, phone,"Doctor");
        this.specialization = specialization;
        this.appointments = new ArrayList<>(); //initialize appointment lists
    }

    public String getSpecialization() {
        return specialization;
    }

    //Method to display doctor detail
    public void displayDoctorDetails() {
        System.out.println("===========================");
        System.out.println("Doctor ID: " + id);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Specialization: " + specialization);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phone);
    }

    // Method to View Doctor's Schedule
    // public void viewDoctorSchedule() {
    //     System.out.println("Schedule for Dr. " + firstName + " " + lastName + " (" + specialization + "):");
    //     for (Map.Entry<String, String> entry : schedule.entrySet()) {
    //         System.out.println("Date: " + entry.getKey() + " Time: " + entry.getValue());
    //     }
    // }

    //getter to get appointments of doctor 
    public List<Appointment> getAppointments() {
        return this.appointments;
    }

    //method to view appointment from class Appointment
    public void viewAppointments() {
        System.out.println("Appointments for Dr. " + firstName + " " + lastName + " (" + specialization + "):");
        for (Appointment appointment : appointments) {
            appointment.displayAppointmentDetails();
        }
    }

    //Method to add appointment
    public void addAppointment(Appointment appointment) {
        if(appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
        System.out.println("Appointment created successfully for Dr. " + firstName + " " + lastName);
    }

    // Method to remove appointment
    public void removeAppointment(Appointment appointment) {
        if(appointments.contains(appointment)) {
            appointments.remove(appointment);
            System.out.println("Appointment removed successfully");
        }else {
            System.out.println("Appointment not found");
        }
    }

    // public void viewPatientList() {
    //     System.out.println("Patient List for Dr. " + firstName + " " + lastName + " (" + specialization + "):");
    //     for (Map.Entry<Integer, String> entry : patientList.entrySet()) {
    //         System.out.println("Patient ID: " + entry.getKey() + " Name: " + entry.getValue());
    //     }
    // }
    
}
