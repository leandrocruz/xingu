package xingu.node.commons.protocol.handshake;

import org.jboss.netty.channel.ChannelHandler;

public interface HandshakeManager
{
	ChannelHandler newHandler();
}
