package xavante;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xingu.url.Url;
import xingu.url.UrlParser;

public class XavanteRequestFactory
{
	public static XavanteRequest build(HttpRequest req, Channel channel)
	{
		return build(req, channel, null);
	}

	public static XavanteRequest build(HttpRequest req, Channel channel, Url url)
	{
		return new XavanteRequestImpl(req, url, channel);
	}
}

class XavanteRequestImpl
	implements XavanteRequest
{

	private HttpRequest	req;

	private Url			url;

	private Channel		channel;

	public XavanteRequestImpl(HttpRequest req, Url url, Channel channel)
	{
		this.req = req;
		this.url = url;
		this.channel = channel;
	}

	@Override
	public Channel getChannel()
	{
		return channel;
	}

	@Override
	public HttpRequest getRequest()
	{
		return req;
	}

	@Override
	public Url getUrl()
	{
		if(url == null)
		{
			String uri = req.getUri();
			url = UrlParser.parse(uri);
		}
		return url;
	}

	@Override
	public ChannelFuture write(HttpResponse response)
		throws Exception
	{
		return channel.write(response);
	}

	@Override
	public String getPath()
	{
		return getUrl().getPath();
	}
}
