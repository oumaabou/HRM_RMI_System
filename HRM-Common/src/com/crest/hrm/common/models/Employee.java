package com.crest.hrm.common.models;

import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String icNumber;
    private String email;
    private String phoneNumber;
    private String address;       // Added
    private String gender;        // Added
    private Department department;
    private UserRole role;
    private LocalDate dateJoined;
    private String username;
    private String passwordHash;
    private Boolean isActive;     // Added

    public Employee() {}

    public Employee(Integer employeeId, String firstName, String lastName,
                    String icNumber, String email, String phoneNumber,
                    String address, String gender, Department department, 
                    UserRole role, LocalDate dateJoined, String username, 
                    String passwordHash, Boolean isActive) {
        this.employeeId   = employeeId;
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.icNumber     = icNumber;
        this.email        = email;
        this.phoneNumber  = phoneNumber;
        this.address      = address;
        this.gender       = gender;
        this.department   = department;
        this.role         = role;
        this.dateJoined   = dateJoined;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.isActive     = isActive;
    }

    public Integer getEmployeeId()             { return employeeId; }
    public void setEmployeeId(Integer v)       { this.employeeId = v; }
    public String getFirstName()               { return firstName; }
    public void setFirstName(String v)         { this.firstName = v; }
    public String getLastName()                { return lastName; }
    public void setLastName(String v)          { this.lastName = v; }
    public String getIcNumber()                { return icNumber; }
    public void setIcNumber(String v)          { this.icNumber = v; }
    public String getEmail()                   { return email; }
    public void setEmail(String v)             { this.email = v; }
    public String getPhoneNumber()             { return phoneNumber; }
    public void setPhoneNumber(String v)       { this.phoneNumber = v; }
    public String getAddress()                 { return address; }
    public void setAddress(String v)           { this.address = v; }
    public String getGender()                  { return gender; }
    public void setGender(String v)            { this.gender = v; }
    public Department getDepartment()          { return department; }
    public void setDepartment(Department v)    { this.department = v; }
    public UserRole getRole()                  { return role; }
    public void setRole(UserRole v)            { this.role = v; }
    public LocalDate getDateJoined()           { return dateJoined; }
    public void setDateJoined(LocalDate v)     { this.dateJoined = v; }
    public String getUsername()                { return username; }
    public void setUsername(String v)          { this.username = v; }
    public String getPasswordHash()            { return passwordHash; }
    public void setPasswordHash(String v)      { this.passwordHash = v; }
    public Boolean getIsActive()               { return isActive; }
    public void setIsActive(Boolean v)         { this.isActive = v; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "[" + employeeId + "] " + getFullName() + " (" + role + ") - " + department;
    }
}