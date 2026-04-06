package com.crest.hrm.common.models;

import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

public class LeaveApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer leaveId;           // Changed to Integer
    private Integer employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDays;
    private String reason;
    private LeaveStatus status;
    private Timestamp appliedDate;     // Changed to Timestamp
    private Integer reviewedBy;

    public LeaveApplication() {}

    public LeaveApplication(Integer leaveId, Integer employeeId,
                            LeaveType leaveType, LocalDate startDate,
                            LocalDate endDate, String reason, Timestamp appliedDate) {
        this.leaveId     = leaveId;
        this.employeeId  = employeeId;
        this.leaveType   = leaveType;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.totalDays   = calculateTotalDays(startDate, endDate);
        this.reason      = reason;
        this.status      = LeaveStatus.PENDING;
        this.appliedDate = appliedDate; 
    }

    private int calculateTotalDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return (int) (end.toEpochDay() - start.toEpochDay()) + 1;
    }

    public Integer getLeaveId()                     { return leaveId; }
    public void setLeaveId(Integer v)               { this.leaveId = v; }
    public Integer getEmployeeId()                  { return employeeId; }
    public void setEmployeeId(Integer v)            { this.employeeId = v; }
    public LeaveType getLeaveType()                 { return leaveType; }
    public void setLeaveType(LeaveType v)           { this.leaveType = v; }
    public LocalDate getStartDate()                 { return startDate; }
    public void setStartDate(LocalDate v)           { this.startDate = v; }
    public LocalDate getEndDate()                   { return endDate; }
    public void setEndDate(LocalDate v)             { this.endDate = v; }
    public int getTotalDays()                       { return totalDays; }
    public void setTotalDays(int v)                 { this.totalDays = v; }
    public String getReason()                       { return reason; }
    public void setReason(String v)                 { this.reason = v; }
    public LeaveStatus getStatus()                  { return status; }
    public void setStatus(LeaveStatus v)            { this.status = v; }
    public Timestamp getAppliedDate()               { return appliedDate; }
    public void setAppliedDate(Timestamp v)         { this.appliedDate = v; }
    public Integer getReviewedBy()                  { return reviewedBy; }
    public void setReviewedBy(Integer v)            { this.reviewedBy = v; }

    @Override
    public String toString() {
        return "[" + leaveId + "] " + leaveType + " | " +
               startDate + " to " + endDate + " (" + totalDays + " days) | " + status;
    }
}