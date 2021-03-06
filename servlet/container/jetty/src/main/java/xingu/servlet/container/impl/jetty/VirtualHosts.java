package xingu.servlet.container.impl.jetty;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.mortbay.jetty.servlet.Context;

public class VirtualHosts
{
    private String[] hosts;

    public VirtualHosts(Configuration conf)
        throws ConfigurationException
    {
        if(conf == null)
        {
            return;
        }
        Configuration[] hostsConfig = conf.getChildren("host");
        hosts = new String[hostsConfig.length];
        int index = 0;
        for (Configuration host : hostsConfig)
        {
            hosts[index++] = host.getAttribute("name");
        }
    }
    
    public void addTo(Context context)
    {
        context.setVirtualHosts(hosts);
    }
}
