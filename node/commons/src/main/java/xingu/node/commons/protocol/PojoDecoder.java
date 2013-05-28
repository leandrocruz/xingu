package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.CharsetUtil;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageDecoder;
import br.com.ibnetwork.xingu.container.Inject;

public class PojoDecoder
	extends FrameBasedMessageDecoder
{
    @Inject("proto")
    private Codec codec;

    @Override
	protected Object toObject(Channel channel, byte[] bytes, int type)
		throws Exception
	{
    	String input = new String(bytes, CharsetUtil.UTF_8);
    	return codec.decode(input);
	}
}