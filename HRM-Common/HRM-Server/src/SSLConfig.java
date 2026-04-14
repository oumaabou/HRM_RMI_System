package hrm.server;

public class SSLConfig {

    public static void configure() {

        // Disable strict SSL verification for testing
        System.setProperty("javax.net.ssl.keyStore", "");
        System.setProperty("javax.net.ssl.keyStorePassword", "");
        System.setProperty("javax.net.ssl.trustStore", "");
        System.setProperty("javax.net.ssl.trustStorePassword", "");

    }
}