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
import java.sql.Timestamp;
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
                "123 Ampang Road", "Male", Department.INFORMATION_TECHNOLOGY,
                UserRole.EMPLOYEE, LocalDate.of(2020, 3, 1),
                "ali.hassan", "hashed_password", true
        );
        assertEquals(Integer.valueOf(1), emp.getEmployeeId());
        assertEquals("Ali", emp.getFirstName());
        assertEquals("Hassan", emp.getLastName());
        assertEquals("123 Ampang Road", emp.getAddress());
        assertEquals("Male", emp.getGender());
        assertTrue(emp.getIsActive());
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
        emp.setIsActive(false);
        emp.setRole(UserRole.HR_STAFF);
        emp.setDepartment(Department.HUMAN_RESOURCES);

        assertEquals(Integer.valueOf(2), emp.getEmployeeId());
        assertEquals("test@email.com", emp.getEmail());
        assertFalse(emp.getIsActive());
        assertEquals(UserRole.HR_STAFF, emp.getRole());
        assertEquals(Department.HUMAN_RESOURCES, emp.getDepartment());
    }

    @Test
    public void testEmployee_Serialization() throws IOException, ClassNotFoundException {
        Employee emp = new Employee(
                3, "Ravi", "Kumar", "880505-10-5678",
                "ravi@crestsolutions.com", "011-9876543",
                "KL Sentral", "Male", Department.HUMAN_RESOURCES,
                UserRole.HR_STAFF, LocalDate.of(2018, 6, 15),
                "ravi.kumar", "hashed_pw", true
        );
        Employee deserialized = serializeAndDeserialize(emp);
        assertEquals(emp.getEmployeeId(), deserialized.getEmployeeId());
        assertEquals(emp.getFullName(), deserialized.getFullName());
        assertEquals(emp.getRole(), deserialized.getRole());
    }

    @Test
    public void testEmployee_ToString_NotEmpty() {
        Employee emp = new Employee();
        emp.setEmployeeId(4);
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setRole(UserRole.EMPLOYEE);
        emp.setDepartment(Department.FINANCE);
        
        assertNotNull(emp.toString());
        assertFalse(emp.toString().isEmpty());
    }

    // =========================================================================
    // LeaveApplication Tests
    // =========================================================================
    @Test
    public void testLeaveApplication_DefaultStatus_IsPending() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LeaveApplication app = new LeaveApplication(
                101, 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5),
                "Family vacation", now
        );
        assertEquals(LeaveStatus.PENDING, app.getStatus());
        assertEquals(now, app.getAppliedDate());
    }

    @Test
    public void testLeaveApplication_TotalDaysCalculation() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LeaveApplication app = new LeaveApplication(
                102, 1, LeaveType.SICK,
                LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 14),
                "Fever", now
        );
        // 10, 11, 12, 13, 14 = 5 days
        assertEquals(5, app.getTotalDays());
    }

    @Test
    public void testLeaveApplication_Serialization() throws IOException, ClassNotFoundException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LeaveApplication app = new LeaveApplication(
                103, 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 7),
                "Holiday", now
        );
        LeaveApplication deserialized = serializeAndDeserialize(app);
        assertEquals(app.getLeaveId(), deserialized.getLeaveId());
        assertEquals(app.getTotalDays(), deserialized.getTotalDays());
        assertEquals(app.getStatus(), deserialized.getStatus());
    }

    @Test
    public void testLeaveApplication_StatusChange() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LeaveApplication app = new LeaveApplication(
                104, 1, LeaveType.ANNUAL,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3),
                "Vacation", now
        );
        app.setStatus(LeaveStatus.APPROVED);
        app.setReviewedBy(99);
        assertEquals(LeaveStatus.APPROVED, app.getStatus());
        assertEquals(Integer.valueOf(99), app.getReviewedBy());
    }

    // =========================================================================
    // LeaveBalance Tests
    // =========================================================================
    @Test
    public void testLeaveBalance_Constructor_And_Getters() {
        LeaveBalance balance = new LeaveBalance(500, 1, 2026, 14.0, 12.5, 3.0);
        assertEquals(Integer.valueOf(500), balance.getBalanceId());
        assertEquals(Integer.valueOf(2026), balance.getLeaveYear());
        assertEquals(Double.valueOf(14.0), balance.getAnnualRemaining());
        assertEquals(Double.valueOf(12.5), balance.getSickRemaining());
        assertEquals(Double.valueOf(3.0), balance.getEmergencyRemaining());
    }

    @Test
    public void testLeaveBalance_Setters() {
        LeaveBalance balance = new LeaveBalance();
        balance.setBalanceId(501);
        balance.setAnnualRemaining(10.0);
        
        assertEquals(Integer.valueOf(501), balance.getBalanceId());
        assertEquals(Double.valueOf(10.0), balance.getAnnualRemaining());
    }

    @Test
    public void testLeaveBalance_Serialization() throws IOException, ClassNotFoundException {
        LeaveBalance balance = new LeaveBalance(502, 2, 2026, 8.0, 14.0, 3.0);
        LeaveBalance deserialized = serializeAndDeserialize(balance);
        assertEquals(balance.getBalanceId(), deserialized.getBalanceId());
        assertEquals(balance.getEmployeeId(), deserialized.getEmployeeId());
        assertEquals(balance.getAnnualRemaining(), deserialized.getAnnualRemaining());
    }

    // =========================================================================
    // FamilyDetails Tests
    // =========================================================================
    @Test
    public void testFamilyDetails_Constructor() {
        FamilyDetails fd = new FamilyDetails(10, 1, "Nurul Ain", "Spouse", "012-9999999");
        assertEquals(Integer.valueOf(10), fd.getFamilyId());
        assertEquals(Integer.valueOf(1), fd.getEmployeeId());
        assertEquals("Nurul Ain", fd.getFamilyMemberName());
        assertEquals("Spouse", fd.getRelationship());
        assertEquals("012-9999999", fd.getPhoneNumber());
    }

    @Test
    public void testFamilyDetails_Setters() {
        FamilyDetails fd = new FamilyDetails();
        fd.setFamilyId(11);
        fd.setEmployeeId(2);
        fd.setFamilyMemberName("Ahmad");
        fd.setRelationship("Brother");
        fd.setPhoneNumber("013-1111111");

        assertEquals(Integer.valueOf(11), fd.getFamilyId());
        assertEquals("Ahmad", fd.getFamilyMemberName());
        assertEquals("Brother", fd.getRelationship());
    }

    @Test
    public void testFamilyDetails_Serialization() throws IOException, ClassNotFoundException {
        FamilyDetails fd = new FamilyDetails(12, 3, "Sarah", "Child", "N/A");
        FamilyDetails deserialized = serializeAndDeserialize(fd);
        assertEquals(fd.getFamilyId(), deserialized.getFamilyId());
        assertEquals(fd.getFamilyMemberName(), deserialized.getFamilyMemberName());
        assertEquals(fd.getRelationship(), deserialized.getRelationship());
    }

    @Test
    public void testFamilyDetails_ToString_NotEmpty() {
        FamilyDetails fd = new FamilyDetails(13, 1, "Maria", "Mother", "019-2222222");
        assertNotNull(fd.toString());
        assertFalse(fd.toString().isEmpty());
    }
}