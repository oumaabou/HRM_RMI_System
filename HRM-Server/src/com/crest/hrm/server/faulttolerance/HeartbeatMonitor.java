package com.crest.hrm.server.faulttolerance;

public class HeartbeatMonitor extends Thread {

    public void run() {
        while (true) {
            try {
                System.out.println("Heartbeat: Server alive");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}