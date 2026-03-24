package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Medication;

import java.util.ArrayList;
import java.util.List;

public class DALMedication {

    private final DatabaseHelper db;

    public DALMedication(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long insert(Medication m) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedication.COLUMN_NAME, m.getName());
        cv.put(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME, m.getGenericName());
        cv.put(DatabaseHelper.TableMedication.COLUMN_DOSAGE, m.getDosage());
        cv.put(DatabaseHelper.TableMedication.COLUMN_FORM, m.getForm());
        cv.put(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER, m.getManufacturer());
        cv.put(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS, m.getSideEffects());
        cv.put(DatabaseHelper.TableMedication.COLUMN_IS_ACTIVE, 1);
        return db.insertMedication(cv);
    }

    public Medication getById(int id) {
        Cursor cursor = db.getMedicationById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Medication m = cursorToMedication(cursor);
            cursor.close();
            return m;
        }
        return null;
    }

    public List<Medication> getAll() {
        List<Medication> list = new ArrayList<>();
        Cursor cursor = db.getAllMedications();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToMedication(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Medication> search(String query) {
        List<Medication> list = new ArrayList<>();
        Cursor cursor = db.searchMedications(query);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToMedication(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int update(Medication m) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedication.COLUMN_NAME, m.getName());
        cv.put(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME, m.getGenericName());
        cv.put(DatabaseHelper.TableMedication.COLUMN_DOSAGE, m.getDosage());
        cv.put(DatabaseHelper.TableMedication.COLUMN_FORM, m.getForm());
        cv.put(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER, m.getManufacturer());
        cv.put(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS, m.getSideEffects());
        return db.updateMedication(m.getId(), cv);
    }

    public int delete(int id) {
        return db.deleteMedication(id);
    }

    private Medication cursorToMedication(Cursor cursor) {
        Medication m = new Medication();
        m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_ID)));
        m.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_NAME)));
        m.setGenericName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_GENERIC_NAME)));
        m.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_DOSAGE)));
        m.setForm(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_FORM)));
        m.setManufacturer(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_MANUFACTURER)));
        m.setSideEffects(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_SIDE_EFFECTS)));
        m.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedication.COLUMN_IS_ACTIVE)));
        return m;
    }
}