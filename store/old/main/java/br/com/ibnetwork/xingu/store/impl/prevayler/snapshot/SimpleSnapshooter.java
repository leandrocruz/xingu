package br.com.ibnetwork.xingu.store.impl.prevayler.snapshot;

import org.apache.avalon.framework.configuration.Configuration;
import org.prevayler.Prevayler;

import br.com.ibnetwork.xingu.store.SnapshotListener;

public class SimpleSnapshooter
    extends SnapshooterSupport
{
    public SimpleSnapshooter(SnapshotListener listener, Prevayler prevayler, String prevalenceDirectoryName, Configuration conf)
    {
        super(listener, prevayler);
    }

    @Override
    protected void deleteOldFiles() 
    {}
}
