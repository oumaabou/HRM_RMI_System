package com.crest.hrm.server.security;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

public class SslRMIClientSocketFactory implements RMIClientSocketFactory, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        // Create fresh instance every time instead of reusing a cached delegate.
        // This ensures SSL properties set via VM options are always picked up.
        return new javax.rmi.ssl.SslRMIClientSocketFactory().createSocket(host, port);
    }
}