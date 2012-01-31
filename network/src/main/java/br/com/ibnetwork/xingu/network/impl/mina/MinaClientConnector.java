package br.com.ibnetwork.xingu.network.impl.mina;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;

public class MinaClientConnector
    extends MinaConnectorSupport
    implements ClientConnector
{
    private SocketAddress address;
    
    private Logger log = LoggerFactory.getLogger(getClass());

	private MinaPipe pipe;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        super.configure(conf);
        String host = conf.getAttribute("address","127.0.0.1");
        address = new InetSocketAddress(host, port);
    }

    @Override
    public SocketAddress address()
    {
        return address;
    }

    @Override
    protected IoService createIoService()
    {
        return new NioSocketConnector();
    }

    @Override
    public void connect() 
        throws Exception
    {
        connect(3000);
    }   

    @Override
    public void connect(long timeout) 
        throws Exception
    {
        if(isConnected())
        {
            return;
        }
        log.info("Connecting client to: '{}'", address);
        SocketConnector io = (SocketConnector) ioService();
        ConnectFuture future = io.connect(address);
        future.awaitUninterruptibly(timeout);
        IoSession session = future.getSession();
		pipe = new MinaPipe(session);
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
        disposeIo();
        Pipe pipe = pipe();
        if(pipe != null)
        {
             pipe.close(true);
        }
    }

    @Override
    public boolean isConnected()
    {
        Pipe pipe = pipe();
        return pipe != null && pipe.isConnected();
    }
    
    @Override
    public Pipe pipe()
    {
    	return pipe;
    }
    
    @Override
    public String toString()
    {
        return alias + " (" +address.toString().substring(1)+"/"+ transport.toUpperCase() + ") ";
    }
}