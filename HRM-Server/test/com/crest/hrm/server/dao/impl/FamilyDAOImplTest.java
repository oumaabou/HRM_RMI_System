package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.FamilyDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FamilyDAOImplTest {
    
    private FamilyDAO familyDAO;
    private EmployeeDAO employeeDAO;
    private Integer testEmployeeId;
    private List<Integer> testFamilyIds;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Clean up any leftover test data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM family_details WHERE family_member_name LIKE '%Test%'");
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE 'familytest%'");
            stmt.executeUpdate("DELETE FROM employees WHERE first_name = 'FamilyTest'");
        } catch (Exception e) {
            // Ignore if tables don't exist
        }
    }
    
    @Before
    public void setUp() throws Exception {
        familyDAO = new FamilyDAOImpl();
        employeeDAO = new EmployeeDAOImpl();
        testEmployeeId = null;
        testFamilyIds = new ArrayList<>();
        
        // Create a test employee to associate family members with
        Employee employee = new Employee();
        employee.setFirstName("FamilyTest");
        employee.setLastName("User");
        employee.setIcNumber("FAMILY" + System.currentTimeMillis());
        employee.setEmail("familytest@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        employee.setUsername("familytest" + System.currentTimeMillis());
        employee.setPasswordHash("test_hash");
        
        testEmployeeId = employeeDAO.save(employee);
        
        // Small delay to ensure connection is ready
        Thread.sleep(50);
    }
    
    @After
    public void tearDown() throws Exception {
        // Delete test family members
        for (int familyId : testFamilyIds) {
            try {
                familyDAO.deleteFamilyMember(familyId);
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Delete test employee (cascade deletes related records)
        if (testEmployeeId != null) {
            try {
                // Delete child records first
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM family_details WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM leave_balance WHERE employee_id = " + testEmployeeId);
                    stmt.executeUpdate("DELETE FROM leaves WHERE employee_id = " + testEmployeeId);
                }
                employeeDAO.delete(testEmployeeId);
            } catch (Exception e) {
                // Ignore
            }
            testEmployeeId = null;
        }
        
        // Small delay to let connections close
        Thread.sleep(100);
    }
    
    // ========================================================================
    // SAVE FAMILY MEMBER TESTS
    // ========================================================================
    
    @Test
    public void testSaveFamilyMember_Success() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("John Test");
        family.setRelationship("SPOUSE");
        family.setPhoneNumber("012-1112223");
        
        int familyId = familyDAO.saveFamilyMember(family);
        testFamilyIds.add(familyId);
        
        assertTrue(familyId > 0);
    }
    
    @Test
    public void testSaveFamilyMember_Child_Success() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("Baby Test");
        family.setRelationship("CHILD");
        family.setPhoneNumber("012-1112224");
        
        int familyId = familyDAO.saveFamilyMember(family);
        testFamilyIds.add(familyId);
        
        assertTrue(familyId > 0);
    }
    
    // ========================================================================
    // SAVE ALL FAMILY MEMBERS TEST
    // ========================================================================
    
    @Test
    public void testSaveAllFamilyMembers_Success() throws Exception {
        List<FamilyDetails> familyMembers = new ArrayList<>();
        
        FamilyDetails spouse = new FamilyDetails();
        spouse.setEmployeeId(testEmployeeId);
        spouse.setFamilyMemberName("Jane Spouse");
        spouse.setRelationship("SPOUSE");
        spouse.setPhoneNumber("012-1112225");
        familyMembers.add(spouse);
        
        FamilyDetails child1 = new FamilyDetails();
        child1.setEmployeeId(testEmployeeId);
        child1.setFamilyMemberName("Child One");
        child1.setRelationship("CHILD");
        child1.setPhoneNumber("012-1112226");
        familyMembers.add(child1);
        
        FamilyDetails child2 = new FamilyDetails();
        child2.setEmployeeId(testEmployeeId);
        child2.setFamilyMemberName("Child Two");
        child2.setRelationship("CHILD");
        child2.setPhoneNumber("012-1112227");
        familyMembers.add(child2);
        
        int savedCount = familyDAO.saveAllFamilyMembers(familyMembers);
        
        assertEquals(3, savedCount);
        
        // Store IDs for cleanup
        for (FamilyDetails member : familyMembers) {
            testFamilyIds.add(member.getFamilyId());
        }
        
        // Verify all were saved
        List<FamilyDetails> retrieved = familyDAO.findByEmployee(testEmployeeId);
        assertTrue(retrieved.size() >= 3);
    }
    
    // ========================================================================
    // FIND BY ID TESTS
    // ========================================================================
    
    @Test
    public void testFindById_ExistingFamilyMember_ReturnsFamilyDetails() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("Find Me");
        family.setRelationship("PARENT");
        family.setPhoneNumber("012-1112228");
        
        int familyId = familyDAO.saveFamilyMember(family);
        testFamilyIds.add(familyId);
        
        FamilyDetails found = familyDAO.findById(familyId);
        
        assertNotNull(found);
        assertEquals(familyId, found.getFamilyId().intValue());
        assertEquals("Find Me", found.getFamilyMemberName());
        assertEquals("PARENT", found.getRelationship());
    }
    
    @Test
    public void testFindById_NonExistentFamilyMember_ReturnsNull() throws Exception {
        FamilyDetails found = familyDAO.findById(99999);
        assertNull(found);
    }
    
    // ========================================================================
    // FIND BY EMPLOYEE TESTS
    // ========================================================================
    
    @Test
    public void testFindByEmployee_ReturnsList() throws Exception {
        // Add two family members
        FamilyDetails member1 = new FamilyDetails();
        member1.setEmployeeId(testEmployeeId);
        member1.setFamilyMemberName("Member One");
        member1.setRelationship("SPOUSE");
        member1.setPhoneNumber("012-1112229");
        int id1 = familyDAO.saveFamilyMember(member1);
        testFamilyIds.add(id1);
        
        FamilyDetails member2 = new FamilyDetails();
        member2.setEmployeeId(testEmployeeId);
        member2.setFamilyMemberName("Member Two");
        member2.setRelationship("CHILD");
        member2.setPhoneNumber("012-1112230");
        int id2 = familyDAO.saveFamilyMember(member2);
        testFamilyIds.add(id2);
        
        List<FamilyDetails> familyMembers = familyDAO.findByEmployee(testEmployeeId);
        
        assertNotNull(familyMembers);
        assertTrue(familyMembers.size() >= 2);
    }
    
    @Test
    public void testFindByEmployee_NoFamilyMembers_ReturnsEmptyList() throws Exception {
        // Use a new employee with no family members
        Employee employee = new Employee();
        employee.setFirstName("NoFamily");
        employee.setLastName("Test");
        employee.setIcNumber("NOFAM" + System.currentTimeMillis());
        employee.setEmail("nofamily@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        employee.setUsername("nofamily" + System.currentTimeMillis());
        employee.setPasswordHash("test_hash");
        
        int newEmpId = employeeDAO.save(employee);
        
        List<FamilyDetails> familyMembers = familyDAO.findByEmployee(newEmpId);
        
        assertNotNull(familyMembers);
        assertTrue(familyMembers.isEmpty());
        
        // Clean up
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + newEmpId);
            stmt.executeUpdate("DELETE FROM employees WHERE employee_id = " + newEmpId);
        }
    }
    
    // ========================================================================
    // UPDATE FAMILY MEMBER TEST
    // ========================================================================
    
    @Test
    public void testUpdateFamilyMember_Success() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("Original Name");
        family.setRelationship("SIBLING");
        family.setPhoneNumber("012-1112231");
        
        int familyId = familyDAO.saveFamilyMember(family);
        testFamilyIds.add(familyId);
        
        // Update the family member
        family.setFamilyMemberName("Updated Name");
        family.setRelationship("SPOUSE");
        family.setPhoneNumber("012-9999999");
        
        boolean result = familyDAO.updateFamilyMember(family);
        
        assertTrue(result);
        
        // Verify update
        FamilyDetails updated = familyDAO.findById(familyId);
        assertEquals("Updated Name", updated.getFamilyMemberName());
        assertEquals("SPOUSE", updated.getRelationship());
        assertEquals("012-9999999", updated.getPhoneNumber());
    }
    
    @Test
    public void testUpdateFamilyMember_NonExistent_ReturnsFalse() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setFamilyId(99999);
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("Ghost");
        family.setRelationship("SPOUSE");
        family.setPhoneNumber("012-1112232");
        
        boolean result = familyDAO.updateFamilyMember(family);
        
        assertFalse(result);
    }
    
    // ========================================================================
    // DELETE FAMILY MEMBER TESTS
    // ========================================================================
    
    @Test
    public void testDeleteFamilyMember_Success() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("To Be Deleted");
        family.setRelationship("SPOUSE");
        family.setPhoneNumber("012-1112233");
        
        int familyId = familyDAO.saveFamilyMember(family);
        
        boolean result = familyDAO.deleteFamilyMember(familyId);
        
        assertTrue(result);
        
        // Verify deletion
        FamilyDetails deleted = familyDAO.findById(familyId);
        assertNull(deleted);
    }
    
    @Test
    public void testDeleteFamilyMember_NonExistent_ReturnsFalse() throws Exception {
        boolean result = familyDAO.deleteFamilyMember(99999);
        assertFalse(result);
    }
    
    // ========================================================================
    // DELETE ALL BY EMPLOYEE TEST
    // ========================================================================
    
    @Test
    public void testDeleteAllByEmployee_Success() throws Exception {
        // Add multiple family members
        FamilyDetails member1 = new FamilyDetails();
        member1.setEmployeeId(testEmployeeId);
        member1.setFamilyMemberName("Delete1");
        member1.setRelationship("SPOUSE");
        member1.setPhoneNumber("012-1112234");
        familyDAO.saveFamilyMember(member1);
        
        FamilyDetails member2 = new FamilyDetails();
        member2.setEmployeeId(testEmployeeId);
        member2.setFamilyMemberName("Delete2");
        member2.setRelationship("CHILD");
        member2.setPhoneNumber("012-1112235");
        familyDAO.saveFamilyMember(member2);
        
        // Count before delete
        int countBefore = familyDAO.getFamilyMemberCount(testEmployeeId);
        assertTrue(countBefore >= 2);
        
        // Delete all
        int deletedCount = familyDAO.deleteAllByEmployee(testEmployeeId);
        
        assertTrue(deletedCount >= 2);
        
        // Verify all deleted
        int countAfter = familyDAO.getFamilyMemberCount(testEmployeeId);
        assertEquals(0, countAfter);
    }
    
    // ========================================================================
    // REPLACE ALL FAMILY MEMBERS TEST
    // ========================================================================
    
    @Test
    public void testReplaceAllFamilyMembers_Success() throws Exception {
        // Add initial family members
        List<FamilyDetails> initialMembers = new ArrayList<>();
        
        FamilyDetails oldSpouse = new FamilyDetails();
        oldSpouse.setEmployeeId(testEmployeeId);
        oldSpouse.setFamilyMemberName("Old Spouse");
        oldSpouse.setRelationship("SPOUSE");
        oldSpouse.setPhoneNumber("012-1112236");
        initialMembers.add(oldSpouse);
        
        familyDAO.saveAllFamilyMembers(initialMembers);
        
        // Replace with new family members
        List<FamilyDetails> newMembers = new ArrayList<>();
        
        FamilyDetails newSpouse = new FamilyDetails();
        newSpouse.setEmployeeId(testEmployeeId);
        newSpouse.setFamilyMemberName("New Spouse");
        newSpouse.setRelationship("SPOUSE");
        newSpouse.setPhoneNumber("012-1112237");
        newMembers.add(newSpouse);
        
        FamilyDetails newChild = new FamilyDetails();
        newChild.setEmployeeId(testEmployeeId);
        newChild.setFamilyMemberName("New Child");
        newChild.setRelationship("CHILD");
        newChild.setPhoneNumber("012-1112238");
        newMembers.add(newChild);
        
        boolean result = familyDAO.replaceAllFamilyMembers(testEmployeeId, newMembers);
        
        assertTrue(result);
        
        // Store new IDs for cleanup
        for (FamilyDetails member : newMembers) {
            testFamilyIds.add(member.getFamilyId());
        }
        
        // Verify old members are gone and new members exist
        List<FamilyDetails> currentMembers = familyDAO.findByEmployee(testEmployeeId);
        assertEquals(2, currentMembers.size());
        
        boolean hasNewSpouse = currentMembers.stream()
            .anyMatch(m -> "New Spouse".equals(m.getFamilyMemberName()));
        boolean hasNewChild = currentMembers.stream()
            .anyMatch(m -> "New Child".equals(m.getFamilyMemberName()));
        
        assertTrue(hasNewSpouse);
        assertTrue(hasNewChild);
    }
    
    // ========================================================================
    // HAS FAMILY MEMBERS TEST
    // ========================================================================
    
    @Test
    public void testHasFamilyMembers_WithMembers_ReturnsTrue() throws Exception {
        FamilyDetails family = new FamilyDetails();
        family.setEmployeeId(testEmployeeId);
        family.setFamilyMemberName("Check Member");
        family.setRelationship("SPOUSE");
        family.setPhoneNumber("012-1112239");
        
        int familyId = familyDAO.saveFamilyMember(family);
        testFamilyIds.add(familyId);
        
        boolean hasMembers = familyDAO.hasFamilyMembers(testEmployeeId);
        
        assertTrue(hasMembers);
    }
    
    @Test
    public void testHasFamilyMembers_WithoutMembers_ReturnsFalse() throws Exception {
        // Use a new employee with no family members
        Employee employee = new Employee();
        employee.setFirstName("NoFamilyCheck");
        employee.setLastName("Test");
        employee.setIcNumber("NOFAMCHK" + System.currentTimeMillis());
        employee.setEmail("nofamilychk@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        employee.setUsername("nofamilychk" + System.currentTimeMillis());
        employee.setPasswordHash("test_hash");
        
        int newEmpId = employeeDAO.save(employee);
        
        boolean hasMembers = familyDAO.hasFamilyMembers(newEmpId);
        
        assertFalse(hasMembers);
        
        // Clean up
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE employee_id = " + newEmpId);
            stmt.executeUpdate("DELETE FROM employees WHERE employee_id = " + newEmpId);
        }
    }
    
    // ========================================================================
    // GET FAMILY MEMBER COUNT TEST
    // ========================================================================
    
    @Test
    public void testGetFamilyMemberCount_ReturnsCorrectCount() throws Exception {
        // Add 3 family members
        for (int i = 1; i <= 3; i++) {
            FamilyDetails family = new FamilyDetails();
            family.setEmployeeId(testEmployeeId);
            family.setFamilyMemberName("Count Member " + i);
            family.setRelationship("CHILD");
            family.setPhoneNumber("012-111224" + i);
            int id = familyDAO.saveFamilyMember(family);
            testFamilyIds.add(id);
        }
        
        int count = familyDAO.getFamilyMemberCount(testEmployeeId);
        
        assertTrue(count >= 3);
    }
    
    // ========================================================================
    // GET EMERGENCY CONTACTS TEST
    // ========================================================================
    
    @Test
    public void testGetEmergencyContacts_ReturnsSpouseAndParent() throws Exception {
        // Add SPOUSE (should be emergency contact)
        FamilyDetails spouse = new FamilyDetails();
        spouse.setEmployeeId(testEmployeeId);
        spouse.setFamilyMemberName("Emergency Spouse");
        spouse.setRelationship("SPOUSE");
        spouse.setPhoneNumber("012-1112241");
        int spouseId = familyDAO.saveFamilyMember(spouse);
        testFamilyIds.add(spouseId);
        
        // Add PARENT (should be emergency contact)
        FamilyDetails parent = new FamilyDetails();
        parent.setEmployeeId(testEmployeeId);
        parent.setFamilyMemberName("Emergency Parent");
        parent.setRelationship("PARENT");
        parent.setPhoneNumber("012-1112242");
        int parentId = familyDAO.saveFamilyMember(parent);
        testFamilyIds.add(parentId);
        
        // Add CHILD (should NOT be emergency contact by default)
        FamilyDetails child = new FamilyDetails();
        child.setEmployeeId(testEmployeeId);
        child.setFamilyMemberName("Regular Child");
        child.setRelationship("CHILD");
        child.setPhoneNumber("012-1112243");
        int childId = familyDAO.saveFamilyMember(child);
        testFamilyIds.add(childId);
        
        List<FamilyDetails> emergencyContacts = familyDAO.getEmergencyContacts(testEmployeeId);
        
        assertNotNull(emergencyContacts);
        assertTrue(emergencyContacts.size() >= 2);
        
        boolean hasSpouse = emergencyContacts.stream()
            .anyMatch(c -> "SPOUSE".equals(c.getRelationship()));
        boolean hasParent = emergencyContacts.stream()
            .anyMatch(c -> "PARENT".equals(c.getRelationship()));
        
        assertTrue(hasSpouse);
        assertTrue(hasParent);
    }
}