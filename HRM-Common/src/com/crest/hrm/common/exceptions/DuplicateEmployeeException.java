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
 * Thrown when an attempt is made to register an employee
 * whose IC/Passport number already exists in the system.
 */
public class DuplicateEmployeeException extends Exception {
    private final String icNumber;
    public DuplicateEmployeeException(String icNumber) {
        super("An employee with IC/Passport number '" + icNumber + "' already exists.");
        this.icNumber = icNumber;
    }
    public String getIcNumber() {
        return icNumber;
    }
}
