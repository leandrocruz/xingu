package br.com.ibnetwork.xingu.network.impl;

import java.io.IOException;

import br.com.ibnetwork.xingu.network.Client;
import br.com.ibnetwork.xingu.network.AsyncEvent;
import br.com.ibnetwork.xingu.network.Network;
import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.PipeHandler;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;

public abstract class ClientSupport
    implements Client, PipeHandler
{
    protected ClientConnector connector; 
    
    protected boolean printPipeEvents = false;
    
    public ClientSupport()
    {}

    public ClientSupport(ClientConnector connector)
    {
        this.connector = connector;
    }

    @Override
    public void attachTo(Network network) 
        throws Exception
    {
        if(connector == null)
        {
            connector = network.clientConnector();
        }
        connector.register(this);
        connector.connect();
    }
    
    @Override
    public ClientConnector connector()
    {
        return connector;
    }

    @Override
    public void dettachFrom(Network network) 
        throws Exception
    {
        connector.disconnect();
    }

    public AsyncEvent send(Object message)
        throws Exception
    {
        return connector.pipe().write(message);
    }
    
    @Override
    public Object sendAndWait(Object obj) 
        throws Exception
    {
        send(obj);
        return receive();
    }
    
    @Override
    public Object receive() 
        throws IOException
    {
        return null;
    }
    
    /*
     * PipeHandler methops 
     */

    @Override
    public void exceptionCaught(Pipe pipe, Throwable cause) 
        throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(Pipe pipe, Object message) 
        throws Exception
    {
        if(printPipeEvents) System.out.println("[CLIENT] messageReceived()");
    }

    @Override
    public void messageSent(Pipe pipe, Object message) 
        throws Exception
    {
        if(printPipeEvents) System.out.println("[CLIENT] messageSent()");
    }

    @Override
    public void pipeClosed(Pipe pipe) 
        throws Exception
    {
        if(printPipeEvents) System.out.println("[CLIENT] pipeClosed()");
    }

    @Override
    public void pipeCreated(Pipe pipe) 
        throws Exception
    {
        if(printPipeEvents) System.out.println("[CLIENT] pipeCreated()");
    }

    @Override
    public void pipeOpened(Pipe pipe) 
        throws Exception
    {
        if(printPipeEvents) System.out.println("[CLIENT] pipeOpened()");
    }
}
