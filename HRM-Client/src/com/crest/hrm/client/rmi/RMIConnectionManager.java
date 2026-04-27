package com.crest.hrm.client.rmi;

import com.crest.hrm.client.utils.SSLUtils;
import com.crest.hrm.client.utils.TrustAllSslClientSocketFactory;
import com.crest.hrm.common.interfaces.AuthService;
import com.crest.hrm.common.interfaces.EmployeeService;
import com.crest.hrm.common.interfaces.HRService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnectionManager {

    private static Registry registry;
    private static AuthService authService;
    private static HRService hrService;
    private static EmployeeService employeeService;

    private static final String SERVER_HOST = "10.214.238.151"; // change to server LAN IP
    private static final int SERVER_PORT = 1099;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    static {
        // Triggers SSLUtils static block — disables cert validation before any connection
        SSLUtils.disableHostnameVerification();
    }

    /**
     * Get RMI registry using trust-all SSL socket factory
     */
    public static Registry getRegistry() throws Exception {
        if (registry == null) {
            Exception lastException = null;

            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    System.out.println("Connecting to RMI registry (attempt " + attempt + " of " + MAX_RETRIES + ")");

                    // Use TrustAllSslClientSocketFactory instead of SslRMIClientSocketFactory
                    // This uses our trust-all SSLContext directly, bypassing JVM cert validation
                    registry = LocateRegistry.getRegistry(
                            SERVER_HOST,
                            SERVER_PORT,
                            new TrustAllSslClientSocketFactory()
                    );

                    registry.list();
                    System.out.println("Connected to RMI registry successfully");
                    return registry;

                } catch (Exception e) {
                    lastException = e;
                    registry = null;
                    System.out.println("Connection attempt " + attempt + " failed: " + e.getMessage());

                    if (attempt < MAX_RETRIES) {
                        try {
                            Thread.sleep(RETRY_DELAY_MS);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new Exception("Retry interrupted", ie);
                        }
                    }
                }
            }

            throw new Exception("Failed to connect to RMI registry after " + MAX_RETRIES + " attempts", lastException);
        }
        return registry;
    }

    /**
     * Get AuthService remote object
     */
    public static AuthService getAuthService() throws Exception {
        if (authService == null) {
            authService = (AuthService) getRegistry().lookup("AuthService");
            System.out.println("AuthService bound successfully");
        }
        return authService;
    }

    /**
     * Get HRService remote object
     */
    public static HRService getHRService() throws Exception {
        if (hrService == null) {
            hrService = (HRService) getRegistry().lookup("HRService");
            System.out.println("HRService bound successfully");
        }
        return hrService;
    }

    /**
     * Get EmployeeService remote object
     */
    public static EmployeeService getEmployeeService() throws Exception {
        if (employeeService == null) {
            employeeService = (EmployeeService) getRegistry().lookup("EmployeeService");
            System.out.println("EmployeeService bound successfully");
        }
        return employeeService;
    }

    /**
     * Reset all cached connections
     */
    public static void resetConnection() {
        registry = null;
        authService = null;
        hrService = null;
        employeeService = null;
        System.out.println("Connection cache cleared. Will reconnect on next request.");
    }

    /**
     * Check if server is currently reachable
     */
    public static boolean isServerReachable() {
        try {
            Registry testRegistry = LocateRegistry.getRegistry(
                    SERVER_HOST,
                    SERVER_PORT,
                    new TrustAllSslClientSocketFactory()
            );
            testRegistry.list();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}