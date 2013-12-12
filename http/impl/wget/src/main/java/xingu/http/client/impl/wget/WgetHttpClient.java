package xingu.http.client.impl.wget;

import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class WgetHttpClient
	implements HttpClient
{
	@Inject
	private Factory factory;
	
	@Override
	public HttpRequest get(String uri)
		throws HttpException
	{
		return factory.create(WgetRequest.class, uri);
	}

	@Override
	public HttpRequest post(String uri)
		throws HttpException
	{
		throw new NotImplementedYet();
	}

	@Override
	public Cookies getCookies(String uri)
		throws HttpException
	{
		throw new NotImplementedYet();
	}

}
