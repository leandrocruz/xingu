package xingu.netty.http;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpChunk;

import xingu.lang.NotImplementedYet;

public class PaddedChunk
    implements HttpChunk
{
    private ChannelBuffer padding;
    private ChannelBuffer buffer;

    public PaddedChunk(ChannelBuffer padding, ChannelBuffer buffer)
    {
        this.padding = padding;
        this.buffer = buffer;
    }

    @Override
    public boolean isLast()
    {
        return true;
    }

    @Override
    public ChannelBuffer getContent()
    {
        return buffer;
    }

    @Override
    public void setContent(ChannelBuffer content)
    {
        throw new NotImplementedYet();
    }

    public ChannelBuffer padding()
    {
        return padding;
    }

}
