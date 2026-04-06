package com.crest.hrm.server.dao;

import com.crest.hrm.common.models.FamilyDetails;
import java.sql.SQLException;
import java.util.List;

/**
 * FamilyDAO - Interface for Family Details database operations
 * 
 * Handles all family member-related database operations.
 * 
 * IMPORTANT: Your database stores ONE ROW PER FAMILY MEMBER.
 * Each family member is a separate FamilyDetails object.
 * 
 * Implementation: FamilyDAOImpl.java
 */
public interface FamilyDAO {
    
    // ========================================================================
    // FAMILY MEMBER OPERATIONS
    // ========================================================================
    
    /**
     * Save a single family member to the database
     * 
     * @param familyDetails The FamilyDetails object for ONE family member
     * @return The generated family ID (int)
     * @throws SQLException if database error occurs
     */
    int saveFamilyMember(FamilyDetails familyDetails) throws SQLException;
    
    /**
     * Save multiple family members for an employee
     * 
     * @param familyMembers List of FamilyDetails objects
     * @return Number of successfully saved records
     * @throws SQLException if database error occurs
     */
    int saveAllFamilyMembers(List<FamilyDetails> familyMembers) throws SQLException;
    
    /**
     * Find a family member by their ID
     * 
     * @param familyId The family ID to search for
     * @return FamilyDetails object if found, null if not found
     * @throws SQLException if database error occurs
     */
    FamilyDetails findById(int familyId) throws SQLException;
    
    /**
     * Get all family members for a specific employee
     * 
     * @param employeeId The employee ID
     * @return List of FamilyDetails objects (one per family member)
     * @throws SQLException if database error occurs
     */
    List<FamilyDetails> findByEmployee(int employeeId) throws SQLException;
    
    /**
     * Update a family member's information
     * 
     * @param familyDetails The FamilyDetails object with updated values
     * @return true if update was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updateFamilyMember(FamilyDetails familyDetails) throws SQLException;
    
    /**
     * Delete a single family member by their ID
     * 
     * @param familyId The family ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean deleteFamilyMember(int familyId) throws SQLException;
    
    /**
     * Delete all family members for a specific employee
     * 
     * @param employeeId The employee ID
     * @return Number of deleted records
     * @throws SQLException if database error occurs
     */
    int deleteAllByEmployee(int employeeId) throws SQLException;
    
    /**
     * Replace all family members for an employee (delete old, insert new)
     * Useful when employee updates their entire family details form
     * 
     * @param employeeId The employee ID
     * @param newFamilyMembers List of new FamilyDetails objects
     * @return true if replacement was successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean replaceAllFamilyMembers(int employeeId, List<FamilyDetails> newFamilyMembers) throws SQLException;
    
    /**
     * Check if an employee has any family members registered
     * 
     * @param employeeId The employee ID
     * @return true if at least one family member exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean hasFamilyMembers(int employeeId) throws SQLException;
    
    /**
     * Get the count of family members for an employee
     * 
     * @param employeeId The employee ID
     * @return Number of family members
     * @throws SQLException if database error occurs
     */
    int getFamilyMemberCount(int employeeId) throws SQLException;
    
    /**
     * Get only emergency contacts for an employee
     * 
     * @param employeeId The employee ID
     * @return List of FamilyDetails where relationship is emergency contact
     * @throws SQLException if database error occurs
     */
    List<FamilyDetails> getEmergencyContacts(int employeeId) throws SQLException;
}