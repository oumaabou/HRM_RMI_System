package com.crest.hrm.client.rmi;

import javax.swing.JOptionPane;

public class ConnectionRetryHandler {
    
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;
    
    /**
     * Attempt to reconnect to the server with retry logic
     * @return true if connection successful, false otherwise
     */
    public static boolean retryConnection() {
        int attempts = 0;
        
        while (attempts < MAX_RETRIES) {
            attempts++;
            System.out.println("Connection attempt " + attempts + " of " + MAX_RETRIES);
            
            try {
                // Reset the registry so it tries to reconnect
                RMIConnectionManager.resetConnection();
                
                // Try to get registry (will retry internally)
                RMIConnectionManager.getRegistry();
                
                System.out.println("Reconnected successfully!");
                return true;
                
            } catch (Exception e) {
                System.out.println("Attempt " + attempts + " failed: " + e.getMessage());
                
                if (attempts < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Show dialog and attempt reconnection with user feedback
     * @param parentComponent The parent GUI component
     * @return true if reconnected, false if user cancels or fails
     */
    public static boolean promptAndRetry(java.awt.Component parentComponent) {
        int choice = JOptionPane.showConfirmDialog(
            parentComponent,
            "Connection to server lost. Would you like to retry?",
            "Connection Error",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE
        );
        
        if (choice != JOptionPane.YES_OPTION) {
            return false;
        }
        
        boolean connected = retryConnection();
        
        if (connected) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "Reconnected successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                parentComponent,
                "Failed to reconnect after " + MAX_RETRIES + " attempts.\nPlease check if server is running.",
                "Connection Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return connected;
    }
}