package xingu.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xingu.container.Binder;
import xingu.container.ConfigurationLoader;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.http.client.CommandLineBuilder;
import xingu.http.client.CookieUtils;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.impl.NameValueImpl;
import xingu.http.client.impl.curl.CurlCommandLineBuilder;
import xingu.http.client.impl.curl.CurlCommandLineBuilder.Builder;

public class CurlCommandLineBuilderTest
	extends XinguTestCase
{
	@Inject
	private CommandLineBuilder builder;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
        Configuration conf = ConfigurationLoader.load(getFile("pulga-curl.xml"));
        Configuration component = Arrays
            .stream(conf.getChildren())
            .filter(c -> c.getAttribute("role", "").equalsIgnoreCase(CommandLineBuilder.class.getCanonicalName()))
            .findFirst()
            .orElse(conf);

		binder.bind(CommandLineBuilder.class).to(CurlCommandLineBuilder.class).with(component);
	}

	@Test
    public void testProxyWithoutDuplicatedHeaders()
        throws Exception
    {
        HttpRequest  req      = mock(HttpRequest.class);
        when(req.getProxy()).thenReturn("testing");
        when(req.getUri()).thenReturn("http://esaj.tjrn.jus.br");

        HttpResponse response = builder.responseFrom(req, getFile("response13.txt"), getFile("headers13.txt"));
        assertEquals(200, 		response.getCode());
        assertEquals("3480", 	response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
        assertEquals("-1001", 	response.getHeader(HttpHeaders.Names.EXPIRES));
    }

	@Test
	public void testHttp100Response()
		throws Exception
	{
		HttpRequest  req      = mock(HttpRequest.class);
		HttpResponse response = builder.responseFrom(req, getFile("empty.txt"), getFile("headers1.txt"));
		assertEquals(200, 		response.getCode());
		assertEquals("1000", 	response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
		assertEquals("Jetty(8.1.2.v20120308)", response.getHeader("Server"));
	}
	
	@Test
	public void testHttp100ResponseWhenProxied()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getProxy()).thenReturn("testing");
		
		HttpResponse response = builder.responseFrom(req, getFile("empty.txt"), getFile("headers5.txt"));
		assertEquals(302, response.getCode());
		assertEquals("0", response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
		assertEquals("Apache/2.4.6 (Red Hat Enterprise Linux) OpenSSL/1.0.2k-fips mod_jk/1.2.42", response.getHeader("Server"));
	}
	
//	@Test
//	public void testHttpSingleProxyHeaders()
//		throws Exception
//	{
//		HttpRequest req = mock(HttpRequest.class);
//		when(req.getProxy()).thenReturn("testing");
//		
//		HttpResponse response = builder.responseFrom(req, getFile("response12.txt"));
//		assertEquals(302, response.getCode());
//		assertEquals("0", response.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
//		assertEquals("Apache/2.4.6 (Red Hat Enterprise Linux) OpenSSL/1.0.2k-fips mod_jk/1.2.42", response.getHeader("Server"));
//	}
	
	@Test
	public void testHttp200ResponseWhenNotProxied()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		
		HttpResponse response = builder.responseFrom(req, getFile("response6.txt"), getFile("headers6.txt"));
		assertEquals(200, response.getCode());
		assertEquals("nginx", response.getHeader("Server"));
	}
	
	@Test
	public void testHttpMisleadingProxyHeaders()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getProxy()).thenReturn("this is a proxy");
		HttpResponse response = builder.responseFrom(req, getFile("response20.txt"), getFile("headers20.txt"));
		assertEquals(200, response.getCode());
	}

	@Test
	public void testHttp100ResponseWithEmptyBody()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		
		HttpResponse response = builder.responseFrom(req, getFile("empty.txt"), getFile("headers2.txt"));
		assertEquals(100, response.getCode());
		assertEquals("", response.getBody());
	}

	@Test
	public void testHttp200ResponseIso88591AsUtf8()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		HttpResponse response = builder.responseFrom(req, getFile("response8.txt"), getFile("headers8.txt"));
		assertEquals(200, response.getCode());
		assertEquals("TJGO", response.getHeader("Server"));
		assertTrue(response.getBody("UTF-8").contains("Juntada de Petição"));
	}
	
	@Test
	public void testHttpUTF8ResponseWithIso88591()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("console4.oystr.com.br");
		HttpResponse response = builder.responseFrom(req, getFile("response9.txt"), getFile("headers9.txt"));
		Document utf8 = response.getDocument("UTF-8");
		Document iso = response.getDocument("ISO-8859-1");
		
		assertTrue(utf8.select("#app button > i.download.icon").first().parent().ownText().equalsIgnoreCase("Baixar relatório"));
		assertTrue(!iso.select("#app button > i.download.icon").first().parent().ownText().equalsIgnoreCase("Baixar relatório"));
	}
	
	@Test
	public void testHttp2Stuff()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("console4.oystr.com.br");
		HttpResponse response = builder.responseFrom(req, getFile("response10.txt"), getFile("headers10.txt"));
		response.getDocument().select("a[id=ctl00_btnCerrarSesion]").text();
		
		assertTrue(response.getDocument().select("a[id=ctl00_btnCerrarSesion]").text().equalsIgnoreCase("Close Session"));
	}

	@Test
	public void testHttp200Response()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("www.globo.com");
		HttpResponse response = builder.responseFrom(req, getFile("response3.txt"), getFile("headers3.txt"));
		Document     doc      = response.getDocument();
		Element      el       = doc.getElementById("busca-padrao");
		assertEquals(200, response.getCode());
		assertEquals("buscarbuscarbuscar", el.text());
	}
	
	@Test
	public void testHttpBBJURResponse()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
