package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.server.dao.FamilyDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FamilyDAOImpl - Implementation of FamilyDAO interface
 * 
 * TODO: Complete this file after Member 2 rewrites FamilyDetails.java
 * 
 * REQUIRED FamilyDetails model structure (must match database):
 * 
 * public class FamilyDetails implements Serializable {
 *     private Integer familyId;
 *     private Integer employeeId;
 *     private String familyMemberName;
 *     private String relationship;  // SPOUSE, CHILD, PARENT, SIBLING
 *     private String phoneNumber;
 *     // getters/setters
 * }
 * 
 * Current FamilyDetails model has spouseName + numberOfChildren - this is WRONG.
 * Database stores ONE ROW PER FAMILY MEMBER.
 */
public class FamilyDAOImpl implements FamilyDAO {

    // ========================================================================
    // FAMILY MEMBER OPERATIONS
    // ========================================================================
    
    @Override
    public int saveFamilyMember(FamilyDetails familyDetails) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // Database columns: family_member_name, relationship, phone_number
        // SQL: INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number)
        //      VALUES (?, ?, ?, ?)
        // Return generated family_id
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public int saveAllFamilyMembers(List<FamilyDetails> familyMembers) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // Loop through list and call saveFamilyMember() for each
        // Return count of successful saves
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public FamilyDetails findById(int familyId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: SELECT * FROM family_details WHERE family_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public List<FamilyDetails> findByEmployee(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: SELECT * FROM family_details WHERE employee_id = ? ORDER BY relationship, family_member_name
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public boolean updateFamilyMember(FamilyDetails familyDetails) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: UPDATE family_details SET family_member_name = ?, relationship = ?, phone_number = ?
        //      WHERE family_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public boolean deleteFamilyMember(int familyId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: DELETE FROM family_details WHERE family_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public int deleteAllByEmployee(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: DELETE FROM family_details WHERE employee_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public boolean replaceAllFamilyMembers(int employeeId, List<FamilyDetails> newFamilyMembers) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // 1. deleteAllByEmployee(employeeId)
        // 2. saveAllFamilyMembers(newFamilyMembers)
        // Return true if both operations succeed
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public boolean hasFamilyMembers(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: SELECT COUNT(*) FROM family_details WHERE employee_id = ?
        // Return count > 0
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public int getFamilyMemberCount(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: SELECT COUNT(*) FROM family_details WHERE employee_id = ?
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
    
    @Override
    public List<FamilyDetails> getEmergencyContacts(int employeeId) throws SQLException {
        // TODO: Implement after Member 2 rewrites FamilyDetails.java
        // SQL: SELECT * FROM family_details WHERE employee_id = ? AND relationship IN ('SPOUSE', 'PARENT', 'SIBLING')
        throw new UnsupportedOperationException("TODO: Implement after Member 2 rewrites FamilyDetails.java");
    }
}