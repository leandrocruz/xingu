package xingu.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class FrameBasedMessageDecoder
    extends FrameDecoder
{
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) 
        throws Exception
    {
        int available = buffer.readableBytes();
        if(available < FramedMessageUtils.HEADER_SIZE_LENGTH)
        {
            return null;
        }
        
        buffer.markReaderIndex();
        int length = buffer.readInt();
        if(available < length + FramedMessageUtils.HEADER_SIZE_LENGTH)
        {
            buffer.resetReaderIndex();
            return null;
        }

        int type = buffer.readInt();
        int capacity = length - FramedMessageUtils.HEADER_TYPE_LENGTH;
        if(capacity <= 0)
        {
            throw new NotImplementedYet("Protocol Error: length size is '"+length+"' Type is '"+type+"'");
        }
        
        byte[] bytes = new byte[capacity];
        buffer.readBytes(bytes);
        return toObject(channel, bytes, type);
    }

    /*
     * Called when all message bytes were read
     */
    protected Object toObject(Channel channel, byte[] bytes, int type)
    	throws Exception
	{
    	return new String(bytes);
	}
}
