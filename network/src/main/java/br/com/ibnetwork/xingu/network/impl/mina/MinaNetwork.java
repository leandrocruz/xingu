package br.com.ibnetwork.xingu.network.impl.mina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.BadParameter;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.lang.Sys;
import br.com.ibnetwork.xingu.network.connector.ClientConnector;
import br.com.ibnetwork.xingu.network.connector.Connector;
import br.com.ibnetwork.xingu.network.connector.ServerConnector;
import br.com.ibnetwork.xingu.network.impl.local.NetworkSupport;

public class MinaNetwork
    extends NetworkSupport
    implements Configurable
{
    @Inject
    private Factory factory;
    
    private List<ServerConnector> serverConnectors;
    
    private List<ClientConnector> clientConnectors;
    
    private ClientConnectorFactory  clientConnectorFactory;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        Configuration[] connectors = conf.getChild("connectors").getChildren();
        serverConnectors = new ArrayList<ServerConnector>();
        clientConnectors = new ArrayList<ClientConnector>();
        clientConnectorFactory = new ClientConnectorFactory(factory);
        
        for (Configuration configuration : connectors)
        {
            int port = configuration.getAttributeAsInteger("port");
            if(port > 0)
            {
                String type = configuration.getName();
                Connector connector;
                if("server".equals(type))
                {
                    connector = factory.create(MinaServerConnector.class, configuration);
                    serverConnectors.add((ServerConnector) connector);
                }
                else if("client".equals(type))
                {
                    clientConnectorFactory.register(configuration);
                }
                else
                {
                    throw new ConfigurationException("Unknown connector type: "+type);
                }
            }
        }
    }

    @Override
    public ClientConnector clientConnector()
    {
        return clientConnector("default");
    }

    @Override
    public ClientConnector clientConnector(String alias)
    {
        ClientConnector connector = clientConnectorFactory.newConnector(alias);
        clientConnectors.add(connector);
        return connector;
    }

    @Override
    public ServerConnector serverConnector()
    {
        return serverConnector("default");
    }

    @Override
    public List<ServerConnector> serverConnectors()
    {
        return serverConnectors;
    }

    @Override
    public ServerConnector serverConnector(String alias)
    {
        return find(serverConnectors, alias);
    }
    
    private <T extends Connector> T find(List<T> connectors, String alias)
    {
        for (T connector : connectors)
        {
            if(connector.alias().equalsIgnoreCase(alias))
            {
                return connector;
            }
        }
        throw new BadParameter("Cant't find connector: "+alias);
    }

    @Override
    public void crash()
    {
        for(ClientConnector connector : clientConnectors)
        {
            connector.disconnect();
            while(connector.isConnected())
            {
                Sys.sleepWithoutInterruptions(100);
            }
        }

        for (ServerConnector connector : serverConnectors)
        {
            connector.unbind();
        }
    }

    @Override
    public void resume()
        throws Exception
    {
        for (ServerConnector connector : serverConnectors)
        {
            connector.bind();
        }

        for(ClientConnector connector : clientConnectors)
        {
            connector.reconnect();
        }
    }

    @Override
    public boolean isCrashed()
    {
        throw new NotImplementedYet();
    }
}

class ClientConnectorFactory
{
    private Map<String, Configuration> config = new HashMap<String, Configuration>();
    
    private Factory factory;
    
    public ClientConnectorFactory(Factory factory)
    {
        this.factory = factory;
    }
    
    public void register(Configuration conf)
    {
        config.put(conf.getAttribute("alias","default"), conf);
    }
    
    public ClientConnector newConnector(String alias)
    {
        return factory.create(MinaClientConnector.class, config.get(alias));
    }
}