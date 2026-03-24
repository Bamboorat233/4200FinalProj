package com.example.a4200finalproj.DAL;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.a4200finalproj.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DALUser {

    private final DatabaseHelper db;

    public DALUser(android.content.Context context) {
        db = DatabaseHelper.getInstance(context);
    }

    public User authenticate(String username, String password) {
        if (db.authenticateUser(username, password)) {
            Cursor cursor = db.getUserByUsername(username);
            if (cursor != null && cursor.moveToFirst()) {
                User user = cursorToUser(cursor);
                cursor.close();
                return user;
            }
        }
        return null;
    }

    public long addUser(User u) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableUser.COLUMN_USERNAME, u.getUsername());
        cv.put(DatabaseHelper.TableUser.COLUMN_PASSWORD, u.getPassword());
        cv.put(DatabaseHelper.TableUser.COLUMN_FULL_NAME, u.getFullName());
        cv.put(DatabaseHelper.TableUser.COLUMN_EMAIL, u.getEmail());
        cv.put(DatabaseHelper.TableUser.COLUMN_ROLE, u.getRole());
        cv.put(DatabaseHelper.TableUser.COLUMN_PHONE, u.getPhone());
        cv.put(DatabaseHelper.TableUser.COLUMN_ADDRESS, u.getAddress());
        cv.put(DatabaseHelper.TableUser.COLUMN_IS_ACTIVE, u.getIsActive());
        return db.insertUser(cv);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                users.add(cursorToUser(cursor));
            }
            cursor.close();
        }
        return users;
    }

    public int updateUser(User u) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TableUser.COLUMN_FULL_NAME, u.getFullName());
        cv.put(DatabaseHelper.TableUser.COLUMN_EMAIL, u.getEmail());
        cv.put(DatabaseHelper.TableUser.COLUMN_ROLE, u.getRole());
        cv.put(DatabaseHelper.TableUser.COLUMN_PHONE, u.getPhone());
        cv.put(DatabaseHelper.TableUser.COLUMN_ADDRESS, u.getAddress());
        cv.put(DatabaseHelper.TableUser.COLUMN_IS_ACTIVE, u.getIsActive());
        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            cv.put(DatabaseHelper.TableUser.COLUMN_PASSWORD, u.getPassword());
        }
        return db.updateUser(u.getId(), cv);
    }

    public int deleteById(int userId) {
        return db.deleteUser(userId);
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_USERNAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_PASSWORD)));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_FULL_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_EMAIL)));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ROLE)));
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_PHONE)));
        user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_ADDRESS)));
        user.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TableUser.COLUMN_IS_ACTIVE)));
        return user;
    }
}