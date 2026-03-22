package com.example.comp4200project;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DALBase {

    protected String connStr;

    public DALBase() {
        try {
            Properties prop = new Properties();

            try (InputStream input = Objects.requireNonNull(
                    Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("db.properties"),
                    "db.properties file not found")) {

                prop.load(input);
                connStr = prop.getProperty("HsConn");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Helper method
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connStr);
    }
}
