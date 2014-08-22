package xavante.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.avalon.framework.configuration.Configuration;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import xavante.dispatcher.impl.RequestDispatcherImpl;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.Factory;

public class RequestDispatcherTest
	extends XinguTestCase
{
	@Inject
	private RequestDispatcher dispatcher;
	
	HandlerForTesting handler = new HandlerForTesting();

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		
		ClassMatcher matcher = new ClassMatcher(RequestHandler.class);
		Factory factory = mock(Factory.class);
		when(factory.create(argThat(matcher))).thenReturn(handler);
		binder.bind(Factory.class).to(factory);
		
		Configuration conf = this.buildFrom(
				"<x><handlers>"
					+ "<handler path=\"/x\" class=\"xavante.dispatcher.HandlerForTesting\"/>"
				+ "</handlers></x>");
		binder.bind(RequestDispatcher.class).to(RequestDispatcherImpl.class).with(conf);
		
	}
	
	@Test
	public void testBadURI()
		throws Exception
	{
		Channel channel = mock(Channel.class);
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("http://x.com:8000/x/");
		
		dispatcher.dispatch(req, channel);
		assertEquals(true, handler.executed);
	}
}

class ClassMatcher
	extends ArgumentMatcher<Class<?>>
{
	private Class<?>	target;

	public ClassMatcher(Class<?> clazz)
	{
		this.target = clazz;
	}

	@Override
	public boolean matches(Object argument)
	{
		return target.isAssignableFrom((Class)argument);
	}
	
}