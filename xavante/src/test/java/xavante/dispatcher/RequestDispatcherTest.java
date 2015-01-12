package xavante.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.avalon.framework.configuration.Configuration;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.junit.Test;

import xavante.dispatcher.impl.RequestDispatcherImpl;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.url.QueryString;
import xingu.url.Url;

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
	public void testHTTP1_0Uri()
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

	@Test
	public void testIfHandlerPathIsRemovedFromURI()
		throws Exception
	{
		Channel channel = mock(Channel.class);
		HttpRequest req = mock(HttpRequest.class);
		
		testPath(req, channel, "http://x.com:8000/x/", "/", null);
		testPath(req, channel, "http://x.com:8000/x?a", "/", "a");
		testPath(req, channel, "http://x.com:8000/x/?a", "/", "a");
		testPath(req, channel, "http://x.com:8000/x//?a", "/", "a");
		testPath(req, channel, "/x/", "/", null);
		testPath(req, channel, "/x?a", "/", "a");
		testPath(req, channel, "/x/?a", "/", "a");

	}
	
	private void testPath(HttpRequest req, Channel channel, String uri, String expectedPath, String expectedQueryString)
		throws Exception
	{
		reset(req);
		when(req.getUri()).thenReturn(uri);
		
		dispatcher.dispatch(req, channel);
		verify(req, times(1)).setUri(expectedPath + (expectedQueryString != null ? "?" + expectedQueryString : ""));
		
		HandlerForTesting handler = (HandlerForTesting) dispatcher.byPath("/x");

		Url url = handler.req.getUrl();
		assertEquals(expectedPath, url.getPath());
		String queryString = url.getQuery();
		assertEquals(expectedQueryString, queryString);
	}
}