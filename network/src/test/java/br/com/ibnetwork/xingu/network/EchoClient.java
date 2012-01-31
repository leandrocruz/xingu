package br.com.ibnetwork.xingu.network;

import java.io.IOException;

import br.com.ibnetwork.xingu.lang.Sys;
import br.com.ibnetwork.xingu.network.impl.ClientSupport;

public class EchoClient
    extends ClientSupport
{
    private Object message;
    
    @Override
    public void messageReceived(Pipe pipe, Object message) 
        throws Exception
    {
        System.out.println("echoClient> "+message);
        this.message = message;
    }

    @Override
    public Object receive()
        throws IOException
    {
        if(!connector.isConnected())
        {
            throw new IOException("Client disconnected. Can't wait from new messages");
        }
        while(message == null)
        {
            Sys.sleepWithoutInterruptions(500);
        }
        Object result = message;
        message = null;
        return result;
    }
}
