package com.example.comp4200project;

public class Medication {

    public int MedID;
    public String Name;
    public String Dosage;
    public double Price;
    public int Quantity;

    public int getMedID(){return MedID;}
    public void setMedID(int MedID){this.MedID = MedID;}

    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}

    public String getDosage(){return Dosage;}
    public void setDosage(String Dosage){this.Dosage = Dosage;}

    public double getPrice(){return Price;}
    public void setPrice(double Price){this.Price = Price;}

    public int getQuantity(){return Quantity;}
    public void setQuantity(int Quantity){this.Quantity = Quantity;}
}
