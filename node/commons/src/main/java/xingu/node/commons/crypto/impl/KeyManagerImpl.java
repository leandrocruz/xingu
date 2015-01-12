package xingu.node.commons.crypto.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.node.commons.crypto.KeyManager;
import xingu.node.commons.crypto.KeyPair;


public class KeyManagerImpl
    implements KeyManager, Configurable
{
    @Inject
    private Factory factory;
    
    private Map<String, KeyPair> map = new HashMap<String, KeyPair>();
    
    @Override
    public void configure(Configuration configuration)
        throws ConfigurationException
    {
        Configuration[] pairs = configuration.getChildren("pair");
        for (Configuration conf : pairs)
        {
            KeyPair pair = factory.create(KeyPair.class, conf);
            map.put(pair.id(), pair);
        }
    }

    @Override
    public KeyPair pair(String id)
    {
        return map.get(id);
    }
}