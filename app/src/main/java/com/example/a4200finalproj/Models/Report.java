package com.example.comp4200project;

import java.sql.Timestamp;

public class Report {

    public int ReportID;
    public int PatientID;
    public int DoctorID;
    public Timestamp ReportDate;
    public String Diagnosis;
    public String Treatment;

    public int getReportID(){return ReportID;}
    public void setReportID(int ReportID){this.ReportID = ReportID;}

    public int getPatientID(){return PatientID;}
    public void setPatientID(int PatientID){this.PatientID = PatientID;}

    public int getDoctorID(){return DoctorID;}
    public void setDoctorID(int DoctorID){this.DoctorID = DoctorID;}

    public Timestamp getReportDate(){return ReportDate;}
    public void setReportDate(Timestamp ReportDate){this.ReportDate = ReportDate;}

    public String getDiagnosis(){return Diagnosis;}
    public void setDiagnosis(String Diagnosis){this.Diagnosis = Diagnosis;}

    public String getTreatment(){return Treatment;}
    public void setTreatment(String Treatment){this.Treatment = Treatment;}


}
