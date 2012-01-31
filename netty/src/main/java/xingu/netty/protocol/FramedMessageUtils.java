package xingu.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class FramedMessageUtils
{
    public static final int HEADER_SIZE_LENGTH = 4;
    
    public static final int HEADER_TYPE_LENGTH = 4;

    public static ChannelBuffer pack(byte[] data, int type) 
        throws Exception
    {
    	int length = data.length + HEADER_TYPE_LENGTH; 
        if(length > Integer.MAX_VALUE)
        {
            throw new Exception("Message is too large to be encoded: " + length);
        }
        ChannelBuffer buffer = allocateBuffer(data.length);
        buffer.writeInt(length);
        buffer.writeInt(type);
        buffer.writeBytes(data);
        return buffer;
    }

    private static ChannelBuffer allocateBuffer(int dataLength)
    {
        ChannelBuffer buffer = ChannelBuffers.buffer(dataLength + HEADER_SIZE_LENGTH + HEADER_TYPE_LENGTH);
        return buffer;
    }

    public static byte[] unpack(ChannelBuffer buffer)
    {
        int length = buffer.readInt();
        byte[] bytes = new byte[length - HEADER_TYPE_LENGTH];
        buffer.readBytes(bytes);
        return bytes;
    }
    
}
