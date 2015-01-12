package xingu.store.impl.prevayler.snapshot;

import java.util.TimerTask;

import org.prevayler.Prevayler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.store.SnapshotListener;
import xingu.store.impl.NullSnapshotListener;
import xingu.utils.TimeUtils;

public abstract class SnapshooterSupport
    extends TimerTask
{
    protected Prevayler prevayler;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected SnapshotListener listener;
    
    public SnapshooterSupport(SnapshotListener listener, Prevayler prevayler)
    {
        this.prevayler = prevayler;
        if(listener == null)
        {
            this.listener = NullSnapshotListener.instance();
        }
        else
        {
            this.listener = listener;
        }
    }

    @Override
    public void run()
    {
        logger.info("Taking snapshot");
        long startTime = System.currentTimeMillis();
        listener.beforeSnapshot();
        try
        {
            prevayler.takeSnapshot();
        }
        catch (Exception e)
        {
            logger.error("Failed to take snapshot", e);
            listener.snapshotFailed(e);
        }
        long end = System.currentTimeMillis();
        logger.info("Snapshot finished in '{}'", TimeUtils.toSeconds(end - startTime));
        listener.afterSnapshot();
        deleteOldFiles();
    }

    protected abstract void deleteOldFiles();
}