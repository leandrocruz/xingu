package br.com.ibnetwork.xingu.network.impl.local;

import java.util.List;

import org.apache.avalon.framework.activity.Initializable;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public class LocalNetwork
    extends NetworkSupport
    implements Initializable
{
    private Buffer buffer;

    private ClientConnector clientConnector;
    
    private ServerConnector serverConnector;

    
    @Override
    public void initialize() 
        throws Exception
    {
        buffer = new Buffer();
        clientConnector = new LocalClientConnector(buffer);
        serverConnector = new LocalServerConnector(buffer);
    }

    @Override
    public void crash()
    {
        buffer.crash();
    }

    @Override
    public void resume()
    {
        buffer.resume();
    }

    @Override
    public boolean isCrashed()
    {
        return buffer.isCrashed();
    }

    @Override
    public ClientConnector clientConnector()
    {
        return clientConnector; 
    }

    @Override
    public ClientConnector clientConnector(String alias)
    {
        return clientConnector;
    }

    @Override
    public ServerConnector serverConnector()
    {
        return serverConnector; 
    }

    @Override
    public ServerConnector serverConnector(String alias)
    {
        return serverConnector;
    }

    @Override
    public List<ServerConnector> serverConnectors()
    {
        throw new NotImplementedYet();
    }
}
