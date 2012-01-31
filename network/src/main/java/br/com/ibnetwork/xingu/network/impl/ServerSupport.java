package br.com.ibnetwork.xingu.network.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.network.Network;
import br.com.ibnetwork.xingu.network.Server;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public class ServerSupport 
	implements Server
{
    @Inject
    private Network network;
    
	private boolean isRunning;
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public void start()
    	throws Exception
	{
	    isRunning = true;
	    List<ServerConnector> connectors = serverConnectors(); 
	    if(connectors != null)
	    {
	        for (ServerConnector connector : connectors)
	        {
	            connector.bind();
	            log.info("Binding connector: {}", connector);
	        }
	    }
    }

	public void stop()
    	throws Exception
    {
	    isRunning = false;
        List<ServerConnector> connectors = serverConnectors(); 
        if(connectors != null)
        {
            for (ServerConnector connector : connectors)
            {
                connector.unbind();
                log.info("Unbinding connector: {}", connector);
            }
        }
    }

	@Override
    public void attachTo(Network network) 
        throws Exception
    {
	    if(!isRunning)
	    {
	        start();
	    }
    }

	@Override
    public void dettachFrom(Network network) 
	    throws Exception
    {
        if(isRunning)
        {
            stop();
        }
    }

    protected List<ServerConnector> serverConnectors()
    {
        return network.serverConnectors();
    }

    public boolean isRunning()
    {
	    return isRunning;
    }
}