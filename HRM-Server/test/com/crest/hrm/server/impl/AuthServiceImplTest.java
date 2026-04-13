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
    public void testLogoutInvalidToken() {
        try {
            authService.logout("invalid_token");
            assertTrue(true); // if no crash → pass
    }     catch (Exception e) {
            fail("Logout should not throw exception");
    }
}
}