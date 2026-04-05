/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.exceptions;

/**
 *
 * @author shuhaab
 */
/**
 * Thrown when an employee with the given ID or IC number cannot be found.
 */
public class EmployeeNotFoundException extends Exception {
    private final String identifier;
    public EmployeeNotFoundException(String identifier) {
        super("Employee not found: " + identifier);
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }
}
