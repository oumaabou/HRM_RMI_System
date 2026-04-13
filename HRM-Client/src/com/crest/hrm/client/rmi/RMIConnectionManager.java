package com.crest.hrm.client.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnectionManager {

    private static Registry registry;

    public static Registry getRegistry() throws Exception {
        if (registry == null) {
            registry = LocateRegistry.getRegistry("localhost", 1099);
        }
        return registry;
    }
}
