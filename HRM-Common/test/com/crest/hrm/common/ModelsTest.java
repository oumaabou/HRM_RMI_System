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
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import org.junit.Test;
import java.io.*;
import java.time.LocalDate;
import static org.junit.Assert.*;
/**
 * Unit tests for all Data Model classes.
 * Tests: constructors, getters/setters, utility methods, and Serialization.
 */
public class ModelsTest {
    // =========================================================================
    // Helper: serialize then deserialize an object
    // =========================================================================
    @SuppressWarnings("unchecked")
    private <T> T serializeAndDeserialize(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.flush();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (T) in.readObject();
    }
    // =========================================================================
    // Employee Tests
    // =========================================================================
    @Test
    public void testEmployee_Constructor_And_Getters() {
        Employee emp = new Employee(
                1, "Ali", "Hassan", "990101-14-1234",
                "ali@crestsolutions.com", "012-3456789",
                "Software Engineer", Department.INFORMATION_TECHNOLOGY,
                UserRole.EMPLOYEE, LocalDate.of(2020, 3, 1),
                "ali.hassan", "hashed_password"
        );
        assertEquals(Integer.valueOf(1), emp.getEmployeeId());
        assertEquals("Ali", emp.getFirstName());
        assertEquals("Hassan", emp.getLastName());
        assertEquals("990101-14-1234", emp.getIcNumber());
        assertEquals("ali@crestsolutions.com", emp.getEmail());
        assertEquals(Department.INFORMATION_TECHNOLOGY, emp.getDepartment());
        assertEquals(UserRole.EMPLOYEE, emp.getRole());
    }
    @Test
    public void testEmployee_GetFullName() {
        Employee emp = new Employee();
        emp.setFirstName("Siti");
        emp.setLastName("Aminah");
        assertEquals("Siti Aminah", emp.getFullName());
    }
    @Test
    public void testEmployee_Setters() {
        Employee emp = new Employee();
        emp.setEmployeeId(2);
        emp.setEmail("test@email.com");
        emp.setRole(UserRole.HR_STAFF);
        emp.setDepartment(Department.HUMAN_RESOURCES);
        assertEquals(Integer.valueOf(2), emp.getEmployeeId());
        assertEquals("test@email.com", emp.getEmail());
        assertEquals(UserRole.HR_STAFF, emp.getRole());
        assertEquals(Department.HUMAN_RESOURCES, emp.getDepartment());
    }
    @Test
    public void testEmployee_Serialization() throws IOException, ClassNotFoundException {
        Employee emp = new Employee(
                3, "Ravi", "Kumar", "880505-10-5678",
                "ravi@crestsolutions.com", "011-9876543",
                "HR Manager", Department.HUMAN_RESOURCES,
                UserRole.HR_STAFF, LocalDate.of(2018, 6, 15),
                "ravi.kumar", "hashed_pw"
        );
        Employee deserialized = serializeAndDeserialize(emp);
        assertEquals(emp.getEmployeeId(), deserialized.getEmployeeId());
        assertEquals(emp.getFullName(), deserialized.getFullName());
        assertEquals(emp.getRole(), deserialized.getRole());
    }
    @Test
    public void testEmployee_ToString_NotEmpty() {
        Employee emp = new Employee(
                4, "John", "Doe", "010203-05-1111",
                "john@email.com", "013-1111111",
                "Analyst", Department.FINANCE,
                UserRole.EMPLOYEE, LocalDate.now(),
                "john.doe", "pw"
        );
        assertNotNull(emp.toString());
        assertFalse(emp.toString().isEmpty());
    }
    // =========================================================================
    // LeaveApplication Tests
    // =========================================================================
    @Test
    public void testLeaveApplication_DefaultStatus_IsPending() {
        LeaveApplication app = new LeaveApplication(
                "LV001", 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5),
                "Family vacation"
        );
        assertEquals(LeaveStatus.PENDING, app.getStatus());
    }
    @Test
    public void testLeaveApplication_TotalDaysCalculation() {
        LeaveApplication app = new LeaveApplication(
                "LV002", 1, LeaveType.SICK,
                LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 14),
                "Fever"
        );
        // 10, 11, 12, 13, 14 = 5 days
        assertEquals(5, app.getTotalDays());
    }
    @Test
    public void testLeaveApplication_SingleDay() {
        LeaveApplication app = new LeaveApplication(
                "LV003", 2, LeaveType.EMERGENCY,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 1),
                "Emergency"
        );
        assertEquals(1, app.getTotalDays());
    }
    @Test
    public void testLeaveApplication_AppliedDate_IsToday() {
        LeaveApplication app = new LeaveApplication(
                "LV004", 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3),
                "Rest"
        );
        assertEquals(LocalDate.now(), app.getAppliedDate());
    }
    @Test
    public void testLeaveApplication_Serialization() throws IOException, ClassNotFoundException {
        LeaveApplication app = new LeaveApplication(
                "LV005", 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 7),
                "Holiday"
        );
        LeaveApplication deserialized = serializeAndDeserialize(app);
        assertEquals(app.getLeaveId(), deserialized.getLeaveId());
        assertEquals(app.getTotalDays(), deserialized.getTotalDays());
        assertEquals(app.getStatus(), deserialized.getStatus());
    }
    @Test
    public void testLeaveApplication_StatusChange() {
        LeaveApplication app = new LeaveApplication(
                "LV006", 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3),
                "Vacation"
        );
        app.setStatus(LeaveStatus.APPROVED);
        app.setReviewedBy(101);
        app.setReviewRemarks("Approved");
        assertEquals(LeaveStatus.APPROVED, app.getStatus());
        assertEquals(Integer.valueOf(101), app.getReviewedBy());
    }
    // =========================================================================
    // LeaveBalance Tests
    // =========================================================================
    @Test
    public void testLeaveBalance_DefaultEntitlements() {
        LeaveBalance balance = new LeaveBalance(1, 2026);
        assertEquals(14, balance.getAnnualLeaveEntitlement());
        assertEquals(14, balance.getSickLeaveEntitlement());
        assertEquals(3,  balance.getEmergencyLeaveEntitlement());
    }
    @Test
    public void testLeaveBalance_RemainingDays_InitiallyFullBalance() {
        LeaveBalance balance = new LeaveBalance(1, 2026);
        assertEquals(14, balance.getRemainingAnnualLeave());
        assertEquals(14, balance.getRemainingSickLeave());
        assertEquals(3,  balance.getRemainingEmergencyLeave());
    }
    @Test
    public void testLeaveBalance_RemainingDays_AfterTaking() {
        LeaveBalance balance = new LeaveBalance(1, 2026);
        balance.setAnnualLeaveTaken(5);
        balance.setSickLeaveTaken(2);
        assertEquals(9, balance.getRemainingAnnualLeave());
        assertEquals(12, balance.getRemainingSickLeave());
    }
    @Test
    public void testLeaveBalance_Serialization() throws IOException, ClassNotFoundException {
        LeaveBalance balance = new LeaveBalance(2, 2026);
        balance.setAnnualLeaveTaken(3);
        LeaveBalance deserialized = serializeAndDeserialize(balance);
        assertEquals(balance.getEmployeeId(), deserialized.getEmployeeId());
        assertEquals(balance.getYear(), deserialized.getYear());
        assertEquals(balance.getRemainingAnnualLeave(), deserialized.getRemainingAnnualLeave());
    }
    // =========================================================================
    // FamilyDetails Tests
    // =========================================================================
    @Test
    public void testFamilyDetails_Constructor() {
        FamilyDetails fd = new FamilyDetails(1);
        assertEquals(Integer.valueOf(1), fd.getEmployeeId());
    }
    @Test
    public void testFamilyDetails_Setters_And_Getters() {
        FamilyDetails fd = new FamilyDetails(1);
        fd.setSpouseName("Nurul Ain");
        fd.setNumberOfChildren(2);
        fd.setEmergencyContactName("Ahmad");
        fd.setEmergencyContactPhone("012-9999999");
        fd.setEmergencyContactRelationship("Brother");
        fd.setHomeAddress("123, Jalan Maju, Kuala Lumpur");
        assertEquals("Nurul Ain", fd.getSpouseName());
        assertEquals(2, fd.getNumberOfChildren());
        assertEquals("Ahmad", fd.getEmergencyContactName());
        assertEquals("012-9999999", fd.getEmergencyContactPhone());
        assertEquals("Brother", fd.getEmergencyContactRelationship());
    }
    @Test
    public void testFamilyDetails_Serialization() throws IOException, ClassNotFoundException {
        FamilyDetails fd = new FamilyDetails(3);
        fd.setSpouseName("Sarah");
        fd.setNumberOfChildren(1);
        FamilyDetails deserialized = serializeAndDeserialize(fd);
        assertEquals(fd.getEmployeeId(), deserialized.getEmployeeId());
        assertEquals(fd.getSpouseName(), deserialized.getSpouseName());
        assertEquals(fd.getNumberOfChildren(), deserialized.getNumberOfChildren());
    }
    @Test
    public void testFamilyDetails_ToString_NotEmpty() {
        FamilyDetails fd = new FamilyDetails(1);
        fd.setSpouseName("Maria");
        fd.setEmergencyContactName("John");
        fd.setEmergencyContactRelationship("Father");
        assertNotNull(fd.toString());
        assertFalse(fd.toString().isEmpty());
    }
}