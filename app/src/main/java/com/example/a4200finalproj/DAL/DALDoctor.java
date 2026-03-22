package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALDoctor extends DALBase {

    public void addDoctor(Doctor d) {

        String sql =
                "INSERT INTO Doctor (Name, Specialization, Contact, DepartmentID) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getName());
            stmt.setString(2, d.getSpecialization());
            stmt.setString(3, d.getContact());
            stmt.setInt(4, d.getDepartmentID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Doctor getById(int doctorId) {

        String sql =
                "SELECT DoctorID, Name, Specialization, Contact, DepartmentID " +
                        "FROM Doctor " + "WHERE DoctorID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            Doctor d = new Doctor();
            d.setDoctorID(rs.getInt("DoctorID"));
            d.setName(rs.getString("Name"));
            d.setSpecialization(rs.getString("Specialization"));
            d.setContact(rs.getString("Contact"));
            d.setDepartmentID(rs.getInt("DepartmentID"));

            return d;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int deleteById(int doctorId) {

        String sql = "DELETE FROM Doctor WHERE DoctorID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int updateContactById(int doctorId, String newContact) {

        String sql = "UPDATE Doctor SET Contact = ? WHERE DoctorID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newContact);
            stmt.setInt(2, doctorId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int updateDoctor(Doctor d) {

        String sql =
                "UPDATE Doctor " + "SET Name = ?, Specialization = ?, Contact = ?, DepartmentID = ? " +
                        "WHERE DoctorID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getName());
            stmt.setString(2, d.getSpecialization());
            stmt.setString(3, d.getContact());
            stmt.setInt(4, d.getDepartmentID());
            stmt.setInt(5, d.getDoctorID());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public List<Doctor> getAllDoctors() {

        String sql =
                "SELECT DoctorID, Name, Specialization, Contact, DepartmentID " + "FROM Doctor " +
                        "ORDER BY Name";

        List<Doctor> doctors = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("DoctorID"));
                d.setName(rs.getString("Name"));
                d.setSpecialization(rs.getString("Specialization"));
                d.setContact(rs.getString("Contact"));
                d.setDepartmentID(rs.getInt("DepartmentID"));

                doctors.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }
}

