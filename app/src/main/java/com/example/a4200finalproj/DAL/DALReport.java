package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Report;

import java.util.ArrayList;
import java.util.List;

public class DALReport {

    private final DatabaseHelper db;

    public DALReport(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long addReport(Report r) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableReport.COLUMN_TITLE, r.getTitle());
        cv.put(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE, r.getReportType());
        cv.put(DatabaseHelper.TableReport.COLUMN_PATIENT_ID, r.getPatientId());
        cv.put(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID, r.getDoctorId());
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE, r.getGeneratedDate());
        cv.put(DatabaseHelper.TableReport.COLUMN_DATA, r.getData());
        cv.put(DatabaseHelper.TableReport.COLUMN_FILE_PATH, r.getFilePath());
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_BY, r.getGeneratedBy());
        return db.insertReport(cv);
    }

    public Report getReportById(int id) {
        Cursor cursor = db.getReportById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Report r = cursorToReport(cursor);
            cursor.close();
            return r;
        }
        return null;
    }

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        Cursor cursor = db.getAllReports();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToReport(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Report> getByPatient(int patientId) {
        List<Report> list = new ArrayList<>();
        Cursor cursor = db.getReportsByPatient(patientId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToReport(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public List<Report> getByDoctor(int doctorId) {
        List<Report> list = new ArrayList<>();
        Cursor cursor = db.getReportsByDoctor(doctorId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToReport(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int updateReport(Report r) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableReport.COLUMN_TITLE, r.getTitle());
        cv.put(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE, r.getReportType());
        cv.put(DatabaseHelper.TableReport.COLUMN_PATIENT_ID, r.getPatientId());
        cv.put(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID, r.getDoctorId());
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE, r.getGeneratedDate());
        cv.put(DatabaseHelper.TableReport.COLUMN_DATA, r.getData());
        cv.put(DatabaseHelper.TableReport.COLUMN_FILE_PATH, r.getFilePath());
        cv.put(DatabaseHelper.TableReport.COLUMN_GENERATED_BY, r.getGeneratedBy());
        return db.updateReport(r.getId(), cv);
    }

    public int deleteReport(int id) {
        return db.deleteReport(id);
    }

    private Report cursorToReport(Cursor cursor) {
        Report r = new Report();
        r.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_ID)));
        r.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_TITLE)));
        r.setReportType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_REPORT_TYPE)));
        r.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_PATIENT_ID)));
        r.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DOCTOR_ID)));
        r.setGeneratedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_GENERATED_DATE)));
        r.setData(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_DATA)));
        r.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_FILE_PATH)));
        r.setGeneratedBy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableReport.COLUMN_GENERATED_BY)));
        return r;
    }
}