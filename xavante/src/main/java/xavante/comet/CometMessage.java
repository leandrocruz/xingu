package xavante.comet;

import org.jboss.netty.channel.Channel;

public interface CometMessage
{
	Channel getChannel();
	
	String getToken();

	String getSequence();

	String getCommand();

	String getData();
}