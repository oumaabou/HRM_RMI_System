package com.crest.hrm.server.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class RMIRegistryManager {

    private static final int PORT = 1099;

    // Cipher suites and protocols can be restricted here if needed, null = use JVM defaults
    private static final String[] ENABLED_CIPHER_SUITES = null;
    private static final String[] ENABLED_PROTOCOLS = null;
    // true = require client to also present a certificate (mutual TLS)
    // false = only server authenticates (more typical for client-server apps)
    private static final boolean NEED_CLIENT_AUTH = false;

    public static Registry startRegistry() {
        try {
            // SslRMIServerSocketFactory reads javax.net.ssl.keyStore system properties
            // set by SSLConfig/SSLContextFactory — so SSL config MUST run before this
            SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(
                    ENABLED_CIPHER_SUITES,
                    ENABLED_PROTOCOLS,
                    NEED_CLIENT_AUTH
            );

            Registry registry = LocateRegistry.createRegistry(PORT, csf, ssf);
            System.out.println("Secure SSL RMI Registry started on port " + PORT);
            return registry;

        } catch (Exception e) {
            System.out.println("Registry may already be running, attempting to connect: " + e.getMessage());

            try {
                // Fallback: connect to existing registry with SSL client factory
                Registry registry = LocateRegistry.getRegistry(
                        "localhost",
                        PORT,
                        new SslRMIClientSocketFactory()
                );
                // Verify it is actually reachable and SSL-compatible
                registry.list();
                System.out.println("Connected to existing SSL RMI Registry on port " + PORT);
                return registry;

            } catch (Exception ex) {
                throw new RuntimeException("Failed to start or connect to secure RMI Registry on port " + PORT, ex);
            }
        }
    }
}