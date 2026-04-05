/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.interfaces;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.exceptions.EmployeeNotFoundException;
import com.crest.hrm.common.exceptions.InvalidLeaveRequestException;
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
/**
 * RMI Interface for Employee self-service operations.
 * Authenticated employees use this to manage their own data and leave.
 */
public interface EmployeeService extends Remote {
    /**
     * Updates the personal details of an employee.
     *
     * @param employee the updated employee data
     * @return the updated Employee object
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    Employee updatePersonalDetails(Employee employee)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Updates the family details of an employee.
     *
     * @param familyDetails the updated family details
     * @return the updated FamilyDetails object
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    FamilyDetails updateFamilyDetails(FamilyDetails familyDetails)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Retrieves the family details of a specific employee.
     *
     * @param employeeId the employee ID
     * @return the FamilyDetails for that employee
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    FamilyDetails getFamilyDetails(String employeeId)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Gets the leave balance for a specific employee in a given year.
     *
     * @param employeeId the employee ID
     * @param year       the year to check
     * @return the LeaveBalance for that year
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    LeaveBalance checkLeaveBalance(String employeeId, int year)
            throws EmployeeNotFoundException, RemoteException;
    /**
     * Submits a new leave application for an employee.
     *
     * @param application the leave application to submit
     * @return the submitted LeaveApplication with a generated leaveId
     * @throws EmployeeNotFoundException    if the employee does not exist
     * @throws InvalidLeaveRequestException if the request violates business rules
     * @throws RemoteException              if a network/RMI error occurs
     */
    LeaveApplication applyLeave(LeaveApplication application)
            throws EmployeeNotFoundException, InvalidLeaveRequestException, RemoteException;
    /**
     * Cancels a pending leave application.
     *
     * @param leaveId    the ID of the leave application to cancel
     * @param employeeId the ID of the employee making the request
     * @return the updated (cancelled) LeaveApplication
     * @throws RemoteException if a network/RMI error occurs
     */
    LeaveApplication cancelLeave(String leaveId, String employeeId)
            throws RemoteException;
    /**
     * Retrieves the current status of a specific leave application.
     *
     * @param leaveId the leave application ID
     * @return the LeaveApplication with up-to-date status
     * @throws RemoteException if a network/RMI error occurs
     */
    LeaveApplication viewLeaveStatus(String leaveId) throws RemoteException;
    /**
     * Retrieves the full leave history for an employee.
     *
     * @param employeeId the employee ID
     * @return list of all leave applications for that employee
     * @throws EmployeeNotFoundException if the employee does not exist
     * @throws RemoteException           if a network/RMI error occurs
     */
    List<LeaveApplication> viewLeaveHistory(String employeeId)
            throws EmployeeNotFoundException, RemoteException;
}

