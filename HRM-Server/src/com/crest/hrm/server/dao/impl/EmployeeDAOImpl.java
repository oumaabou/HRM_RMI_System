package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAOImpl - Implementation of EmployeeDAO interface
 * 
 * Handles all database operations for Employee entities.
 * Works with employees table AND users table (since Employee contains login info).
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    // ========================================================================
    // SAVE - Insert new employee into both employees and users tables
    // ========================================================================
    @Override
    public int save(Employee employee) throws SQLException {
        String employeeSql = "INSERT INTO employees (first_name, last_name, ic_number, address, gender, phone_number, email, hire_date, department, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(employeeSql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set employee fields
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getIcNumber());
            ps.setString(4, null); // address - not in model yet
            ps.setString(5, null); // gender - not in model yet
            ps.setString(6, employee.getPhoneNumber());
            ps.setString(7, employee.getEmail());
            ps.setDate(8, Date.valueOf(employee.getDateJoined()));
            ps.setString(9, employee.getDepartment() != null ? employee.getDepartment().toString() : null);
            ps.setBoolean(10, true); // is_active = true
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating employee failed, no rows affected.");
            }
            
            // Get the generated employee_id
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);
                    employee.setEmployeeId(employeeId);
                    
                    // Now insert into users table
                    insertUser(conn, employeeId, employee.getUsername(), employee.getPasswordHash(), employee.getRole());
                    
                    return employeeId;
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        }
    }
    
    private void insertUser(Connection conn, int employeeId, String username, String passwordHash, UserRole role) throws SQLException {
        String userSql = "INSERT INTO users (employee_id, username, password_hash, salt, role) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(userSql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setString(4, "default_salt"); // In production, generate random salt
            ps.setString(5, role != null ? role.toString() : "EMPLOYEE");
            ps.executeUpdate();
        }
    }

    // ========================================================================
    // FIND BY ID
    // ========================================================================
    @Override
    public Employee findById(int employeeId) throws SQLException {
        String sql = "SELECT e.*, u.username, u.password_hash, u.role FROM employees e " +
                     "LEFT JOIN users u ON e.employee_id = u.employee_id " +
                     "WHERE e.employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            }
        }
    }

    // ========================================================================
    // FIND BY IC NUMBER
    // ========================================================================
    @Override
    public Employee findByIC(String icNumber) throws SQLException {
        String sql = "SELECT e.*, u.username, u.password_hash, u.role FROM employees e " +
                     "LEFT JOIN users u ON e.employee_id = u.employee_id " +
                     "WHERE e.ic_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, icNumber);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            }
        }
    }

    // ========================================================================
    // FIND BY USERNAME (for login)
    // ========================================================================
    @Override
    public Employee findByUsername(String username) throws SQLException {
        String sql = "SELECT e.*, u.username, u.password_hash, u.role FROM employees e " +
                     "INNER JOIN users u ON e.employee_id = u.employee_id " +
                     "WHERE u.username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            }
        }
    }

    // ========================================================================
    // UPDATE - Update both employees and users tables
    // ========================================================================
    @Override
    public boolean update(Employee employee) throws SQLException {
        String employeeSql = "UPDATE employees SET first_name = ?, last_name = ?, ic_number = ?, " +
                             "phone_number = ?, email = ?, department = ? WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(employeeSql)) {
            
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getIcNumber());
            ps.setString(4, employee.getPhoneNumber());
            ps.setString(5, employee.getEmail());
            ps.setString(6, employee.getDepartment() != null ? employee.getDepartment().toString() : null);
            ps.setInt(7, employee.getEmployeeId());
            
            int affectedRows = ps.executeUpdate();
            
            // Update users table
            if (affectedRows > 0 && employee.getUsername() != null) {
                updateUser(conn, employee);
            }
            
            return affectedRows > 0;
        }
    }
    
    private void updateUser(Connection conn, Employee employee) throws SQLException {
        String userSql = "UPDATE users SET username = ?, password_hash = ?, role = ? WHERE employee_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(userSql)) {
            ps.setString(1, employee.getUsername());
            ps.setString(2, employee.getPasswordHash());
            ps.setString(3, employee.getRole() != null ? employee.getRole().toString() : "EMPLOYEE");
            ps.setInt(4, employee.getEmployeeId());
            ps.executeUpdate();
        }
    }

    // ========================================================================
    // FIND ALL EMPLOYEES
    // ========================================================================
    @Override
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT e.*, u.username, u.password_hash, u.role FROM employees e " +
                     "LEFT JOIN users u ON e.employee_id = u.employee_id";
        
        List<Employee> employees = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        
        return employees;
    }

    // ========================================================================
    // DELETE
    // ========================================================================
    @Override
    public boolean delete(int employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // ========================================================================
    // EXISTS BY IC
    // ========================================================================
    @Override
    public boolean existsByIC(String icNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE ic_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, icNumber);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // ========================================================================
    // EXISTS BY USERNAME
    // ========================================================================
    @Override
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // ========================================================================
    // HELPER - Map ResultSet to Employee object
    // ========================================================================
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        
        // From employees table
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setIcNumber(rs.getString("ic_number"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setEmail(rs.getString("email"));
        
        // Handle date
        Date hireDate = rs.getDate("hire_date");
        if (hireDate != null) {
            employee.setDateJoined(hireDate.toLocalDate());
        }
        
        // Handle department enum
        String deptStr = rs.getString("department");
        if (deptStr != null) {
            try {
                employee.setDepartment(Department.valueOf(deptStr.replace(" ", "_").toUpperCase()));
            } catch (IllegalArgumentException e) {
                // If enum doesn't match, ignore
            }
        }
        
        // From users table
        String username = rs.getString("username");
        if (username != null) {
            employee.setUsername(username);
        }
        
        String passwordHash = rs.getString("password_hash");
        if (passwordHash != null) {
            employee.setPasswordHash(passwordHash);
        }
        
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            try {
                employee.setRole(UserRole.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                employee.setRole(UserRole.EMPLOYEE);
            }
        }
        
        return employee;
    }
}