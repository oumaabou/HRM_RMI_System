package hrm.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {

        try {
            System.out.println("Starting HRM Server...");

            Registry registry = LocateRegistry.createRegistry(1099);

            HRMServiceImpl service = new HRMServiceImpl();

            registry.rebind("HRMService", service);

            new HeartbeatMonitor().start();

            System.out.println("Server running...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}