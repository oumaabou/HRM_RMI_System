package com.crest.hrm.server.impl;

import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.common.exceptions.AuthenticationException;
import com.crest.hrm.common.interfaces.AuthService;
import com.crest.hrm.common.models.Employee;
import java.sql.Date;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {

    private static final String DB_URL = "jdbc:derby://localhost:1527/HRM_DB";
    private static final String USERNAME = "app";
    private static final String PASSWORD = "app";

    private static final Map<String, String> activeSessions = new HashMap<>();

    public AuthServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Employee login(String username, String password)
            throws AuthenticationException, RemoteException {

        String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.ic_number, " +
                     "e.email, e.phone_number, e.hire_date, e.department, " +
                     "u.username, u.password_hash, u.role " +
                     "FROM users u " +
                     "JOIN employees e ON u.employee_id = e.employee_id " +
                     "WHERE u.username = ? AND u.password_hash = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new AuthenticationException("Invalid username or password");
                }

                Employee emp = new Employee();
                emp.setEmployeeId(String.valueOf(rs.getInt("employee_id")));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setIcNumber(rs.getString("ic_number"));
                emp.setEmail(rs.getString("email"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setUsername(rs.getString("username"));
                emp.setPasswordHash(rs.getString("password_hash"));

                Date hireDate = rs.getDate("hire_date");
                if (hireDate != null) {
                    emp.setDateJoined(hireDate.toLocalDate());
                }

                String deptStr = rs.getString("department");
                if (deptStr != null) {
                    emp.setDepartment(parseDepartment(deptStr));
                }

                String roleStr = rs.getString("role");
                if (roleStr != null) {
                    emp.setRole(parseUserRole(roleStr));
                }

                String token = UUID.randomUUID().toString();
                activeSessions.put(token, emp.getEmployeeId());

                return emp;
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error during login", e);
        }
    }

    @Override
    public void logout(String employeeId) throws RemoteException {
        activeSessions.entrySet().removeIf(entry -> entry.getValue().equals(employeeId));
    }

    @Override
    public boolean isSessionValid(String sessionToken) throws RemoteException {
        return activeSessions.containsKey(sessionToken);
    }

    private Department parseDepartment(String value) throws RemoteException {
        try {
            return Department.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RemoteException("Invalid department value in database: " + value, e);
        }
    }

    private UserRole parseUserRole(String value) throws RemoteException {
        try {
            return UserRole.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RemoteException("Invalid role value in database: " + value, e);
        }
    }
}