package com.crest.hrm.common.models;

import java.io.Serializable;

public class LeaveBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer balanceId;
    private Integer employeeId;
    private Integer leaveYear;
    private Double annualRemaining;      // DECIMAL mapping
    private Double sickRemaining;        // DECIMAL mapping
    private Double emergencyRemaining;   // DECIMAL mapping

    public LeaveBalance() {}

    public LeaveBalance(Integer balanceId, Integer employeeId, Integer leaveYear, 
                        Double annualRemaining, Double sickRemaining, Double emergencyRemaining) {
        this.balanceId = balanceId;
        this.employeeId = employeeId;
        this.leaveYear = leaveYear;
        this.annualRemaining = annualRemaining;
        this.sickRemaining = sickRemaining;
        this.emergencyRemaining = emergencyRemaining;
    }

    public Integer getBalanceId()                   { return balanceId; }
    public void setBalanceId(Integer v)             { this.balanceId = v; }
    public Integer getEmployeeId()                  { return employeeId; }
    public void setEmployeeId(Integer v)            { this.employeeId = v; }
    public Integer getLeaveYear()                   { return leaveYear; }
    public void setLeaveYear(Integer v)             { this.leaveYear = v; }
    public Double getAnnualRemaining()              { return annualRemaining; }
    public void setAnnualRemaining(Double v)        { this.annualRemaining = v; }
    public Double getSickRemaining()                { return sickRemaining; }
    public void setSickRemaining(Double v)          { this.sickRemaining = v; }
    public Double getEmergencyRemaining()           { return emergencyRemaining; }
    public void setEmergencyRemaining(Double v)     { this.emergencyRemaining = v; }

    @Override
    public String toString() {
        return "LeaveBalance [" + employeeId + " | Year: " + leaveYear + "] " +
               "Annual Remaining: " + annualRemaining + " | Sick: " + sickRemaining;
    }
}