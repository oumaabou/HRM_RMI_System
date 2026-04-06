package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveDAOImpl - Implementation of LeaveDAO interface
 * 
 * TODO: Complete this file after Member 2 fixes:
 *       1. LeaveApplication.leaveId from String to Integer
 *       2. LeaveBalance model to match database (annualRemaining, sickRemaining, emergencyRemaining)
 */
public class LeaveDAOImpl implements LeaveDAO {

    // ========================================================================
    // LEAVE APPLICATION OPERATIONS
    // ========================================================================
    
    @Override
    public int saveLeave(LeaveApplication leave) throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status, applied_date)
        //      VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        // Return generated leave_id
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes leaveId type");
    }
    
    @Override
    public LeaveApplication findLeaveById(int leaveId) throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: SELECT * FROM leaves WHERE leave_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes leaveId type");
    }
    
    @Override
    public List<LeaveApplication> findLeavesByEmployee(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: SELECT * FROM leaves WHERE employee_id = ? ORDER BY start_date DESC
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes leaveId type");
    }
    
    @Override
    public List<LeaveApplication> findPendingLeaves() throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: SELECT * FROM leaves WHERE status = 'PENDING' ORDER BY applied_date
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes leaveId type");
    }
    
    @Override
    public boolean updateLeaveStatus(int leaveId, String status, int approvedBy) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: UPDATE leaves SET status = ?, approved_by = ?, approval_date = CURRENT_TIMESTAMP WHERE leave_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public boolean cancelLeaveApplication(int leaveId) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: UPDATE leaves SET status = 'CANCELLED' WHERE leave_id = ? AND status = 'PENDING'
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // LEAVE BALANCE OPERATIONS
    // ========================================================================
    
    @Override
    public LeaveBalance getLeaveBalance(int employeeId, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model to match database
        // Database has: annual_remaining, sick_remaining, emergency_remaining
        // SQL: SELECT * FROM leave_balance WHERE employee_id = ? AND leave_year = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes LeaveBalance model");
    }
    
    @Override
    public boolean updateLeaveBalance(int employeeId, int year, double daysUsed, String leaveType) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // SQL: UPDATE leave_balance SET 
        //      annual_remaining = annual_remaining - ? WHERE leave_type = 'ANNUAL'
        //      (similar for SICK, EMERGENCY)
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public boolean initializeLeaveBalance(int employeeId, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // SQL: INSERT INTO leave_balance (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining)
        //      VALUES (?, ?, 14, 14, 3)
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public boolean hasSufficientBalance(int employeeId, int year, double daysRequested, String leaveType) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // Get balance, compare daysRequested to remaining days for leaveType
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // REPORT OPERATIONS
    // ========================================================================
    
    @Override
    public List<LeaveApplication> getYearlyReport(int year) throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: SELECT * FROM leaves WHERE YEAR(start_date) = ? OR YEAR(end_date) = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public List<LeaveApplication> getEmployeeYearlyReport(int employeeId, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes leaveId from String to Integer
        // SQL: SELECT * FROM leaves WHERE employee_id = ? AND (YEAR(start_date) = ? OR YEAR(end_date) = ?)
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public double getTotalLeaveDaysTaken(int employeeId, int year, String leaveType) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: SELECT SUM(total_days) FROM leaves 
        //      WHERE employee_id = ? AND status = 'APPROVED' 
        //      AND YEAR(start_date) = ? AND (leave_type = ? OR ? IS NULL)
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // HELPER METHODS (TODO when implementing)
    // ========================================================================
    
    // private LeaveApplication mapResultSetToLeaveApplication(ResultSet rs) throws SQLException {
    //     TODO: Map database row to LeaveApplication object
    //     Requires leaveId as Integer from database
    // }
    
    // private LeaveBalance mapResultSetToLeaveBalance(ResultSet rs) throws SQLException {
    //     TODO: Map database row to LeaveBalance object
    //     Requires LeaveBalance model to match database
    // }
}