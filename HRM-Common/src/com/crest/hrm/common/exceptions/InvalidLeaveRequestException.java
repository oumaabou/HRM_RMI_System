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
 * Thrown when a leave request violates business rules,
 * e.g. invalid dates, insufficient leave balance, or overlapping leaves.
 */
public class InvalidLeaveRequestException extends Exception {
    private final String reason;
    public InvalidLeaveRequestException(String reason) {
        super("Invalid leave request: " + reason);
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}
