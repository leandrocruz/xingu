package xingu.node.client.bridge;

import org.jboss.netty.channel.Channel;

public interface OnConnect
{
	void onSuccess(Channel channel);
	
	void onError(Throwable t);

	void onFailure();
}
