package xingu.node.commons.session;

import org.jboss.netty.channel.Channel;

public interface SessionManager
{
	Session newSession(Channel channel);

	Session by(Channel channel);

	Session by(long id);
}
