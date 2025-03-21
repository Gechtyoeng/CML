package javaCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
//this booking system can be used by admin and doctor
//admin can book a patient with any doctor while doctors can only book for themselves

import database.AppointmentDao;
import database.DoctorDao;
import database.UserDao;
import util.base.Person;

public class BookingSystem {
  
    public LocalDate isvalidDate(String dateInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate appointmentDate = LocalDate.parse(dateInput, formatter);

             // Check if the date is in the past
            if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Invalid date! You entered the past date!");
                return null;
            }
            
             return appointmentDate;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Please use dd-MM-yyyy.");
            return null;
        }
    }

    public LocalTime isValidTime(String timeInput){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime appointmentTime = LocalTime.parse(timeInput, formatter);

            //check if the time is during working hours
            if (!((appointmentTime.isAfter(LocalTime.of(8, 59)) && appointmentTime.isBefore(LocalTime.of(11, 0))) ||
            (appointmentTime.isAfter(LocalTime.of(13, 59)) && appointmentTime.isBefore(LocalTime.of(17, 0))))) {
            System.out.println("Invalid time! Doctors are only available between 9:00-11:00 and 14:00-17:00.");
            return null;
        }
            return appointmentTime;

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format! Please use HH:mm.");
            return null;
        }
    }
    
    public static boolean isDoctorAvailiable(Doctor doctor, LocalDateTime appointmentDate, Duration duration){
        List<Appointment> appointments = doctor.getAppointments();//check to make sure this appointment is recently
        LocalDateTime currentEnd = appointmentDate.plus(duration);
        LocalDateTime currentStart = appointmentDate;

        for (Appointment appointment : appointments) {
            LocalDateTime existingStart = appointment.getAppointmentDate();
            LocalDateTime existingEnd = appointment.getAppointmentDate().plus(appointment.getDuration());

            if (currentStart.isBefore(existingEnd) && currentEnd.isAfter(existingStart)
            || currentStart.isEqual(existingEnd) || currentEnd.isEqual(existingStart)) {
                return false;
            }
        }
        return true;
    }

    public static String bookAppointment(int doctorId, int patientId, LocalDateTime appointmentDate, Duration duration, String role){
        if (!role.equals("Doctor") && !role.equals("Receptionist") && !role.equals("Admin")) {
            return "You do not have permission to book appointment";
        }
          // Check if the doctor is available for the given time
          Doctor doctor =  DoctorDao.getDoctorById(doctorId);
          try {
            if (!isDoctorAvailiable(doctor, appointmentDate, duration)) {

                return "Doctor "+doctor.getFirstName()+" "+ doctor.getLastName()+" not availiable for this time"; 
            }else{
                Appointment newAppointment = new Appointment(patientId, doctorId, appointmentDate, duration, "schedule");

                //save appointment to database
                AppointmentDao.saveAppointment(newAppointment);
                return "Appointment booking successfully";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }  
}

