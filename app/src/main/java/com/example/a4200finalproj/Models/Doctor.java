package com.example.comp4200project;

public class Doctor {

    public int DoctorID;
    public String Name;
    public String Specialization;
    public String Contact;
    public int DepartmentID;

    public int getDoctorID(){return DoctorID;}
    public void setDoctorID(int DoctorID){this.DoctorID = DoctorID;}

    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public String getSpecialization(){return Specialization;}
    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }

    public String getContact(){return Contact;}
    public void setContact(String Contact){this.Contact = Contact;}

    public int getDepartmentID(){return DepartmentID;}
    public void setDepartmentID(int DepartmentID){this.DepartmentID = DepartmentID;}
}
