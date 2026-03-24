package com.example.a4200finalproj.Models;

import java.sql.Date;


public class Patient {

    public int PatientID;
    public String Name;
    public Date DOB;
    public String Gender;
    public String Contact;
    public String Address;

    public int getPatientID(){return PatientID;}

    public void setPatientID(int PatientID){this.PatientID = PatientID;}

    public String getName(){return Name;}

    public void setName(String Name){this.Name = Name;}

    public Date getDOB(){return DOB;}

    public void setDOB(Date DOB){this.DOB = DOB;}

    public String getGender(){return Gender;}

    public void setGender(String Gender){this.Gender = Gender;}

    public String getContact(){return Contact;}

    public void setContact(String Contact){this.Contact = Contact;}

    public String getAddress(){return Address;}

    public void setAddress(String Address){this.Address = Address;}

}
