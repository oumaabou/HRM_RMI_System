package com.crest.hrm.server.rmi;

import com.crest.hrm.server.rmi.RMIRegistryManager;

public class ServerDriver {

    public static void main(String[] args) {

        System.out.println("Starting HRM RMI Server...");

        RMIRegistryManager.startRegistry();

        System.out.println("Server started successfully.");

    }
}   