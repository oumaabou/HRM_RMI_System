/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crest.hrm.common.interfaces;

/**
 *
 * @author shuhaab
 */
import com.crest.hrm.common.exceptions.AuthenticationException;
import com.crest.hrm.common.models.Employee;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * RMI Interface for authentication operations.
 * Both HR Staff and Employees use this to log in and out.
 */
public interface AuthService extends Remote {
    /**
     * Authenticates a user with the given credentials.
     *
     * @param username the user's username
     * @param password the user's plain-text password (will be hashed server-side)
     * @return the authenticated Employee object
     * @throws AuthenticationException if credentials are invalid
     * @throws RemoteException         if a network/RMI error occurs
     */
    Employee login(String username, String password)
            throws AuthenticationException, RemoteException;
    /**
     * Logs out the current session for the given employee.
     *
     * @param employeeId the ID of the employee to log out
     * @throws RemoteException if a network/RMI error occurs
     */
    void logout(String employeeId) throws RemoteException;
    /**
     * Checks whether a given session token is still valid.
     *
     * @param sessionToken the token issued at login
     * @return true if valid, false otherwise
     * @throws RemoteException if a network/RMI error occurs
     */
    boolean isSessionValid(String sessionToken) throws RemoteException;
}
