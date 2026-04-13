package com.crest.hrm.server.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HRServiceImplTest {

    private HRServiceImpl hrService;

    @Before
    public void setUp() throws Exception {
        hrService = new HRServiceImpl();
    }

    @Test
    public void testGetEmployeeInvalidId() {
        try {
            hrService.getEmployeeById("99999");
            fail("Expected EmployeeNotFoundException");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGenerateReportInvalidEmployee() {
        try {
            hrService.generateYearlyLeaveReport("99999", 2025);
            fail("Expected exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}