/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.enums;
/**
 * Defines the roles available in the HRM system.
 * HR_STAFF can register employees and generate reports.
 * EMPLOYEE can apply for leave, check balance, and update personal details.
 */
public enum UserRole {
    HR_STAFF("HR Staff"),
    EMPLOYEE("Employee");
    private final String displayName;
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
}

