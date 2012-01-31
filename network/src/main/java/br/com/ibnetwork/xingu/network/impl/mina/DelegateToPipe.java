package br.com.ibnetwork.xingu.network.impl.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.PipeHandler;

public class DelegateToPipe
    implements IoHandler
{
    private PipeHandler delegate;
    
    private Map<IoSession, Pipe> pipes = new HashMap<IoSession, Pipe>();
    
    public DelegateToPipe(PipeHandler pipeHandler)
    {
        this.delegate = pipeHandler;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
        throws Exception
    {
        delegate.exceptionCaught(pipe(session), cause);
    }

	@Override
    public void messageReceived(IoSession session, Object message)
        throws Exception
    {
        delegate.messageReceived(pipe(session), message);
    }

    @Override
    public void messageSent(IoSession session, Object message) 
        throws Exception
    {
        delegate.messageSent(pipe(session), message);
    }

    @Override
    public void sessionClosed(IoSession session) 
        throws Exception
    {
        Pipe pipe = pipe(session);
        pipes.remove(session);
		delegate.pipeClosed(pipe);
    }

    @Override
    public void sessionCreated(IoSession session) 
        throws Exception
    {
        Pipe pipe = new MinaPipe(session);
        pipes.put(session, pipe);
        delegate.pipeCreated(pipe);
    }

    @Override
    public void sessionOpened(IoSession session) 
        throws Exception
    {
        delegate.pipeOpened(pipe(session));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
        throws Exception
    {
        //TODO
    }

    private Pipe pipe(IoSession session)
    {
		return pipes.get(session);
	}
}
