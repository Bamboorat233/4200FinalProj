package com.example.comp4200project;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALUser extends DALBase {


    public User authenticate(String username, String password) {

        String sql =
                "SELECT UserID, Username, Password, Role, Email, CreatedDate, IsActive " +
                        "FROM [User] " + "WHERE Username = ? AND Password = ? AND IsActive = 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            User user = new User();
            user.setUserID(rs.getInt("UserID"));
            user.setUsername(rs.getString("Username"));
            user.setPassword(rs.getString("Password"));
            user.setRole(rs.getString("Role"));
            user.setEmail(rs.getString("Email"));
            user.setCreatedDate(rs.getTimestamp("CreatedDate"));
            user.setisActive(rs.getBoolean("IsActive"));

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public void addUser(User u) {

        String sql =
                "INSERT INTO [User] (Username, Password, Role, Email, CreatedDate, IsActive) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getRole());
            stmt.setString(4, u.getEmail());
            stmt.setTimestamp(5, new Timestamp(u.getCreatedDate().getTime()));
            stmt.setBoolean(6, u.getisActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<User> getAllUsers() {

        String sql =
                "SELECT UserID, Username, Password, Role, Email, CreatedDate, IsActive " +
                        "FROM [User] " +
                        "ORDER BY Username";

        List<User> users = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                user.setEmail(rs.getString("Email"));
                user.setCreatedDate(rs.getTimestamp("CreatedDate"));
                user.setisActive(rs.getBoolean("IsActive"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }



    public int updateUser(User u) {

        String sql =
                "UPDATE [User] " +
                        "SET Username = ?, Password = ?, Role = ?, Email = ?, IsActive = ? " +
                        "WHERE UserID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getRole());
            stmt.setString(4, u.getEmail());
            stmt.setBoolean(5, u.getisActive());
            stmt.setInt(6, u.getUserID());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // Delete user
    public int deleteById(int userId) {

        String sql = "DELETE FROM [User] WHERE UserID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
