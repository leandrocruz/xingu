package br.com.ibnetwork.xingu.network.impl.mina;

import org.apache.mina.core.future.IoFuture;

import br.com.ibnetwork.xingu.network.AsyncEvent;

public class MinaFuture
    implements AsyncEvent
{
    private IoFuture future;
    
    public MinaFuture(IoFuture future)
    {
        this.future = future;
    }

    @Override
    public void waitWithoutInterruptions()
    {
        future.awaitUninterruptibly();
    }

    @Override
    public boolean isDone()
    {
        return future.isDone();
    }
}
