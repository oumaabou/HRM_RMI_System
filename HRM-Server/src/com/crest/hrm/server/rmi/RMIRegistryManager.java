package com.crest.hrm.server.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class RMIRegistryManager {

    private static final int PORT = 1099;

    public static Registry startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(
                    PORT,
                    new SslRMIClientSocketFactory(),
                    new SslRMIServerSocketFactory()
            );

            System.out.println("Secure SSL RMI Registry started on port " + PORT);
            return registry;

        } catch (Exception e) {
            System.out.println("Secure registry may already be running.");

            try {
                return LocateRegistry.getRegistry(
                        "localhost",
                        PORT,
                        new SslRMIClientSocketFactory()
                );
            } catch (Exception ex) {
                throw new RuntimeException("Failed to connect to secure RMI Registry", ex);
            }
        }
    }
}