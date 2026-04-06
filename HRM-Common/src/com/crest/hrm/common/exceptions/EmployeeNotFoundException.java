/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.exceptions;

/**
 * Exception thrown when an employee cannot be found in the database.
 */
public class EmployeeNotFoundException extends Exception {
    
    private final Integer identifier; // Changed from String to Integer

    public EmployeeNotFoundException(Integer identifier) { // Changed parameter type
        super("Employee not found with ID: " + identifier);
        this.identifier = identifier;
    }

    public Integer getIdentifier() { // Changed return type
        return identifier;
    }
}