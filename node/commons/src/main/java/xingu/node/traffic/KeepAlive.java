package xingu.node.traffic;

import java.util.Collection;

import org.apache.avalon.framework.activity.Startable;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import xingu.container.Inject;
import xingu.lang.thread.Task;
import xingu.lang.thread.Worker;
import xingu.node.commons.signal.impl.Ping;
import xingu.utils.TimeUtils;

public class KeepAlive
	extends SimpleChannelHandler
	implements Startable, Task
{
	@Inject
	private TrafficHandler traffic;

	private Worker worker;
	
	private static final long FIVE_MINUTES = TimeUtils.toMillis("5m");
	@Override
	public void start()
		throws Exception
	{
		worker = new Worker("KeepAliveWorker", true, FIVE_MINUTES, this);
	}

	@Override
	public void stop()
		throws Exception
	{
		if(worker != null)
		{
			worker.interrupt();
		}
	}	

	@Override
	public void execute()
		throws Exception
	{
		long time = System.currentTimeMillis();
		Collection<ChannelData> coll = traffic.getData();
		for(ChannelData data : coll)
		{
			long last = data.timeForLastEvent();
			long duration = time - last;
			if(duration >= FIVE_MINUTES)
			{
				Channel channel = data.getChannel();
				Ping ping = new Ping();
				ping.sTime(time);
				channel.write(ping);
			}
		}
	}

	@Override
	public void pause(long time)
		throws InterruptedException
	{
		Thread.sleep(time);
	}

	@Override
	public boolean isFinished()
	{
		return false;
	}

	@Override
	public boolean abortOnError(Throwable t)
	{
		return true;
	}

	@Override
	public void beforeExit()
	{}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		traffic.onConnect(channel);
		ctx.sendUpstream(e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		traffic.onDisconnect(channel);
		ctx.sendUpstream(e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		traffic.onMessage(channel);
		ctx.sendUpstream(e);
	}

	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		traffic.onWrite(channel);
		ctx.sendUpstream(e);
	}
}
