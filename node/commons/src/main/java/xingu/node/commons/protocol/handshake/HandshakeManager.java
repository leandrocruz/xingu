package xingu.node.commons.protocol.handshake;

import org.jboss.netty.channel.ChannelHandler;

@Deprecated
public interface HandshakeManager
{
	ChannelHandler newHandler();
}
