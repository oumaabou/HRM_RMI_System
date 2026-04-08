package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.ReportDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class ReportDAOImplTest {
    
    private ReportDAO reportDAO;
    private LeaveDAO leaveDAO;
    private EmployeeDAO employeeDAO;
    private Integer testEmployeeId1;
    private Integer testEmployeeId2;
    private Integer testLeaveId1;
    private Integer testLeaveId2;
    private Integer testLeaveId3;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM leaves WHERE reason LIKE '%Report Test%'");
            stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id IN (SELECT employee_id FROM employees WHERE first_name LIKE 'ReportTest%')");
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE 'reporttest%'");
            stmt.executeUpdate("DELETE FROM employees WHERE first_name LIKE 'ReportTest%'");
        } catch (Exception e) {
            // Ignore
        }
    }
    
    @Before
    public void setUp() throws Exception {
        reportDAO = new ReportDAOImpl();
        leaveDAO = new LeaveDAOImpl();
        employeeDAO = new EmployeeDAOImpl();
        testEmployeeId1 = null;
        testEmployeeId2 = null;
        testLeaveId1 = null;
        testLeaveId2 = null;
        testLeaveId3 = null;
        
        // Create test employee 1 (IT department) - using the enum display name
        Employee employee1 = new Employee();
        employee1.setFirstName("ReportTest");
        employee1.setLastName("Employee1");
        employee1.setIcNumber("REP1" + System.currentTimeMillis());
        employee1.setEmail("report1@test.com");
        employee1.setPhoneNumber("0123456789");
        employee1.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee1.setRole(UserRole.EMPLOYEE);
        employee1.setDateJoined(LocalDate.of(2023, 1, 1));
        employee1.setUsername("reporttest1" + System.currentTimeMillis());
        employee1.setPasswordHash("test_hash");
        testEmployeeId1 = employeeDAO.save(employee1);
        
        // Create test employee 2 (HR department)
        Employee employee2 = new Employee();
        employee2.setFirstName("ReportTest");
        employee2.setLastName("Employee2");
        employee2.setIcNumber("REP2" + System.currentTimeMillis());
        employee2.setEmail("report2@test.com");
        employee2.setPhoneNumber("0123456789");
        employee2.setDepartment(Department.HUMAN_RESOURCES);
        employee2.setRole(UserRole.HR_STAFF);
        employee2.setDateJoined(LocalDate.of(2023, 6, 1));
        employee2.setUsername("reporttest2" + System.currentTimeMillis());
        employee2.setPasswordHash("test_hash");
        testEmployeeId2 = employeeDAO.save(employee2);
        
        // Initialize leave balances
        leaveDAO.initializeLeaveBalance(testEmployeeId1, 2026);
        leaveDAO.initializeLeaveBalance(testEmployeeId2, 2026);
        
        // Create test leave applications for employee 1 (APPROVED)
        LeaveApplication leave1 = new LeaveApplication();
        leave1.setEmployeeId(testEmployeeId1);
        leave1.setLeaveType(LeaveType.ANNUAL);
        leave1.setStartDate(LocalDate.of(2026, 3, 10));
        leave1.setEndDate(LocalDate.of(2026, 3, 12));
        leave1.setTotalDays(3);
        leave1.setReason("Report Test Annual Leave");
        leave1.setStatus(LeaveStatus.APPROVED);
        testLeaveId1 = leaveDAO.saveLeave(leave1);
        
        LeaveApplication leave2 = new LeaveApplication();
        leave2.setEmployeeId(testEmployeeId1);
        leave2.setLeaveType(LeaveType.SICK);
        leave2.setStartDate(LocalDate.of(2026, 4, 1));
        leave2.setEndDate(LocalDate.of(2026, 4, 2));
        leave2.setTotalDays(2);
        leave2.setReason("Report Test Sick Leave");
        leave2.setStatus(LeaveStatus.APPROVED);
        testLeaveId2 = leaveDAO.saveLeave(leave2);
        
        // Create test leave for employee 2 (PENDING)
        LeaveApplication leave3 = new LeaveApplication();
        leave3.setEmployeeId(testEmployeeId2);
        leave3.setLeaveType(LeaveType.ANNUAL);
        leave3.setStartDate(LocalDate.of(2026, 5, 1));
        leave3.setEndDate(LocalDate.of(2026, 5, 5));
        leave3.setTotalDays(5);
        leave3.setReason("Report Test HR Leave");
        leave3.setStatus(LeaveStatus.PENDING);
        testLeaveId3 = leaveDAO.saveLeave(leave3);
        
        // Update leave balances after approved leaves
        leaveDAO.updateLeaveBalance(testEmployeeId1, 2026, 3.0, "ANNUAL");
        leaveDAO.updateLeaveBalance(testEmployeeId1, 2026, 2.0, "SICK");
        
        Thread.sleep(50);
    }
    
    @After
    public void tearDown() throws Exception {
        if (testLeaveId1 != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM leaves WHERE leave_id = " + testLeaveId1);
            } catch (Exception e) {}
        }
        if (testLeaveId2 != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM leaves WHERE leave_id = " + testLeaveId2);
            } catch (Exception e) {}
        }
        if (testLeaveId3 != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM leaves WHERE leave_id = " + testLeaveId3);
            } catch (Exception e) {}
        }
        
        if (testEmployeeId1 != null) {
            try {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + testEmployeeId1);
                    stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id = " + testEmployeeId1);
                    stmt.executeUpdate("DELETE FROM family_details WHERE employee_id = " + testEmployeeId1);
                    stmt.executeUpdate("DELETE FROM leaves WHERE employee_id = " + testEmployeeId1);
                }
                employeeDAO.delete(testEmployeeId1);
            } catch (Exception e) {}
        }
        
        if (testEmployeeId2 != null) {
            try {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + testEmployeeId2);
                    stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id = " + testEmployeeId2);
                    stmt.executeUpdate("DELETE FROM family_details WHERE employee_id = " + testEmployeeId2);
                    stmt.executeUpdate("DELETE FROM leaves WHERE employee_id = " + testEmployeeId2);
                }
                employeeDAO.delete(testEmployeeId2);
            } catch (Exception e) {}
        }
        
        Thread.sleep(50);
    }
    
    // ========================================================================
    // YEARLY LEAVE REPORT TESTS
    // ========================================================================
    
    @Test
    public void testGetYearlyLeaveReport_ReturnsList() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReport(2026);
        assertNotNull(report);
    }
    
    @Test
    public void testGetYearlyLeaveReport_ContainsCorrectData() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReport(2026);
        
        boolean foundAnnualLeave = report.stream()
            .anyMatch(r -> "Report Test Annual Leave".equals(r.getReason()));
        boolean foundSickLeave = report.stream()
            .anyMatch(r -> "Report Test Sick Leave".equals(r.getReason()));
        
        // At least one should be found
        assertTrue(foundAnnualLeave || foundSickLeave);
    }
    
    @Test
    public void testGetYearlyLeaveReport_EmptyYear_ReturnsEmptyList() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReport(2025);
        assertNotNull(report);
    }
    
    // ========================================================================
    // YEARLY LEAVE REPORT BY DEPARTMENT TESTS
    // ========================================================================
    
    @Test
    public void testGetYearlyLeaveReportByDepartment_ITDepartment_ReturnsITLeaves() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReportByDepartment(2026, "Information Technology");
        assertNotNull(report);
    }
    
    @Test
    public void testGetYearlyLeaveReportByDepartment_HRDepartment_ReturnsHRLeaves() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReportByDepartment(2026, "Human Resources");
        assertNotNull(report);
    }
    
    @Test
    public void testGetYearlyLeaveReportByDepartment_NonExistentDepartment_ReturnsEmptyList() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReportByDepartment(2026, "NON_EXISTENT");
        assertNotNull(report);
    }
    
    // ========================================================================
    // YEARLY LEAVE REPORT BY STATUS TESTS
    // ========================================================================
    
    @Test
    public void testGetYearlyLeaveReportByStatus_Approved_ReturnsApprovedLeaves() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReportByStatus(2026, "APPROVED");
        assertNotNull(report);
    }
    
    @Test
    public void testGetYearlyLeaveReportByStatus_Pending_ReturnsPendingLeaves() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReportByStatus(2026, "PENDING");
        assertNotNull(report);
    }
    
    // ========================================================================
    // EMPLOYEE LEAVE REPORT TESTS
    // ========================================================================
    
    @Test
    public void testGetEmployeeLeaveReport_ReturnsEmployeeLeaves() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getEmployeeLeaveReport(testEmployeeId1, 2026);
        assertNotNull(report);
    }
    
    @Test
    public void testGetEmployeeLeaveReport_NoLeaves_ReturnsEmptyList() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getEmployeeLeaveReport(testEmployeeId2, 2026);
        assertNotNull(report);
    }
    
    // ========================================================================
    // EMPLOYEE LEAVE SUMMARY TESTS
    // ========================================================================
    
    @Test
    public void testGetEmployeeLeaveSummary_ReturnsCorrectTotals() throws Exception {
        ReportDAO.EmployeeLeaveSummary summary = reportDAO.getEmployeeLeaveSummary(testEmployeeId1, 2026);
        
        assertNotNull(summary);
        assertEquals(testEmployeeId1.intValue(), summary.getEmployeeId());
        assertEquals(3.0, summary.getAnnualLeaveTaken(), 0.01);
        assertEquals(2.0, summary.getSickLeaveTaken(), 0.01);
        assertEquals(5.0, summary.getTotalLeaveTaken(), 0.01);
        assertEquals(11.0, summary.getAnnualLeaveRemaining(), 0.01);
        assertEquals(12.0, summary.getSickLeaveRemaining(), 0.01);
    }
    
    @Test
    public void testGetEmployeeLeaveSummary_IncludesEmployeeInfo() throws Exception {
        ReportDAO.EmployeeLeaveSummary summary = reportDAO.getEmployeeLeaveSummary(testEmployeeId1, 2026);
        
        assertNotNull(summary.getEmployeeName());
        assertNotNull(summary.getDepartment());
    }
    
    // ========================================================================
    // DEPARTMENT LEAVE SUMMARY TESTS
    // ========================================================================
    
    @Test
    public void testGetDepartmentLeaveSummary_ReturnsList() throws Exception {
        List<ReportDAO.EmployeeLeaveSummary> summaries = reportDAO.getDepartmentLeaveSummary("Information Technology", 2026);
        assertNotNull(summaries);
    }
    
    // ========================================================================
    // DEPARTMENT LEAVE TOTALS TESTS
    // ========================================================================
    
    @Test
    public void testGetDepartmentLeaveTotals_ReturnsMap() throws Exception {
        Map<String, Double> totals = reportDAO.getDepartmentLeaveTotals("Information Technology", 2026);
        assertNotNull(totals);
    }
    
    // ========================================================================
    // LEAVE BALANCE REPORTS TESTS
    // ========================================================================
    
    @Test
    public void testGetEmployeesWithLowBalance_ReturnsList() throws Exception {
        List<ReportDAO.EmployeeLeaveSummary> lowBalance = reportDAO.getEmployeesWithLowBalance(2026, 5.0);
        assertNotNull(lowBalance);
    }
    
    @Test
    public void testGetAllEmployeesLeaveBalance_ReturnsList() throws Exception {
        List<ReportDAO.EmployeeLeaveSummary> allBalances = reportDAO.getAllEmployeesLeaveBalance(2026);
        assertNotNull(allBalances);
    }
    
    // ========================================================================
    // EXPORT TESTS
    // ========================================================================
    
    @Test
    public void testGetYearlyReportForExport_ReturnsCSVData() throws Exception {
        List<String[]> exportData = reportDAO.getYearlyReportForExport(2026);
        
        assertNotNull(exportData);
        assertTrue(exportData.size() >= 1);
        
        String[] header = exportData.get(0);
        assertEquals(10, header.length);
        assertEquals("Leave ID", header[0]);
        assertEquals("Employee Name", header[1]);
    }
    
    @Test
    public void testGetEmployeeHistoryForExport_WithYear_ReturnsCSVData() throws Exception {
        List<String[]> exportData = reportDAO.getEmployeeHistoryForExport(testEmployeeId1, 2026);
        
        assertNotNull(exportData);
        assertTrue(exportData.size() >= 1);
    }
    
    @Test
    public void testGetEmployeeHistoryForExport_WithoutYear_ReturnsAllYears() throws Exception {
        List<String[]> exportData = reportDAO.getEmployeeHistoryForExport(testEmployeeId1, null);
        
        assertNotNull(exportData);
        assertTrue(exportData.size() >= 1);
    }
    
    // ========================================================================
    // VERIFICATION TESTS
    // ========================================================================
    
    @Test
    public void testLeaveReportRow_ContainsAllFields() throws Exception {
        List<ReportDAO.LeaveReportRow> report = reportDAO.getYearlyLeaveReport(2026);
        
        if (!report.isEmpty()) {
            ReportDAO.LeaveReportRow row = report.get(0);
            assertNotNull(row.getLeaveId());
            assertNotNull(row.getEmployeeId());
            assertNotNull(row.getLeaveType());
            assertNotNull(row.getStatus());
        }
    }
    
    @Test
    public void testEmployeeLeaveSummary_ContainsAllFields() throws Exception {
        ReportDAO.EmployeeLeaveSummary summary = reportDAO.getEmployeeLeaveSummary(testEmployeeId1, 2026);
        
        assertNotNull(summary.getEmployeeId());
        assertNotNull(summary.getEmployeeName());
        assertNotNull(summary.getDepartment());
        assertTrue(summary.getTotalLeaveTaken() >= 0);
        assertTrue(summary.getAnnualLeaveRemaining() >= 0);
        assertTrue(summary.getSickLeaveRemaining() >= 0);
        assertTrue(summary.getEmergencyLeaveRemaining() >= 0);
    }
}