package br.com.ibnetwork.xingu.network.impl;

import br.com.ibnetwork.xingu.network.AsyncEvent;

public class InstantaneousAsyncEvent
    implements AsyncEvent
{
    private static final InstantaneousAsyncEvent INSTANCE = new InstantaneousAsyncEvent();
    
    private InstantaneousAsyncEvent()
    {}
    
    public static InstantaneousAsyncEvent instance()
    {
        return INSTANCE;
    }
    
    @Override
    public void waitWithoutInterruptions()
    {
        return;
    }

    @Override
    public boolean isDone()
    {
        return true;
    }
}
