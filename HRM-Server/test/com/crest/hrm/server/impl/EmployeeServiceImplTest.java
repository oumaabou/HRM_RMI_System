package com.crest.hrm.server.impl;

import com.crest.hrm.common.models.Employee;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployeeServiceImplTest {

    private EmployeeServiceImpl employeeService;

    @Before
    public void setUp() throws Exception {
        employeeService = new EmployeeServiceImpl();
    }

    @Test
    public void testUpdatePersonalDetailsNull() {
        try {
            employeeService.updatePersonalDetails(null);
            fail("Expected exception for null employee");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetFamilyDetailsInvalidEmployee() {
        try {
            employeeService.getFamilyDetails("99999");
            fail("Expected exception for invalid employee");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testViewLeaveHistoryInvalidEmployee() {
        try {
            employeeService.viewLeaveHistory("99999");
            fail("Expected exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}