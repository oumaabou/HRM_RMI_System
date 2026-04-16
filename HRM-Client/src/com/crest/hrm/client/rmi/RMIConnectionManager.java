package com.crest.hrm.client.rmi;

import com.crest.hrm.common.interfaces.EmployeeService;
import com.crest.hrm.common.interfaces.HRService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnectionManager {

    private static Registry registry;
    private static HRService hrService;
    private static EmployeeService employeeService;

    public static Registry getRegistry() throws Exception {
        if (registry == null) {
            registry = LocateRegistry.getRegistry("localhost", 1099);
        }
        return registry;
    }

    public static HRService getHRService() throws Exception {
        if (hrService == null) {
            hrService = (HRService) getRegistry().lookup("HRService");
        }
        return hrService;
    }

    public static EmployeeService getEmployeeService() throws Exception {
        if (employeeService == null) {
            employeeService = (EmployeeService) getRegistry().lookup("EmployeeService");
        }
        return employeeService;
    }
}