package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DALDoctor {

    private final DatabaseHelper db;

    public DALDoctor(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addDoctor(Doctor d) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME, d.getFullName());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION, d.getSpecialization());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER, d.getLicenseNumber());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_PHONE, d.getPhone());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_EMAIL, d.getEmail());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_ADDRESS, d.getAddress());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID, d.getDepartmentId());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_IS_ACTIVE, 1);
        return db.insertDoctor(cv);
    }

    public Doctor getById(int doctorId) {
        Cursor cursor = db.getDoctorById(doctorId);
        if (cursor != null && cursor.moveToFirst()) {
            Doctor d = cursorToDoctor(cursor);
            cursor.close();
            return d;
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        Cursor cursor = db.getAllDoctors();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                doctors.add(cursorToDoctor(cursor));
            }
            cursor.close();
        }
        return doctors;
    }

    public int updateDoctor(Doctor d) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME, d.getFullName());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION, d.getSpecialization());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER, d.getLicenseNumber());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_PHONE, d.getPhone());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_EMAIL, d.getEmail());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_ADDRESS, d.getAddress());
        cv.put(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID, d.getDepartmentId());
        return db.updateDoctor(d.getId(), cv);
    }

    public int deleteById(int doctorId) {
        return db.deleteDoctor(doctorId);
    }

    private Doctor cursorToDoctor(Cursor cursor) {
        Doctor d = new Doctor();
        d.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ID)));
        d.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_FULL_NAME)));
        d.setSpecialization(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_SPECIALIZATION)));
        d.setLicenseNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_LICENSE_NUMBER)));
        d.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_PHONE)));
        d.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_EMAIL)));
        d.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_ADDRESS)));
        d.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDoctor.COLUMN_DEPARTMENT_ID)));
        return d;
    }
}