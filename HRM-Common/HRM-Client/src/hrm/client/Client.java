package hrm.client;

import hrm.common.HRMService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            HRMService service = (HRMService) registry.lookup("HRMService");

            Scanner sc = new Scanner(System.in);

            while (true) {

                System.out.println("\n1. Register Employee");
                System.out.println("2. Apply Leave");
                System.out.println("3. Check Balance");
                System.out.println("4. Heartbeat");

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.println(service.registerEmployee(name));
                }

                else if (choice == 2) {
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Days: ");
                    int days = sc.nextInt();
                    System.out.println(service.applyLeave(name, days));
                }

                else if (choice == 3) {
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.println(service.checkLeaveBalance(name));
                }

                else if (choice == 4) {
                    System.out.println(service.heartbeat());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}