package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALPrescription extends DALBase {

    // CREATE
    public void addPrescription(Prescription p) {

        String sql = "INSERT INTO Prescription (RecordID, MedID, Quantity) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getRecordID());
            stmt.setInt(2, p.getMedID());
            stmt.setInt(3, p.getQuantity());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ
    public Prescription getPrescription(int recordId, int medId) {

        String sql = "SELECT RecordID, MedID, Quantity FROM Prescription WHERE RecordID = ? AND MedID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            stmt.setInt(2, medId);

            try (ResultSet rdr = stmt.executeQuery()) {

                if (rdr.next()) {

                    Prescription p = new Prescription();

                    p.setRecordID(rdr.getInt(1));
                    p.setMedID(rdr.getInt(2));
                    p.setQuantity(rdr.getInt(3));

                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ (all prescriptions)
    public List<Prescription> getAllPrescriptions() {

        List<Prescription> list = new ArrayList<>();

        String sql = "SELECT RecordID, MedID, Quantity FROM Prescription";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rdr = stmt.executeQuery()) {

            while (rdr.next()) {

                Prescription p = new Prescription();

                p.setRecordID(rdr.getInt(1));
                p.setMedID(rdr.getInt(2));
                p.setQuantity(rdr.getInt(3));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE
    public void updatePrescription(Prescription p) {

        String sql = "UPDATE Prescription SET Quantity = ? WHERE RecordID = ? AND MedID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getQuantity());
            stmt.setInt(2, p.getRecordID());
            stmt.setInt(3, p.getMedID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePrescription(int recordId, int medId) {

        String sql = "DELETE FROM Prescription WHERE RecordID = ? AND MedID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            stmt.setInt(2, medId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}