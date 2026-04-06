package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.server.dao.FamilyDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FamilyDAOImpl - Complete Implementation of FamilyDAO interface
 * 
 * Handles all family member database operations.
 * Database stores ONE ROW PER FAMILY MEMBER.
 */
public class FamilyDAOImpl implements FamilyDAO {

    // ========================================================================
    // FAMILY MEMBER OPERATIONS
    // ========================================================================
    
    @Override
    public int saveFamilyMember(FamilyDetails familyDetails) throws SQLException {
        String sql = "INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, familyDetails.getEmployeeId());
            ps.setString(2, familyDetails.getFamilyMemberName());
            ps.setString(3, familyDetails.getRelationship());
            ps.setString(4, familyDetails.getPhoneNumber());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Saving family member failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int familyId = generatedKeys.getInt(1);
                    familyDetails.setFamilyId(familyId);
                    return familyId;
                } else {
                    throw new SQLException("Saving family member failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public int saveAllFamilyMembers(List<FamilyDetails> familyMembers) throws SQLException {
        if (familyMembers == null || familyMembers.isEmpty()) {
            return 0;
        }
        
        int savedCount = 0;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Disable auto-commit for batch insert
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) " +
                         "VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (FamilyDetails member : familyMembers) {
                    ps.setInt(1, member.getEmployeeId());
                    ps.setString(2, member.getFamilyMemberName());
                    ps.setString(3, member.getRelationship());
                    ps.setString(4, member.getPhoneNumber());
                    ps.addBatch();
                }
                
                int[] results = ps.executeBatch();
                savedCount = results.length;
                
                // Retrieve generated IDs
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    int index = 0;
                    while (generatedKeys.next() && index < familyMembers.size()) {
                        familyMembers.get(index).setFamilyId(generatedKeys.getInt(1));
                        index++;
                    }
                }
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            throw new SQLException("Batch insert failed: " + e.getMessage(), e);
        }
        
        return savedCount;
    }
    
    @Override
    public FamilyDetails findById(int familyId) throws SQLException {
        String sql = "SELECT * FROM family_details WHERE family_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, familyId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFamilyDetails(rs);
                }
                return null;
            }
        }
    }
    
    @Override
    public List<FamilyDetails> findByEmployee(int employeeId) throws SQLException {
        String sql = "SELECT * FROM family_details WHERE employee_id = ? ORDER BY relationship, family_member_name";
        List<FamilyDetails> familyMembers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    familyMembers.add(mapResultSetToFamilyDetails(rs));
                }
            }
        }
        return familyMembers;
    }
    
    @Override
    public boolean updateFamilyMember(FamilyDetails familyDetails) throws SQLException {
        String sql = "UPDATE family_details SET family_member_name = ?, relationship = ?, phone_number = ? " +
                     "WHERE family_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, familyDetails.getFamilyMemberName());
            ps.setString(2, familyDetails.getRelationship());
            ps.setString(3, familyDetails.getPhoneNumber());
            ps.setInt(4, familyDetails.getFamilyId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    @Override
    public boolean deleteFamilyMember(int familyId) throws SQLException {
        String sql = "DELETE FROM family_details WHERE family_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, familyId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    @Override
    public int deleteAllByEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM family_details WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows;
        }
    }
    
    @Override
    public boolean replaceAllFamilyMembers(int employeeId, List<FamilyDetails> newFamilyMembers) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Delete all existing family members
                deleteAllByEmployee(employeeId);
                
                // Insert new family members
                if (newFamilyMembers != null && !newFamilyMembers.isEmpty()) {
                    // Ensure all new members have the correct employee ID
                    for (FamilyDetails member : newFamilyMembers) {
                        member.setEmployeeId(employeeId);
                    }
                    saveAllFamilyMembers(newFamilyMembers);
                }
                
                conn.commit();
                conn.setAutoCommit(true);
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new SQLException("Replace family members failed: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public boolean hasFamilyMembers(int employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM family_details WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
    
    @Override
    public int getFamilyMemberCount(int employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM family_details WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
    
    @Override
    public List<FamilyDetails> getEmergencyContacts(int employeeId) throws SQLException {
        String sql = "SELECT * FROM family_details WHERE employee_id = ? AND relationship IN ('SPOUSE', 'PARENT', 'SIBLING') ORDER BY relationship";
        List<FamilyDetails> emergencyContacts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emergencyContacts.add(mapResultSetToFamilyDetails(rs));
                }
            }
        }
        return emergencyContacts;
    }
    
    // ========================================================================
    // HELPER METHOD
    // ========================================================================
    
    private FamilyDetails mapResultSetToFamilyDetails(ResultSet rs) throws SQLException {
        FamilyDetails familyDetails = new FamilyDetails();
        
        familyDetails.setFamilyId(rs.getInt("family_id"));
        familyDetails.setEmployeeId(rs.getInt("employee_id"));
        familyDetails.setFamilyMemberName(rs.getString("family_member_name"));
        familyDetails.setRelationship(rs.getString("relationship"));
        familyDetails.setPhoneNumber(rs.getString("phone_number"));
        
        return familyDetails;
    }
}