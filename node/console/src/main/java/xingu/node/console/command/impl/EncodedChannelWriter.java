package xingu.node.console.command.impl;

import org.jboss.netty.channel.Channel;

import xingu.codec.Codec;
import xingu.node.console.command.Writer;

public class EncodedChannelWriter
	implements Writer
{
	private Channel	channel;

	private Codec	codec;

	public EncodedChannelWriter(Channel channel, Codec codec)
	{
		this.channel = channel;
		this.codec   = codec;
	}

	@Override
	public void write(Object obj)
		throws Exception
	{
		boolean open = channel.isOpen();
		if(open)
		{
			String text;
			if(obj instanceof String)
			{
				text = obj.toString();
			}
			else
			{
				text = codec.encode(obj);
			}
			channel.write(text + "\r\n");
		}
	}
}