package com.crest.hrm.server.dao.impl;

import com.crest.hrm.server.dao.ReportDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.util.*;

/**
 * ReportDAOImpl - Implementation of ReportDAO interface
 * 
 * TODO: Complete this file after Member 2 fixes:
 *       1. LeaveApplication.leaveId from String to Integer
 *       2. LeaveBalance model to match database
 * 
 * This implementation uses the existing database schema:
 * - employees table (employee_id, first_name, last_name, department)
 * - leaves table (leave_id, employee_id, leave_type, start_date, end_date, total_days, reason, status, approved_by, applied_date, approval_date)
 * - leave_balance table (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining)
 */
public class ReportDAOImpl implements ReportDAO {

    // ========================================================================
    // YEARLY LEAVE REPORTS
    // ========================================================================
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReport(int year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: SELECT l.*, e.first_name, e.last_name, e.department, 
        //      CONCAT(e.first_name, ' ', e.last_name) as employee_name
        //      FROM leaves l
        //      JOIN employees e ON l.employee_id = e.employee_id
        //      WHERE YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?
        //      ORDER BY l.start_date
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes leaveId type");
    }
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReportByDepartment(int year, String department) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // Same as above with AND e.department = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReportByStatus(int year, String status) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // Same as getYearlyLeaveReport with AND l.status = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // EMPLOYEE-SPECIFIC REPORTS
    // ========================================================================
    
    @Override
    public List<LeaveReportRow> getEmployeeLeaveReport(int employeeId, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: SELECT l.*, e.first_name, e.last_name, e.department
        //      FROM leaves l
        //      JOIN employees e ON l.employee_id = e.employee_id
        //      WHERE l.employee_id = ? AND (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?)
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public EmployeeLeaveSummary getEmployeeLeaveSummary(int employeeId, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // 1. Get leave taken totals from leaves table
        // 2. Get remaining balances from leave_balance table
        // 3. Combine into EmployeeLeaveSummary object
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes LeaveBalance model");
    }
    
    // ========================================================================
    // DEPARTMENT REPORTS
    // ========================================================================
    
    @Override
    public List<EmployeeLeaveSummary> getDepartmentLeaveSummary(String department, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // Get all employees in department, then get leave summary for each
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public Map<String, Double> getDepartmentLeaveTotals(String department, int year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // SQL: SELECT leave_type, SUM(total_days) as total
        //      FROM leaves l
        //      JOIN employees e ON l.employee_id = e.employee_id
        //      WHERE e.department = ? AND l.status = 'APPROVED'
        //      AND (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?)
        //      GROUP BY leave_type
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // LEAVE BALANCE REPORTS
    // ========================================================================
    
    @Override
    public List<EmployeeLeaveSummary> getEmployeesWithLowBalance(int year, double threshold) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // Get all employees where annual_remaining < threshold
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public List<EmployeeLeaveSummary> getAllEmployeesLeaveBalance(int year) throws SQLException {
        // TODO: Implement after Member 2 fixes LeaveBalance model
        // Get leave summary for all employees
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // EXPORT REPORTS
    // ========================================================================
    
    @Override
    public List<String[]> getYearlyReportForExport(int year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // Get yearly report data, convert each row to String array for CSV/Excel
        // Format: [LeaveID, EmployeeName, Department, LeaveType, StartDate, EndDate, TotalDays, Status]
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    @Override
    public List<String[]> getEmployeeHistoryForExport(int employeeId, Integer year) throws SQLException {
        // TODO: Implement after Member 2 fixes
        // Get employee leave history, convert to String arrays for export
        throw new UnsupportedOperationException("TODO: Implement after Member 2 fixes");
    }
    
    // ========================================================================
    // HELPER METHODS (TODO when implementing)
    // ========================================================================
    
    // private LeaveReportRow mapResultSetToLeaveReportRow(ResultSet rs) throws SQLException {
    //     TODO: Map database row to LeaveReportRow object
    // }
    
    // private EmployeeLeaveSummary mapResultSetToEmployeeLeaveSummary(ResultSet rs) throws SQLException {
    //     TODO: Map database row to EmployeeLeaveSummary object
    // }
}