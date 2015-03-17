package xingu.node.commons.signal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.avalon.framework.configuration.Configuration;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.netty.channel.InstantaneousChannelEvent;
import xingu.node.commons.session.Session;
import xingu.node.commons.session.SessionManager;
import xingu.node.commons.signal.impl.ExceptionSignal;
import xingu.node.commons.signal.impl.SignalHandlerImpl;
import xingu.node.commons.signal.impl.TimeoutSignal;

public class SignalHandlerTest
	extends XinguTestCase
{
	@Inject
	private SignalHandler handler;
	
	@Inject
	private SessionManager sessions;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		Configuration conf = this.buildFrom("<component><query timeout=\"1s\"/></component>");
		binder.bind(SignalHandler.class).to(SignalHandlerImpl.class).with(conf);
		withMock(SessionManager.class);
	}

	@Test
	public void testTimeout()
		throws Exception
	{
		Signal signal = new MyRequest();
		Session session = mock(Session.class);
		Channel channel = mock(Channel.class);
		
		when(sessions.by(channel)).thenReturn(session);
		when(channel.write(any(Signal.class))).thenReturn(InstantaneousChannelEvent.instance(channel));
		
		TimeoutSignal<?> reply = (TimeoutSignal<?>) handler.query(signal, null, channel, 1000);
		assertSame(signal, reply.getSignal());
		assertEquals(1000, reply.getTimeout());
	}

	@Test
	public void testWriteError()
		throws Exception
	{
		Signal        signal    = new MyRequest();
		Session       session   = mock(Session.class);
		Channel       channel   = mock(Channel.class);
		ChannelFuture future    = mock(ChannelFuture.class);
		Exception     exception = new Exception();
		
		when(future.isSuccess()).thenReturn(false);
		when(future.getCause()).thenReturn(exception);
		when(sessions.by(channel)).thenReturn(session);
		when(channel.write(any(Signal.class))).thenReturn(future);
		
		ExceptionSignal<?> reply = (ExceptionSignal<?>) handler.query(signal, null, channel, 1000);
		assertSame(signal, reply.getSignal());
		assertSame(exception, reply.getCause());
	}

	@Test
	public void testInterruption()
		throws Exception
	{
		final Signal signal = new MyRequest();
		Session session = mock(Session.class);
		final Channel channel = mock(Channel.class);
		
		when(sessions.by(channel)).thenReturn(session);
		when(channel.write(any(Signal.class))).thenReturn(InstantaneousChannelEvent.instance(channel));
		
		final AtomicBoolean interrupted = new AtomicBoolean(false);
		
		final Thread t1 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					handler.query(signal, null, channel, 1000);
				}
				catch(Exception e)
				{
					interrupted.set(true);
				}
			}
		});

		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
					t1.interrupt();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		assertEquals(true, interrupted.get());
	}

	@Test
	public void testQueryReturn()
		throws Exception
	{
		final Signal signal = new MyRequest<Object>("a1b2");
		final Signal response = new MyResponse<Object>("a1b2");

		Session session = mock(Session.class);
		final Channel channel = mock(Channel.class);
		
		when(sessions.by(channel)).thenReturn(session);
		when(channel.write(any(Signal.class))).thenReturn(InstantaneousChannelEvent.instance(channel));
		
		final AtomicBoolean match = new AtomicBoolean(false);
		
		final Thread t1 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					Signal reply = handler.query(signal, null, channel, 1000);
					match.set(reply == response);
				}
				catch(Exception e)
				{
				}
			}
		});

		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
					handler.on(response, channel);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	
		assertEquals(true, match.get());
	}

	@Test
	public void testLateReply()
		throws Exception
	{
		final Signal signal = new MyRequest<Object>("a1b2");
		final Signal response = new MyResponse<Object>("a1b2");

		Session session = mock(Session.class);
		final Channel channel = mock(Channel.class);
		
		when(sessions.by(channel)).thenReturn(session);
		when(channel.write(any(Signal.class))).thenReturn(InstantaneousChannelEvent.instance(channel));
		
		final AtomicBoolean match = new AtomicBoolean(false);
		
		final Thread t1 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					Signal reply = handler.query(signal, null, channel, 1000);
					match.set(reply instanceof TimeoutSignal);
				}
				catch(Exception e)
				{
				}
			}
		});

		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(2000);
					handler.on(response, channel);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	
		assertEquals(true, match.get());
	}

}