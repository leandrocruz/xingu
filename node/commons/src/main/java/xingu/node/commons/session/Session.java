package xingu.node.commons.session;

import org.jboss.netty.channel.Channel;

public interface Session
{
	long getId();

	Channel getChannel();
}
