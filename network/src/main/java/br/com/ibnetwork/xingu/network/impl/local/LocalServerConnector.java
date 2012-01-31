package br.com.ibnetwork.xingu.network.impl.local;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.lang.Sys;
import br.com.ibnetwork.xingu.network.AsyncEvent;
import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.PipeHandler;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;
import br.com.ibnetwork.xingu.network.impl.InstantaneousAsyncEvent;
import br.com.ibnetwork.xingu.network.impl.NullPipe;

public class LocalServerConnector
    implements ServerConnector, Runnable, Configurable
{
    private PipeHandler handler;
    
    private boolean binded = false;

    private Buffer buffer;

    private Pipe pipe;

    LocalServerConnector(final Buffer buffer)
    {
        this.buffer = buffer;
        pipe = new NullPipe()
        {
            @Override
            public AsyncEvent write(Object message)
                throws IOException
            {
                buffer.toClient(message);
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
    public void configure(Configuration conf) 
        throws ConfigurationException
    {}

    @Override
    public void register(PipeHandler handler)
    {
        this.handler = handler;
        this.handler.toString();
    }

    @Override
    public void run()
    {
        while(binded)
        {
            try
            {
                Object message = buffer.fromClient();
                handler.messageReceived(pipe, message);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public String alias()
    {
        return "default";
    }

    @Override
    public void bind() 
        throws IOException
    {
        binded = true;
        Thread t = Sys.startDaemon(this);
        t.setName("LocalServer");
    }

    @Override
    public List<InetSocketAddress> getAddresses()
    {
        throw new NotImplementedYet();
    }

    @Override
    public void unbind()
    {
        binded = false;
    }
}