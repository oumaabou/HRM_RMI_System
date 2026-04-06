/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.exceptions.AuthenticationException;
import com.crest.hrm.common.exceptions.DuplicateEmployeeException;
import com.crest.hrm.common.exceptions.EmployeeNotFoundException;
import com.crest.hrm.common.exceptions.InvalidLeaveRequestException;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Unit tests for all custom Exception classes in the common module.
 */
public class ExceptionsTest {
    // =========================================================================
    // EmployeeNotFoundException Tests
    // =========================================================================
    @Test
    public void testEmployeeNotFoundException_Message() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException(1);
        assertTrue(ex.getMessage().contains("1"));
    }
    @Test
    public void testEmployeeNotFoundException_Identifier() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException(1);
        assertEquals(Integer.valueOf(1), ex.getIdentifier());
    }
    @Test
    public void testEmployeeNotFoundException_IsException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException(999);
        assertTrue(ex instanceof Exception);
    }
    // =========================================================================
    // InvalidLeaveRequestException Tests
    // =========================================================================
    @Test
    public void testInvalidLeaveRequestException_Message() {
        InvalidLeaveRequestException ex = new InvalidLeaveRequestException("Insufficient balance");
        assertTrue(ex.getMessage().contains("Insufficient balance"));
    }
    @Test
    public void testInvalidLeaveRequestException_Reason() {
        String reason = "Start date cannot be in the past";
        InvalidLeaveRequestException ex = new InvalidLeaveRequestException(reason);
        assertEquals(reason, ex.getReason());
    }
    // =========================================================================
    // AuthenticationException Tests
    // =========================================================================
    @Test
    public void testAuthenticationException_DefaultMessage() {
        AuthenticationException ex = new AuthenticationException();
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }
    @Test
    public void testAuthenticationException_CustomMessage() {
        AuthenticationException ex = new AuthenticationException("Account locked");
        assertEquals("Account locked", ex.getMessage());
    }
    @Test
    public void testAuthenticationException_IsException() {
        AuthenticationException ex = new AuthenticationException();
        assertTrue(ex instanceof Exception);
    }
    // =========================================================================
    // DuplicateEmployeeException Tests
    // =========================================================================
    @Test
    public void testDuplicateEmployeeException_Message() {
        DuplicateEmployeeException ex = new DuplicateEmployeeException("990101-14-1234");
        assertTrue(ex.getMessage().contains("990101-14-1234"));
    }
    @Test
    public void testDuplicateEmployeeException_IcNumber() {
        String ic = "990101-14-1234";
        DuplicateEmployeeException ex = new DuplicateEmployeeException(ic);
        assertEquals(ic, ex.getIcNumber());
    }
}