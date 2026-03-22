package com.example.comp4200project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DALReport extends DALBase {

    // CREATE
    public void addReport(Report r) {
        String sql = "INSERT INTO Report (PatientID, DoctorID, ReportDate, Diagnosis, Treatment) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getPatientID());
            stmt.setInt(2, r.getDoctorID());
            stmt.setTimestamp(3, new Timestamp(r.getReportDate().getTime()));
            stmt.setString(4, r.getDiagnosis());

            if (r.getTreatment() != null) {
                stmt.setString(5, r.getTreatment());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - get one
    public Report getReportById(int id) {
        String sql = "SELECT ReportID, PatientID, DoctorID, ReportDate, Diagnosis, Treatment " +
                "FROM Report WHERE ReportID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Report r = new Report();
                    r.setReportID(rs.getInt(1));
                    r.setPatientID(rs.getInt(2));
                    r.setDoctorID(rs.getInt(3));
                    r.setReportDate(rs.getTimestamp(4));
                    r.setDiagnosis(rs.getString(5));
                    r.setTreatment(rs.getString(6)); // returns null if DB value is NULL
                    return r;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - get all reports
    public List<Report> getAllReports() {
        String sql = "SELECT ReportID, PatientID, DoctorID, ReportDate, Diagnosis, Treatment FROM Report";
        List<Report> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Report r = new Report();
                r.setReportID(rs.getInt(1));
                r.setPatientID(rs.getInt(2));
                r.setDoctorID(rs.getInt(3));
                r.setReportDate(rs.getTimestamp(4));
                r.setDiagnosis(rs.getString(5));
                r.setTreatment(rs.getString(6)); // returns null if DB value is NULL
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE
    public void updateReport(Report r) {
        String sql = "UPDATE Report SET PatientID = ?, DoctorID = ?, ReportDate = ?, Diagnosis = ?, Treatment = ? " +
                "WHERE ReportID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getPatientID());
            stmt.setInt(2, r.getDoctorID());
            stmt.setTimestamp(3, new Timestamp(r.getReportDate().getTime()));
            stmt.setString(4, r.getDiagnosis());

            if (r.getTreatment() != null) {
                stmt.setString(5, r.getTreatment());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            stmt.setInt(6, r.getReportID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteReport(int id) {
        String sql = "DELETE FROM Report WHERE ReportID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}