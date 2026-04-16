package com.crest.hrm.server.rmi;

import com.crest.hrm.server.impl.AuthServiceImpl;
import com.crest.hrm.server.impl.EmployeeServiceImpl;
import com.crest.hrm.server.impl.HRServiceImpl;
import java.rmi.registry.Registry;

public class ServerDriver {

    public static void main(String[] args) {

        System.out.println("Starting HRM RMI Server...");

        try {
            Registry registry = RMIRegistryManager.startRegistry();

            registry.rebind("AuthService", new AuthServiceImpl());
            registry.rebind("EmployeeService", new EmployeeServiceImpl());
            registry.rebind("HRService", new HRServiceImpl());

            System.out.println("All services bound successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Server started successfully.");
    }
}