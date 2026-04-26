package com.crest.hrm.server.rmi;

import com.crest.hrm.server.impl.AuthServiceImpl;
import com.crest.hrm.server.impl.EmployeeServiceImpl;
import com.crest.hrm.server.impl.HRServiceImpl;
import com.crest.hrm.server.security.SSLConfig;
import com.crest.hrm.server.security.SSLContextFactory;
import java.rmi.registry.Registry;

public class ServerDriver {

    public static void main(String[] args) {

        System.out.println("Starting Secure HRM RMI Server...");

        try {
            SSLConfig.configure();
            SSLContextFactory.configureSSL();

            Registry registry = RMIRegistryManager.startRegistry();

            registry.rebind("AuthService", new AuthServiceImpl());
            registry.rebind("EmployeeService", new EmployeeServiceImpl());
            registry.rebind("HRService", new HRServiceImpl());

            System.out.println("All secure services bound successfully.");
            System.out.println("Secure server started successfully.");

        } catch (Exception e) {
            System.err.println("Failed to start secure HRM RMI Server.");
            e.printStackTrace();
        }
    }
}