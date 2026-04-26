package com.crest.hrm.server.security;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

public class SslRMIClientSocketFactory implements RMIClientSocketFactory, Serializable {

    private static final long serialVersionUID = 1L;

    private final javax.rmi.ssl.SslRMIClientSocketFactory delegate =
            new javax.rmi.ssl.SslRMIClientSocketFactory();

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return delegate.createSocket(host, port);
    }
}