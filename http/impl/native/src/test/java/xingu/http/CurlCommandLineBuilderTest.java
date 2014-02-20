package xingu.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.net.URL;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.junit.Test;

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
		URL          url      = getResource("response1.txt");
		HttpResponse response = builder.responseFrom(req, new File(url.getFile()));
		assertEquals(200, 		response.getCode());
		assertEquals("1000", 	response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
		assertEquals("Jetty(8.1.2.v20120308)", response.getHeader("Server"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testHttp100ResponseWithError()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		URL         url = getResource("response2.txt");
		builder.responseFrom(req, new File(url.getFile()));
		throw new Exception("Should have thrown exception");
	}

}
