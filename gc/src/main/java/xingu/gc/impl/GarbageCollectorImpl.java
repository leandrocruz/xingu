package xingu.gc.impl;

import java.util.concurrent.TimeUnit;

import org.apache.avalon.framework.activity.Startable;

import xingu.gc.Collectable;
import xingu.gc.GarbageCollector;
import xingu.lang.NotImplementedYet;

public class GarbageCollectorImpl
	implements GarbageCollector, Startable
{
	@Override
	public void start()
		throws Exception
	{}

	@Override
	public void stop()
		throws Exception
	{}

	@Override
	public void register(Collectable collectable, int time, TimeUnit unit)
	{
		throw new NotImplementedYet();
	}
}
