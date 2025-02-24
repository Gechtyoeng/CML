package javaCode;

import java.util.List;
import java.util.ArrayList;
import util.base.Person;

public class Patient extends Person {

    private String gender;
    private String dateOfBirth;
    private String address;
    private List<Appointment> appointments; //store all appointments of patient

    public Patient(String username, String password, String firstName, String lastName, String email, String phone, String 
    gender, String dateOfBirth, String address) {
        super(username, password, firstName, lastName, email, phone);
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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
    public void addAppointment(Appointment appointment) {
        if(appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }
    public void showAppointments() {
        for (Appointment appointment : appointments) {
            appointment.displayAppointmentDetails();
        }
    }
}
