package com.example.comp4200project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DALPayment extends DALBase {

    // CREATE
    public void addPayment(Payment p) {
        String sql = "INSERT INTO Payment (InvoiceID, PaymentDate, Amount, PaymentMethod) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getInvoiceID());
            stmt.setTimestamp(2, new Timestamp(p.getPaymentDate().getTime()));
            stmt.setDouble(3, p.getAmount());
            stmt.setString(4, p.getPaymentMethod());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - get one
    public Payment getPaymentById(int id) {
        String sql = "SELECT PaymentID, InvoiceID, PaymentDate, Amount, PaymentMethod " +
                "FROM Payment WHERE PaymentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Payment p = new Payment();
                    p.setPaymentID(rs.getInt(1));
                    p.setInvoiceID(rs.getInt(2));
                    p.setPaymentDate(rs.getTimestamp(3));
                    p.setAmount(rs.getDouble(4));
                    p.setPaymentMethod(rs.getString(5));
                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - get all
    public List<Payment> getAllPayments() {
        String sql = "SELECT PaymentID, InvoiceID, PaymentDate, Amount, PaymentMethod FROM Payment";
        List<Payment> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentID(rs.getInt(1));
                p.setInvoiceID(rs.getInt(2));
                p.setPaymentDate(rs.getTimestamp(3));
                p.setAmount(rs.getDouble(4));
                p.setPaymentMethod(rs.getString(5));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE
    public void updatePayment(Payment p) {
        String sql = "UPDATE Payment SET InvoiceID = ?, PaymentDate = ?, Amount = ?, PaymentMethod = ? " +
                "WHERE PaymentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getInvoiceID());
            stmt.setTimestamp(2, new Timestamp(p.getPaymentDate().getTime()));
            stmt.setDouble(3, p.getAmount());
            stmt.setString(4, p.getPaymentMethod());
            stmt.setInt(5, p.getPaymentID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePayment(int id) {
        String sql = "DELETE FROM Payment WHERE PaymentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}