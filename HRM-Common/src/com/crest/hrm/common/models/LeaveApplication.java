/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.models;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.enums.LeaveStatus;
import com.crest.hrm.common.enums.LeaveType;
import java.io.Serializable;
import java.time.LocalDate;
/**
 * Represents a leave application submitted by an employee.
 * Implements Serializable for RMI transmission.
 */
public class LeaveApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    private String leaveId;
    private Integer employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDays;
    private String reason;
    private LeaveStatus status;
    private LocalDate appliedDate;
    private Integer reviewedBy;        // Employee ID of HR who approved/rejected
    private String reviewRemarks;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public LeaveApplication() {}
    public LeaveApplication(String leaveId, Integer employeeId,
                            LeaveType leaveType, LocalDate startDate,
                            LocalDate endDate, String reason) {
        this.leaveId     = leaveId;
        this.employeeId  = employeeId;
        this.leaveType   = leaveType;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.totalDays   = calculateTotalDays(startDate, endDate);
        this.reason      = reason;
        this.status      = LeaveStatus.PENDING;
        this.appliedDate = LocalDate.now();
    }
    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------
    private int calculateTotalDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return (int) (end.toEpochDay() - start.toEpochDay()) + 1;
    }
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    public String getLeaveId()                      { return leaveId; }
    public void   setLeaveId(String v)              { this.leaveId = v; }
    public Integer getEmployeeId()                  { return employeeId; }
    public void   setEmployeeId(Integer v)          { this.employeeId = v; }
    public LeaveType getLeaveType()                 { return leaveType; }
    public void      setLeaveType(LeaveType v)      { this.leaveType = v; }
    public LocalDate getStartDate()                 { return startDate; }
    public void      setStartDate(LocalDate v)      { this.startDate = v; }
    public LocalDate getEndDate()                   { return endDate; }
    public void      setEndDate(LocalDate v)        { this.endDate = v; }
    public int  getTotalDays()                      { return totalDays; }
    public void setTotalDays(int v)                 { this.totalDays = v; }
    public String getReason()                       { return reason; }
    public void   setReason(String v)               { this.reason = v; }
    public LeaveStatus getStatus()                  { return status; }
    public void        setStatus(LeaveStatus v)     { this.status = v; }
    public LocalDate getAppliedDate()               { return appliedDate; }
    public void      setAppliedDate(LocalDate v)    { this.appliedDate = v; }
    public Integer getReviewedBy()                  { return reviewedBy; }
    public void   setReviewedBy(Integer v)          { this.reviewedBy = v; }
    public String getReviewRemarks()                { return reviewRemarks; }
    public void   setReviewRemarks(String v)        { this.reviewRemarks = v; }
    @Override
    public String toString() {
        return "[" + leaveId + "] " + leaveType + " | " +
               startDate + " to " + endDate + " (" + totalDays + " days) | " + status;
    }
}