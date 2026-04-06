package com.crest.hrm.server.dao;

import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import java.sql.SQLException;
import java.util.List;

/**
 * LeaveDAO - Interface for Leave database operations
 * 
 * Handles all leave-related database operations including:
 * - Leave applications (CRUD)
 * - Leave balance management
 * - Leave reports
 * 
 * Implementation: LeaveDAOImpl.java
 */
public interface LeaveDAO {
    
    // ========================================================================
    // LEAVE APPLICATION OPERATIONS
    // ========================================================================
    
    /**
     * Save a new leave application
     * 
     * @param leave The LeaveApplication object to save
     * @return The generated leave ID (int)
     * @throws SQLException if database error occurs
     */
    int saveLeave(LeaveApplication leave) throws SQLException;
    
    /**
     * Find a leave application by its ID
     * 
     * @param leaveId The leave ID to search for
     * @return LeaveApplication object if found, null if not found
     * @throws SQLException if database error occurs
     */
    LeaveApplication findLeaveById(int leaveId) throws SQLException;
    
    /**
     * Find all leave applications for a specific employee
     * 
     * @param employeeId The employee ID to search for
     * @return List of LeaveApplication objects (empty if none)
     * @throws SQLException if database error occurs
     */
    List<LeaveApplication> findLeavesByEmployee(int employeeId) throws SQLException;
    
    /**
     * Find all pending leave applications (for HR approval)
     * 
     * @return List of pending LeaveApplication objects
     * @throws SQLException if database error occurs
     */
    List<LeaveApplication> findPendingLeaves() throws SQLException;
    
    /**
     * Update the status of a leave application (approve/reject)
     * 
     * @param leaveId The leave ID to update
     * @param status New status (APPROVED, REJECTED, CANCELLED)
     * @param approvedBy Employee ID of HR staff who approved/rejected
     * @return true if update was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updateLeaveStatus(int leaveId, String status, int approvedBy) throws SQLException;
    
    /**
     * Cancel a pending leave application (employee cancels before approval)
     * 
     * @param leaveId The leave ID to cancel
     * @return true if cancellation was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean cancelLeaveApplication(int leaveId) throws SQLException;
    
    // ========================================================================
    // LEAVE BALANCE OPERATIONS
    // ========================================================================
    
    /**
     * Get leave balance for an employee for a specific year
     * 
     * @param employeeId The employee ID
     * @param year The year (e.g., 2026)
     * @return LeaveBalance object with remaining days, null if not found
     * @throws SQLException if database error occurs
     */
    LeaveBalance getLeaveBalance(int employeeId, int year) throws SQLException;
    
    /**
     * Update leave balance after a leave is approved
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @param daysUsed Number of days taken
     * @param leaveType Type of leave (ANNUAL, SICK, EMERGENCY)
     * @return true if update was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updateLeaveBalance(int employeeId, int year, double daysUsed, String leaveType) throws SQLException;
    
    /**
     * Create or initialize leave balance for a new employee
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @return true if creation was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean initializeLeaveBalance(int employeeId, int year) throws SQLException;
    
    /**
     * Check if employee has sufficient leave balance
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @param daysRequested Number of days requested
     * @param leaveType Type of leave (ANNUAL, SICK, EMERGENCY)
     * @return true if sufficient balance exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean hasSufficientBalance(int employeeId, int year, double daysRequested, String leaveType) throws SQLException;
    
    // ========================================================================
    // REPORT OPERATIONS
    // ========================================================================
    
    /**
     * Get all leave applications for a specific year (for HR reports)
     * 
     * @param year The year (e.g., 2026)
     * @return List of all LeaveApplication objects for that year
     * @throws SQLException if database error occurs
     */
    List<LeaveApplication> getYearlyReport(int year) throws SQLException;
    
    /**
     * Get all leave applications for a specific employee and year
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @return List of LeaveApplication objects for that employee and year
     * @throws SQLException if database error occurs
     */
    List<LeaveApplication> getEmployeeYearlyReport(int employeeId, int year) throws SQLException;
    
    /**
     * Get total leave days taken by an employee for a specific year
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @param leaveType Type of leave (or null for all types)
     * @return Total days taken
     * @throws SQLException if database error occurs
     */
    double getTotalLeaveDaysTaken(int employeeId, int year, String leaveType) throws SQLException;
}