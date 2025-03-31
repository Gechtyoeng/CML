package javaCode;

import java.util.*;
import database.AppointmentDao;
import database.DoctorDao;
import util.base.Person;

public class Doctor extends Person{
    private String specialization;

    public Doctor(int id,String username, String password, String firstName, String lastName,
         String email, String phone, String specialization) {

        super(id,username, password, firstName, lastName, email, phone,"Doctor");
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }
    @Override
    public String toString() {
    return "===========================\n" +
           "Doctor ID: " + id + "\n" +
           "Name: " + firstName + " " + lastName + "\n" +
           "Specialization: " + specialization + "\n" +
           "Email: " + email + "\n" +
           "Phone Number: " + phone + "\n";
    }
   
    //getter to get appointments of doctor 
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = AppointmentDao.getDoctorAppointments(this.id);
        return (appointments != null) ? appointments : new ArrayList<>();
    }

     // Fetch doctor details from the database
    public static Doctor getDoctorById(int doctorId) {
        return DoctorDao.getDoctorById(doctorId);
    }

    //method to view appointment from class Appointment
    public void viewAppointments() {
        List<Appointment> appointments = getAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for Dr. " + firstName + " " + lastName);
        } else {
            System.out.println("Appointments for Dr. " + firstName + " " + lastName + ":");
            for (Appointment appointment : appointments) {
                System.out.println(appointment); 
            }
        }
    }

    //doctor appointment
     public void addAppointment(Appointment appointment) {
        AppointmentDao.saveAppointment(appointment);
    }

   
}
