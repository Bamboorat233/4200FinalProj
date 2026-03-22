package com.example.comp4200project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALAppointment extends DALBase {

    //CREATE
    public void addAppointment(Appointment a) {
        String sql =
                "INSERT INTO Appointment (PatientID, DoctorID, AppointmentDate, AppointmentTime, Status) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getPatientID());
            stmt.setInt(2, a.getDoctorID());
            stmt.setTimestamp(3, new Timestamp(a.getAppointmentDate().getTime()));
            stmt.setTime(4, a.getAppointmentTime()); // java.sql.Time
            stmt.setString(5, a.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Appointment getAppointmentById(int id) {
        String sql =
                "SELECT AppointmentID, PatientID, DoctorID, AppointmentDate, AppointmentTime, Status " +
                        "FROM Appointment WHERE AppointmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentID(rs.getInt(1));
                    a.setPatientID(rs.getInt(2));
                    a.setDoctorID(rs.getInt(3));
                    a.setAppointmentDate(rs.getDate(4));
                    a.setAppointmentTime(rs.getTime(5)); // maps from SQL TIME
                    a.setStatus(rs.getString(6));
                    return a;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //get all
    public List<Appointment> getAllAppointments() {
        String sql =
                "SELECT AppointmentID, PatientID, DoctorID, AppointmentDate, AppointmentTime, Status " +
                        "FROM Appointment";

        List<Appointment> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentID(rs.getInt(1));
                a.setPatientID(rs.getInt(2));
                a.setDoctorID(rs.getInt(3));
                a.setAppointmentDate(rs.getDate(4));
                a.setAppointmentTime(rs.getTime(5));
                a.setStatus(rs.getString(6));

                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //UPDATE
    public void updateAppointment(Appointment a) {
        String sql =
                "UPDATE Appointment SET " +
                        "PatientID = ?, DoctorID = ?, AppointmentDate = ?, AppointmentTime = ?, Status = ? " +
                        "WHERE AppointmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getPatientID());
            stmt.setInt(2, a.getDoctorID());
            stmt.setTimestamp(3, new Timestamp(a.getAppointmentDate().getTime()));
            stmt.setTime(4, a.getAppointmentTime());
            stmt.setString(5, a.getStatus());
            stmt.setInt(6, a.getAppointmentID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public void deleteAppointment(int id) {
        String sql = "DELETE FROM Appointment WHERE AppointmentID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}