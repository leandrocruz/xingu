package br.com.ibnetwork.xingu.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public abstract class NetworkTestSupport
    extends XinguTestCase
{
    @Inject
    protected Network network;
    
    protected Server server;
    
    protected Client client;
    
    @Override
    protected boolean resetContainer()
    {
        return true;
    }
    
    @Before
    public void attachDevices()
        throws Exception
    {
        ServerConnector connector = network.serverConnector();
        PipeHandler handler = new Reverser();
        connector.register(handler);
        server = new SimpleServer(connector);
        network.attach(server);

        client = new EchoClient();
        network.attach(client);
    }

    @After
    public void dettach()
        throws Exception
    {
        network.dettach(server);
        network.dettach(client);
    }
    
    @Test
    public void testNetwork()
        throws Exception
    {
        //sends message to server
        AsyncEvent event = client.send("hello");
        event.waitWithoutInterruptions();
        assertTrue(event.isDone());
        
        //reads message from server
        Object obj = client.receive();
        
        assertEquals("olleh", obj);
    }

    @Test(expected=IOException.class)
    public void testCrashOnPut()
        throws Exception
    {
        network.crash();
        AsyncEvent event = client.send("message");
        event.waitWithoutInterruptions();
        assertTrue(event.isDone());
    }

    @Test(expected=IOException.class)
    public void testCrashOnGet()
        throws Exception
    {
        AsyncEvent event = client.send("message");
        event.waitWithoutInterruptions();
        assertTrue(event.isDone());
        network.crash();
        client.receive();
    }

    @Test
    public void testCrashAndResume()
        throws Exception
    {
        network.crash();
        try
        {
            client.send("_message");
        } 
        catch(IOException e)
        {
            //ignored
        }
        network.resume();
        Object obj = client.sendAndWait("_message");
        assertEquals("egassem_", obj);
    }
}
