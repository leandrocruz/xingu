package br.com.ibnetwork.xingu.network;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.impl.ClientSupport;

public class MinaNetworkTest
    extends NetworkTestSupport
{
    @Override
    protected String getContainerFile()
    {
        return "pulga-network-mina.xml";
    }

    @Test(expected=Exception.class)
    @Ignore
    public void testDisconnectDetachedClient()
        throws Exception
    {
        ClientConnector connector = network.clientConnector();
        Client c = new MyClient(connector);
        c.dettachFrom(network);
    }
}

class MyClient
    extends ClientSupport
{
    public MyClient(ClientConnector c)
    {
        super(c);
    }
}