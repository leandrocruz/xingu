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

		adjustStateCookies(res);

		try
		{
			return state.handle(req, res);
		}
		catch(Throwable t)
		{
			throw new HttpException("Error handling exec()", t);
		}
	}

	private void adjustStateCookies(HttpResponse res)
	{
		Cookies cookies    = state.getCookies();
		Cookies newCookies = CookieUtils.getCookies(res);
		if(cookies == null)
		{
			cookies            = newCookies;
		}
		else
		{
			cookies            = CookieUtils.mergeCookies(cookies, newCookies);
		}
		state.setCookies(cookies);		
	}

	@Override
	public HttpResponse execAndRetry(int attempts)
		throws HttpException
	{
		return execAndRetry(attempts, 1);
	}
	
	private HttpResponse execAndRetry(int attempts, int i)
		throws HttpException
	{
		try
		{
			return exec();
		}
		catch(Exception ex)
		{
			System.err.println(this.req.getUri() + " failed, " + i + "/" + attempts + " - " + ex.getMessage());
			
			if(ex.getCause() instanceof ExceptionWithResponse)
			{
				HttpResponse res = ((ExceptionWithResponse)ex.getCause()).getHttpResponse();
			}
			
			try
			{
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(i >= attempts)
			{
				throw ex;
			}
			return execAndRetry(attempts, ++i);
		}
	}
}