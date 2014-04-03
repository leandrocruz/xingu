package xingu.node.commons.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.CharsetUtil;

import xingu.codec.Codec;
import xingu.netty.protocol.FrameBasedMessageDecoder;
import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxManager;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;

public class PojoDecoder
	extends FrameBasedMessageDecoder
{
	private static final String KEY = "proto";
	
    @Inject
    private SandboxManager sandboxes;

    @SuppressWarnings({ "unchecked" })
	@Override
    protected Object toObject(Channel channel, int type, byte[] data)
    	throws Exception
	{
		String      input                  = new String(data, CharsetUtil.UTF_8);
		int         idx                    = input.indexOf("@");
		String      sandboxId              = input.substring(0, idx);
		String      payload                = input.substring(idx + 1);
		Sandbox     sandbox                = sandboxes.byId(sandboxId);
		Container   container              = sandbox.container();
		ClassLoader cl                     = sandbox.classLoader();
		Class<?     extends   Codec> clazz = (Class<? extends Codec>) cl.loadClass("xingu.codec.Codec");
		Codec       codec                  = container.lookup(clazz, KEY);
    	return codec.decode(payload, cl);
	}
}