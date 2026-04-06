package com.crest.hrm.server.dao;

import com.crest.hrm.common.models.Employee;
import java.sql.SQLException;
import java.util.List;

/**
 * EmployeeDAO - Interface for Employee database operations
 * 
 * This interface defines all methods needed to interact with
 * the EMPLOYEES table in the database.
 * 
 * Implementation: EmployeeDAOImpl.java
 */
public interface EmployeeDAO {
    
    /**
     * Save a new employee to the database
     * 
     * @param employee The Employee object to save (employeeId will be ignored, DB generates it)
     * @return The generated employee ID (int)
     * @throws SQLException if database error occurs
     */
    int save(Employee employee) throws SQLException;
    
    /**
     * Find an employee by their ID (int)
     * 
     * @param employeeId The employee ID to search for
     * @return Employee object if found, null if not found
     * @throws SQLException if database error occurs
     */
    Employee findById(int employeeId) throws SQLException;
    
    /**
     * Find an employee by their IC/Passport number
     * 
     * @param icNumber The IC/Passport number to search for
     * @return Employee object if found, null if not found
     * @throws SQLException if database error occurs
     */
    Employee findByIC(String icNumber) throws SQLException;
    
    /**
     * Find an employee by their username (for login)
     * 
     * @param username The username to search for
     * @return Employee object if found, null if not found
     * @throws SQLException if database error occurs
     */
    Employee findByUsername(String username) throws SQLException;
    
    /**
     * Update an existing employee's information
     * 
     * @param employee The Employee object with updated values
     * @return true if update was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean update(Employee employee) throws SQLException;
    
    /**
     * Get all employees from the database
     * 
     * @return List of all Employee objects
     * @throws SQLException if database error occurs
     */
    List<Employee> findAll() throws SQLException;
    
    /**
     * Delete an employee by their ID
     * 
     * @param employeeId The employee ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean delete(int employeeId) throws SQLException;
    
    /**
     * Check if an employee exists by IC number
     * 
     * @param icNumber The IC number to check
     * @return true if employee exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean existsByIC(String icNumber) throws SQLException;
    
    /**
     * Check if an employee exists by username
     * 
     * @param username The username to check
     * @return true if employee exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean existsByUsername(String username) throws SQLException;
}