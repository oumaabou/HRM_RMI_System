package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveDAOImpl - Complete Implementation of LeaveDAO interface
 */
public class LeaveDAOImpl implements LeaveDAO {

    // ========================================================================
    // LEAVE APPLICATION OPERATIONS
    // ========================================================================
    
    @Override
    public int saveLeave(LeaveApplication leave) throws SQLException {
        String sql = "INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status, applied_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, leave.getEmployeeId());
            // Use .name() to get "ANNUAL" not "Annual Leave"
            ps.setString(2, leave.getLeaveType().name());
            ps.setDate(3, Date.valueOf(leave.getStartDate()));
            ps.setDate(4, Date.valueOf(leave.getEndDate()));
            ps.setDouble(5, leave.getTotalDays());
            ps.setString(6, leave.getReason());
            ps.setString(7, leave.getStatus().name());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating leave application failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int leaveId = generatedKeys.getInt(1);
                    leave.setLeaveId(leaveId);
                    return leaveId;
                } else {
                    throw new SQLException("Creating leave application failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public LeaveApplication findLeaveById(int leaveId) throws SQLException {
        String sql = "SELECT * FROM leaves WHERE leave_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, leaveId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeaveApplication(rs);
                }
                return null;
            }
        }
    }
    
    @Override
    public List<LeaveApplication> findLeavesByEmployee(int employeeId) throws SQLException {
        String sql = "SELECT * FROM leaves WHERE employee_id = ? ORDER BY start_date DESC";
        List<LeaveApplication> leaves = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapResultSetToLeaveApplication(rs));
                }
            }
        }
        return leaves;
    }
    
    @Override
    public List<LeaveApplication> findPendingLeaves() throws SQLException {
        String sql = "SELECT * FROM leaves WHERE status = 'PENDING' ORDER BY applied_date";
        List<LeaveApplication> leaves = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                leaves.add(mapResultSetToLeaveApplication(rs));
            }
        }
        return leaves;
    }
    
    @Override
    public boolean updateLeaveStatus(int leaveId, String status, int approvedBy) throws SQLException {
        String sql = "UPDATE leaves SET status = ?, approved_by = ?, approval_date = CURRENT_TIMESTAMP WHERE leave_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, approvedBy);
            ps.setInt(3, leaveId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    @Override
    public boolean cancelLeaveApplication(int leaveId) throws SQLException {
        String sql = "UPDATE leaves SET status = 'CANCELLED' WHERE leave_id = ? AND status = 'PENDING'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, leaveId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    // ========================================================================
    // LEAVE BALANCE OPERATIONS
    // ========================================================================
    
    @Override
    public LeaveBalance getLeaveBalance(int employeeId, int year) throws SQLException {
        String sql = "SELECT * FROM leave_balance WHERE employee_id = ? AND leave_year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeaveBalance(rs);
                }
                return null;
            }
        }
    }
    
    @Override
    public boolean updateLeaveBalance(int employeeId, int year, double daysUsed, String leaveType) throws SQLException {
        String columnName;
        switch (leaveType.toUpperCase()) {
            case "ANNUAL":
                columnName = "annual_remaining";
                break;
            case "SICK":
                columnName = "sick_remaining";
                break;
            case "EMERGENCY":
                columnName = "emergency_remaining";
                break;
            default:
                return false;
        }
        
        String sql = "UPDATE leave_balance SET " + columnName + " = " + columnName + " - ? " +
                     "WHERE employee_id = ? AND leave_year = ? AND " + columnName + " >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, daysUsed);
            ps.setInt(2, employeeId);
            ps.setInt(3, year);
            ps.setDouble(4, daysUsed);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    @Override
    public boolean initializeLeaveBalance(int employeeId, int year) throws SQLException {
        String sql = "INSERT INTO leave_balance (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining) " +
                     "VALUES (?, ?, 14.0, 14.0, 3.0)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    @Override
    public boolean hasSufficientBalance(int employeeId, int year, double daysRequested, String leaveType) throws SQLException {
        LeaveBalance balance = getLeaveBalance(employeeId, year);
        if (balance == null) {
            return false;
        }
        
        switch (leaveType.toUpperCase()) {
            case "ANNUAL":
                return balance.getAnnualRemaining() >= daysRequested;
            case "SICK":
                return balance.getSickRemaining() >= daysRequested;
            case "EMERGENCY":
                return balance.getEmergencyRemaining() >= daysRequested;
            default:
                return false;
        }
    }
    
    // ========================================================================
    // REPORT OPERATIONS
    // ========================================================================
    
    @Override
    public List<LeaveApplication> getYearlyReport(int year) throws SQLException {
        String sql = "SELECT * FROM leaves WHERE YEAR(start_date) = ? OR YEAR(end_date) = ? ORDER BY start_date";
        List<LeaveApplication> leaves = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            ps.setInt(2, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapResultSetToLeaveApplication(rs));
                }
            }
        }
        return leaves;
    }
    
    @Override
    public List<LeaveApplication> getEmployeeYearlyReport(int employeeId, int year) throws SQLException {
        String sql = "SELECT * FROM leaves WHERE employee_id = ? AND (YEAR(start_date) = ? OR YEAR(end_date) = ?) ORDER BY start_date";
        List<LeaveApplication> leaves = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            ps.setInt(3, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapResultSetToLeaveApplication(rs));
                }
            }
        }
        return leaves;
    }
    
    @Override
    public double getTotalLeaveDaysTaken(int employeeId, int year, String leaveType) throws SQLException {
        String sql;
        if (leaveType == null || leaveType.isEmpty()) {
            sql = "SELECT SUM(total_days) FROM leaves WHERE employee_id = ? AND status = 'APPROVED' AND YEAR(start_date) = ?";
        } else {
            sql = "SELECT SUM(total_days) FROM leaves WHERE employee_id = ? AND status = 'APPROVED' AND YEAR(start_date) = ? AND leave_type = ?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            if (leaveType != null && !leaveType.isEmpty()) {
                ps.setString(3, leaveType);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
                return 0;
            }
        }
    }
    
    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    
    private LeaveApplication mapResultSetToLeaveApplication(ResultSet rs) throws SQLException {
        LeaveApplication leave = new LeaveApplication();
        
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getInt("employee_id"));
        
        // Convert database string to enum (handle both formats)
        String leaveTypeStr = rs.getString("leave_type");
        if (leaveTypeStr != null) {
            switch (leaveTypeStr.toUpperCase()) {
                case "ANNUAL":
                case "ANNUAL LEAVE":
                    leave.setLeaveType(LeaveType.ANNUAL);
                    break;
                case "SICK":
                case "SICK LEAVE":
                    leave.setLeaveType(LeaveType.SICK);
                    break;
                case "EMERGENCY":
                case "EMERGENCY LEAVE":
                    leave.setLeaveType(LeaveType.EMERGENCY);
                    break;
                case "MATERNITY":
                case "MATERNITY LEAVE":
                    leave.setLeaveType(LeaveType.MATERNITY);
                    break;
                case "PATERNITY":
                case "PATERNITY LEAVE":
                    leave.setLeaveType(LeaveType.PATERNITY);
                    break;
                case "UNPAID":
                case "UNPAID LEAVE":
                    leave.setLeaveType(LeaveType.UNPAID);
                    break;
                default:
                    leave.setLeaveType(LeaveType.ANNUAL);
            }
        }
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            leave.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            leave.setEndDate(endDate.toLocalDate());
        }
        
        leave.setTotalDays(rs.getInt("total_days"));
        leave.setReason(rs.getString("reason"));
        
        // Convert database string to enum
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            switch (statusStr.toUpperCase()) {
                case "PENDING":
                    leave.setStatus(LeaveStatus.PENDING);
                    break;
                case "APPROVED":
                    leave.setStatus(LeaveStatus.APPROVED);
                    break;
                case "REJECTED":
                    leave.setStatus(LeaveStatus.REJECTED);
                    break;
                case "CANCELLED":
                    leave.setStatus(LeaveStatus.CANCELLED);
                    break;
                default:
                    leave.setStatus(LeaveStatus.PENDING);
            }
        }
        
        leave.setReviewedBy(rs.getInt("approved_by"));
        leave.setAppliedDate(rs.getTimestamp("applied_date"));
        
        return leave;
    }
    
    private LeaveBalance mapResultSetToLeaveBalance(ResultSet rs) throws SQLException {
        LeaveBalance balance = new LeaveBalance();
        
        balance.setBalanceId(rs.getInt("balance_id"));
        balance.setEmployeeId(rs.getInt("employee_id"));
        balance.setLeaveYear(rs.getInt("leave_year"));
        balance.setAnnualRemaining(rs.getDouble("annual_remaining"));
        balance.setSickRemaining(rs.getDouble("sick_remaining"));
        balance.setEmergencyRemaining(rs.getDouble("emergency_remaining"));
        
        return balance;
    }
}