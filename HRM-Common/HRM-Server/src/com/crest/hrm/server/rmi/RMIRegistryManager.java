package com.crest.hrm.server.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIRegistryManager {

    private static final int PORT = 1099;

    public static Registry startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("RMI Registry started on port " + PORT);
            return registry;
        } catch (Exception e) {
            System.out.println("Registry already running.");
            try {
                return LocateRegistry.getRegistry(PORT);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to connect to RMI Registry", ex);
            }
        }
    }
}
