package javaCode;

import java.util.List;
import java.util.ArrayList;
import util.base.Person;

public class Patient extends Person {

    private String gender;
    private String dateOfBirth;
    private String address;
    private List<Appointment> appointments; //store all appointments of patient
    private List<String> medicalHistory; // Store patient's medical records

    //constructor
    public Patient(int id,String username, String password, String firstName, String lastName, String email, String phone, String 
    gender, String dateOfBirth, String address) {
        super(id,username, password, firstName, lastName, email, phone,"Patient");
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.appointments = new ArrayList<>(); //initialize appointment lists
        this.medicalHistory = new ArrayList<>(); //initialize medical history
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public String getGender(){
        return this.gender;
    }
    //Method to display patient detail
    public void displayPatientDetails() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Date of Birth: " + dateOfBirth);
    }
   
    public void addAppointment(Appointment appointment) {
        if(appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }

     // Method to show all appointments
     public void showAppointments() {
        System.out.println("Appointments for " + firstName + " " + lastName + ":");
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : appointments) {
                appointment.displayAppointmentDetails();
            }
        }
    }
}
