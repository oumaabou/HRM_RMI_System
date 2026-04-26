package com.crest.hrm.server.security;

import java.io.File;

public class SSLContextFactory {

    private static final String KEYSTORE_PATH = "config/server.keystore";
    private static final String TRUSTSTORE_PATH = "config/client.truststore";
    private static final String PASSWORD = "changeit";

    public static void configureSSL() {
        File keyStore = new File(KEYSTORE_PATH);
        File trustStore = new File(TRUSTSTORE_PATH);

        if (!keyStore.exists()) {
            System.out.println("WARNING: server.keystore not found at " + keyStore.getAbsolutePath());
        }

        if (!trustStore.exists()) {
            System.out.println("WARNING: client.truststore not found at " + trustStore.getAbsolutePath());
        }

        System.setProperty("javax.net.ssl.keyStore", keyStore.getPath());
        System.setProperty("javax.net.ssl.keyStorePassword", PASSWORD);

        System.setProperty("javax.net.ssl.trustStore", trustStore.getPath());
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);

        System.out.println("SSL context configured successfully.");
    }
}