package xingu.node.traffic;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class Pong
	extends SimpleChannelUpstreamHandler
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Object obj = e.getMessage();
		if(obj instanceof String)
		{
			String msg = String.class.cast(obj);
			if(msg.startsWith("PNG"))
			{
				Channel channel = e.getChannel();
				channel.write("ACK" + msg.substring(3));
				return;
			}
		}
		ctx.sendUpstream(e);
	}
}