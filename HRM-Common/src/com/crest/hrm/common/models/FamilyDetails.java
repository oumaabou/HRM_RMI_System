/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.models;

/**
 *
 * @author shuhaab
 */
import java.io.Serializable;
/**
 * Stores personal and family details for an employee.
 * Implements Serializable for RMI transmission.
 */
public class FamilyDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    private String employeeId;
    private String spouseName;
    private int    numberOfChildren;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;
    private String homeAddress;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public FamilyDetails() {}
    public FamilyDetails(String employeeId) {
        this.employeeId = employeeId;
    }
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    public String getEmployeeId()                          { return employeeId; }
    public void   setEmployeeId(String v)                  { this.employeeId = v; }
    public String getSpouseName()                          { return spouseName; }
    public void   setSpouseName(String v)                  { this.spouseName = v; }
    public int  getNumberOfChildren()                      { return numberOfChildren; }
    public void setNumberOfChildren(int v)                 { this.numberOfChildren = v; }
    public String getEmergencyContactName()                { return emergencyContactName; }
    public void   setEmergencyContactName(String v)        { this.emergencyContactName = v; }
    public String getEmergencyContactPhone()               { return emergencyContactPhone; }
    public void   setEmergencyContactPhone(String v)       { this.emergencyContactPhone = v; }
    public String getEmergencyContactRelationship()        { return emergencyContactRelationship; }
    public void   setEmergencyContactRelationship(String v){ this.emergencyContactRelationship = v; }
    public String getHomeAddress()                         { return homeAddress; }
    public void   setHomeAddress(String v)                 { this.homeAddress = v; }
    @Override
    public String toString() {
        return "FamilyDetails [" + employeeId + "] " +
               "Spouse: " + (spouseName != null ? spouseName : "N/A") + " | " +
               "Children: " + numberOfChildren + " | " +
               "Emergency: " + emergencyContactName + " (" + emergencyContactRelationship + ")";
    }
}

