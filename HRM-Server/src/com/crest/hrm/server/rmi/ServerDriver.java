package com.crest.hrm.server.rmi;

import com.crest.hrm.server.faulttolerance.HeartbeatMonitor;
import com.crest.hrm.server.impl.AuthServiceImpl;
import com.crest.hrm.server.impl.EmployeeServiceImpl;
import com.crest.hrm.server.impl.HRServiceImpl;
import com.crest.hrm.server.security.SSLConfig;
import com.crest.hrm.server.security.SSLContextFactory;
import java.net.InetAddress;
import java.rmi.registry.Registry;

public class ServerDriver {

    public static void main(String[] args) {

        System.out.println("Starting Secure HRM RMI Server...");

        try {
            // Set hostname before SSL config and registry start
            String hostname = System.getProperty("java.rmi.server.hostname");
            if (hostname == null || hostname.isEmpty()) {
                hostname = InetAddress.getLocalHost().getHostAddress();
                System.setProperty("java.rmi.server.hostname", hostname);
            }
            System.out.println("RMI server hostname set to: " + hostname);

            SSLConfig.configure();
            SSLContextFactory.configureSSL();

            Registry registry = RMIRegistryManager.startRegistry();

            registry.rebind("AuthService", new AuthServiceImpl());
            registry.rebind("EmployeeService", new EmployeeServiceImpl());
            registry.rebind("HRService", new HRServiceImpl());

            System.out.println("All secure services bound successfully.");
            System.out.println("Secure server started successfully.");

            // Start heartbeat monitor — runs as daemon so it won't block JVM shutdown
            new HeartbeatMonitor().start();

        } catch (Exception e) {
            System.err.println("Failed to start secure HRM RMI Server.");
            e.printStackTrace();
        }
    }
}