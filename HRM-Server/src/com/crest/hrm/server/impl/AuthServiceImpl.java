package com.crest.hrm.server.impl;

import com.crest.hrm.common.exceptions.AuthenticationException;
import com.crest.hrm.common.interfaces.AuthService;
import com.crest.hrm.common.models.Employee;
import com.crest.hrm.server.dao.EmployeeDAO;
import com.crest.hrm.server.dao.impl.EmployeeDAOImpl;
import com.crest.hrm.server.security.PasswordEncryption;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {

    private final EmployeeDAO employeeDAO;
    private static final Map<String, Integer> activeSessions = new HashMap<>();

    public AuthServiceImpl() throws RemoteException {
        super(
    0,
    new javax.rmi.ssl.SslRMIClientSocketFactory(),
    new javax.rmi.ssl.SslRMIServerSocketFactory()
);
        this.employeeDAO = new EmployeeDAOImpl();
    }

    @Override
    public Employee login(String username, String password)
            throws AuthenticationException, RemoteException {

        try {
            Employee employee = employeeDAO.findByUsername(username);

            if (employee == null) {
                throw new AuthenticationException("Invalid username or password");
            }

            // Verify password using SHA-256 hashing
            if (employee.getPasswordHash() == null || 
                !PasswordEncryption.verifyPassword(password, employee.getPasswordHash())) {
                throw new AuthenticationException("Invalid username or password");
            }

            String token = UUID.randomUUID().toString();
            activeSessions.put(token, employee.getEmployeeId());

            return employee;

        } catch (SQLException e) {
            throw new RemoteException("Database error during login", e);
        }
    }

    @Override
    public void logout(String employeeId) throws RemoteException {
        try {
            int id = Integer.parseInt(employeeId);
            activeSessions.entrySet().removeIf(entry -> entry.getValue().equals(id));
        } catch (NumberFormatException e) {
            throw new RemoteException("Invalid employee ID format", e);
        }
    }

    @Override
    public boolean isSessionValid(String sessionToken) throws RemoteException {
        return activeSessions.containsKey(sessionToken);
    }
}