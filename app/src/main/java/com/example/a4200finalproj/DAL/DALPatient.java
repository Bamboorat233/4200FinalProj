package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALPatient extends DALBase {

    public void addPatient(Patient p) {

        String sql =
                "INSERT INTO Patient (Name, DOB, Gender, Contact, [Address]) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDate(2, new java.sql.Date(p.getDOB().getTime()));
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getContact());
            stmt.setString(5, p.getAddress());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Patient getById(int patientId) {

        String sql =
                "SELECT PatientID, Name, DOB, Gender, Contact, [Address] " +
                        "FROM Patient " +
                        "WHERE PatientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            Patient p = new Patient();
            p.setPatientID(rs.getInt("PatientID"));
            p.setName(rs.getString("Name"));
            p.setDOB(rs.getDate("DOB"));
            p.setGender(rs.getString("Gender"));
            p.setContact(rs.getString("Contact"));
            p.setAddress(rs.getString("Address"));

            return p;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int deleteById(int patientId) {

        String sql = "DELETE FROM Patient WHERE PatientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int updateContactById(int patientId, String newContact) {

        String sql = "UPDATE Patient SET Contact = ? WHERE PatientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newContact);
            stmt.setInt(2, patientId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int updatePatient(Patient p) {

        String sql =
                "UPDATE Patient " +
                        "SET Name = ?, DOB = ?, Gender = ?, Contact = ?, [Address] = ? " +
                        "WHERE PatientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDate(2, new java.sql.Date(p.getDOB().getTime()));
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getContact());
            stmt.setString(5, p.getAddress());
            stmt.setInt(6, p.getPatientID());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public List<Patient> getAllPatients() {

        String sql = "SELECT PatientID, Name, DOB, Gender, Contact, [Address] " +
                "FROM Patient " +
                "ORDER BY Name";

        List<Patient> patients = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Patient p = new Patient();

                p.setPatientID(rs.getInt("PatientID"));
                p.setName(rs.getString("Name"));
                p.setDOB(rs.getDate("DOB"));
                p.setGender(rs.getString("Gender"));
                p.setContact(rs.getString("Contact"));
                p.setAddress(rs.getString("Address"));

                patients.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
}

