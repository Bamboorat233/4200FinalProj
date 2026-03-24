package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class DALAppointment {

    private final DatabaseHelper db;

    public DALAppointment(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addAppointment(Appointment a) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID, a.getPatientId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID, a.getDoctorId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DEPARTMENT_ID, a.getDepartmentId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE, a.getAppointmentDate());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME, a.getAppointmentTime());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_REASON, a.getReason());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_STATUS, a.getStatus());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_NOTES, a.getNotes());
        return db.insertAppointment(cv);
    }

    public Appointment getAppointmentById(int id) {
        Cursor cursor = db.getAppointmentById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Appointment a = cursorToAppointment(cursor);
            cursor.close();
            return a;
        }
        return null;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        Cursor cursor = db.getAllAppointments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToAppointment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Appointment> getByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        Cursor cursor = db.getAppointmentsByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToAppointment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Appointment> getByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        Cursor cursor = db.getAppointmentsByDoctor(doctorId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToAppointment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int updateAppointment(Appointment a) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID, a.getPatientId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID, a.getDoctorId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_DEPARTMENT_ID, a.getDepartmentId());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE, a.getAppointmentDate());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME, a.getAppointmentTime());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_REASON, a.getReason());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_STATUS, a.getStatus());
        cv.put(DatabaseHelper.TableAppointment.COLUMN_NOTES, a.getNotes());
        return db.updateAppointment(a.getId(), cv);
    }

    public int deleteAppointment(int id) {
        return db.deleteAppointment(id);
    }

    private Appointment cursorToAppointment(Cursor cursor) {
        Appointment a = new Appointment();
        a.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_ID)));
        a.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_PATIENT_ID)));
        a.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_DOCTOR_ID)));
        a.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_DEPARTMENT_ID)));
        a.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_DATE)));
        a.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_APPOINTMENT_TIME)));
        a.setReason(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_REASON)));
        a.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_STATUS)));
        a.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableAppointment.COLUMN_NOTES)));
        return a;
    }
}