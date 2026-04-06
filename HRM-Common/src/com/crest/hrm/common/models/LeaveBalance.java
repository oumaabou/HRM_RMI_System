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
 * Tracks the number of leave days available and used for an employee.
 * Implements Serializable for RMI transmission.
 */
public class LeaveBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer employeeId;
    private int year;
    // Leave entitlements (days granted per year)
    private int annualLeaveEntitlement;
    private int sickLeaveEntitlement;
    private int emergencyLeaveEntitlement;
    private int maternityLeaveEntitlement;
    private int paternityLeaveEntitlement;
    // Leave taken (days already used)
    private int annualLeaveTaken;
    private int sickLeaveTaken;
    private int emergencyLeaveTaken;
    private int maternityLeaveTaken;
    private int paternityLeaveTaken;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public LeaveBalance() {}
    public LeaveBalance(Integer employeeId, int year) {
        this.employeeId = employeeId;
        this.year       = year;
        // Default entitlements (Malaysian Labour Law standard)
        this.annualLeaveEntitlement    = 14;
        this.sickLeaveEntitlement      = 14;
        this.emergencyLeaveEntitlement = 3;
        this.maternityLeaveEntitlement = 60;
        this.paternityLeaveEntitlement = 3;
    }
    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------
    public int getRemainingAnnualLeave()    { return annualLeaveEntitlement    - annualLeaveTaken; }
    public int getRemainingSickLeave()      { return sickLeaveEntitlement      - sickLeaveTaken; }
    public int getRemainingEmergencyLeave() { return emergencyLeaveEntitlement - emergencyLeaveTaken; }
    public int getRemainingMaternityLeave() { return maternityLeaveEntitlement - maternityLeaveTaken; }
    public int getRemainingPaternityLeave() { return paternityLeaveEntitlement - paternityLeaveTaken; }
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    public Integer getEmployeeId()                  { return employeeId; }
    public void   setEmployeeId(Integer v)          { this.employeeId = v; }
    public int  getYear()                           { return year; }
    public void setYear(int v)                      { this.year = v; }
    public int  getAnnualLeaveEntitlement()         { return annualLeaveEntitlement; }
    public void setAnnualLeaveEntitlement(int v)    { this.annualLeaveEntitlement = v; }
    public int  getSickLeaveEntitlement()           { return sickLeaveEntitlement; }
    public void setSickLeaveEntitlement(int v)      { this.sickLeaveEntitlement = v; }
    public int  getEmergencyLeaveEntitlement()      { return emergencyLeaveEntitlement; }
    public void setEmergencyLeaveEntitlement(int v) { this.emergencyLeaveEntitlement = v; }
    public int  getMaternityLeaveEntitlement()      { return maternityLeaveEntitlement; }
    public void setMaternityLeaveEntitlement(int v) { this.maternityLeaveEntitlement = v; }
    public int  getPaternityLeaveEntitlement()      { return paternityLeaveEntitlement; }
    public void setPaternityLeaveEntitlement(int v) { this.paternityLeaveEntitlement = v; }
    public int  getAnnualLeaveTaken()               { return annualLeaveTaken; }
    public void setAnnualLeaveTaken(int v)          { this.annualLeaveTaken = v; }
    public int  getSickLeaveTaken()                 { return sickLeaveTaken; }
    public void setSickLeaveTaken(int v)            { this.sickLeaveTaken = v; }
    public int  getEmergencyLeaveTaken()            { return emergencyLeaveTaken; }
    public void setEmergencyLeaveTaken(int v)       { this.emergencyLeaveTaken = v; }
    public int  getMaternityLeaveTaken()            { return maternityLeaveTaken; }
    public void setMaternityLeaveTaken(int v)       { this.maternityLeaveTaken = v; }
    public int  getPaternityLeaveTaken()            { return paternityLeaveTaken; }
    public void setPaternityLeaveTaken(int v)       { this.paternityLeaveTaken = v; }
    @Override
    public String toString() {
        return "LeaveBalance [" + employeeId + " | " + year + "] " +
               "Annual: " + getRemainingAnnualLeave() + "/" + annualLeaveEntitlement + " | " +
               "Sick: " + getRemainingSickLeave() + "/" + sickLeaveEntitlement;
    }
}