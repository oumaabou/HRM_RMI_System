package com.crest.hrm.client.rmi;

public class ConnectionRetryHandler {

    public static boolean retryConnection() {
        try {
            RMIConnectionManager.getRegistry();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}