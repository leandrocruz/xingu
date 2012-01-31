package org.jboss.netty.handler.codec.compression;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;

import xingu.netty.Deflater;


public class EmbeddedDeflater
    implements Deflater
{
    DecoderEmbedder<ChannelBuffer> e;
    
    public EmbeddedDeflater(ZlibWrapper wrapper)
    {
        e = new DecoderEmbedder<ChannelBuffer>(new ZlibDecoder(wrapper));
    }

    @Override
    public ChannelBuffer deflate(ChannelBuffer buffer) 
        throws Exception
    {
        buffer.markReaderIndex();
        try
        {
            e.offer(buffer);
        }
        catch(Throwable t)
        {
            if(t instanceof Exception)
            {
                throw (Exception) t;
            }
            throw new Exception(t);
        }
        finally
        {
            buffer.resetReaderIndex();
        }
        ChannelBuffer result = e.poll();
        return result;
    }
}
