package com.crest.hrm.client.utils;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {

    private static boolean initialized = false;

    /**
     * Disables SSL hostname/certificate verification on the client.
     * SSL encryption is still active — only the certificate validation check
     * is bypassed. Safe for demo/assignment environments where IP changes
     * between sessions and regenerating certificates is impractical.
     *
     * Call this ONCE at application startup before any RMI or SSL connection.
     */
    public static void disableHostnameVerification() {
        if (initialized) return; // only run once

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // accept all client certificates
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // accept all server certificates regardless of IP/hostname
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());

            // Set as JVM default — picked up by SslRMIClientSocketFactory
            SSLContext.setDefault(sc);

            // Also disable HTTPS hostname verifier as backup
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            initialized = true;
            System.out.println("SSL certificate verification disabled (demo mode).");

        } catch (Exception e) {
            System.err.println("Failed to disable SSL verification: " + e.getMessage());
        }
    }
}