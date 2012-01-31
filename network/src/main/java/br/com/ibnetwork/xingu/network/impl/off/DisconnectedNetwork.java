package br.com.ibnetwork.xingu.network.impl.off;

import java.util.List;

import br.com.ibnetwork.xingu.network.Attachable;
import br.com.ibnetwork.xingu.network.Network;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public class DisconnectedNetwork
    implements Network
{
    private boolean isCrashed = false;

    @Override
    public void attach(Attachable device) 
        throws Exception
    {
        
    }

    @Override
    public ClientConnector clientConnector()
    {
        return null;
    }

    @Override
    public ClientConnector clientConnector(String alias)
    {
        return null;
    }

    @Override
    public void dettach(Attachable device) 
        throws Exception
    {
    }

    @Override
    public ServerConnector serverConnector()
    {
        return null;
    }

    @Override
    public ServerConnector serverConnector(String alias)
    {
        return null;
    }

    @Override
    public List<ServerConnector> serverConnectors()
    {
        return null;
    }

    @Override
    public void crash()
    {
        isCrashed = true;
    }

    @Override
    public boolean isCrashed()
    {
        return isCrashed;
    }

    @Override
    public void resume() 
        throws Exception
    {
        isCrashed = false;
    }

}
