package xingu.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;

import xingu.netty.protocol.frame.ByteArrayFrame;
import xingu.netty.protocol.frame.Frame;
import xingu.netty.protocol.frame.IntegerFrame;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class FrameBasedMessageEncoder
	implements ChannelDownstreamHandler
{
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
		throws Exception
	{
		if(e instanceof MessageEvent)
		{
			Object obj  = ((MessageEvent) e).getMessage();
			int    type = typeFrom(obj);
			byte[] data = toByteArray(e.getChannel(), obj, type);

			Frame[] frames = new Frame[] {
					new IntegerFrame(type),
					new ByteArrayFrame(data)
			};

			ChannelBuffer buffer = Frame.packArray(frames);
			Channels.write(ctx, e.getFuture(), buffer);
		}
		else
		{
			ctx.sendDownstream(e);
		}
	}

	protected byte[] toByteArray(Channel channel, Object obj, int type)
		throws Exception
	{
		if(obj instanceof String)
		{
			String s = (String) obj;
			return s.getBytes();
		}
		throw new NotImplementedYet("Object type is not suported: " + obj);
	}

	protected int typeFrom(Object obj)
		throws Exception
	{
		return 0;
	}
}