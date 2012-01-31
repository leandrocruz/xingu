package br.com.ibnetwork.xingu.network.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;


public interface ServerConnector
    extends Connector
{
    void bind() 
        throws IOException;

    void unbind();

    List<InetSocketAddress> getAddresses();
}
