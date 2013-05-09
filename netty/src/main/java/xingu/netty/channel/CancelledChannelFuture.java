package xingu.netty.channel;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.CompleteChannelFuture;

public class CancelledChannelFuture
	extends CompleteChannelFuture
{
	private final Throwable cause;

	public CancelledChannelFuture(Channel channel)
	{
		this(channel, null);
	}

	public CancelledChannelFuture(Channel channel, Throwable cause)
	{
		super(channel);
		this.cause = cause;
	}

	@Override
	public boolean isSuccess()
	{
		return false;
	}
	
	@Override
	public boolean isCancelled()
	{
		return true;
	}

    public boolean isDone()
    {
        return false;
    }

	@Override
	public Throwable getCause()
	{
		return cause;
	}

	@Override
	public ChannelFuture rethrowIfFailed()
		throws Exception
	{
		if (cause instanceof Exception)
		{
			throw (Exception) cause;
		}

		if (cause instanceof Error)
		{
			throw (Error) cause;
		}

		throw new RuntimeException(cause);
	}

	@Override
	public ChannelFuture sync()
		throws InterruptedException
	{
		rethrow();
		return this;
	}

	private void rethrow()
	{
		if (cause instanceof RuntimeException)
		{
			throw (RuntimeException) cause;
		}

		if (cause instanceof Error)
		{
			throw (Error) cause;
		}

		throw new ChannelException(cause);

	}

	@Override
	public ChannelFuture syncUninterruptibly()
	{
		rethrow();
		return this;
	}
}
