package xavante.comet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URLEncoder;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.junit.Test;

import xavante.XavanteRequest;
import xavante.XavanteRequestFactory;
import xavante.comet.impl.MessageFactoryImpl;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class MessageFactoryTest
	extends XinguTestCase
{
	@Inject
	private MessageFactory factory;
	
	@Override
	protected String getContainerFile()
	{
		return "pulga-empty.xml";
	}

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		super.rebind(binder);
	}
	
	@Test
	public void testCommand()
		throws Exception
	{
		HttpRequest  req     = mock(HttpRequest.class);
		HttpResponse resp    = mock(HttpResponse.class);
		Channel      channel = mock(Channel.class);
		
		when(req.getUri()).thenReturn("/xxx");
		XavanteRequest xeq = XavanteRequestFactory.build(req, channel);
		
		CometMessage msg = factory.build(xeq, resp);
		assertEquals("xxx", msg.getCommand());
		assertEquals(null, msg.getToken());
		assertEquals(null, msg.getSequence());
		assertEquals(null, msg.getData());
	}

	@Test
	public void testCommandWithToken()
		throws Exception
	{
		HttpRequest  req     = mock(HttpRequest.class);
		HttpResponse resp    = mock(HttpResponse.class);
		Channel      channel = mock(Channel.class);
		
		String token = RandomStringUtils.randomAlphanumeric(MessageFactoryImpl.ID_LEN);
		when(req.getUri()).thenReturn("/yyy/" + token);
		
		XavanteRequest xeq = XavanteRequestFactory.build(req, channel);
		CometMessage msg = factory.build(xeq, resp);
		assertEquals("yyy", msg.getCommand());
		assertEquals(token, msg.getToken());
		assertEquals(null, msg.getSequence());
		assertEquals(null, msg.getData());
	}

	@Test
	public void testCommandWithTokenAndSequence()
		throws Exception
	{
		HttpRequest  req     = mock(HttpRequest.class);
		HttpResponse resp    = mock(HttpResponse.class);
		Channel      channel = mock(Channel.class);
		
		String token = RandomStringUtils.randomAlphanumeric(MessageFactoryImpl.ID_LEN);
		when(req.getUri()).thenReturn("/zzz/" + token + "/1000");
		
		XavanteRequest xeq = XavanteRequestFactory.build(req, channel);
		CometMessage   msg = factory.build(xeq, resp);
		assertEquals("zzz", msg.getCommand());
		assertEquals(token, msg.getToken());
		assertEquals("1000", msg.getSequence());
		assertEquals(null, msg.getData());
	}

	@Test
	public void testPayloadEncodingWithUTF8()
		throws Exception
	{
		HttpRequest  req     = mock(HttpRequest.class);
		HttpResponse resp    = mock(HttpResponse.class);
		Channel      channel = mock(Channel.class);
		
		String token = RandomStringUtils.randomAlphanumeric(MessageFactoryImpl.ID_LEN);
		when(req.getUri()).thenReturn("/aaa/" + token + "/1");
		
		String charset = "utf-8";
		when(req.getHeader(HttpHeaders.Names.CONTENT_TYPE)).thenReturn("application/x-www-form-urlencoded; charset=" + charset);
		
		String payload = "Léandro Çruz";
		String encoded = URLEncoder.encode(payload, charset);
		byte[] bytes   = encoded.getBytes(charset);
		when(req.getContent()).thenReturn(ChannelBuffers.wrappedBuffer(bytes));
		
		XavanteRequest xeq = XavanteRequestFactory.build(req, channel);
		CometMessage msg = factory.build(xeq, resp);
		assertEquals("aaa", msg.getCommand());
		assertEquals(token, msg.getToken());
		assertEquals("1", msg.getSequence());
		assertEquals(payload, msg.getData());
	}

}
