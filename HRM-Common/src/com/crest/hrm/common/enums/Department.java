/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.enums;

/**
 *
 * @author shuhaab
 */
public enum Department {
    HUMAN_RESOURCES("Human Resources"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    FINANCE("Finance"),
    OPERATIONS("Operations"),
    MARKETING("Marketing"),
    ADMINISTRATION("Administration");
    private final String displayName;
    Department(String displayName) {
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
