package com.crest.hrm.server.security;

public class SSLConfig {

    public static void configure() {

        // Set keystore (server identity)
        System.setProperty(
            "javax.net.ssl.keyStore",
            "config/server.keystore"
        );

        System.setProperty(
            "javax.net.ssl.keyStorePassword",
            "changeit"
        );

        // Set truststore (trusted certificates)
        System.setProperty(
            "javax.net.ssl.trustStore",
            "config/client.truststore"
        );

        System.setProperty(
            "javax.net.ssl.trustStorePassword",
            "changeit"
        );

        System.out.println("SSL properties configured.");
    }
}