package xavante.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.avalon.framework.configuration.Configuration;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.junit.Test;

import xavante.dispatcher.impl.RequestDispatcherImpl;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class RequestDispatcherTest
	extends XinguTestCase
{
	@Inject
	private RequestDispatcher dispatcher;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		Configuration conf = this.buildFrom(
				"<x><handlers>"
					+ "<handler path=\"/x\" class=\"xavante.dispatcher.HandlerForTesting\"/>"
					+ "<handler path=\"/\" class=\"xavante.dispatcher.HandlerForTesting\"/>"
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
		HandlerForTesting handler = (HandlerForTesting) dispatcher.byPath("/x");
		assertEquals(true, handler.executed);
		
		handler = (HandlerForTesting) dispatcher.byPath("/");
		assertEquals(false, handler.executed);
	}
}