package hrm.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HRMService extends Remote {

    String registerEmployee(String name) throws RemoteException;

    String applyLeave(String name, int days) throws RemoteException;

    String checkLeaveBalance(String name) throws RemoteException;

    String heartbeat() throws RemoteException;
}