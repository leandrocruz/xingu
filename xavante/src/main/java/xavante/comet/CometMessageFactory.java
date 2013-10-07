package xavante.comet;

import java.net.SocketAddress;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xingu.netty.http.HttpUtils;


public class CometMessageFactory
{
	public static final int ID_LEN = 64;
	
	public static final int CMD_LEN = 3;

	public static CometMessage build(HttpRequest req, HttpResponse resp, Channel channel)
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
		start = end + 1;
		end   = start + CMD_LEN;
		msg.setCommand(path.substring(start, end));

		if(end == len)
		{
			return msg;
		}
		
		//hash - client id
		start = end + 1;
		end   = start + ID_LEN;
		msg.setToken(path.substring(start, end));
		
		//counter
		start     = end + 1;
		int   idx = path.indexOf("?");
		if(start < len)
		{
			end = idx >= 0 ? idx : len;
			String seq = path.substring(start, end);
			msg.setSequence(seq);
		}

		start = end + 1;
		if(idx >= 0)
		{
			String src = path.substring(start); 
			Map<String, String[]> qs = HttpUtils.parseQueryString(src);
			String data = qs.get("data")[0];
			msg.setData(data);
		}
		
		return msg;
	}
}