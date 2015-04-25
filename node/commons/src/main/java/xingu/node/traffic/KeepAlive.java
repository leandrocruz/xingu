package xingu.node.traffic;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.avalon.framework.activity.Startable;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.lang.thread.Task;
import xingu.lang.thread.Worker;
import xingu.time.Time;
import xingu.utils.TimeUtils;

public class KeepAlive
	extends SimpleChannelHandler
	implements Startable, Task
{
	@Inject
	private Time					time;

	@Inject
	private TrafficHandler			traffic;

	private Worker					worker;

	protected volatile AtomicLong	count		= new AtomicLong(0);

	private long					interval	= TimeUtils.toMillis("5m");

	private Logger					logger		= LoggerFactory.getLogger(getClass());
	
	public KeepAlive()
	{}
	
	public KeepAlive(long interval)
	{
		this.interval = interval;
	}

	@Override
	public void start()
		throws Exception
	{
		worker = new Worker("KeepAliveWorker", true, interval, this);
		worker.start();
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
		long now = time.now().time();
		Collection<ChannelData> coll = traffic.getData();
		for(ChannelData data : coll)
		{
			long duration = now - data.timeForLastEvent();
			if(duration >= interval)
			{
				Channel channel = data.getChannel();
				long    id      = count.incrementAndGet();
				logger.info("Sending PNG to {}", channel.getRemoteAddress());
				channel.write("PNG." + id + "." + channel.getId() + "." + now);
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
		Object obj = e.getMessage();
		if(obj instanceof String)
		{
			String msg = String.class.cast(obj);
			if(msg.startsWith("ACK"))
			{
				String[]    parts     = StringUtils.split(msg, ".");
				int         channelId = Integer.valueOf(parts[2]);
				long        start     = Long.valueOf(parts[3]);
				ChannelData data      = traffic.byChannel(channelId);
				long        now       = time.now().time();
				long duration = now - start;
				data.addPing(duration);

				Channel channel = e.getChannel();
				logger.info("PNG {} ms response from {}", duration, channel.getRemoteAddress());
				
				return;
			}
		}
		
		Channel channel = e.getChannel();
		traffic.onMessage(channel);
		ctx.sendUpstream(e);
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		traffic.onWrite(channel);
		ctx.sendDownstream(e);
	}
}