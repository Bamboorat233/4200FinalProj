package com.example.comp4200project;

import java.sql.Date;
import java.sql.Timestamp;

public class Invoice {

    public int InvoiceID;
    public int PatientID;
    public int AppointmentID;
    public double Amount;
    public Timestamp DateIssued;
    public String Status;

    public Invoice(){
        InvoiceID=0;
        PatientID=0;
        AppointmentID=0;
        Amount=0.0;
        DateIssued = null;
        Status = "Unpaid";
    }

    public int getInvoiceID(){return InvoiceID;}
    public void setInvoiceID(int InvoiceID){this.InvoiceID = InvoiceID;}

    public int getPatientID(){return PatientID;}
    public void setPatientID(int PatientID){this.PatientID = PatientID;}

    public int getAppointmentID(){return AppointmentID;}
    public void setAppointmentID(int AppointmentID){this.AppointmentID = AppointmentID;}

     public double getAmount(){return Amount;}
    public void setAmount(double Amount){this.Amount = Amount;}

    public Timestamp getDateIssued(){return DateIssued;}
    public void setDateIssued(Timestamp DateIssued){this.DateIssued = DateIssued;}

    public String getStatus(){return Status;}
    public void setStatus(String Status){this.Status = Status;}
}

