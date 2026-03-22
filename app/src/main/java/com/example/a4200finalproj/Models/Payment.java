package com.example.comp4200project;

import java.sql.Date;
import java.sql.Timestamp;

public class Payment {

    public int PaymentID;
    public int InvoiceID;
    public Timestamp PaymentDate;
    public double Amount;
    public String PaymentMethod;

    public int getPaymentID(){return PaymentID;}
    public void setPaymentID(int PaymentID){this.PaymentID = PaymentID;}

    public int getInvoiceID(){return InvoiceID;}
    public void setInvoiceID(int InvoiceID){this.InvoiceID = InvoiceID;}

    public Timestamp getPaymentDate(){return PaymentDate;}
    public void setPaymentDate(Timestamp PaymentDate){this.PaymentDate = PaymentDate;}

    public double getAmount(){return Amount;}
    public void setAmount(double Amount){this.Amount = Amount;}

    public String getPaymentMethod(){return PaymentMethod;}
    public void setPaymentMethod(String PaymentMethod){this.PaymentMethod = PaymentMethod;}


}
