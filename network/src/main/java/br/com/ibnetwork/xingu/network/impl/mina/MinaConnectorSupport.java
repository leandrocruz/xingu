package br.com.ibnetwork.xingu.network.impl.mina;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.BadParameter;
import br.com.ibnetwork.xingu.network.PipeHandler;
import br.com.ibnetwork.xingu.network.connector.Connector;

public abstract class MinaConnectorSupport
    implements Connector, Configurable
{
    @Inject
    protected Factory factory;
    
    protected String alias;
    
    protected String transport;
    
    protected int port;

    private IoService io;
    
    private IoConfig ioConfig;

    protected DelegateToPipe delegate;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        alias = conf.getAttribute("alias");
        transport = conf.getAttribute("transport", "tcp");
        port = conf.getAttributeAsInteger("port", 0);
        ioConfig = new IoConfig(conf);
    }

    private void config(IoService service, IoConfig ioConfig)
    {
        String handler = ioConfig.handler;
        if(handler != null)
        {
            PipeHandler pipeHandler = (PipeHandler) factory.create(handler);
            register(pipeHandler);
        }
        DefaultIoFilterChainBuilder filterChain = service.getFilterChain();
        if(ioConfig.logEnabled)
        {
            filterChain.addLast("logger", new LoggingFilter()); 
        }
        String protocolCodecFactory = ioConfig.protocolCodecFactory;
        if(protocolCodecFactory != null)
        {
            ProtocolCodecFactory codecFactory = (ProtocolCodecFactory) factory.create(protocolCodecFactory);
            filterChain.addLast("codec", new ProtocolCodecFilter(codecFactory));
        }
        else if(ioConfig.useTextLineCodec)
        {
            filterChain.addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        }
        if(ioConfig.useSSL)
        {
            SSLContext sslContext = sslContext();
            if(sslContext != null)
            {
                SslFilter sslFilter = new SslFilter(sslContext);
                if(ioConfig.isClient())
                {
                    sslFilter.setUseClientMode(true);
                }
                filterChain.addLast("ssl", sslFilter);                
            }
        }
        service.getSessionConfig().setReadBufferSize(2048);
        service.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
    }

    protected SSLContext sslContext()
    {
        SSLContext sslContext;
        try
        {
            sslContext = SSLContext.getDefault();
            return sslContext;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    protected abstract IoService createIoService();
    
    protected IoService ioService()
    {
        if(io != null)
        {
            return io;
        }
        io = createIoService();
        config(io, ioConfig);
        if(delegate != null)
        {
            io.setHandler(delegate);
        }
        return io;
    }

    protected void disposeIo()
    {
        if(io != null)
        {
            io.dispose();
        }
        io = null;
    }
    
    @Override
    public String alias()
    {
        return alias;
    }
    
    @Override
    public void register(PipeHandler handler)
    {
        if(delegate != null)
        {
            throw new BadParameter("Can't set another pipe delegate. " + this.delegate + " is still active");
        }
        this.delegate = new DelegateToPipe(handler); 
        ioService().setHandler(delegate);
    }

    public String toString()
    {
        return alias + " (" + transport.toUpperCase() + ")";
    }
}

class IoConfig
{
    boolean logEnabled;
    String handler;
    String protocolCodecFactory;
    boolean useTextLineCodec;
    boolean useSSL;
    String name;

    public IoConfig(Configuration conf)
    {
        logEnabled = conf.getAttributeAsBoolean("logEnabled", false);
        handler = conf.getAttribute("pipeHandler", null);
        protocolCodecFactory = conf.getAttribute("protocolCodecFactory", null);
        useTextLineCodec = conf.getAttributeAsBoolean("useTextLineCodec", true);
        useSSL = conf.getAttributeAsBoolean("useSSL", false);
        name = conf.getName();
    }
    
    public boolean isClient()
    {
        return "client".equals(name);
    }
}