package javaCode;

import java.time.Duration;
import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int patient_id;     
    private int doctor_id;      
    private LocalDateTime appointmentDate;
    private Duration duration;
    private String status;

    public Appointment(int appointmentId, int patient_id, int doctor_id, LocalDateTime appointmentDate,Duration duration, String status) {
        this.appointmentId = appointmentId;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.appointmentDate = appointmentDate;
        this.duration = duration;
        this.status = status;
    }
    // Constructor when creating a new appointment (without ID)
    public Appointment(int patient_id, int doctor_id, LocalDateTime appointmentDate, Duration duration, String status) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.appointmentDate = appointmentDate;
        this.duration = duration;
        this.status = status;
    }

    //getters
    public int getAppointmentId() {
        return appointmentId;
    }
    public int getPatientId() {
        return this.patient_id;
    }
    public int getDoctoId() {
        return this.doctor_id;
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

    @Override
    public String toString() {
         return "===========================\n" +
        "Appointment ID: " + appointmentId + "\n" +
        "Doctor ID: " + doctor_id +"\n" +
        "Patient ID: " + patient_id + "\n" +
        "Date: " + appointmentDate + "\n" +
        "Duration: " + duration + "\n" +
        "Status: " + status + "\n";
    }


    
}
