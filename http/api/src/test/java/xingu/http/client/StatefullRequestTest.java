package xingu.http.client;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.netty.handler.codec.http.DefaultCookie;
import org.junit.Test;

import xingu.http.client.impl.CookiesImpl;
import xingu.http.client.impl.NameValueImpl;
import xingu.http.client.impl.StatefullRequest;

public class StatefullRequestTest
{
	@Test
	public void testCookieUpdate()
		throws Exception
	{
		HttpResponse    reply   = mock(HttpResponse.class);
		HttpRequest     req     = mock(HttpRequest.class);
		HttpStateHandler state   = mock(HttpStateHandler.class);
		HttpContext     ctx     = mock(HttpContext.class);
		NameValue[]     headers = new NameValue[]{new NameValueImpl("set-cookie", "my_cookie=a")};

		when(state.getContext()).thenReturn(ctx);
		when(state.getCookies()).thenReturn(null);
		when(req.exec()).thenReturn(reply);
		when(reply.getHeaders()).thenReturn(headers);

		HttpResponse res = new StatefullRequest(req, state).exec();
		assertSame(reply, res);

		Cookies cookies = new CookiesImpl();
		cookies.add(new DefaultCookie("my_cookie", "a"));

		verify(req, times(1)).context(ctx);
		verify(req, times(0)).withCookies(any(Cookies.class));
		verify(state, times(1)).setCookies(eq(cookies));
	}
}