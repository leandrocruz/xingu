package br.com.ibnetwork.xingu.network;

import java.io.IOException;
import java.net.SocketAddress;

public interface Pipe
{
    long id();
    
    void close(boolean immediately);
    
    AsyncEvent write(Object message)
        throws IOException;

    SocketAddress getRemoteAddress();

    SocketAddress getLocalAddress();

    boolean isConnected();
}
