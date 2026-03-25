package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Prescription;

import java.util.ArrayList;
import java.util.List;

public class DALPrescription {

    private final DatabaseHelper db;

    public DALPrescription(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addPrescription(Prescription p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID, p.getPatientId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID, p.getDoctorId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID, p.getMedicationId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICAL_RECORD_ID, p.getMedicalRecordId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOSAGE, p.getDosage());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DURATION, p.getDuration());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY, p.getFrequency());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS, p.getInstructions());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_IS_ACTIVE, 1);
        return db.insertPrescription(cv);
    }

    public Prescription getById(int id) {
        Cursor cursor = db.getPrescriptionById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Prescription p = cursorToPrescription(cursor);
            cursor.close();
            return p;
        }
        return null;
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        Cursor cursor = db.getAllPrescriptions();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPrescription(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Prescription> getByPatient(int patientId) {
        List<Prescription> list = new ArrayList<>();
        Cursor cursor = db.getPrescriptionsByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPrescription(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Prescription> getByDoctor(int doctorId) {
        List<Prescription> list = new ArrayList<>();
        Cursor cursor = db.getPrescriptionsByDoctor(doctorId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToPrescription(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int updatePrescription(Prescription p) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID, p.getPatientId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID, p.getDoctorId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID, p.getMedicationId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_MEDICAL_RECORD_ID, p.getMedicalRecordId());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DOSAGE, p.getDosage());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_DURATION, p.getDuration());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY, p.getFrequency());
        cv.put(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS, p.getInstructions());
        return db.updatePrescription(p.getId(), cv);
    }

    public int deletePrescription(int id) {
        return db.deletePrescription(id);
    }

    private Prescription cursorToPrescription(Cursor cursor) {
        Prescription p = new Prescription();
        p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_ID)));
        p.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_PATIENT_ID)));
        p.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOCTOR_ID)));
        p.setMedicationId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_MEDICATION_ID)));
        p.setMedicalRecordId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_MEDICAL_RECORD_ID)));
        p.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DOSAGE)));
        p.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_DURATION)));
        p.setFrequency(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_FREQUENCY)));
        p.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_INSTRUCTIONS)));
        p.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TablePrescription.COLUMN_IS_ACTIVE)));
        return p;
    }
}