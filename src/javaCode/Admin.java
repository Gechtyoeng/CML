package javaCode;
import java.util.*;
import util.base.Person;

public class Admin extends Person {
    private List<Doctor> doctors = new ArrayList<>();//store all doctors
    private List<Patient> patients = new ArrayList<>();//store all patients
    
    public Admin(int id,String username, String password, String firstName, String lastName,
                    String email, String phone) {
        super(id,username, password, firstName, lastName, email, phone, "Admin");
    }

    @Override
    public boolean signUp() {
        if(this.username.equals(username)) {
            System.out.println("Username already exists");
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "Admin Profile: \n" +
            "ID: " + getId() + "\n" + 
            "Username: " + getUsername() + "\n" + 
            "First Name: " + getFirstName() + "\n" + 
            "Last Name: " + getLastName() + "\n" + 
            "Email: " + getEmail() + "\n" + 
            "Phone: " + getPhone() + "\n" + 
            "Role: Admin";
    }
    //=============doctor management
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void removeDoctor(Doctor doctor) {
      if(doctors.contains(doctor)) {
          doctors.remove(doctor);
          System.out.println("Doctor removed successfully");
        }else {
            System.out.println("Doctor not found");
        }
    }
    
    public List<Doctor> getDoctors() {
        return doctors;
    }
    //=============patient management
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(Patient patient) {
        if(patients.contains(patient)) {
            patients.remove(patient);
            System.out.println("Patient removed successfully");
        }else {
            System.out.println("Patient not found");
        }
    }
    
    public List<Patient> getPatients() {
        return patients;
    }

}
