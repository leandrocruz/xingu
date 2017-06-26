package xingu.http.client.impl;

import xingu.http.client.HttpResponse;

public class ExceptionWithResponse
	extends Exception
{

	private HttpResponse response;

	public ExceptionWithResponse(Exception ex, HttpResponse c)
	{
		super(ex.getMessage(), ex);
		this.response = c;
	}
	
	public HttpResponse getHttpResponse()
	{
		return response;
	}
}
