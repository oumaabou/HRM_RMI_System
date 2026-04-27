package com.crest.hrm.client.utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TrustAllSslClientSocketFactory implements RMIClientSocketFactory, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        // Use the trust-all SSLContext directly — bypasses JVM default certificate validation
        SSLSocketFactory factory = SSLUtils.getTrustAllContext().getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        return socket;
    }
}