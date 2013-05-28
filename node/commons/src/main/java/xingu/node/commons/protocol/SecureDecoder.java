package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageDecoder;
import br.com.ibnetwork.xingu.container.Inject;

public class SecureDecoder
    extends FrameBasedMessageDecoder
{
	@Inject
	private ProtocolCodec proto;
	
    @Inject("secureCodec")
    private Codec codec;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
	protected Object toObject(Channel channel, byte[] bytes, int type)
		throws Exception
	{
        String text = proto.decode(channel.getId() /* channel id is used as key id */, bytes, type);
        Object obj = null;
        try
        {
            obj = codec.decode(text);
        }
        catch(Throwable t)
        {
            logger.error("error decoding message", t);
            obj = t;
        }
        return obj;
	}
}