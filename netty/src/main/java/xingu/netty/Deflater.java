package xingu.netty;

import org.jboss.netty.buffer.ChannelBuffer;

public interface Deflater
{
    ChannelBuffer deflate(ChannelBuffer buffer)
        throws Exception;
}
