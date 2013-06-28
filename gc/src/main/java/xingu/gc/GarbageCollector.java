package xingu.gc;

import java.util.concurrent.TimeUnit;

public interface GarbageCollector
{
	void register(Collectable collectable, int time, TimeUnit unit);
}
