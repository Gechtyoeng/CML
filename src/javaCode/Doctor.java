package javaCode;

import java.util.List;
import java.util.ArrayList;
import util.base.Person;

public class Doctor extends Person{
    private String specialization;
    private List<Appointment> appointments;

    public Doctor(String username, String password, String firstName, String lastName, String email, String phone, String specialization) {
        super(username, password, firstName, lastName, email, phone);
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void displayDoctorDetails() {
        System.out.println("Doctor ID: " + id);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Specialization: " + specialization);
    }
   public List<Appointment> getAppointments() {
        return appointments;
    }
    public void addAppointment(Appointment appointment) {
        if(appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }
    public void removeAppointment(Appointment appointment) {
        if(appointments.contains(appointment)) {
            appointments.remove(appointment);
            System.out.println("Appointment removed successfully");
        }else {
            System.out.println("Appointment not found");
        }
    }
    
}
