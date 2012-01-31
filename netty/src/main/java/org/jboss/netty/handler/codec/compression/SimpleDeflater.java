package org.jboss.netty.handler.codec.compression;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.internal.jzlib.JZlib;
import org.jboss.netty.util.internal.jzlib.ZStream;

import xingu.netty.Deflater;

public class SimpleDeflater
    implements Deflater
{
    private final ZStream z = new ZStream();
    
    private volatile boolean finished;

    public SimpleDeflater() 
    {
        this(ZlibWrapper.ZLIB);
    }

    public SimpleDeflater(ZlibWrapper wrapper) 
    {
        if (wrapper == null) 
        {
            throw new NullPointerException("wrapper");
        }

        int resultCode = z.inflateInit(ZlibUtil.convertWrapperType(wrapper));
        if (resultCode != JZlib.Z_OK) 
        {
            ZlibUtil.fail(z, "initialization failure", resultCode);
        }
    }

    /**
     * Returns {@code true} if and only if the end of the compressed stream
     * has been reached.
     */
    public boolean isClosed() 
    {
        return finished;
    }

    @Override
    public ChannelBuffer deflate(ChannelBuffer buffer)
    {
        try 
        {
            // Configure input.
            ChannelBuffer compressed = buffer;
            byte[] in = new byte[compressed.readableBytes()];
            compressed.readBytes(in);
            z.next_in = in;
            z.next_in_index = 0;
            z.avail_in = in.length;
            
            // Configure output.
            byte[] out = new byte[in.length << 1];
            ChannelBuffer decompressed = ChannelBuffers.dynamicBuffer(compressed.order(), out.length, buffer.factory());
            z.next_out = out;
            z.next_out_index = 0;
            z.avail_out = out.length;
            
            do 
            {
                // Decompress 'in' into 'out'
                int resultCode = z.inflate(JZlib.Z_SYNC_FLUSH);
                switch (resultCode) 
                {
                    case JZlib.Z_STREAM_END:
                    case JZlib.Z_OK:
                    case JZlib.Z_BUF_ERROR:
                        decompressed.writeBytes(out, 0, z.next_out_index);
                        z.next_out_index = 0;
                        z.avail_out = out.length;
                        if (resultCode == JZlib.Z_STREAM_END) 
                        {
                            finished = true; // Do not decode anymore.
                            z.inflateEnd();
                        }
                        break;
                    default:
                        ZlibUtil.fail(z, "decompression failure", resultCode);
                }
            } 
            while (z.avail_in > 0);

            if (decompressed.writerIndex() != 0) 
            { // readerIndex is always 0
                return decompressed;
            } 
            else 
            {
                return null;
            }
        } 
        finally 
        {
            // Deference the external references explicitly to tell the VM that
            // the allocated byte arrays are temporary so that the call stack
            // can be utilized.
            // I'm not sure if the modern VMs do this optimization though.
            z.next_in = null;
            z.next_out = null;
        }
    }
}