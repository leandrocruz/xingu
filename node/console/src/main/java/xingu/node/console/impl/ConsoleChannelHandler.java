package xingu.node.console.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;
import br.com.ibnetwork.xingu.lang.thread.ThreadNamer;

import xingu.codec.Codec;
import xingu.node.console.RemoteConsoleRegistry;
import xingu.node.console.command.Command;

import org.apache.avalon.framework.activity.Startable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ConsoleChannelHandler
	extends SimpleChannelUpstreamHandler
	implements Startable
{
	@Inject("console")
	private Codec					codec;

	@Inject
	private RemoteConsoleRegistry	registry;

	protected ExecutorService		executor;

	protected DaemonThreadFactory	threadFactory;
	
	@Override
	public void start()
		throws Exception
	{
		threadFactory = new DaemonThreadFactory(new ThreadNamer()
		{
			@Override
			public String nameFor(int num)
			{
				return "CommandConsoleWorker #" + num;
			}
		});
		
		executor = Executors.newFixedThreadPool(2, threadFactory);
	}

	@Override
	public void stop()
		throws Exception
	{
		threadFactory.interruptAllThreads();
		executor.shutdownNow();
	}


	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		e.getChannel().write("Who are you? What do you want? Get out!! I told you!\r\n");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel  channel  = e.getChannel();
		String   received = (String) e.getMessage();
		String[] args     = null;
		String   name     = received;
		String[] parts    = StringUtils.split(received);
		if(parts.length > 1)
		{
			name = parts[0];
			args = new String[parts.length - 1];
			System.arraycopy(parts, 1, args, 0, args.length);
		}
		
		if("q".equals(name))
		{
			channel.write("bye!\r\n").addListener(ChannelFutureListener.CLOSE);
		}
		else
		{
			Command cmd = registry.by(name);
			if(cmd != null)
			{
				write(channel, "Executing command '" + name + "'");
				Callable<Command> task = new CommandTask(codec, channel, cmd, args);
				executor.submit(task);
			}
			else
			{
				write(channel, "Command '" + received + "' is invalid!");
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		Throwable t = e.getCause();
		t.printStackTrace();

		Channel channel = e.getChannel();
		if(channel.isConnected())
		{
			String trace = ExceptionUtils.getStackTrace(t);
			write(channel, trace);
		}
	}

	private void write(Channel channel, String message)
	{
		if(channel.isConnected())
		{
			channel.write("> " + message + "\r\n");
		}
	}
}