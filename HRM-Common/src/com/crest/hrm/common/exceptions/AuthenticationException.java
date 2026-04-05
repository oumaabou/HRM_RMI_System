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
 * Thrown when a user provides incorrect credentials or
 * attempts to access a resource without sufficient permissions.
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException() {
        super("Authentication failed: Invalid username or password.");
    }
}
