/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.interfaces;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.exceptions.DuplicateEmployeeException;
import com.crest.hrm.common.exceptions.EmployeeNotFoundException;
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.LeaveApplication;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
/**
 * RMI Interface for HR Staff operations.
 * Only users with UserRole.HR_STAFF may invoke these methods.
 */
public interface HRService extends Remote {
    /**
     * Registers a new employee in the system.
     *
     * @param employee the employee data to register
     * @return the registered Employee with a generated employeeId
     * @throws DuplicateEmployeeException if IC/Passport number already exists
     * @throws RemoteException            if a network/RMI error occurs
     */
    Employee registerEmployee(Employee employee)
            throws DuplicateEmployeeException, RemoteException;
    /**
     * Retrieves all employees registered in the system.
     *
     * @return list of all employees
     * @throws RemoteException if a network/RMI error occurs
     */
    List<Employee> getAllEmployees() throws RemoteException;
    List<LeaveApplication> getPendingLeaves() throws RemoteException;
    /**
     * Retrieves an employee by their unique ID.
     *
     * @param employeeId the employee ID to search for
     * @return the matching Employee
     * @throws EmployeeNotFoundException if no employee has that ID
     * @throws RemoteException           if a network/RMI error occurs
     */
    Employee getEmployeeById(String employeeId)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Generates a yearly leave report for a specific employee.
     *
     * @param employeeId the ID of the employee
     * @param year       the year for the report
     * @return list of leave applications for that employee in that year
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    List<LeaveApplication> generateYearlyLeaveReport(String employeeId, int year)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Approves or rejects a pending leave application.
     *
     * @param leaveId    the ID of the leave application
     * @param approved   true to approve, false to reject
     * @param remarks    optional remarks from HR
     * @param hrStaffId  the ID of the HR staff reviewing the application
     * @return the updated LeaveApplication
     * @throws RemoteException if a network/RMI error occurs
     */
    LeaveApplication reviewLeaveApplication(String leaveId, boolean approved,
                                            String remarks, String hrStaffId)
            throws RemoteException;
}

