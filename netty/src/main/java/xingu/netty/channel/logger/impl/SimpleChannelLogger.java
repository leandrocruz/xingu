package xingu.netty.channel.logger.impl;

import xingu.netty.channel.logger.ChannelLoggerSupport;


public class SimpleChannelLogger
	extends ChannelLoggerSupport
{
	@Override
	protected void write(String marker, String message)
		throws Exception
	{
		System.err.println(marker + " " + message + "\n");
	}
}
