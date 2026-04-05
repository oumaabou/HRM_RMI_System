/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import com.crest.hrm.common.enums.UserRole;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Unit tests for all Enum classes in the common module.
 */
public class EnumsTest {
    // =========================================================================
    // UserRole Tests
    // =========================================================================
    @Test
    public void testUserRole_HRStaff_DisplayName() {
        assertEquals("HR Staff", UserRole.HR_STAFF.getDisplayName());
    }
    @Test
    public void testUserRole_Employee_DisplayName() {
        assertEquals("Employee", UserRole.EMPLOYEE.getDisplayName());
    }
    @Test
    public void testUserRole_ToString() {
        assertEquals("HR Staff", UserRole.HR_STAFF.toString());
        assertEquals("Employee", UserRole.EMPLOYEE.toString());
    }
    @Test
    public void testUserRole_TotalValues() {
        // Must have exactly 2 roles
        assertEquals(2, UserRole.values().length);
    }
    // =========================================================================
    // LeaveType Tests
    // =========================================================================
    @Test
    public void testLeaveType_Annual_DisplayName() {
        assertEquals("Annual Leave", LeaveType.ANNUAL.getDisplayName());
    }
    @Test
    public void testLeaveType_Sick_DisplayName() {
        assertEquals("Sick Leave", LeaveType.SICK.getDisplayName());
    }
    @Test
    public void testLeaveType_Emergency_DisplayName() {
        assertEquals("Emergency Leave", LeaveType.EMERGENCY.getDisplayName());
    }
    @Test
    public void testLeaveType_Maternity_DisplayName() {
        assertEquals("Maternity Leave", LeaveType.MATERNITY.getDisplayName());
    }
    @Test
    public void testLeaveType_TotalValues() {
        // Must have exactly 6 leave types
        assertEquals(6, LeaveType.values().length);
    }
    // =========================================================================
    // LeaveStatus Tests
    // =========================================================================
    @Test
    public void testLeaveStatus_Pending_DisplayName() {
        assertEquals("Pending", LeaveStatus.PENDING.getDisplayName());
    }
    @Test
    public void testLeaveStatus_Approved_DisplayName() {
        assertEquals("Approved", LeaveStatus.APPROVED.getDisplayName());
    }
    @Test
    public void testLeaveStatus_Rejected_DisplayName() {
        assertEquals("Rejected", LeaveStatus.REJECTED.getDisplayName());
    }
    @Test
    public void testLeaveStatus_Cancelled_DisplayName() {
        assertEquals("Cancelled", LeaveStatus.CANCELLED.getDisplayName());
    }
    @Test
    public void testLeaveStatus_TotalValues() {
        // Must have exactly 4 statuses
        assertEquals(4, LeaveStatus.values().length);
    }
    // =========================================================================
    // Department Tests
    // =========================================================================
    @Test
    public void testDepartment_IT_DisplayName() {
        assertEquals("Information Technology", Department.INFORMATION_TECHNOLOGY.getDisplayName());
    }
    @Test
    public void testDepartment_HR_DisplayName() {
        assertEquals("Human Resources", Department.HUMAN_RESOURCES.getDisplayName());
    }
    @Test
    public void testDepartment_Finance_DisplayName() {
        assertEquals("Finance", Department.FINANCE.getDisplayName());
    }
    @Test
    public void testDepartment_TotalValues() {
        // Must have exactly 6 departments
        assertEquals(6, Department.values().length);
    }
}

