package br.com.ibnetwork.xingu.container.components.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.container.components.Component;
import br.com.ibnetwork.xingu.container.components.DefaultExternalConf;

public class ComponentImpl
    implements Component, DefaultExternalConf, Configurable
{

    private Configuration conf;

    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        this.conf = conf;
    }

    public String getValue()
    {
        return conf.getChild("some").getAttribute("key",null);
    }

}
