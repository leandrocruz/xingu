package xavante.dispatcher.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xavante.comet.CometHandler;
import xavante.comet.CometMessage;
import xavante.comet.MessageFactory;
import xavante.dispatcher.impl.RequestHandlerSupport;
import xingu.netty.http.HttpResponseBuilder;
import br.com.ibnetwork.xingu.container.Inject;

public class LongPollingCometHandler
	extends RequestHandlerSupport
{
	@Inject
	private CometHandler	handler;
	
	@Inject
	private MessageFactory factory;

	@Override
	public void handle(HttpRequest req, Channel channel)
		throws Exception
	{
		/*
		 * See: fortius.comet.CometServlet
		 */
		HttpResponse resp = HttpResponseBuilder
				.builder()
				.withHeader("Access-Control-Allow-Origin", "*")
				.build();
		
		String reply = null;
		try
		{
			CometMessage msg = factory.build(req, resp, channel);
			reply            = handler.onMessage(msg);
		}
		catch(Throwable t)
		{
			reply = handler.onError(t);
		}
		finally
		{
			if(reply != null)
			{
				byte[]        bytes  = reply.getBytes();
				ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
				resp.setHeader(HttpHeaders.Names.CONTENT_LENGTH, bytes.length);
				resp.addHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json");
				resp.setContent(buffer);
			}
		}
		
		channel.write(resp).addListener(ChannelFutureListener.CLOSE);
	}
}
