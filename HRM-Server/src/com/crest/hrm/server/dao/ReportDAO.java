package com.crest.hrm.server.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * ReportDAO - Interface for Report Generation database operations
 * 
 * Handles all report-related database queries including:
 * - Yearly leave reports for HR
 * - Employee-specific leave summaries
 * - Leave balance reports
 * 
 * Implementation: ReportDAOImpl.java
 */
public interface ReportDAO {
    
    // ========================================================================
    // LEAVE REPORT STRUCTURES
    // ========================================================================
    
    /**
     * Data class for yearly leave report row
     * Used by HR to see all leave applications for a year
     */
    public static class LeaveReportRow {
        private int leaveId;
        private int employeeId;
        private String employeeName;
        private String department;
        private String leaveType;
        private String startDate;
        private String endDate;
        private double totalDays;
        private String reason;
        private String status;
        private String approvedBy;
        private String appliedDate;
        
        // Getters and setters
        public int getLeaveId() { return leaveId; }
        public void setLeaveId(int leaveId) { this.leaveId = leaveId; }
        public int getEmployeeId() { return employeeId; }
        public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getLeaveType() { return leaveType; }
        public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public double getTotalDays() { return totalDays; }
        public void setTotalDays(double totalDays) { this.totalDays = totalDays; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getApprovedBy() { return approvedBy; }
        public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
        public String getAppliedDate() { return appliedDate; }
        public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }
    }
    
    /**
     * Data class for employee leave summary (used in reports)
     */
    public static class EmployeeLeaveSummary {
        private int employeeId;
        private String employeeName;
        private String department;
        private double annualLeaveTaken;
        private double sickLeaveTaken;
        private double emergencyLeaveTaken;
        private double totalLeaveTaken;
        private double annualLeaveRemaining;
        private double sickLeaveRemaining;
        private double emergencyLeaveRemaining;
        
        // Getters and setters
        public int getEmployeeId() { return employeeId; }
        public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public double getAnnualLeaveTaken() { return annualLeaveTaken; }
        public void setAnnualLeaveTaken(double annualLeaveTaken) { this.annualLeaveTaken = annualLeaveTaken; }
        public double getSickLeaveTaken() { return sickLeaveTaken; }
        public void setSickLeaveTaken(double sickLeaveTaken) { this.sickLeaveTaken = sickLeaveTaken; }
        public double getEmergencyLeaveTaken() { return emergencyLeaveTaken; }
        public void setEmergencyLeaveTaken(double emergencyLeaveTaken) { this.emergencyLeaveTaken = emergencyLeaveTaken; }
        public double getTotalLeaveTaken() { return totalLeaveTaken; }
        public void setTotalLeaveTaken(double totalLeaveTaken) { this.totalLeaveTaken = totalLeaveTaken; }
        public double getAnnualLeaveRemaining() { return annualLeaveRemaining; }
        public void setAnnualLeaveRemaining(double annualLeaveRemaining) { this.annualLeaveRemaining = annualLeaveRemaining; }
        public double getSickLeaveRemaining() { return sickLeaveRemaining; }
        public void setSickLeaveRemaining(double sickLeaveRemaining) { this.sickLeaveRemaining = sickLeaveRemaining; }
        public double getEmergencyLeaveRemaining() { return emergencyLeaveRemaining; }
        public void setEmergencyLeaveRemaining(double emergencyLeaveRemaining) { this.emergencyLeaveRemaining = emergencyLeaveRemaining; }
    }
    
    // ========================================================================
    // YEARLY LEAVE REPORTS (for HR)
    // ========================================================================
    
    /**
     * Get complete yearly leave report for all employees
     * 
     * @param year The year (e.g., 2026)
     * @return List of LeaveReportRow objects for all approved leaves in that year
     * @throws SQLException if database error occurs
     */
    List<LeaveReportRow> getYearlyLeaveReport(int year) throws SQLException;
    
    /**
     * Get yearly leave report filtered by department
     * 
     * @param year The year
     * @param department The department name (e.g., "IT", "HR")
     * @return List of LeaveReportRow objects for that department
     * @throws SQLException if database error occurs
     */
    List<LeaveReportRow> getYearlyLeaveReportByDepartment(int year, String department) throws SQLException;
    
    /**
     * Get yearly leave report filtered by status
     * 
     * @param year The year
     * @param status The status (APPROVED, REJECTED, PENDING)
     * @return List of LeaveReportRow objects with that status
     * @throws SQLException if database error occurs
     */
    List<LeaveReportRow> getYearlyLeaveReportByStatus(int year, String status) throws SQLException;
    
    // ========================================================================
    // EMPLOYEE-SPECIFIC REPORTS
    // ========================================================================
    
    /**
     * Get leave report for a specific employee for a specific year
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @return List of LeaveReportRow objects for that employee
     * @throws SQLException if database error occurs
     */
    List<LeaveReportRow> getEmployeeLeaveReport(int employeeId, int year) throws SQLException;
    
    /**
     * Get leave summary for a specific employee (taken + remaining)
     * 
     * @param employeeId The employee ID
     * @param year The year
     * @return EmployeeLeaveSummary object with totals
     * @throws SQLException if database error occurs
     */
    EmployeeLeaveSummary getEmployeeLeaveSummary(int employeeId, int year) throws SQLException;
    
    // ========================================================================
    // DEPARTMENT REPORTS
    // ========================================================================
    
    /**
     * Get leave summary for all employees in a department
     * 
     * @param department The department name
     * @param year The year
     * @return List of EmployeeLeaveSummary objects for that department
     * @throws SQLException if database error occurs
     */
    List<EmployeeLeaveSummary> getDepartmentLeaveSummary(String department, int year) throws SQLException;
    
    /**
     * Get department-wide totals for a specific year
     * 
     * @param department The department name
     * @param year The year
     * @return Map with totals (total days taken, by leave type)
     * @throws SQLException if database error occurs
     */
    Map<String, Double> getDepartmentLeaveTotals(String department, int year) throws SQLException;
    
    // ========================================================================
    // LEAVE BALANCE REPORTS
    // ========================================================================
    
    /**
     * Get all employees with low leave balance (for HR alert)
     * 
     * @param year The year
     * @param threshold Days remaining threshold (e.g., less than 3 days)
     * @return List of employees with low balance
     * @throws SQLException if database error occurs
     */
    List<EmployeeLeaveSummary> getEmployeesWithLowBalance(int year, double threshold) throws SQLException;
    
    /**
     * Get complete leave balance report for all employees
     * 
     * @param year The year
     * @return List of EmployeeLeaveSummary for all employees
     * @throws SQLException if database error occurs
     */
    List<EmployeeLeaveSummary> getAllEmployeesLeaveBalance(int year) throws SQLException;
    
    // ========================================================================
    // EXPORT REPORTS (CSV/Excel format data)
    // ========================================================================
    
    /**
     * Get yearly report as raw data for CSV/Excel export
     * 
     * @param year The year
     * @return List of String arrays (each array is one row of data)
     * @throws SQLException if database error occurs
     */
    List<String[]> getYearlyReportForExport(int year) throws SQLException;
    
    /**
     * Get employee leave history as raw data for export
     * 
     * @param employeeId The employee ID
     * @param year The year (or null for all years)
     * @return List of String arrays for CSV/Excel
     * @throws SQLException if database error occurs
     */
    List<String[]> getEmployeeHistoryForExport(int employeeId, Integer year) throws SQLException;
}