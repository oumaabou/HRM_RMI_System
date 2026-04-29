package com.crest.hrm.server.faulttolerance;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.function.Supplier;

/**
 * ReconnectionManager - Handles reconnection attempts when RMI connection fails
 * 
 * This class provides utility methods to retry RMI calls when the connection
 * to the server is lost. It uses exponential backoff to avoid overwhelming
 * the server during recovery.
 */
public class ReconnectionManager {
    
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 1000;
    private static final long MAX_DELAY_MS = 5000;
    
    /**
     * Execute an RMI call with automatic retry on connection failure
     * 
     * @param <T> The return type of the RMI call
     * @param call The RMI call to execute
     * @return The result of the RMI call
     * @throws RemoteException if all retries fail
     */
    public static <T> T executeWithRetry(Supplier<T> call) throws RemoteException {
        Exception lastException = null;
        long delay = INITIAL_DELAY_MS;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                System.out.println("RMI attempt " + attempt + " of " + MAX_RETRIES);
                return call.get();
                
            } catch (Exception e) {
                lastException = e;
                
                if (isConnectionFailure(e)) {
                    System.out.println("Connection failed. Retrying in " + delay + "ms...");
                    
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RemoteException("Retry interrupted", ie);
                    }
                    
                    // Exponential backoff: double the delay each time, up to MAX_DELAY
                    delay = Math.min(delay * 2, MAX_DELAY_MS);
                } else {
                    // Non-connection failure - don't retry
                    throw new RemoteException("RMI call failed", e);
                }
            }
        }
        
        throw new RemoteException("Failed after " + MAX_RETRIES + " attempts", lastException);
    }
    
    /**
     * Check if the exception is a connection failure (recoverable)
     */
    private static boolean isConnectionFailure(Exception e) {
        if (e instanceof RemoteException) {
            Throwable cause = e.getCause();
            return cause instanceof ConnectException || 
                   cause instanceof java.rmi.ConnectIOException ||
                   e.getMessage().contains("Connection refused") ||
                   e.getMessage().contains("Connection timed out");
        }
        return false;
    }
    
    /**
     * Check if server is reachable by attempting a simple connection
     * 
     * @param serverHost The server hostname or IP
     * @param serverPort The RMI registry port
     * @return true if server is reachable, false otherwise
     */
    public static boolean isServerReachable(String serverHost, int serverPort) {
        try {
            java.net.Socket socket = new java.net.Socket(serverHost, serverPort);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for server to become available
     * 
     * @param serverHost The server hostname or IP
     * @param serverPort The RMI registry port
     * @param maxWaitMs Maximum time to wait in milliseconds
     * @return true if server became available, false if timeout
     */
    public static boolean waitForServer(String serverHost, int serverPort, long maxWaitMs) {
        long startTime = System.currentTimeMillis();
        long retryDelay = 2000; // 2 seconds between checks
        
        while (System.currentTimeMillis() - startTime < maxWaitMs) {
            if (isServerReachable(serverHost, serverPort)) {
                System.out.println("Server is now reachable!");
                return true;
            }
            
            try {
                System.out.println("Waiting for server to become available...");
                Thread.sleep(retryDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        System.out.println("Server did not become available within " + maxWaitMs + "ms");
        return false;
    }
}