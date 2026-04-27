package com.crest.hrm.server.faulttolerance;

public class HeartbeatMonitor extends Thread {

    public HeartbeatMonitor() {
        setDaemon(true); // dies automatically when main thread dies
        setName("HeartbeatMonitor");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("Heartbeat: Server alive");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restore interrupted status
                break;
            }
        }
    }
}