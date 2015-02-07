package xingu.dns;

import java.net.SocketAddress;

public interface DNS
{
    SocketAddress addressFor(String host, int port);
}
