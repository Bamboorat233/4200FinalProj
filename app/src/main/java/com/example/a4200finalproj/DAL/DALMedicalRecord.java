package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class DALMedicalRecord {

    private final DatabaseHelper db;

    public DALMedicalRecord(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long insert(MedicalRecord m) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID, m.getPatientId());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID, m.getDoctorId());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE, m.getVisitDate());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS, m.getDiagnosis());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS, m.getComplaints());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT, m.getTreatment());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VITAL_SIGNS, m.getVitalSigns());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_NOTES, m.getNotes());
        return db.insertMedicalRecord(cv);
    }

    public MedicalRecord getById(int id) {
        Cursor cursor = db.getMedicalRecordById(id);
        if (cursor != null && cursor.moveToFirst()) {
            MedicalRecord m = cursorToRecord(cursor);
            cursor.close();
            return m;
        }
        return null;
    }

    public List<MedicalRecord> getByPatient(int patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        Cursor cursor = db.getMedicalRecordsByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToRecord(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<MedicalRecord> getByDoctor(int doctorId) {
        List<MedicalRecord> list = new ArrayList<>();
        Cursor cursor = db.getMedicalRecordsByDoctor(doctorId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToRecord(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int update(MedicalRecord m) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID, m.getPatientId());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID, m.getDoctorId());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE, m.getVisitDate());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS, m.getDiagnosis());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS, m.getComplaints());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT, m.getTreatment());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_VITAL_SIGNS, m.getVitalSigns());
        cv.put(DatabaseHelper.TableMedicalRecord.COLUMN_NOTES, m.getNotes());
        return db.updateMedicalRecord(m.getId(), cv);
    }

    public int delete(int id) {
        return db.deleteMedicalRecord(id);
    }

    private MedicalRecord cursorToRecord(Cursor cursor) {
        MedicalRecord m = new MedicalRecord();
        m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_ID)));
        m.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_PATIENT_ID)));
        m.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DOCTOR_ID)));
        m.setVisitDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_VISIT_DATE)));
        m.setDiagnosis(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_DIAGNOSIS)));
        m.setComplaints(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_COMPLAINTS)));
        m.setTreatment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_TREATMENT)));
        m.setVitalSigns(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_VITAL_SIGNS)));
        m.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableMedicalRecord.COLUMN_NOTES)));
        return m;
    }
}