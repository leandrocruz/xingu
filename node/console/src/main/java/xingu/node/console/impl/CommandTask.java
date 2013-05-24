package xingu.node.console.impl;

import java.util.concurrent.Callable;

import xingu.codec.Codec;
import xingu.node.console.command.Command;

import org.jboss.netty.channel.Channel;

public class CommandTask
	implements Callable<Command>
{

	private final Channel channel;
	
	private final Command command;
	
	private final String[] args;

	private final Codec codec;

	public CommandTask(Codec codec, Channel channel, Command command, String[] args)
	{
		this.codec = codec;
		this.channel = channel;
		this.command = command;
		this.args = args;
	}

	@Override
	public Command call()
		throws Exception
	{
		String text = "OK";
		Object result = command.execute(args);
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
		channel.write(text + "\r\n");
		return command;
	}

}
