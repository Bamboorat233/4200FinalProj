package com.example.comp4200project;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {

    public int AppointmentID;
    public int PatientID;
    public int DoctorID;
    public Date AppointmentDate;
    public Time AppointmentTime; //SQL TIME
    public String Status;

    public int getAppointmentID() {
        return AppointmentID;
    }

    public void setAppointmentID(int AppointmentID) {
        this.AppointmentID = AppointmentID;
    }

    public int getPatientID() {
        return PatientID;
    }

    public void setPatientID(int PatientID) {
        this.PatientID = PatientID;
    }

    public int getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(int DoctorID) {
        this.DoctorID = DoctorID;
    }

    public Date getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(Date AppointmentDate) {
        this.AppointmentDate = AppointmentDate;
    }

    public Time getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(Time AppointmentTime) {
        this.AppointmentTime = AppointmentTime;
    }

    public String getStatus(){return Status;}

    public void setStatus(String status){this.Status = Status;}



}
