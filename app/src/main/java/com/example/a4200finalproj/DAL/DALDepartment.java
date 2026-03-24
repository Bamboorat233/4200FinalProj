package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.a4200finalproj.Models.Department;

import java.util.ArrayList;
import java.util.List;

public class DALDepartment {

    private final DatabaseHelper db;

    public DALDepartment(Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public long insert(Department d) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableDepartment.COLUMN_NAME, d.getName());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION, d.getDescription());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_LOCATION, d.getLocation());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_PHONE, d.getPhone());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_IS_ACTIVE, 1);
        return db.insertDepartment(cv);
    }

    public Department getById(int id) {
        Cursor cursor = db.getDepartmentById(id);
        if (cursor != null && cursor.moveToFirst()) {
            Department d = cursorToDepartment(cursor);
            cursor.close();
            return d;
        }
        return null;
    }

    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        Cursor cursor = db.getAllDepartments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToDepartment(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public int update(Department d) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableDepartment.COLUMN_NAME, d.getName());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION, d.getDescription());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_LOCATION, d.getLocation());
        cv.put(DatabaseHelper.TableDepartment.COLUMN_PHONE, d.getPhone());
        return db.updateDepartment(d.getId(), cv);
    }

    public int delete(int id) {
        return db.deleteDepartment(id);
    }

    private Department cursorToDepartment(Cursor cursor) {
        Department d = new Department();
        d.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_ID)));
        d.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_NAME)));
        d.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_DESCRIPTION)));
        d.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_LOCATION)));
        d.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_PHONE)));
        d.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableDepartment.COLUMN_IS_ACTIVE)));
        return d;
    }
}