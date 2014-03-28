package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.CharsetUtil;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageDecoder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoaderManager;

public class PojoDecoder
	extends FrameBasedMessageDecoder
{
    @Inject("proto")
    private Codec codec;
    
    @Inject
    private NamedClassLoaderManager clm;

    @Override
    protected Object toObject(Channel channel, int type, byte[] data)
    	throws Exception
	{
    	String      input           = new String(data, CharsetUtil.UTF_8);
    	int         idx             = input.indexOf("@");
    	String      classLoaderName = input.substring(0, idx);
    	String      payload         = input.substring(idx + 1);
    	ClassLoader cl              = clm.byName(classLoaderName);
    	
    	return codec.decode(payload);
	}
}