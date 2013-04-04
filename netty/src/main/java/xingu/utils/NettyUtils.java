package xingu.utils;

import java.util.concurrent.Executor;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.CharUtils;

import xingu.netty.IoModel;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class NettyUtils
{
	public static String readLine(ChannelBuffer buffer, int max)
	{
		StringBuilder sb = new StringBuilder(64);
		int len = 0;
		while(len < max)
		{
			len++;
			byte nextByte = buffer.readByte();
			if(nextByte == CharUtils.CR)
			{
				nextByte = buffer.readByte();
				len++;
				if(nextByte == CharUtils.LF)
				{
					return sb.toString();
				}
			}
			else if(nextByte == CharUtils.LF)
			{
				return sb.toString();
			}
			else
			{
				sb.append((char) nextByte);
			}
		}
		return sb.toString();
	}

    public static String readLine(ChannelBuffer buffer) 
    {
    	return readLine(buffer, buffer.capacity());
    }
    
	public static String[] splitInitialLine(String sb)
	{
		int aStart;
		int aEnd;
		int bStart;
		int bEnd;
		int cStart;
		int cEnd;

		aStart = findNonWhitespace(sb, 0);
		aEnd = findWhitespace(sb, aStart);

		bStart = findNonWhitespace(sb, aEnd);
		bEnd = findWhitespace(sb, bStart);

		cStart = findNonWhitespace(sb, bEnd);
		cEnd = findEndOfString(sb);

		return new String[] {
				sb.substring(aStart, aEnd), 
				sb.substring(bStart, bEnd), 
				cStart < cEnd ? sb.substring(cStart, cEnd) : ""
		};
	}

	private static int findNonWhitespace(String sb, int offset)
	{
		int result;
		for(result = offset; result < sb.length(); result++)
		{
			if(!Character.isWhitespace(sb.charAt(result)))
			{
				break;
			}
		}
		return result;
	}

	private static int findWhitespace(String sb, int offset)
	{
		int result;
		for(result = offset; result < sb.length(); result++)
		{
			if(Character.isWhitespace(sb.charAt(result)))
			{
				break;
			}
		}
		return result;
	}

	private static int findEndOfString(String sb)
	{
		int result;
		for(result = sb.length(); result > 0; result--)
		{
			if(!Character.isWhitespace(sb.charAt(result - 1)))
			{
				break;
			}
		}
		return result;
	}

    public static void closeOnFlush(Channel channel) 
    {
    	closeOnFlush(channel, ChannelFutureListener.CLOSE);
    }
    
    public static void closeOnFlush(Channel channel, ChannelFutureListener listener) 
    {
        if (channel != null && channel.isConnected()) 
        {
            channel.write(ChannelBuffers.EMPTY_BUFFER).addListener(listener);
        }
    }

    public static int indexOf(ChannelBuffer buffer, ChannelBuffer wanted)
    {
        for (int i = buffer.readerIndex(); i < buffer.writerIndex(); i++)
        {
            int bufferIndex = i;
            int wantedIndex;
            for (wantedIndex = 0; wantedIndex < wanted.capacity(); wantedIndex++)
            {
                if (buffer.getByte(bufferIndex) != wanted.getByte(wantedIndex))
                {
                    break;
                }
                else
                {
                    bufferIndex++;
                    if (bufferIndex == buffer.writerIndex()
                            && wantedIndex != wanted.capacity() - 1)
                    {
                        return -1;
                    }
                }
            }
    
            if (wantedIndex == wanted.capacity())
            {
                // Found wanted on buffer
                return i - buffer.readerIndex();
            }
        }
        return -1;
    }
    
    public static ChannelBuffer bufferFrom(Object obj)
    {
        if (obj instanceof HttpChunk)
        {
            return ((HttpChunk) obj).getContent();
        }
        else if (obj instanceof HttpResponse)
        {
            return ((HttpResponse) obj).getContent();
        }
        else
        {
            throw new NotImplementedYet();
        }
    }
    
    public static ChannelFactory clientChannelFactory(Executor boss, Executor worker, String ioModel)
    {
        ChannelFactory channelFactory;
        if(IoModel.NEW.equalsIgnoreCase(ioModel))
        {
            channelFactory = new NioClientSocketChannelFactory(boss, worker);
        }
        else if(IoModel.OLD.equalsIgnoreCase(ioModel))
        {
            channelFactory = new OioClientSocketChannelFactory(boss);
        }
        else
        {
            throw new NotImplementedYet("Io Model '"+ioModel+"' not supported");
        }
        return channelFactory;
    }
    
    public static ChannelFactory serverChannelFactory(Executor boss, Executor worker, String ioModel)
    {
        ChannelFactory channelFactory;
        if(IoModel.NEW.equalsIgnoreCase(ioModel))
        {
            channelFactory = new NioServerSocketChannelFactory(boss, worker);
        }
        else if(IoModel.OLD.equalsIgnoreCase(ioModel))
        {
            channelFactory = new OioServerSocketChannelFactory(boss, worker); 
        } 
        else
        {
            throw new NotImplementedYet("Io model '"+ioModel+"' not supported");
        }
        return channelFactory;
    }

	public static void closeQuietly(Channel channel)
	{
		if(channel == null)
		{
			return;
		}
		try
		{
			channel.close().await();
		}
		catch (InterruptedException e)
		{}
	}

	public static final boolean removeOnce(ChannelPipeline pipeline, String name)
	{
		if(pipeline.get(name) != null)
		{
			pipeline.remove(name);
			return true;
		}
		return false;
	}
	
	public static final boolean addOnce(ChannelPipeline pipeline, String name, ChannelHandler handler)
	{
		if(pipeline.get(name) == null)
		{
			pipeline.addFirst(name, handler);
			return true;
		}
		return false;
	}

}