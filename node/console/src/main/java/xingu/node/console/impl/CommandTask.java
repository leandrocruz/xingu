package xingu.node.console.impl;

import java.util.concurrent.Callable;

import xingu.codec.Codec;
import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;
import xingu.node.console.command.impl.EncodedChannelWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.utils.TimeUtils;

public class CommandTask
	implements Callable<Command>
{
	private final Channel	channel;

	private final Command	command;

	private final String[]	args;

	private final Codec		codec;

	private Writer			writer;

	private Logger			logger	= LoggerFactory.getLogger(getClass());

	public CommandTask(Codec codec, Channel channel, Command command, String[] args)
	{
		this.codec   = codec;
		this.channel = channel;
		this.command = command;
		this.args    = args;
		this.writer  = new EncodedChannelWriter(channel, codec); 
	}

	@Override
	public Command call()
		throws Exception
	{
		String text   = "OK";
		Object result = null;
		long   start  = System.currentTimeMillis();
		try
		{
			result = command.execute(args, writer);
		}
		catch(Throwable t)
		{
			logger.error("Error executing command '" + command + "'", t);
			text = ExceptionUtils.getStackTrace(t);
		}
		
		long end = System.currentTimeMillis();

		if(result != null)
		{
			if(result instanceof String)
			{
				text = (String) result;
			}
			else 
			{
				text = codec.encode(result);		
			}
		}
		channel.write(text + "\r\n\r\nin " + TimeUtils.toSeconds(end - start) + " seconds\r\n");
		return command;
	}

}
