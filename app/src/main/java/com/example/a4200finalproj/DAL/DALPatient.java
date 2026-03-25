package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Patient;

import java.util.ArrayList;
import java.util.List;

public class DALPatient {

    private final DatabaseHelper db;

    public DALPatient(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addPatient(Patient p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePatient.COLUMN_FULL_NAME, p.getFullName());
        cv.put(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH, p.getDateOfBirth());
        cv.put(DatabaseHelper.TablePatient.COLUMN_GENDER, p.getGender());
        cv.put(DatabaseHelper.TablePatient.COLUMN_PHONE, p.getPhone());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMAIL, p.getEmail());
        cv.put(DatabaseHelper.TablePatient.COLUMN_ADDRESS, p.getAddress());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT, p.getEmergencyContact());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE, p.getEmergencyPhone());
        cv.put(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE, p.getBloodType());
        cv.put(DatabaseHelper.TablePatient.COLUMN_ALLERGIES, p.getAllergies());
        cv.put(DatabaseHelper.TablePatient.COLUMN_IS_ACTIVE, 1);
        return db.insertPatient(cv);
    }

    public Patient getById(int patientId) {
        Cursor cursor = db.getPatientById(patientId);
        if (cursor != null && cursor.moveToFirst()) {
            Patient p = cursorToPatient(cursor);
            cursor.close();
            return p;
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        Cursor cursor = db.getAllPatients();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                patients.add(cursorToPatient(cursor));
            }
            cursor.close();
        }
        return patients;
    }

    public int updatePatient(Patient p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePatient.COLUMN_FULL_NAME, p.getFullName());
        cv.put(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH, p.getDateOfBirth());
        cv.put(DatabaseHelper.TablePatient.COLUMN_GENDER, p.getGender());
        cv.put(DatabaseHelper.TablePatient.COLUMN_PHONE, p.getPhone());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMAIL, p.getEmail());
        cv.put(DatabaseHelper.TablePatient.COLUMN_ADDRESS, p.getAddress());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT, p.getEmergencyContact());
        cv.put(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE, p.getEmergencyPhone());
        cv.put(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE, p.getBloodType());
        cv.put(DatabaseHelper.TablePatient.COLUMN_ALLERGIES, p.getAllergies());
        return db.updatePatient(p.getId(), cv);
    }

    public int deleteById(int patientId) {
        return db.deletePatient(patientId);
    }

    private Patient cursorToPatient(Cursor cursor) {
        Patient p = new Patient();
        p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ID)));
        p.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_FULL_NAME)));
        p.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_DATE_OF_BIRTH)));
        p.setGender(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_GENDER)));
        p.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_PHONE)));
        p.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMAIL)));
        p.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ADDRESS)));
        p.setEmergencyContact(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_CONTACT)));
        p.setEmergencyPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_EMERGENCY_PHONE)));
        p.setBloodType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_BLOOD_TYPE)));
        p.setAllergies(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePatient.COLUMN_ALLERGIES)));
        return p;
    }
}