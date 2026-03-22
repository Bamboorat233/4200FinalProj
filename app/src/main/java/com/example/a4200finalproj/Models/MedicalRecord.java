package com.example.comp4200project;

import java.sql.Date;
import java.sql.Timestamp;

public class MedicalRecord {

    public int RecordID;
    public int PatientID;
    public int DoctorID;
    public String Diagnosis;
    public String Treatment;
    public Timestamp VisitDate;

    public int getRecordID(){return RecordID;}
    public void setRecordID(int RecordID){this.RecordID = RecordID;}

    public int getPatientID(){return PatientID;}
    public void setPatientID(int PatientID){this.PatientID = PatientID;}

    public int getDoctorID(){return DoctorID;}
    public void setDoctorID(int DoctorID){this.DoctorID = DoctorID;}

    public String getDiagnosis(){return Diagnosis;}
    public void setDiagnosis(String Diagnosis){this.Diagnosis = Diagnosis;}

    public String getTreatment(){return Treatment;}
    public void setTreatment(String Treatment){this.Treatment = Treatment;}

    public Timestamp getVisitDate(){return VisitDate;}
    public void setVisitDate(Timestamp VisitDate){this.VisitDate = VisitDate;}



}
