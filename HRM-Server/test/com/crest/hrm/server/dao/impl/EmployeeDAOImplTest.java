package com.crest.hrm.server.dao.impl;

import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.enums.Department;
import com.crest.hrm.common.enums.UserRole;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

public class EmployeeDAOImplTest {
    
    private EmployeeDAO employeeDAO;
    private Integer testEmployeeId;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Clean up any leftover test data once before all tests
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE '%test%' OR username LIKE '%find%' OR username LIKE '%login%'");
            stmt.executeUpdate("DELETE FROM employees WHERE first_name IN ('Test', 'FindByID', 'ICTest', 'Login', 'Update', 'DeleteMe', 'Exists', 'ExistsUser', 'First', 'Second')");
        } catch (Exception e) {
            // Ignore if tables don't exist
        }
    }
    
    @Before
    public void setUp() throws Exception {
        employeeDAO = new EmployeeDAOImpl();
        testEmployeeId = null;
    }
    
    @After
    public void tearDown() throws Exception {
        if (testEmployeeId != null) {
            try {
                // Delete child records first
                try (Connection conn = DatabaseConnection.getConnection()) {
                    try (var ps = conn.prepareStatement("DELETE FROM users WHERE employee_id = ?")) {
                        ps.setInt(1, testEmployeeId);
                        ps.executeUpdate();
                    }
                    try (var ps = conn.prepareStatement("DELETE FROM leave_balance WHERE employee_id = ?")) {
                        ps.setInt(1, testEmployeeId);
                        ps.executeUpdate();
                    }
                    try (var ps = conn.prepareStatement("DELETE FROM family_details WHERE employee_id = ?")) {
                        ps.setInt(1, testEmployeeId);
                        ps.executeUpdate();
                    }
                    try (var ps = conn.prepareStatement("DELETE FROM leaves WHERE employee_id = ?")) {
                        ps.setInt(1, testEmployeeId);
                        ps.executeUpdate();
                    }
                }
                employeeDAO.delete(testEmployeeId);
            } catch (Exception e) {
                // Ignore cleanup errors
            }
            testEmployeeId = null;
        }
    }
    
    private Employee createTestEmployee(String firstName, String username) throws Exception {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName("Test");
        
        // Generate unique IC within 20 char limit
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String ic = firstName.substring(0, Math.min(3, firstName.length())) + uniqueId.substring(uniqueId.length() - 8);
        employee.setIcNumber(ic);
        
        employee.setEmail(firstName.toLowerCase() + uniqueId + "@test.com");
        employee.setPhoneNumber("0123456789");
        employee.setDepartment(Department.INFORMATION_TECHNOLOGY);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setDateJoined(LocalDate.now());
        
        String uniqueUsername = username + uniqueId.substring(uniqueId.length() - 6);
        employee.setUsername(uniqueUsername);
        employee.setPasswordHash("test_password_hash");
        return employee;
    }
    
    @Test
    public void testSaveEmployee_Success() throws Exception {
        Employee employee = createTestEmployee("Test", "testuser");
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        assertNotNull(employeeId);
        assertTrue(employeeId > 0);
    }
    
    @Test
    public void testFindById_ExistingEmployee_ReturnsEmployee() throws Exception {
        Employee employee = createTestEmployee("FindByID", "findbyid");
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        Employee found = employeeDAO.findById(employeeId);
        
        assertNotNull(found);
        assertEquals(employeeId, found.getEmployeeId().intValue());
    }
    
    @Test
    public void testFindById_NonExistentEmployee_ReturnsNull() throws Exception {
        Employee found = employeeDAO.findById(99999);
        assertNull(found);
    }
    
    @Test
    public void testFindByIC_ExistingIC_ReturnsEmployee() throws Exception {
        Employee employee = createTestEmployee("ICTest", "ictest");
        String icNumber = employee.getIcNumber();
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        Employee found = employeeDAO.findByIC(icNumber);
        
        assertNotNull(found);
        assertEquals(icNumber, found.getIcNumber());
    }
    
    @Test
    public void testFindByIC_NonExistentIC_ReturnsNull() throws Exception {
        Employee found = employeeDAO.findByIC("NONEXISTENT123");
        assertNull(found);
    }
    
    @Test
    public void testFindByUsername_ExistingUsername_ReturnsEmployee() throws Exception {
        Employee employee = createTestEmployee("Login", "loginuser");
        String username = employee.getUsername();
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        Employee found = employeeDAO.findByUsername(username);
        
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }
    
    @Test
    public void testFindByUsername_NonExistentUsername_ReturnsNull() throws Exception {
        Employee found = employeeDAO.findByUsername("nonexistentuser12345");
        assertNull(found);
    }
    
    @Test
    public void testUpdate_ExistingEmployee_ReturnsTrue() throws Exception {
        Employee employee = createTestEmployee("Update", "update");
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        employee.setFirstName("UpdatedFirstName");
        employee.setLastName("UpdatedLastName");
        employee.setPhoneNumber("9999999999");
        
        boolean result = employeeDAO.update(employee);
        
        assertTrue(result);
        
        Employee updated = employeeDAO.findById(employeeId);
        assertEquals("UpdatedFirstName", updated.getFirstName());
        assertEquals("UpdatedLastName", updated.getLastName());
    }
    
    @Test
    public void testUpdate_NonExistentEmployee_ReturnsFalse() throws Exception {
        Employee employee = createTestEmployee("Ghost", "ghost");
        employee.setEmployeeId(99999);
        
        boolean result = employeeDAO.update(employee);
        assertFalse(result);
    }
    
    @Test
    public void testFindAll_ReturnsList() throws Exception {
        List<Employee> employees = employeeDAO.findAll();
        assertNotNull(employees);
    }
    
    @Test
    public void testDelete_ExistingEmployee_ReturnsTrue() throws Exception {
        Employee employee = createTestEmployee("DeleteMe", "deleteme");
        int employeeId = employeeDAO.save(employee);
        
        // Delete child records first
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (var ps = conn.prepareStatement("DELETE FROM users WHERE employee_id = ?")) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("DELETE FROM leave_balance WHERE employee_id = ?")) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("DELETE FROM family_details WHERE employee_id = ?")) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("DELETE FROM leaves WHERE employee_id = ?")) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            }
        }
        
        boolean result = employeeDAO.delete(employeeId);
        
        assertTrue(result);
        
        Employee deleted = employeeDAO.findById(employeeId);
        assertNull(deleted);
        
        testEmployeeId = null;
    }
    
    @Test
    public void testDelete_NonExistentEmployee_ReturnsFalse() throws Exception {
        boolean result = employeeDAO.delete(99999);
        assertFalse(result);
    }
    
    @Test
    public void testExistsByIC_ExistingIC_ReturnsTrue() throws Exception {
        Employee employee = createTestEmployee("Exists", "exists");
        String icNumber = employee.getIcNumber();
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        boolean exists = employeeDAO.existsByIC(icNumber);
        assertTrue(exists);
    }
    
    @Test
    public void testExistsByIC_NonExistentIC_ReturnsFalse() throws Exception {
        boolean exists = employeeDAO.existsByIC("NOTEXIST123456");
        assertFalse(exists);
    }
    
    @Test
    public void testExistsByUsername_ExistingUsername_ReturnsTrue() throws Exception {
        Employee employee = createTestEmployee("ExistsUser", "existsuser");
        String username = employee.getUsername();
        int employeeId = employeeDAO.save(employee);
        testEmployeeId = employeeId;
        
        boolean exists = employeeDAO.existsByUsername(username);
        assertTrue(exists);
    }
    
    @Test
    public void testExistsByUsername_NonExistentUsername_ReturnsFalse() throws Exception {
        boolean exists = employeeDAO.existsByUsername("notexistsuser12345");
        assertFalse(exists);
    }
}