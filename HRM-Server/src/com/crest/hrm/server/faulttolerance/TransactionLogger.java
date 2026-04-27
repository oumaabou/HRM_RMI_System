package com.crest.hrm.server.faulttolerance;

import java.io.FileWriter;
import java.time.LocalDateTime;

public class TransactionLogger {

    public static void log(String message) {
        try (FileWriter writer = new FileWriter("transactions.log", true)) {
            writer.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (Exception e) {
            System.out.println("Log error: " + e.getMessage());
        }
    }
}