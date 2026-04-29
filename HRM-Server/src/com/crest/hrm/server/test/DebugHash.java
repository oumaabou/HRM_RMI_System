package com.crest.hrm.server.test;

import com.crest.hrm.server.security.PasswordEncryption;

public class DebugHash {
    public static void main(String[] args) {
        String password = "password123";
        String expectedHash = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";
        
        String actualHash = PasswordEncryption.hashPassword(password);
        
        System.out.println("Expected: " + expectedHash);
        System.out.println("Actual:   " + actualHash);
        System.out.println("Match: " + expectedHash.equals(actualHash));
        
        // Also test verify
        boolean verify = PasswordEncryption.verifyPassword(password, expectedHash);
        System.out.println("Verify result: " + verify);
    }
}