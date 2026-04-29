package com.crest.hrm.client.utils;

public class SSLUtils {

    /**
     * Sets the truststore system properties so the JVM can validate
     * the server's SSL certificate during RMI connections.
     *
     * Call this once at application startup before any RMI or SSL connection.
     */
    public static void disableHostnameVerification() {
        System.setProperty("javax.net.ssl.trustStore", "config/client.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.out.println("SSL truststore configured.");
    }
}