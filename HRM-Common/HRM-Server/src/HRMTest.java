package hrm.server;

public class HRMTest {

    public static void main(String[] args) {

        try {
            HRMServiceImpl service = new HRMServiceImpl();

            System.out.println("=== UNIT TESTING START ===");

            // Test 1: Register Employee
            String result1 = service.registerEmployee("TestUser");
            System.out.println("Test Register: " + result1);

            // Test 2: Apply Leave
            String result2 = service.applyLeave("TestUser", 3);
            System.out.println("Test Apply Leave: " + result2);

            // Test 3: Check Balance
            String result3 = service.checkLeaveBalance("TestUser");
            System.out.println("Test Balance: " + result3);

            // Test 4: Invalid Employee
            String result4 = service.applyLeave("Unknown", 2);
            System.out.println("Test Invalid Employee: " + result4);

            System.out.println("=== UNIT TESTING COMPLETE ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
