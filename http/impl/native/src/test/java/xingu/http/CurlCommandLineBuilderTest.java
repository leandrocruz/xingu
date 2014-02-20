package xingu.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import xingu.http.client.CookieUtils;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.CommandLineBuilder;
import xingu.http.client.impl.curl.CurlCommandLineBuilder;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class CurlCommandLineBuilderTest
	extends XinguTestCase
{
	@Inject
	private CommandLineBuilder builder;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(CommandLineBuilder.class).to(CurlCommandLineBuilder.class);
	}

	@Test
	public void testHttp100Response()
		throws Exception
	{
		HttpRequest  req      = mock(HttpRequest.class);
		HttpResponse response = builder.responseFrom(req, getFile("response1.txt"));
		assertEquals(200, 		response.getCode());
		assertEquals("1000", 	response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
		assertEquals("Jetty(8.1.2.v20120308)", response.getHeader("Server"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testHttp100ResponseWithError()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		builder.responseFrom(req, getFile("response2.txt"));
		throw new Exception("Should have thrown exception");
	}

	@Test
	public void testHttp200Response()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("www.globo.com");
		HttpResponse response = builder.responseFrom(req, getFile("response3.txt"));
		Document     doc      = response.getDocument();
		Element      el       = doc.getElementById("busca-padrao");
		assertEquals(200, response.getCode());
		assertEquals("buscarbuscarbuscar", el.text());
	}

	@Test
	public void testHttpCookies()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("www.google.com");
		HttpResponse response = builder.responseFrom(req, getFile("response4.txt"));
		Cookies      cookies  = CookieUtils.getCookies(response);
		Cookie       c1       = cookies.byName("PREF");
		Cookie       c2       = cookies.byName("NID");
		assertEquals(302, response.getCode());
		assertEquals("ID=ce20a24454b3e7b0:FF=0:TM=1392923056:LM=1392923056:S=XJzMcL5FMR_wGaFN", c1.getValue());
		assertEquals("67=gbVYO3CBYtd5fAHBUEPbz6FkA8vQ7MqymZaWefWUgAK0gfW0PGnZc7ByFguhcOI798Kh-y6eZVNaqYJVZ2ytjGl7Kbsvx21ndYBds0qUkS-NsBeBRuBMh3mcvR55N18S", c2.getValue());
	}
}