//		when(req.getProxy()).thenReturn("testing");
		HttpResponse response = builder.responseFrom(req, getFile("response7.txt"), getFile("headers7.txt"));

		assertEquals(200, response.getCode());
		assertEquals("resource=2.1", response.getHeader("Content-API-Version"));
		assertEquals("application/json; charset=UTF-8", response.getHeader("Content-Type"));
		assertTrue(response.getBody().contains("https://juridico.bb.com.br/paj/juridico"));
	}

	@Test
	public void testHttpCookies()
		throws Exception
	{
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("www.google.com");
		HttpResponse response = builder.responseFrom(req, getFile("response4.txt"), getFile("headers4.txt"));
		Cookies      cookies  = CookieUtils.getCookies(response);
		Cookie       c1       = cookies.byName("PREF");
		Cookie       c2       = cookies.byName("NID");
		assertEquals(302, response.getCode());
		assertEquals("ID=ce20a24454b3e7b0:FF=0:TM=1392923056:LM=1392923056:S=XJzMcL5FMR_wGaFN", c1.getValue());
		assertEquals("67=gbVYO3CBYtd5fAHBUEPbz6FkA8vQ7MqymZaWefWUgAK0gfW0PGnZc7ByFguhcOI798Kh-y6eZVNaqYJVZ2ytjGl7Kbsvx21ndYBds0qUkS-NsBeBRuBMh3mcvR55N18S", c2.getValue());
	}

	@Test
	public void testCookieFilter()
		throws Exception
	{
		Cookies cookies = CookieUtils.toCookies("a=1; b=2; domain=mimimi.com; c=3; domain=google.com");
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("http://www.google.com");
		when(req.getCookies()).thenReturn(cookies);
		when(req.getMethod()).thenReturn("GET");
		List<String> cmd = builder.buildLine(req, getFile("empty.txt"), getFile("headers1.txt"));
		String header = cmd.get(cmd.indexOf("--cookie") + 1);
		assertEquals("a=1; c=3", header);
	}
	
	@Test
	public void testDifferentCharsetsForHeaderAndBody()
		throws Exception
	{
		List<NameValue> headers = new ArrayList<NameValue>();
		headers.add(new NameValueImpl(HttpHeaders.Names.CONTENT_TYPE, "json/application"));
		
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("http://www.google.com");
		when(req.getMethod()).thenReturn("POST");
		when(req.hasPayload()).thenReturn(true);
		when(req.getPayloadAsJson()).thenReturn("{\"value\": \"ÁéÓão\", \"onclick\": \"CreateNewDoc()\"}");
		when(req.getPayloadCharset()).thenReturn("UTF-8");
		when(req.getHeaders()).thenReturn(headers);
		
		List<String> cmd = builder.buildLine(req, getFile("empty.txt"), getFile("headers1.txt"));
		Builder cmdLineBuilder = ((CurlCommandLineBuilder)builder).getBuilder();
		
		File f = cmdLineBuilder.getPayloadDumpFile();
		InputStream is = new FileInputStream(f);
		String content = IOUtils.toString(is, req.getPayloadCharset());
		JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
		
		// just to ensure that nobody will change the mock
		assertEquals("UTF-8", req.getPayloadCharset());
		assertEquals("ÁéÓão", obj.get("value").getAsString());
		assertEquals("ISO-8859-1", cmdLineBuilder.getHeaderCharset());		
	}
	
	@Test
	public void testSameCharsetForHeaderAndBody()
		throws Exception
	{
		List<NameValue> headers = new ArrayList<NameValue>();
		headers.add(new NameValueImpl(HttpHeaders.Names.CONTENT_TYPE, "json/application"));
		
		HttpRequest req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn("http://www.google.com");
		when(req.getMethod()).thenReturn("POST");
		when(req.hasPayload()).thenReturn(true);
		when(req.getPayloadAsJson()).thenReturn("{\"value\": \"ÁéÓão\", \"onclick\": \"CreateNewDoc()\"}");
		when(req.getHeaders()).thenReturn(headers);
		
		List<String> cmd = builder.buildLine(req, getFile("empty.txt"), getFile("headers1.txt"));
		Builder cmdLineBuilder = ((CurlCommandLineBuilder)builder).getBuilder();
		
		File f = cmdLineBuilder.getPayloadDumpFile();
		InputStream is = new FileInputStream(f);
		String content = IOUtils.toString(is,  cmdLineBuilder.getHeaderCharset());
		JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
		
		// just to ensure that nobody will change the mock
		assertEquals(null, req.getPayloadCharset());
		assertEquals("ÁéÓão", obj.get("value").getAsString());
		assertEquals("ISO-8859-1", cmdLineBuilder.getHeaderCharset());		
	}
}
