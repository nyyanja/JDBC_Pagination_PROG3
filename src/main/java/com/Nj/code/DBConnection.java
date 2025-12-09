package com.Nj.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url;
    private final String user;
    private final String password;

    public DBConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    public Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
