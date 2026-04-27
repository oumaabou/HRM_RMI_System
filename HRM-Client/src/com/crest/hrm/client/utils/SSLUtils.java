package com.crest.hrm.client.utils;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {

    private static SSLContext trustAllContext;

    static {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            trustAllContext = SSLContext.getInstance("TLS");
            trustAllContext.init(null, trustAllCerts, new SecureRandom());

            SSLContext.setDefault(trustAllContext);
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            System.out.println("SSL certificate verification disabled (demo mode).");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create trust-all SSL context", e);
        }
    }

    /**
     * Returns the trust-all SSLContext.
     * Use this to create socket factories directly instead of relying on JVM default.
     */
    public static SSLContext getTrustAllContext() {
        return trustAllContext;
    }

    // Keep this method for backwards compatibility — calling it triggers the static block
    public static void disableHostnameVerification() {
        // static block handles everything on class load
    }
}