package javaCode;

import java.util.List;
import database.AppointmentDao;
import database.PatientDao;
import java.util.ArrayList;
import util.base.Person;

public class Patient extends Person {

    private String gender;
    private String dateOfBirth;
    private String address;

    //constructor
    public Patient(int id,String username, String password, String firstName, String lastName, String email, String phone, String 
    gender, String dateOfBirth, String address) {
        super(id,username, password, firstName, lastName, email, phone,"Patient");
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
    @Override
    public String toString() {
    return "===========================\n" +
           "Patient ID: " + id + "\n" +
           "Name: " + firstName + " " + lastName + "\n" +
           "Gender: " + gender + "\n" +
           "Date of Birth: " + dateOfBirth + "\n" +
           "Email: " + email + "\n" +
           "Address: " + address + "\n" +
           "Phone Number: " + phone + "\n";
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
    //get patients by id
    public Patient getPatientById(){
        
       return PatientDao.getPatientById(id);
    }
    //get appointment by patient id
    public List<Appointment> getAppointments(){
        List<Appointment> appointments = AppointmentDao.getAppointmentsByPatientId(id);
        return (appointments != null) ? appointments : new ArrayList<>();

    }

     // Method to show all appointments
     public void showAppointments() {
        List<Appointment> appointments = getAppointments();
        if(appointments.isEmpty()){
            System.out.println("No appointment with patient "+ firstName +" "+lastName);
        }else{
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

}
