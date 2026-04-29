package com.crest.hrm.server.impl;

import com.crest.hrm.common.exceptions.EmployeeNotFoundException;
import com.crest.hrm.common.exceptions.InvalidLeaveRequestException;
import com.crest.hrm.common.interfaces.EmployeeService;
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.common.models.FamilyDetails;
import com.crest.hrm.common.models.LeaveApplication;
import com.crest.hrm.common.models.LeaveBalance;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.FamilyDAO;
import com.crest.hrm.server.dao.LeaveDAO;
import com.crest.hrm.server.dao.impl.EmployeeDAOImpl;
import com.crest.hrm.server.dao.impl.FamilyDAOImpl;
import com.crest.hrm.server.dao.impl.LeaveDAOImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmployeeServiceImpl extends UnicastRemoteObject implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final FamilyDAO familyDAO;
    private final LeaveDAO leaveDAO;

    public EmployeeServiceImpl() throws RemoteException {
        super(
    0,
    new javax.rmi.ssl.SslRMIClientSocketFactory(),
    new javax.rmi.ssl.SslRMIServerSocketFactory()
);
        this.employeeDAO = new EmployeeDAOImpl();
        this.familyDAO = new FamilyDAOImpl();
        this.leaveDAO = new LeaveDAOImpl();
    }

    @Override
    public Employee updatePersonalDetails(Employee employee)
            throws EmployeeNotFoundException, RemoteException {
        try {
            if (employee == null) {
                throw new EmployeeNotFoundException(null);
            }

            Integer employeeId = employee.getEmployeeId();
            if (employeeId == null) {
                throw new EmployeeNotFoundException(null);
            }

            Employee existing = employeeDAO.findById(employeeId);
            if (existing == null) {
                throw new EmployeeNotFoundException(employeeId);
            }

            boolean updated = employeeDAO.update(employee);
            if (!updated) {
                throw new RemoteException("Failed to update employee details");
            }

            return employeeDAO.findById(employeeId);

        } catch (SQLException e) {
            throw new RemoteException("Database error while updating personal details", e);
        }
    }

    @Override
    public FamilyDetails updateFamilyDetails(FamilyDetails familyDetails)
            throws EmployeeNotFoundException, RemoteException {
        try {
            if (familyDetails == null) {
                throw new EmployeeNotFoundException(null);
            }

            Integer employeeId = familyDetails.getEmployeeId();
            if (employeeId == null) {
                throw new EmployeeNotFoundException(null);
            }

            Employee existing = employeeDAO.findById(employeeId);
            if (existing == null) {
                throw new EmployeeNotFoundException(employeeId);
            }

            if (familyDetails.getFamilyId() == null) {
                int generatedId = familyDAO.saveFamilyMember(familyDetails);
                familyDetails.setFamilyId(generatedId);
            } else {
                boolean updated = familyDAO.updateFamilyMember(familyDetails);
                if (!updated) {
                    throw new RemoteException("Failed to update family details");
                }
            }

            return familyDetails;

        } catch (SQLException e) {
            throw new RemoteException("Database error while updating family details", e);
        }
    }

    @Override
    public FamilyDetails getFamilyDetails(String employeeId)
            throws EmployeeNotFoundException, RemoteException {
        try {
            int id = Integer.parseInt(employeeId);

            Employee existing = employeeDAO.findById(id);
            if (existing == null) {
                throw new EmployeeNotFoundException(id);
            }

            List<FamilyDetails> familyMembers = familyDAO.findByEmployee(id);
            if (familyMembers == null || familyMembers.isEmpty()) {
                return null;
            }

            return familyMembers.get(0);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving family details", e);
        }
    }

    @Override
    public LeaveBalance checkLeaveBalance(String employeeId, int year)
            throws EmployeeNotFoundException, RemoteException {
        try {
            int id = Integer.parseInt(employeeId);

            Employee existing = employeeDAO.findById(id);
            if (existing == null) {
                throw new EmployeeNotFoundException(id);
            }

            return leaveDAO.getLeaveBalance(id, year);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while checking leave balance", e);
        }
    }

    @Override
    public LeaveApplication applyLeave(LeaveApplication application)
            throws EmployeeNotFoundException, InvalidLeaveRequestException, RemoteException {
        try {
            if (application == null) {
                throw new InvalidLeaveRequestException("Leave application is null");
            }

            Integer employeeId = application.getEmployeeId();
            if (employeeId == null) {
                throw new EmployeeNotFoundException(null);
            }

            Employee existing = employeeDAO.findById(employeeId);
            if (existing == null) {
                throw new EmployeeNotFoundException(employeeId);
            }

            if (application.getStartDate() == null || application.getEndDate() == null) {
                throw new InvalidLeaveRequestException("Leave dates are required");
            }

            if (application.getStartDate().isAfter(application.getEndDate())) {
                throw new InvalidLeaveRequestException("Start date cannot be after end date");
            }

            if (application.getLeaveType() == null) {
                throw new InvalidLeaveRequestException("Leave type is required");
            }

            int year = application.getStartDate().getYear();
            double daysRequested = ChronoUnit.DAYS.between(
                    application.getStartDate(),
                    application.getEndDate()
            ) + 1;

            boolean sufficient = leaveDAO.hasSufficientBalance(
                    employeeId,
                    year,
                    daysRequested,
                    application.getLeaveType().name()
            );

            if (!sufficient) {
                throw new InvalidLeaveRequestException("Insufficient leave balance");
            }

            int leaveId = leaveDAO.saveLeave(application);
            application.setLeaveId(leaveId);

            return application;

        } catch (SQLException e) {
            throw new RemoteException("Database error while applying leave", e);
        }
    }

    @Override
    public LeaveApplication cancelLeave(String leaveId, String employeeId)
            throws RemoteException {
        try {
            int lId = Integer.parseInt(leaveId);
            int eId = Integer.parseInt(employeeId);

            LeaveApplication leave = leaveDAO.findLeaveById(lId);
            if (leave == null) {
                return null;
            }

            if (!leave.getEmployeeId().equals(eId)) {
                throw new RemoteException("Employee is not allowed to cancel this leave");
            }

            boolean cancelled = leaveDAO.cancelLeaveApplication(lId);
            if (!cancelled) {
                throw new RemoteException("Failed to cancel leave application");
            }

            return leaveDAO.findLeaveById(lId);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid leave ID or employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while cancelling leave", e);
        }
    }

    @Override
    public LeaveApplication viewLeaveStatus(String leaveId) throws RemoteException {
        try {
            int id = Integer.parseInt(leaveId);
            return leaveDAO.findLeaveById(id);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid leave ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while viewing leave status", e);
        }
    }

    @Override
    public List<LeaveApplication> viewLeaveHistory(String employeeId)
            throws EmployeeNotFoundException, RemoteException {
        try {
            int id = Integer.parseInt(employeeId);

            Employee existing = employeeDAO.findById(id);
            if (existing == null) {
                throw new EmployeeNotFoundException(id);
            }

            return leaveDAO.findLeavesByEmployee(id);

        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        } catch (SQLException e) {
            throw new RemoteException("Database error while viewing leave history", e);
        }
    }
}