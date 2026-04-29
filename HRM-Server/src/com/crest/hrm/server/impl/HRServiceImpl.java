package com.crest.hrm.server.impl;

import com.crest.hrm.common.exceptions.DuplicateEmployeeException;
import com.crest.hrm.common.exceptions.EmployeeNotFoundException;
import com.crest.hrm.common.interfaces.HRService;
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.impl.EmployeeDAOImpl;
import com.crest.hrm.server.dao.impl.LeaveDAOImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HRServiceImpl extends UnicastRemoteObject implements HRService {

    private final EmployeeDAO employeeDAO;
    private final LeaveDAO leaveDAO;

    public HRServiceImpl() throws RemoteException {
        super(
    0,
    new javax.rmi.ssl.SslRMIClientSocketFactory(),
    new javax.rmi.ssl.SslRMIServerSocketFactory()
);
        this.employeeDAO = new EmployeeDAOImpl();
        this.leaveDAO = new LeaveDAOImpl();
    }

    @Override
    public Employee registerEmployee(Employee employee)
            throws DuplicateEmployeeException, RemoteException {
        try {
            if (employee == null) {
                throw new RemoteException("Employee data is null");
            }

            if (employee.getIcNumber() != null && employeeDAO.existsByIC(employee.getIcNumber())) {
                throw new DuplicateEmployeeException(employee.getIcNumber());
            }

            if (employee.getUsername() != null && employeeDAO.existsByUsername(employee.getUsername())) {
                throw new DuplicateEmployeeException(employee.getUsername());
            }

            int generatedId = employeeDAO.save(employee);
            employee.setEmployeeId(generatedId);

            return employeeDAO.findById(generatedId);

        } catch (SQLException e) {
            throw new RemoteException("Database error while registering employee", e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws RemoteException {
        try {
            return employeeDAO.findAll();
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving employees", e);
        }
    }

    @Override
    public Employee getEmployeeById(String employeeId)
            throws EmployeeNotFoundException, RemoteException {
        try {
            int id = Integer.parseInt(employeeId);

            Employee employee = employeeDAO.findById(id);
            if (employee == null) {
                throw new EmployeeNotFoundException(id);
            }

            return employee;

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving employee", e);
        }
    }

    @Override
    public List<LeaveApplication> generateYearlyLeaveReport(String employeeId, int year)
            throws EmployeeNotFoundException, RemoteException {
        try {
            int id = Integer.parseInt(employeeId);

            Employee employee = employeeDAO.findById(id);
            if (employee == null) {
                throw new EmployeeNotFoundException(id);
            }

            return leaveDAO.getEmployeeYearlyReport(id, year);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while generating yearly leave report", e);
        }
    }

    @Override
    public LeaveApplication reviewLeaveApplication(String leaveId, boolean approved,
                                                   String remarks, String hrStaffId)
            throws RemoteException {
        try {
            int lId = Integer.parseInt(leaveId);
            int hrId = Integer.parseInt(hrStaffId);

            LeaveApplication leave = leaveDAO.findLeaveById(lId);
            if (leave == null) {
                return null;
            }

            String newStatus = approved ? "APPROVED" : "REJECTED";

            boolean updated = leaveDAO.updateLeaveStatus(lId, newStatus, hrId);
            if (!updated) {
                throw new RemoteException("Failed to update leave application status");
            }

            if (approved) {
                LocalDate start = leave.getStartDate();
                int year = start.getYear();
                double daysUsed = leave.getTotalDays();

                leaveDAO.updateLeaveBalance(
                        leave.getEmployeeId(),
                        year,
                        daysUsed,
                        leave.getLeaveType().name()   // ✅ FIXED HERE
                );
            }

            return leaveDAO.findLeaveById(lId);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid leave ID or HR staff ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while reviewing leave application", e);
        }
    }

    @Override
    public List<LeaveApplication> getPendingLeaves() throws RemoteException {
        try {
            return leaveDAO.findPendingLeaves();
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving pending leave applications", e);
        }
    }
}