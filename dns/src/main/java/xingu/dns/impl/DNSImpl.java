package xingu.dns.impl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import xingu.dns.DNS;

public class DNSImpl
    implements DNS
{
    private Map<String, InetSocketAddress> cache = new HashMap<String, InetSocketAddress>();
    
    private static final String SEPARATOR = ":";
    
    @Override
    public SocketAddress addressFor(String host, int port)
    {
        int idx = host.indexOf(SEPARATOR);
        String key;
        if(idx > 0)
        {
            key = host;
            port = Integer.parseInt(host.substring(idx + 1));
            host = host.substring(0, idx);
        }
        else
        {
            key = host+SEPARATOR+port;
        }
        InetSocketAddress result = cache.get(key);
        if(result == null || result.isUnresolved())
        {
            result = new InetSocketAddress(host, port);
            if(result.isUnresolved())
            {
                result = callSecondaryDNS(host, port);
                
            }
            cache.put(key, result);
        }
        return result; 
    }

    private InetSocketAddress callSecondaryDNS(String host, int port)
    {
        return null;
    }
}