package hrm.server;

import hrm.common.HRMService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class HRMServiceImpl extends UnicastRemoteObject implements HRMService {

    private HashMap<String, Integer> employees = new HashMap<>();

    public HRMServiceImpl() throws RemoteException {
        super();
    }

    public String registerEmployee(String name) throws RemoteException {
        employees.put(name, 14);
        TransactionLogger.log("Registered employee: " + name);
        return "Employee registered: " + name;
    }

    public String applyLeave(String name, int days) throws RemoteException {

        if (!employees.containsKey(name)) {
            return "Employee not found";
        }

        int balance = employees.get(name);

        if (balance < days) {
            return "Not enough leave balance";
        }

        employees.put(name, balance - days);

        TransactionLogger.log(name + " applied leave: " + days + " days");

        return "Leave applied successfully";
    }

    public String checkLeaveBalance(String name) throws RemoteException {

        if (!employees.containsKey(name)) {
            return "Employee not found";
        }

        return "Leave balance: " + employees.get(name);
    }

    public String heartbeat() throws RemoteException {
        return "Server alive";
    }
}