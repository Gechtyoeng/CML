package javaCode;

import java.time.Duration;
import java.time.LocalDateTime;

public class Appointment {
    private static int idcounter = 1;   //auto increment id
    private int appointmentId;
    private Patient patient;      // Corresponds to patient class
    private Doctor doctor;       // Corresponds to Docotr class
    private LocalDateTime appointmentDate;
    private Duration duration;
    private String status;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDate,Duration duration, String status) {
        this.appointmentId = idcounter++;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.duration = duration;
        this.status = "Scheduled";
    }

    //getters
    public int getAppointmentId() {
        return appointmentId;
    }
    public Patient getPatient() {
        return patient;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }
    public String getStatus() {
        return status;
    }
    public Duration getDuration(){
        return this.duration;
    }
    //setter
    public void setStatus(String status) {
        this.status = status;
    }
    //mark appointment as completed
    public void markAsCompleted() {
        this.status = "Completed";
    }
    //mark appointment as cancelled
    public void markAsCancelled() {
        this.status = "Cancelled";
    }
    //mark appointment as rescheduled
    public void markAsRescheduled() {
        this.status = "Rescheduled";
    }
    //display appointment details
    public void displayAppointmentDetails() {
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient: " + patient.getFirstName() + " " + patient.getLastName());
        System.out.println("Doctor: " + doctor.getFirstName() + " " + doctor.getLastName());
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Duration: " + duration);
        System.out.println("Status: " + status);
    }
}
