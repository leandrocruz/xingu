package xingu.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import xingu.netty.protocol.frame.Frame;
import xingu.netty.protocol.frame.FrameException;

public class FrameBasedMessageDecoder
    extends FrameDecoder
{
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) 
        throws Exception
    {
    	byte[][] frames = null;
    	try
    	{
    		buffer.markReaderIndex();
    		frames = Frame.unpackArray(buffer);
    	}
    	catch(FrameException e)
    	{
            buffer.resetReaderIndex();
            return null;
    	}
    	
    	byte[] type = frames[0];
    	byte[] data = frames[1];
    	
    	
        return toObject(channel, Frame.toInt(type), data);
    }

	/*
     * Called when all message bytes were read
     */
    protected Object toObject(Channel channel, int type, byte[] data)
    	throws Exception
	{
    	return new String(data);
	}
}
