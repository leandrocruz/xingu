package br.com.ibnetwork.xingu.network;

import java.util.List;

import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public interface Network
    extends Crashable
{
    ClientConnector clientConnector();
    
    ClientConnector clientConnector(String alias);
    
    ServerConnector serverConnector();
    
    ServerConnector serverConnector(String alias);
    
    List<ServerConnector> serverConnectors();
    
    void attach(Attachable device)
        throws Exception;

    void dettach(Attachable device)
        throws Exception;
}
