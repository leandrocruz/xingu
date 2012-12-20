package xingu.netty.channel.logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.MessageEvent;

import xingu.utils.HexDump;

public abstract class ChannelLoggerSupport
	implements ChannelUpstreamHandler, ChannelDownstreamHandler
{
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
		throws Exception
	{
		log(">", e);
		ctx.sendDownstream(e);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
		throws Exception
	{
		log("<", e);
		ctx.sendUpstream(e);
	}

	protected void log(String marker, ChannelEvent e)
		throws Exception
	{
		if(!(e instanceof MessageEvent))
		{
			return;
		}
		
		MessageEvent me = (MessageEvent) e;
		Object msg = me.getMessage();
		String txt = toString(msg);
		write(marker, txt);
	}
	
	protected String toString(Object msg)
	{
		if(msg instanceof ChannelBuffer)
        {
			ChannelBuffer buffer = (ChannelBuffer) msg;
			return HexDump.toHex(buffer);
        }
		else
		{
			return msg.toString();
		}
	}

	protected abstract void write(String marker, String message)
		throws Exception;
}