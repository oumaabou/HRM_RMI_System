/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.models;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import java.io.Serializable;
import java.time.LocalDate;
/**
 * Represents an employee in the HRM system.
 * Implements Serializable so it can be transmitted via RMI.
 */
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String icNumber;       // IC or Passport number
    private String email;
    private String phoneNumber;
    private String position;
    private Department department;
    private UserRole role;
    private LocalDate dateJoined;
    private String username;
    private String passwordHash;   // Store hashed password, never plain text
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public Employee() {}
    public Employee(Integer employeeId, String firstName, String lastName,
                    String icNumber, String email, String phoneNumber,
                    String position, Department department, UserRole role,
                    LocalDate dateJoined, String username, String passwordHash) {
        this.employeeId   = employeeId;
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.icNumber     = icNumber;
        this.email        = email;
        this.phoneNumber  = phoneNumber;
        this.position     = position;
        this.department   = department;
        this.role         = role;
        this.dateJoined   = dateJoined;
        this.username     = username;
        this.passwordHash = passwordHash;
    }
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    public Integer getEmployeeId()             { return employeeId; }
    public void   setEmployeeId(Integer v)     { this.employeeId = v; }
    public String getFirstName()               { return firstName; }
    public void   setFirstName(String v)       { this.firstName = v; }
    public String getLastName()                { return lastName; }
    public void   setLastName(String v)        { this.lastName = v; }
    public String getIcNumber()                { return icNumber; }
    public void   setIcNumber(String v)        { this.icNumber = v; }
    public String getEmail()                   { return email; }
    public void   setEmail(String v)           { this.email = v; }
    public String getPhoneNumber()             { return phoneNumber; }
    public void   setPhoneNumber(String v)     { this.phoneNumber = v; }
    public String getPosition()                { return position; }
    public void   setPosition(String v)        { this.position = v; }
    public Department getDepartment()          { return department; }
    public void       setDepartment(Department v) { this.department = v; }
    public UserRole getRole()                  { return role; }
    public void     setRole(UserRole v)        { this.role = v; }
    public LocalDate getDateJoined()           { return dateJoined; }
    public void      setDateJoined(LocalDate v){ this.dateJoined = v; }
    public String getUsername()                { return username; }
    public void   setUsername(String v)        { this.username = v; }
    public String getPasswordHash()            { return passwordHash; }
    public void   setPasswordHash(String v)    { this.passwordHash = v; }
    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------
    public String getFullName() {
        return firstName + " " + lastName;
    }
    @Override
    public String toString() {
        return "[" + employeeId + "] " + getFullName() + " (" + role + ") - " + department;
    }
}