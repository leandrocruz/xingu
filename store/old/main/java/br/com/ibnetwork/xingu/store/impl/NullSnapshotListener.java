package br.com.ibnetwork.xingu.store.impl;

import br.com.ibnetwork.xingu.store.SnapshotListener;

public class NullSnapshotListener
    implements SnapshotListener
{
    private static final NullSnapshotListener INSTANCE = new NullSnapshotListener();
    
    private NullSnapshotListener()
    {}
    
    public static NullSnapshotListener instance()
    {
        return INSTANCE;
    }
    
    @Override
    public void beforeSnapshot()
    {}

    @Override
    public void snapshotFailed(Exception e)
    {}

    @Override
    public void afterSnapshot()
    {}

}
