package xingu.lang.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InstantFuture<T>
	implements Future<T>
{
	private T t;

	public InstantFuture(T t2)
	{
		this.t = t2;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return true;
	}

	@Override
	public T get()
		throws InterruptedException, ExecutionException
	{
		return t;
	}

	@Override
	public T get(long timeout, TimeUnit unit)
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return t;
	}

	@Override
	public boolean isCancelled()
	{
		return false;
	}

	@Override
	public boolean isDone()
	{
		return true;
	}
}
