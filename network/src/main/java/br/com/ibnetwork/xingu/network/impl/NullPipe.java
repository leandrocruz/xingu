package br.com.ibnetwork.xingu.network.impl;

import java.net.SocketAddress;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.network.Pipe;

public abstract class NullPipe
    implements Pipe
{
    @Override
    public void close(boolean immediately)
    {
        throw new NotImplementedYet();
    }

    @Override
    public SocketAddress getLocalAddress()
    {
        throw new NotImplementedYet();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isConnected()
    {
        throw new NotImplementedYet();
    }
}