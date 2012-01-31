package br.com.ibnetwork.xingu.network.impl.local;

import java.io.IOException;
import java.net.SocketAddress;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.lang.Sys;
import br.com.ibnetwork.xingu.network.AsyncEvent;
import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.PipeHandler;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.impl.InstantaneousAsyncEvent;
import br.com.ibnetwork.xingu.network.impl.NullPipe;

public class LocalClientConnector
    implements ClientConnector, Runnable
{
    private boolean connected = false;
    
    private PipeHandler handler;
    
    private Pipe pipe ;

    private Buffer buffer;

    LocalClientConnector(final Buffer buffer)
    {
        this.buffer = buffer;
        pipe = new NullPipe() 
        {
            @Override
            public AsyncEvent write(Object message)
                throws IOException
            {
                buffer.toServer(message);
                return InstantaneousAsyncEvent.instance();
            }

            @Override
            public long id()
            {
                throw new NotImplementedYet();
            }
        };
    }

    @Override
    public Pipe pipe()
    {
        return pipe;
    }

    @Override
    public void connect() 
        throws Exception
    {
        connect(0);
    }

    @Override
    public void connect(long timeout) 
        throws Exception
    {
        this.connected = true;
        Thread t = Sys.startDaemon(this);
        t.setName("LocalClient");
    }

    @Override
    public void reconnect() 
        throws Exception
    {
        disconnect();
        connect();
    }

    @Override
    public void disconnect()
    {
        this.connected = false;
    }

    @Override
    public boolean isConnected()
    {
        return this.connected && !buffer.isCrashed();
    }

    @Override
    public void register(PipeHandler handler)
    {
        this.handler = handler;
    }


    public boolean send(Object message)
        throws IOException
    {
        buffer.toServer(message);
        return true;
    }

    @Override
    public void run()
    {
        while(connected)
        {
            Object obj;
            try
            {
                obj = buffer.fromServer();
                handler.messageReceived(null, obj);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public SocketAddress address()
    {
        throw new NotImplementedYet();
    }

    @Override
    public String alias()
    {
        throw new NotImplementedYet();
    }
}
