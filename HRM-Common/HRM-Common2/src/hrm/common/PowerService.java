package hrm.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PowerService extends Remote {

    String turnOnLight() throws RemoteException;

    String turnOffFan() throws RemoteException;

    String heartbeat() throws RemoteException;

}