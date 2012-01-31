package br.com.ibnetwork.xingu.network.impl.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import br.com.ibnetwork.xingu.network.connector.ServerConnector;

public class MinaServerConnector
    extends MinaConnectorSupport
    implements ServerConnector
{
    private static final String ALL_INTERFACES = "0.0.0.0";
    
    protected String[] interfaces;

    public IoAcceptor acceptor;

    private List<InetSocketAddress> addresses;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        super.configure(conf);
        String ifaces = conf.getAttribute("interfaces", ALL_INTERFACES);
        interfaces = StringUtils.split(ifaces, ",");
        ioService();
        if(conf.getAttributeAsBoolean("reuseAddress", true))
        {
            if(acceptor instanceof NioSocketAcceptor)
            {
                ((NioSocketAcceptor) acceptor).setReuseAddress(true);
            }
        }
    }
    
    @Override
    protected IoService createIoService()
    {
        if("UDP".equalsIgnoreCase(transport))
        {
            acceptor = new NioDatagramAcceptor();
        }
        else if("TCP".equalsIgnoreCase(transport))
        {
            acceptor = new NioSocketAcceptor();
        }
        else
        {
            throw new IllegalArgumentException("Unknown transport: "+transport);
        }
        return acceptor;
    }


    public void bind() 
        throws IOException
    {
        addresses = parseInterfaces();
        acceptor.bind(addresses);
    }

    protected List<InetSocketAddress> parseInterfaces()
    {
        List<InetSocketAddress> result = new ArrayList<InetSocketAddress>();
        for (String iface : interfaces)
        {
            iface = StringUtils.trimToNull(iface);
            if (iface != null)
            {
                InetSocketAddress addr = new InetSocketAddress(iface, port);
                result.add(addr);
            }
        }
        return result;
    }


    public void unbind()
    {
        acceptor.unbind();
    }

    public List<InetSocketAddress> getAddresses()
    {
        return addresses;
    }

    @Override
    public String toString()
    {
        return alias + " (" 
        + StringUtils.join(interfaces, ",") + ":" + port 
        + "/"+ transport.toUpperCase() 
        + ") ";
    }
}
