/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.enums;

/**
 *
 * @author shuhaab
 */
public enum LeaveType {
    ANNUAL("Annual Leave"),
    SICK("Sick Leave"),
    EMERGENCY("Emergency Leave"),
    MATERNITY("Maternity Leave"),
    PATERNITY("Paternity Leave"),
    UNPAID("Unpaid Leave");
    private final String displayName;
    LeaveType(String displayName) {
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
