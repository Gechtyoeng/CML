package javaCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import util.base.Person;

public class Admin extends Person {
    private LocalDate lastLogin;
    private List<Doctor> doctors = new ArrayList<>();//store all doctors
    private List<Patient> patients = new ArrayList<>();//store all patients
    
    public Admin(String username, String password, String firstName, String lastName,
                    String email, String phone, String role) {
        super(username, password, firstName, lastName, email, phone, role);
        this.lastLogin = LocalDate.now();
    }
    public Admin(String username, String password) {
        super(username, password,"Admin");
    }
    
    @Override
    public boolean signUp() {
        if(this.username.equals(username)) {
            System.out.println("Username already exists");
            return false;
        }
        return true;
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
