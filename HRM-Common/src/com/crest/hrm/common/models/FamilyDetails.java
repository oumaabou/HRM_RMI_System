package com.crest.hrm.common.models;

import java.io.Serializable;

public class FamilyDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer familyId;
    private Integer employeeId;
    private String familyMemberName;
    private String relationship;
    private String phoneNumber;

    public FamilyDetails() {}

    public FamilyDetails(Integer familyId, Integer employeeId, String familyMemberName, 
                         String relationship, String phoneNumber) {
        this.familyId = familyId;
        this.employeeId = employeeId;
        this.familyMemberName = familyMemberName;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    public Integer getFamilyId()                    { return familyId; }
    public void setFamilyId(Integer v)              { this.familyId = v; }
    public Integer getEmployeeId()                  { return employeeId; }
    public void setEmployeeId(Integer v)            { this.employeeId = v; }
    public String getFamilyMemberName()             { return familyMemberName; }
    public void setFamilyMemberName(String v)       { this.familyMemberName = v; }
    public String getRelationship()                 { return relationship; }
    public void setRelationship(String v)           { this.relationship = v; }
    public String getPhoneNumber()                  { return phoneNumber; }
    public void setPhoneNumber(String v)            { this.phoneNumber = v; }

    @Override
    public String toString() {
        return "FamilyMember [" + familyId + "] " + familyMemberName + " (" + relationship + ") - " + phoneNumber;
    }
}