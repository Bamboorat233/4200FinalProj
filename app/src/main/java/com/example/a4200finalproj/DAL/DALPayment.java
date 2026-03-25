package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Payment;

import java.util.ArrayList;
import java.util.List;

public class DALPayment {

    private final DatabaseHelper db;

    public DALPayment(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addPayment(Payment p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID, p.getInvoiceId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID, p.getPatientId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_AMOUNT, p.getAmount());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE, p.getPaymentDate());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD, p.getPaymentMethod());
        cv.put(DatabaseHelper.TablePayment.COLUMN_TRANSACTION_ID, p.getTransactionId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_STATUS, p.getStatus());
        cv.put(DatabaseHelper.TablePayment.COLUMN_NOTES, p.getNotes());
        return db.insertPayment(cv);
    }

    public Payment getPaymentById(int id) {
        Cursor cursor = db.getPaymentById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Payment p = cursorToPayment(cursor);
            cursor.close();
            return p;
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        Cursor cursor = db.getAllPayments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPayment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Payment> getByInvoice(int invoiceId) {
        List<Payment> list = new ArrayList<>();
        Cursor cursor = db.getPaymentsByInvoice(invoiceId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPayment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Payment> getByPatient(int patientId) {
        List<Payment> list = new ArrayList<>();
        Cursor cursor = db.getPaymentsByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPayment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int updatePayment(Payment p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID, p.getInvoiceId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID, p.getPatientId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_AMOUNT, p.getAmount());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE, p.getPaymentDate());
        cv.put(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD, p.getPaymentMethod());
        cv.put(DatabaseHelper.TablePayment.COLUMN_TRANSACTION_ID, p.getTransactionId());
        cv.put(DatabaseHelper.TablePayment.COLUMN_STATUS, p.getStatus());
        cv.put(DatabaseHelper.TablePayment.COLUMN_NOTES, p.getNotes());
        return db.updatePayment(p.getId(), cv);
    }

    public int deletePayment(int id) {
        return db.deletePayment(id);
    }

    private Payment cursorToPayment(Cursor cursor) {
        Payment p = new Payment();
        p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_ID)));
        p.setInvoiceId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_INVOICE_ID)));
        p.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PATIENT_ID)));
        p.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_AMOUNT)));
        p.setPaymentDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_DATE)));
        p.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_PAYMENT_METHOD)));
        p.setTransactionId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_TRANSACTION_ID)));
        p.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_STATUS)));
        p.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePayment.COLUMN_NOTES)));
        return p;
    }
}