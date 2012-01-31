package br.com.ibnetwork.xingu.network.connector;

import java.net.SocketAddress;

import br.com.ibnetwork.xingu.network.Pipe;

public interface ClientConnector
    extends Connector
{
    SocketAddress address();
    
    void connect() 
        throws Exception;

    void connect(long timeout) 
        throws Exception;

    void reconnect() 
        throws Exception;

    void disconnect();

    boolean isConnected();
    
    Pipe pipe();
}
