package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

public class LeaveDAOImplTest {
    
    private LeaveDAO leaveDAO;
    private EmployeeDAO employeeDAO;
    private Integer testEmployeeId;
    private Integer testLeaveId;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Increase connection limit for tests
        // (Already done in DatabaseConnection)
        
        // Clean up any leftover test data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM leaves WHERE reason LIKE '%Test%' OR reason LIKE '%Vacation%'");
            stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id IN (SELECT employee_id FROM employees WHERE first_name = 'LeaveTest')");
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE 'leavetest%'");
            stmt.executeUpdate("DELETE FROM employees WHERE first_name = 'LeaveTest'");
        } catch (Exception e) {
            // Ignore if tables don't exist
        }
    }
    
    @Before
    public void setUp() throws Exception {
        leaveDAO = new LeaveDAOImpl();
        employeeDAO = new EmployeeDAOImpl();
        testEmployeeId = null;
        testLeaveId = null;
        
        // Create a test employee to associate leaves with
        Employee employee = new Employee();
        employee.setFirstName("LeaveTest");
        employee.setLastName("User");
        employee.setIcNumber("LEAVE" + System.currentTimeMillis());
        employee.setEmail("leavetest@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        employee.setUsername("leavetest" + System.currentTimeMillis());
        employee.setPasswordHash("test_hash");
        
        testEmployeeId = employeeDAO.save(employee);
        
        // Initialize leave balance for test employee
        leaveDAO.initializeLeaveBalance(testEmployeeId, 2026);
    }
    
    @After
    public void tearDown() throws Exception {
        // Delete test leave first
        if (testLeaveId != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM leaves WHERE leave_id = " + testLeaveId);
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Delete test employee (cascade deletes related records)
        if (testEmployeeId != null) {
            try {
                // Delete child records first
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM family_details WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM leaves WHERE employee_id = " + testEmployeeId);
                }
                employeeDAO.delete(testEmployeeId);
            } catch (Exception e) {
                // Ignore
            }
            testEmployeeId = null;
        }
        
        // Small delay to let connections close
        Thread.sleep(50);
    }
    
    // ========================================================================
    // SAVE LEAVE TEST
    // ========================================================================
    
    @Test
    public void testSaveLeave_Success() throws Exception {
        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveType(LeaveType.ANNUAL);
        leave.setStartDate(LocalDate.of(2026, 5, 15));
        leave.setEndDate(LocalDate.of(2026, 5, 17));
        leave.setTotalDays(3);
        leave.setReason("Test vacation");
        leave.setStatus(LeaveStatus.PENDING);
        
        int leaveId = leaveDAO.saveLeave(leave);
        testLeaveId = leaveId;
        
        assertTrue(leaveId > 0);
    }
    
    // ========================================================================
    // FIND LEAVE BY ID TEST
    // ========================================================================
    
    @Test
    public void testFindLeaveById_ExistingLeave_ReturnsLeave() throws Exception {
        // Create leave first
        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveType(LeaveType.ANNUAL);
        leave.setStartDate(LocalDate.of(2026, 6, 1));
        leave.setEndDate(LocalDate.of(2026, 6, 5));
        leave.setTotalDays(5);
        leave.setReason("Find by ID test");
        leave.setStatus(LeaveStatus.PENDING);
        
        int leaveId = leaveDAO.saveLeave(leave);
        testLeaveId = leaveId;
        
        // Find by ID
        LeaveApplication found = leaveDAO.findLeaveById(leaveId);
        
        assertNotNull(found);
        assertEquals(leaveId, found.getLeaveId().intValue());
        assertEquals(LeaveType.ANNUAL, found.getLeaveType());
        assertEquals("Find by ID test", found.getReason());
    }
    
    @Test
    public void testFindLeaveById_NonExistentLeave_ReturnsNull() throws Exception {
        LeaveApplication found = leaveDAO.findLeaveById(99999);
        assertNull(found);
    }
    
    // ========================================================================
    // FIND LEAVES BY EMPLOYEE TEST
    // ========================================================================
    
    @Test
    public void testFindLeavesByEmployee_ReturnsList() throws Exception {
        // Create two leaves for the same employee
        LeaveApplication leave1 = new LeaveApplication();
        leave1.setEmployeeId(testEmployeeId);
        leave1.setLeaveType(LeaveType.ANNUAL);
        leave1.setStartDate(LocalDate.of(2026, 7, 10));
        leave1.setEndDate(LocalDate.of(2026, 7, 12));
        leave1.setTotalDays(3);
        leave1.setReason("First leave");
        leave1.setStatus(LeaveStatus.PENDING);
        leaveDAO.saveLeave(leave1);
        
        LeaveApplication leave2 = new LeaveApplication();
        leave2.setEmployeeId(testEmployeeId);
        leave2.setLeaveType(LeaveType.SICK);
        leave2.setStartDate(LocalDate.of(2026, 8, 1));
        leave2.setEndDate(LocalDate.of(2026, 8, 1));
        leave2.setTotalDays(1);
        leave2.setReason("Second leave");
        leave2.setStatus(LeaveStatus.PENDING);
        leaveDAO.saveLeave(leave2);
        
        List<LeaveApplication> leaves = leaveDAO.findLeavesByEmployee(testEmployeeId);
        
        assertNotNull(leaves);
        assertTrue(leaves.size() >= 2);
    }
    
    // ========================================================================
    // FIND PENDING LEAVES TEST
    // ========================================================================
    
    @Test
    public void testFindPendingLeaves_ReturnsList() throws Exception {
        List<LeaveApplication> pendingLeaves = leaveDAO.findPendingLeaves();
        assertNotNull(pendingLeaves);
        // All leaves we created are PENDING, so should be in list
    }
    
    // ========================================================================
    // UPDATE LEAVE STATUS TEST
    // ========================================================================
    
    @Test
    public void testUpdateLeaveStatus_Approve_Success() throws Exception {
        Thread.sleep(100);
        // Create a pending leave
        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveType(LeaveType.ANNUAL);
        leave.setStartDate(LocalDate.of(2026, 9, 1));
        leave.setEndDate(LocalDate.of(2026, 9, 3));
        leave.setTotalDays(3);
        leave.setReason("To be approved");
        leave.setStatus(LeaveStatus.PENDING);
        
        int leaveId = leaveDAO.saveLeave(leave);
        testLeaveId = leaveId;
        
        // Approve the leave
        boolean result = leaveDAO.updateLeaveStatus(leaveId, "APPROVED", 1);
        
        assertTrue(result);
        
        // Verify status changed
        LeaveApplication updated = leaveDAO.findLeaveById(leaveId);
        assertEquals(LeaveStatus.APPROVED, updated.getStatus());
    }
    
    @Test
    public void testUpdateLeaveStatus_Reject_Success() throws Exception {
        // Create a pending leave
        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveType(LeaveType.SICK);
        leave.setStartDate(LocalDate.of(2026, 10, 1));
        leave.setEndDate(LocalDate.of(2026, 10, 2));
        leave.setTotalDays(2);
        leave.setReason("To be rejected");
        leave.setStatus(LeaveStatus.PENDING);
        
        int leaveId = leaveDAO.saveLeave(leave);
        testLeaveId = leaveId;
        
        // Reject the leave
        boolean result = leaveDAO.updateLeaveStatus(leaveId, "REJECTED", 1);
        
        assertTrue(result);
        
        // Verify status changed
        LeaveApplication updated = leaveDAO.findLeaveById(leaveId);
        assertEquals(LeaveStatus.REJECTED, updated.getStatus());
    }
    
    // ========================================================================
    // CANCEL LEAVE TEST
    // ========================================================================
    
    @Test
    public void testCancelLeaveApplication_Success() throws Exception {
        // Create a pending leave
        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(testEmployeeId);
        leave.setLeaveType(LeaveType.EMERGENCY);
        leave.setStartDate(LocalDate.of(2026, 11, 1));
        leave.setEndDate(LocalDate.of(2026, 11, 1));
        leave.setTotalDays(1);
        leave.setReason("To be cancelled");
        leave.setStatus(LeaveStatus.PENDING);
        
        int leaveId = leaveDAO.saveLeave(leave);
        testLeaveId = leaveId;
        
        // Cancel the leave
        boolean result = leaveDAO.cancelLeaveApplication(leaveId);
        
        assertTrue(result);
        
        // Verify status changed to CANCELLED
        LeaveApplication updated = leaveDAO.findLeaveById(leaveId);
        assertEquals(LeaveStatus.CANCELLED, updated.getStatus());
    }
    
    // ========================================================================
    // GET LEAVE BALANCE TEST
    // ========================================================================
    
    @Test
    public void testGetLeaveBalance_ReturnsBalance() throws Exception {
        Thread.sleep(100);
        LeaveBalance balance = leaveDAO.getLeaveBalance(testEmployeeId, 2026);
        
        assertNotNull(balance);
        assertEquals(testEmployeeId, balance.getEmployeeId());
        assertEquals(2026, balance.getLeaveYear().intValue());
        assertTrue(balance.getAnnualRemaining() > 0);
    }
    
    @Test
    public void testGetLeaveBalance_NonExistent_ReturnsNull() throws Exception {
        LeaveBalance balance = leaveDAO.getLeaveBalance(99999, 2026);
        assertNull(balance);
    }
    
    // ========================================================================
    // UPDATE LEAVE BALANCE TEST
    // ========================================================================
    
    @Test
    public void testUpdateLeaveBalance_Annual_Success() throws Exception {
        // Get initial balance
        LeaveBalance initial = leaveDAO.getLeaveBalance(testEmployeeId, 2026);
        double initialAnnual = initial.getAnnualRemaining();
        
        // Update balance (use 2 days)
        boolean result = leaveDAO.updateLeaveBalance(testEmployeeId, 2026, 2.0, "ANNUAL");
        
        assertTrue(result);
        
        // Verify balance decreased
        LeaveBalance updated = leaveDAO.getLeaveBalance(testEmployeeId, 2026);
        assertEquals(initialAnnual - 2.0, updated.getAnnualRemaining(), 0.01);
    }
    
    @Test
    public void testUpdateLeaveBalance_Sick_Success() throws Exception {
        LeaveBalance initial = leaveDAO.getLeaveBalance(testEmployeeId, 2026);
        double initialSick = initial.getSickRemaining();
        
        boolean result = leaveDAO.updateLeaveBalance(testEmployeeId, 2026, 1.0, "SICK");
        
        assertTrue(result);
        
        LeaveBalance updated = leaveDAO.getLeaveBalance(testEmployeeId, 2026);
        assertEquals(initialSick - 1.0, updated.getSickRemaining(), 0.01);
    }
    
    @Test
    public void testUpdateLeaveBalance_InsufficientBalance_ReturnsFalse() throws Exception {
        Thread.sleep(100);
        // Try to use more days than available (assume max 14)
        boolean result = leaveDAO.updateLeaveBalance(testEmployeeId, 2026, 100.0, "ANNUAL");
        
        assertFalse(result);
    }
    
    // ========================================================================
    // HAS SUFFICIENT BALANCE TEST
    // ========================================================================
    
    @Test
    public void testHasSufficientBalance_Sufficient_ReturnsTrue() throws Exception {
        boolean hasBalance = leaveDAO.hasSufficientBalance(testEmployeeId, 2026, 5.0, "ANNUAL");
        assertTrue(hasBalance);
    }
    
    @Test
    public void testHasSufficientBalance_Insufficient_ReturnsFalse() throws Exception {
        boolean hasBalance = leaveDAO.hasSufficientBalance(testEmployeeId, 2026, 100.0, "ANNUAL");
        assertFalse(hasBalance);
    }
    
    // ========================================================================
    // INITIALIZE LEAVE BALANCE TEST
    // ========================================================================
    
    @Test
    public void testInitializeLeaveBalance_Success() throws Exception {
        // Create a new employee
        Employee employee = new Employee();
        employee.setFirstName("BalanceTest");
        employee.setLastName("User");
        employee.setIcNumber("BAL" + System.currentTimeMillis());
        employee.setEmail("balancetest@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        employee.setUsername("balancetest" + System.currentTimeMillis());
        employee.setPasswordHash("test_hash");
        
        int newEmpId = employeeDAO.save(employee);
        
        // Initialize balance for year 2026
        boolean result = leaveDAO.initializeLeaveBalance(newEmpId, 2026);
        
        assertTrue(result);
        
        // Verify balance was created
        LeaveBalance balance = leaveDAO.getLeaveBalance(newEmpId, 2026);
        assertNotNull(balance);
        assertEquals(14.0, balance.getAnnualRemaining(), 0.01);
        assertEquals(14.0, balance.getSickRemaining(), 0.01);
        assertEquals(3.0, balance.getEmergencyRemaining(), 0.01);
        
        // Clean up
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + newEmpId);
            stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id = " + newEmpId);
            stmt.executeUpdate("DELETE FROM employees WHERE employee_id = " + newEmpId);
        }
    }
    
    // ========================================================================
    // YEARLY REPORT TESTS
    // ========================================================================
    
    @Test
    public void testGetYearlyReport_ReturnsList() throws Exception {
        List<LeaveApplication> report = leaveDAO.getYearlyReport(2026);
        assertNotNull(report);
        // May be empty if no leaves, but should not be null
    }
    
    @Test
    public void testGetEmployeeYearlyReport_ReturnsList() throws Exception {
        List<LeaveApplication> report = leaveDAO.getEmployeeYearlyReport(testEmployeeId, 2026);
        assertNotNull(report);
    }
    
    // ========================================================================
    // TOTAL LEAVE DAYS TAKEN TEST
    // ========================================================================
    
    @Test
    public void testGetTotalLeaveDaysTaken_ReturnsTotal() throws Exception {
        Thread.sleep(100);
        double total = leaveDAO.getTotalLeaveDaysTaken(testEmployeeId, 2026, null);
        assertTrue(total >= 0);
    }
    
    @Test
    public void testGetTotalLeaveDaysTaken_ByType_ReturnsTotal() throws Exception {
        double annualTotal = leaveDAO.getTotalLeaveDaysTaken(testEmployeeId, 2026, "ANNUAL");
        assertTrue(annualTotal >= 0);
    }
}