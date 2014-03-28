package xingu.netty.protocol.frame;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public abstract class Frame
{
	public static final int	INT_LEN	= 4;

	public ChannelBuffer pack()
	{
		int size = getSize();
		int len  = size < 0 ? 0 : size;
		ChannelBuffer buffer = ChannelBuffers.buffer(INT_LEN + len);
		buffer.writeInt(size);
		if(len > 0)
		{
			writePayload(buffer);
		}
		
		assert buffer.writableBytes() == 0;
		
		return buffer;
	}

	public static byte[] unpack(ChannelBuffer buffer)
		throws FrameException
	{
		int readable = buffer.readableBytes();
		if(readable < INT_LEN)
		{
			throw new FrameException("Not enough bytes to read. Wanted "+INT_LEN+" got "+readable);
		}
		
		int size = buffer.readInt();
		if(size <= 0)
		{
			return size < 0 ? null : new byte[]{};
		}
		
		readable = buffer.readableBytes();
		if(readable < size)
		{
			throw new FrameException("Not enough bytes to read. Wanted "+size+" got "+readable);
		}
		
		byte[] data = new byte[size];
		buffer.readBytes(data);
		
		return data;
	}

	public static ChannelBuffer packArray(Frame... frames)
	{
		int           len    = frames.length;
		ChannelBuffer prolog = new IntegerFrame(len).pack();
		int           size   = prolog.capacity();

		//Prolog
		ChannelBuffer[] buffers = new ChannelBuffer[len + 1];
		buffers[0] = prolog;
		
		//Payload
		for(int i = 0; i < len; i++)
		{
			ChannelBuffer buffer = frames[i].pack();
			buffers[i + 1] = buffer;
			size += buffer.capacity();
		}

		//Envelope
		ChannelBuffer result = ChannelBuffers.buffer(Frame.INT_LEN + size);
		result.writeInt(size);
		for(ChannelBuffer buffer : buffers)
		{
			result.writeBytes(buffer);
		}
		return result;
	}

	public static byte[][] unpackArray(ChannelBuffer buffer)
		throws FrameException
	{
		//Envelope
		byte[]        data  = Frame.unpack(buffer);
		ChannelBuffer slice = ChannelBuffers.wrappedBuffer(data);
		
		//Prolog
		byte[]   prolog = Frame.unpack(slice);
		int      size   = toInt(prolog);
		
		//Payload
		byte[][] result = new byte[size][];
		for(int i=0 ; i<size ; i++)
		{
			result[i] = Frame.unpack(slice);
		}
		
		return result;
	}
	
	public static int toInt(byte[] data)
	{
		return ChannelBuffers.wrappedBuffer(data).readInt();
	}

	public static String toString(byte[] data)
	{
		return new String(data);
	}

	protected abstract void writePayload(ChannelBuffer buffer);
	protected abstract int getSize();

}