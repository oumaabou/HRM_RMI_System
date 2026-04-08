package com.crest.hrm.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/HRM_DB";
    private static final String USERNAME = "app";
    private static final String PASSWORD = "app";
    
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Could not connect to database. Make sure Derby server is running.", e);
        }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✓ Database connection test: SUCCESS");
        } catch (SQLException e) {
            System.err.println("✗ Database connection test: FAILED - " + e.getMessage());
        }
    }
}