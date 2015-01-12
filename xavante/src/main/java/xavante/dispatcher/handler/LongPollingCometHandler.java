package xavante.dispatcher.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xavante.XavanteRequest;
import xavante.comet.CometHandler;
import xavante.comet.CometMessage;
import xavante.comet.MessageFactory;
import xavante.dispatcher.impl.RequestHandlerSupport;
import xingu.container.Inject;
import xingu.netty.http.HttpResponseBuilder;

public class LongPollingCometHandler
	extends RequestHandlerSupport
{
	@Inject
	private CometHandler	handler;
	
	@Inject
	private MessageFactory factory;

	public LongPollingCometHandler(String path)
	{
		super(path);
	}

	@Override
	public void handle(XavanteRequest xeq)
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
			CometMessage msg = factory.build(xeq, resp);
			reply            = handler.onMessage(msg);
		}
		catch(InterruptedException e)
		{
			throw e;
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
		
		xeq.write(resp).addListener(ChannelFutureListener.CLOSE);
	}
}
