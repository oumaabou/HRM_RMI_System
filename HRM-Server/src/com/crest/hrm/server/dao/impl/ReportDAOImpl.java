package com.crest.hrm.server.dao.impl;

import com.crest.hrm.server.dao.ReportDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ReportDAOImpl implements ReportDAO {

    // ========================================================================
    // HELPER - Map ResultSet to LeaveReportRow
    // ========================================================================
    
    private LeaveReportRow mapResultSetToLeaveReportRow(ResultSet rs) throws SQLException {
        LeaveReportRow row = new LeaveReportRow();
        
        row.setLeaveId(rs.getInt("leave_id"));
        row.setEmployeeId(rs.getInt("employee_id"));
        row.setEmployeeName(rs.getString("employee_name"));
        row.setDepartment(rs.getString("department"));
        row.setLeaveType(rs.getString("leave_type"));
        
        java.sql.Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            row.setStartDate(startDate.toString());
        }
        
        java.sql.Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            row.setEndDate(endDate.toString());
        }
        
        row.setTotalDays(rs.getDouble("total_days"));
        row.setReason(rs.getString("reason"));
        row.setStatus(rs.getString("status"));
        
        // approved_by is INT in database, convert to String for display
        int approvedBy = rs.getInt("approved_by");
        row.setApprovedBy(approvedBy == 0 ? "" : String.valueOf(approvedBy));
        
        java.sql.Timestamp appliedDate = rs.getTimestamp("applied_date");
        if (appliedDate != null) {
            row.setAppliedDate(appliedDate.toString());
        }
        
        return row;
    }

    // ========================================================================
    // YEARLY LEAVE REPORTS
    // ========================================================================
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReport(int year) throws SQLException {
        String sql = "SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, " +
                     "l.total_days, l.reason, l.status, l.applied_date, l.approved_by, " +
                     "e.first_name || ' ' || e.last_name as employee_name, e.department " +
                     "FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.employee_id " +
                     "WHERE YEAR(l.start_date) = ? OR YEAR(l.end_date) = ? " +
                     "ORDER BY l.start_date";
        
        List<LeaveReportRow> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            ps.setInt(2, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToLeaveReportRow(rs));
                }
            }
        }
        return reports;
    }
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReportByDepartment(int year, String department) throws SQLException {
        String sql = "SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, " +
                     "l.total_days, l.reason, l.status, l.applied_date, l.approved_by, " +
                     "e.first_name || ' ' || e.last_name as employee_name, e.department " +
                     "FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.employee_id " +
                     "WHERE (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?) AND e.department = ? " +
                     "ORDER BY l.start_date";
        
        List<LeaveReportRow> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            ps.setInt(2, year);
            ps.setString(3, department);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToLeaveReportRow(rs));
                }
            }
        }
        return reports;
    }
    
    @Override
    public List<LeaveReportRow> getYearlyLeaveReportByStatus(int year, String status) throws SQLException {
        String sql = "SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, " +
                     "l.total_days, l.reason, l.status, l.applied_date, l.approved_by, " +
                     "e.first_name || ' ' || e.last_name as employee_name, e.department " +
                     "FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.employee_id " +
                     "WHERE (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?) AND l.status = ? " +
                     "ORDER BY l.start_date";
        
        List<LeaveReportRow> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            ps.setInt(2, year);
            ps.setString(3, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToLeaveReportRow(rs));
                }
            }
        }
        return reports;
    }
    
    // ========================================================================
    // EMPLOYEE-SPECIFIC REPORTS
    // ========================================================================
    
    @Override
    public List<LeaveReportRow> getEmployeeLeaveReport(int employeeId, int year) throws SQLException {
        String sql = "SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, " +
                     "l.total_days, l.reason, l.status, l.applied_date, l.approved_by, " +
                     "e.first_name || ' ' || e.last_name as employee_name, e.department " +
                     "FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.employee_id " +
                     "WHERE l.employee_id = ? AND (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?) " +
                     "ORDER BY l.start_date";
        
        List<LeaveReportRow> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            ps.setInt(3, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToLeaveReportRow(rs));
                }
            }
        }
        return reports;
    }
    
    @Override
    public EmployeeLeaveSummary getEmployeeLeaveSummary(int employeeId, int year) throws SQLException {
        EmployeeLeaveSummary summary = new EmployeeLeaveSummary();
        
        String empSql = "SELECT employee_id, first_name, last_name, department FROM employees WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(empSql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary.setEmployeeId(rs.getInt("employee_id"));
                    summary.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
                    summary.setDepartment(rs.getString("department"));
                }
            }
        }
        
        String leaveSql = "SELECT leave_type, SUM(total_days) as total FROM leaves " +
                          "WHERE employee_id = ? AND status = 'APPROVED' AND YEAR(start_date) = ? " +
                          "GROUP BY leave_type";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(leaveSql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String leaveType = rs.getString("leave_type");
                    double days = rs.getDouble("total");
                    
                    switch (leaveType) {
                        case "ANNUAL":
                            summary.setAnnualLeaveTaken(days);
                            break;
                        case "SICK":
                            summary.setSickLeaveTaken(days);
                            break;
                        case "EMERGENCY":
                            summary.setEmergencyLeaveTaken(days);
                            break;
                    }
                }
            }
        }
        
        String balanceSql = "SELECT annual_remaining, sick_remaining, emergency_remaining FROM leave_balance " +
                            "WHERE employee_id = ? AND leave_year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(balanceSql)) {
            
            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary.setAnnualLeaveRemaining(rs.getDouble("annual_remaining"));
                    summary.setSickLeaveRemaining(rs.getDouble("sick_remaining"));
                    summary.setEmergencyLeaveRemaining(rs.getDouble("emergency_remaining"));
                }
            }
        }
        
        double total = summary.getAnnualLeaveTaken() + summary.getSickLeaveTaken() + summary.getEmergencyLeaveTaken();
        summary.setTotalLeaveTaken(total);
        
        return summary;
    }
    
    // ========================================================================
    // DEPARTMENT REPORTS
    // ========================================================================
    
    @Override
    public List<EmployeeLeaveSummary> getDepartmentLeaveSummary(String department, int year) throws SQLException {
        String sql = "SELECT employee_id FROM employees WHERE department = ?";
        List<Integer> employeeIds = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, department);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employeeIds.add(rs.getInt("employee_id"));
                }
            }
        }
        
        List<EmployeeLeaveSummary> summaries = new ArrayList<>();
        for (int empId : employeeIds) {
            summaries.add(getEmployeeLeaveSummary(empId, year));
        }
        
        return summaries;
    }
    
    @Override
    public Map<String, Double> getDepartmentLeaveTotals(String department, int year) throws SQLException {
        String sql = "SELECT l.leave_type, SUM(l.total_days) as total " +
                     "FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.employee_id " +
                     "WHERE e.department = ? AND l.status = 'APPROVED' " +
                     "AND (YEAR(l.start_date) = ? OR YEAR(l.end_date) = ?) " +
                     "GROUP BY l.leave_type";
        
        Map<String, Double> totals = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, department);
            ps.setInt(2, year);
            ps.setInt(3, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totals.put(rs.getString("leave_type"), rs.getDouble("total"));
                }
            }
        }
        return totals;
    }
    
    // ========================================================================
    // LEAVE BALANCE REPORTS
    // ========================================================================
    
    @Override
    public List<EmployeeLeaveSummary> getEmployeesWithLowBalance(int year, double threshold) throws SQLException {
        String sql = "SELECT lb.employee_id FROM leave_balance lb " +
                     "WHERE lb.leave_year = ? AND (lb.annual_remaining < ? OR lb.sick_remaining < ? OR lb.emergency_remaining < ?)";
        
        List<Integer> employeeIds = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            ps.setDouble(2, threshold);
            ps.setDouble(3, threshold);
            ps.setDouble(4, threshold);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employeeIds.add(rs.getInt("employee_id"));
                }
            }
        }
        
        List<EmployeeLeaveSummary> summaries = new ArrayList<>();
        for (int empId : employeeIds) {
            summaries.add(getEmployeeLeaveSummary(empId, year));
        }
        
        return summaries;
    }
    
    @Override
    public List<EmployeeLeaveSummary> getAllEmployeesLeaveBalance(int year) throws SQLException {
        String sql = "SELECT employee_id FROM employees WHERE is_active = true";
        List<Integer> employeeIds = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employeeIds.add(rs.getInt("employee_id"));
            }
        }
        
        List<EmployeeLeaveSummary> summaries = new ArrayList<>();
        for (int empId : employeeIds) {
            summaries.add(getEmployeeLeaveSummary(empId, year));
        }
        
        return summaries;
    }
    
    // ========================================================================
    // EXPORT REPORTS (CSV/Excel format data)
    // ========================================================================
    
    @Override
    public List<String[]> getYearlyReportForExport(int year) throws SQLException {
        List<LeaveReportRow> reports = getYearlyLeaveReport(year);
        List<String[]> exportData = new ArrayList<>();
        
        exportData.add(new String[]{"Leave ID", "Employee Name", "Department", "Leave Type", 
                                    "Start Date", "End Date", "Total Days", "Reason", "Status", "Applied Date"});
        
        for (LeaveReportRow row : reports) {
            exportData.add(new String[]{
                String.valueOf(row.getLeaveId()),
                row.getEmployeeName(),
                row.getDepartment(),
                row.getLeaveType(),
                row.getStartDate(),
                row.getEndDate(),
                String.valueOf(row.getTotalDays()),
                row.getReason(),
                row.getStatus(),
                row.getAppliedDate()
            });
        }
        
        return exportData;
    }
    
    @Override
    public List<String[]> getEmployeeHistoryForExport(int employeeId, Integer year) throws SQLException {
        List<LeaveReportRow> reports;
        
        if (year != null) {
            reports = getEmployeeLeaveReport(employeeId, year);
        } else {
            String sql = "SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, " +
                         "l.total_days, l.reason, l.status, l.applied_date, l.approved_by, " +
                         "e.first_name || ' ' || e.last_name as employee_name, e.department " +
                         "FROM leaves l " +
                         "JOIN employees e ON l.employee_id = e.employee_id " +
                         "WHERE l.employee_id = ? " +
                         "ORDER BY l.start_date";
            
            reports = new ArrayList<>();
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setInt(1, employeeId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        reports.add(mapResultSetToLeaveReportRow(rs));
                    }
                }
            }
        }
        
        List<String[]> exportData = new ArrayList<>();
        
        exportData.add(new String[]{"Leave ID", "Employee Name", "Department", "Leave Type", 
                                    "Start Date", "End Date", "Total Days", "Reason", "Status", "Applied Date"});
        
        for (LeaveReportRow row : reports) {
            exportData.add(new String[]{
                String.valueOf(row.getLeaveId()),
                row.getEmployeeName(),
                row.getDepartment(),
                row.getLeaveType(),
                row.getStartDate(),
                row.getEndDate(),
                String.valueOf(row.getTotalDays()),
                row.getReason(),
                row.getStatus(),
                row.getAppliedDate()
            });
        }
        
        return exportData;
    }
}