package xingu.http.client.impl;

import xingu.http.client.CookieUtils;
import xingu.http.client.Cookies;
import xingu.http.client.HttpContext;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.HttpStateHandler;

public class StatefullRequest
	extends RequestDelegator
{
	private HttpStateHandler	state;

	public StatefullRequest(HttpRequest req, HttpStateHandler state)
	{
		super(req);
		this.state = state;
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		Cookies cookies = state.getCookies();
		if(cookies != null)
		{
			req.withCookies(cookies);
		}

		HttpContext ctx = state.getContext();
		req.context(ctx);
	
		HttpResponse res = req.exec();

		Cookies newCookies = CookieUtils.getCookies(res);
		if(cookies == null)
		{
			cookies = newCookies;
		}
		else
		{
			cookies = CookieUtils.mergeCookies(cookies, newCookies);
		}
		state.setCookies(cookies);

		try
		{
			return state.handle(req, res);
		}
		catch(Throwable t)
		{
			throw new HttpException("Error handling exec()", t);
		}
	}
}