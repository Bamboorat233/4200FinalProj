package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALMedicalRecord extends DALBase {

    public MedicalRecord getById(int id) {

        String sql = "SELECT RecordID, PatientID, DoctorID, Diagnosis, Treatment, VisitDate " +
                "FROM dbo.MedicalRecord WHERE RecordID = ?";

        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next())
                    return null;

                MedicalRecord m = new MedicalRecord();

                m.setRecordID(rs.getInt(1));
                m.setPatientID(rs.getInt(2));
                m.setDoctorID(rs.getInt(3));
                String diagnosis = rs.getString(4);
                m.setDiagnosis(diagnosis == null ? "" : diagnosis);
                String treatment = rs.getString(5);
                m.setTreatment(treatment == null ? "" : treatment);
                m.setVisitDate(rs.getTimestamp(6));

                return m;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<MedicalRecord> getByPatient(int patientId) {

        String sql = "SELECT RecordID, PatientID, DoctorID, Diagnosis, Treatment, VisitDate " +
                "FROM dbo.MedicalRecord " +
                "WHERE PatientID = ? " +
                "ORDER BY VisitDate DESC";

        List<MedicalRecord> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    MedicalRecord m = new MedicalRecord();

                    m.setRecordID(rs.getInt(1));
                    m.setPatientID(rs.getInt(2));
                    m.setDoctorID(rs.getInt(3));
                    String diagnosis = rs.getString(4);
                    m.setDiagnosis(diagnosis == null ? "" : diagnosis);
                    String treatment = rs.getString(5);
                    m.setTreatment(treatment == null ? "" : treatment);
                    m.setVisitDate(rs.getTimestamp(6));

                    list.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MedicalRecord> getByDoctor(int doctorId) {

        String sql = "SELECT RecordID, PatientID, DoctorID, Diagnosis, Treatment, VisitDate " +
                "FROM dbo.MedicalRecord " +
                "WHERE DoctorID = ? " +
                "ORDER BY VisitDate DESC";

        List<MedicalRecord> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    MedicalRecord m = new MedicalRecord();

                    m.setRecordID(rs.getInt(1));
                    m.setPatientID(rs.getInt(2));
                    m.setDoctorID(rs.getInt(3));
                    String diagnosis = rs.getString(4);
                    m.setDiagnosis(diagnosis == null ? "" : diagnosis);
                    String treatment = rs.getString(5);
                    m.setTreatment(treatment == null ? "" : treatment);
                    m.setVisitDate(rs.getTimestamp(6));

                    list.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insert(MedicalRecord m) {

        String sql = "INSERT INTO dbo.MedicalRecord(PatientID, DoctorID, Diagnosis, Treatment, VisitDate) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, m.getPatientID());
            stmt.setInt(2, m.getDoctorID());
            stmt.setString(3, m.getDiagnosis());
            stmt.setString(4, m.getTreatment());
            stmt.setTimestamp(5, new Timestamp(m.getVisitDate().getTime()));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int update(MedicalRecord m) {

        String sql = "UPDATE dbo.MedicalRecord " +
                "SET PatientID = ?, DoctorID = ?, Diagnosis = ?, Treatment = ?, VisitDate = ? " +
                "WHERE RecordID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getPatientID());
            stmt.setInt(2, m.getDoctorID());
            stmt.setString(3, m.getDiagnosis());
            stmt.setString(4, m.getTreatment());
            stmt.setTimestamp(5, new Timestamp(m.getVisitDate().getTime()));
            stmt.setInt(6, m.getRecordID());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int delete(int id) {

        String sql = "DELETE FROM dbo.MedicalRecord WHERE RecordID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}