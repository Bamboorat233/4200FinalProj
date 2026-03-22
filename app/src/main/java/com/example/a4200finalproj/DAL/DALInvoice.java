package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALInvoice extends DALBase {

    public Invoice getById(int id) {

        String sql = "SELECT InvoiceID, PatientID, AppointmentID, Amount, DateIssued, Status " +
                "FROM dbo.Invoice WHERE InvoiceID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

                if (!rs.next())
                    return null;

                Invoice i = new Invoice();

                i.setInvoiceID(rs.getInt(1));
                i.setPatientID(rs.getInt(2));
                i.setAppointmentID(rs.getInt(3));
                i.setAmount(rs.getDouble(4));
                i.setDateIssued(rs.getTimestamp(5));
                i.setStatus(rs.getString(6));

                return i;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Invoice getByAppointment(int appointmentId) {

        String sql = "SELECT InvoiceID, PatientID, AppointmentID, Amount, DateIssued, Status " +
                "FROM dbo.Invoice WHERE AppointmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next())
                    throw new RuntimeException("Invoice not found for this appointment.");

                Invoice i = new Invoice();

                i.setInvoiceID(rs.getInt(1));
                i.setPatientID(rs.getInt(2));
                i.setAppointmentID(rs.getInt(3));
                i.setAmount(rs.getDouble(4));
                i.setDateIssued(rs.getTimestamp(5));
                i.setStatus(rs.getString(6));

                return i;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Invoice> getByPatient(int patientId) {

        String sql = "SELECT InvoiceID, PatientID, AppointmentID, Amount, DateIssued, Status " +
                "FROM dbo.Invoice WHERE PatientID = ? ORDER BY DateIssued DESC";

        List<Invoice> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Invoice i = new Invoice();

                    i.setInvoiceID(rs.getInt(1));
                    i.setPatientID(rs.getInt(2));
                    i.setAppointmentID(rs.getInt(3));
                    i.setAmount(rs.getDouble(4));
                    i.setDateIssued(rs.getTimestamp(5));
                    i.setStatus(rs.getString(6));

                    list.add(i);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insert(Invoice i) {

        String sql = "INSERT INTO dbo.Invoice(PatientID, AppointmentID, Amount, DateIssued, Status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, i.getPatientID());
            stmt.setInt(2, i.getAppointmentID());
            stmt.setDouble(3, i.getAmount());
            stmt.setTimestamp(4, new Timestamp(i.getDateIssued().getTime()));
            stmt.setString(5, i.getStatus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    return keys.getInt(1);
            }

        } catch (SQLException ex) {

            if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601)
                throw new RuntimeException("An invoice already exists for this appointment.", ex);

            throw new RuntimeException(ex);
        }

        return 0;
    }

    public int update(Invoice i) {

        String sql = "UPDATE dbo.Invoice " +
                "SET PatientID = ?, AppointmentID = ?, Amount = ?, DateIssued = ?, Status = ? " +
                "WHERE InvoiceID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, i.getPatientID());
            stmt.setInt(2, i.getAppointmentID());
            stmt.setDouble(3, i.getAmount());
            stmt.setTimestamp(4, new Timestamp(i.getDateIssued().getTime()));
            stmt.setString(5, i.getStatus());
            stmt.setInt(6, i.getInvoiceID());

            return stmt.executeUpdate();

        } catch (SQLException ex) {

            if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601)
                throw new RuntimeException("Another invoice already uses this appointment.", ex);

            throw new RuntimeException(ex);
        }
    }

    public int updateStatus(int invoiceId, String status) {

        String sql = "UPDATE dbo.Invoice SET Status = ? WHERE InvoiceID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, invoiceId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int delete(int id) {

        String sql = "DELETE FROM dbo.Invoice WHERE InvoiceID = ?";

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