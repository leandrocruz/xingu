package xingu.http.client.impl.unirest;

import xingu.http.client.CookieUtils;
import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class UnirestHttpClient
	implements HttpClient
{
	@Override
	public HttpRequest get(String uri)
	{
		GetRequest req = Unirest.get(uri);
		return new UnirestGetRequest(req);
	}

	@Override
	public HttpRequest post(String uri)
	{
		HttpRequestWithBody req = Unirest.post(uri);
		return new UnirestPostRequest(req);
	}

	@Override
	public Cookies getCookies(String uri)
	{
		xingu.http.client.HttpResponse<?> res = get(uri)
				.header("Accept", "text/html")
				.header("Accept-Charset", "ISO-8859-1,utf-8")
				.asString();
		
		
		return CookieUtils.getCookies(res);
	}

}