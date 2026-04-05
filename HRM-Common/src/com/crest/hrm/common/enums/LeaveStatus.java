/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.enums;

/**
 *
 * @author shuhaab
 */
public enum LeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");
    private final String displayName;
    LeaveStatus(String displayName) {
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
