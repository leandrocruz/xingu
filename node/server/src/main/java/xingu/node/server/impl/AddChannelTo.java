package xingu.node.server.impl;

import static org.jboss.netty.channel.ChannelState.CONNECTED;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;

public class AddChannelTo
	implements ChannelUpstreamHandler
{
	private final ChannelGroup group;
	
	public AddChannelTo(ChannelGroup group)
	{
		this.group = group;
	}
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
		throws Exception
	{
		if(e instanceof ChannelStateEvent)
		{
			ChannelStateEvent evt = (ChannelStateEvent) e;
			Object value = evt.getValue();
			if(evt.getState() == CONNECTED && value != null)
			{
				group.add(evt.getChannel());
			}
		}
	}
}
