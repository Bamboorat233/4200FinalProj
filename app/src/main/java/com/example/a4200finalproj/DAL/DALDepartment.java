package com.example.comp4200project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALDepartment extends DALBase {

    public Department getById(int id) {

        String sql = "SELECT DepartmentID, Name, Location " +
                "FROM dbo.Department WHERE DepartmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setInt(1, id);
            ResultSet rs = cmd.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Department not found.");
            }

            Department d = new Department();
            d.setDepartmentID(rs.getInt(1));
            d.setName(rs.getString(2));
            d.setLocation(rs.getString(3) == null ? "" : rs.getString(3));

            return d;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Department> getAll() {

        String sql = "SELECT DepartmentID, Name, Location " +
                "FROM dbo.Department ORDER BY Name";

        List<Department> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {

            while (rs.next()) {

                Department d = new Department();
                d.setDepartmentID(rs.getInt(1));
                d.setName(rs.getString(2));
                d.setLocation(rs.getString(3) == null ? "" : rs.getString(3));

                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public int insert(Department d) {

        String sql = "INSERT INTO dbo.Department(Name, Location) VALUES(?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cmd.setString(1, d.getName());
            cmd.setString(2, d.getLocation());

            cmd.executeUpdate();

            ResultSet keys = cmd.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int update(Department d) {

        String sql = "UPDATE dbo.Department " +
                "SET Name = ?, Location = ? " +
                "WHERE DepartmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, d.getName());
            cmd.setString(2, d.getLocation());
            cmd.setInt(3, d.getDepartmentID());

            return cmd.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int delete(int id) {

        String sql = "DELETE FROM dbo.Department WHERE DepartmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setInt(1, id);

            return cmd.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}