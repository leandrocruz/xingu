package br.com.ibnetwork.xingu.network.impl.mina;

import java.io.IOException;
import java.net.SocketAddress;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import br.com.ibnetwork.xingu.network.AsyncEvent;
import br.com.ibnetwork.xingu.network.Pipe;

public class MinaPipe
    implements Pipe
{
    private IoSession session;
    
    public MinaPipe(IoSession session)
    {
        //System.out.println(">> "+br.com.ibnetwork.xingu.utils.ObjectUtils.caller()+" creating pipe for session: "+ObjectUtils.identityToString(session));
        this.session = session;
    }

    @Override
    public AsyncEvent write(Object message) 
        throws IOException
    {
        if(!isConnected())
        {
            throw new IOException("Pipe is not connected. Can't send message");
        }
        WriteFuture future = session.write(message);
        return new MinaFuture(future);
    }

    @Override
    public SocketAddress getLocalAddress()
    {
        return session.getLocalAddress();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return session.getRemoteAddress();
    }

    @Override
    public void close(boolean immediately)
    {
        session.close(immediately);
    }

    @Override
    public boolean isConnected()
    {
        return session.isConnected() && !session.isClosing();
    }

    @Override
    public long id()
    {
        return session.getId();
    }
}
