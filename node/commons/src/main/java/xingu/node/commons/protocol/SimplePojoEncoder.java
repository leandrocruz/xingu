package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.CharsetUtil;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageEncoder;
import br.com.ibnetwork.xingu.container.Inject;

public class SimplePojoEncoder
	extends FrameBasedMessageEncoder
{
	@Inject("proto")
	private Codec	codec;

	@Override
	protected byte[] toByteArray(Channel channel, Object obj, int type)
		throws Exception
	{
		String encoded = codec.encode(obj);
		return encoded.getBytes(CharsetUtil.UTF_8);
	}
}
