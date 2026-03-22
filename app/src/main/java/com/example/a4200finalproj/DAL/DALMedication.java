package com.example.comp4200project;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALMedication extends DALBase {

    public Medication getById(int id) {

        String sql = "SELECT MedID, Name, Dosage, Price, Quantity " +
                "FROM dbo.Medication WHERE MedID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next())
                    return null;

                Medication m = new Medication();

                m.setMedID(rs.getInt(1));
                m.setName(rs.getString(2));

                String dosage = rs.getString(3);
                m.setDosage(dosage == null ? "" : dosage);

                m.setPrice(rs.getDouble(4));
                m.setQuantity(rs.getInt(5));

                return m;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Medication> getAll() {

        String sql = "SELECT MedID, Name, Dosage, Price, Quantity " +
                "FROM dbo.Medication ORDER BY Name";

        List<Medication> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Medication m = new Medication();

                m.setMedID(rs.getInt(1));
                m.setName(rs.getString(2));
                String dosage = rs.getString(3);
                m.setDosage(dosage == null ? "" : dosage);
                m.setPrice(rs.getDouble(4));
                m.setQuantity(rs.getInt(5));

                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insert(Medication m) {

        String sql = "INSERT INTO dbo.Medication(Name, Dosage, Price, Quantity) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getName());
            stmt.setString(2, m.getDosage());
            stmt.setDouble(3, m.getPrice());
            stmt.setInt(4, m.getQuantity());

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

    public int update(Medication m) {

        String sql = "UPDATE dbo.Medication " +
                "SET Name = ?, Dosage = ?, Price = ?, Quantity = ? " +
                "WHERE MedID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getName());
            stmt.setString(2, m.getDosage());
            stmt.setDouble(3, m.getPrice());
            stmt.setInt(4, m.getQuantity());
            stmt.setInt(5, m.getMedID());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int delete(int id) {

        String sql = "DELETE FROM dbo.Medication WHERE MedID = ?";

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