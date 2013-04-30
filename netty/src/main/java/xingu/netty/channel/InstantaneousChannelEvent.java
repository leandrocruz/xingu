package xingu.netty.channel;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class InstantaneousChannelEvent
    implements ChannelFuture
{
    private static final InstantaneousChannelEvent INSTANCE = new InstantaneousChannelEvent(null);
    
    private Channel channel;
    
    private InstantaneousChannelEvent(Channel channel)
    {
    	this.channel = channel;
    }
    
    public static InstantaneousChannelEvent instance()
    {
        return INSTANCE;
    }

	public static ChannelFuture instance(Channel channel)
	{
		return new InstantaneousChannelEvent(channel);
	}

    @Override
    public boolean isDone()
    {
        return true;
    }

    @Override
    public void addListener(ChannelFutureListener listener)
    {
    	try
		{
			listener.operationComplete(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }

    @Override
    public ChannelFuture await() 
        throws InterruptedException
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean await(long timeoutMillis) 
        throws InterruptedException
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture awaitUninterruptibly()
    {
        return this;
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis)
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean cancel()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Throwable getCause()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Channel getChannel()
    {
        return channel;
    }

    @Override
    public boolean isCancelled()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isSuccess()
    {
        return true;
    }

    @Override
    public void removeListener(ChannelFutureListener listener)
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean setFailure(Throwable cause)
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean setProgress(long amount, long current, long total)
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean setSuccess()
    {
        throw new NotImplementedYet();
    }

	//@Override
	public ChannelFuture rethrowIfFailed()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	//@Override
	public ChannelFuture sync()
		throws InterruptedException
	{
		throw new NotImplementedYet();
	}

	//@Override
	public ChannelFuture syncUninterruptibly()
	{
		throw new NotImplementedYet();
	}
}
