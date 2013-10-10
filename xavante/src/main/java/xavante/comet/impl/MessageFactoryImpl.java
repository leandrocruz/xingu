package xavante.comet.impl;

import java.net.SocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xavante.comet.CometMessage;
import xavante.comet.MessageFactory;
import xingu.netty.http.HttpUtils;


public class MessageFactoryImpl
	implements MessageFactory
{
	public static final int ID_LEN = 64;
	
	public static final int CMD_LEN = 3;

	@Override
	public CometMessage build(HttpRequest req, HttpResponse resp, Channel channel)
		throws Exception
	{
		String        path          = req.getUri();
		int           len           = path.length();
		SocketAddress remoteAddress = channel.getRemoteAddress();
		SocketAddress localAddress  = channel.getLocalAddress();

		CometMessageImpl msg  = new CometMessageImpl();
		msg.setRequest(req);
		msg.setResponse(resp);
		msg.setRemoteAddress(remoteAddress);
		msg.setLocalAddress(localAddress);
		int start, end = 0;
		
		//command
		start      = end + 1;
		end        = start + CMD_LEN;
		String cmd = path.substring(start, end);
		msg.setCommand(cmd);

		if(end == len)
		{
			return msg;
		}
		
		//token
		start        = end + 1;
		end          = start + ID_LEN;
		String token = path.substring(start, end);
		msg.setToken(token);
		
		//counter
		start     = end + 1;
		int   idx = path.indexOf("?");
		if(start < len)
		{
			end        = idx >= 0 ? idx : len;
			String seq = path.substring(start, end);
			msg.setSequence(seq);
		}

		start = end + 1;
		if(idx >= 0)
		{
			String      src          = path.substring(start);
			Map<String, String[]> qs = HttpUtils.parseQueryString(src);
			String      data         = qs.get("data")[0];
			msg.setData(data);
		}
		
		ChannelBuffer buffer        = req.getContent();
		if(buffer != null)
		{
			int readable = buffer.readableBytes();
			if(readable > 0)
			{
				String  cType        = req.getHeader(HttpHeaders.Names.CONTENT_TYPE);
				String  csName       = HttpUtils.charset(cType, HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
				Charset charset      = Charset.forName(csName);
				String  data         = buffer.toString(charset);
				boolean isUrlEncoded = HttpUtils.isUrlEncoded(cType);
				if(isUrlEncoded)
				{
					data = URLDecoder.decode(data, csName);
				}
				msg.setData(data);
			}
		}
		
		return msg;
	}

	@Override
	public int getMessageIdLength()
	{
		return ID_LEN;
	}
}