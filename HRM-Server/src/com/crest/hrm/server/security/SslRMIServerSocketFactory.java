package com.crest.hrm.server.security;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

public class SslRMIServerSocketFactory implements RMIServerSocketFactory, Serializable {

    private static final long serialVersionUID = 1L;

    private final javax.rmi.ssl.SslRMIServerSocketFactory delegate =
            new javax.rmi.ssl.SslRMIServerSocketFactory();

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return delegate.createServerSocket(port);
    }
}