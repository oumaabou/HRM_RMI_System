package com.crest.hrm.server.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DirectHashTest {
    public static void main(String[] args) throws Exception {
        String password = "password123";
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        
        // Convert to hex
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        System.out.println("Direct hex: " + hex.toString());
        
        // Your actual stored hash
        String stored = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
        System.out.println("Stored:     " + stored);
        System.out.println("Match: " + hex.toString().equals(stored));
    }
}