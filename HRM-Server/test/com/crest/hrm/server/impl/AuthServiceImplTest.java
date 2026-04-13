package com.crest.hrm.server.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthServiceImplTest {

    private AuthServiceImpl authService;

    @Before
    public void setUp() throws Exception {
        authService = new AuthServiceImpl();
    }

    @Test
    public void testLoginInvalidUser() {
        try {
            authService.login("fakeuser", "wrongpass");
            fail("Expected exception for invalid login");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLogoutInvalidEmployeeId() {
        try {
            authService.logout("invalid_token");
            fail("Expected exception for invalid employee ID");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}