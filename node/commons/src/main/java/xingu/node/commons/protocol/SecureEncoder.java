package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;

import xingu.codec.Codec;
import xingu.container.Inject;
import xingu.netty.protocol.FrameBasedMessageEncoder;

public class SecureEncoder
    extends FrameBasedMessageEncoder
{
    @Inject("secureCodec")
    private Codec codec;
    
    @Inject
    private ProtocolCodec proto;

    @Override
    protected int typeFrom(Object obj)
    {
    	return proto.typeFrom(obj);
    }

    @Override
    protected byte[] toByteArray(Channel channel, Object obj, int type)
        throws Exception
    {
    	/* encoded is XML or JSON now */
    	String encoded = codec.encode(obj);
        
        /* data is a byte array which may have been encrypted */
    	long keyId = channel.getId();
    	byte[] data = proto.encode(keyId, encoded, type);
        
    	/* last step is packing */
        return data;
    }
}