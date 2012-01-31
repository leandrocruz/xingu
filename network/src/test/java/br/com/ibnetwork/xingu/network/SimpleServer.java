package br.com.ibnetwork.xingu.network;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.network.connector.ServerConnector;
import br.com.ibnetwork.xingu.network.impl.ServerSupport;

public class SimpleServer
    extends ServerSupport
{
    private ServerConnector connector;
    
    public SimpleServer(ServerConnector connector)
    {
        this.connector = connector;
    }

    @Override
    protected List<ServerConnector> serverConnectors()
    {
        List<ServerConnector> connectors = new ArrayList<ServerConnector>(); 
        connectors.add(connector);
        return connectors;
    }
}
