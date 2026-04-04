package com.crest.hrm.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/HRM_DB";
    private static final String USERNAME = "app";
    private static final String PASSWORD = "app";
    
    private static int activeConnections = 0;
    private static int totalConnectionsOpened = 0;
    private static final int MAX_CONNECTIONS = 20;
    
    public static Connection getConnection() throws SQLException {
        if (activeConnections >= MAX_CONNECTIONS) {
            throw new SQLException("Maximum connections reached (" + MAX_CONNECTIONS + "). Please try again later.");
        }
        
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            activeConnections++;
            totalConnectionsOpened++;
            return conn;
            
        } catch (SQLException e) {
            throw new SQLException("Could not connect to database. Make sure Derby server is running.", e);
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    activeConnections--;
                }
            } catch (SQLException e) {
                // Log error silently or handle as needed
            }
        }
    }
    
    public static int getActiveConnectionCount() {
        return activeConnections;
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✓ Database connection test: SUCCESS");
        } catch (SQLException e) {
            System.err.println("✗ Database connection test: FAILED - " + e.getMessage());
        }
    }
}