package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Invoice;

import java.util.ArrayList;
import java.util.List;

public class DALInvoice {

    private final DatabaseHelper db;

    public DALInvoice(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long insert(Invoice i) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID, i.getPatientId());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_INVOICE_NUMBER, db.generateInvoiceNumber());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE, i.getInvoiceDate());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE, i.getDueDate());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL, i.getSubtotal());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TAX, i.getTax());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TOTAL, i.getTotal());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_STATUS, i.getStatus());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_NOTES, i.getNotes());
        return db.insertInvoice(cv);
    }

    public Invoice getById(int id) {
        Cursor cursor = db.getInvoiceById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Invoice i = cursorToInvoice(cursor);
            cursor.close();
            return i;
        }
        return null;
    }

    public List<Invoice> getByPatient(int patientId) {
        List<Invoice> list = new ArrayList<>();
        Cursor cursor = db.getInvoicesByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToInvoice(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Invoice> getAll() {
        List<Invoice> list = new ArrayList<>();
        Cursor cursor = db.getAllInvoices();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToInvoice(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int update(Invoice i) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID, i.getPatientId());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE, i.getInvoiceDate());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE, i.getDueDate());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL, i.getSubtotal());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TAX, i.getTax());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_TOTAL, i.getTotal());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_STATUS, i.getStatus());
        cv.put(DatabaseHelper.TableInvoice.COLUMN_NOTES, i.getNotes());
        return db.updateInvoice(i.getId(), cv);
    }

    public int updateStatus(int invoiceId, String status) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableInvoice.COLUMN_STATUS, status);
        return db.updateInvoice(invoiceId, cv);
    }

    public int delete(int id) {
        return db.deleteInvoice(id);
    }

    private Invoice cursorToInvoice(Cursor cursor) {
        Invoice i = new Invoice();
        i.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_ID)));
        i.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_PATIENT_ID)));
        i.setInvoiceNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_NUMBER)));
        i.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_INVOICE_DATE)));
        i.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_DUE_DATE)));
        i.setSubtotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_SUBTOTAL)));
        i.setTax(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_TAX)));
        i.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_TOTAL)));
        i.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_STATUS)));
        i.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableInvoice.COLUMN_NOTES)));
        return i;
    }
}