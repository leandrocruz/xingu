package xingu.utils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class AddressUtils
{
    public static SocketAddress parseAddress(String hostAndPort, int defaultPort)
    {
        String host = hostAndPort;
        int port = defaultPort;
        int idx = hostAndPort.indexOf(":"); 
        if(idx > 0)
        {
            host = hostAndPort.substring(0, idx);
            port = Integer.parseInt(hostAndPort.substring(idx+1));
        }
        SocketAddress addr = new InetSocketAddress(host, port);
        return addr;
    }

    public static String ipAndPort(InetSocketAddress address)
    {
        String result = address.getAddress().getHostAddress() + ":" + address.getPort();
        return result;
    }
    
}
