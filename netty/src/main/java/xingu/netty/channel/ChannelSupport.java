package xingu.netty.channel;

import java.net.SocketAddress;


import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;


import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class ChannelSupport
    implements Channel
{
    protected Object lastWrite = null;
    
    private Channel delegate;
    
    public ChannelSupport()
    {}

    public ChannelSupport(Channel delegate)
    {
        this.delegate = delegate;
    }

    public Object waitForWrite()
    {
        return lastWrite;
    }

    @Override
    public ChannelFuture write(Object obj) 
    {
        lastWrite = obj;
        return InstantaneousChannelEvent.instance();
    }

    @Override
    public ChannelFuture close()
    {
        throw new NotImplementedYet();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        if(delegate != null)
        {
            return delegate.getRemoteAddress();
        }
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress)
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress)
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture disconnect()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture getCloseFuture()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelConfig getConfig()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFactory getFactory()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Integer getId()
    {
        throw new NotImplementedYet();
    }

    @Override
    public int getInterestOps()
    {
        throw new NotImplementedYet();
    }

    @Override
    public SocketAddress getLocalAddress()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Channel getParent()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelPipeline getPipeline()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isBound()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isConnected()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isOpen()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isReadable()
    {
        throw new NotImplementedYet();
    }

    @Override
    public boolean isWritable()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture setInterestOps(int interestOps)
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture setReadable(boolean readable)
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture unbind()
    {
        throw new NotImplementedYet();
    }

    @Override
    public ChannelFuture write(Object message, SocketAddress remoteAddress)
    {
        throw new NotImplementedYet();
    }

    @Override
    public int compareTo(Channel o)
    {
        throw new NotImplementedYet();
    }
}
