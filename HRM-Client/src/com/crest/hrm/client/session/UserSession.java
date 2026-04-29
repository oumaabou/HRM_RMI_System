package com.crest.hrm.client.session;

import com.crest.hrm.common.models.Employee;

public class UserSession {

    private static Employee currentUser;

    public static void setCurrentUser(Employee employee) {
        currentUser = employee;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Convenience method kept for any code that only needs the username
    public static String getUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
}