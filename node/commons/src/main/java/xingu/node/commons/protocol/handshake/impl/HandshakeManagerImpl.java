package xingu.node.commons.protocol.handshake.impl;

import org.jboss.netty.channel.ChannelHandler;

import xingu.netty.channel.logger.impl.SimpleChannelLogger;
import xingu.node.commons.protocol.handshake.HandshakeManager;

@Deprecated
public class HandshakeManagerImpl
	implements HandshakeManager
{
	@Override
	public ChannelHandler newHandler()
	{
		return new SimpleChannelLogger();
	}
}
